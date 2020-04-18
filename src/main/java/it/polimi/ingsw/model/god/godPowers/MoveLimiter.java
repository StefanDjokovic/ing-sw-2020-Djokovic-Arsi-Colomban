package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.GodLogic;

public class MoveLimiter extends Move {


    public MoveLimiter(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            if (board.getTile(posXTo, posYTo).getBuildingLevel() > board.getTile(posXFrom, posYFrom).getBuildingLevel()) {
                getGodLogic().limitOpp(0);
            }
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return super.power(board, posXFrom, posYFrom, posXTo, posYTo);
    }
}