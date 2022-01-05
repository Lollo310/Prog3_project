package it.unito.prog.controllers;

import it.unito.prog.models.Email;
import it.unito.prog.utils.WebUtils;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EmailReadController implements Controller {

    private String user;

    private Email emailModel;

    @FXML
    private AnchorPane contentAnchorPane;

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
    private Button replyButton;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    void onDeleteButtonAction() {
        WebUtils.deleteMessage(user, emailModel);
    }

    @FXML
    void onForwardButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email forwardEmail = new Email(
                emailModel.getId(),
                user,
                "",
                emailModel.getObject(),
                emailModel.getMessage(),
                emailModel.getTimestamp()
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(forwardEmail);
            contentAnchorPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onReplyAllButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email replyAllEmail = new Email();

    }

    @FXML
    void onReplyButtonAction() {

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

    private void setProperty() {
        fromTextField.textProperty().bind(this.emailModel.senderProperty());
        toTextField.textProperty().bind(this.emailModel.receiversProperty());
        subjectTextField.textProperty().bind(this.emailModel.objectProperty());
        messageAreaField.textProperty().bind(this.emailModel.messageProperty());
        datetimeTextField.textProperty().bind(this.emailModel.timestampProperty());
    }

}


