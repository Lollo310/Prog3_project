package it.unito.prog.server;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.models.Server;
import it.unito.prog.utils.Utils;
import javafx.application.Platform;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
                    outputStream.writeObject(new Feedback(0, "Send success!"));
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

                    outputStream.writeObject(new Feedback(0, "Delete success"));
                    outputStream.flush();
                }

                case "UPDATE" -> {
                    String user = inputStream.readUTF();

                    Platform.runLater(() -> serverModel.updateLog("[UPDATE] "
                            + user
                            + " - "
                            + Utils.getTimestamp()
                    ));

                    outputStream.writeObject(new Feedback(0, "Update success"));
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

                    List<Email> emails = new ArrayList<>();
                    emails.add(new Email("elisali.perottino@edu.unito.it", "michele.lorenzo@edu.unito.it; foca.grassa@unito.it; ciao@cio.it", "Ciao finocchia", "<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p style=\"text-align: center;\"><span style=\"font-family: &quot;&quot;;\">csalkcnlsan</span></p></body></html>"));
                    outputStream.writeObject(new Feedback(0, "Load success", emails));
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
