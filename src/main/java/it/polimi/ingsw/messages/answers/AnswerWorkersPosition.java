package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

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
    public void act(ControllerView controller) {
        controller.setWorker(x, y, initial);
    }
}
