package it.polimi.ingsw.model.player;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.logger.Logger;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;

/**
 * Player class.
 *
 */
public class Player {

    private final String name;
    private char initial;
    private GodLogic godLogic = null;
    private ArrayList<Worker> workers = new ArrayList<>();

    /**
     * Constructor
     * @param name Name of the player.
     * @param initial Char containing one letter representative of the player. Should be unique.
     */
    public Player(String name, char initial) {
        this.name = name;
        this.initial = initial;
    }

    /**
     * Returns the name of the player.
     * @return String containing the name of the player.
     */
    public String getName() { return name; }

    /**
     * Returns the letter representative of the player.
     * @return Char containing the letter representative of the player.
     */
    public char getInitial() { return initial; }

    /**
     * Sets the god logic of the player.
     * @param godLogic String containing the name of the god logic to implement.
     * @return Reference to the god logic class implemented.
     */
    public GodLogic setGodLogic(String godLogic, Logger logger, Board board) {

        this.godLogic = new GodLogic(godLogic, this, logger, board);
        return this.godLogic;

    }

    /**
     * Returns the god logic of the player.
     * @return Reference to the god logic class implemented.
     */
    public GodLogic getGodLogic() { return godLogic; }
    public int getWorkersSize() {
        return workers.size();
    }

    /**
     * Add a worker to the list of workers possessed by the player.
     * @param tile Tile where the worker should be positioned.
     * @return Reference to the worker created.
     */
    public Worker addWorker(Tile tile) {
        Worker newWorker = new Worker(this, tile);
        workers.add(newWorker);
        return newWorker;
    }

    /**
     * Returns quantity of workers possessed by the player.
     * @return Number of workers.
     */
    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    /**
     * Returns list of the workers possessed by the player.
     * @return List of workers.
     */
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

    /**
     * Sets the initial letter of the player.
     * @param inital String containing the initial letter to set.
     */
    public void setInitial(char initial) { this.initial = initial; }


    public void executeTurn(Game game) {
        godLogic.executeTurn(game);
    }

    public int playerReceiveOptions() {
        return getGodLogic().godLogicReceiveOptions();
    }

    public int playerReceiveOptions(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        return getGodLogic().godLogicReceiveOptions(board, posXFrom, posYFrom, posXTo, posYTo);
    }

    public OptionSelection getOptionsPlayer(int upDiff, int downDiff, boolean canIntoOpp, ArrayList<Integer> limitations) {
        OptionSelection opt = new OptionSelection();
        for (Worker w: workers) {
            opt.fuseOptions(w.getOptionsWorker(upDiff, downDiff, canIntoOpp, limitations));
        }

        return opt;
    }


    @Override
    public String toString() {
        return "Name: " + name + "; Initial: " + initial + "\nSelected God: " + godLogic.getGodLogicName() +
                "\nWorker 1 at: " + workers.get(0).toString() + "\nWorker 2 at: " + workers.get(1).toString();
    }
}
