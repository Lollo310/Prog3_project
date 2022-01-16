package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.IOException;

public class EmailReadController implements Controller {

    private Client clientModel;

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
    private Label infoLabel;

    @FXML
    void onDeleteButtonAction() {
        Feedback feedback = WebUtils.deleteMessage(clientModel.getUser(), emailModel);

        if (feedback.getId() == 0) {
            clientModel.removeEmail(emailModel);
            infoLabel.setText(feedback.getMsg());
            infoLabel.setVisible(true);
        } else
            Utils.showAlert(feedback.getMsg());
    }

    @FXML
    void onForwardButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email forwardEmail = new Email(
                clientModel.getUser(),
                "",
                "[forward]" + emailModel.getSubject(), //far capire la data e chi la inviata
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
                clientModel.getUser(),
                emailModel.getSender() + "; " + Utils.filterReceivers(clientModel.getUser(), emailModel.getReceivers()),
                "[replyAll] " + emailModel.getSubject(),
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
                clientModel.getUser(),
                emailModel.getSender(),
                "[reply] " + emailModel.getSubject(),
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
    void initialize() {
        infoLabel.setVisible(false);
        infoLabel.getStyleClass().add("alert-success");
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
        if (!(extraArgs instanceof Client))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a String instance");
        this.clientModel = (Client) extraArgs;
    }

    private void setProperty() {
        fromTextField.textProperty().bind(emailModel.senderProperty());
        toTextField.textProperty().bind(emailModel.receiversProperty());
        subjectTextField.textProperty().bind(emailModel.subjectProperty());
        messageAreaField.getEngine().loadContent(emailModel.getMessage());
        datetimeTextField.textProperty().bind(emailModel.timestampProperty());
    }

}


