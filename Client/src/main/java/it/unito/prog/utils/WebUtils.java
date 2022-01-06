package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class WebUtils {

    private static final int port = 8189; //non so se va bene

    private static boolean online = true;

    private static Socket connect() {
        try {
            String address = InetAddress.getLocalHost().getHostName();
            return new Socket(address, port);
        } catch (IOException e) {
            online = false;
            return null;
        }
    }

    public static Feedback sendMessage(Email email) { //gestire eccezione
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = online ? connect() : null;

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("SEND"); //ci servirà per dire al server che operazione deve fare
                outputStream.flush();
                outputStream.writeObject(email);
                outputStream.flush();
                // feedback = inputStream.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return feedback;
    }

    public static Feedback deleteMessage(String user, Email email) {
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = online ? connect() : null;

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("DELETE");
                outputStream.flush();
                outputStream.writeUTF(user);
                outputStream.flush();
                outputStream.writeObject(email);
                outputStream.flush();
                //feedback = inputStream.readObject();
                //stessa cosa si sopra
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return feedback;
    }
}
