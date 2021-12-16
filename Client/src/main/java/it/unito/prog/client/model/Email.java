package it.unito.prog.client.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.util.List;

public class Email {
    private StringProperty sender; // it's assumed that the email addresses are correct
    private ListProperty<String> receivers; // it's assumed that the email addresses are correct
    private StringProperty object;
    private StringProperty message;
    private StringProperty timestamp;

    public Email(String sender, List<String> receivers, String object, String message, String timestamp) {
        this.sender = new SimpleStringProperty(sender);

        this.receivers = new SimpleListProperty<String>();
        this.receivers.addAll(receivers);

        this.object = new SimpleStringProperty(object);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(timestamp);
    }

    public String getSender() {
        return sender.get();
    }

    public StringProperty senderProperty() {
        return sender;
    }

    public ObservableList<String> getReceivers() {
        return receivers.get();
    }

    public ListProperty<String> receiversProperty() {
        return receivers;
    }

    public String getObject() {
        return object.get();
    }

    public StringProperty objectProperty() {
        return object;
    }

    public String getMessage() {
        return message.get();
    }

    public StringProperty messageProperty() {
        return message;
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public void setReceivers(ObservableList<String> receivers) {
        this.receivers.set(receivers);
    }

    public void setObject(String object) {
        this.object.set(object);
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }
}
