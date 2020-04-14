package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

public class AnswerMoveCoordinates extends Answer {

    Worker worker;
    int x;
    int y;

    public AnswerMoveCoordinates(int x, int y, Worker worker) {
        message = "Answer move coordinates";
        this.worker = worker;
    }


    @Override
    public void act(Controller controller) {
        controller.move(x, y, worker);
    }

}
