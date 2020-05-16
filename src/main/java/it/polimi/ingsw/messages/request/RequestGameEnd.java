package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestGameEnd extends Request {

    char winnerInitial;

    public RequestGameEnd(char winnerInitial) {
        this.isAsync = true;
        this.winnerInitial = winnerInitial;
        this.message = "END";
    }

    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.displayGameEnd(winnerInitial);
    }
}