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
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            switch (inputStream.readUTF()) {
                case "SEND" -> send(outputStream, inputStream);
                case "DELETE" -> delete(outputStream, inputStream);
                case "UPDATE" -> update(outputStream, inputStream);
                case "LOAD" -> load(outputStream, inputStream);
                default -> updateServerLog("[FATAL ERROR] Error in Server Thread - " + Utils.getTimestamp());
            }
        } catch (IOException e) {
            updateServerLog("[ERROR] Socket might be closed - " + Utils.getTimestamp());
        } catch (ClassNotFoundException e) {
            updateServerLog("[FATAL ERROR] " + e.getMessage() + " - " + Utils.getTimestamp());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
                socket.close();
            } catch (IOException e) {
                //ignored
            }
        }
    }

    private void send(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        Email email = (Email) inputStream.readObject();
        Feedback feedback = FileManager.sendEmail(email);

        updateServerLog( feedback.getId() == 0
                ? "[SEND] FROM " + email.getSender() + " TO " + email.getReceivers() + " - " + Utils.getTimestamp()
                : "[ERROR] " + feedback.getMsg() + " - " + Utils.getTimestamp()
        );

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    private void delete(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        String user = inputStream.readUTF();
        Email email = (Email) inputStream.readObject();
        Feedback feedback = FileManager.deleteEmail(email, user);


        updateServerLog( feedback.getId() == 0
                ? "[DELETE] FROM " + user + " WITH EMAIL ID " + email.getId() + " - " + Utils.getTimestamp()
                : "[ERROR] " + feedback.getMsg() + " - " + Utils.getTimestamp()
        );

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    private void update(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        String user = inputStream.readUTF();
        Feedback feedback = FileManager.updateInbox(user);

        if (feedback.getId() == 0) {
            updateServerLog("[UPDATE] "
                    + user
                    + " - "
                    + Utils.getTimestamp()
            );
        }

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    private void load(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        String user = inputStream.readUTF();
        String dir = inputStream.readUTF();
        Feedback feedback = FileManager.getEmailList(user, dir);

        updateServerLog( feedback.getId() == 0
                ? "[LOAD " + dir.toUpperCase() + "] " + user + " - " + Utils.getTimestamp()
                : "[ERROR] " + feedback.getMsg() + " - " + Utils.getTimestamp()
        );

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    private void updateServerLog(String msg) {
        Platform.runLater(() -> serverModel.updateLog(msg));
    }
}
