package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.god.*;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;


import java.util.Scanner;

public class DoubleMove extends Move {


    public DoubleMove(GodLogic godLogic) {
        super(godLogic);
    }

    public void power(int x1, int x2, int y1, int y2, Worker worker, Board board) throws UnreachableTileException, OccupiedTileException, NonExistingTileException { // Overload of Move's power method
        super.power(x1, y1, worker, board);
        if( !(x2 == x1 && y2 == y1) ) {    // The second movement has to be on a different tile than the first one
            super.power(x2, y2, worker, board);
        }
    }

}
