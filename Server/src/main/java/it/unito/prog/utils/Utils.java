package it.unito.prog.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String parseEmailAddress (String emailAddress) {
        return emailAddress.split("@")[0];
    }

    public static String[] parseReceivers (String receivers) {
        return receivers.split("\\s*;\\s*");
    }

    public static String getTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
