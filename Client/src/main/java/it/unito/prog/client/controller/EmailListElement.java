package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
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
                EmailListElementController controller;

                loader.load();
                controller = loader.getController();
                controller.setEmail(email);
                controller.setContentPane(this.contentPanel);
                setGraphic(controller.getEmailListElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
