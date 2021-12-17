package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private ListView<Email> emailListView;

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
        setEmailListView();
    }

    private void setEmailListView() {
        ObservableList<Email> emails = FXCollections.observableArrayList();
        emails.add(new Email("Michele Foca Grassa", "Elisa grassa", "Ciao finocchia", "Sei proprio bella", "10/12/2021"));
        emailListView.setItems(emails);
        emailListView.setCellFactory(listView -> new EmailListElement());
    }
}

