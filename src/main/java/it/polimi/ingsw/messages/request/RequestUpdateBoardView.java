package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.model.BoardView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.board.Board;

public class RequestUpdateBoardView extends Request {

    private BoardView boardView;

    public RequestUpdateBoardView(BoardView boardView) {
        this.boardView = boardView;
    }

    public RequestUpdateBoardView(Board board) {
        this.boardView = new BoardView(board);
    }

    @Override
    public void accept(View view) {
        view.updateBoardView(boardView);
    }
}
