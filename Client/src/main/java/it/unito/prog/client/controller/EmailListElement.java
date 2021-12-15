package it.unito.prog.client.controller;

import it.unito.prog.client.model.Email;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class EmailListElement extends ListCell<Email> {
    private Parent elementRoot;
    private Node user;
    private String mail;
    private String date;

    //https://examples.javacodegeeks.com/desktop-java/javafx/listview-javafx/javafx-listview-example/#select
    //https://stackoverflow.com/questions/9722418/how-to-handle-listview-item-clicked-action
    @Override
    public void updateItem(Email email, boolean empty) {
        super.updateItem(email, empty);

        if (email == null || empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("email-list.element-view.fxml"));
            //loader.setController(this) qui o altrove?
            try {
                System.out.println("[email-list-element-view] loading... ");
                elementRoot = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            user = elementRoot.lookup("#userLabel");


        }

        setGraphic(null);
    }
}
