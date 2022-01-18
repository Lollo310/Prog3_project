package it.unito.prog.utils;

import it.unito.prog.controllers.Controller;
import it.unito.prog.controllers.EmailListElementController;
import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailListElement extends ListCell<Email> {

    private final BorderPane contentPanel;

    private final Client clientModel;

    public EmailListElement(BorderPane contentPanel, Client clientModel) {
        this.contentPanel = contentPanel;
        this.clientModel = clientModel;
    }

    @Override
    protected void updateItem(Email email, boolean b) {
        super.updateItem(email, b);

        if (email != null) {
            FXMLLoader loader = new FXMLLoader(
                    ClientApplication.class.getResource("email-list-element-view.fxml")
            );

            try {
                Controller controller;

                loader.load();
                controller = loader.getController();

                if (!(controller instanceof EmailListElementController))
                    throw new RuntimeException("unexpected return value");

                controller.setModel(email);
                controller.setExtraArgs(this.clientModel);
                controller.setContentPanel(this.contentPanel);
                setGraphic(((EmailListElementController) controller).getEmailListElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
