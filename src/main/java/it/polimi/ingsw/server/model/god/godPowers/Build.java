package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.NotBuildableException;
import it.polimi.ingsw.server.model.god.*;
import it.polimi.ingsw.server.model.board.Board;


import java.util.ArrayList;

// Standard build GodPower
public class Build extends GodPower {

    public Build(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }


    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        try {
            board.getTile(posXTo, posYTo).buildUp();
            System.out.println("Should have built... right?");
        } catch (NonExistingTileException | NotBuildableException e) {
            System.out.println("You failed!");
        }
        return 0;
    }


    @Override
    public OptionSelection getOptions(Logger logger) {
        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 99, false, null, getCanPass());
        // Build is used after a Move: you can only build with the character that has moved
        if (logger.getLastLog().getPlayerInit() == getGodLogic().getPlayer().getInitial()) {
            Log log;
            if (logger.getLastLog().getType() == 0) {
                log = logger.getLastLog();
            }
            else {
                // In case there was some other power before this build
                log = logger.getSecondToLastLog();
            }
            int lastWorkerUsedX = log.getAction(2);
            int lastWorkerUsedY = log.getAction(3);

            opt = opt.singleOption(lastWorkerUsedX, lastWorkerUsedY);
            System.out.println(opt);
        }

        return opt;
    }

}
