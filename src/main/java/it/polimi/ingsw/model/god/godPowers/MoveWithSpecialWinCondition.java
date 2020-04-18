package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.player.Worker;

public class MoveWithSpecialWinCondition extends Move {

    int fromLevel;
    int toLevel;

    public MoveWithSpecialWinCondition(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

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
            System.out.println("Should have moved... right?");
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
