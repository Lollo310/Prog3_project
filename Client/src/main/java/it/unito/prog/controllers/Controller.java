package it.unito.prog.controllers;

import javafx.scene.layout.BorderPane;

public interface Controller {
    void setModel(Object model);
    void setExtraArgs(Object extraArgs);
    void setContentPanel(BorderPane contentPanel);
}
