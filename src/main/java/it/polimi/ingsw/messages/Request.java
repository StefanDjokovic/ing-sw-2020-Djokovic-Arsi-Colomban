package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.client.view.View;

import java.io.Serializable;

public abstract class Request implements Serializable {

    protected String message;
    protected char initial;

    public Request() {
        message = "This message should be set by subclasses";
    }

    public void printMessage() {
        System.out.println(message);
    }

    public abstract void accept(View view);

    public char getInitial() {
        return this.initial;
    }
}
