package it.unito.prog.models;

import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

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

    /**
     * Sorts and sets the inbox email list.
     * @param inboxEmails list of emails to be set.
     */
    public void setAllInboxEmails(List<Email> inboxEmails) {
        inboxEmails.sort(Collections.reverseOrder(Email::compareTo));
        this.inboxEmails.setAll(inboxEmails);
    }

    public ObservableList<Email> getSentEmails() {
        return this.sentEmails;
    }

    /**
     * Sorts and sets the Sent email list.
     * @param sentEmails list of emails to be set.
     */
    public void setAllSentEmails(List<Email> sentEmails) {
        sentEmails.sort(Collections.reverseOrder(Email::compareTo));
        this.sentEmails.setAll(sentEmails);
    }

    /**
     * Append a sorted list of emails to the beginning of the current inbox email list, then increments the new emails
     * counter and visualizes it.
     * @param emails email list to be added.
     */
    public void addInboxEmails(List<Email> emails) {
        emails.sort(Collections.reverseOrder(Email::compareTo));
        this.inboxEmails.addAll(0, emails);
        increaseCounterNewEmail(emails.size());
        Utils.showAlert(Alert.AlertType.INFORMATION,"You have (" + emails.size() + ") new emails!");
    }

    /**
     * Appends the email to the beginning of the current inbox sent list.
     * @param email email to be added.
     */
    public void addSentEmails(Email email) {
        this.sentEmails.add(0, email);
    }

    public void setServerStatus(boolean serverStatus) {
        this.serverStatus.set(serverStatus ? "Server online" : "Server offline");
    }

    public StringProperty serverStatusProperty() {
        return serverStatus;
    }

    /**
     * Deletes an email.
     * @param email to be deleted.
     */
    public void removeEmail(Email email) {
        inboxEmails.remove(email);
        sentEmails.remove(email);
    }

    /**
     * Used for counting the number of new emails received.
     * @param n counter.
     */
    private void increaseCounterNewEmail(int n) {
        int counter = counterNewEmail.get().equals("") ? 0 : Integer.parseInt(counterNewEmail.get());
        counter+=n;
        counterNewEmail.set(Integer.toString(counter));
    }

    /**
     * Resets the counter used for counting the number of new emails received.
     */
    public void resetCounterNewEmail() {
        counterNewEmail.set("");
    }
}
