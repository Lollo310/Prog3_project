package it.unito.prog.views;

import it.unito.prog.controllers.ServerController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Server");
        stage.setMinWidth(400);
        stage.setMinHeight(300);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> ((ServerController) fxmlLoader.getController()).exitApplication());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}