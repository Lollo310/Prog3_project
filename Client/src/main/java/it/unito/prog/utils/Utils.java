package it.unito.prog.utils;

import javafx.scene.control.Alert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    // used for clearing the HTMLEditor used for composing emails
    public static final String clearHTML = "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"></body>";

    // DateTime format
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

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
     * Given a string of receivers, splits the receivers.
     * @param receivers string of receivers.
     * @return array of receivers.
     */
    public static String[] parseReceivers(String receivers) {
        return receivers.split("\\s*;\\s*");
    }

    /**
     * Given a string of receivers, splits the receivers and eventually removes the sender if present.
     * @param user user email.
     * @param receivers string of receivers.
     * @return filtered string of receivers.
     */
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

    /**
     * Visualises the alert box.
     * @param alertType type of the alert.
     * @param msg alert message to be shown.
     */
    public static void showAlert(Alert.AlertType alertType, String msg) {
        Alert alert = new Alert(alertType, msg);
        alert.show();
    }
}
