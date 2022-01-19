package it.unito.prog.models;

import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Client {

    private final StringProperty counterNewEmail;

    private final StringProperty serverStatus;

    private final StringProperty user;

    private final ObservableList<Email> inboxEmails;

    private final ObservableList<Email> sentEmails;

    public Client(String user) {
        counterNewEmail = new SimpleStringProperty("");
        serverStatus = new SimpleStringProperty();
        setServerStatus(WebUtils.isOnline());
        this.user = new SimpleStringProperty(user);
        inboxEmails = FXCollections.observableArrayList();
        sentEmails = FXCollections.observableArrayList();
    }

    public StringProperty counterNewEmailProperty() {
        return counterNewEmail;
    }

    public String getUser() {
        return this.user.get();
    }

    public StringProperty userProperty() {
        return this.user;
    }

    public ObservableList<Email> getInboxEmails() {
        return this.inboxEmails;
    }

    public void setAllInboxEmails(List<Email> inboxEmails) {
        inboxEmails.sort(Collections.reverseOrder(Email::compareTo));
        this.inboxEmails.setAll(inboxEmails);
    }

    public ObservableList<Email> getSentEmails() {
        return this.sentEmails;
    }

    public void setAllSentEmails(List<Email> sentEmails) {
        sentEmails.sort(Collections.reverseOrder(Email::compareTo));
        this.sentEmails.setAll(sentEmails);
    }

    public void addInboxEmails(List<Email> emails) {
        emails.sort(Collections.reverseOrder(Email::compareTo));
        this.inboxEmails.addAll(0, emails);
        increaseCounterNewEmail(emails.size());
        Utils.showAlert(Alert.AlertType.INFORMATION,"You have (" + emails.size() + ") new emails!");
    }

    public void addSentEmails(Email email) {
        this.sentEmails.add(0, email);
    }

    public void setServerStatus(boolean serverStatus) {
        this.serverStatus.set(serverStatus ? "Server online" : "Server offline");
    }

    public StringProperty serverStatusProperty() {
        return serverStatus;
    }

    public void removeEmail(Email email) {
        inboxEmails.remove(email);
        sentEmails.remove(email);
    }

    private void increaseCounterNewEmail(int n) {
        int counter = counterNewEmail.get().equals("") ? 0 : Integer.parseInt(counterNewEmail.get());
        counter+=n;
        counterNewEmail.set(Integer.toString(counter));
    }

    public void resetCounterNewEmail() {
        counterNewEmail.set("");
    }
}
