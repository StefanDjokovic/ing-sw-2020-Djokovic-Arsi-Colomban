package it.polimi.ingsw.messages.answers;


import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;

public class AnswerPlayerName extends Answer {

    String string;

    public AnswerPlayerName(String string) {
        message = "Answer Player's name";
        this.string = string;
    }


    @Override
    public void act(Controller controller) {
        //controller.initPlayer(string);
    }

    public String getString() {
        return this.string;
    }
}