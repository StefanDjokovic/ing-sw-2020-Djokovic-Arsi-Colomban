package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;

public class RequestKillPlayer extends Request {

    private static final long serialVersionUID = 6529685098267757612L;

    private char loserInitial;

    public RequestKillPlayer(char loserInitial) {
        this.initial = loserInitial;
        this.loserInitial = loserInitial;
        this.message = "F";
        this.isAsync = true;
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.displayGameEnd();
        clientView.updateObservers(new AnswerKillPlayer(loserInitial));
    }
}
