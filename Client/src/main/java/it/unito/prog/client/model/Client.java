package it.unito.prog.client.model;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class Client {
    private StringProperty user;
    private ListProperty<Email> inbox;
    private ListProperty<Email> sent;

    public Client(StringProperty user) {
        this.user = user;
        this.inbox = new SimpleListProperty<>();
        this.sent = new SimpleListProperty<>();
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

    public ObservableList<Email> getInbox() {
        return inbox.get();
    }

    public ListProperty<Email> inboxProperty() {
        return inbox;
    }

    public void setInbox(ObservableList<Email> inbox) {
        this.inbox.set(inbox);
    }

    public ObservableList<Email> getSent() {
        return sent.get();
    }

    public ListProperty<Email> sentProperty() {
        return sent;
    }

    public void setSent(ObservableList<Email> sent) {
        this.sent.set(sent);
    }
}
