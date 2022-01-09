package it.unito.prog.controllers;

import it.unito.prog.models.Server;
import it.unito.prog.server.ServerThread;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    private Server serverModel;

    private static final int port = 8189;

    @FXML
    private ListView<String> logListView;

    @FXML
    void initialize() {
        serverModel = new Server();
        logListView.setItems(serverModel.getLog());
        startServerService();
    }


    public void exitApplication() {
        System.out.println("Server close");
        Platform.exit();
    }

    private void startServerService() {
        Thread serverService = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                ExecutorService executorService = Executors.newFixedThreadPool(6);

                while (true) {
                    Socket socket = serverSocket.accept();
                    executorService.execute(new ServerThread(socket, serverModel));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverService.setDaemon(true);
        serverService.start();
    }
}
