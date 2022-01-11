package it.unito.prog.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Client {

    private final StringProperty serverStatus;

    private final StringProperty user;

    private final ObservableList<Email> inboxEmails;

    private final ObservableList<Email> sentEmails;

    public Client(String user) {
        this.serverStatus = new SimpleStringProperty("Server offline");
        this.user = new SimpleStringProperty(user);
        this.inboxEmails = FXCollections.observableArrayList();
        this.sentEmails = FXCollections.observableArrayList();
    }

    public String getUser() {
        return this.user.get();
    }

    public StringProperty userProperty() {
        return this.user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public ObservableList<Email> getInboxEmails() {
        return this.inboxEmails;
    }

    public void setAllInboxEmails(List<Email> inboxEmails) {
        this.inboxEmails.setAll(inboxEmails);
    }

    public ObservableList<Email> getSentEmails() {
        return this.sentEmails;
    }

    public void setAllSentEmails(List<Email> sentEmails) {
        this.sentEmails.setAll(sentEmails);
    }

    public void addInboxEmails(List<Email> emails) {
        this.inboxEmails.addAll(0, emails);
    }

    public void addSentEmails(List<Email> emails) {
        this.sentEmails.addAll(emails);
    }

    public String getServerStatus() {
        return serverStatus.get();
    }

    public void setServerStatus(boolean serverStatus) {
        this.serverStatus.set(serverStatus ? "Server online" : "Server offline");
    }

    public StringProperty serverStatusProperty() {
        return serverStatus;
    }
}
