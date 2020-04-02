package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.model.board.Board;

public class RequestDisplayBoard extends Request {

    // IN THE FUTURE IT WILL DISPLAY A COPY OF THE BOARD
    Board board;

    public RequestDisplayBoard(Board board) {
        this.board = board;
    }

    @Override
    public void accept(View view) {
        view.displayBoard(board);
    }
}
