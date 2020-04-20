package it.polimi.ingsw.model.player;


import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.board.Tile;

import java.util.ArrayList;


/**
 * Worker class.
 *
 */
public class Worker {
    private final Player owner;
    private Tile posTile;

    public Worker(Player owner) {
        this.owner = owner;
    }

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

    public OptionSelection getOptionsWorker(int upDiff, int downDiff, boolean canIntoOpp, ArrayList<Integer> limitations) {
        return posTile.getOptions(upDiff, downDiff, canIntoOpp, limitations);
    }

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

    @Override
    public String toString() {
        return posTile.getX() + " " + posTile.getY();
    }
}
