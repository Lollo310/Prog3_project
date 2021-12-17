package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class EmailListElementController {
    private Email model;
    private Pane contentPane;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    @FXML
    void onEmailListElementMouseClicked() { // gestire eccezione
        //check che non sia null altrimenti genera errori on click
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-read-view.fxml"));

        try {
            Node panel = loader.load();
            EmailReadController emailReadController = loader.getController();

            emailReadController.setEmail(this.model);
            this.contentPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    void setModel(Email email) {
        this.model = email;
        userLabel.setText(email.getSender());
        previewLabel.setText(email.getObject());
        dateLabel.setText(email.getTimestamp());
    }

    public void setContentPane(Pane contentPane) {
        this.contentPane = contentPane;
    }
}

