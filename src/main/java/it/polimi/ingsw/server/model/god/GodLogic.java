package it.polimi.ingsw.server.model.god;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
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
    private Board board;
    private boolean canPass = false;
    private Logger logger;
    private int upDiffDebuff = 0;
    private int downDiffDebuff = 0;
    private boolean hasDebuff = false;
    private ArrayList<GodLogic> otherGodLogic;


    public GodLogic(String godLogic, Player p, Logger logger, Board board) {
        godLogicName = godLogic;
        this.player = p;
        this.logger = logger;
        this.board = board;

        // TODO: pull this information from JSON file
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
                turn.add(new Move(this, false));
                turn.add(new DoubleMove(this, true));
                turn.add(new Build(this, false));
                break;
            case "Atlas":
                turn.add(new Move(this, false));
                turn.add(new Build(this, true));
                turn.add(new BuildDome(this, false));
                break;
            case "Pan":
                turn.add(new MoveWithSpecialWinCondition(this, false));
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
                turn.add(new MoveLimitedOnFirstBuild(this, false));
                turn.add(new Build(this, false));
                break;
            case "Athena":
                turn.add(new MoveLimiter(this, false));
                turn.add(new Build(this, false));
                break;
            case "Zeus":
                turn.add(new Move(this, false));
                turn.add(new BuildUnderneath(this, false));
                break;
            case "Ares":
                turn.add(new Move(this, false));
                turn.add(new Build(this, true));
                turn.add(new Destroy(this, false));
                break;
            case "Hestia":
                turn.add(new Move(this, false));
                turn.add(new Build(this, false));
                turn.add(new BuildLimited(this, true));
                break;
            case "Poseidon":
                turn.add(new Move(this, false));
                turn.add(new Build(this, false));
                turn.add(new BuildMultipleWithCondition(this, true));
                turn.add(new BuildMultipleWithCondition(this, true));
                break;
            case "Charon":
                turn.add(new Teleport(this, true));
                turn.add(new MoveLimitedOnFirstTeleport(this, false));
                turn.add(new Build(this, false));
                break;
        }
    }

    /**
     * Executes the turn routine, creating all the messages that the model will have to send to the view to update
     * the view's board, to request the power coordinates.
     * @param game model's main object, that initializes everything else
     */
    public int executeTurn(Game game) {
        OptionSelection opt = turn.get(currStep).getOptions(logger);
        if (opt != null) {
            if (opt.hasOptions() || turn.get(currStep).getCanPass()) {
                opt.compressUselessOptions();
                System.out.println(opt);
                RequestUpdateBoardView RequestUpdateBoardView = new RequestUpdateBoardView(new BoardView(board), '*');
                game.updateObservers(RequestUpdateBoardView);
                Request request = new RequestPowerCoordinates(opt, this.canPass, player.getInitial());
                System.out.println("\u001B[101m" +  "Sending stuff" + "\u001B[0m");
                game.updateObservers(request);
            }
            else {
                System.out.println("THIS BOY HAS LOST! Initial: " + getPlayer().getInitial());
                game.reportLossPlayer(getPlayer());
            }
        }
        else {
            game.gameReceiveOptions();
            return 1;
        }
        return 0;
    }

    /**
     * Executes the power from "clean" options, notifying the caller at the end
     * of the execution if the power usage wins the game
     * @param board variable that contains the state of the board
     * @param posXFrom x coordinate of the worker that is going to use the power
     * @param posYFrom y coordinate of the worker that is going to use the power
     * @param posXTo x coordinate of the tile targeted by the power
     * @param posYTo y coordinate of the tile targeted by the power
     * @return 2 if the power usage wins the game,  1 if the turn step was the last one of the routine,
     *         0 if there are other steps afterwards
     */
    public int godLogicReceiveOptions(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        int status = turn.get(currStep).power(board, posXFrom, posYFrom, posXTo, posYTo);
        if (status == 2)
            return 2;
        currStep++;
        if (currStep % turn.size() == 0) {
            currStep = 0;
            this.hasDebuff = false; // resets the debuff state
            return 1;
        }
        return 0;
    }

    public int levelWorker(int x, int y) {
        return player.getWorkerLevel(x, y);
    }
    /**
     * Executes one step of the turn when the player passes
     * @return 1 if the turn step was the last one of the routine, 0 if there are other steps afterwards
     */
    public int godLogicReceiveOptions() {
        currStep++;
        if (currStep % turn.size() == 0) {
            currStep = 0;
            return 1;
        }
        return 0;
    }

    /**
     * Debuffs opponents when a particular power is used
     * @param upDiffDebuff "magnitude" of the debuff
     */
    public void debuffOpponents(int upDiffDebuff) {
        for (GodLogic g: otherGodLogic) {
            g.upDiffDebuff = upDiffDebuff;
            g.hasDebuff = true;
        }
    }


    /**
     * @param upDiff how many levels it may move upwards
     * @param downMin minimum level of downwards
     * @param canIntoOpp if it may move into an opponent
     * @param limitations if there are particular cell where it may not move
     * @param canPass if the power can be skipped
     * @return OptionSelection contains the cells in which the player may decide to move
     */
    public OptionSelection getOptionsGodLogic(int upDiff, int downMin, boolean canIntoOpp, ArrayList<Integer> limitations, boolean canPass) {
        this.canPass = canPass;
        if (!hasDebuff || upDiff > 4)   // upDiff > 4 means it's a build power, which is not affected by any debuff
            return getPlayer().getOptionsPlayer(upDiff, downMin, canIntoOpp, limitations);
        else {
            return getPlayer().getOptionsPlayer(this.upDiffDebuff, this.downDiffDebuff, canIntoOpp, limitations);
        }
    }

    public int getLevelTile(int x, int y) {
        try {
            return board.getTile(x, y).getBuildingLevel();
        } catch (NonExistingTileException e) {
            System.out.println("This should really not happen");
        }
        return -1;
    }

    /**
     * Gets the building level on a specified tile
     * @param posX x coordinate of the tile
     * @param posY y coordinate of the tile
     * @return building level of the tile
     */
    public int getBuildingLevel(int posX, int posY) {
        try {
            return board.getTile(posX, posY).getBuildingLevel();
        } catch (NonExistingTileException e) {
            System.out.println("You dummy dum");
        }
        return -99;
    }

    /**
     * Says if there's a worker on a specific tile
     * @param posX x coordinate of the tile
     * @param posY y coordinate of the tile
     * @return true if there's a worker on the tile, false if there isn't one
     */
    public boolean hasOpposingOpponentWorker(int posX, int posY) {
        try {
            if (board.getTile(posX, posY).hasWorker()) {
                return board.getTile(posX, posY).getWorker().getOwner().getInitial() != getPlayer().getInitial();
            }
        } catch (NonExistingTileException e) {
            System.out.println("no no no no!");
        }
        return false;
    }

    /**
     * Says if the tile directly behind the destination tile is free
     * @param XFrom x coordinate of the starting tile
     * @param YFrom y coordinate of the starting tile
     * @param XTo x coordinate of the destination tile
     * @param YTo y coordinate of the destination tile
     * @return true if the tile is free, false if it isn't
     */
    public boolean isBehindFree(int XFrom, int YFrom, int XTo, int YTo) {
        int dx = XTo - XFrom;
        int dy = YTo - YFrom;
        if (XTo + dx >= 0 && XTo + dx <= 4 && YTo + dy >= 0 && YTo + dy <= 4) {
            try {
                if (board.getTile(XTo + dx, YTo + dy).isWalkable())
                    return true;
            } catch (NonExistingTileException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Getter method for player attribute
     * @return the object's player attribute
     */
    public Player getPlayer() { return this.player; }

    /**
     * Getter method for godLociName attribute
     * @return the object's godLogicName attribute
     */
    public String getGodLogicName() {
        return godLogicName;
    }

    /**
     * Setter method for player attribute
     * @param canPass flag, if true the move can be skipped, if false it can't be skipped
     */
    public void setPass(boolean canPass) {
        this.canPass = canPass;
    }

    /**
     * Setter method for player attribute
     * @param otherGodLogic contains the godLogic of the other opponents, so it may debuff them if particular powers
     *                      are activated
     */
    public void setOtherGodLogic(ArrayList<GodLogic> otherGodLogic) { this.otherGodLogic = otherGodLogic; }

}
