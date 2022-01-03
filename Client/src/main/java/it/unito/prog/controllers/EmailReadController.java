package it.unito.prog.controllers;

import it.unito.prog.models.Email;
import it.unito.prog.utils.WebUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EmailReadController implements Controller {

    private String user;

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

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        WebUtils.deleteMessage(user, emailModel);
    }

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
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a String instance");
        this.user = (String) extraArgs;
    }
}


