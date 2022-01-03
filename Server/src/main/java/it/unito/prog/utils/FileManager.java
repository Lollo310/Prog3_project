package it.unito.prog.utils;

import it.unito.prog.models.Email;

import java.io.IOException;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
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
    public static void initUserDir(String username) {

        Path user = Paths.get(basePath + username);
        Path inbox = Paths.get(basePath + username + File.separator + "Inbox");
        Path sent = Paths.get(basePath + username + File.separator + "Sent");

        try {
            Files.createDirectories(user);
            Files.createDirectories(inbox);
            Files.createDirectories(sent);
        } catch (IOException e) {
            System.err.println("Failed to initialize user's directory - " + e);
        }
    }

    public static List<Email> getDifference (List<Email> client, List<Email> server) {
        return null;
    }

    //caricare lista delle mail
    public static void loadList(String list) {

    }

    public static void sendEmail(Email email) {
        String sender = parseEmailAddress(email.getSender());
        String[]

        if(!existsUserDir(sender)) ; //handle user does not exists



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
    public static void deleteEmail(Email email, String emailAddress, String dir) throws IOException {
        String username = parseEmailAddress(emailAddress);
        File lockPath = new File(basePath + username + File.separator);

        if(!existsUserDir(sender)) ; //handle user does not exists

        if (Files.exists(Paths.get(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt"))) {
            FileChannel channel = new RandomAccessFile(lockPath, "rw").getChannel();
            FileLock lock = channel.lock();

            try {
                lock = channel.tryLock();
                File forDeletion = new File(basePath + username + File.separator + dir + File.separator + email.getId() + ".txt");
                if(!forDeletion.delete()) System.err.println("Failed to delete file.");

            } catch(OverlappingFileLockException e) {
                System.err.println("File is already locked in this thread or virtual machine - " + e);
            } finally {
                if(lock != null) lock.release();
                channel.close();
            }
        } else {
            //file not found, handle error
        }
    }
}
