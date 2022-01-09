package it.unito.prog.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Client {

    private final StringProperty user;

    private ObservableList<Email> inboxEmails;

    private ObservableList<Email> sentEmails;

    public Client(String user) {
        this.user = new SimpleStringProperty(user);
        inboxEmails = FXCollections.observableArrayList();
        sentEmails = FXCollections.observableArrayList();
    }

    public String getUser() {
        return user.get();
    }

    public StringProperty userProperty() {
        return user;
    }

    public void setUser(String user) {
        this.user.set(user);
    }

    public ObservableList<Email> getInboxEmails() {
        return inboxEmails;
    }

    public void setAllInboxEmails(List<Email> inboxEmails) {
        this.inboxEmails.setAll(inboxEmails);
    }

    public ObservableList<Email> getSentEmails() {
        return sentEmails;
    }

    public void setAllSentEmails(List<Email> sentEmails) {
        this.sentEmails.setAll(sentEmails);
    }

    public void addInboxEmails(List<Email> emails) {
        inboxEmails.addAll(0, emails);
    }

    public void addSentEmails(List<Email> emails) {
        sentEmails.addAll(emails);
    }
}
