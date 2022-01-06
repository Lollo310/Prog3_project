package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.EmailListElement;
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
    void initialize() {}

    private void setEmailListView() {
        ObservableList<Email> emails = FXCollections.observableArrayList();
        emails.add(new Email("elisali.perottino@edu.unito.it", "michele.lorenzo@edu.unito.it; foca.grassa@unito.it; ciao@cio.it", "Ciao finocchia", "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p style=\"text-align: center;\"><span style=\"font-family: &quot;&quot;;\">csalkcnlsan</span></p></body></html>"));
        clientModel.setEmails(emails);
        emailListView.setItems(clientModel.getEmails()); //equals to bind for listView
        emailListView.setCellFactory(listView -> new EmailListElement(contentAnchorPane, clientModel.getUser()));
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw  new IllegalArgumentException("Client cannot be null and it must be a Client instance");
        this.clientModel = (Client) model;
        setEmailListView(); //altrimenti dà null
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}

