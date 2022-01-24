package it.unito.prog.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
    private final ObservableList<String> log;

    public Server() {
        log = FXCollections.observableArrayList();
    }

    public ObservableList<String> getLog() {
        return log;
    }

    /**
     * Given a string, adds it to the server log.
     * @param s string to be added.
     */
    public void updateLog(String s){
        log.add(s);
    }
}
