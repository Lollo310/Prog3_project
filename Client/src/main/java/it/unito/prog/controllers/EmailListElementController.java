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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.List;

public class EmailListElementController implements Controller {

    private Email emailModel;

    private BorderPane contentPanel;

    private Client clientModel;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    @FXML
    void onEmailListElementMouseClicked() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-read-view.fxml"));

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(clientModel);
            controller.setModel(emailModel);
            controller.setContentPanel(contentPanel);
            contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    private void setLabel() {
        userLabel.setText(emailModel.getSender());
        previewLabel.setText(emailModel.getSubject());
        dateLabel.setText(emailModel.getTimestamp());
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Email))
            throw new IllegalArgumentException("models cannot be null and it must be a Email instance");

        emailModel = (Email) model;
        setLabel();
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof Client))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a Client instance");

        clientModel = (Client)  extraArgs;
    }

    @Override
    public void setContentPanel(BorderPane contentPanel) {
        if (contentPanel == null)
            throw new IllegalArgumentException("contentPanel cannot be null");

        this.contentPanel = contentPanel;
    }
}

