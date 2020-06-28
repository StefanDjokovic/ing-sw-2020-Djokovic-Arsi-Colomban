package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;

public class RequestPlayerGod extends Request {

    private ArrayList<String> options;
    private int nPicks;
    private boolean multipleDecision;

    public RequestPlayerGod(char initial, int nPicks, ArrayList<String> options) {
        message = "What God shall you pick?";
        this.initial = initial;
        this.nPicks = nPicks;
        this.options = options;
    }

    public int getNPicks() { return this.nPicks; }


    public char getInitial() { return this.initial; }

    @Override
    public void accept(ClientView clientView) {
        clientView.getPlayerGod(initial, options, nPicks);
    }
}
