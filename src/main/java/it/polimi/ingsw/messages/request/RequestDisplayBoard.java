package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

public class RequestDisplayBoard extends Request {

    private RequestUpdateBoardView viewBoardView = null;

    public RequestDisplayBoard(char initial) {
        this.initial = initial;
        message = "Board display";
        this.isAsync = true;
    }

    public RequestDisplayBoard(char initial, RequestUpdateBoardView requestUpdateBoardView) {
        this.viewBoardView = requestUpdateBoardView;
        this.initial = initial;
        message = "Board display";
        this.isAsync = true;
    }

    @Override
    public void accept(ClientView clientView) {
        if (viewBoardView != null)
            viewBoardView.accept(clientView);
        clientView.displayBoard();
    }
}
