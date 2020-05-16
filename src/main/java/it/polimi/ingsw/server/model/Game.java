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

import static java.lang.Thread.sleep;

public class Game extends Observable{

    private ArrayList<Player> players;
    private Board board;
    private int numberOfPlayers = 2;    // Will let the players decide how many there are
    private Logger logger;

    public Game() {
        players = new ArrayList<>();
        board = new Board();
        logger = new Logger();
    }

    // For testing purposes
    public Board getBoard() {
        return board;
    }

    private int waitGodSelection = 0;
    private ArrayList<String> opt;

    public void initGods() {
        System.out.println("Initializing Process: Asking for God's name");

        if (opt == null) {
            opt = new ArrayList<>();
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
        }

        if (players.get(0).getGodLogic() == null) {
            updateObservers(new RequestPlayerGod(players.get(0).getInitial(), opt));
        }
        if (players.get(1).getGodLogic() == null) {
            updateObservers(new RequestPlayerGod(players.get(1).getInitial(), opt));
        }
        if (players.size() > 2 && players.get(2).getGodLogic() == null) {
            updateObservers(new RequestPlayerGod(players.get(2).getInitial(), opt));
        }
    }

    public void initWorker() {
        System.out.println("Init worker");
        printPlayersDescription();
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(0).getInitial()));
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(0).getInitial()));
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(1).getInitial()));
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(1).getInitial()));
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        if (players.size() > 2) {
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(2).getInitial()));
            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(2).getInitial()));
            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        }

        // Setting up otherGodLogic for each player (essential feature for Athena)
        for (Player player : players) {
            setOtherGodLogic(player);
        }
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        System.out.println("Everything is ready for the game!");

    }

    public void init() {
        /*
        updateObservers(new RequestPlayerName("First"));
        updateObservers(new RequestPlayerName("Second"));
        if (numberOfPlayers == 3)
            updateObservers(new RequestPlayerName("Third"));
        */
        /*
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
        */
        /*
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
            //System.out.println("Player " + count + " : " + p.toString());
            count++;
        }*/
        System.out.println("Setup completed. Let's play!\n");
    }

    private int currPlayer = 0;
    public void gameStart() {

        while (players != null  && players.size() != 1 && status != 2) {
            System.out.println("Executing turn!");
            players.get(currPlayer).executeTurn(this);
            System.out.println("Turn completed!\n");
        }
        if (players != null) {
            if (players.size() == 1)
                updateObservers(new RequestGameEnd(players.get(0).getInitial()));
            else
                updateObservers(new RequestGameEnd('*'));
        }
        gameEnd();

    }

    // Skip option
    public void gameReceiveOptions() {
        logger.addNewLog(players.get(currPlayer).getInitial());
        int status = players.get(currPlayer).playerReceiveOptions();
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }

    }

    public int status = -1;
    public void gameReceiveOptions(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        logger.addNewLog(posXFrom, posYFrom, posXTo, posYTo, players.get(currPlayer).getInitial());
        status = players.get(currPlayer).playerReceiveOptions(getBoard(), posXFrom, posYFrom, posXTo, posYTo);
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }
    }

    private void gameEnd() {
//        updateObservers(new RequestUpdateBoardView(board));   // TODO: end of game requests
//        updateObservers(new RequestDisplayBoard());
        System.out.println("End of the game!");
    }


    public char initPlayer(String newPlayerName) {
        if (players.size() == numberOfPlayers) {
            updateObservers(new RequestCriticalError('*'));
            System.out.println("ER DET MULIG, HERREGOD");
        }
        char initial = newPlayerName.charAt(0);

        Player newPlayer = new Player(newPlayerName, '*');

        if (players.size() == 0) {
            newPlayer.setInitial('A');
        }
        else if (players.size() == 1) {
            newPlayer.setInitial('B');
        }
        else {
            newPlayer.setInitial('C');
        }
//
//        if (players.size() > 0) {
//            int countStartingInitials = 0;
//            for (Player player : players) {
//                if (player.getInitial() == initial) {
//                    if (player.getInitial() == 'A') {
//                        initial = 'B';
//                        countStartingInitials++;
//                    }
//                    else if (player.getInitial() == 'B') {
//                        initial = 'A';
//                        countStartingInitials++;
//                    }
//                }
//            }
//            if (countStartingInitials != 2)
//                newPlayer.setInitial(initial);
//            else
//                newPlayer.setInitial('C');
//        }
//        if (newPlayer.getInitial() == '*')
//            newPlayer.setInitial(newPlayer.getName().charAt(0));
        players.add(newPlayer);
        System.out.println("\nIM ASSIGNING" + newPlayerName + " THIS INITIAL: " + newPlayer.getInitial() + "\n");
        System.out.println("Kommer du her?");

        printPlayersDescription();

        return newPlayer.getInitial();

    }

    public void killPlayer(char initial) {
        for (Player p: players) {
            if (p.getInitial() == initial) {
                p.delete();
                players.remove(p);
            }
            break;
        }
    }

    public void setPlayerGod(String godName, char initial) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard());
        opt.remove(godName);
        //initGods();
    }
//
//    public void setPlayerGod(String godName, String name) {
//        getPlayerFromName(name).setGodLogic(godName, logger, getBoard());
//    }

    public Player getPlayerFromName(String name) {
        for (Player p: players) {
            if (p.getName().equals(name))
                return p;
        }

        System.out.println("You are so bad, look at what you have done");
        return null;
    }

    private void setOtherGodLogic(Player player) {
        System.out.println("Setting up " + player.getName() + "other god logics:");
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

    public void killGame() {
        System.out.println("A used has disconnected, killing everything");
        players = null;     // Killing the whole thing
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

    // TODO: delete this horriblenesssss
    public Player getPlayerFromNumber(int i) {
        return players.get(i);
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

    public void getPrintableGameStatus() {
        printPlayersDescription();
        if (board != null)
            System.out.println("Board is on");

    }

    public void printPlayersDescription() {
        for (Player p : players) {
            if (p.getWorkersSize() == 2) {
                System.out.println(p);
            }
            else
                System.out.println("Player: " + p.getName() + " Initial: " + p.getInitial());
            if (p.getGodLogic() != null)
                System.out.println("Has god logic!");
            else
                System.out.println("It doesn't have a godLogic");
            System.out.println("Workers size: " + p.getWorkersSize());

        }
    }

}
