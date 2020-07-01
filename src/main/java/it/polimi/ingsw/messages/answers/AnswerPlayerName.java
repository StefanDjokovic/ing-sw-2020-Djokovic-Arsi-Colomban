package it.polimi.ingsw.messages.answers;


import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

public class AnswerPlayerName extends Answer {

    private String name;

    public AnswerPlayerName(String name) {
        message = "Answer Player's name";
        this.name = name;
    }

    @Override
    public void act(ControllerView controller) {
        controller.initPlayer(name);
    }

    public String getString() {
        return this.name;
    }
}