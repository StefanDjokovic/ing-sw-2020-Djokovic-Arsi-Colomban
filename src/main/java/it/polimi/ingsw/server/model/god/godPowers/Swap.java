package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.player.Worker;

public class Swap extends Move {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public Swap(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Moves the worker from the starting tile to the destination tile, if there's
     * already an enemy worker on the destination tile the two workers get swapped
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return 2 if the player wins, 1 if they don't
     */
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
