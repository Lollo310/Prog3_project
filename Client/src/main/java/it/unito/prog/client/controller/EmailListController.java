package it.unito.prog.client.controller;

import it.unito.prog.client.model.Client;
import it.unito.prog.client.model.Email;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

public class EmailListController implements Controller {
    private Client clientModel;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private ListView<Email> emailListView;

    @FXML
    void initialize() {
        setEmailListView();
    }

    //Implementare listView con le property
    private void setEmailListView() {
        ObservableList<Email> emails = FXCollections.observableArrayList();

        emails.add(new Email("Michele Foca Grassa", "Elisa grassa", "Ciao finocchia", "Sei proprio bella", "10/12/2021"));
        emailListView.setItems(emails);
        emailListView.setCellFactory(listView -> new EmailListElement(contentAnchorPane));
    }

    @Override
    public void setModel(Object model) {
        if (model == null || !(model instanceof Client))
            throw  new IllegalArgumentException("Client cannot be null and it must be a Client instance");
        this.clientModel = (Client) model;
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}

