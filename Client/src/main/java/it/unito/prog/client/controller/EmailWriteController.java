package it.unito.prog.client.controller;

import it.unito.prog.client.view.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class EmailWriteController {

    @FXML
    private HTMLEditor messageHTMLEditor;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    void onSendButtonAction(ActionEvent event) {
        //update timestamp
        System.out.println("[EmailWrite] send button.");
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {

        System.out.println("[EmailWrite] delete button.");
    }

    @FXML
    void initialize() {
        System.out.println("[EmailWrite] init().");
    }

}

