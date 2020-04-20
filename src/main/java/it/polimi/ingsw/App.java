package it.polimi.ingsw;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewSimulated;


public class App {

    public static void main( String[] args ) {
        System.out.println("Starting the game!");

        for (int i = 0; i < 10000; i++) {
            Game game = new Game();
            View view = new View();
            // View view = new ViewSimulated(); //Uncomment this and comment view to get simulated matches

            Controller controller = new Controller(game, view);

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





    }
}
