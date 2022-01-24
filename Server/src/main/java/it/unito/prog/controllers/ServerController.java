package it.unito.prog.controllers;

import it.unito.prog.models.Server;
import it.unito.prog.service.ServerThread;
import it.unito.prog.utils.Utils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerController {

    private Server serverModel;

    private ExecutorService executorService;

    private ServerSocket serverSocket;

    private static final int port = 8189;

    @FXML
    private ListView<String> logListView;

    /**
     * Initializes the server view by binding the list view and the log then starting the server service.
     */
    @FXML
    void initialize() {
        serverModel = new Server();
        logListView.setItems(serverModel.getLog());
        startServerService();
        serverModel.updateLog("SERVER START - " + Utils.getTimestamp());
    }

    /**
     * Overrides the window closure event.
     */
    public void exitApplication() {
        System.out.println("Shutting down...");

        try {
            serverSocket.close();
            executorService.shutdown();
            Platform.exit();
            System.exit(0);
        } catch (IOException e) {
            //ignored
        }
    }

    /**
     * Launches the execution of the thread which carries out the clients requests handling task.
     */
    private void startServerService() {
        Thread serverService = new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                executorService = Executors.newFixedThreadPool(6);

                while (true) {
                    Socket socket = serverSocket.accept();
                    executorService.execute(new ServerThread(socket, serverModel));
                }
            } catch (IOException e) {
                //ignored
            }
        });

        serverService.setDaemon(true);
        serverService.start();
    }
}
