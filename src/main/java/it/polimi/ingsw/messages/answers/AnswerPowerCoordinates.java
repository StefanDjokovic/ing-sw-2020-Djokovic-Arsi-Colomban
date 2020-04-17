package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.model.player.Worker;

public class AnswerPowerCoordinates extends Answer {

    private int posXFrom;
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


    @Override
    public void act(Controller controller) {
        controller.executePower(posXFrom, posYFrom, posXTo, posYTo);
    }

}