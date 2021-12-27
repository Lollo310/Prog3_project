package it.unito.prog.server.controllers;

import it.unito.prog.server.models.Server;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class ServerController {

    @FXML
    private ListView<String> logListView;

    @FXML
    void initialize() {
        Server serverModel = new Server();
        logListView.setItems(serverModel.getLog());
    }
}
