package it.unito.prog.controllers;

import it.unito.prog.models.Email;
import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.IOException;

public class EmailReadController implements Controller {

    private String user;

    private Email emailModel;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private TextField datetimeTextField;

    @FXML
    private TextField fromTextField;

    @FXML
    private WebView messageAreaField;

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
                user,
                "",
                "[forward]" + emailModel.getObject(),
                emailModel.getMessage()
        );

        forwardEmail.setTimestamp(emailModel.getTimestamp());

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
        Email replyAllEmail = new Email(
                user,
                emailModel.getSender() + "; " + Utils.filterReceivers(user, emailModel.getReceivers()),
                "[replyAll] " + emailModel.getObject(),
                ""
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(replyAllEmail);
            contentAnchorPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onReplyButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email replyAllEmail = new Email(
                user,
                emailModel.getSender(),
                "[reply] " + emailModel.getObject(),
                ""
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(replyAllEmail);
            contentAnchorPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        fromTextField.textProperty().bind(emailModel.senderProperty());
        toTextField.textProperty().bind(emailModel.receiversProperty());
        subjectTextField.textProperty().bind(emailModel.objectProperty());
        messageAreaField.getEngine().loadContent(emailModel.getMessage());
        datetimeTextField.textProperty().bind(emailModel.timestampProperty());
    }

}


