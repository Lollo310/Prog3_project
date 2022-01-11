package it.unito.prog.utils;

import it.unito.prog.controllers.Controller;
import it.unito.prog.controllers.EmailListElementController;
import it.unito.prog.models.Email;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmailListElement extends ListCell<Email> {

    private final Pane contentPanel;

    private final String user;

    public EmailListElement(Pane contentPanel, String user) {
        this.contentPanel = contentPanel;
        this.user = user;
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
                List<Object> args = new ArrayList<>();

                loader.load();
                controller = loader.getController();

                if (!(controller instanceof EmailListElementController))
                    throw new RuntimeException("unexpected return value");

                controller.setModel(email);
                args.add(this.contentPanel);
                args.add(this.user);
                controller.setExtraArgs(args);
                setGraphic(((EmailListElementController) controller).getEmailListElement());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
