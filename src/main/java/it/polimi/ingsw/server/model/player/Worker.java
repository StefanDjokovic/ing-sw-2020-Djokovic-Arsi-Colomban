package it.polimi.ingsw.server.model.player;


import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.board.Tile;

import java.util.ArrayList;


/**
 * Worker class.
 *
 */
public class Worker {
    private final Player owner;
    private Tile posTile;

    /**
     * Constructor.
     * @param owner The player that owns the worker (final).
     * @param tile Reference to the tile where the worker is positioned.
     */
    public Worker(Player owner, Tile tile) {
        /*if (tile.hasWorker())
            throw new IllegalArgumentException();*/
        this.owner = owner;
        this.posTile = tile;

        tile.setWorker(this);
    }

    /**
     * Returns reference to the player that owns the worker.
     * @return Reference to the player that owns the worker.
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Returns X coordinate of the worker on the board.
     * @return X coordinate of the worker on the board.
     */
    public int getPosX() { return posTile.getX(); }

    /**
     * Returns Y coordinate of the worker on the board.
     * @return Y coordinate of the worker on the board.
     */
    public int getPosY() { return posTile.getY(); }

    //TODO: Complete the param part of the JAVADOC
    /**
     * Builds the object that contains all the options for a turn step, given the parameters
     * @param upDiff
     * @param downMin
     * @param canIntoOpp
     * @param limitations
     * @return OptionSelection instance with all the player's options for that turn step
     */
    public ArrayList<Integer> getOptionsWorker(int upDiff, int downMin, boolean canIntoOpp, ArrayList<Integer> limitations) {
        return posTile.getOptions(upDiff, downMin, canIntoOpp, limitations);
    }

    /**
     * Moves the worker from its current tile to the destination tile
     * @param dest destination tile
     */
    public void changePosition(Tile dest) {
        if (posTile.getWorker() == this)
            this.posTile.deleteWorkerAndOwner();
        dest.setWorker(this);
        this.posTile = dest;
    }

    /**
     * Returns reference to the tile where the worker is positioned.
     * @return Reference to the tile where the worker is positioned.
     */
    public Tile getPosTile() { return this.posTile; }

    /**
     * Deletes worker and removes the reference from the tile where the worker is placed
     */
    public void delete() {
        posTile.deleteWorkerAndOwner();
    }

    @Override
    public String toString() {
        return posTile.getX() + " " + posTile.getY();
    }
}
