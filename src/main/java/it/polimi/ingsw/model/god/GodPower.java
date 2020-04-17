package it.polimi.ingsw.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
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
    public void power() { }
    public void power(Worker worker) {}
    public void power(Worker worker, int destX, int destY) { System.out.println("Will need to override it I guess");}
    public void power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) { }

    public OptionSelection getOptions(int lastWorkerUsed) {
        return getGodLogic().getOptionsGodLogic(1, 99, false);
    }

    public GodLogic getGodLogic() {
        return godLogic;
    }
}
