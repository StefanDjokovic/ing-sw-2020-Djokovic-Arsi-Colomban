package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.client.view.ClientCLI;

public class RequestDisplayBoard extends Request {

    private RequestUpdateBoardView requestUpdateBoardViewBoardView = null;

    public RequestDisplayBoard(char initial) {
        this.initial = initial;
        message = "Board display";
        this.isAsync = true;
    }

    public RequestDisplayBoard(char initial, RequestUpdateBoardView requestUpdateBoardView) {
        this.requestUpdateBoardViewBoardView = requestUpdateBoardView;
        this.initial = initial;
        message = "Board display";
        this.isAsync = true;
    }

    @Override
    public void accept(ClientCLI clientCLI) {
        if (requestUpdateBoardViewBoardView != null)
            requestUpdateBoardViewBoardView.accept(clientCLI);
        clientCLI.displayBoard();
    }
}
