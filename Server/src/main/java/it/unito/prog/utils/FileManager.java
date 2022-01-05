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

public class FileManager {

    private static final String cwd = Path.of("").toAbsolutePath().toString();

    private static final String parent = Paths.get(cwd).getParent().toAbsolutePath().toString();

    private static final String basePath = parent + File.separator + "users" + File.separator;

    /**
     * Checks if a user's directory already exists.
     * @param username username
     * @return true if the user's directory exists, false otherwise.
     */
    public static boolean existsUserDir(String username) {
        Path path = Paths.get(basePath + File.separator + username);
        return Files.exists(path);
    }

    /**
     * Creates a user directory containing an inbox and a sent directories.
     * @param username username
     */
    public static boolean initUserDir(String username) {
        boolean ret = false;

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
            System.err.println("Failed to initialize user's directory - " + e);
            ret = false;
        }
        return ret;
    }

    //https://stackoverflow.com/questions/106770/standard-concise-way-to-copy-a-file-in-java/16600787#16600787
    public static List<Email> getNewEmails (String emailAddress) {
        String username = parseEmailAddress(emailAddress);
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        List<Email> newEmails = new ArrayList<>(incomingEmails.length - 1);

        //lock incoming dir [work in progress]

        //get Email list and move emails to Inbox dir
        for (String email : incomingEmails) {
            if(!email.equals("lock")) {
                newEmails.add(readEmailFromFile(new Email(), new File(basePath + username + File.separator + "Incoming" + File.separator + email)));
            }
        }

        if(!moveNewEmails(emailAddress)) newEmails = null;

        return newEmails;
    }

    public static boolean moveNewEmails(String emailAddress) {
        String username = parseEmailAddress(emailAddress);
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        int counter = 0; //per sapere dove si interrompe la move eventualmente
        boolean ret = true;

        //GESTIRE LOCK [work in progress]

        for (String email : incomingEmails) {
            try {
                if(!email.equals("lock")) {
                    counter++;
                    java.nio.file.Files.move(
                            Paths.get(basePath + username + File.separator + "Incoming" + File.separator + email), //from
                            Paths.get(basePath + username + File.separator + "Inbox" + File.separator + email), //to
                            java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    ret = ret && true;
                }
            } catch(IOException e) {
                //return new Feedback(-1, "Couldn't retrieve new emails.");
                ret = false;
            }
        }

        //handle MOVE unsuccessful, rollback [WORK IN PROGRESS]
        if(!ret) {

        }

        return ret;
    }

    public static boolean writeEmailOnFile(Email email, File f) throws IOException {
        boolean ret = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(f);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            email.writeExternal(objectOutputStream);

            objectOutputStream.flush();
            objectOutputStream.close();
            fileOutputStream.close();

            ret = true;
        } catch (Exception e) {
            ret = false;
        }
        return ret;
    }

    public static Email readEmailFromFile(Email email, File f) {
        boolean ret = false;
        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            email.readExternal(objectInputStream);

            objectInputStream.close();
            fileInputStream.close();

            ret = true;
        } catch (Exception e) {
            ret = false;
        }
        return email;
    }

    //https://stackoverflow.com/questions/8066130/should-i-close-the-filechannel
    public static Feedback sendEmail(Email email) throws IOException {
         String sender = parseEmailAddress(email.getSender());
         String[] receivers = parseReceivers(email.getReceivers());

         if(!existsUserDir(sender)) {
             if(!initUserDir(sender)) {
                 return new Feedback(-1, "Failed to access user's data.");
             }
             return new Feedback(-1, "Invalid sender.");
         }
         if(receivers.length == 0) return new Feedback(-1, "No receivers specified."); //handle no receivers specified

         for (String receiver : receivers) {
             if(!existsUserDir(receiver)) return new Feedback(-1, "Receiver " + receiver + " does not exist."); // dà errore o prova a creare a la directory?
         }

         //lock sender's dir
         FileChannel channel = FileChannel.open(Paths.get(basePath + sender + File.separator + "Sent" + File.separator + "lock"), StandardOpenOption.READ);
         FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
         //https://mkyong.com/java/how-to-read-and-write-java-object-to-a-file/
         //write email to file in sender's Sent dir
         FileOutputStream fileStream = new FileOutputStream(new File (basePath + sender + File.separator + "Sent" + File.separator + email.getId() + ".txt"));
         ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
         objectStream.writeObject(email);
         objectStream.close();
         lock.release();

         for (String receiver : receivers) {
             //lock receivers' dirs
             String username = parseEmailAddress(receiver);
             channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock"), StandardOpenOption.READ);
             channel.lock(0, Long.MAX_VALUE, true);

             //write email to file in receivers' Inbox dir
             fileStream = new FileOutputStream(new File (basePath + username + File.separator + "Incoming" + File.separator + email.getId() + ".txt"));
             objectStream = new ObjectOutputStream(fileStream);
             objectStream.writeObject(email);
             objectStream.close();
             lock.release();
         }
         return new Feedback(0, "Message sent successfully.");
    }

    public static void readEmail(List<Email> dir) {

    }

    /*
     * https://www.baeldung.com/java-lock-files
     * https://stackoverflow.com/questions/128038/how-can-i-lock-a-file-using-java-if-possible
     * https://www.tabnine.com/code/java/classes/java.nio.channels.FileLock
     * https://stackoverflow.com/questions/8678384/java-locking-a-file-for-exclusive-access
     * https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-nio-2/src/main/java/com/baeldung/lock/FileLocks.java
     */
    public static Feedback deleteEmail(Email email, String emailAddress) throws IOException {
        String username = parseEmailAddress(emailAddress);
        String dir = email.getSender().equals(username) == true? "Sent" : "Inbox";
        Feedback f = new Feedback(0, "File deleted successfully.");

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
}
