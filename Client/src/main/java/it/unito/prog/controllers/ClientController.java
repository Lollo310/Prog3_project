package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.views.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ClientController implements Controller{
    private Client clientModel;
    private Map<String, String> views;

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Label userEmail;

    @FXML
    void onVBoxButtonAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource(views.get(node.getId())));
            Node panel = loader.load();
            Controller controller = loader.getController();

            controller.setModel(this.clientModel);
            contentPanel.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        this.clientModel = new Client("michele.lorenzo@edu.unito.it");

        /* for future testing
            List<String> rec = new ArrayList<>();
            rec.add("x@y.z");
            Email test = new Email("me", rec, "test mail", "hello it's me", 124L);
            clientModel.getInbox().add(test);
        */

        initRouteMap();
        userEmail.textProperty().bind(clientModel.userProperty());
    }

    private void initRouteMap(){
        views = new HashMap<>();
        views.put("composeButton", "email-write-view.fxml");
        views.put("inboxButton", "email-list-view.fxml");
        views.put("sentButton", "email-list-view.fxml");
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("Model cannot be null and it must be a Client instance");

        this.clientModel = (Client) model;
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        //do nothing
    }
}
