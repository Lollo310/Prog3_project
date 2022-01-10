package it.unito.prog.controllers;

import it.unito.prog.models.Email;
import it.unito.prog.views.ClientApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

public class EmailListElementController implements Controller {

    private Email emailModel;

    private Pane contentPane;

    private String user;

    @FXML
    private Label dateLabel;

    @FXML
    private AnchorPane emailListElement;

    @FXML
    private Label previewLabel;

    @FXML
    private Label userLabel;

    @FXML
    void onEmailListElementMouseClicked() {
        FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource("email-read-view.fxml"));

        try {
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setModel(this.emailModel);
            controller.setExtraArgs(this.user);
            this.contentPane.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public AnchorPane getEmailListElement() {
        return emailListElement;
    }

    private void setLabel() {
        userLabel.setText(this.emailModel.getSender());
        previewLabel.setText(this.emailModel.getObject());
        dateLabel.setText(this.emailModel.getTimestamp());
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Email))
            throw new IllegalArgumentException("models cannot be null and it must be a Email instance");
        this.emailModel = (Email) model;
        setLabel();
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof List) && ((List<?>) extraArgs).isEmpty())
            throw new IllegalArgumentException("extraArgs connot be null and it must be a Pane instance");
        this.contentPane = (Pane) ((List<?>) extraArgs).get(0);
        this.user = (String) ((List<?>) extraArgs).get(1);
    }
}

