package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.player.Worker;

public class MoveWithSpecialWinCondition extends Move {

    private int fromLevel;
    private int toLevel;

    public MoveWithSpecialWinCondition(GodLogic godLogic, boolean canPass, int fromLevel, int toLevel) {
        super(godLogic, canPass);
        this.fromLevel = fromLevel;
        this.toLevel = toLevel;
    }

    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            Worker sourceWorker = board.getTile(posXFrom, posYFrom).getWorker();
            Tile destTile = board.getTile(posXTo, posYTo);
            sourceWorker.changePosition(destTile);
            if (specialWinCondition(board.getTile(posXFrom, posYFrom).getBuildingLevel(), board.getTile(posXTo, posYTo).getBuildingLevel()))
                return 2;
            return 1;
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    private boolean specialWinCondition(int fromLevel, int toLevel) {
        return fromLevel - toLevel >= this.fromLevel - this.toLevel;
    }

}
