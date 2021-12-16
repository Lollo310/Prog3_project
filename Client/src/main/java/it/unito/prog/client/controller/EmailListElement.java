package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import it.unito.prog.client.view.ClientApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class EmailListElement extends ListCell<Email> {
    private Node user;
    private String mail;
    private String date;

    //https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/#select
    //https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
    //https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
    @Override
    public void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);

        if (email == null || empty) {
            setText(null);
            setGraphic(null);
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-list-element-view.fxml"));
                EmailListElementController controller  = loader.getController();

                controller.setModel(email);
                setGraphic(loader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
