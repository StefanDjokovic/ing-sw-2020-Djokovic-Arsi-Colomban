package it.polimi.ingsw.model;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;

public class BoardView {

    private TileView[][] boardView;

    public BoardView() {
        this.boardView = new TileView[5][];

        for (int i = 0; i < 5; i++) {
            boardView[i] = new TileView[5];
            for (int j = 0; j < 5; j++)
                boardView[i][j] = new TileView(0, false, '?');
        }
    }

    public BoardView(Board board) {
        this.boardView = new TileView[5][];

        try {
            for (int i = 0; i < 5; i++) {
                boardView[i] = new TileView[5];
                for (int j = 0; j < 5; j++) {
                    boardView[i][j] = new TileView(board.getTile(i, j).getBuildingLevel(),
                            board.getTile(i, j).hasDome(), '?');
                    if (board.getTile(i, j).hasWorker())
                        boardView[i][j].setInitWorker(board.getTile(i, j).getWorker().getOwner().getInitial());
                }
            }
        } catch (NonExistingTileException e) {
            e.printStackTrace();
        }

    }

    public TileView[][] getBoardView() {
        return boardView;
    }

}
