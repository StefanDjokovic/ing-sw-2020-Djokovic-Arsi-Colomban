package it.polimi.ingsw;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;

import it.polimi.ingsw.view.View;


public class App {

    public static void main( String[] args ) {
        System.out.println("Starting the game!");


        Game game = new Game();
        // todo: delete game from View, make a message schema to send board details to view
        //      without accessing the model
        View view = new View(game);
        Controller controller = new Controller(game, view);

        // Set controller as Observer of view, set view as Observer of game
        view.addObserver(controller);
        game.addObserver(view);

        // Simple printing board method, to delete (?)
        view.displayBoard(game.getBoard());

        // Initializing the game components and states
        game.init();

        // Start the game
        game.gameStart();




    }
}
