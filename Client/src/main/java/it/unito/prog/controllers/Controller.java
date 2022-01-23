package it.unito.prog.controllers;

import javafx.scene.layout.BorderPane;

public interface Controller {
    /**
     * Sets the controller's model.
     * @param model model used by the controller.
     */
    void setModel(Object model);

    /**
     * Allows to pass extra arguments, such as models, to the controller.
     * @param extraArgs extra arguments to be used by the controller.
     */
    void setExtraArgs(Object extraArgs);

    /**
     * Sets the panel used for attaching graphical components.
     * @param contentPanel border pane.
     */
    void setContentPanel(BorderPane contentPanel);
}
