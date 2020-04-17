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

    public Worker(Player owner, Tile tile) {
        if (tile.hasWorker())
            throw new IllegalArgumentException();
        this.owner = owner;
        this.posTile = tile;

        tile.setWorker(this);
    }

    public Player getOwner() {
        return this.owner;
    }

    public int getPosX() { return posTile.getX(); }
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


    public Tile getPosTile() { return this.posTile; }

    @Override
    public String toString() {
        return posTile.getX() + " " + posTile.getY();
    }
}
