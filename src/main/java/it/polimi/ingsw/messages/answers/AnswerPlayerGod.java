package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.ControllerView;

import java.util.ArrayList;

public class AnswerPlayerGod extends Answer {

    private static final long serialVersionUID = 6529685098267757602L;

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

    public String getGodName() { return godName; }

    public ArrayList<String> getGodNamesList() { return godNamesList; }

    public boolean isMultipleDecision() { return multipleDecision; }

    @Override
    public void act(ControllerView controller) {
        if (multipleDecision)
            controller.setPlayerGod(godNamesList, initial);
        else
            controller.setPlayerGod(godName, initial);
    }
}
