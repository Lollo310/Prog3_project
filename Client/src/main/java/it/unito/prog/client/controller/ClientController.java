package it.unito.prog.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ClientController {

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Label userEmail;

    @FXML
    void onComposeButtonAction(ActionEvent event) {
        System.out.println("Clicked compose button!");
    }

    @FXML
    void onInboxButtonAction(ActionEvent event) {
        System.out.println("Clicked inbox button!");
    }

    @FXML
    void onSentButtonAction(ActionEvent event) {
        System.out.println("Clicked sent button!");
    }

}
