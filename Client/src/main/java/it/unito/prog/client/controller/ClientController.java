package it.unito.prog.client.controller;

import it.unito.prog.client.model.Client;
import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientController {
    private Map<String, String> views;

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Label userEmail;

    @FXML
    void onVBoxButtonAction(ActionEvent event) throws IOException { /* gestire eccezione */
        Node node = (Node) event.getSource();
        Node panel = FXMLLoader.load(ClientApplication.class.getResource(views.get(node.getId())));

        contentPanel.getChildren().setAll(panel);
    }

    @FXML
    void initialize() {
        Client clientModel = new Client("michele.lorenzo@edu.unito.it");

        /* for future testing
            List<String> rec = new ArrayList<>();
            rec.add("x@y.z");
            Email test = new Email("me", rec, "test mail", "hello it's me", 124L);
            clientModel.getInbox().add(test);
        */

        views = new HashMap<>();
        views.put("composeButton", "email-write-view.fxml");
        views.put("inboxButton", "email-list-view.fxml");
        views.put("sentButton", "email-list-view.fxml");

        userEmail.textProperty().bind(clientModel.userProperty());
    }

}
