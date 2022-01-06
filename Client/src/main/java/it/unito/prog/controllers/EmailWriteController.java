package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.utils.WebUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

public class EmailWriteController implements Controller {

    private Email emailModel;

    @FXML
    private HTMLEditor messageHTMLEditor;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    void onSendButtonAction() {
        //update timestamp
        emailModel.setMessage(messageHTMLEditor.getHtmlText());
        System.out.println(emailModel);
        WebUtils.sendMessage(emailModel);
    }

    @FXML
    void onDeleteButtonAction() {
        System.out.println("[EmailWrite] delete button.");
    }

    @FXML
    void initialize() {
        this.emailModel = new Email();
        setProperty();
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null && it must Client instance");

        emailModel.setSender(((Client) model).getUser());
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof Email))
            throw new IllegalArgumentException("extraArgs cannot be null && it must Email instance");

        emailModel = (Email) extraArgs;
        setProperty();
    }

    private void setProperty() {
        subjectTextField.textProperty().bindBidirectional(emailModel.objectProperty());
        toTextField.textProperty().bindBidirectional(emailModel.receiversProperty());
        messageHTMLEditor.setHtmlText(emailModel.getMessage());
    }
}

