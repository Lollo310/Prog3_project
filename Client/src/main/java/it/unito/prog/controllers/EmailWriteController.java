package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.WebUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import java.io.IOException;

public class EmailWriteController implements Controller {
    private Email emailModel;

    @FXML
    private HTMLEditor messageHTMLEditor;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    void onSendButtonAction(ActionEvent event) {
        //update timestamp
        System.out.println(emailModel);
        try {
            WebUtils.sendMessage(emailModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        System.out.println("[EmailWrite] delete button.");
    }

    @FXML
    void initialize() {
        this.emailModel = new Email();
        this.subjectTextField.textProperty().bindBidirectional(emailModel.objectProperty());
        this.toTextField.textProperty().bindBidirectional(emailModel.receiversProperty());
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null && it must Client instance");

        emailModel.setSender(((Client) model).getUser());
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}
