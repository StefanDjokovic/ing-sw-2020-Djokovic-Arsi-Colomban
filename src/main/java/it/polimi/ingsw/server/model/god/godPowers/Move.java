package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.god.GodPower;
import it.polimi.ingsw.server.model.player.Worker;

// Standard movement GodPower
public class Move extends GodPower {

    int moveUp = 1;
    int moveDown = 99;

    public Move(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            Worker sourceWorker = board.getTile(posXFrom, posYFrom).getWorker();
            Tile destTile = board.getTile(posXTo, posYTo);
            sourceWorker.changePosition(destTile);
            if (checkWinCondition(board.getTile(posXFrom, posYFrom), board.getTile(posXTo, posYTo)) == 2)
                return 2;
            return 1;
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        return getGodLogic().getOptionsGodLogic(moveUp, moveDown, false, null, getCanPass());
    }
}
