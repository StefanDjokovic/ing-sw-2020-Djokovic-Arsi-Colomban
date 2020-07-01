package it.polimi.ingsw.messages;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.ControllerView;

import java.io.Serializable;

public abstract class Answer implements Serializable {

    protected String message;
    public char initial = '*';

    public void printMessage() {
        System.out.println(message);
    }

    public abstract void act(ControllerView controller);

    public void setInitial(char initial) {
        this.initial = initial;
    }

}