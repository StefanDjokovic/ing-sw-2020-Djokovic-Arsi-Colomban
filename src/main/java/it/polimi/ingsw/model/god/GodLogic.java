package it.polimi.ingsw.model.god;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestPowerCoordinates;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Logger;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.god.godPowers.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

public class GodLogic extends Observable {

    String godLogicName;
    Player player;
    ArrayList<GodPower> turn = new ArrayList<>();
    int currStep = 0;
    OptionSelection optionSelection;
    Board board;
    boolean canPass = false;
    Logger logger;
    int upDiffDebuff = 0;
    int downDiffDebuff = 99;
    boolean hasDebuff = false;
    ArrayList<GodLogic> otherGodLogic;


    public GodLogic(String godLogic, Player p, Logger logger, Board board) {
        godLogicName = godLogic;
        this.player = p;
        this.logger = logger;
        this.board = board;

        // Initializing Turn Schema
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
        else if (godLogicName.equals("Pan")) {
            turn.add(new MoveWithSpecialWinCondition(this, false, 2, 0));
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Demeter")) {
            turn.add(new Move(this, false));
            turn.add(new Build(this, false));
            turn.add(new BuildWithLimitation(this, true));
        }
        else if (godLogicName.equals("Hephaestus")) {
            turn.add(new Move(this, false));
            turn.add(new Build(this, false));
            turn.add(new BuildOverOneTile(this, true));
        }
        else if (godLogicName.equals("Minotaur")) {
            turn.add(new Push(this, false));
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Prometheus")) {
            turn.add(new Build(this, true));
            turn.add(new MoveLimited(this, false));
            turn.add(new Build(this, false));
        }
        else if (godLogicName.equals("Athena")) {
            turn.add(new MoveLimiter(this, false));
            turn.add(new Build(this, false));
        }
    }

    public void setOtherGodLogic(ArrayList<GodLogic> otherGodLogic) {
        this.otherGodLogic = otherGodLogic;
    }

    public void executeTurn(Game game) {
        System.out.println(logger);
        OptionSelection opt = turn.get(currStep).getOptions(logger);
        System.out.println("CURRENTLY CANPASS IS " + canPass);
        System.out.println(opt);
        if (opt != null) {
            if (hasOptions(opt)) {
                this.optionSelection = opt;
                Request request = new RequestPowerCoordinates(opt, this.canPass);
                game.updateObservers(request);
            }
            else {
                System.out.println("THIS BOY HAS LOST!");
                game.deletePlayer(getPlayer());
                game.gameStart();
            }
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

    public void limitOpp(int upDiffDebuff) {
        for (GodLogic g: otherGodLogic) {
            System.out.println("------------" + otherGodLogic.size());
            g.upDiffDebuff = upDiffDebuff;
            g.hasDebuff = true;
        }
    }

    public ArrayList<GodLogic> getOtherGodLogic() {
        return otherGodLogic;
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
        if (!hasDebuff || upDiff > 4)
            return getPlayer().getOptionsPlayer(upDiff, downDiff, canIntoOpp, limitations);
        else {
            this.hasDebuff = false;
            return getPlayer().getOptionsPlayer(this.upDiffDebuff, this.downDiffDebuff, canIntoOpp, limitations);
        }
    }

    public Player getPlayer() { return this.player; }

    public String getGodLogicName() {
        return godLogicName;
    }

    public void setPass(boolean canPass) {
        this.canPass = canPass;
    }

    public ArrayList<GodPower> getTurn() {
        return this.turn;
    }

    public int tileBuildingLevel(int posX, int posY) {
        try {
            return board.getTile(posX, posY).getBuildingLevel();
        } catch (NonExistingTileException e) {
            System.out.println("You dummy dum");
        }
        return -99;
    }

    public boolean hasOpposingWorker(int posX, int posY) {
        try {
            if (board.getTile(posX, posY).hasWorker()) {
                if (board.getTile(posX, posY).getWorker().getOwner().getInitial() != getPlayer().getInitial())
                    return true;
                else
                    return false;
            }
        } catch (NonExistingTileException e) {
            System.out.println("no no no no!");
        }
        return false;
    }

    public boolean isBehindFree(int XFrom, int YFrom, int XTo, int YTo) {
        int dx = XTo - XFrom;
        int dy = YTo - YFrom;
        if (XTo + dx >= 0 && XTo + dx <= 4 && YTo + dy >= 0 && YTo + dy <= 4) {
            try {
                if (board.getTile(XTo + dx, YTo + dy).isWalkable())
                    return true;
            } catch (NonExistingTileException e) {
                e.printStackTrace();
                System.out.println("Er det mulig?!?");
            }
        }
        return false;

    }

    public boolean hasOptions(OptionSelection opt) {
        for (ArrayList<Integer> o: opt.getComb()) {
            if (o.size() > 2)
                return true;
        }
        return false;
    }
}
