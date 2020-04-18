package it.polimi.ingsw.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

public abstract class GodPower {

    private GodLogic godLogic;

    public GodPower(GodLogic godLogic) {
        this.godLogic = godLogic;
    }

    public Worker power(ArrayList<Worker> workers) {
        System.out.println("There may have been a mistake, this is not a MovableStrategy");

        return null;
    }

    public abstract int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo);

    public abstract OptionSelection getOptions(Logger logger);

    public GodLogic getGodLogic() {
        return godLogic;
    }

    public int checkWinCondition(Tile from, Tile to) {
        if (from.getBuildingLevel() == 2 && to.getBuildingLevel() == 3)
            return 2;
        else
            return 0;
    }
}
