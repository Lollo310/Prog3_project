package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.utils.Utils;
import it.unito.prog.utils.WebUtils;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;

import java.io.IOException;

public class EmailReadController implements Controller {

    private Client clientModel;

    private Email emailModel;

    private BorderPane contentPanel;

    @FXML
    private Button replyAllButton;

    @FXML
    private Button replyButton;

    @FXML
    private TextField datetimeTextField;

    @FXML
    private TextField fromTextField;

    @FXML
    private WebView messageAreaField;

    @FXML
    private TextField subjectTextField;

    @FXML
    private TextField toTextField;

    @FXML
    private Label infoLabel;

    /**
     * Defines the behaviour on email delete.
     * Tries deleting the email from the server.
     * Deletes the email from the client's model's list and updates the view on success.
     * Shows the error on the infoLabel on failure.
     */
    @FXML
    void onDeleteButtonAction() {
        Feedback feedback = WebUtils.deleteMessage(this.clientModel.getUser(), this.emailModel);

        if (feedback.getId() == 0) {
            try {
                FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-list-view.fxml"));
                Node panel = loader.load();
                Controller controller = loader.getController();

                controller.setExtraArgs(
                        this.emailModel.getSender().equals(this.clientModel.getUser()) ? "SENT" : "INBOX"
                );

                controller.setModel(this.clientModel);
                controller.setContentPanel(this.contentPanel);
                this.clientModel.removeEmail(this.emailModel);
                this.contentPanel.setCenter(panel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            infoLabel.setText(feedback.getMsg());
            infoLabel.setVisible(true);
        }
    }

    /**
     * Defines the behaviour on email forward.
     * Initializes the email to forward then loads the writing view.
     */
    @FXML
    void onForwardButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email forwardEmail = new Email(
                this.clientModel.getUser(),
                "",
                "[Forwarded from " + this.emailModel.getSender() + "] " + this.emailModel.getSubject(),
                this.emailModel.getMessage()
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(forwardEmail);
            controller.setModel(this.clientModel);
            this.contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Defines the behaviour on email reply all.
     * Initializes the email to forward then loads the writing view where the to field has been completed with the list
     * of users the email has been received from.
     */
    @FXML
    void onReplyAllButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email replyAllEmail = new Email(
                this.clientModel.getUser(),
                this.emailModel.getSender() + "; " + Utils.filterReceivers(this.clientModel.getUser(), this.emailModel.getReceivers()),
                "[ReplyAll] " + this.emailModel.getSubject(),
                ""
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(replyAllEmail);
            controller.setModel(this.clientModel);
            this.contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Defines the behaviour on email reply.
     * Initializes the email to forward then loads the writing view where the to field has been completed with the email
     * address of the user the email has been received from.
     */
    @FXML
    void onReplyButtonAction() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-write-view.fxml"));
        Email replyAllEmail = new Email(
                this.clientModel.getUser(),
                this.emailModel.getSender(),
                "[Reply] " + this.emailModel.getSubject(),
                ""
        );

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(replyAllEmail);
            controller.setModel(this.clientModel);
            this.contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * On initialization, hides the label used for showing errors.
     */
    @FXML
    void initialize() {
        this.infoLabel.setVisible(false);
    }

    /**
     * Takes an email as model, sets its properties then shows the reply, reply all, forward buttons if the email has
     * been received, only the forward button if the email has been sent.
     * @param model model used by the controller.
     */
    @Override
    public void setModel(Object model) {
        if (!(model instanceof Email))
            throw new IllegalArgumentException("models cannot be null and it must be a Email instance");

        this.emailModel = (Email) model;
        setProperty();

        if (emailModel.getSender().equals(clientModel.getUser())){
            replyButton.setVisible(false);
            replyAllButton.setVisible(false);
        }
    }

    /**
     * Takes the client model as extra arg.
     * @param extraArgs extra arguments to be used by the controller.
     */
    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof Client))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a String instance");

        this.clientModel = (Client) extraArgs;
    }

    /**
     * Sets the panel used for attaching graphical components.
     * @param contentPanel border pane.
     */
    @Override
    public void setContentPanel(BorderPane contentPanel) {
        if (contentPanel == null)
            throw new IllegalArgumentException("contentPanel cannot be null");

        this.contentPanel = contentPanel;
    }

    /**
     * Binds the view properties to the respective email model properties.
     */
    private void setProperty() {
        fromTextField.textProperty().bind(emailModel.senderProperty());
        toTextField.textProperty().bind(emailModel.receiversProperty());
        subjectTextField.textProperty().bind(emailModel.subjectProperty());
        messageAreaField.getEngine().loadContent(emailModel.getMessage());
        datetimeTextField.textProperty().bind(emailModel.timestampProperty());
    }

}


