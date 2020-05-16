package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.board.Board;

public class RequestUpdateBoardView extends Request {

    //private BoardView boardView;


    public RequestUpdateBoardView(BoardView boardView, char initial) {
        this.initial = initial;
        this.boardView = boardView;
        this.isAsync = true;
    }

    public RequestUpdateBoardView(Board board, char initial) {
        this.initial = initial;
        //this.boardView = new BoardView(board);
        this.message = "Update boardView";
    }

    @Override
    public void accept(clientCLI clientCLI) {
        printMessage();
        clientCLI.updateBoardView(boardView);
    }
}
