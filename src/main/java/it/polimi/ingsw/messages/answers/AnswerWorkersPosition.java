package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;

public class AnswerWorkersPosition extends Answer {

    private int x;
    private int y;
    private char initial;

    public AnswerWorkersPosition(int x, int y, char initial) {
        message = "Answer Worker Position: ";
        this.x = x;
        this.y = y;
        this.initial = initial;
    }

    @Override
    public void act(Controller controller) {
        controller.setWorker(x, y, initial);
    }
}
