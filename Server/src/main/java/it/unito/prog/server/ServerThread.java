package it.unito.prog.server;

import it.unito.prog.models.Email;
import it.unito.prog.models.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerThread implements Runnable{

    private Socket socket;

    private Server severModel;

    public ServerThread(Socket socket, Server serverModel) {
        this.socket = socket;
        this.severModel = serverModel;
    }

    @Override
    public void run() {
        ObjectInputStream inputStream = null;
        ObjectOutputStream outputStream = null;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());

            switch (inputStream.readUTF()) {
                case "SEND":
                    System.out.println(inputStream.readObject());
                    break;
                case "DELETE":
                    System.out.println("delete okay!");
                    break;
                default:
                    break;
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
