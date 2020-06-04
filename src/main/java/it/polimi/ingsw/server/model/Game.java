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

    /**
     * Constructor
     */
    public Game() {
        players = new ArrayList<>();
        board = new Board();
        logger = new Logger();
    }

    /**
     * Getter method for board attribute, for testing purposes
     * @return the object's board attribute
     */
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

        updateObservers(new RequestPlayerGod(players.get(nPlayersWithGod()).getInitial(), opt));
//        updateObservers(new RequestPlayerGod(players.get(0).getInitial(), opt));
//        updateObservers(new RequestPlayerGod(players.get(1).getInitial(), opt));
//        if (players.size() > 2 && players.get(2).getGodLogic() == null) {
//            updateObservers(new RequestPlayerGod(players.get(2).getInitial(), opt));
//        }
    }

    /**
     * Initializes workers after sending requests for the workers' placement
     */
    public void initWorker() {
        System.out.println("Init worker");
        printPlayersDescription();
        updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));

//        for (int i = 0; i < players.size(); i++) {
//            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
//            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(i).getInitial()));
//            updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
//            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(i).getInitial()));
//        }
        //updateObservers(new RequestDisplayBoard('*', new RequestUpdateBoardView(new BoardView(board), '*')));
        System.out.println("\n\nSending request worker placement to 0\n\n");
        if (players.get(0).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(0).getInitial()));
        else if (players.get(1).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(1).getInitial()));
        else if (players.size() == 3 && players.get(2).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(2).getInitial()));


        // Setting up otherGodLogic for each player (essential feature for Athena)
//        setOtherGodLogic();

    }

    /**
     * Tells the caller if all the necessary workers are on the board
     * @return true if there are missing workers, false if there aren't
     */
    public boolean missingWorkers() {
        for (Player p: players) {
            if (p.getWorkersSize() != 2)
                return true;
        }
        updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));
        return false;
    }

    /**
     * Starts the whole game logic, by initializing the turn routine and terminating the game if there are no players
     */
    private int currPlayer = 0;
    public void gameStart() {
        while (players != null  && players.size() != 1 && status != 2) {    // status == 2 when someone has won
            players.get(currPlayer).executeTurn(this);
        }
        if (players != null) {
            if (players.size() == 1)
                updateObservers(new RequestGameEnd(players.get(0).getInitial()));
            else {
                updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));
                updateObservers(new RequestGameEnd('*'));
            }
        }
        gameEnd();
    }


    public void asyncGameStart() {
        if (players.size() > 1 && status != 2)
            players.get(currPlayer).executeTurn(this);
        else {
            if (players.size() == 1)
                updateObservers(new RequestGameEnd(players.get(0).getInitial()));
            else if (players.size() > 1){
                updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));
                updateObservers(new RequestGameEnd(players.get(currPlayer).getInitial()));  // TODO: check if correct
            }
            gameEnd();
        }
    }

    public int status = -1;
    /**
     * Pushes the options to the player and executes a turn step from the starting tile to the destination tile.
     * Sets new currPlayer if the player's turn is finished
     * @param posXFrom x coordinate of the starting tile
     * @param posYFrom y coordinate of the starting tile
     * @param posXTo x coordinate of the destination tile
     * @param posYTo y coordinate of the destination tile
     */
    public void gameReceiveOptions(int posXFrom, int posYFrom, int posXTo, int posYTo) {
        logger.addNewLog(posXFrom, posYFrom, posXTo, posYTo, players.get(currPlayer).getInitial());
        status = players.get(currPlayer).playerReceiveOptions(getBoard(), posXFrom, posYFrom, posXTo, posYTo);
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }
    }

    /**
     * Pushes the options to the player and executes a turn step when the player passes
     */
    public void gameReceiveOptions() {
        logger.addNewLog(players.get(currPlayer).getInitial());
        int status = players.get(currPlayer).playerReceiveOptions();
        if (status == 1) {
            currPlayer = (currPlayer + 1) % players.size();
        }
    }

    /**
     * Prints "End of the game!" on the server's console, not really useful
     */
    private void gameEnd() {
        System.out.println("End of the game!");
    }

    /**
     * Assigns a unique initial to a new player based on the order of arrival
     * @param newPlayerName new player's name
     * @return player's initial (A, B or C)
     */
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

    /**
     * Sets the god card picked by the player
     * @param godName god's name select by the player
     * @param initial player's initial
     */
    public void setPlayerGod(String godName, char initial) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard());
        opt.remove(godName);
    }

    /**
     * Sets the otherGodlLogic field for all the players,
     * that cointains the other players' god cards informations
     */
    public void setOtherGodLogic() {
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

    /**
     * Deletes a player after they get eliminated
     * @param p player that needs to be deleted
     */
    public void deletePlayer(Player p) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) == p) {
                players.get(i).delete();
                players.remove(p);
            }
        }
        if (players.size() != 0)
            currPlayer = currPlayer % players.size();
    }

    /**
     * Initializes a worker in the selected tile
     * @param x x coordinate of the selected tile
     * @param y y coordinate of the selected tile
     * @param initial player's initial
     * @throws NonExistingTileException the selected tile doesn't exist
     */
    public void setWorker(int x, int y, char initial) throws NonExistingTileException {
        getPlayerFromInitial(initial).addWorker(getBoard().getTile(x, y));
    }

    /**
     * Gets the player associated to the selected initial
     * @param initial selected initial
     * @return player associated to the initial
     */
    public Player getPlayerFromInitial(char initial) {
        for (Player p : players) {
            if (p.getInitial() == initial)
                return p;
        }
        return null;
    }

    /**
     * Gets all the workers that are on the board
     * @return a matrix with the position of all the workers
     */
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

    /**
     * Gets the number of players
     * @return number of players
     */
    public int nPlayers() {
        return players.size();
    }

    /**
     * Gets the number of players with a god card associated to them
     * @return number of players with a god card associated to them
     */
    public int nPlayersWithGod() {
        int count = 0;
        for (Player p: players) {
            if (p.getGodLogic() != null)
                count++;
        }
        return count;
    }

    /**
     * Prints on the server's terminal player's names with their initials,
     * whether they have or don't have an associated god card
     * and the number of workers each player has.
     */
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
