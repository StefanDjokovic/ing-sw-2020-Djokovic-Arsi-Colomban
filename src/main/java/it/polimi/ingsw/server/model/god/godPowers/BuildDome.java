package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.board.Board;


public class BuildDome extends Build {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public BuildDome(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    /**
     * Builds a dome on the targeted tile
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return 0 at the end of the execution
     */
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            board.getTile(posXTo, posYTo).setDome(true);
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        if (logger.getLastLog().getType() == 1)
            return super.getOptions(logger);
        else
            return null;
    }


}
