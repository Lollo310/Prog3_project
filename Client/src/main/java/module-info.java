module it.unito.prog.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.jsoup;

    exports it.unito.prog.client.views;
    opens it.unito.prog.client.views to javafx.fxml;
    exports it.unito.prog.client.controllers;
    opens it.unito.prog.client.controllers to javafx.fxml;
}