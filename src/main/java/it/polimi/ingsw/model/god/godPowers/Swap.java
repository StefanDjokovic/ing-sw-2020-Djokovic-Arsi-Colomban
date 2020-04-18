package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.player.Worker;

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
                System.out.println("You could have swapped... right?");
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
        return getGodLogic().getOptionsGodLogic(1, 99, true, null, getCanPass());
    }
}
