package it.unito.prog.models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.*;

public class Email implements Externalizable {
    private transient final StringProperty sender; // it's assumed that the email addresses are correct
    private transient final StringProperty receivers; // it's assumed that the email addresses are correct
    private transient final StringProperty object;
    private transient final StringProperty message;
    private transient final StringProperty timestamp;
    private transient final LongProperty id;

    public Email() {
        this.sender = new SimpleStringProperty();
        this.receivers = new SimpleStringProperty();
        this.object = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
        this.timestamp = new SimpleStringProperty();
        this.id = new SimpleLongProperty();
    }

    public Email(String sender, String receivers, String object, String message, String timestamp, long id) {
        this.sender = new SimpleStringProperty(sender);
        this.receivers = new SimpleStringProperty(receivers);
        this.object = new SimpleStringProperty(object);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(timestamp);
        this.id = new SimpleLongProperty(id);
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

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
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

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.sender.get());
        out.writeUTF(this.receivers.get());
        out.writeUTF(this.object.get());
        //out.writeUTF(this.message.get());
        //out.writeUTF(this.timestamp.get());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.sender.set(in.readUTF());
        this.receivers.set(in.readUTF());
        this.object.set(in.readUTF());
        //this.message.set(in.readUTF());
        //this.timestamp.set(in.readUTF());
    }
}