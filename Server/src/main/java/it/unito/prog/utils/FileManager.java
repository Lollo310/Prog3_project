package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static it.unito.prog.utils.Utils.*;

public class FileManager {

    private static final String cwd = Path.of("").toAbsolutePath().toString();

    private static final String basePath = cwd + File.separator + "users" + File.separator;

    private final ConcurrentHashMap<String, ReadWriteLock> lockHashMap;

    public FileManager() {
        lockHashMap = new ConcurrentHashMap<>();
        init();
    }

    /**
     * Initializes the lock hashmap by adding an Inbox lock, Sent lock and Incoming lock for each user.
     */
    private void init() {
        File directory = new File(basePath);
        String[] users = directory.list();

        if (users != null && users.length > 0)
            for (String user : users) {
                lockHashMap.put(basePath + user + File.separator + "Inbox", new ReentrantReadWriteLock());
                lockHashMap.put(basePath + user + File.separator + "Sent", new ReentrantReadWriteLock());
                lockHashMap.put(basePath + user + File.separator + "Incoming", new ReentrantReadWriteLock());
            }
    }

    /**
     * Makes effective the send operation by writing the sent email in the sender's Sent directory and in the receivers'
     * Incoming directory.
     * @param email email to be sent
     * @return      code 0 Feedback on success with the updated email as Feedback result, code -1 Feedback on failure
     */
    public Feedback sendEmail(Email email) {
         String[] receivers = parseReceivers(email.getReceivers());
         String senderPath = getPath(email.getSender(), "Sent");

         //check if sender exists
         if(notExistsUserDir(email.getSender()))
             return new Feedback(-1, "Invalid sender " + email.getSender() + ".");

        //check if each receiver exists
         for (String receiver : receivers) {
             if(receiver.equals(email.getSender()))
                 return new Feedback(-1, "Cannot send email to yourself.");
             if(notExistsUserDir(receiver))
                 return new Feedback(-1, "Invalid receiver " + receiver + ".");
         }

        //write email in sender's Sent directory
        try {
             lockHashMap.get(senderPath).writeLock().lock();
             email.setId(generateUUID());
             writeEmailOnFile(email, new File(senderPath + File.separator + email.getId() + ".txt"));
         } catch (IOException ioException) {
             return new Feedback(-1, "Error occurred while sending the email.");
         } finally {
             lockHashMap.get(senderPath).writeLock().unlock();
         }

        //write email in receivers' Incoming directory
        for (String receiver : receivers) {
            String receiverPath = getPath(receiver, "Incoming");

            try {
                lockHashMap.get(receiverPath).writeLock().lock();
                writeEmailOnFile(email, new File(receiverPath + File.separator + email.getId() + ".txt"));
            } catch (IOException e) {
                return new Feedback(-1, "Error occurred while sending the email.");
            } finally {
                lockHashMap.get(receiverPath).writeLock().unlock();
            }
        }

        return new Feedback(0, "Message sent successfully.", email);
    }

    /**
     * Makes effective the delete operation removing the file associated with the email id.
     * @param email         email to be deleted
     * @param emailAddress  user for whom the email has to be deleted
     * @return              code 0 Feedback on success, code -1 Feedback on failure
     */
    public Feedback deleteEmail(Email email, String emailAddress) {
        String dir = email.getSender().equals(emailAddress) ? "Sent" : "Inbox";
        String userPath = getPath(emailAddress, dir);

        //check if user exists
        if(notExistsUserDir(emailAddress))
            return new Feedback(-1, "Invalid user " + emailAddress + ".");

        try {
            lockHashMap.get(userPath).writeLock().lock();

            return Files.deleteIfExists(Paths.get(userPath + File.separator + email.getId() + ".txt"))
                    ? new Feedback(0, "File deleted successfully.")
                    : new Feedback(-1, "Email not found for deletion.");

        } catch (IOException e) {
            return new Feedback(-1, "Error occurred while deleting the email.");
        } finally {
            lockHashMap.get(userPath).writeLock().unlock();
        }
    }

