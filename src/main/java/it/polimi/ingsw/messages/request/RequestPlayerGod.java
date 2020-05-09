package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.View;

import java.util.ArrayList;

public class RequestPlayerGod extends Request {

    char initial;
    ArrayList<String> options;

    public RequestPlayerGod(char initial, ArrayList<String> options) {
        message = "What God shall you pick?";
        this.initial = initial;
        this.options = options;
    }

    @Override
    public void accept(View view) {
        view.getPlayerGod(initial, options);
    }

    public char getInitial() { return this.initial; }
}
