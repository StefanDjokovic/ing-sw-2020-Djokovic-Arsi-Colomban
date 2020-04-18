package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.board.Board;


public class BuildDome extends Build {

    public BuildDome(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("IM BUILDING DOMES");
        try {
            board.getTile(posXTo, posYTo).setDome(true);
            System.out.println("Should have built... right?");
        } catch (NonExistingTileException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        System.out.println("IM SELECTING FOR DOUBLE BUILDING");
        if (logger.getLastLog().getType() == 1)
            return super.getOptions(logger);
        else
            return null;
    }


}
