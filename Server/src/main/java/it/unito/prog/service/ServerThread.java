package it.unito.prog.service;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.models.Server;
import it.unito.prog.utils.FileManager;
import it.unito.prog.utils.Utils;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable{

    private final Socket socket;

    private final Server serverModel;

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

                    Platform.runLater(() -> serverModel.updateLog("[SEND] FROM "
                                + email.getSender()
                                + " TO "
                                + email.getReceivers()
                                + " - "
                                + Utils.getTimestamp()
                        )
                    );

                    System.out.println(email);
                    outputStream.writeObject(FileManager.sendEmail(email));
                    outputStream.flush();
                }

                case "DELETE" -> {
                    String user = inputStream.readUTF();
                    Email email = (Email) inputStream.readObject();

                    Platform.runLater(() -> serverModel.updateLog("[DELETE] FROM "
                            + user
                            + " WITH EMAIL ID "
                            + email.getId()
                            + " - "
                            + Utils.getTimestamp()
                    ));

                    outputStream.writeObject(FileManager.deleteEmail(email, user));
                    outputStream.flush();
                }

                case "UPDATE" -> {
                    String user = inputStream.readUTF();
                    Feedback feedback = FileManager.updateInbox(user);

                    if (feedback.getId() == 0) {
                        Platform.runLater(() -> serverModel.updateLog("[UPDATE] "
                                + user
                                + " - "
                                + Utils.getTimestamp()
                        ));
                    }

                    outputStream.writeObject(feedback);
                    outputStream.flush();
                }

                case "LOAD" -> {
                    String user = inputStream.readUTF();
                    String dir = inputStream.readUTF();

                    Platform.runLater(() -> serverModel.updateLog("[LOAD " + dir.toUpperCase()
                            + "] "
                            + user
                            + " - "
                            + Utils.getTimestamp()
                    ));

                    outputStream.writeObject(FileManager.getEmailList(user, dir));
                    outputStream.flush();
                }

                default -> throw new RuntimeException("Ops...Error in switch-case");
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
