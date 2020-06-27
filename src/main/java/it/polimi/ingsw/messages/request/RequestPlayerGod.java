package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;

public class RequestPlayerGod extends Request {

    private ArrayList<String> options;

    public RequestPlayerGod(char initial, ArrayList<String> options) {
        System.out.println("GIMME PLAYER GOD");
        message = "What God shall you pick?";
        this.initial = initial;
        this.options = options;
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.getPlayerGod(initial, options);
    }

    public char getInitial() { return this.initial; }
}
