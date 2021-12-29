package it.unito.prog.server.controllers;

import it.unito.prog.server.models.Server;
import it.unito.prog.server.service.ServerThread;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    public static final int port = 8189;

    @FXML
    private ListView<String> logListView;

    @FXML
    void initialize() {
        Server serverModel = new Server();
        logListView.setItems(serverModel.getLog());
        startServerService();
    }

    private static void startServerService() {
        Thread serverService = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                ExecutorService executorService = Executors.newFixedThreadPool(6);

                while (true) {
                    Socket socket = serverSocket.accept();
                    executorService.execute(new ServerThread(socket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverService.setDaemon(true);
        serverService.start();
    }
}
