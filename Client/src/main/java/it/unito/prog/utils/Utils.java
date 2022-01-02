package it.unito.prog.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Utils {

    public static String htmlToText(String htmlCode) {
        Document doc = Jsoup.parse(htmlCode);
        return doc.body().text();
    }

}
