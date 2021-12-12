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
    void onComposeButtonAction(ActionEvent event) throws IOException { /* gestire eccezione */
        Pane panel = FXMLLoader.load(ClientApplication.class.getResource("email-write-view.fxml"));

        contentPanel.getChildren().setAll(panel);
    }

    @FXML
    void onInboxButtonAction(ActionEvent event) throws IOException { /* gestire eccezione */
        Pane panel = FXMLLoader.load(ClientApplication.class.getResource("email-list-view.fxml"));

        contentPanel.getChildren().setAll(panel);
    }

    //Incorporare con onInboxButtonAction?!
    @FXML
    void onSentButtonAction(ActionEvent event) throws IOException { /* gestire eccezione */
        Pane panel = FXMLLoader.load(ClientApplication.class.getResource("email-list-view.fxml"));

        contentPanel.getChildren().setAll(panel);
    }

}
