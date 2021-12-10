module it.unito.prog.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports it.unito.prog.client.view;
    opens it.unito.prog.client.view to javafx.fxml;
    exports it.unito.prog.client.controller;
    opens it.unito.prog.client.controller to javafx.fxml;
}