package it.unito.prog.client.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

public class Email {
    private StringProperty sender; // it's assumed that the email addresses are correct
    private ListProperty<String> receivers; // it's assumed that the email addresses are correct
    private StringProperty object;
    private StringProperty message;
    private LongProperty timestamp;

    public Email(StringProperty sender, ListProperty<String> receivers, StringProperty object, StringProperty message, LongProperty timestamp) {
        this.sender = sender;
        this.receivers = receivers;
        this.object = object;
        this.message = message;
        this.timestamp = timestamp;
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

    public long getTimestamp() {
        return timestamp.get();
    }

    public LongProperty timestampProperty() {
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

    public void setTimestamp(long timestamp) {
        this.timestamp.set(timestamp);
    }
}
