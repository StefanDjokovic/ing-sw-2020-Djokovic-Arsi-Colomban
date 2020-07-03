package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

public class AnswerPing extends Answer {

    private static final long serialVersionUID = 6529685098267757113L;



    @Override
    public void act(ControllerView controller) {
        this.message = "This is a ping answer";
    }
}
