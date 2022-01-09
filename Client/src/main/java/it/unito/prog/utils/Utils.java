package it.unito.prog.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static final String clearHTML = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body>";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public static String getTimestamp() {
        return LocalDateTime.now().format(formatter);
    }

    public static LocalDateTime parseTimestamp(String timestamp) {
        return LocalDateTime.parse(timestamp, formatter);
    }

    public static String[] parseReceivers(String receivers) {
        return receivers.split("\\s*;\\s*");
    }

    public static String filterReceivers(String user, String receivers) {
        String[] splittedReceivers = parseReceivers(receivers);
        List<String> filteredReceiverList = new ArrayList<>();

        for (String userEmail :
                splittedReceivers) {
            if (!userEmail.equals(user))
                filteredReceiverList.add(userEmail);
        }

        return String.join("; ", filteredReceiverList);
    }
}
