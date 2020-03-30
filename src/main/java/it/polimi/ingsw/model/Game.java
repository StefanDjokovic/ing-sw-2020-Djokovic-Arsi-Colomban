package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;

import java.util.ArrayList;

public class Game extends Observable {

    ArrayList<Player> players;
    Board board;


    public Game() {
        players = new ArrayList<>();
        board = new Board();
    }

    public void initPlayers(String playersDescription) {
        String[] temp = playersDescription.split(" ");
        players.add(new Player(temp[1], temp[2].charAt(0)));
        players.add(new Player(temp[3], temp[4].charAt(0)));

        printPlayersDescription();


    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void init() {

        System.out.println("\u001B[31m" + "\n\n\nPLEASE NOTE THE GAME IS STILL IN DEVELOPMENT\n" +
                "Debugging messages are active, the turn mechanic is not complete, and there are some bugs\n\n\n" +
                "\u001B[0m");
        updateObservers("Required Players Name");
        updateObservers("Required Players Gods");

        System.out.println("Time to place the Workers on the board!");
        updateObservers("Required Worker Position");

        System.out.println("Player 1: " + players.get(0).toString() );
        System.out.println("Player 2: " + players.get(1).toString() );

        System.out.println("Setup completed. Let's play!");
    }

    public void gameStart() {

        System.out.println("Note: only 4 turns are applied, this is still in testing");

        turnLogic();

        System.out.println("We have a winner! GG! Hope you enjoyed the game! :)");
    }

    public void turnLogic() {
        for (int i = 0; i < 2; i++) {   // TESTING PURPOSES: just 2 turns
            for (Player p : players)
                p.executeTurn();
        }
    }

    // For testing purposes
    public Board getBoard() {
        return board;
    }

    public void printPlayersDescription() {
        for (Player p : players) {
            System.out.println("Player: " + p.getName() + " Initial: " + p.getInitial());
        }
    }

}
