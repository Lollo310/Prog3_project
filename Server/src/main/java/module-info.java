module it.unito.prog {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports it.unito.prog.controllers;
    opens it.unito.prog.controllers to javafx.fxml;
    exports it.unito.prog.views;
    opens it.unito.prog.views to javafx.fxml;
}