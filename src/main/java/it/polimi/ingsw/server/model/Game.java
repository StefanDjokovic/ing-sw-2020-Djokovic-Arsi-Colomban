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

    // TODO: initialise opt through JSON file
    // Sends to ServerSocket the Request for the God the users pick
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

        updateObservers(new RequestPlayerGod(players.get(0).getInitial(), opt));
        updateObservers(new RequestPlayerGod(players.get(1).getInitial(), opt));
        if (players.size() > 2 && players.get(2).getGodLogic() == null) {
            updateObservers(new RequestPlayerGod(players.get(2).getInitial(), opt));
        }
    }

    // Sending sequentially Requests for the workers placement
    public void initWorker() {
        System.out.println("Init worker");
        printPlayersDescription();
        for (int i = 0; i < players.size(); i++) {
            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(i).getInitial()));
            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(i).getInitial()));
        }
        updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));

        // Setting up otherGodLogic for each player (essential feature for Athena)
        setOtherGodLogic();

        System.out.println("Everything is ready for the game!");
    }

    // Method that starts the whole game logic
    private int currPlayer = 0;
    public void gameStart() {
        while (players != null  && players.size() != 1 && status != 2) {    // status == 2 when someone has won
            players.get(currPlayer).executeTurn(this);
        }
        if (players != null) {
            if (players.size() == 1)
                updateObservers(new RequestGameEnd(players.get(0).getInitial()));
            else {
                updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
                updateObservers(new RequestGameEnd('*'));
            }
        }
        gameEnd();
    }

    // Pushes the option to player and sets the new currPlayer if it changes
    public int status = -1;
    public void gameReceiveOptions(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        logger.addNewLog(posXFrom, posYFrom, posXTo, posYTo, players.get(currPlayer).getInitial());
        status = players.get(currPlayer).playerReceiveOptions(getBoard(), posXFrom, posYFrom, posXTo, posYTo);
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }
    }

    // gameReceiveOptions for passes
    public void gameReceiveOptions() {
        logger.addNewLog(players.get(currPlayer).getInitial());
        int status = players.get(currPlayer).playerReceiveOptions();
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }
    }

    // Method that prints "End of the game" in the server console, kinda useless
    private void gameEnd() {
        System.out.println("End of the game!");
    }

    // Gives a unique initial to each player, A B or C depending on the order
    public char initPlayer(String newPlayerName) {
        Player newPlayer = new Player(newPlayerName, '*');

        if (players.size() == 0)
            newPlayer.setInitial('A');
        else if (players.size() == 1)
            newPlayer.setInitial('B');
        else
            newPlayer.setInitial('C');

        players.add(newPlayer);
        System.out.println("\nIM ASSIGNING" + newPlayerName + " THIS INITIAL: " + newPlayer.getInitial() + "\n");
        printPlayersDescription();

        return newPlayer.getInitial();

    }

    // Sets the God picked by the player
    public void setPlayerGod(String godName, char initial) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard());
        opt.remove(godName);
    }

    // Set the otherGodLogic fields for the player
    private void setOtherGodLogic() {
        ArrayList<GodLogic> otherGodLogic;
        for (Player p: players) {
            otherGodLogic = new ArrayList<>();

            for (Player q: players) {
                if (p.getInitial() != q.getInitial())
                    otherGodLogic.add(q.getGodLogic());
            }

            p.getGodLogic().setOtherGodLogic(otherGodLogic);
        }
    }

    // Deletes the noob that got eliminated
    public void deletePlayer(Player p) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == p) {
                players.get(i).delete();
                players.remove(p);
            }
        }
        currPlayer = currPlayer % players.size();
    }

    // Sets the worker in the right spot
    public void setWorker(int x, int y, char initial) throws NonExistingTileException {
        getPlayerFromInitial(initial).addWorker(getBoard().getTile(x, y));
    }

    // Gets the player that is associated to the right initial
    public Player getPlayerFromInitial(char initial) {
        for (Player p : players) {
            if (p.getInitial() == initial)
                return p;
        }
        return null;
    }

    // Return a handy structure with the position of all the workers in the game
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

    // Gets most of the useful information from the game
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
