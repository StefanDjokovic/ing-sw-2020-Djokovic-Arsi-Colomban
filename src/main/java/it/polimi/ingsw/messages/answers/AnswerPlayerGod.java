package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;

public class AnswerPlayerGod extends Answer {

    String godName;
    char initial;

    public AnswerPlayerGod(String godName, char initial) {
        System.out.println("HERE IT IS!");
        message = "Answer Player's name";
        this.godName = godName;
        this.initial = initial;
    }

    @Override
    public void act(Controller controller) {
        controller.setPlayerGod(godName, initial);
    }

    public String getGodName() {
        return this.godName;
    }
}
