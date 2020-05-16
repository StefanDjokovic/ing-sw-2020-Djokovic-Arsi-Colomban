package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.clientCLI;

public class RequestDisplayBoard extends Request {

    public RequestDisplayBoard(char initial) {
        this.initial = initial;
        message = "Board display";
        this.isAsync = true;
    }

    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.printSelectableBoard(null);
    }
}
