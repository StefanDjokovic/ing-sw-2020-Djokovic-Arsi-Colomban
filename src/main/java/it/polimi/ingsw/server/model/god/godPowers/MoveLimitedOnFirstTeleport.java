package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;

import java.util.ArrayList;

public class MoveLimitedOnFirstTeleport extends Move {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public MoveLimitedOnFirstTeleport(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        if (logger.getLastLog().getType() == 1) {
            return getGodLogic().getOptionsGodLogic(moveUp, moveDown, false, null, getCanPass());

        }
        else {
            OptionSelection opt = getGodLogic().getOptionsGodLogic(moveUp, moveDown, false, null, getCanPass());
            Log log = logger.getLastLog();

            int lastWorkerUsedX = log.getAction(0);
            int lastWorkerUsedY = log.getAction(1);
            for (ArrayList<Integer> a: opt.getValues()) {
                if (a.get(0) == lastWorkerUsedX && a.get(1) == lastWorkerUsedY) {
                    opt = new OptionSelection();
                    opt.setOptions(a);
                    break;
                }
            }
            moveUp = 1;

            return opt;
        }

    }

}
