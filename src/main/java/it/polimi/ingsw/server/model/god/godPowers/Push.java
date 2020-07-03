package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.player.Worker;

import java.util.ArrayList;

public class Push extends Move {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public Push(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Moves the worker from the starting tile to the destination tile, if there's
     * already an enemy worker on the enemy worker gets pushed back 1 tile
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
                destWorker.changePosition(board.getTile(posXTo + posXTo - posXFrom, posYTo + posYTo - posYFrom));
                sourceWorker.changePosition(board.getTile(posXTo, posYTo));
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

        OptionSelection opt = getGodLogic().getOptionsGodLogic(1, 0, true, null, getCanPass());

        for (ArrayList<Integer> comb: opt.getValues()) {
            for (int i = 2; i < comb.size(); i += 2) {
                if (getGodLogic().hasOpposingOpponentWorker(comb.get(i), comb.get(i + 1))) {
                    if (!(getGodLogic().isBehindFree(comb.get(0), comb.get(1), comb.get(i), comb.get(i + 1)))) {
                        comb.remove(i + 1);
                        comb.remove(i);
                        i -= 2;
                    }
                }
            }
        }

        return opt;
    }


}
