package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;

import java.io.Serializable;

public class BoardView implements Serializable {

    private TileView[][] boardView;

    private static final int BSIZE = 5;

    public BoardView() {
        this.boardView = new TileView[BSIZE][];

        for (int i = 0; i < BSIZE; i++) {
            boardView[i] = new TileView[BSIZE];
            for (int j = 0; j < BSIZE; j++)
                boardView[i][j] = new TileView(0, false, '?');
        }
    }

    public BoardView(Board board) {
        this.boardView = new TileView[BSIZE][];

        try {
            for (int i = 0; i < BSIZE; i++) {
                boardView[i] = new TileView[BSIZE];
                for (int j = 0; j < BSIZE; j++) {
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
