package it.polimi.ingsw.server.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestCriticalError;
import it.polimi.ingsw.messages.request.RequestDisplayBoard;
import it.polimi.ingsw.messages.request.RequestPowerCoordinates;
import it.polimi.ingsw.messages.request.RequestUpdateBoardView;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.godPowers.*;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.board.Board;

import java.util.ArrayList;

public class GodLogic {

    private String godLogicName;
    private Player player;
    private ArrayList<GodPower> turn = new ArrayList<>();
    private int currStep = 0;
    OptionSelection optionSelection;
    private Board board;
    private boolean canPass = false;
    private Logger logger;
    private int upDiffDebuff = 0;
    private int downDiffDebuff = 99;
    private boolean hasDebuff = false;
    private ArrayList<GodLogic> otherGodLogic;


    public GodLogic(String godLogic, Player p, Logger logger, Board board) {
        godLogicName = godLogic;
        this.player = p;
        this.logger = logger;
        this.board = board;

        // Initializing Turn Schema
        switch (godLogicName) {
            case "Basic":
                turn.add(new Move(this, false));
                turn.add(new Build(this, false));
                break;
            case "Apollo":
                turn.add(new Swap(this, false));
                turn.add(new Build(this, false));
                break;
            case "Artemis":
                GodPower db = new DoubleMove(this, false);
                turn.add(db);
                turn.add(db);
                turn.add(new Build(this, false));
                break;
            case "Atlas":
                turn.add(new Move(this, false));
                turn.add(new Build(this, true));
                turn.add(new BuildDome(this, false));
                break;
            case "Pan":
                turn.add(new MoveWithSpecialWinCondition(this, false, 2, 0));
                turn.add(new Build(this, false));
                break;
            case "Demeter":
                turn.add(new Move(this, false));
                turn.add(new Build(this, false));
                turn.add(new BuildWithLimitation(this, true));
                break;
            case "Hephaestus":
                turn.add(new Move(this, false));
                turn.add(new Build(this, false));
                turn.add(new BuildOverOneTile(this, true));
                break;
            case "Minotaur":
                turn.add(new Push(this, false));
                turn.add(new Build(this, false));
                break;
            case "Prometheus":
                turn.add(new Build(this, true));
                turn.add(new MoveLimited(this, false));
                turn.add(new Build(this, false));
                break;
            case "Athena":
                turn.add(new MoveLimiter(this, false));
                turn.add(new Build(this, false));
                break;
        }
    }

    public void setOtherGodLogic(ArrayList<GodLogic> otherGodLogic) {
        this.otherGodLogic = otherGodLogic;
    }

    public void executeTurn(Game game) {
        OptionSelection opt = turn.get(currStep).getOptions(logger);
        if (opt != null) {
            game.printPlayersDescription();
            if (hasOptions(opt)) {
                this.optionSelection = opt;
                RequestUpdateBoardView RequestUpdateBoardView = new RequestUpdateBoardView(new BoardView(board), player.getInitial());
                Request request = new RequestPowerCoordinates(opt, this.canPass, player.getInitial(), RequestUpdateBoardView);
                System.out.println("Sending stuff");
                //BoardView boardView = new BoardView(board);
//                game.updateObservers(new RequestUpdateBoardView(new BoardView(board), player.getInitial()));
//                System.out.println("Please, display the board");
//                game.updateObservers(new RequestDisplayBoard(player.getInitial()));
//                System.out.println("Sending the real request");
                game.updateObservers(request);

                System.out.println("here?");
            }
            else {
                System.out.println("THIS BOY HAS LOST!");
                game.deletePlayer(getPlayer());
                game.updateObservers(new RequestUpdateBoardView(board, player.getInitial()));
                game.updateObservers(new RequestDisplayBoard(player.getInitial()));
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
        System.out.println("OTHERGODLOGIC SIZE : " + otherGodLogic.size());
        for (GodLogic g: otherGodLogic) {
            g.upDiffDebuff = upDiffDebuff;
            g.hasDebuff = true;
        }
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
                return board.getTile(posX, posY).getWorker().getOwner().getInitial() != getPlayer().getInitial();
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

    private boolean hasOptions(OptionSelection opt) {
        for (ArrayList<Integer> o: opt.getValues()) {
            if (o.size() > 2)
                return true;
        }
        return false;
    }
}
