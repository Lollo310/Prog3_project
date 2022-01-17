package it.unito.prog.utils;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String parseEmailAddress (String emailAddress) {
        return emailAddress.split("@")[0];
    }

    public static String[] parseReceivers (String receivers) {

        return receivers.split("\\s*;\\s*");
    }

    public static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

    public static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, formatter);
    }

    public static long generateUUID() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
