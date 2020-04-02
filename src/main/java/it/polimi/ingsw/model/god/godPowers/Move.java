package gioV2.godPowers;


import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.god.GodPower;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

public class Move extends GodPower {

    private Board board;
    private Player player;

    public Move(Board board, Player player) {
        this.board = board;
        this.player = player;
    }

/*
    public void power(int x, int y, Worker worker) {
        Tile destination = this.board.getTile(x, y); // We select the tile where the player wants to move to
        Tile start = this.board.getTile(worker.getPosX(), worker.getPosY()); // Tile where the selected worker is standing before moving
        // IN GENERAL, the move can be completed only if the destination has no worker on it
        if (!destination.hasWorker()) {
            if(destination.getLevel() - start.getLevel()  <=  1 && !destination.hasDome()) {// The worker can move only if the destination tile is not more than one level up the and if it doesn't have a dome
                worker.setPosX(x);    // We update the position of the "worker" object with the destination
                worker.setPosY(y);
                start.setWorker(null); // "Removes" the worker from the old tile
                destination.setWorker(this.player);// "Adds" the worker to the new tile, owned by "player"
            }
            else throw new UnreachableTileException("The destination is out of reach"); //Lore: formely called Oopsie
        }
        else throw new OccupiedTileException("The destination is already occupied by another worker");  // if the tile is occupied, throws an exception
        return this.board;
    }

}
*/

