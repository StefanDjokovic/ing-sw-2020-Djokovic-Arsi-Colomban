package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.god.OccupiedTileException;
import it.polimi.ingsw.model.god.UnreachableTileException;
import it.polimi.ingsw.model.god.GodPower;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;


import java.util.Scanner;

public class Move extends GodPower {


    public Move(GodLogic godLogic) {
        super(godLogic);
    }

    @Override
    public void power() {
        System.out.println("test message: I DONT SEE ENOUGH MOVEMENT (cit)");
    }


    public void power(Worker worker, Board board) throws NonExistingTileException, UnreachableTileException, OccupiedTileException {
        int x;
        int y;
        /* This reads the input provided by user
         * using keyboard
         */
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter x coordinate: ");
        x = scan.nextInt();
        System.out.print("Enter y coordinate: ");
        y = scan.nextInt();
        scan.close();
        Tile destination = board.getTile(x, y); // We select the tile where the player wants to move to
        Tile start = board.getTile(worker.getPosX(), worker.getPosY()); // Tile where the selected worker is standing before moving
        // IN GENERAL, the move can be completed only if the destination has no worker on it
        if (!destination.hasWorker()) {
            if(destination.getBuildingLevel() - start.getBuildingLevel()  <=  1 && !destination.hasDome()) {// The worker can move only if the destination tile is not more than one level up the and if it doesn't have a dome
                // todo: HO CANCELLATO QUESTE, OH NO
                start.setWorker(null); // "Removes" the worker from the old tile
                destination.setWorker(worker);// "Adds" the worker to the new tile, owned by "player"
            }
            else throw new UnreachableTileException("The destination is out of reach"); //Lore: formely called Oopsie
        }
        else throw new OccupiedTileException("The destination is already occupied by another worker");  // if the tile is occupied, throws an exception
    }

    public void power(int x, int y, Worker worker, Board board) throws NonExistingTileException, UnreachableTileException, OccupiedTileException { // Overloaded method, it takes the coordinates as arguments rather than from stdio
        Tile destination = board.getTile(x, y); // We select the tile where the player wants to move to
        Tile start = board.getTile(worker.getPosX(), worker.getPosY()); // Tile where the selected worker is standing before moving
        // IN GENERAL, the move can be completed only if the destination has no worker on it
        if (!destination.hasWorker()) {
            if(destination.getBuildingLevel() - start.getBuildingLevel()  <=  1 && !destination.hasDome()) {// The worker can move only if the destination tile is not more than one level up the and if it doesn't have a dome
                // todo: HO CANCELLATO QUESTE, OH NO
                start.setWorker(null); // "Removes" the worker from the old tile
                destination.setWorker(worker);// "Adds" the worker to the new tile, owned by "player"
            }
            else throw new UnreachableTileException("The destination is out of reach"); //Lore: formely called Oopsie
        }
        else throw new OccupiedTileException("The destination is already occupied by another worker");  // if the tile is occupied, throws an exception
    }

}


