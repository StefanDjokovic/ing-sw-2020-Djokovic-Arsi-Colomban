package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.BoardView;

public class RequestUpdateBoardView extends Request {

    public RequestUpdateBoardView(BoardView boardView, char initial) {
        this.initial = initial;
        this.boardView = boardView;
        this.isAsync = true;
    }

    @Override
    public void accept(clientCLI clientCLI) {
        printMessage();
        clientCLI.updateBoardView(boardView);
    }
}
