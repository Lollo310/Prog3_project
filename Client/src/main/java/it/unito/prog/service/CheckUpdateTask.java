package it.unito.prog.service;

import it.unito.prog.models.Client;
import it.unito.prog.models.Email;
import it.unito.prog.models.Feedback;
import it.unito.prog.utils.WebUtils;
import javafx.application.Platform;

import java.util.List;
import java.util.TimerTask;

public class CheckUpdateTask extends TimerTask {

    private final Client clientModel;

    private boolean isLoad;

    public CheckUpdateTask(Client clientModel) {
        this.clientModel = clientModel;
        isLoad = false;
    }

    @Override
    public void run() {
        if (!isLoad)
            isLoad = loadEmail();

        Feedback feedback = WebUtils.updateInbox(this.clientModel.getUser());
        System.out.println(feedback);
        System.out.println(WebUtils.isOnline());
        Platform.runLater(() -> clientModel.setServerStatus(WebUtils.isOnline()));
    }

    @SuppressWarnings("unchecked")
    private boolean loadEmail() {
        Feedback feedback = WebUtils.load(clientModel.getUser(), "Inbox");

        if (feedback.getId() < 0 || !(feedback.getResult() instanceof List<?>))
            return false;

        clientModel.setAllInboxEmails((List<Email>) feedback.getResult());
        feedback = WebUtils.load(clientModel.getUser(), "Sent");

        if (feedback.getId() < 0 || !(feedback.getResult() instanceof List<?>))
            return false;

        clientModel.setAllSentEmails((List<Email>) feedback.getResult());
        return true;
    }
}
