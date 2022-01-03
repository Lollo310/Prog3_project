package it.unito.prog.utils;

import it.unito.prog.models.Email;

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

    public static void sendMessage(Email email) throws IOException { //gestire eccezione
        Socket server = online ? connect() : null;

        if (server != null) {
            ObjectOutputStream outputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());

            outputStream.writeUTF("SEND"); //ci servirà per dire al server che operazione deve fare
            outputStream.flush();
            outputStream.writeObject(email);
            outputStream.flush();
            //leggere dal server se l'operazione è andata a buon fine

            inputStream.close();
            outputStream.close();
            server.close();
            //ritornare il mesg
        }

        //se offline ritornare msg
    }

    public static void deleteMessage(String user, Email email) {
        Socket server = online ? connect() : null;

        if (server != null) {
            ObjectOutputStream outputStream = null;
            ObjectInputStream inputStream = null;

            try {
                outputStream = new ObjectOutputStream(server.getOutputStream());
                inputStream = new ObjectInputStream(server.getInputStream());
                outputStream.writeUTF("DELETE"); //stessa cosa di sopra
                outputStream.flush();
                outputStream.writeUTF(user);
                //inviare cartella da dove elimare probabilmente basta controllare se chi la invia è lo user (o qui o nel server)
                outputStream.writeObject(email);
                outputStream.flush();
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

            //return
        }

        //return msg
    }
}
