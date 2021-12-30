package it.unito.prog.server.utils;

import java.io.IOException;

public class FileManager {

    //created directory
    public static void createUserDir(String path) {

        //how to get username ??
        Path path = Paths.get(path + "\\Images\\Background\\..\\Foreground\\Necklace\\..\\Earrings\\..\\Etc");

        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Cannot create directories - " + e);
            System.out.println("Failed to initialize user's directory.");
        }
    }

    //handle locks

    //handle send

    //handle delete

}
