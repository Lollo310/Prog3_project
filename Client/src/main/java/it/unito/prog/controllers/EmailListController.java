package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.EmailListElement;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class EmailListController implements Controller {

    private Client clientModel;

    private String typeOfList;

    @FXML
    private AnchorPane contentAnchorPane;

    @FXML
    private ListView<Email> emailListView;

    @FXML
    void initialize() {}

    private void setEmailListView() {
        List<Email> emails = new ArrayList<>();
        emails.add(new Email("elisali.perottino@edu.unito.it", "michele.lorenzo@edu.unito.it; foca.grassa@unito.it; ciao@cio.it", "Ciao finocchia", "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p style=\"text-align: center;\"><span style=\"font-family: &quot;&quot;;\">csalkcnlsan</span></p></body></html>"));
        clientModel.setAllInboxEmails(emails);

        switch (typeOfList) {
            case "INBOX" -> emailListView.setItems(clientModel.getInboxEmails());
            case "SENT" -> emailListView.setItems(clientModel.getSentEmails());
            default -> System.err.println("Error");
        }

        emailListView.setCellFactory(listView -> new EmailListElement(contentAnchorPane, clientModel.getUser()));
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw  new IllegalArgumentException("Model cannot be null and it must be a Client instance");
        this.clientModel = (Client) model;
        setEmailListView(); //altrimenti dà null
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("ExtraArgs cannot be null and it must be a String instance");
        typeOfList = (String) extraArgs;
    }
}

