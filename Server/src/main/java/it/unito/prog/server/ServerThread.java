package it.unito.prog.server;

import it.unito.prog.models.Email;
import it.unito.prog.models.Server;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable{

    private Socket socket;

    private Server serverModel;

    public ServerThread(Socket socket, Server serverModel) {
        this.socket = socket;
        this.serverModel = serverModel;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            switch (inputStream.readUTF()) {
                case "SEND" -> {
                    Email email = (Email) inputStream.readObject();

                    Platform.runLater(() -> {
                        serverModel.updateLog("[SEND] FROM " + email.getSender() + " TO " + email.getReceivers());
                    });

                    System.out.println(email);
                }
                case "DELETE" -> {
                    String user = inputStream.readUTF();
                    Email email = (Email) inputStream.readObject();

                    Platform.runLater(() -> {
                        serverModel.updateLog("[DELETE] FROM " + user + " WITH EMAIL ID " + email.getId());
                    });

                    //FileManager.deleteEmail(email, user);
                }
                case "LOAD INBOX" -> {
                    System.out.println("Load inbox okay");
                }
                case "LOAD SENT" -> {
                    System.out.println("Load sent okay");
                }
                default -> {
                    System.out.println("Probably is a error");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
