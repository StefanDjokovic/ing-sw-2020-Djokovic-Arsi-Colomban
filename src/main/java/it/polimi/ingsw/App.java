/*
package it.polimi.ingsw;



import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;

import it.polimi.ingsw.client.view.View;


public class App {

    public static void main( String[] args ) {
        System.out.println("Starting the game!");

        for (int i = 0; i < 1; i++) {
            Game game = new Game();
            View view = new View();
            // View view = new ViewSimulated(); //Uncomment this and comment view to get simulated matches

            Controller controller = new Controller(game);

            // Set controller as Observer of view, set view as Observer of game
            view.addObserver(controller);
            game.addObserver(view);

            // Simple printing board method, to delete (?)
            view.printSelectableBoard(null);

            // Initializing the game components and states
            game.init();

            // Start the game
            game.gameStart();
        }

        System.out.println("END");





    }
}
*/
