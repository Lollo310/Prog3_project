package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private Label infoLabel;

    @FXML
    void onSendButtonAction() {
        Feedback feedback;

        emailModel.setTimestamp(Utils.getTimestamp());
        emailModel.setMessage(messageHTMLEditor.getHtmlText());
        feedback = checkData()
                ? WebUtils.sendMessage(emailModel)
                : new Feedback(-1, "Incorrect data format. To and subject fields cannot be empty.");

        if (feedback.getId() == 0) {
            infoLabel.setText(feedback.getMsg());
            infoLabel.setVisible(true);
        } else
            Utils.showAlert(feedback.getMsg());
    }

    @FXML
    void onClearButtonAction() {
        infoLabel.setVisible(false);
        emailModel = new Email();
        emailModel.setMessage(Utils.clearHTML);
        setProperty();
    }

    @FXML
    void initialize() {
        infoLabel.setVisible(false);
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
        subjectTextField.textProperty().bindBidirectional(emailModel.subjectProperty());
        toTextField.textProperty().bindBidirectional(emailModel.receiversProperty());
        messageHTMLEditor.setHtmlText(emailModel.getMessage());
    }

    private boolean checkData() {

        if (toTextField.getText() == null
                || subjectTextField.getText() == null
                || toTextField.getText().equals("")
                || subjectTextField.getText().equals("")
        )
            return false;

        return toTextField.getText().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\s*;?\\s*+");
    }
}

