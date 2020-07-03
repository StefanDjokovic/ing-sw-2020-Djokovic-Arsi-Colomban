package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;

public class BuildMultipleWithCondition extends Build {

    /**
     * Constructor
     * @param godLogic player's god
     * @param canPass  true if the power can be skipped, false otherwise
     */
    public BuildMultipleWithCondition(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        Log log;
        if (logger.getLastLog().getType() == 0) {
            log = logger.getLastLog();
            int lastWorkerUsedX = log.getAction(0);
            int lastWorkerUsedY = log.getAction(1);
            if (getGodLogic().levelWorker(lastWorkerUsedX, lastWorkerUsedY) == 0) {
                OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 0, false, null, getCanPass());
                opt = opt.singleOption(lastWorkerUsedX, lastWorkerUsedY);
                return opt;
            }
        }
        return null;

    }
}
