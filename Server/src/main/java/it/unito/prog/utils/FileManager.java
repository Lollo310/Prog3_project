package it.unito.prog.utils;

import it.unito.prog.models.Email;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    private static final String cwd = Path.of("").toAbsolutePath().toString();
    private static final String parentDir = Paths.get(cwd).getParent().getFileName().toString();
    public static final String BASE_PATH = parentDir + File.separator + "users";

    //misc
    public static boolean userDirExists(String basePath, String username) {
        Path path = Paths.get(basePath + ":\\" + username);
        return Files.exists(path);
    }

    public static void initUserDir(String basePath, String username) {

        Path path = Paths.get(basePath + ":\\" + username + "\\Inbox\\..\\Sent\\");

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Failed to initialize user's directory - " + e);
        }
    }

    //handle locks

    //handle send
    public static void readEmail() {}

    public static void sendEmail(Email email, String basePath, String username) {
        if(!userDirExists(basePath, username)) {
            initUserDir(basePath, username);
        }
    }

    //handle delete
    public static void deleteEmail() {}

}
