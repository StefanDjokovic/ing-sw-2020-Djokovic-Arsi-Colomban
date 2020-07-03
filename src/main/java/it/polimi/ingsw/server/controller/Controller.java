package it.polimi.ingsw.server.controller;

import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.board.NonExistingTileException;

import java.util.ArrayList;

public class Controller implements Observer, ControllerView {

    private Game game;
    private Request waitingAnswer;

    /**
     * Constructor
     * @param game model's main object, that initializes everything else
     */
    public Controller(Game game) {
        this.game = game;
    }

    /**
     * Gets called when of the Controller's observed objects (view) updates its observers,
     * it makes the controller act based on the message that the observee sent
     * @param answer message sent by observed object that tells the controller which method to call
     */
    @Override
    public void update(Answer answer) {
        // We check with the last request sent if it's valid
        answer.printMessage();
        if (waitingAnswer == null || answer instanceof AnswerKillPlayer ||
                (waitingAnswer.isValidAnswer(answer) && waitingAnswer.getInitial() == answer.getInitial())) {
            // We can now act
            answer.act(this);
        }
        else {
            System.out.println("Invalid Answer! Reason:");
            if (waitingAnswer != null) {
                waitingAnswer.printMessage();
                answer.printMessage();
            }
            else {
                System.out.println("Some weird stuff");
            }

            // TODO: decide what to do in case the answer is wrong, for now it's just hanging
        }


    }

    @Override
    public void update(Request request) {
        // We keep the last request sent to check with the answer
        if (!request.isAsync()) {
            waitingAnswer = request;
        }
    }

    /**
     * Initializes the player's name
     * @param name player's name
     * @return unique initial for the player (A, B or C)
     */
    public char initPlayer(String name) {
        return game.initPlayer(name);
    }


    /**
     * Initializes the gods, that asks for the workers and then starts the game
     */
    public void initGods() {
        game.initGods();
    }

    /**
     * Sets the god card picked by the player
     * @param godNames list of the gods picked by the first player
     * @param initial player's initial
     */
    public void setPlayerGod(ArrayList<String> godNames, char initial) {
        game.setPlayerGods(godNames);
        initGodsOrContinue();
    }

    /**
     * Sets the god card picked by the player
     * @param godName name of the god picked by the player
     * @param initial player's initial
     */
    public void setPlayerGod(String godName, char initial) {
        game.setPlayerGod(godName, initial);
        initGodsOrContinue();
    }


    /**
     * If there are still players with uninitialized gods it initializes them,
     * otherwise it initializes the workers
     */
    public void initGodsOrContinue() {
        if (game.nPlayersWithGod() == game.nPlayers() - 1) {
            game.initLastGod();
            game.initWorker();
        }
        else if (game.nPlayersWithGod() != game.nPlayers())
            game.initGods();
        else
            game.initWorker();
    }

    /**
     * Initializes the worker on the tile picked by the player
     * @param x x coordinate of the selected tile
     * @param y y coordinate of the selected tile
     * @param initial player's initial
     */
    public void setWorker(int x, int y, char initial) {
        try {
            System.out.println("Setting worker from controller: Initial: " + initial);
            System.out.println("Positions at: " + x + " " + y);
            game.setWorker(x, y, initial);
        } catch (NonExistingTileException e) {
            System.out.println("Sth wrong");
        }
        initWorkerOrContinue();
    }

    /**
     * If there are still players with uninitialized workers it initializes them,
     * otherwise it starts the game
     */
    public void initWorkerOrContinue() {
        if (game.missingWorkers()) {
            game.initWorker();
        }
        else {
            game.setOtherGodLogic();
            game.gameTurnExecution();
        }
    }

    /**
     * Executes a turn step, using a power from a starting tile to a destination tile
     * @param posXFrom x coordinate of the starting tile
     * @param posYFrom y coordinate of the starting tile
     * @param posXTo x coordinate of the destination tile
     * @param posYTo y coordinate of the destination tile
     */
    public void executeMoveOrBuild(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("Controller is executing the action");
        game.gameReceiveOptions(posXFrom, posYFrom, posXTo, posYTo);
        game.gameTurnExecution();
    }

    /**
     * Executes a turn step when the player passes
     */
    public void executePass() {
        System.out.println("Controller has received a pass");
        game.gameReceiveOptions();
        game.gameTurnExecution();
    }

    /**
     * Called by ServerSocket when a player disconnects, it deletes the player from the game
     * @param initial initial of the player that needs to be deleted
     */
    public void killPlayer(char initial) {
        boolean isCurrent = game.deletePlayer(game.getPlayerFromInitial(initial));
        if (isCurrent)
            gameContinueOnKillPlayer();
    }

    /**
     * Called after a player is removed from the game. If there are still players with uninitialized gods, it initializes them.
     * If there are still players with uninitialized workers, it initializes them.
     * Otherwise, it starts the game
     */
    public void gameContinueOnKillPlayer() {
        if (game.nPlayersWithGod() != game.nPlayers()) {
            game.initGods();
        }
        else if (game.missingWorkers()) {
            game.initWorker();
        }
        else {
            game.gameTurnExecution();
        }
    }
}
