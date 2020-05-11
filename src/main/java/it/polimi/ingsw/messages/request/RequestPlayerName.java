package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.View;

public class RequestPlayerName extends Request {

    public RequestPlayerName(String numberPlayer) {
        initial = '*';
        message = "Required " + numberPlayer + "'s Name";
    }

    @Override
    public void accept(View view) {
        view.getPlayerInfo();
    }

}
