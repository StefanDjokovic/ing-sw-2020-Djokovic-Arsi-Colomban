package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

/**
 * Server makes client wait for opponent's move
 */
public class RequestWaitOpponentMove extends Request {

    private static final long serialVersionUID = 6529685098267757617L;

    // Note: opponentInitial is at '*' when there is no specified opponent that has to play
    private char opponentInitial;

    public RequestWaitOpponentMove(char opponentInitial) {
        this.message = "Waiting Opponents' move";
        this.opponentInitial = opponentInitial;
        this.isAsync = true;
    }



    @Override
    public void accept(ClientView clientView) {
        clientView.waitingOpponent(opponentInitial);
    }

    public char getInitial() { return this.initial; }
}
