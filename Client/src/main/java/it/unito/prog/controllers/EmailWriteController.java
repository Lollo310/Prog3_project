package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;

public class EmailWriteController implements Controller {

    private Client clientModel;

    private Email emailModel;

    @FXML
    private HTMLEditor messageHTMLEditor;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    private Label infoLabel;

    /**
     * Defines the behaviour on email send.
     * The current time is taken as timestamp and the email is sent.
     * On success, the infoLabel shows that the email has been sent.
     * On failure, an alert shows that the email has not been sent.
     */
    @FXML
    void onSendButtonAction() {
        Feedback feedback;

        emailModel.setTimestamp(Utils.getTimestamp());
        emailModel.setMessage(messageHTMLEditor.getHtmlText());
        feedback = checkData()
                ? WebUtils.sendMessage(emailModel)
                : new Feedback(-1,
                        "Incorrect data format. To and subject fields cannot be empty and receivers format must be correct."
                   );

        if (feedback.getId() == 0) {
            clientModel.addSentEmails((Email) feedback.getResult());
            infoLabel.setText(feedback.getMsg());
            infoLabel.setVisible(true);
        } else
            Utils.showAlert(Alert.AlertType.ERROR, feedback.getMsg());
    }

    /**
     * Defines the behaviour on email clear.
     * Deletes all the current email content.
     */
    @FXML
    void onClearButtonAction() {
        infoLabel.setVisible(false);
        emailModel = new Email();
        emailModel.setMessage(Utils.clearHTML);
        emailModel.setSender(clientModel.getUser());
        setProperty();
    }

    /**
     * Initializes the writing view by hiding the infoLabel and setting and empty email as email model.
     */
    @FXML
    void initialize() {
        infoLabel.setVisible(false);
        this.emailModel = new Email();
        setProperty();
    }

    /**
     * Takes the client as model and sets the client user as email sender.
     * @param model model used by the controller.
     */
    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null && it must Client instance");

        clientModel = (Client) model;
        emailModel.setSender(clientModel.getUser());
    }

    /**
     * Takes the email model in case the view is called by the forward and reply buttons.
     * @param extraArgs extra arguments to be used by the controller.
     */
    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof Email))
            throw new IllegalArgumentException("extraArgs cannot be null && it must Email instance");

        emailModel = (Email) extraArgs;
        setProperty();
    }

    /**
     * Sets the panel used for attaching graphical components.
     * @param contentPanel border pane.
     */
    @Override
    public void setContentPanel(BorderPane contentPanel) {
        //do nothing
    }

    /**
     * Bi-directionally binds the view properties to the respective email model properties.
     */
    private void setProperty() {
        toTextField.textProperty().bindBidirectional(emailModel.receiversProperty());
        subjectTextField.textProperty().bindBidirectional(emailModel.subjectProperty());
        messageHTMLEditor.setHtmlText(emailModel.getMessage());
    }

    /**
     * Checks that the email data is valid.
     * @return true if the data is valid, false otherwise.
     */
    private boolean checkData() {
        String emailRegex = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}";

        if (toTextField.getText() == null
                || subjectTextField.getText() == null
                || toTextField.getText().equals("")
                || subjectTextField.getText().equals("")
        )
            return false;

        return toTextField.getText().matches("(" + emailRegex + "\\s*;\\s*)*(" + emailRegex + "\\s*;?\\s*)");
    }
}

