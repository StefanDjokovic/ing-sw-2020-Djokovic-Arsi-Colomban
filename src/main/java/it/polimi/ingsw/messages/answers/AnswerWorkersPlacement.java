package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

public class AnswerWorkersPlacement extends Answer {

    private static final long serialVersionUID = 6529685098267757604L;

    private int x;
    private int y;
    private char initial;

    public AnswerWorkersPlacement(int x, int y, char initial) {
        message = "Answer Worker Position: ";
        this.x = x;
        this.y = y;
        this.initial = initial;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    @Override
    public void act(ControllerView controller) {
        controller.setWorker(x, y, initial);
    }
}
