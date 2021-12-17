package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EmailListElementController {

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    public EmailListElementController() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-list-element-view.fxml"));
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    void setModel(Email email) {
        userLabel.setText(email.getSender());
        previewLabel.setText(email.getObject());
        dateLabel.setText(email.getTimestamp());
    }
}

