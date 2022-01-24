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

    private final FileManager fileManager;

    public ServerThread(Socket socket, Server serverModel, FileManager fileManager) {
        this.socket = socket;
        this.serverModel = serverModel;
        this.fileManager = fileManager;
    }

    /**
     * Calls the right method according to the request.
     */
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

    /**
     * Calls the FileManager send method to make the operation effects persistent.
     * Updates the server log then writes a Feedback on the output stream which can be:
     * - code 0 on success, containing the updated email
     * - code -1 on failure, with its respective error msg
     * @param outputStream socket output stream.
     * @param inputStream socket input stream.
     * @throws IOException eventually caused by writes on the outputStream.
     * @throws ClassNotFoundException eventually caused by readObject.
     */
    private void send(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        Email email = (Email) inputStream.readObject();
        Feedback feedback = fileManager.sendEmail(email);

        updateServerLog( feedback.getId() == 0
                ? "[SEND] FROM " + email.getSender() + " TO " + email.getReceivers() + " - " + Utils.getTimestamp()
                : "[ERROR] " + feedback.getMsg() + " - " + Utils.getTimestamp()
        );

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    /**
     * Calls the FileManager delete method to make the operation effects persistent.
     * Updates the server log then writes a Feedback on the output stream.
     * @param outputStream socket output stream.
     * @param inputStream socket input stream.
     * @throws IOException eventually caused by writes on the outputStream.
     * @throws ClassNotFoundException eventually caused by readObject.
     */
    private void delete(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        String user = inputStream.readUTF();
        Email email = (Email) inputStream.readObject();
        Feedback feedback = fileManager.deleteEmail(email, user);


        updateServerLog( feedback.getId() == 0
                ? "[DELETE] FROM " + user + " WITH EMAIL ID " + email.getId() + " - " + Utils.getTimestamp()
                : "[ERROR] " + feedback.getMsg() + " - " + Utils.getTimestamp()
        );

        outputStream.writeObject(feedback);
        outputStream.flush();
    }

    /**
     * Calls the FileManager updateInbox method to make the operation effects persistent.
     * Updates the server log then writes a Feedback on the output stream which can be:
     * - code 0 on success, containing the new emails list
     * - code -1 on failure, with its respective error msg
     * @param outputStream socket output stream.
     * @param inputStream socket input stream.
     * @throws IOException eventually caused by writes on the outputStream.
     */
    private void update(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        String user = inputStream.readUTF();
        Feedback feedback = fileManager.updateInbox(user);

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

    /**
     * Calls the FileManager load method to make the operation effects persistent.
     * Updates the server log then writes a Feedback on the output stream which can be:
     * - code 0 on success, containing the specified emails list
     * - code -1 on failure, with its respective error msg
     * @param outputStream socket output stream.
     * @param inputStream socket input stream.
     * @throws IOException eventually caused by writes on the outputStream.
     */
    private void load(ObjectOutputStream outputStream, ObjectInputStream inputStream) throws IOException {
        String user = inputStream.readUTF();
        String dir = inputStream.readUTF();
        Feedback feedback = fileManager.getEmailList(user, dir);

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
