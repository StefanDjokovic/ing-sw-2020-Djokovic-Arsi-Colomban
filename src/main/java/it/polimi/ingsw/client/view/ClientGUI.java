package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.*;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClientGUI extends ClientView {
    private BoardView bv;
    private TileView[][] boardView;
    private static ClientGUI instance;
    private String playerName;
    private ArrayList<String> players;
    private int playersNum = 1;
    private char playerInit;
    private ArrayList<String> selectedGods;

    private LoginUI login;
    private GodSelectionUI selection;
    private GameUI game;
    private LobbyUI lobby;
    private boolean joining;
    private int chosenLobby;
    private int chosenNumPlayers;

    public ClientGUI() {
        instance = this;
        ThreadGUI tg = new ThreadGUI();
        tg.start();
    }

    public char getPlayerInitial() {
        return this.playerInit;
    }

    /**
     * Gets called when one of the observed classes sends an update,
     * the view acts accordingly to the content of the received Request message
     */
    @Override
    public void update(Request request) {
        request.printMessage();
        request.accept(this);
    }
    @Override
    public void update(Answer answer) {
        System.out.println("clientGUI should not receive answers");
    }

    /**
     * Returns static instance of clientGUI, used by JavaFX class to send information to the server.
     */
    public static ClientGUI getInstance() {
        return instance;
    }

    /**
     * Saves the names of the other players in the match as a list of strings.
     * @param pl ArrayList of strings representing the names of the players.
     */
    public void setOtherPlayers(ArrayList<String> pl) {
        this.players = pl;
        this.playersNum = pl.stream().map(x -> 1).reduce(0, Integer::sum) + 1;
    }

    /**
     * Returns list of the other players. Used by JavaFX class to display names on the GUI.
     * @return ArrayList of strings representing the names of the players.
     */
    public ArrayList<String> getPlayers() {
        return this.players;
    }

    /**
     * Returns number of players in the match.
     * @return int representing the number of players in the match.
     */
    public int getPlayersNum() {
        return this.playersNum;
    }

    /**
     * Returns the name of the player using this client.
     * @return String containing the name of the player using this client.
     */
    public String getName() {
        return this.playerName;
    }

    /**
     * Returns a character representing the player in the match. Should be unique.
     * @return Character of the player using this client.
     */
    public char getInit() {
        return this.playerInit;
    }

    /**
     * Returns list of gods selected by the player using this client.
     * @return List of gods selected by the player using this client.
     */
    public ArrayList<String> getGods() {
        return this.selectedGods;
    }

    /**
     * Receives the BoardView containing the current situation of the board and updates it, displaying it in the GUI.
     * @param bv BoardView containing a representation of the board.
     */
    public void updateBoardView(BoardView bv) {
        this.bv=bv;
        if(game != null) {
            Platform.runLater(() -> {
                game.updateBoard(bv.getBoardView());
            });
        }
    }

    /**
     * Server type function, asks the client to answer with the name. In the GUI it starts the scene containing the login page(LoginUI).
     */
    public void getPlayerInfo() {
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            login = new LoginUI(joining);
            ss.setScene(login.getScene());
            ss.setTitle("Login phase");
            ss.setResizable(true);
            ss.setMinHeight(400);
            ss.setMinWidth(400);
        });
    }

    /**
     * Sends the information asked with "getPlayerInfo()" to server. Called by LoginUI.class.
     * @param name Name of the player using this client.
     * @param playerNum Preferred number of players selected by the player using this client.
     */
    public void sendPlayerInfo(String name, int playerNum) {
        this.playerName = name;
        //updateObservers(new AnswerPlayerName(name));
        if(joining) {
            updateObservers(new AnswerLobbyAndName(chosenLobby, playerName, -1));
        } else {
            updateObservers(new AnswerLobbyAndName(chosenLobby, playerName, playerNum));
        }
    }

    /**
     * Server type function, asks the client to answer with the god. In the GUI it starts the scene containing the god selection page(GodSelectionUI).
     * @param initial Character unique for the player using this client (assigned by server).
     * @param options List of gods that the player can select.
     */
    public void getPlayerGod(char initial, ArrayList<String> options, int nPicks) {
        playerInit = initial;
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            selection = new GodSelectionUI();
            ss.setScene(selection.getScene());
            ss.setTitle("God selection");
            ss.setMinHeight(300);
            ss.setMinWidth(350);
            selection.startGodSelection(options, nPicks);
        });
    }

    /**
     * Sends the information asked with "getPlayerGod(...)" to server. Called by GodSelectionUI.class.
     * @param gods List of gods selected by the player (can be 1 or more).
     */
    public void sendGods(ArrayList<String> gods, boolean first) {
        this.selectedGods = gods;
        if (first) {
            updateObservers(new AnswerPlayerGod(gods, this.playerInit));
        } else {
            updateObservers(new AnswerPlayerGod(gods.get(0), this.playerInit));
        }
    }

    /**
     * Server type function, asks the client to answer with the position of 1 tile on the board to place a worker. In the GUI it starts the scene containing the game page(GameUI).
     * @param workers Matrix containing the tiles already occupied by other workers.
     * @param initial Character unique for the player using this client (check).
     */
    public void getWorkerPlacement(int[][] workers, char initial) {

        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            game = new GameUI();
            ss.setScene(game.getScene());
            ss.setTitle("Santorini");
            ss.setMinHeight(670);
            ss.setMinWidth(650);
            game.updateBoard(bv.getBoardView());
            game.placeWorkers(workers);
        });
    }

    /**
     * Sends the information asked with "getWorkerPlacement(...)" to server. Called by GameUI.class.
     * @param tiles List containing the coordinates of the selected tile (position of the worker).
     */
    public void sendWorkerPlacement(ArrayList<String> tiles) {
        updateObservers(new AnswerWorkersPosition(tiles.get(0).charAt(0) - 48, tiles.get(0).charAt(2) - 48, playerInit));
    }

    /**
     * Server type function, if opt.size() = 2 it asks the client to answer with the position of 1 worker on the board to move. If opt.size() = 1 it asks the client to answer with the tile where the player wants to build.
     * @param opt Possible workers the player can select.
     * @param canPass Boolean, true if the player can skip this phase.
     */
    public void getSelectedWorker(OptionSelection opt, boolean canPass) {
        if (canPass) {
            game.makeSkippable();
        }
        if(opt.getValues().size() == 2) {
            game.selectWorker(opt);
        } else if (opt.getValues().size() == 1) {
            game.selectWorkerOneOption(opt);
        }
    }

    /**
     * Sends the information asked with "getSelectedWorker(...)" to server. Called by GameUI.class.
     * @param pow Coordinates of the selected tile/worker, based on information contained in getSelectedWorker(...).
     */
    public void sendPower(ArrayList<Integer> pow) {
        Answer answer = new AnswerPowerCoordinates(pow.get(0), pow.get(1), pow.get(2), pow.get(3));

        updateObservers(answer);
    }

    /**
     * Signals to the server that the player wants to skip this phase.
     */
    public void sendPass() {
        updateObservers(new AnswerPowerCoordinates());
    }

    //??????
    public void getWorkerSelection(OptionSelection opt, boolean canPass) {

    }

    /**
     * Displays final message based on who won.
     * @param winnerInit Character of the player that won the match.
     */
    public void displayGameEnd(char winnerInit) {
        int won;

        if(winnerInit == playerInit) {
            won = 0;
        } else {
            won = 1;
        }

        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            EndUI e = new EndUI(won);
            CoreGUI.getStage().setScene(e.getScene());
            ss.setMinHeight(500);
            ss.setMinWidth(500);
        });
    }

    /**
     * Displays final message in case of loss because of no option
     */
    public void displayGameEnd() {
        game.youLose();
    }

    //needed but not used
    public void displayBoard() {
    }

    //needed but not used
    public void setPlayerInit(char init) {
    }

    //needed but not used
    public void waitingOpponent() {
    }

    boolean isActive = false;

    /**
     * Server type function, sends information about current active matches. In the GUI it starts the scene containing the lobby selection page(LobbyUI).
     * @param lobbies Information about every active match hosted by the server.
     * @param error Status flag, used to notify that there is no match available.
     */
    public void lobbyAndNameSelection(ArrayList<LobbyView> lobbies, int error) {
        if (error != 0 && error != 2) {
            System.out.println("lobby error: "+ error);
            //return;
        }

        if (!isActive) {
            isActive = true;
            Stage ss = CoreGUI.getStage();
            Platform.runLater(() -> {
                lobby = new LobbyUI();
                ss.setScene(lobby.getScene());
                ss.setResizable(true);
                ss.setMinHeight(500);
                ss.setMinWidth(700);
                ss.setResizable(true);
                lobby.refresh(lobbies);
            });
        } else {
            Platform.runLater(() -> {
                lobby.refresh(lobbies);
            });
        }
    }

    /**
     * Sends the information asked with "lobbyAndNameSelection(...)" to server. Called by LobbyUI.class.
     * @param number Room number selected.
     * @param isJoining True if the player is joining a match or creating one.
     * @param plNum Selected number of players, "-1" if the player is joining.
     */
    public void sendLobbySelection(int number, boolean isJoining, int plNum) {
        joining = isJoining;
        chosenLobby = number;
        chosenNumPlayers = plNum;

        if (number == 0) {
            updateObservers(new AnswerLobbyAndName(chosenLobby, playerName, plNum));
        } else {
            getPlayerInfo();
            isActive = false;
        }
    }

    /**
     * Restarts the process of connection and login
     */
    public void replay() {
        sendLobbySelection(-1, false, 0);
    }

    /**
     * Connects the client to the server
     */
    public void connectToServer(String ip, String port) {

    }

    /**
     * Notifies to the player that the connection to the server has been lost
     */
    public void displayLostConnection() {
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            EndUI e = new EndUI(2);
            ss.setScene(e.getScene());
            ss.setMinHeight(500);
            ss.setMinWidth(500);
        });
    }
}
