package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmailReadController {
    private Email email;

    @FXML
    private TextField datetimeTextField;

    @FXML
    private Button deleteButton;

    @FXML
    private Button forwardButton;

    @FXML
    private TextField fromTextField;

    @FXML
    private TextArea messageAreaField;

    @FXML
    private Button replyAllButton;

    @FXML
    private Button sendButton;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    void setEmail(Email email) {
        this.email = email;
        setProperty();
    }

    private void setProperty() {
        fromTextField.textProperty().bind(this.email.senderProperty());
        toTextField.textProperty().bind(this.email.receiversProperty());
        subjectTextField.textProperty().bind(this.email.objectProperty());
        messageAreaField.textProperty().bind(this.email.messageProperty());
        datetimeTextField.textProperty().bind(this.email.timestampProperty());
    }

}


