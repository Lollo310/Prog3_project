package it.unito.prog.client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Client {
    private StringProperty user;
    private ObservableList<Email> emails;

    public Client(String user) {
        this.user = new SimpleStringProperty(user);
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

    public ObservableList<Email> getEmails() {
        return emails;
    }

    public void setEmails(ObservableList<Email> emails) {
        this.emails = emails;
    }
}
