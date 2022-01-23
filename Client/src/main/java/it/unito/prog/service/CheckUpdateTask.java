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

    private boolean isLoaded;

    public CheckUpdateTask(Client clientModel) {
        this.clientModel = clientModel;
        isLoaded = false;
    }

    /**
     * Checks the server status.
     * If the server is online and isLoaded == false, calls the loadEmail method and checks if there are new emails.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void run() {
        if (!isLoaded)
            isLoaded = loadEmail();

        Feedback feedback = WebUtils.updateInbox(this.clientModel.getUser());
        Platform.runLater(() -> clientModel.setServerStatus(WebUtils.isOnline()));

        if (feedback.getId() == 0)
            Platform.runLater(() -> clientModel.addInboxEmails((List<Email>) feedback.getResult()));
    }

    /**
     * Tries loading the emails from the server to the Inbox and Sent lists.
     * @return true on success, false otherwise.
     */
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
