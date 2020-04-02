package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.messages.Answer;

public class AnswerPlayerGod extends Answer {

    String godName;
    char initial;

    public AnswerPlayerGod(String godName, char initial) {
        message = "Answer Player's name";
        this.godName = godName;
        this.initial = initial;
    }

    @Override
    public void act(Controller controller) {
        controller.setPlayerGod(godName, initial);
    }
}
