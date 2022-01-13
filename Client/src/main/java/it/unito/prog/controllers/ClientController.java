package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.service.CheckUpdateTask;
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
import java.util.Timer;

public class ClientController implements Controller{

    private Client clientModel;

    private Map<String, String> views;

    @FXML
    private AnchorPane contentPanel;

    @FXML
    private Label userEmail;

    @FXML
    private Label serverInfoLabel;

    @FXML
    void onVBoxButtonAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            FXMLLoader loader = new FXMLLoader(ClientApplication.class.getResource(views.get(node.getId())));
            Node panel = loader.load();
            Controller controller = loader.getController();

            if (!node.getId().equals("composeButton")) {
                String typeOfList = node.getId().equals("inboxButton") ? "INBOX" : "SENT";

                controller.setExtraArgs(typeOfList);
            }

            controller.setModel(clientModel);
            contentPanel.getChildren().setAll(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        serverInfoLabel.getStyleClass().add("text-danger");
        initRouteMap();
    }

    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null and it must be a Client instance");

        clientModel = (Client) model;
    }

    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a String instance");

        clientModel = new Client((String) extraArgs);
        setProperty();
        startCheckUpdate();
    }

    private void startCheckUpdate() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new CheckUpdateTask(clientModel), 1000, 5000);
    }

    private void initRouteMap(){
        views = new HashMap<>();
        views.put("composeButton", "email-write-view.fxml");
        views.put("inboxButton", "email-list-view.fxml");
        views.put("sentButton", "email-list-view.fxml");
    }

    private void setProperty() {
        userEmail.textProperty().bind(clientModel.userProperty());
        serverInfoLabel.textProperty().bind(clientModel.serverStatusProperty());
        serverInfoLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue))
                if (newValue.equals("Server online"))
                    serverInfoLabel.getStyleClass().setAll("text-success");
                else
                    serverInfoLabel.getStyleClass().setAll("text-danger");
        });
    }
}
