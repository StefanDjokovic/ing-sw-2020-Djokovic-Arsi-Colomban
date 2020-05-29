package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.BoardView;

public class RequestUpdateBoardView extends Request {

    public RequestUpdateBoardView(BoardView boardView, char initial) {
        this.initial = initial;
        this.boardView = boardView;
        this.isAsync = true;
        this.message = "Im requesting you to update the Board view";
    }

    @Override
    public void accept(ClientView clientView) {
        printMessage();
        clientView.updateBoardView(boardView);
        clientView.displayBoard();
    }
}
