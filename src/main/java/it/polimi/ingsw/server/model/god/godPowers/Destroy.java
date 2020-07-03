package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.NotBuildableException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.god.GodPower;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;

public class Destroy extends GodPower {
    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public Destroy(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Lowers the building level of the selected tile by 1 level
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return
     */
    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            board.getTile(posXTo, posYTo).buildDown();
        } catch (NonExistingTileException | NotBuildableException e) {
            System.out.println("You failed!");
        }
        return 0;

    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        if (logger.getLastLog().getType() == 1) {
            OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 1, false, null, getCanPass());
            // Build is used after a Move: you can only build with the character that has moved
            if (logger.getLastLog().getPlayerInit() == getGodLogic().getPlayer().getInitial()) {
                Log log;
                if (logger.getLastLog().getType() == 0) {
                    log = logger.getLastLog();
                }
                else {
                    // In case there was some other power before this build
                    log = logger.getSecondToLastLog();
                }
                int lastWorkerUsedX = log.getAction(2);
                int lastWorkerUsedY = log.getAction(3);

                opt = opt.singleOption(lastWorkerUsedX, lastWorkerUsedY);
            }

            return opt;
        }
        else
            return null;
    }
}
