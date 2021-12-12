package it.unito.prog.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class EmailWriteController {

    @FXML
    private Button deleteButton;

    @FXML
    private HTMLEditor messageHTMLEditor;

    @FXML
    private Button sendButton;

    @FXML
    private Label subjectLabel;

    @FXML
    private TextField subjectTextField;

    @FXML
    private Label toLabel;

    @FXML
    private TextField toTextField;

    @FXML
    public void initialize() {
        System.out.println("Done!");
    }

}

