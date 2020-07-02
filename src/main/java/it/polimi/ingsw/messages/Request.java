package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.server.model.BoardView;

import java.io.Serializable;

public abstract class Request implements Serializable {

    protected String message;
    protected char initial = '*';
    protected BoardView boardView;
    protected boolean isAsync = false;
    public boolean internal = false;

    public Request() {
        message = "Nananananana batman!";
    }

    public void printMessage() {
        System.out.println(message);
    }

    public abstract void accept(ClientView clientView);

    public char getInitial() {
        return this.initial;
    }

    public String getMessage() {
        return message;
    }
}
