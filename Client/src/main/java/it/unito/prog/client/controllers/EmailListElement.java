package it.unito.prog.client.controllers;

import it.unito.prog.client.models.Email;
import it.unito.prog.client.views.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class EmailListElement extends ListCell<Email> {
    private Pane contentPanel;

    public EmailListElement(Pane contentPanel) {
        this.contentPanel = contentPanel;
    }

    @Override
    protected void updateItem(Email email, boolean b) {
        super.updateItem(email, b);

        if (email != null) {
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-list-element-view.fxml"));

            try {
                Controller controller;

                loader.load();
                controller = loader.getController();

                if (controller == null || !(controller instanceof EmailListElementController))
                    throw new RuntimeException("unexpected return value");

                controller.setModel(email);
                controller.setExtraArgs(this.contentPanel);
                setGraphic(((EmailListElementController) controller).getEmailListElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
