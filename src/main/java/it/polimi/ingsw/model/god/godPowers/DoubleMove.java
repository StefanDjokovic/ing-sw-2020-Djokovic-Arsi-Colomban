package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class DoubleMove extends Move {

    ArrayList<Integer> limitations;

    public DoubleMove(GodLogic godLogic) {
        super(godLogic);
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
    public OptionSelection getOptions(int lastWorkerUsed) {
        System.out.println("IM SELECTING FOR DOUBLEMOVING");
        OptionSelection opt;
        if (lastWorkerUsed != -1) {
            opt =  getGodLogic().getOptionsGodLogic(1, 99, false, limitations, true);
            int lastWorkerUsedX = getGodLogic().lastWorkerUsedPosX(lastWorkerUsed);
            int lastWorkerUsedY = getGodLogic().lastWorkerUsedPosY(lastWorkerUsed);
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
            opt =  getGodLogic().getOptionsGodLogic(1, 99, false, limitations, false);
        return opt;
    }
}
