package it.unito.prog.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server {
    private ObservableList<String> log;

    public Server() {
        log = FXCollections.observableArrayList();
    }

    public ObservableList<String> getLog() {
        return log;
    }

    public void update(String s){
        log.add(s);
    }
}
