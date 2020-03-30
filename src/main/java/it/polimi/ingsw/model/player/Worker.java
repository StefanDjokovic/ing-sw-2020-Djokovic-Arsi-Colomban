package it.polimi.ingsw.model.player;


import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.board.Board;


/**
 * Worker class.
 *
 */
public class Worker {
    private final Player owner;
    private Tile posTile;
    private int posX;
    private int posY;

    public Worker(Player owner) {
        this.owner = owner;
    }

    public Worker(Player owner, int posX, int posY, Tile tile) {
        if (tile.hasWorker())
            throw new IllegalArgumentException();
        this.owner = owner;
        this.posX = posX;
        this.posY = posY;
        this.posTile = tile;


        tile.setWorker(this);
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setPosX(int x) {
        this.posX = x;
    }

    public void setPosY(int y) {
        this.posY = y;
    }

    public int getPosX() {
        return this.posX;
    }

    public int getPosY() {
        return this.posY;
    }

    public Tile getPosTile() { return this.posTile; }

    @Override
    public String toString() {
        return posX + " " + posY;
    }
}
