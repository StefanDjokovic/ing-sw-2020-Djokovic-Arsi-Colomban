package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.NotBuildableException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.logger.Logger;

public class BuildUnderneath extends Build {

    public BuildUnderneath(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            board.getTile(posXTo, posYTo).buildUp();
        } catch (NonExistingTileException | NotBuildableException e) {
            System.out.println("You failed!");
        }
        return 0;
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        OptionSelection opt = super.getOptions(logger);
        opt.addWorkerAsOption();

        return opt;
    }

}
