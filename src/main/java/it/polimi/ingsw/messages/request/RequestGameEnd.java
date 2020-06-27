package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

public class RequestGameEnd extends Request {

    private char winnerInitial;

    public RequestGameEnd(char winnerInitial) {
        this.isAsync = true;
        this.winnerInitial = winnerInitial;
        this.message = "END";
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.displayGameEnd(winnerInitial);
    }
}
