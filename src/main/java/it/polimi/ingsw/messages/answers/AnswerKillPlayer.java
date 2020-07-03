package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.ControllerView;

/**
 * Sent when a player needs to be removed from the game
 */
public class AnswerKillPlayer extends Answer {

    private static final long serialVersionUID = 6529685098267757600L;

    private char initial;

    public AnswerKillPlayer(char initial) {
        message = "This client was killed";
        this.initial = initial;
    }

    @Override
    public void act(ControllerView controller) {
        controller.killPlayer(initial);
    }
}
