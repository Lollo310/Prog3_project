package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EmailListElementController {

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    void setModel(Email email) {
        userLabel.setText(email.getSender());
        previewLabel.setText(email.getObject());
        dateLabel.setText(email.getTimestamp());
    }
}

