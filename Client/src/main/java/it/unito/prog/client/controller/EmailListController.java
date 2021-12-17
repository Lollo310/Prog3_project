package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class EmailListController {

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private ListView<Email> emailListView;

    @FXML
    void initialize() {
        setEmailListView();
    }

    private void setEmailListView() {
        ObservableList<Email> emails = FXCollections.observableArrayList();
        emails.add(new Email("Michele Foca Grassa", "Elisa grassa", "Ciao finocchia", "Sei proprio bella", "10/12/2021"));
        emailListView.setItems(emails);
        emailListView.setCellFactory(listView -> new EmailListElement(contentAnchorPane));
    }
}

