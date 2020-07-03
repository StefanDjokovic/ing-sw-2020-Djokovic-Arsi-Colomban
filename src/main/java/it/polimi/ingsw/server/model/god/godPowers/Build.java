package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.NotBuildableException;
import it.polimi.ingsw.server.model.god.*;
import it.polimi.ingsw.server.model.board.Board;


import java.util.ArrayList;

// Standard build GodPower
public class Build extends GodPower {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public Build(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Builds one building level on the targeted tile
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return
     */
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            board.getTile(posXTo, posYTo).buildUp();
        } catch (NonExistingTileException | NotBuildableException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    /**
     *
     * @param logger the logger of the game
     * @return
     */
    @Override
    public OptionSelection getOptions(Logger logger) {
        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 0, false, null, getCanPass());
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

}
