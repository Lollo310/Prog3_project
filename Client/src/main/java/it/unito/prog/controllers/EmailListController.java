package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.EmailListElement;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class EmailListController implements Controller {

    private Client clientModel;

    //typeOfList can either be "Inbox" or "Sent"
    private String typeOfList;

    private BorderPane contentPanel;

    @FXML
    private ListView<Email> emailListView;

    /**
     * Sets the view's content according to the list type which can be either "Inbox" or "Sent".
     */
    private void setEmailListView() {
        switch (typeOfList) {
            case "INBOX" -> {
                this.clientModel.resetCounterNewEmail();
                this.emailListView.setItems(clientModel.getInboxEmails());
            }
            case "SENT" -> this.emailListView.setItems(clientModel.getSentEmails());
            default -> System.err.println("Error");
        }

        this.emailListView.setCellFactory(listView -> new EmailListElement(contentPanel, clientModel));
    }

    /**
     * Sets the client model and the email list view.
     * @param model model used by the controller.
     */
    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw  new IllegalArgumentException("Model cannot be null and it must be a Client instance");

        this.clientModel = (Client) model;
        setEmailListView();
    }

    /**
     * Sets the typeOfList as extraArg.
     * @param extraArgs extra arguments to be used by the controller.
     */
    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("ExtraArgs cannot be null and it must be a String instance");

        this.typeOfList = (String) extraArgs;
    }

    /**
     * Sets the panel used for attaching graphical components.
     * @param contentPanel border pane.
     */
    @Override
    public void setContentPanel(BorderPane contentPanel) {
        if (contentPanel == null)
            throw new IllegalArgumentException("contentPanel cannot be null");

        this.contentPanel = contentPanel;
    }
}

