package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.god.GodPower;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.player.Worker;

import java.util.ArrayList;

public class Teleport extends GodPower {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public Teleport(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Teleports the enemy worker placed on the starting tile on tile that has
     * x = posXFrom + posXFrom - posXTo
     * and y = posYFrom + posYFrom - posYTo
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return 1 if the move succedes, -2 if something went wrong
     */
    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            Worker destWorker = board.getTile(posXTo, posYTo).getWorker();
            destWorker.changePosition(board.getTile(posXFrom + posXFrom - posXTo, posYFrom + posYFrom - posYTo));
            return 1;
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return -2;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {

        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 0, true, null, getCanPass());
        OptionSelection newOpt = new OptionSelection();


        for (ArrayList<Integer> comb: opt.getValues()) {
            ArrayList<Integer> singleOpt = new ArrayList<>();
            singleOpt.add(comb.get(0));
            singleOpt.add(comb.get(1));
            for (int i = 2; i < comb.size(); i += 2) {
                if (getGodLogic().hasOpposingOpponentWorker(comb.get(i), comb.get(i + 1))) {
                    if (getGodLogic().isBehindFree(comb.get(i), comb.get(i + 1), comb.get(0), comb.get(1))) {
                        singleOpt.add(comb.get(i));
                        singleOpt.add(comb.get(i + 1));
                    }
                }
            }
            if (singleOpt.size() > 2)
                newOpt.addWorkerOptions(singleOpt);
        }

        if (newOpt.getValues().size() != 0)
            return newOpt;
        else
            return null;
    }
}
