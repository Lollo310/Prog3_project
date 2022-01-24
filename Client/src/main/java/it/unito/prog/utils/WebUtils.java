package it.unito.prog.utils;

import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class WebUtils {

    // port used by the server service
    private static final int port = 8189;

    private static boolean online = false;

    /**
     * @return true if the server is online, false otherwise.
     */
    public static boolean isOnline() {
        return online;
    }

    /**
     * Tries to connect the client to the server.
     * @return socket on success, null otherwise.
     */
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

    /**
     * Tries sending an email through the socket to the server.
     * @param email email to be sent.
     * @return code 0 Feedback on success, code -1 Feedback on failure with its respective error msg.
     */
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

    /**
     * Tries deleting an email present in the server.
     * @param email email to be deleted.
     * @return code 0 Feedback on success, code -1 Feedback on failure with its respective error msg.
     */
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

    /**
     * Tries retrieving the Incoming new emails through the socket from the server to update the Inbox.
     * @param user user whose new emails are to be retrieved.
     * @return code 0 Feedback on success, code -1 Feedback on failure with its respective error msg.
     */
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

    /**
     * Tries retrieving the specified email list through the socket from the server.
     * @param user user whose  emails are to be retrieved.
     * @param dir  directory from where the emails are to be retrieved.
     * @return code 0 Feedback on success, code -1 Feedback on failure with its respective error msg.
     */
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
}
