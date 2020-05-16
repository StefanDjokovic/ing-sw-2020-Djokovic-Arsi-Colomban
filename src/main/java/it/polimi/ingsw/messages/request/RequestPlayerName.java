package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestPlayerName extends Request {

    public RequestPlayerName(String numberPlayer) {
        initial = '*';
        message = "Required " + numberPlayer + "'s Name";
    }

    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.getPlayerInfo();
    }

}
