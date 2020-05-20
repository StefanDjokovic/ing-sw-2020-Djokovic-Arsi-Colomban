package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestWaitOpponentMove extends Request {

    public RequestWaitOpponentMove() {
        this.message = "Waiting Opponents' move";
        this.isAsync = true;
    }



    @Override
    public void accept(ClientCLI clientCLI) {
        clientCLI.waitingOpponent();
    }

    public char getInitial() { return this.initial; }
}
