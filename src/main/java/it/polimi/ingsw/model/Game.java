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
    Logger logger;


    public Game() {
        players = new ArrayList<>();
        board = new Board();
        logger = new Logger();
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
                "Debugging messages are active\n\n\n" +
                "\u001B[0m");
        updateObservers(new RequestPlayerName("First"));
        updateObservers(new RequestPlayerName("Second"));
        if (numberOfPlayers == 3)
            updateObservers(new RequestPlayerName("Third"));

        ArrayList<String> opt = new ArrayList<>();
        opt.add("Basic");
        opt.add("Apollo");
        opt.add("Artemis");
        opt.add("Atlas");
        opt.add("Pan");
        opt.add("Demeter");
        opt.add("Hephaestus");
        opt.add("Minotaur");

        for (Player player : players) {
            updateObservers(new RequestPlayerGod(player.getInitial(), opt));
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

    int currPlayer = 0;
    public void gameStart() {

        System.out.println("Note: only 4 turns are applied, this is still in testing");

        printWorkerOptionPlayers();

        players.get(currPlayer).executeTurn(this);
    }

    // Skip option
    public void gameReceiveOptions() {
        logger.addNewLog(players.get(currPlayer).getInitial());
        int status = players.get(currPlayer).playerReceiveOptions();
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
            players.get(currPlayer).executeTurn(this);
        }
        else {
            players.get(currPlayer).executeTurn(this);
        }
    }


    public void gameReceiveOptions(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        logger.addNewLog(posXFrom, posYFrom, posXTo, posYTo, players.get(currPlayer).getInitial());
        int status = players.get(currPlayer).playerReceiveOptions(getBoard(), posXFrom, posYFrom, posXTo, posYTo);
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
            players.get(currPlayer).executeTurn(this);
        }
        else if (status == 0) {
            players.get(currPlayer).executeTurn(this);
        }
        else if (status == 2) {
            gameEnd();
        }
    }

    public void gameEnd() {
        updateObservers(new RequestDisplayBoard(getBoard()));
        System.out.println("We have a winner! GG! Hope you enjoyed the game! :)");
    }


    public void turnLogic() {
        for (int i = 0; i < 2; i++) {   // TESTING PURPOSES: just 2 turns
            for (Player p : players)
                p.executeTurn(this);
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
                        newPlayer.setInitial('B');
                    else
                        newPlayer.setInitial('A');
                }
            }
        }
        if (newPlayer.getInitial() == '*')
            newPlayer.setInitial(playerName.charAt(0));
        players.add(newPlayer);

        printPlayersDescription();

    }

    public void setPlayerGod(String godName, char initial, View view) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard()).addObserver(view);
    }

    public void setWorker(int x, int y, char initial) throws NonExistingTileException {
        getPlayerFromInitial(initial).addWorker(getBoard().getTile(x, y));
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

    public void printWorkerOptionPlayers() {
        for (Player p: players) {
            ArrayList<ArrayList<Tile>> result = p.getOptionTiles();
            System.out.println("Options player " + p.getName());
            for (ArrayList<Tile> a: result) {
                System.out.println("Options worker: " + a.get(0).toString());
                for (Tile t: a) {
                    System.out.println(t.toString());
                }
            }
        }
    }

    public void printPlayersDescription() {
        for (Player p : players) {
            System.out.println("Player: " + p.getName() + " Initial: " + p.getInitial());
        }
    }

}
