package it.unito.prog.client.models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Email {
    private StringProperty sender; // it's assumed that the email addresses are correct
    private StringProperty receivers; // it's assumed that the email addresses are correct
    private StringProperty object;
    private StringProperty message;
    private StringProperty timestamp;

    public Email() {
        this.sender = new SimpleStringProperty();
        this.receivers = new SimpleStringProperty();
        this.object = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
        this.timestamp = new SimpleStringProperty();
    }

    public Email(String sender, String receivers, String object, String message, String timestamp) {
        this.sender = new SimpleStringProperty(sender);
        this.receivers = new SimpleStringProperty(receivers);
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

    public String getReceivers() {
        return receivers.get();
    }

    public StringProperty receiversProperty() {
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

    public void setReceivers(String receivers) {
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

    @Override
    public String toString() {  //debug
        return "Email{" +
                "sender=" + sender +
                ", receivers=" + receivers +
                ", object=" + object +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }
}
