package it.unito.prog.utils;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {

    // DateTime format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Given a string of receivers, splits the receivers.
     * @param receivers string of receivers.
     * @return array of receivers.
     */
    public static String[] parseReceivers (String receivers) {
        return receivers.split("\\s*;\\s*");
    }

    /**
     * @return formatted timestamp.
     */
    public static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * Given a string timestamp, converts it in LocalDateTime format.
     * @param timestamp timestamp to convert.
     * @return LocalDateTime timestamp.
     */
    public static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, formatter);
    }

    /**
     * @return random positive UUID converted to long.
     */
    public static long generateUUID() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }
}
