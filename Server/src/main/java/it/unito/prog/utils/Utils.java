package it.unito.prog.utils;

public class Utils {

    public static String parseEmailAddress (String emailAddress) {
        return emailAddress.split("@")[0];
    }

    public static String[] parseReceivers (String receivers) {
        return receivers.split(";");
    }

}
