/*
package it.polimi.ingsw;



import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;

import it.polimi.ingsw.client.view.clientCLI;


public class App {

    public static void main( String[] args ) {
        System.out.println("Starting the game!");

        for (int i = 0; i < 100000; i++) {
            Game game = new Game();
            clientCLI view = new clientCLI();

            Controller controller = new Controller(game);

            // Set controller as Observer of view, set view as Observer of game
            view.addObserver(controller);
            game.addObserver(view);

            // Simple printing board method, to delete (?)
            view.printSelectableBoard(null);

            // Initializing the game components and states
            game.init();

            // Start the game
            game.gameTurnExecution();
        }

        System.out.println("END");





    }
}
*/
