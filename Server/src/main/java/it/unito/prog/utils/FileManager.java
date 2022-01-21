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

    private static final String basePath = cwd + File.separator + "users" + File.separator;

    // PUBLIC METHODS

    /**
     * Sends an email.
     * @param email         email to be sent
     * @return              code 0 Feedback on success, code -1 Feedback on failure
     */
    public static Feedback sendEmail(Email email) {
         String sender = parseEmailAddress(email.getSender());
         String[] receivers = parseReceivers(email.getReceivers());
         Feedback f = new Feedback(0, "Message sent successfully.", email);

         //check if sender exists
         if(notExistsUserDir(sender))
             return new Feedback(-1, "Invalid sender " + sender + ".");

        //check if each receiver exists
         for (String receiver : receivers) {
             if(receiver.equals(email.getSender()))
                 return new Feedback(-1, "Cannot send email to yourself.");
             if(notExistsUserDir(parseEmailAddress(receiver)))
                 return new Feedback(-1, "Invalid receiver " + receiver + ".");
         }

         FileChannel channel = null;
         FileLock lock = null;
         try {
             //write email in sender's Sent directory
             channel = FileChannel.open(Paths.get(basePath + sender + File.separator + "Sent" + File.separator + "lock"), StandardOpenOption.WRITE);
             lock = channel.lock();

             email.setId(generateUUID());

             writeEmailOnFile(email, new File(basePath + sender + File.separator + "Sent" + File.separator + email.getId() + ".txt"));

             if (lock != null) lock.release();
             channel.close();

             //write email in receivers' Incoming directory
             for (String receiver : receivers) {
                 String username = parseEmailAddress(receiver);

                 channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.WRITE);
                 lock = channel.lock();

                 writeEmailOnFile(email, new File(basePath + username + File.separator + "Incoming" + File.separator + email.getId() + ".txt"));
             }

         } catch (IOException e) {
             f.setAll(-1, "Error occurred while sending the email.");

         } finally {
             try {
                 if (lock != null) lock.release();
                 if (channel != null) channel.close(); //also releases the lock
             } catch (IOException ignore) {}
         }

         return f;
    }

    /**
     * Deletes an email.
     * @param email         email to be deleted
     * @param emailAddress  user for whom the email has to be deleted
     * @return              code 0 Feedback on success, code -1 Feedback on failure
     */
    public static Feedback deleteEmail(Email email, String emailAddress) {
        String username = parseEmailAddress(emailAddress);
        String dir = email.getSender().equals(emailAddress) ? "Sent" : "Inbox";
        Feedback f = new Feedback(0, "File deleted successfully.");

        //check if user exists
        if(notExistsUserDir(username))
            return new Feedback(-1, "Invalid user " + username + ".");

        FileChannel channel = null;
        FileLock lock = null;
        try {
            if (Files.exists(Paths.get(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt"))) {
                channel = FileChannel.open(Paths.get(basePath + username + File.separator + dir + File.separator + "lock"), StandardOpenOption.READ);
                lock = channel.lock(0, Long.MAX_VALUE, true);

                File forDeletion = new File(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt");

                if(!forDeletion.delete())
                    f.setAll(-1, "Error occurred while deleting the email.");
            } else {
                f.setAll(-1, "Email not found for deletion.");
            }
        } catch (IOException e) {
            f.setAll(-1, "Error occurred while deleting the email.");
        } finally {
            try {
                if (lock != null) lock.release();
                if (channel != null) channel.close();
            } catch (IOException ignore) {}
        }

        return f;
    }

    /**
     * Retrieves the list of email in the specified directory.
     * @param emailAddress user whose emails have to be retrieved
     * @param dir          directory in which the emails to be retrieved are located
     * @return             code 0 Feedback on success, code -1 Feedback on failure
     */
    public static Feedback getEmailList (String emailAddress, String dir) {
        String username = parseEmailAddress(emailAddress);
        File directory = new File(basePath + username + File.separator + dir);
        String[] directoryList = directory.list();
        List<Email> retrievedEmails = new ArrayList<>();
        Feedback f = new Feedback(0, "Success", retrievedEmails);

        //check if user exists
        if(notExistsUserDir(username))
            return new Feedback(-1, "Invalid user " + username + ".");

        FileLock lock = null;
        try (FileChannel channel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.READ)) {
            lock = channel.lock(0, Long.MAX_VALUE, true);

            //get emails from the specified directory
            if (directoryList != null && directoryList.length > 0) {
                for (String email : directoryList) {
                    if (!email.equals("lock")) {
                        retrievedEmails.add(readEmailFromFile(new Email(), new File(basePath + username + File.separator + dir + File.separator + email)));
                    }
                }
            }

        } catch (IOException e) {
            f.setAll(-1, "Error occurred while retrieving email list.");

        } finally {
            try {
                if (lock != null) lock.release();
            } catch (IOException ignore) {}

        }

        return f;
    }

    /**
     * Returns a list of incoming emails and moves said emails from the user's Incoming directory to the Inbox directory.
     * @param emailAddress user's email address
     * @return             code 0 Feedback and list of incoming emails on success, code -1 Feedback otherwise
     */
    @SuppressWarnings("unchecked")
    public static Feedback updateInbox (String emailAddress){
        String username = parseEmailAddress(emailAddress);
        List<Email> newEmails = (List<Email>) getEmailList(emailAddress, "Incoming").getResult();
        Feedback f = new Feedback (0, "Inbox updated successfully.", newEmails);

        //check if user exists
        if(notExistsUserDir(username))
            return new Feedback(-1, "Invalid user " + username + ".");

        //move email from Incoming directory to Inbox directory
        if (newEmails != null && newEmails.size() > 0) {
            if (!moveNewEmails(username))
                f.setAll(-1, "Error occurred while updating inbox.");
        }

        if (newEmails == null || newEmails.size() <= 0) f.setAll(-1, "No updates available.");

        return f;
    }

    // AUXILIARY METHODS

    /**
     * Checks if a user's directory already exists.
     * @param username user's username
     * @return         true if the user's directory exists, false otherwise
     */
    private static boolean notExistsUserDir(String username) {
        Path path = Paths.get(basePath + File.separator + username);

        return !Files.exists(path);
    }

    /**
     * Given a user, moves said user's incoming emails from the incoming directory to the inbox directory.
     * @param username user's username
     * @return         true if the move has been successful, false otherwise
     */
    private static boolean moveNewEmails(String username) {
        File incomingDir = new File(basePath + username + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        boolean ret = true;

        FileChannel inboxChannel = null;
        FileLock inboxLock = null;
        FileChannel incomingChannel = null;
        FileLock incomingLock = null;
        try {
            inboxChannel = FileChannel.open(Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock"), StandardOpenOption.WRITE);
            inboxLock = inboxChannel.lock();
            incomingChannel = FileChannel.open(Paths.get(basePath + username + File.separator + "Incoming" + File.separator + "lock"), StandardOpenOption.WRITE);
            incomingLock = incomingChannel.lock();

            //try moving each and every file which isn't the lock file
            if (incomingEmails != null && incomingEmails.length > 0) {
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

        } catch (IOException e) {
            ret = false;

        } finally {
            try {
                if (inboxLock != null) inboxLock.release();
                if (incomingLock != null) incomingLock.release();
                if (inboxChannel != null) inboxChannel.close();
                if (incomingChannel != null) incomingChannel.close();
            } catch (IOException ignore) {}
        }

        return ret;
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
        email.writeExternal(objectOutputStream);

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

    /*
    This method's supposed to be used when the user registers to the service and the persistent directory gets set up.
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
    } */
}
