package it.unito.prog.controllers;

import it.unito.prog.models.Client;
import it.unito.prog.service.CheckUpdateTask;
import it.unito.prog.views.ClientApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class ClientController implements Controller{

    private Client clientModel;

    private Map<String, String> views;

    @FXML
    private Label counterLabel;

    @FXML
    private BorderPane contentBorderPane;

    @FXML
    private Label userEmail;

    @FXML
    private Label serverInfoLabel;

    @FXML
    private FontIcon serverInfoIcon;

    /**
     * Handles client navigation by loading the respective views while interacting with the sidebar.
     * @param event used to identify the button that has been interacted with.
     */
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
            controller.setContentPanel(contentBorderPane);
            contentBorderPane.setCenter(panel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the client navigation.
     */
    @FXML
    void initialize() {
        serverInfoLabel.getStyleClass().add("text-danger");
        initRouteMap();
    }

    /**
     * Sets the client model.
     * @param model model used by the controller.
     */
    @Override
    public void setModel(Object model) {
        if (!(model instanceof Client))
            throw new IllegalArgumentException("model cannot be null and it must be a Client instance");

        clientModel = (Client) model;
    }

    /**
     * Doesn't take any border pane since it is itself the border pane used for attaching the other views' components.
     * @param contentPanel border pane.
     */
    @Override
    public void setContentPanel(BorderPane contentPanel) {
        //do nothing
    }

    /**
     * Binds a user's username to the client's instance and starts the checkUpdate routine.
     * @param extraArgs extra arguments to be used by the controller.
     */
    @Override
    public void setExtraArgs(Object extraArgs) {
        if (!(extraArgs instanceof String))
            throw new IllegalArgumentException("extraArgs cannot be null and it must be a String instance");

        clientModel = new Client((String) extraArgs);
        setProperty();
        startCheckUpdate();
    }

    /**
     * Launches a scheduled task thread for checking updates and server connection status periodically
     * by using the CheckUpdateTask.
     */
    private void startCheckUpdate() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new CheckUpdateTask(clientModel), 1000, 5000);
    }

    /**
     * Initializes a hashmap structure for navigating the client by connecting each button to its respective view.
     */
    private void initRouteMap(){
        views = new HashMap<>();
        views.put("composeButton", "email-write-view.fxml");
        views.put("inboxButton", "email-list-view.fxml");
        views.put("sentButton", "email-list-view.fxml");
    }

    /**
     * Binds the view properties to the respective client model properties.
     * The InfoLabelProperty change listener has been rewritten so that it changes colour according to the actual
     * server status.
     */
    private void setProperty() {
        counterLabel.textProperty().bind(clientModel.counterNewEmailProperty());
        userEmail.textProperty().bind(clientModel.userProperty());
        serverInfoLabel.textProperty().bind(clientModel.serverStatusProperty());
        serverInfoLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue))
                if (newValue.equals("Server online")) {
                    serverInfoLabel.getStyleClass().setAll("text-success");
                    serverInfoIcon.getStyleClass().setAll("text-success");
                } else {
                    serverInfoLabel.getStyleClass().setAll("text-danger");
                    serverInfoIcon.getStyleClass().setAll("text-danger");
                }
        });
    }
}
