package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;

public class RequestPlayerGod extends Request {

    char initial;

    public RequestPlayerGod(char initial) {
        message = "What God shall you pick?";
        this.initial = initial;
    }

    @Override
    public void accept(View view) {
        view.getPlayerGod(initial);
    }
}
