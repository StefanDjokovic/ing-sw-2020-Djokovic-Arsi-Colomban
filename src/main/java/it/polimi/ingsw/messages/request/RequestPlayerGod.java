package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;

import java.util.ArrayList;

public class RequestPlayerGod extends Request {

    private ArrayList<String> options;
    private int nPicks;

    public RequestPlayerGod(char initial, int nPicks, ArrayList<String> options) {
        message = "What God shall you pick?";
        this.initial = initial;
        this.nPicks = nPicks;
        this.options = options;
    }


    public char getInitial() { return this.initial; }


    @Override
    public boolean isValidAnswer(Answer answer) {
        try {
            AnswerPlayerGod answerPlayerGod = (AnswerPlayerGod) answer;
            if (answerPlayerGod.isMultipleDecision()) {
                for (String godName: answerPlayerGod.getGodNamesList()) {
                    if (!options.contains(godName)) {
                        return false;
                    }
                    else {
                        options.remove(godName);
                    }
                }
                return true;
            }
            else {
                return options.contains(answerPlayerGod.getGodName());
            }
        }
        catch (java.lang.ClassCastException e) {
            System.out.println("Not the expected Answer type! Will block");
        }
        return false;
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.getPlayerGod(initial, options, nPicks);
    }
}
