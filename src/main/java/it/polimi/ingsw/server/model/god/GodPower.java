package it.polimi.ingsw.server.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.board.Board;


public abstract class GodPower {

    private GodLogic godLogic;
    private boolean canPass;

    /**
     * Constructor
     * @param godLogic God containing the logic of the god selected
     * @param canPass true if the power can be skipped, false otherwise
     */
    public GodPower(GodLogic godLogic, boolean canPass) {
        this.godLogic = godLogic;
        this.canPass = canPass;
    }

    /**
     * Abstract power execution method
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return defined by the implementations of this abstract class
     */
    public abstract int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo);

    /**
     * Getter method for optionSelection
     * @param logger the logger of the game
     * @return class that contains the player's options for this power's turn step
     */
    public abstract OptionSelection getOptions(Logger logger);

    /**
     * Getter method for godLogic variable
     * @return the godLogic that called the power
     */
    public GodLogic getGodLogic() {
        return godLogic;
    }

    /**
     * The basic win condition (that can be overruled by specific god powers) is moving from a
     * lvl 2 building to a lvl 3 building
     * @param from starting tile
     * @param to starting tile
     * @return 2 if the condition is met (win), 0 if it isn't
     */
    protected int checkWinCondition(Tile from, Tile to) {
        if (from.getBuildingLevel() == 2 && to.getBuildingLevel() == 3)
            return 2;
        else
            return 0;
    }

    /**
     * Getter method for canPass variable
     * @return true if the power can be skipped, false if it can't
     */
    public boolean getCanPass() {
        return canPass;
    }

}
