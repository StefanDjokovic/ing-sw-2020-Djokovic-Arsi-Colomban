package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

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

    public int getPosXFrom() {
        return posXFrom;
    }

    public int getPosYFrom() {
        return posYFrom;
    }

    public int getPosXTo() {
        return posXTo;
    }

    public int getPosYTo() {
        return posYTo;
    }

    @Override
    public void act(ControllerView controller) {
        if (posXFrom == -1)
            controller.executePass();
        else
            controller.executeMoveOrBuild(posXFrom, posYFrom, posXTo, posYTo);
    }

}