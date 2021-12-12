package it.unito.prog.client.controller;

import it.unito.prog.client.view.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ClientController {

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Label userEmail;

    @FXML
    void onComposeButtonAction(ActionEvent event) {
        System.out.println("Clicked compose button!"); /* for testing */
    }

    @FXML
    void onInboxButtonAction(ActionEvent event) throws IOException {
        System.out.println("Clicked inbox button!"); /* for testing */

        /* to test the loading pages */
        Pane panel = FXMLLoader.load(ClientApplication.class.getResource("email-list-view.fxml"));
        contentPanel.getChildren().setAll(panel);

    }

    @FXML
    void onSentButtonAction(ActionEvent event) {
        System.out.println("Clicked sent button!");
    }

}
