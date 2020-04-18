package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.model.player.Worker;

public class AnswerPowerCoordinates extends Answer {

    private int posXFrom = -1;
    private int posYFrom;
    private int posXTo;
    private int posYTo;

    public AnswerPowerCoordinates(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        message = "Answer move coordinates";
        this.posXFrom = posXFrom;
        this.posYFrom = posYFrom;
        this.posXTo = posXTo;
        this.posYTo = posYTo;
    }

    public AnswerPowerCoordinates() {
        message = "Answer move coordinates";
    }


    @Override
    public void act(Controller controller) {
        if (posXFrom == -1)
            controller.executePower();
        else
            controller.executePower(posXFrom, posYFrom, posXTo, posYTo);
    }

}