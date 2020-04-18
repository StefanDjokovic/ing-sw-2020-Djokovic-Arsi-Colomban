package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.Log;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.god.*;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;


import java.util.ArrayList;


public class Build extends GodPower {

    public Build(GodLogic godLogic) {
        super(godLogic);
    }

    public void power() {
        System.out.println("test message: Build, Build, Build your boat");
    }

    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("IM BUILDING");
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
        System.out.println("IM SELECTING FOR BUILDING");
        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 99, false, null, false);
        if (logger.getLastLog().getPlayerInit() == getGodLogic().getPlayer().getInitial()) {
            Log log;
            if (logger.getLastLog().getType() == 0) {
                log = logger.getLastLog();
            }
            else {
                log = logger.getSecondToLastLog();
            }
            int lastWorkerUsedX = log.getAction(2);
            int lastWorkerUsedY = log.getAction(3);
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
