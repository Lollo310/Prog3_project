module it.unito.prog.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports it.unito.prog.server.controllers;
    opens it.unito.prog.server.controllers to javafx.fxml;
    exports it.unito.prog.server.views;
    opens it.unito.prog.server.views to javafx.fxml;
}