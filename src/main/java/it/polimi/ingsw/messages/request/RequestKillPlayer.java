package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;

public class RequestKillPlayer extends Request {

    private char loserInitial;

    public RequestKillPlayer(char loserInitial) {
        this.initial = loserInitial;
        this.loserInitial = loserInitial;
        this.message = "F";
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.displayGameEnd();
        clientView.updateObservers(new AnswerKillPlayer(loserInitial));
    }
}
