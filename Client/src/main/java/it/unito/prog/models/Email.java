package it.unito.prog.models;

import it.unito.prog.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.time.LocalDateTime;

public class Email implements Externalizable, Comparable<Email> {

    private long id;

    private transient final StringProperty sender;

    private transient final StringProperty receivers;

    private transient final StringProperty subject;

    private transient final StringProperty message;

    private transient final StringProperty timestamp;

    public Email() {
        this.id = -1;
        this.sender = new SimpleStringProperty();
        this.receivers = new SimpleStringProperty();
        this.subject = new SimpleStringProperty();
        this.message = new SimpleStringProperty();
        this.timestamp = new SimpleStringProperty();
    }

    public Email(String sender, String receivers, String subject, String message) {
        this.id = -1;
        this.sender = new SimpleStringProperty(sender);
        this.receivers = new SimpleStringProperty(receivers);
        this.subject = new SimpleStringProperty(subject);
        this.message = new SimpleStringProperty(message);
        this.timestamp = new SimpleStringProperty(Utils.getTimestamp());
    }

    public String getSender() {
        return this.sender.get();
    }

    public StringProperty senderProperty() {
        return this.sender;
    }

    public String getReceivers() {
        return this.receivers.get();
    }

    public StringProperty receiversProperty() {
        return this.receivers;
    }

    public String getSubject() {
        return this.subject.get();
    }

    public StringProperty subjectProperty() {
        return this.subject;
    }

    public String getMessage() {
        return this.message.get();
    }

    public String getTimestamp() {
        return this.timestamp.get();
    }

    public StringProperty timestampProperty() {
        return this.timestamp;
    }

    public void setSender(String sender) {
        this.sender.set(sender);
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    public void setTimestamp(String timestamp) {
        this.timestamp.set(timestamp);
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return timestamp in dd/mm/yyyy hh:mm format.
     */
    private LocalDateTime getParsedTimestamp() {
        return Utils.parseTimestamp(this.getTimestamp());
    }

    /**
     * Used for custom serialization.
     * @param out ObjectOutput stream.
     * @throws IOException caused by writes on the output stream.
     */
    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(this.id);
        out.writeUTF(this.sender.get());
        out.writeUTF(this.receivers.get());
        out.writeUTF(this.subject.get());
        out.writeUTF(this.message.get());
        out.writeUTF(this.timestamp.get());
    }

    /**
     * Used for custom deserialization.
     * @param in ObjectInput stream.
     * @throws IOException caused by reads on the input stream.
     */
    @Override
    public void readExternal(ObjectInput in) throws IOException {
        this.id = in.readLong();
        this.sender.set(in.readUTF());
        this.receivers.set(in.readUTF());
        this.subject.set(in.readUTF());
        this.message.set(in.readUTF());
        this.timestamp.set(in.readUTF());
    }

    /**
     * Compares the emails' timestamps.
     * @param o email to be compared.
     * @return n == 0 if equal, n < 0 if lesser, n > 0 if greater.
     */
    @Override
    public int compareTo(Email o) {
        return this.getParsedTimestamp().compareTo(o.getParsedTimestamp());
    }
}
