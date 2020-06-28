package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;

import java.util.ArrayList;

public class AnswerPlayerGod extends Answer {

    private String godName;
    private char initial;
    private boolean multipleDecision;
    private ArrayList<String> godNamesList;

    public AnswerPlayerGod(String godName, char initial) {
        message = "Answer Player's name";
        this.multipleDecision = false;
        this.godName = godName;
        this.initial = initial;
    }

    public AnswerPlayerGod(ArrayList<String> godNamesList, char initial) {
        message = "Answer Player's name";
        this.multipleDecision = true;
        this.godNamesList = godNamesList;
        this.initial = initial;
    }

    public ArrayList<String> getGodNamesList() { return godNamesList; }

    public boolean isMultipleDecision() { return multipleDecision; }

    @Override
    public void act(Controller controller) {
        if (multipleDecision)
            controller.setPlayerGod(godNamesList, initial);
        else
            controller.setPlayerGod(godName, initial);
    }
}
