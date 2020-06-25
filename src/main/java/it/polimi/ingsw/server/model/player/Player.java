package it.polimi.ingsw.server.model.player;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.Game;

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
     * @param logger
     * @param board
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
     * Returns number of workers associated to the player.
     * @return Number of workers.
     */
    public ArrayList<Worker> getWorkers() {
        return workers;
    }

    /**
     * Sets the initial letter of the player.
     * @param initial String containing the initial letter to set.
     */
    public void setInitial(char initial) { this.initial = initial; }

    /**
     * Starts executing the turn routine
     * @param game model's main object, that initializes everything else
     */
    public void executeTurn(Game game) {
        System.out.println(godLogic);
        godLogic.executeTurn(game);
    }

    /**
     * Executes the turn step when the player decides to pass the turn step
     * @return 1 if the turn step was the last one of the routine, 0 if there are other steps afterwards
     */
    public int playerReceiveOptions() {
        return getGodLogic().godLogicReceiveOptions();
    }

    public int nWorkers() {
        return workers.size();
    }

    /**
     * Executes the power and notifies the player if the move wins the game
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return 2 if the power usage wins the game, 1 if the turn step was the last one of the routine, 0 if there are other steps afterwards
     */
    public int playerReceiveOptions(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        return getGodLogic().godLogicReceiveOptions(board, posXFrom, posYFrom, posXTo, posYTo);
    }

    //TODO: Complete the param part of the JAVADOC
    /**
     * Builds the object that contains all the options for a turn step, given the parameters
     * @param upDiff Max levels the worker can climb up (aka level difference from start to destination tile)
     * @param downDiff Max levels the worker can go down (aka level difference from start to destination tile)
     * @param canIntoOpp True if the worker is allowed to go in a tile occupied by a worker of other players
     * @param limitations List of tiles the worker can't move to
     * @return OptionSelection instance with all the player's options for that turn step
     */
    public OptionSelection getOptionsPlayer(int upDiff, int downDiff, boolean canIntoOpp, ArrayList<Integer> limitations) {
        OptionSelection opt = new OptionSelection();
        for (Worker w: workers) {
            opt.fuseOptions(w.getOptionsWorker(upDiff, downDiff, canIntoOpp, limitations));
        }

        return opt;
    }

    /**
     * Deletes all the workers
     */
    public void delete() {
        for (Worker w: workers) {
            w.delete();
        }
    }

    @Override
    public String toString() {
        return "Name: " + name + "; Initial: " + initial + "\nSelected God: " + godLogic.getGodLogicName() +
                "\nWorker 1 at: " + workers.get(0).toString() + "\nWorker 2 at: " + workers.get(1).toString();
    }

}
