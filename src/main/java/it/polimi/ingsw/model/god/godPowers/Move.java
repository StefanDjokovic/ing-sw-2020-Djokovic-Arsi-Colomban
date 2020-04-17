package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
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

    @Override
    public void power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            Worker sourceWorker = board.getTile(posXFrom, posYFrom).getWorker();
            Tile destTile = board.getTile(posXTo, posYTo);
            sourceWorker.changePosition(destTile);
            System.out.println("Should have moved... right?");
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
    }

    @Override
    public OptionSelection getOptions(int lastWorkerUsed) {
        return getGodLogic().getOptionsGodLogic(1, 99, false);
    }
}
