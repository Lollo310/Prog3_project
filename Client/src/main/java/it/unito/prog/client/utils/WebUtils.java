package it.unito.prog.client.utils;

import it.unito.prog.client.models.Email;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class WebUtils {

    private static int port = 8189; //non so se va bene

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

    public static void deleteMessage(Email email) throws IOException {
        Socket server = online ? connect() : null;

        if (server != null) {
            ObjectOutputStream outputStream = new ObjectOutputStream(server.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());

            outputStream.writeUTF(""); //stessa cosa di sopra
            outputStream.flush();
            outputStream.writeObject(email);
            outputStream.flush();
            //stessa cosa si sopra

            inputStream.close();
            outputStream.close();
            server.close();
            //return
        }

        //return msg
    }
}
