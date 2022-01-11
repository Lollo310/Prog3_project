package it.unito.prog.utils;

import it.unito.prog.models.Client;
import it.unito.prog.models.Feedback;
import javafx.application.Platform;

import java.util.TimerTask;

public class CheckUpdateTask extends TimerTask {

    private final Client clientModel;

    public CheckUpdateTask(Client clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public void run() {
        Feedback feedback = WebUtils.updateInbox(this.clientModel.getUser());
        System.out.println(feedback);
        System.out.println(WebUtils.isOnline());
        Platform.runLater(() -> clientModel.setServerStatus(WebUtils.isOnline()));
    }
}
