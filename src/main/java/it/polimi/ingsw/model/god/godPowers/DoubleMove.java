package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.logger.Logger;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.god.GodLogic;

import java.util.ArrayList;

public class DoubleMove extends Move {

    private ArrayList<Integer> limitations;

    public DoubleMove(GodLogic godLogic, boolean canPass) {
        super(godLogic, canPass);
    }

    @Override
    public int power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        if (limitations == null) {
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(posXFrom);
            temp.add(posYFrom);
            this.limitations = temp;
        }
        else {
            limitations = null;
        }
        return super.power(board, posXFrom, posYFrom, posXTo, posYTo);
    }

    @Override
    public OptionSelection getOptions(Logger logger) {
        OptionSelection opt;
        if (logger.getLastLog().getPlayerInit() == getGodLogic().getPlayer().getInitial()) {
            opt =  getGodLogic().getOptionsGodLogic(1, 99, false, limitations, true);
            int lastWorkerUsedX = logger.getLastLog().getAction(2);     // X position dest
            int lastWorkerUsedY = logger.getLastLog().getAction(3);     // Y position dest
            for (ArrayList<Integer> a: opt.getComb()) {
                if (a.get(0) == lastWorkerUsedX && a.get(1) == lastWorkerUsedY) {
                    opt = new OptionSelection();
                    opt.setOptions(a);
                    break;
                }
            }
            System.out.println(opt);
        }
        else
            opt =  getGodLogic().getOptionsGodLogic(1, 99, false, null, false);
        return opt;
    }
}
