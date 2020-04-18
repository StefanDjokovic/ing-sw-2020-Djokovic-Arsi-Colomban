package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.god.GodLogic;

import java.util.ArrayList;

public class BuildWithLimitation extends Build {

    public BuildWithLimitation(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }


    @Override
    public OptionSelection getOptions(Logger logger) {
        System.out.println("IM SELECTING FOR BUILDING");
        ArrayList<Integer> limit = new ArrayList<>();
        limit.add(logger.getLastLog().getAction(2));
        limit.add(logger.getLastLog().getAction(3));
        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 99, false, limit, getCanPass());
        if (logger.getLastLog().getPlayerInit() == getGodLogic().getPlayer().getInitial()) {
            Log log = logger.getLastLog();
            int lastWorkerUsedX = log.getAction(0);
            int lastWorkerUsedY = log.getAction(1);
            for (ArrayList<Integer> a: opt.getComb()) {
                if (a.get(0) == lastWorkerUsedX && a.get(1) == lastWorkerUsedY) {
                    opt = new OptionSelection();
                    opt.setOptions(a);
                    break;
                }
            }
            System.out.println(opt);
        }

        return opt;
    }

}