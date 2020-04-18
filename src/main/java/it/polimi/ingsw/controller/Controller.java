package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.View;

public class Controller implements Observer {

    private Game game;
    private View view;

    public Controller(Game game, View view) {
        this.game = game;
        this.view = view;
    }

    @Override
    public void update(Answer answer) {

        System.out.print("Controller Update received: ");
        answer.printMessage();
        answer.act(this);

    }

    public void initPlayer(String name) {
        game.initPlayer(name);
    }

    public void setPlayerGod(String godName, char initial) {
        game.setPlayerGod(godName, initial, view);
    }


    public void setWorker(int x, int y, char initial) {
        try {
            game.setWorker(x, y, initial);
        } catch (NonExistingTileException e) {
            System.out.println("Sth wrong");
        }
    }

    public void executePower(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("Controller has received: " + posXFrom + " " + posYFrom + " " + posXTo + " " + posYTo);
        game.gameReceiveOptions(posXFrom, posYFrom, posXTo, posYTo);
    }

    public void executePower() {
        System.out.println("Controller has received: Pass" );
        game.gameReceiveOptions();
    }

    @Override
    public void update(Request request) {
        System.out.println("Controller should not receive Requests");
    }
}
