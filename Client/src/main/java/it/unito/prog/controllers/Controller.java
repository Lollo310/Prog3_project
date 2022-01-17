package it.unito.prog.controllers;

import javafx.scene.Node;

public interface Controller {
    void setModel(Object model);
    void setExtraArgs(Object extraArgs);
    void setContentPanel(Node contentPanel);
}
