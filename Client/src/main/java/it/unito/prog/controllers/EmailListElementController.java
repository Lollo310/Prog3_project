package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class EmailListElementController implements Controller {

    private Email emailModel;

    private BorderPane contentPanel;

    private Client clientModel;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    /**
     * Specifies the list view's cells behaviour on click.
     * Allows to visualize the email content when double-clicking on the list's emails.
     */
    @FXML
    void onEmailListElementMouseClicked() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-read-view.fxml"));

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setExtraArgs(clientModel);
            controller.setModel(emailModel);
            controller.setContentPanel(contentPanel);
            contentPanel.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the anchor pane containing the list view's elements.
     */
    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    /**
     * Sets the email's preview in the list view's cell.
     */
    private void setLabel() {
        userLabel.setText(emailModel.getSender());
        previewLabel.setText(emailModel.getSubject());
        dateLabel.setText(emailModel.getTimestamp());
    }

    /**
     * Takes an email as model and sets its labels accordingly.
     * @param model model used by the controller.
     */
    @Override
    public void setModel(Object model) {
        if (!(model instanceof Email))
            throw new IllegalArgumentException("models cannot be null and it must be a Email instance");

        emailModel = (Email) model;
        setLabel();
    }

    /**
     * Takes the client model as extra arg to pass to the other controllers.
     * @param extraArgs extra arguments to be used by the controller.
     */
    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof Client))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a Client instance");

        clientModel = (Client)  extraArgs;
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
}

