package it.polimi.ingsw.client.view;

import javafx.application.Application;

public class ThreadGUI extends Thread{
    public void run() {
        Application.launch(clientGUI.class);
    }
}
