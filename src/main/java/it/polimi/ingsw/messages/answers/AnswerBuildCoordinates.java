package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

public class AnswerBuildCoordinates extends Answer {

    Worker worker;
    int x;
    int y;

    public AnswerBuildCoordinates(int x, int y, Worker worker) {
        message = "Answer build coordinates";
        this.worker = worker;
    }


    @Override
    public void act(Controller controller) {
      //  controller.build(x, y, worker);
    }

}
