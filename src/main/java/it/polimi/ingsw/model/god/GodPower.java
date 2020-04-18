package it.polimi.ingsw.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.board.Board;


public abstract class GodPower {

    private GodLogic godLogic;
    private boolean canPass;

    public GodPower(GodLogic godLogic, boolean canPass) {
        this.godLogic = godLogic;
        this.canPass = canPass;
    }

    public abstract int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo);

    public abstract OptionSelection getOptions(Logger logger);

    public GodLogic getGodLogic() {
        return godLogic;
    }

    protected int checkWinCondition(Tile from, Tile to) {
        if (from.getBuildingLevel() == 2 && to.getBuildingLevel() == 3)
            return 2;
        else
            return 0;
    }

    public boolean getCanPass() {
        return canPass;
    }

}
