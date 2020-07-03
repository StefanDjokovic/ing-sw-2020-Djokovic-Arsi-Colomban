package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

/**
 * Sent by the server when the game ends, makes the client display the game end message
 */
public class RequestGameEnd extends Request {

    private static final long serialVersionUID = 6529685098267757610L;

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
