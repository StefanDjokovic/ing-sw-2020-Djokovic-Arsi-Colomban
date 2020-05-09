package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.View;

public class RequestDisplayBoard extends Request {

    public RequestDisplayBoard() { message = "Board display";}

    @Override
    public void accept(View view) {
        view.printSelectableBoard(null);
    }
}
