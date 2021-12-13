package it.unito.prog.client.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        System.out.println("Ok!");
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        System.out.println("delete ok!");
    }

    @FXML
    void initialize() {
        System.out.println("Done!");
    }

}

