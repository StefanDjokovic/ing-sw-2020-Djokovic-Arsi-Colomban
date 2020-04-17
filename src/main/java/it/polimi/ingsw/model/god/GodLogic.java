package it.polimi.ingsw.model.god;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestPowerCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.god.godPowers.Build;
import it.polimi.ingsw.model.god.godPowers.DoubleMove;
import it.polimi.ingsw.model.god.godPowers.Move;
import it.polimi.ingsw.model.god.godPowers.Swap;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

public class GodLogic extends Observable {

    String godLogicName;
    Player player;
    ArrayList<GodPower> turn = new ArrayList<>();
    int moveUpMax = 1;
    int moveDownMax = 99;
    int currStep = 0;
    OptionSelection optionSelection;
    int lastWorkerUsed = -1;


    public GodLogic(String godLogic, Player p) {
        godLogicName = godLogic;
        this.player = p;
        if (godLogicName.equals("Basic")) {
            turn.add(new Move(this));
            turn.add(new Build(this));
        }
        else if (godLogicName.equals("Apollo")) {
            turn.add(new Swap(this));
            turn.add(new Build(this));
        }
        else if (godLogicName.equals("Artemis")) {
            GodPower db = new DoubleMove(this);
            turn.add(db);
            turn.add(db);
            turn.add(new Build(this));
        }
    }

    public void executeTurn(Game game) {
        OptionSelection opt = turn.get(currStep).getOptions(lastWorkerUsed);
        System.out.println(opt);
        this.optionSelection = opt;
        Request request = new RequestPowerCoordinates(opt);
        game.updateObservers(request);
    }

    public int godLogicReceiveOptions(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        this.lastWorkerUsed = getPlayer().getWorkerIndexFromCoordinates(posXFrom, posYFrom);
        int status = turn.get(currStep).power(board, posXFrom, posYFrom, posXTo, posYTo);
        if (status == 2)
            return 2;
        currStep++;
        if (currStep % turn.size() == 0) {
            currStep = 0;
            return 1;
        }
        return 0;
    }

    public void deleteLastWorkerUsed() {
        lastWorkerUsed = -1;
    }

    public int lastWorkerUsedPosX(int lastWorkerUsed) {
        return getPlayer().getWorkers().get(lastWorkerUsed).getPosX();
    }
    public int lastWorkerUsedPosY(int lastWorkerUsed) {
        return getPlayer().getWorkers().get(lastWorkerUsed).getPosY();
    }

    public OptionSelection getOptionsGodLogic(int upDiff, int downDiff, boolean canIntoOpp, ArrayList<Integer> limitations) {
        return getPlayer().getOptionsPlayer(upDiff, downDiff, canIntoOpp, limitations);
    }

    public Player getPlayer() { return this.player; }

    public String getGodLogicName() {
        return godLogicName;
    }

    public int getMoveUpMax() {
        return moveUpMax;
    }

    public int getMoveDownMax() {
        return moveDownMax;
    }

    public ArrayList<GodPower> getTurn() {
        return this.turn;
    }
}
