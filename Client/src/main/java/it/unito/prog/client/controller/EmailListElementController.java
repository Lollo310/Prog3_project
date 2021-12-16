package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class EmailListElementController {
    private Email model;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    @FXML
    void initialize() {
        userLabel.textProperty().bind(model.senderProperty());
        previewLabel.textProperty().bind(model.objectProperty());
        dateLabel.textProperty().bind(model.timestampProperty());
    }

    void setModel(Email email) {
        this.model = email;
    }
}