    /**
     * Retrieves the list of email in the specified directory.
     * @param emailAddress user whose emails have to be retrieved
     * @param dir          directory in which the emails to be retrieved are located
     * @return             code 0 Feedback on success, code -1 Feedback on failure
     */
    public Feedback getEmailList (String emailAddress, String dir) {
        String userPath = getPath(emailAddress, dir);
        File directory = new File(userPath);
        String[] directoryList = directory.list();
        List<Email> retrievedEmails = new ArrayList<>();
        Feedback f = new Feedback(0, "Success", retrievedEmails);

        //check if user exists
        if(notExistsUserDir(emailAddress))
            return new Feedback(-1, "Invalid user " + emailAddress + ".");

        try {
            lockHashMap.get(userPath).readLock().lock();

            //get emails from the specified directory
            if (directoryList != null && directoryList.length > 0)
                for (String email : directoryList)
                    retrievedEmails.add(readEmailFromFile(new Email(), new File(userPath + File.separator + email)));
        } catch (IOException e) {
            f.setAll(-1, "Error occurred while retrieving email list.");
        } finally {
            lockHashMap.get(userPath).readLock().unlock();
        }

        return f;
    }

    /**
     * Returns a list of incoming emails and moves said emails from the user's Incoming directory to the Inbox directory.
     * @param emailAddress user's email address
     * @return             code 0 Feedback and list of incoming emails on success, code -1 Feedback otherwise
     */
    @SuppressWarnings("unchecked")
    public Feedback updateInbox (String emailAddress){
        List<Email> newEmails = (List<Email>) getEmailList(emailAddress, "Incoming").getResult();
        Feedback f = new Feedback (0, "Inbox updated successfully.", newEmails);

        //check if user exists
        if(notExistsUserDir(emailAddress))
            return new Feedback(-1, "Invalid user " + emailAddress + ".");

        //move email from Incoming directory to Inbox directory
        if (newEmails != null && newEmails.size() > 0) {
            if (!moveNewEmails(emailAddress))
                f.setAll(-1, "Error occurred while updating inbox.");
        }

        if (newEmails == null || newEmails.size() <= 0) f.setAll(-1, "No updates available.");

        return f;
    }

    /**
     * Checks if a user's directory already exists.
     * @param emailAddress user's username
     * @return             true if the user's directory exists, false otherwise
     */
    private boolean notExistsUserDir(String emailAddress) {
        Path path = Paths.get(basePath + File.separator + emailAddress);

        return !Files.exists(path);
    }

    /**
     * Given a user, moves said user's incoming emails from the incoming directory to the inbox directory.
     * @param emailAddress user's email address
     * @return             true if the move has been successful, false otherwise
     */
    private boolean moveNewEmails(String emailAddress) {
        File incomingDir = new File(basePath + emailAddress + File.separator + "Incoming");
        String[] incomingEmails = incomingDir.list();
        boolean ret = true;

        try {
            lockHashMap.get(basePath + emailAddress + File.separator + "Inbox").writeLock().lock();
            lockHashMap.get(basePath + emailAddress + File.separator + "Incoming").writeLock().lock();

            if (incomingEmails != null && incomingEmails.length > 0)
                for (String incomingEmail : incomingEmails)
                        Files.move(
                                Paths.get(basePath + emailAddress + File.separator + "Incoming" + File.separator + incomingEmail), //from
                                Paths.get(basePath + emailAddress + File.separator + "Inbox" + File.separator + incomingEmail), //to
                                java.nio.file.StandardCopyOption.ATOMIC_MOVE,
                                java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            ret = false;
        } finally {
            lockHashMap.get(basePath + emailAddress + File.separator + "Incoming").writeLock().unlock();
            lockHashMap.get(basePath + emailAddress + File.separator + "Inbox").writeLock().unlock();
        }

        return ret;
    }

    /**
     * Given an email and a file, writes said email on said file.
     * @param email         email to write
     * @param f             file to be written
     * @throws IOException  on failure
     */
    private void writeEmailOnFile(Email email, File f)  throws IOException {
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
    private Email readEmailFromFile(Email email, File f) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(f);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

        email.readExternal(objectInputStream);
        objectInputStream.close();
        fileInputStream.close();

        return email;
    }

    /**
     * Given a user and a directory, returns the path to the user's said directory.
     * @param emailAddress  user's email address.
     * @param directory     user's directory which path is needed.
     * @return              user's directory's path.
     */
    private String getPath(String emailAddress, String directory) {
        return basePath + emailAddress + File.separator + directory;
    }
}
