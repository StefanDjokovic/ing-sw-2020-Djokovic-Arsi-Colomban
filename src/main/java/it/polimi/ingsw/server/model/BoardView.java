package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;

import java.io.Serializable;
import java.util.ArrayList;

public class BoardView implements Serializable {

    private TileView[][] boardView;

    public BoardView() {
        this.boardView = new TileView[5][];

        for (int i = 0; i < 5; i++) {
            boardView[i] = new TileView[5];
            for (int j = 0; j < 5; j++)
                boardView[i][j] = new TileView(0, false, '?');
        }
    }

    public ArrayList<Integer> comp1() {
        ArrayList<Integer> comp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j ++) {
                comp.add(boardView[i][j].getBuildingLevel());
            }
        }
        return comp;
    }

    public ArrayList<Boolean> comp2() {
        ArrayList<Boolean> comp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j ++) {
                comp.add(boardView[i][j].hasDome());
            }
        }
        return comp;
    }

    public ArrayList<Character> comp3() {
        ArrayList<Character> comp = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j ++) {
                comp.add(boardView[i][j].getInitWorker());
            }
        }
        return comp;
    }

    public BoardView(ArrayList<Integer> comp1, ArrayList<Boolean> comp2, ArrayList<Character> comp3) {

        this.boardView = new TileView[5][];

        for (int i = 0; i < 5; i++) {
            boardView[i] = new TileView[5];
            for (int j = 0; j < 5; j++)
                boardView[i][j] = new TileView(comp1.get(i * 5 + j), comp2.get(i * 5 + j), comp3.get(i * 5 + j));
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
