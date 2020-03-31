package it.polimi.ingsw.controller;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.view.View;

public class Controller implements Observer {

    private Game game;
    private View view;

    public Controller(Game game, View view) {
        this.game = game;
        this.view = view;
    }



    @Override
    public void update(String string) {

        System.out.println("Controller Update received: " + string);

        if (string.split(" ")[0].equals("PlayersName")) {
            game.initPlayers(string);
        }
        else if (string.split(" ")[0].equals("SelectedGod")) {
            if (string.split(" ")[1].equals("Basic")) {
                if (game.getPlayers().get(0).getGodLogic() == null)
                    game.getPlayers().get(0).setGodLogic("Basic").addObserver(view);
                else
                    game.getPlayers().get(1).setGodLogic("Basic").addObserver(view);
            }
            else
                throw new IllegalArgumentException();
        }
        else if (string.split(" ")[0].equals("PlacementWorker")) {
            int posX = Integer.parseInt(string.split(" ")[1]);
            int posY = Integer.parseInt(string.split(" ")[2]);
            try {
                if (!game.getBoard().getTile(posX, posY).hasWorker()) {
                    try {
                        if (game.getPlayers().get(0).getWorkersSize() < 2) {
                            game.getPlayers().get(0).addWorker(game.getBoard().getTile(posX, posY));
                        }
                        else {
                            game.getPlayers().get(1).addWorker(game.getBoard().getTile(posX, posY));
                        }
                    } catch (IllegalArgumentException | NonExistingTileException e) {
                        System.out.println("Impossible selection");
                    }
                }
                else
                    throw new IllegalArgumentException();
            }
            catch (NonExistingTileException e) {
                System.out.println("Non-existent tile!");
            }



        }
        else if (string.split(" ")[0].equals("WorkerSelection")) {
            System.out.println("Ah, you selected a worker. Just a sec, will implement soon");
        }
        else
            System.out.println("\n**********STRIKE STRIKE STRIKE YOU DID SOMETHING WRONG**************\nThe string was: " + string);
    }
}
