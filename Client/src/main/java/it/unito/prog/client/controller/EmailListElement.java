package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.scene.control.ListCell;

public class EmailListElement extends ListCell<Email> {
    @Override
    protected void updateItem(Email email, boolean b) {
        super.updateItem(email, b);

        if (email != null) {
            EmailListElementController element = new EmailListElementController();
            element.setModel(email);
            setGraphic(element.getEmailListElement());
        }
    }
}
