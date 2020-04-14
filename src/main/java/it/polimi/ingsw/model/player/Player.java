package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.god.GodPower;
import it.polimi.ingsw.model.god.godPowers.Move;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;

/**
 * Player class.
 *
 */
public class Player {

    final String name;
    char inital;
    GodLogic godLogic = null;
    ArrayList<Worker> workers = new ArrayList<>();

    public Player(String name, char initial) {
        this.name = name;
        this.inital = initial;
    }

    public String getName() { return name; }
    public char getInitial() { return inital; }
    public GodLogic setGodLogic(String godLogic) {

        this.godLogic = new GodLogic(godLogic);
        return this.godLogic;

    }
    public GodLogic getGodLogic() { return godLogic; }
    public int getWorkersSize() {
        return workers.size();
    }

    public Worker addWorker(Tile tile) {
        Worker newWorker = new Worker(this, tile);
        workers.add(newWorker);
        return newWorker;
    }

    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    public ArrayList<ArrayList<Tile>> getOptionTiles() {
        ArrayList<ArrayList<Tile>> optionTiles = new ArrayList<>();
        for (Worker w: workers) {
            ArrayList<Tile> optionWorker = new ArrayList<>();
            optionWorker.add(w.getPosTile());
            optionWorker.addAll(w.getPosTile().getNeighbors());
            optionTiles.add(optionWorker);
        }

        return optionTiles;

    }

    public void setInital(char inital) { this.inital = inital; }

    public void executeTurn(Game game) {
       godLogic.executeTurn(workers, game);
    }

    @Override
    public String toString() {
        return "Name: " + name + "; Initial: " + inital + "\nSelected God: " + godLogic.getGodLogicName() +
                "\nWorker 1 at: " + workers.get(0).toString() + "\nWorker 2 at: " + workers.get(1).toString();
    }
}
