package it.polimi.ingsw.messages;


import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.server.controller.Controller;

import java.io.Serializable;

public abstract class Answer implements Serializable {

    protected String message;
    public char initial = '*';

    public void printMessage() {
        message = "This string should have been overridden";
    }

    public abstract void act(Controller controller);

    public void setInitial(char initial) {
        this.initial = initial;
    }

}