package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Path inbox_lock = Paths.get(basePath + username + File.separator + "Inbox" + File.separator + "lock");
        Path sent_lock = Paths.get(basePath + username + File.separator + "Sent" + File.separator + "lock");

        try {
            Files.createDirectories(user);
            Files.createDirectories(inbox);
            Files.createDirectories(sent);
            Files.createFile(inbox);
            ret = true;;
        } catch (IOException e) {
            System.err.println("Failed to initialize user's directory - " + e);
            ret = false;
        } finally {
            return ret;
        }
    }

    public static List<Email> getDifference (List<Email> client, List<Email> server) {
        return null;
    }

    //caricare lista delle mail
    public static void loadList(String list) {

    }

    public static Feedback sendEmail(Email email) throws IOException {
        String sender = parseEmailAddress(email.getSender());
        String[] receivers = parseReceivers(email.getReceivers());

        if(!existsUserDir(sender)) {
            if(!initUserDir(sender)) {
                return new Feedback(-1, "Failed to access user's data.");
            }
        }
        if(receivers.length == 0) return new Feedback(-1, "No receivers specified."); //handle no receivers specified

        for (String receiver : receivers) {
            if(!existsUserDir(receiver)) return new Feedback(-1, "Receiver " + receiver + " does not exist."); // dà errore o prova a creare a la directory?
        }

        //lock sender's dir
        FileChannel channel = FileChannel.open(Paths.get(), StandardOpenOption.READ);
        FileLock lock = channel.lock(0, Long.MAX_VALUE, true);

        return null;
    }

    public static void readEmail(List<Email> list) {

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
        String dir = email.getSender() == username? "Sent" : "Inbox";
        Feedback f = new Feedback(0, "File deleted successfully.");

        if(!existsUserDir(username)) {
            if(!initUserDir(username)) {
                return new Feedback(-1, "Failed to access user's data.");
            }
        }

        if (Files.exists(Paths.get(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt"))) {
            FileChannel channel = FileChannel.open(placeholder.getAbsolutePath(), StandardOpenOption.READ);
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
