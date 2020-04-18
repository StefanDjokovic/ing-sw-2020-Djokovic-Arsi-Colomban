package it.polimi.ingsw.model.god;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestPowerCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.god.godPowers.*;
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
    boolean canPass = false;
    Logger logger;


    public GodLogic(String godLogic, Player p, Logger logger) {
        godLogicName = godLogic;
        this.player = p;
        this.logger = logger;
        if (godLogicName.equals("Basic")) {
            turn.add(new Move(this, false));
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Apollo")) {
            turn.add(new Swap(this, false));
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Artemis")) {
            GodPower db = new DoubleMove(this, false);
            turn.add(db);
            turn.add(db);
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Atlas")) {
            turn.add(new Move(this, false));
            turn.add(new Build(this, true));
            turn.add(new BuildDome(this, false));
        }

    }

    public void executeTurn(Game game) {
        System.out.println(logger);
        OptionSelection opt = turn.get(currStep).getOptions(logger);
        System.out.println("CURRENTLY CANPASS IS " + canPass);
        System.out.println(opt);
        if (opt != null) {
            this.optionSelection = opt;
            Request request = new RequestPowerCoordinates(opt, this.canPass);
            game.updateObservers(request);
        }
        else
            game.gameReceiveOptions();
    }

    public int godLogicReceiveOptions() {
        currStep++;
        if (currStep % turn.size() == 0) {
            currStep = 0;
            return 1;
        }
        return 0;
    }

    public int godLogicReceiveOptions(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
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

    public OptionSelection getOptionsGodLogic(int upDiff, int downDiff, boolean canIntoOpp, ArrayList<Integer> limitations, boolean canPass) {
        this.canPass = canPass;
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
