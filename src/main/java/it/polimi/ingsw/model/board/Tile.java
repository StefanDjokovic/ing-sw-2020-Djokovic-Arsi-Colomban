package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class that represents a tile, the single slot of the board.
 * It holds reference to the proprietary
 * board and has various information stored.
 *
 */
public class Tile {

    private int buildingLevel;
    private boolean dome;
    private Worker worker;
    private final int x;
    private final int y;
    private final Board currentBoard;
    private ArrayList<Tile> neighbors;

    /**
     * Constructor.
     * @param a Row index of the board.
     * @param b Column position of the board.
     * @param currentBoard Reference to the board.
     */
    public Tile(int a, int b, Board currentBoard) {
        this.buildingLevel = 0;
        this.dome = false;
        this.worker = null;
        y = a;
        x = b;
        this.currentBoard = currentBoard;
    }

    /**
     * Initializes every tile's neighbor
     */
    public void initNeighbors() {
        this.neighbors = this.getNeighbors();
    }

    /**
     * Set the new building level of the tile. Must be between 0 and 3.
     * @param buildingLevel Level to set the level of the building to.
     * @throws NotBuildableException The given value of building level was invalid.
     */
    public void setBuildingLevel(int buildingLevel) throws NotBuildableException {
        if (buildingLevel < 0 || buildingLevel > 4){
            throw new NotBuildableException();
        } else {
            this.buildingLevel = buildingLevel;
        }
    }

    /**
     * Set whether there is the dome or not.
     * @param dome True if there is a dome, false if not.
     */
    public void setDome(boolean dome) {
        this.dome = dome;
    }

    /**
     * Sets the reference to the worker positioned in the tile.
     * @param worker Reference to the worker.
     */
    public void setWorker(Worker worker){
        this.worker = worker;
    }


    /**
     * Return index value of x in the board.
     * @return Index value of x in the board.
     */
    public int getX() {
        return x;
    }

    /**
     * Return index value of y in the board.
     * @return Index value of y in the board.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns reference to the worker positioned in the tile.
     * @return Reference to the worker (if present).
     */
    public Worker getWorker() { return worker; }


    /**
     * Returns the level of the building of the tile.
     * @return Level of the building of the tile.
     */
    public int getBuildingLevel(){
        return buildingLevel;
    }

    /**
     * Returns the board whom the tile belongs.
     * @return The board whom the tile belongs.
     */
    public Board getBoard(){
        return currentBoard;
    }

    /**
     * Sets the reference of the worker to NULL.
     */
    public void deleteWorkerAndOwner(){
        this.worker=null;
    }

    /**
     * Returns true whether there is a worker on this tile.
     * @return True if there is a worker on this tile, false if not.
     */
    public boolean hasWorker() {
        return worker != null;
    }

    /**
     * Returns true if there is a dome on this tile.
     * @return True if there is a dome on this tile, false if not.
     */
    public boolean hasDome(){
        return dome;
    }

    /**
     * Returns true if the selected worker can move on this tile.
     * @return True if it's possible to move on this tile.
     */
    public boolean isWalkable() {
        return !this.hasDome() && !this.hasWorker();
    }

    /**
     * Returns true if the player can build in this tile.
     * @return True if the worker can build in this tile.
     */
    public boolean isBuildable() {
        return !hasDome() && !hasWorker() && this.getBuildingLevel() <= 3;
    }

    /**
     * Adds 1 building level to the tile, returns true if the action was successful.
     * @throws NotBuildableException Thrown if it was not possible to build on this tile.
     * @return Reference to the tile used.
     */
    public Tile buildUp() throws NotBuildableException {
        if (buildingLevel >= 0 && buildingLevel < 3 && !this.hasDome()) {
            buildingLevel++;
            return this;
        } else if (buildingLevel == 3 && !this.hasDome()){
            //buildingLevel++;
            dome = true;
            return this;
        } else {
            throw new NotBuildableException();
        }
    }

    /**
     * Returns a list of the neighbors.
     * @return List with reference to all the 8 neighbors of this tile.
     */
    public ArrayList<Tile> getNeighbors() {
        ArrayList<Tile> retList = new ArrayList<>();

        try {
            if (y > 0) {
                if (x > 0){
                    retList.add(currentBoard.getTile( y-1, x-1));
                }
                retList.add(currentBoard.getTile( y-1, x));
                if (x < 4) {
                    retList.add(currentBoard.getTile( y-1, x+1));
                }
            }
            if (x < 4) {
                retList.add(currentBoard.getTile( y, x+1));
            }
            if (y < 4) {
                if (x < 4) {
                    retList.add(currentBoard.getTile( y+1, x+1));
                }
                retList.add(currentBoard.getTile( y+1, x));
                if (x > 0) {
                    retList.add(currentBoard.getTile( y + 1, x-1));
                }
            }
            if (x > 0){
                retList.add(currentBoard.getTile( y , x-1 ));
            }
        }
        catch (NonExistingTileException e) {
            System.out.println("Wait, what?! This should have not happened");
        }


        return retList;
    }

    public ArrayList<Tile> getNeighborsOptimized() {
        return neighbors;
    }

    /**
     * Returns a list of only the neighbors where the worker can move to.
     * @return List of only the neighbors where the worker can move to.
     * @throws NoWalkableTilesException There are no tiles where the worker can move to.
     */
    public List<Tile> getWalkableNeighbors() throws NoWalkableTilesException {
        List<Tile> Wlist=this.getNeighborsOptimized().stream().filter(x -> x.isWalkable()).collect(Collectors.toList());
        if (!Wlist.isEmpty()) {
            return Wlist;
        } else {
            throw new NoWalkableTilesException();
        }
    }

    /**
     * Returns a list of only the neighbors where the worker can build on.
     * @return List of only the neighbors where the worker can build on.
     * @throws NoBuildableTilesException There are no tiles where the worker can build on.
     */
    public List<Tile> getBuildableNeighbors() throws NoBuildableTilesException {
        List<Tile> Wlist=this.getNeighborsOptimized().stream().filter(x -> x.isBuildable()).collect(Collectors.toList());
        if (!Wlist.isEmpty()) {
            return Wlist;
        } else {
            throw new NoBuildableTilesException();
        }
    }

    // todo: Also to test and maybe improve
    public boolean isMovableTo(Tile from, Tile dest, int moveUpMax, int moveDownMax) {
        if (dest.hasDome() || dest.hasWorker())
            return false;
        return from.getBuildingLevel() + moveUpMax >= dest.getBuildingLevel() && from.getBuildingLevel() - moveDownMax <= dest.getBuildingLevel();



    }

    // todo: TEST THIS, THERE IS PROBABLY A MISTAKE SOMEWHERE, GetMovableWorker does not work properly I think
    public List<Tile> getMovableToNeigh(int moveUpMax, int moveDownMax) {
        List<Tile> result = new ArrayList<>();
        try {
            result = this.getWalkableNeighbors();
        } catch (NoWalkableTilesException e) {
            return result;
        }

        result.removeIf(temp -> !(isMovableTo(this, temp, moveUpMax, moveDownMax)));

        return result;
    }

    @Override
    public String toString() {
        return ((this.getX())+" "+(this.getY()));
    }
}