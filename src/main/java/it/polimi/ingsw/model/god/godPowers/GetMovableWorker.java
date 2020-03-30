package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.model.god.GodLogic;
import it.polimi.ingsw.model.god.GodPower;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class GetMovableWorker extends GodPower {

    public GetMovableWorker(GodLogic godLogic) {
        super(godLogic);
    }

    @Override
    public Worker power(ArrayList<Worker> workers) {
        System.out.println("Now we will try to choose a worker, and only the worker that can move will be available");
        int currMoveUpMax = getGodLogic().getMoveUpMax();
        int currMoveDownMax = getGodLogic().getMoveDownMax();
        ArrayList<Integer> options = new ArrayList<>(); //BLEAH, BUT MY MIND IS NOT FUNCTIONING PROPERLY RN

        for (Worker w : workers) {
            if (w.getPosTile().getMovableToNeigh(currMoveUpMax, currMoveDownMax) != null) {
                System.out.println("TESTING: " + w.getPosX());
                options.add(w.getPosX());
                options.add(w.getPosY());
            }
        }
        System.out.println("JUST CHECKING HOW IT LOOKS" + options.toString());
        String toPass = "Required Pick Worker ";
        if (options.size() == 0) {
            System.out.println("OMG YOU LOSE HAHAHA");
            return null;
        }
        else
            toPass = toPass + options.get(0) + " " + options.get(1);

        if (options.size() > 2)
            toPass = toPass + " " + options.get(2) + " " + options.get(3);

        System.out.println(toPass);
        getGodLogic().updateObservers(toPass);

        return null;
    }
}
