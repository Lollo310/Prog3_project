package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static it.unito.prog.utils.Utils.*;

public class FileManager {

    private static final String cwd = Path.of("").toAbsolutePath().toString();

    private static final String parent = Paths.get(cwd).getParent().toAbsolutePath().toString();

    private static final String basePath = parent + File.separator + "users" + File.separator;

    // PUBLIC METHODS

    /**
     * Sends an email.
     * @param email         email to be sent
     * @return              code 0 Feedback on success, code -1 Feedback on failure
     */
    public static Feedback sendEmail(Email email) throws IOException {
         String sender = parseEmailAddress(email.getSender());
         String[] receivers = parseReceivers(email.getReceivers());

         //check if sender exists, if not try creating a directory for the sender
         if(!existsUserDir(sender)) {
             initUserDir(sender);
         }

        //check if there's at least one receiver
         if(receivers.length == 0) return new Feedback(-1, "No receivers specified.");

        //check if each receiver exists, if not try creating a directory for them
         for (String receiver : receivers) {
             if(receiver.equals(sender)) return new Feedback(-1, "Cannot send mail to yourself.");
             if(!existsUserDir(receiver)) initUserDir(receiver);
         }


             //lock sender's dir
             FileChannel channel = FileChannel.open(Paths.get(basePath + sender + File.separator + "Sent" + File.separator + "lock"), StandardOpenOption.WRITE);
             FileLock lock = channel.lock();

             //write email to file in sender's Sent dir
             writeEmailOnFile(email, new File (basePath + sender + File.separator + "Sent" + File.separator + email.getId() + ".txt"));
             lock.release();

             for (String receiver : receivers) {
                 //lock receiver's dir
                 String username = parseEmailAddress(receiver);
                 channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.WRITE);
                 channel.lock();

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
            initUserDir(username);
        }

        if (Files.exists(Paths.get(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt"))) {
            //lock dir
            FileChannel channel = FileChannel.open(Paths.get(basePath + username + File.separator + dir + File.separator + "lock"), StandardOpenOption.READ);
            FileLock lock = channel.lock(0, Long.MAX_VALUE, true);


                File forDeletion = new File(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt");

                if(!forDeletion.delete()) {
                    f = new Feedback(-1, "Failed to delete file.");
                }

                if(lock != null) lock.release();
                channel.close(); //also releases the lock
        }
        return f;
    }

    public static Feedback getEmailList (String emailAddress, String dir) throws IOException {
        String username = parseEmailAddress(emailAddress);
        File directory = new File(basePath + username + File.separator + dir);
        String[] directoryList = directory.list();
        List<Email> retrievedEmails = new ArrayList<>();

        //acquire lock on the specified directory
        FileChannel channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.READ);
        FileLock lock = channel.lock(0, Long.MAX_VALUE, true);

        //get emails from the directory
        if (directoryList != null) {
            for (String email : directoryList) {
                if (!email.equals("lock")) {
                    //try catch
                    retrievedEmails.add(readEmailFromFile(new Email(), new File(basePath + username + File.separator + dir + File.separator + email)));
                }
            }
        }

        if(lock != null) lock.release();

        //return the emails list
        return new Feedback(0, "Success", retrievedEmails);
    }

    /**
     * Returns a list of incoming emails and moves said emails from the user's Incoming directory to the Inbox directory.
     * @param emailAddress user's email address
     * @return             list of incoming emails on success, null otherwise
     */
    @SuppressWarnings("unchecked")
    public static Feedback updateInbox (String emailAddress) throws IOException {
        String username = parseEmailAddress(emailAddress);
        List<Email> newEmails = (List<Email>) getEmailList(emailAddress, "Incoming").getResult();

        //check if user exists
        if(!existsUserDir(username)) {
            initUserDir(username);
        }

        //move email from Incoming directory to Inbox directory
        if (newEmails != null && newEmails.size() > 0) {
            moveNewEmails(username); //try catch
        }

        return new Feedback (0, "Inbox updated successfully.");
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
     */
    private static void initUserDir(String username) throws IOException {
        Path user = Paths.get(basePath + username);
        Path inbox = Paths.get(basePath + username + File.separator + "Inbox");
        Path sent = Paths.get(basePath + username + File.separator + "Sent");
        Path incoming = Paths.get(basePath + username + File.separator + "Incoming");
        Path inbox_lock = Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock");
        Path sent_lock = Paths.get(basePath + username + File.separator + "Sent" + File.separator + "lock");
        Path incoming_lock = Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock");

            Files.createDirectories(user);
            Files.createDirectories(inbox);
            Files.createDirectories(sent);
            Files.createDirectories(incoming);
            Files.createFile(inbox_lock);
            Files.createFile(sent_lock);
            Files.createFile(incoming_lock);
    }

    /**
     * Given a user, moves said user's incoming emails from the incoming directory to the inbox directory.
     * @param username user's username
     */
    private static void moveNewEmails(String username) throws IOException {
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        List<Email> movedEmails = new ArrayList<>();

        //if there are no emails to be moved
        //f (incomingEmails.length == 0) return true;

        //else, if there are emails to be moved
        //lock Inbox and Incoming directories for writing
        FileChannel inboxChannel = FileChannel.open(Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock"), StandardOpenOption.WRITE);
        FileLock inboxLock = inboxChannel.lock(0, Long.MAX_VALUE, true);
        FileChannel incomingChannel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.WRITE);
        FileLock incomingLock = incomingChannel.lock(0, Long.MAX_VALUE, true);

        //try moving each and every file which isn't the lock file
        if (incomingEmails != null) {
            for (String incomingEmail : incomingEmails) {
                if (!incomingEmail.equals("lock")) {
                    Files.move(
                            Paths.get(basePath + username + File.separator + "Incoming" + File.separator + incomingEmail), //from
                            Paths.get(basePath + username + File.separator + "Inbox" + File.separator + incomingEmail), //to
                            java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }

        if(inboxLock != null) inboxLock.release();
        //Te l'ho commentato perché mi dava errore
        if(incomingLock != null) incomingLock.release();
    }


    /**
     * Given an email and a file, writes said email on said file.
     * @param email         email to write
     * @param f             file to be written
     * @throws IOException  on failure
     */
    private static void writeEmailOnFile(Email email, File f)  throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(f);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.flush();
        objectOutputStream.close();
        fileOutputStream.close();
    }

    /**
     * Given an email and a file, reads said email from said file.
     * @param email email object
     * @param f     file to be read
     * @return      read email on success, null on failure
     */
    private static Email readEmailFromFile(Email email, File f) throws IOException {

        FileInputStream fileInputStream = new FileInputStream(f);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        email.readExternal(objectInputStream);

        objectInputStream.close();
        fileInputStream.close();

        return email;
    }
}
