package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.clientCLI;

import java.util.ArrayList;

public class RequestPlayerGod extends Request {

    ArrayList<String> options;

    public RequestPlayerGod(char initial, ArrayList<String> options) {
        System.out.println("GIMME PLAYER GOD");
        message = "What God shall you pick?";
        this.initial = initial;
        this.options = options;
    }

    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.getPlayerGod(initial, options);
    }

    public char getInitial() { return this.initial; }
}
