package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.god.AlreadyHasDomeException;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.god.OccupiedTileException;
import it.polimi.ingsw.model.god.OutOfReachException;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;


import java.util.Scanner;

public class BuildDome extends Build{

    public BuildDome(GodLogic godLogic) {
        super(godLogic);
    }

//    public void power(int x, int y, Worker worker, boolean usePower, Board board) throws NonExistingTileException, OccupiedTileException, AlreadyHasDomeException, NotBuildableException, OutOfReachException {    // Overloaded method. If usePower = true, then the worker builds a dome instead of a normal level
//        Tile destination = board.getTile(x, y); // Tile the player wants build on
//        if (usePower){
//            if ( !destination.hasWorker() ) {
//                if (!destination.hasDome()) { // Check if there's already a dome on the target tile
//                    destination.setDome(true);
//                } else throw new AlreadyHasDomeException("The selected tile already has a dome");
//            } else throw new OccupiedTileException("The selected tile is occupied by another worker");
//        }
//        else super.power(x, y, worker, board);
//    }


}
