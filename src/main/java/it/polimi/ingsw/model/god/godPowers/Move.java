package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.god.GodPower;
import it.polimi.ingsw.model.god.OccupiedTileException;
import it.polimi.ingsw.model.god.UnreachableTileException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

public class Move extends GodPower {

    public Move(GodLogic godLogic) {
        super(godLogic);
    }

    public void power(int x, int y, Worker worker, Board board) throws NonExistingTileException, UnreachableTileException, OccupiedTileException {
        Tile destination = board.getTile(x, y); // We select the tile where the player wants to move to
        Tile start = board.getTile(worker.getPosX(), worker.getPosY()); // Tile where the selected worker is standing before moving
        // IN GENERAL, the move can be completed only if the destination has no worker on it
        if (!destination.hasWorker()) {
            if(destination.getBuildingLevel() - start.getBuildingLevel() <=  1 && !destination.hasDome()) {// The worker can move only if the destination tile is not more than one level up the and if it doesn't have a dome
                destination.setWorker(worker); // Puts the worker in the new tile
                start.deleteWorkerAndOwner(); // Removes the worker from the old tile
            }
            else throw new UnreachableTileException("The destination is out of reach"); //Lore: formely called Oopsie
        }
        else throw new OccupiedTileException("The destination is already occupied by another worker");  // if the tile is occupied, throws an exception
    }
}
