package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestWaitOpponentMove extends Request {

    public RequestWaitOpponentMove() {
        this.message = "Need worker position";
        this.isAsync = true;
    }



    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.waitingOpponent();
    }

    public char getInitial() { return this.initial; }
}
