package it.polimi.ingsw.model.god;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.model.god.godPowers.Build;
import it.polimi.ingsw.model.god.godPowers.GetMovableWorker;
import it.polimi.ingsw.model.god.godPowers.Move;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class GodLogic extends Observable {

    String godLogicName;
    ArrayList<GodPower> turn = new ArrayList<>();
    int moveUpMax = 1;
    int moveDownMax = 99;
    GodPower selectionStrategy;


    public GodLogic(String godLogic) {
        godLogicName = godLogic;
        if (godLogicName.equals("Basic")) {
            selectionStrategy = new GetMovableWorker(this);
            turn.add(new Move(this));
            turn.add(new Build(this));
        }
    }

    public void executeTurn(ArrayList<Worker> workers) {
        Worker selectedWorker = selectionStrategy.power(workers);
        for (GodPower power : turn) {
            power.power();
        }
    }

    public String getGodLogicName() {
        return godLogicName;
    }

    public int getMoveUpMax() {
        return moveUpMax;
    }

    public int getMoveDownMax() {
        return moveDownMax;
    }
}
