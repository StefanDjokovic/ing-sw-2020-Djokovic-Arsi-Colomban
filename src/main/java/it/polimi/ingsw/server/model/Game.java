package it.polimi.ingsw.server.model;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.request.*;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.god.GodLogic;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.player.Worker;


import java.util.ArrayList;

public class Game extends Observable {

    private ArrayList<Player> players;
    private Board board;
    private int numberOfPlayers = 3;    // Will let the players decide how many there are
    private Logger logger;


    public Game(Player player1, Player player2) {
        players = new ArrayList<>();
        board = new Board();
        logger = new Logger();
        initPlayer(player1);
        initPlayer(player2);
    }

    // For testing purposes
    public Board getBoard() {
        return board;
    }

    public void init() {
        /*
        updateObservers(new RequestPlayerName("First"));
        updateObservers(new RequestPlayerName("Second"));
        if (numberOfPlayers == 3)
            updateObservers(new RequestPlayerName("Third"));
        */
        ArrayList<String> opt = new ArrayList<>();
        opt.add("Basic");
        opt.add("Apollo");
        opt.add("Artemis");
        opt.add("Atlas");
        opt.add("Pan");
        opt.add("Demeter");
        opt.add("Hephaestus");
        opt.add("Minotaur");
        opt.add("Prometheus");
        opt.add("Athena");

        for (Player player : players) {
            updateObservers(new RequestPlayerGod(player.getInitial(), opt));
        }

        for (Player player : players) {
            setOtherGodLogic(player);
        }


        System.out.println("Time to place the Workers on the board!");
        for (Player player : players) {
            for (int i = 0; i < 2; i++) {
                updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), player.getInitial()));
                updateObservers(new RequestUpdateBoardView(board));
                updateObservers(new RequestDisplayBoard());
            }
        }

        int count = 1;
        for (Player p: players) {
            System.out.println("Player " + count + " : " + p.toString());
            count++;
        }

        System.out.println("Setup completed. Let's play!");
    }

    private int currPlayer = 0;
    public void gameStart() {

        if (players.size() == 1) {
            gameEnd();
        }
        else {
            players.get(currPlayer).executeTurn(this);

        }
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

    private void gameEnd() {
        updateObservers(new RequestUpdateBoardView(board));
        updateObservers(new RequestDisplayBoard());
        System.out.println("We have a winner! GG! Hope you enjoyed the game! :)");
    }


    public void initPlayer(Player newPlayer) {
        if (players.size() == numberOfPlayers)
            updateObservers(new RequestCriticalError());
        char initial = newPlayer.getName().charAt(0);
        // Will update for 3 players with same name in the future
        if (players.size() > 0) {
            int countStartingInitials = 0;
            for (Player player : players) {
                if (player.getInitial() == initial) {
                    if (player.getInitial() == 'A') {
                        initial = 'B';
                        countStartingInitials++;
                    }
                    else if (player.getInitial() == 'B') {
                        initial = 'A';
                        countStartingInitials++;
                    }
                }
            }
            if (countStartingInitials != 2)
                newPlayer.setInitial(initial);
            else
                newPlayer.setInitial('C');
        }
        if (newPlayer.getInitial() == '*')
            newPlayer.setInitial(newPlayer.getName().charAt(0));
        players.add(newPlayer);

        printPlayersDescription();

    }

    public void setPlayerGod(String godName, char initial) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard());
    }

    private void setOtherGodLogic(Player player) {
        ArrayList<GodLogic> otherGodLogics = new ArrayList<>();

        for (Player q: players) {
            if (player.getInitial() != q.getInitial())
                otherGodLogics.add(q.getGodLogic());
        }

        player.getGodLogic().setOtherGodLogic(otherGodLogics);
    }

    public void deletePlayer(Player p) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == p) {
                players.get(i).delete();
                players.remove(p);
            }
        }
        currPlayer = currPlayer % players.size();
    }

    public void setWorker(int x, int y, char initial) throws NonExistingTileException {
        getPlayerFromInitial(initial).addWorker(getBoard().getTile(x, y));
    }

    private Player getPlayerFromInitial(char initial) {
        for (Player p : players) {
            if (p.getInitial() == initial)
                return p;
        }
        return null;
    }

    private int[][] getAllWorkersAsMatrix() {

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

    private void printPlayersDescription() {
        for (Player p : players) {
            System.out.println("Player: " + p.getName() + " Initial: " + p.getInitial());
        }
    }

}
