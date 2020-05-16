package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;

public class AnswerFatalEndGame extends Answer {


    @Override
    public void act(Controller controller) {
        controller.killGame();
    }
}
