package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;

public class AnswerKillPlayer extends Answer {

    char initial;

    public AnswerKillPlayer(char initial) {
        message = "This client was killed";
        this.initial = initial;
    }

    @Override
    public void act(Controller controller) {
        controller.killPlayer(initial);
    }
}
