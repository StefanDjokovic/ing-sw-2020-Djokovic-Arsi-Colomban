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


import java.util.Scanner;

public class DoubleBuild extends Build {


    public DoubleBuild(GodLogic godLogic) {
        super(godLogic);
    }
//
//    public void power(int x1, int y1, int x2, int y2, Worker worker, Board board) throws NonExistingTileException, NotBuildableException, AlreadyHasDomeException, OccupiedTileException, OutOfReachException { // Overload of Move's power method
//        super.power(x1, y1, worker, board);
//        if( !(x2 == x1 && y2 == y1) ) {    // The second build has to be on a different tile than the first one
//            super.power(x2, y2, worker, board);
//        }
//        // Ho tolto SameTileExcpetion, da riaggiungere in futuro
//    }

}
