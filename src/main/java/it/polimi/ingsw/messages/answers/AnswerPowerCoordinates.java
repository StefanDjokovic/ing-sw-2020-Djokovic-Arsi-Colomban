package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.model.player.Worker;

public class AnswerPowerCoordinates extends Answer {

    private int powerIndex;
    private Worker worker;
    private int x;
    private int y;

    public AnswerPowerCoordinates(int x, int y, Worker worker, int powerIndex) {
        message = "Answer move coordinates";
        this.worker = worker;
        this.x = x;
        this.y = y;
        this.powerIndex = powerIndex;
    }


    @Override
    public void act(Controller controller) {
        controller.executePower(x, y, worker, powerIndex);
    }

}