package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.board.NonExistingTileException;

public class Controller implements Observer {

    private Game game;

    public Controller(Game game) {
        this.game = game;
    }

    @Override
    public void update(Answer answer) {
        System.out.println("Controller received answer");
        answer.printMessage();
        System.out.println("Yes. Look.");
        answer.act(this);
        System.out.println("Controller has completed its duties");
    }

    @Override
    public void update(Request request) {
        System.out.println("Controller should not receive Requests");
    }

    // Initializes Player's name
    public char initPlayer(String name) {
        return game.initPlayer(name);
    }

    // Setting the God picked by the client
    public void setPlayerGod(String godName, char initial) {
        game.setPlayerGod(godName, initial);
    }

    // Method called by ServerSocket when a Player is disconnected, and thus gets deleted from the game
    public void killPlayer(char initial) {
        game.deletePlayer(game.getPlayerFromInitial(initial));
    }

    // First asks for the gods, than workers, and then starts the game
    public void startGame() {
        game.initGods();
        game.initWorker();
        game.gameStart();
    }

    // Sets the worker in the right position
    public void setWorker(int x, int y, char initial) {
        try {
            System.out.println("Setting worker from controller: Initial: " + initial);
            System.out.println("Positions at: " + x + " " + y);
            game.setWorker(x, y, initial);
        } catch (NonExistingTileException e) {
            System.out.println("Sth wrong");
        }
    }

    // Executes the movement routine
    public void executeMoveOrBuild(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("Controller is executing Move");
        game.gameReceiveOptions(posXFrom, posYFrom, posXTo, posYTo);
    }

    // Executes the pass routine
    public void executePass() {
        System.out.println("Controller has received a pass");
        game.gameReceiveOptions();
    }
}
