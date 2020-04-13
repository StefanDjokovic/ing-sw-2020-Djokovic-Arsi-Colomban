package it.polimi.ingsw.model.god;

import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.player.Worker;

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

    public int[][] generateOptions() {
        int[][] options = new int[2][];



        return options;
    }

    public GodLogic getGodLogic() {
        return godLogic;
    }
}
