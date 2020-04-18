package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.god.GodLogic;

import java.util.ArrayList;

public class BuildOverOneTile extends Build {
    public BuildOverOneTile(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        System.out.println("IM SELECTING FOR BUILDING");
        int posXBuild = logger.getLastLog().getAction(2);
        int posYBuild = logger.getLastLog().getAction(3);
        if (getGodLogic().tileBuildingLevel(posXBuild, posYBuild) <= 2) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(logger.getLastLog().getAction(0));
            temp.add(logger.getLastLog().getAction(1));
            temp.add(posXBuild);
            temp.add(posYBuild);
            OptionSelection opt = new OptionSelection();
            opt.setOptions(temp);
            getGodLogic().setPass(true);

            return opt;
        }
        return null;
    }
}
