package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.EmailListElement;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class EmailListController implements Controller {

    private Client clientModel;

    private String typeOfList;

    private BorderPane contentPanel;

    @FXML
    private ListView<Email> emailListView;

    @FXML
    void initialize() {}

    private void setEmailListView() {
        switch (typeOfList) {
            case "INBOX" -> emailListView.setItems(clientModel.getInboxEmails());
            case "SENT" -> emailListView.setItems(clientModel.getSentEmails());
            default -> System.err.println("Error");
        }

        emailListView.setCellFactory(listView -> new EmailListElement(contentPanel, clientModel));
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw  new IllegalArgumentException("Model cannot be null and it must be a Client instance");
        clientModel = (Client) model;
        setEmailListView();
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("ExtraArgs cannot be null and it must be a String instance");
        typeOfList = (String) extraArgs;
    }

    @Override
    public void setContentPanel(Node contentPanel) {
        if (!(contentPanel instanceof BorderPane))
            throw new IllegalArgumentException("ExtraArgs cannot be null and it must be a String instance");
        this.contentPanel = (BorderPane) contentPanel;
    }
}

