package it.polimi.ingsw.messages;

import it.polimi.ingsw.client.networkLayer.Client;
import it.polimi.ingsw.client.view.View;

public abstract class Request {

    protected String message;

    public Request() {
        message = "This message should be set by subclasses";
    }

    public void printMessage() {
        System.out.println(message);
    }

    public abstract void accept(View view);


}
