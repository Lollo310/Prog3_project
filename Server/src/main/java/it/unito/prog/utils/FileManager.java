package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static it.unito.prog.utils.Utils.*;

/**
 * NOTE
 * - GESTIRE LOCK OVUNQUE
 * - controllo di non aver inviato a me stesso una mail
 *
 * - deleteEmail
 *     //decide WHERE TO DELETE THE EMAIL FROM
 *
 * - getEmail List
 *     // forse conviene creare un wrapper che include getList + moveEmails che restituisca un feedback
 *     // però poi come restituire la lista con il feedback? maybe aggiungere un extra args al feedback?
 *     // ordinamento nel client
 *
 * - moveNewEmails
 *      // aggiungere metodo moveEmail per fattorizzare il codice?
 *      // ordinare mail
 *
 * - readEmailFromFile
 *      // handle errors
 *
 */

public class FileManager {

    private static final String cwd = Path.of("").toAbsolutePath().toString();

    private static final String parent = Paths.get(cwd).getParent().toAbsolutePath().toString();

    private static final String basePath = parent + File.separator + "users" + File.separator;

    // PUBLIC METHODS

    /**
     * Sends an email.
     * @param email email to be sent
     * @return      code 0 Feedback on success, code -1 Feedback on failure
     * @throws IOException on channel/lock failures
     */
    public static Feedback sendEmail(Email email) throws IOException {
         String sender = parseEmailAddress(email.getSender());
         String[] receivers = parseReceivers(email.getReceivers());

         //check if sender exists, if not try creating a directory for the sender
         if(!existsUserDir(sender)) {
             if(!initUserDir(sender)) {
                 return new Feedback(-1, "Sender " + sender + " does not exist.");
             }
             return new Feedback(-1, "Invalid sender: " + sender);
         }

        //check if there's at least one receiver
         if(receivers.length == 0) return new Feedback(-1, "No receivers specified.");

        //check if each receiver exists, if not try creating a directory for them
         for (String receiver : receivers) {
             if(!existsUserDir(receiver))
                 if(!initUserDir(receiver)) {
                     return new Feedback(-1, "Receiver " + receiver + " does not exist.");
                 }
             return new Feedback(-1, "Invalid receiver: " + receiver);
         }

         //lock sender's dir
         FileChannel channel = FileChannel.open(Paths.get(basePath + sender + File.separator + "Sent" + File.separator + "lock"), StandardOpenOption.READ);
         FileLock lock = channel.lock(0, Long.MAX_VALUE, true);

         //write email to file in sender's Sent dir
         writeEmailOnFile(email, new File (basePath + sender + File.separator + "Sent" + File.separator + email.getId() + ".txt"));
         lock.release();

         for (String receiver : receivers) {
             //lock receiver's dir
             String username = parseEmailAddress(receiver);
             channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.READ);
             channel.lock(0, Long.MAX_VALUE, true);

             //write email to file in receivers' Incoming dir
             writeEmailOnFile(email, new File (basePath + username + File.separator + "Incoming" + File.separator + email.getId() + ".txt"));
             lock.release();
         }
         return new Feedback(0, "Message sent successfully.");
    }

    /**
     * Deletes an email.
     * @param email         to be deleted
     * @param emailAddress  user for whom the email has to be deleted
     * @return              code 0 Feedback on success, code -1 Feedback on failure
     * @throws IOException  on channel/lock failures
     */
    public static Feedback deleteEmail(Email email, String emailAddress) throws IOException {
        String username = parseEmailAddress(emailAddress);
        String dir = email.getSender().equals(username) ? "Sent" : "Inbox";
        Feedback f = new Feedback(0, "File deleted successfully.");

        //check if user exists, if not try creating a directory for said user
        if(!existsUserDir(username)) {
            if(!initUserDir(username)) {
                return new Feedback(-1, "Failed to access user's data.");
            }
            return new Feedback(-1, "Invalid sender.");
        }

        if (Files.exists(Paths.get(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt"))) {
            FileChannel channel = FileChannel.open(Paths.get(basePath + username + File.separator + dir + File.separator + "lock"), StandardOpenOption.READ);
            FileLock lock = channel.lock(0, Long.MAX_VALUE, true);

            try {
                File forDeletion = new File(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt");
                if(!forDeletion.delete()) {
                    System.err.println("Failed to delete file.");
                    f = new Feedback(-1, "Failed to delete file.");
                }
            } catch(OverlappingFileLockException e) {
                System.err.println("File is already locked in this thread or virtual machine - " + e);
                f = new Feedback(-1, "Failed to complete operation.");
            } finally {
                if(lock != null) lock.release();
                channel.close(); //also releases the lock
            }
        } else {
            f = new Feedback(-1, "File not found.");
        }
        return f;
    }

    /**
     * Returns a list of incoming emails and moves said emails from the user's Incoming directory to the Inbox directory.
     * @param emailAddress user's email address
     * @return             list of incoming emails on success, null otherwise
     */
    public static List<Email> getNewEmailsList (String emailAddress) {
        String username = parseEmailAddress(emailAddress);
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        List<Email> newEmails = null;

        //lock incoming dir [work in progress]

        //if there are no emails
        if (incomingEmails.length > 0) {
            newEmails = new ArrayList<>(incomingEmails.length - 1);

            //get incoming emails
            for (String email : incomingEmails) {
                if(!email.equals("lock")) {
                    newEmails.add(readEmailFromFile(new Email(), new File(basePath + username + File.separator + "Incoming" + File.separator + email)));
                }
            }

            //move emails from Incoming directory to Inbox directory
            if(!moveNewEmails(username)) newEmails = null;
        }
        return newEmails;
    }

    // AUXILIARY METHODS

    /**
     * Checks if a user's directory already exists.
     * @param username user's username
     * @return         true if the user's directory exists, false otherwise
     */
    private static boolean existsUserDir(String username) {
        Path path = Paths.get(basePath + File.separator + username);
        return Files.exists(path);
    }

    /**
     * Creates a user directory containing the following sub-directories: inbox, sent, incoming.
     * @param username user's username
     * @return         true if the directories have been initialized correctly, false otherwise
     */
    private static boolean initUserDir(String username) {
        boolean ret;

        Path user = Paths.get(basePath + username);
        Path inbox = Paths.get(basePath + username + File.separator + "Inbox");
        Path sent = Paths.get(basePath + username + File.separator + "Sent");
        Path incoming = Paths.get(basePath + username + File.separator + "Incoming");
        Path inbox_lock = Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock");
        Path sent_lock = Paths.get(basePath + username + File.separator + "Sent" + File.separator + "lock");
        Path incoming_lock = Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock");

        try {
            Files.createDirectories(user);
            Files.createDirectories(inbox);
            Files.createDirectories(sent);
            Files.createDirectories(incoming);
            Files.createFile(inbox_lock);
            Files.createFile(sent_lock);
            Files.createFile(incoming_lock);
            ret = true;
        } catch (IOException e) {
            //System.err.println("Failed to initialize user's directory - " + e);
            //serve un feedback invece del booleano? poi ci pensiamo
            ret = false;
        }
        return ret;
    }

    /**
     * Given a user, moves said user's incoming emails from the incoming directory to the inbox directory.
     * In case of failure, restores the files that have already been moved.
     * @param username user's username
     * @return         true if the files have been moved correctly, false otherwise
     */
    private static boolean moveNewEmails(String username) {
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        boolean ret = true;
        ArrayList<String> movedEmails = new ArrayList<>();

        //if there are emails to be moved
        if (incomingEmails.length > 0) return true;

        //GESTIRE LOCK [work in progress]

        for (String email : incomingEmails) {
            try {
                if (!email.equals("lock")) {
                    java.nio.file.Files.move(
                            Paths.get(basePath + username + File.separator + "Incoming" + File.separator + email), //from
                            Paths.get(basePath + username + File.separator + "Inbox" + File.separator + email), //to
                            java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    movedEmails.add(email);
                }
            } catch (IOException e) {
                //return new Feedback(-1, "Couldn't retrieve new emails.");
                ret = false;
            }
        }

        //if move is unsuccessful, rollback and restore previous state
        if(!ret) {
            movedEmails = new ArrayList<>();

            //lock inbox

            File inboxDir = new File(basePath + username + File.separator + "Inbox");
            String[] inboxEmails = inboxDir.list();

            for (String email : inboxEmails) {
                try {
                    if(!email.equals("lock")) {
                        java.nio.file.Files.move(
                                Paths.get(basePath + username + File.separator + "Inbox" + File.separator + email), //from
                                Paths.get(basePath + username + File.separator + "Incoming" + File.separator + email), //to
                                java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        movedEmails.add(email);
                    }
                } catch(IOException e) {
                    //return new Feedback(-1, "Couldn't retrieve new emails.");
                }
            }
        }

        return ret;
    }

    /**
     * Given an email and a file, writes said email on said file.
     * @param email email to write
     * @param f     file to be written
     * @return      true on success, false otherwise
     */
    private static boolean writeEmailOnFile(Email email, File f) {
        boolean ret;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            email.writeExternal(objectOutputStream);

            // FORSE DOVREI CHIUDERLI NELLA FINALLY? IDK YET
            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

            ret = true;
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    /**
     * Given an email and a file, reads said email from said file.
     * @param email email object
     * @param f     file to be read
     * @return      read email
     */
    private static Email readEmailFromFile(Email email, File f) {

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            email.readExternal(objectInputStream);

            objectInputStream.close();
            fileInputStream.close();

        } catch (Exception e) {

        }
        return email;
    }
}
