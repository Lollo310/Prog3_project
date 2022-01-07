package it.unito.prog.utils;

import it.unito.prog.models.Client;

import java.util.TimerTask;

public class CheckUpdateTask extends TimerTask {

    private Client clientModel;

    public CheckUpdateTask(Client clientModel) {
        this.clientModel = clientModel;
    }

    @Override
    public void run() {
        System.out.println("Hello I'm the scheduled Thread");
    }
}
