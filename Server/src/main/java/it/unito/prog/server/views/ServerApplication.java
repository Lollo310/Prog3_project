package it.unito.prog.server.views;

import it.unito.prog.server.models.Email;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Server");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    /* serve ficcarlo in un thread e runnarlo dal controller probably
    private static void test() {
        Socket socket = null;
        ObjectInputStream inputStream = null;

        try {
            ServerSocket serverSocket = new ServerSocket(8689);
            socket = serverSocket.accept();
            inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println(inputStream.readUTF());
            System.out.println(inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
}