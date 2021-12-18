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
    private Email email;
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

            emailReadController.setEmail(this.email);
            this.contentPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    void setEmail(Email email) {
        if (email == null)
            throw new IllegalArgumentException("Email can't be null!");

        this.email= email;
        setLabel();
    }

    public void setContentPane(Pane contentPane) {
        if (contentPane == null)
            throw new IllegalArgumentException("contentPane can't be null");

        this.contentPane = contentPane;
    }

    private void setLabel() {
        userLabel.setText(this.email.getSender());
        previewLabel.setText(this.email.getObject());
        dateLabel.setText(this.email.getTimestamp());
    }
}

