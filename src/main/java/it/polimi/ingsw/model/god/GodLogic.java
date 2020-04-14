package it.polimi.ingsw.model.god;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestMoveCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.god.godPowers.Build;
import it.polimi.ingsw.model.god.godPowers.GetMovableWorker;
import it.polimi.ingsw.model.god.godPowers.Move;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;

import java.util.ArrayList;

public class GodLogic extends Observable {

    String godLogicName;
    ArrayList<GodPower> turn = new ArrayList<>();
    int moveUpMax = 1;
    int moveDownMax = 99;
    GodPower selectionStrategy; // temporary, to place in turn


    public GodLogic(String godLogic) {
        godLogicName = godLogic;
        if (godLogicName.equals("Basic")) {
            selectionStrategy = new GetMovableWorker(this);
            turn.add(new Move(this));
            turn.add(new Build(this));
        }
    }

    // it is not a complete function, will fix soon
    public void executeTurn(ArrayList<Worker> workers, Game game) {
        Worker selectedWorker = selectionStrategy.power(workers);
        Request request = new RequestMoveCoordinates(selectedWorker);
        game.updateObservers(request);
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
