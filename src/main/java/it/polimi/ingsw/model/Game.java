package it.polimi.ingsw.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.request.*;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;

public class Game extends Observable {

    ArrayList<Player> players;
    Board board;
    int numberOfPlayers = 2;    // Will let the players decide how many there are


    public Game() {
        players = new ArrayList<>();
        board = new Board();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    // For testing purposes
    public Board getBoard() {
        return board;
    }

    public void init() {

        System.out.println("\u001B[31m" + "\n\n\nPLEASE NOTE THE GAME IS STILL IN DEVELOPMENT\n" +
                "Debugging messages are active, the turn mechanic is not complete, and there are some bugs\n\n\n" +
                "\u001B[0m");
        updateObservers(new RequestPlayerName("First"));
        updateObservers(new RequestPlayerName("Second"));
        if (numberOfPlayers == 3)
            updateObservers(new RequestPlayerName("Third"));

        for (Player player : players) {
            updateObservers(new RequestPlayerGod(player.getInitial()));
        }

        System.out.println("Time to place the Workers on the board!");
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                updateObservers(new RequestWorkerPosition(getAllWorkersAsMatrix(), player.getInitial()));
                updateObservers(new RequestDisplayBoard(board));
            }
        }

        int count = 1;
        for (Player p: players) {
            System.out.println("Player " + count + " : " + p.toString());
            count++;
        }

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

    public void initPlayer(String playerName) {
        Player newPlayer = new Player(playerName, '*');
        if (players.size() == numberOfPlayers)
            updateObservers(new RequestCriticalError());
        // Will update for 3 players with same name in the future
        if (players.size() > 0) {
            for (Player player : players) {
                if (player.getInitial() == playerName.charAt(0)) {
                    if (player.getInitial() == 'A')
                        newPlayer.setInital('B');
                    else
                        newPlayer.setInital('A');
                }
            }
        }
        if (newPlayer.getInitial() == '*')
            newPlayer.setInital(playerName.charAt(0));
        players.add(newPlayer);

        printPlayersDescription();

    }

    public void setPlayerGod(String godName, char initial, View view) {
        getPlayerFromInitial(initial).setGodLogic("Basic").addObserver(view);
    }

    public void setWorker(int x, int y, char initial) {
        try {
            getPlayerFromInitial(initial).addWorker(getBoard().getTile(x, y));
        } catch (NonExistingTileException e) {
            System.out.println("Oof getTile got it wrong");
        }
    }

    public Player getPlayerFromInitial(char initial) {
        for (Player p : players) {
            if (p.getInitial() == initial)
                return p;
        }
        return null;
    }

    public int[][] getAllWorkersAsMatrix() {

        int nWorkers = 0;
        for (Player p : players) {
            nWorkers += p.getWorkersSize();
        }
        if (nWorkers == 0)
            return null;

        int[][] returnMatrix = new int[nWorkers][2];

        nWorkers = 0;

        for (Player p : players) {
            for (Worker w : p.getWorkers()) {
                returnMatrix[nWorkers][0] = w.getPosX();
                returnMatrix[nWorkers][1] = w.getPosY();
                nWorkers++;
            }
        }

        return returnMatrix;

    }

    public void printPlayersDescription() {
        for (Player p : players) {
            System.out.println("Player: " + p.getName() + " Initial: " + p.getInitial());
        }
    }

}
