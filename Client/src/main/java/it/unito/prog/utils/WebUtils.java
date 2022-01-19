package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class WebUtils {

    private static final int port = 8189;

    private static boolean online = false;

    public static boolean isOnline() {
        return online;
    }

    private static Socket connect() {
        try {
            String address = InetAddress.getLocalHost().getHostName();
            Socket server = new Socket(address, port);

            online = true;
            return server;
        } catch (IOException e) {
            online = false;
            return null;
        }
    }

    public static Feedback sendMessage(Email email) {
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = connect();

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("SEND");
                outputStream.flush();
                outputStream.writeObject(email);
                outputStream.flush();
                feedback = (Feedback) inputStream.readObject();
            } catch (IOException e) {
                feedback.setAll(-1, "Send aborted. Connection error.");
            } catch (ClassNotFoundException e) {
                feedback.setAll(-1, "Fatal error: " + e.getMessage());
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                    server.close();
                } catch (IOException e) {
                    //ignored
                }
            }
        }

        return feedback;
    }

    public static Feedback deleteMessage(String user, Email email) {
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = connect();

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
                feedback = (Feedback) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                feedback.setAll(-1, e.getMessage());
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                    if (outputStream != null) outputStream.close();
                    server.close();
                } catch (IOException e) {
                    feedback.setAll(-1, e.getMessage());
                }
            }
        }

        return feedback;
    }

    public static Feedback updateInbox(String user) {
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = connect();

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("UPDATE");
                outputStream.flush();
                outputStream.writeUTF(user);
                outputStream.flush();
                feedback = (Feedback) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
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

    public static Feedback load(String user, String dir) {
        Feedback feedback = new Feedback(-1, "Server offline");
        Socket server = connect();

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("LOAD");
                outputStream.flush();
                outputStream.writeUTF(user);
                outputStream.flush();
                outputStream.writeUTF(dir);
                outputStream.flush();
                feedback = (Feedback) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
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
