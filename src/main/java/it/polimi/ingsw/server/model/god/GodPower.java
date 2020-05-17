package it.polimi.ingsw.server.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.board.Board;


public abstract class GodPower {

    private GodLogic godLogic;
    private boolean canPass;

    public GodPower(GodLogic godLogic, boolean canPass) {
        this.godLogic = godLogic;
        this.canPass = canPass;
    }

    // Activates the power on the selected tiles
    public abstract int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo);

    // Gets the options the player has
    public abstract OptionSelection getOptions(Logger logger);

    // Returns the GodLogic that calls this GodPower
    public GodLogic getGodLogic() {
        return godLogic;
    }

    // The base win condition is that it wins when moves from a 2 lev building to a 3
    // Returns 2 if the condition is met (so a win), else 0
    protected int checkWinCondition(Tile from, Tile to) {
        if (from.getBuildingLevel() == 2 && to.getBuildingLevel() == 3)
            return 2;
        else
            return 0;
    }

    // Some GodPowers can be skipped
    public boolean getCanPass() {
        return canPass;
    }

}
