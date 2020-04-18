package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.board.Board;

public class RequestDisplayBoard extends Request {

    public RequestDisplayBoard() {

    }

    @Override
    public void accept(View view) {
        view.printSelectableBoard(null);
    }
}
