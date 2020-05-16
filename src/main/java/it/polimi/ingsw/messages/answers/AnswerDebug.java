package it.polimi.ingsw.messages.answers;


import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;

public class AnswerDebug extends Answer {


    public AnswerDebug() {
        System.out.println("HERE IT IS!");
        message = "DEBUG";
    }

    @Override
    public void act(Controller controller) {
        controller.debugMessage();
    }

}
