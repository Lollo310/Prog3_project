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
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.io.IOException;

public class EmailReadController implements Controller {

    private Client clientModel;

    private Email emailModel;

    private BorderPane contentPanel;

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
            try {
                FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-list-view.fxml"));
                Node panel = loader.load();
                Controller controller = loader.getController();

                controller.setExtraArgs(emailModel.getSender().equals(clientModel.getUser()) ? "SENT" : "INBOX");
                controller.setModel(clientModel);
                clientModel.removeEmail(emailModel);
                contentPanel.setCenter(panel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            infoLabel.setText(feedback.getMsg());
            infoLabel.setVisible(true);
        }
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
            controller.setModel(this.clientModel);
            this.contentPanel.setCenter(panel);
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
            controller.setModel(this.clientModel);
            contentPanel.setCenter(panel);
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
            controller.setModel(this.clientModel);
            contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        infoLabel.setVisible(false);
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

    @Override
    public void setContentPanel(BorderPane contentPanel) {
        if (contentPanel == null)
            throw new IllegalArgumentException("contentPanel cannot be null");

        this.contentPanel = contentPanel;
    }

    private void setProperty() {
        fromTextField.textProperty().bind(emailModel.senderProperty());
        toTextField.textProperty().bind(emailModel.receiversProperty());
        subjectTextField.textProperty().bind(emailModel.subjectProperty());
        messageAreaField.getEngine().loadContent(emailModel.getMessage());
        datetimeTextField.textProperty().bind(emailModel.timestampProperty());
    }

}


