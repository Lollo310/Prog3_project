package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class EmailListController {

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private ListView<?> emailListView;

    @FXML
    void onEmailListViewMouseClicked() throws IOException { // gestire eccezione
        //check che non sia null altrimenti genera errori on click
        System.out.println("[EmailList] list button.");

        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-read-view.fxml"));
        EmailReadController emailReadController = loader.getController();

        emailReadController.setEmail((Email) emailListView.getSelectionModel().getSelectedItem());
        Node panel = loader.load();
        contentAnchorPane.getChildren().setAll(panel);
    }

    @FXML
    void initialize() {

    }
}

