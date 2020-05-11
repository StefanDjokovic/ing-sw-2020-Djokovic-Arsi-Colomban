package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.server.model.board.Board;

public class RequestUpdateBoardView extends Request {

    private BoardView boardView;

    public RequestUpdateBoardView(BoardView boardView) {
        this.initial = '*';
        this.boardView = boardView;
    }

    public RequestUpdateBoardView(Board board) {
        this.initial = '*';
        this.boardView = new BoardView(board);
        this.message = "Update boardView";
    }

    @Override
    public void accept(View view) {
        view.updateBoardView(boardView);
    }
}
