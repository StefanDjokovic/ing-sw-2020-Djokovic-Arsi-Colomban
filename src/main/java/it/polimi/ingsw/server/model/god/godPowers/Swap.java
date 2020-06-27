package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.player.Worker;

public class Swap extends Move {

    public Swap(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            if (board.getTile(posXTo, posYTo).hasWorker()) {
                Worker sourceWorker = board.getTile(posXFrom, posYFrom).getWorker();
                Worker destWorker = board.getTile(posXTo, posYTo).getWorker();
                sourceWorker.changePosition(destWorker.getPosTile());
                destWorker.changePosition(board.getTile(posXFrom, posYFrom));
                if (checkWinCondition(board.getTile(posXFrom, posYFrom), board.getTile(posXTo, posYTo)) == 2)
                    return 2;
                return 1;
            }
            else
                return super.power(board, posXFrom, posYFrom, posXTo, posYTo);
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return -2;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        return getGodLogic().getOptionsGodLogic(1, 0, true, null, getCanPass());
    }
}
