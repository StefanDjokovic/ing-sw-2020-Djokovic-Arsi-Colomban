package it.polimi.ingsw.server.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.logger.Log;
import it.polimi.ingsw.server.model.logger.Logger;

public class BuildLimited extends Build {

    public BuildLimited(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        Log log = logger.getLastLog();
        int lastWorkerUsedX = log.getAction(0);
        int lastWorkerUsedY = log.getAction(1);

        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 0, false, null, getCanPass());
        opt = opt.singleOption(lastWorkerUsedX, lastWorkerUsedY);

        opt = opt.removeOptionsPerimeter();

        return opt;
    }
}
