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
import java.util.HashMap;


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

        return newPlayer.getInitial();

    }

    // TODO: initialise opt through JSON file
    // Sends to ServerSocket the Request for the God the users pick
    private HashMap<String, Integer> opt;
    private ArrayList<String> optList;
    public void initGods() {

        if (opt == null) {
            opt = new HashMap<>();
            opt.put("Apollo", 1);
            opt.put("Artemis", 2);
            opt.put("Athena", 3);
            opt.put("Atlas", 4);
            opt.put("Demeter", 5);
            opt.put("Hephaestus", 6);
            opt.put("Minotaur", 8);
            opt.put("Pan", 9);
            opt.put("Prometheus", 10);
            opt.put("Ares", 12);
            opt.put("Charon", 15);
            opt.put("Hestia", 21);
            opt.put("Poseidon", 27);
            opt.put("Zeus", 30);
        }

        if (optList == null)
            updateObservers(new RequestPlayerGod(players.get(nPlayersWithGod()).getInitial(), nPlayers(), new ArrayList<>(opt.keySet())));
        else
            updateObservers(new RequestPlayerGod(players.get(nPlayersWithGod() + 1).getInitial(), 1, optList));

    }

    /**
     * Initializes workers after sending requests for the workers' placement
     */
    public void initWorker() {
        updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));

        if (players.get(0).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(0).getInitial()));
        else if (players.get(1).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(1).getInitial()));
        else if (players.size() == 3 && players.get(2).getWorkersSize() != 2)
            updateObservers(new RequestWorkerPlacement(getAllWorkersAsMatrix(), players.get(2).getInitial()));

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
     * Sets the god card picked by the player
     * @param godName god's name select by the player
     * @param initial player's initial
     */
    public void setPlayerGod(String godName, char initial) {
        getPlayerFromInitial(initial).setGodLogic(godName, logger, getBoard());
        optList.remove(godName);
    }

    /**
     * Sets the god card picked by the player
     * @param godNames gods' name select by the player
     */
    public void setPlayerGods(ArrayList<String> godNames) {
        optList = godNames;
    }

    public void initLastGod() {
        for (Player p: players) {
            if (p.getGodLogic() == null) {
                p.setGodLogic(optList.get(0), logger, getBoard());
                printPlayersDescription();

                if (nPlayers() == 2)
                    updateObservers(new RequestGameInformation(players.get(0).getName(), players.get(1).getName(),
                            players.get(0).getInitial(), players.get(1).getInitial(), players.get(0).getGodLogic().getGodLogicName(),
                            players.get(1).getGodLogic().getGodLogicName()));
                if (nPlayers() == 3)
                    updateObservers(new RequestGameInformation(players.get(0).getName(), players.get(1).getName(), players.get(2).getName(),
                            players.get(0).getInitial(), players.get(1).getInitial(), players.get(2).getInitial(), players.get(0).getGodLogic().getGodLogicName(),
                            players.get(1).getGodLogic().getGodLogicName(), players.get(2).getGodLogic().getGodLogicName()));
                return;
            }
        }
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
     * Sets the otherGodLogic field for all the players,
     * that contains the other players' god cards information
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

    public void reportLossPlayer(Player p) {
        updateObservers(new RequestKillPlayer(p.getInitial()));
    }

    /**
     * Deletes a player after they get eliminated
     * @param initialKilled initial of player that needs to be deleted
     */
    public boolean deletePlayer(char initialKilled) {
        boolean isCurrent = false;
        for (int i = 0; i < players.size(); i++) {
            if (currPlayer < players.size() && players.get(currPlayer).getInitial() == initialKilled)
                isCurrent = true;
            if (players.get(i).getInitial() == initialKilled) {
                players.get(i).delete();
                players.remove(i);

                if (isCurrent) {
                    if (players.size() != 0 && currPlayer == players.size())
                        currPlayer = 0;
                }
                else {
                    if (i < currPlayer)
                        currPlayer--;
                }

                break;
            }
        }

        return isCurrent;
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
     * Starts the whole game logic, by initializing the turn routine and terminating the game if there are no players
     */
    // currPlayer keeps track of the player that is playing (it is the position in the ArrayList of players)
    private int currPlayer = 0;
    private boolean gameEndDeclared = false;
    //status is 0 when the player has not finished his turn, 1 when the player finished his turn, 2 when the current player won
    private int status = -1;
    public void gameTurnExecution() {
        if (players.size() > 1 && status != 2) {
            int val = 1;
            // Some turns may be model-internal: if val == 1 it means it was model-internal
            while (val == 1)
                val = players.get(currPlayer).executeTurn(this);
        }
        else {
            if (players.size() == 1) {
                if (!gameEndDeclared) {
                    gameEndConditionOnOnePlayer();
                    System.out.println("Won because just one player was left!");
                    gameEndDeclared = true;
                }
            }
            else if (players.size() > 1){
                if (!gameEndDeclared) {
                    updateObservers(new RequestUpdateBoardView(new BoardView(board), '*'));
                    updateObservers(new RequestGameEnd(players.get(currPlayer).getInitial()));
                    System.out.println("Game ended, won by " + players.get(currPlayer).getInitial());
                    gameEndDeclared = true;
                }
            }
        }
    }

    public void gameContinueOnKill(boolean isCurrent) {
        if (isCurrent)
            gameTurnExecution();
        else {
            gameEndConditionOnOnePlayer();
        }
    }

    /**
     * Prints "End of the game!" on the server's console, not really useful
     */
    private boolean gameEndConditionOnOnePlayer() {
        if (players.size() == 1) {
            updateObservers(new RequestGameEnd(players.get(0).getInitial()));
            if (!gameEndDeclared) {
                gameEndConditionOnOnePlayer();
                System.out.println("Won because just one player was left!");
                gameEndDeclared = true;
            }
            return true;
        }
        return false;
    }

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
     * Getter method for board attribute
     * @return the object's board attribute
     */
    public Board getBoard() {
        return board;
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
