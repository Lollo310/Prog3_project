package it.unito.prog.client.controller;

import it.unito.prog.client.model.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class EmailWriteController implements Controller {
    private Client clientModel;

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

    @Override
    public void setModel(Object model) {
        if (model == null || !(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null and it must be a Client instance");
        this.clientModel = (Client) model;
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}

