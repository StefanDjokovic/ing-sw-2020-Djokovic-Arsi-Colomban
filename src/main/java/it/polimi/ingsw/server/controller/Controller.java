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

    public char initPlayer(String name) {
        return game.initPlayer(name);
    }

    public void setPlayerGod(String godName, char initial) {
        System.out.println("ARE WE HERE???");
        game.setPlayerGod(godName, initial);
        System.out.println("hmm???");
    }

    public void killPlayer(char initial) {
        game.deletePlayer(game.getPlayerFromInitial(initial));
    }

    public void killGame() {
        game.killGame();
    }

    public void initProcess() {
        game.initGods();
        game.initWorker();
        game.gameStart();
    }


    public void setWorker(int x, int y, char initial) {
        try {
            System.out.println("Setting worker from controller: Initial: " + initial);
            System.out.println("Positions at: " + x + " " + y);
            game.setWorker(x, y, initial);
        } catch (NonExistingTileException e) {
            System.out.println("Sth wrong");
        }
    }

    public void executePower(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("Controller is executing its power");
        // System.out.println("Controller has received: " + posXFrom + " " + posYFrom + " " + posXTo + " " + posYTo);
        game.gameReceiveOptions(posXFrom, posYFrom, posXTo, posYTo);
    }

    public void executePower() {
        // System.out.println("Controller has received: Pass");
        game.gameReceiveOptions();
    }

    public void debugMessage() {
        System.out.println("Controller is ON");
    }

    @Override
    public void update(Request request) {
        System.out.println("Controller should not receive Requests");
    }
}
