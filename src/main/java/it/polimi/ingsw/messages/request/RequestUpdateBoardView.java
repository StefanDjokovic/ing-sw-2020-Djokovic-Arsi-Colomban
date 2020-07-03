package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.BoardView;

/**
 * Server tells the client to updated the board
 */
public class RequestUpdateBoardView extends Request {

    private static final long serialVersionUID = 6529685098267757616L;

    public RequestUpdateBoardView(BoardView boardView, char initial) {
        this.initial = initial;
        this.boardView = boardView;
        this.isAsync = true;
        this.message = "Im requesting you to update the Board view";
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.updateBoardView(boardView);
        clientView.displayBoard();
    }
}
