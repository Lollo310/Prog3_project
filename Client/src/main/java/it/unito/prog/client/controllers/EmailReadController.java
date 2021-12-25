package it.unito.prog.client.controllers;

import it.unito.prog.client.models.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmailReadController implements Controller {
    private Email emailModel;

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

    private void setProperty() {
        fromTextField.textProperty().bind(this.emailModel.senderProperty());
        toTextField.textProperty().bind(this.emailModel.receiversProperty());
        subjectTextField.textProperty().bind(this.emailModel.objectProperty());
        messageAreaField.textProperty().bind(this.emailModel.messageProperty());
        datetimeTextField.textProperty().bind(this.emailModel.timestampProperty());
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Email))
            throw new IllegalArgumentException("models cannot be null and it must be a Email instance");
        this.emailModel = (Email) model;
        setProperty();
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}


