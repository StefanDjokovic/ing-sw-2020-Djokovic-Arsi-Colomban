package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.answers.AnswerWorkersPosition;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClientGUI extends ClientView {
    //private TileView[][] boardView;
    private BoardView bv;
    private TileView[][] boardView;
    private static ClientGUI instance;
    private String playerName;
    private ArrayList<String> players;
    private int playersNum = 1;
    private char playerInit;
    private ArrayList<String> selectedGods;
    private Button[][] boardSlots;

    public ClientGUI() {
        instance = this;
        ThreadGUI tg = new ThreadGUI();
        tg.start();
    }

    @Override
    public void update(Request request) {
        request.printMessage();
        request.accept(this);
    }
    @Override
    public void update(Answer answer) {
        System.out.println("clientGUI should not receive answers");
    }

    // gui specific method
    public static ClientGUI getInstance() {
        return instance;
    }

    // gui specific method
    public void setOtherPlayers(ArrayList<String> pl) {
        this.players = pl;
        this.playersNum = pl.stream().map(x -> 1).reduce(0, Integer::sum) + 1;
    }

    // gui specific method
    public ArrayList<String> getPlayers() {
        return this.players;
    }

    // gui specific method
    public int getPlayersNum() {
        return this.playersNum;
    }

    //gui specific method
    public String getName() {
        return this.playerName;
    }

    //gui specific method
    public char getInit() {
        return this.playerInit;
    }

    // gui specific method
    public ArrayList<String> getGods() {
        return this.selectedGods;
    }

    public void setBoardSlots(Button[][] bs) {
        this.boardSlots = bs;
    }

    public Button[][] getBoardSlots() {
        return this.boardSlots;
    }

    public void updateBoardView(BoardView bv) {
        this.bv=bv;
    }

    public void getPlayerInfo() {
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            LoginUI l = new LoginUI();
            ss.setScene(l.getScene());
        });
    }

    // gui specific method
    public void sendPlayerInfo(String name) {
        this.playerName = name;
        updateObservers(new AnswerPlayerName(name));
    }

    public void getPlayerGod(char initial, ArrayList<String> options) {
        playerInit = initial;
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            GodSelectionUI l = new GodSelectionUI();
            ss.setScene(l.getScene());
        });
    }

    // gui specific method
    public void sendGods(ArrayList<String> gods) {
        this.selectedGods = gods;
        updateObservers(new AnswerPlayerGod(gods.get(0), this.playerInit));
    }

    private int called = 0;

    //TODO COMPLETE THE FUNCTION
    public void getWorkerPlacement(int[][] workers, char initial) {
        if(called == 0) {
            //playerInit = initial;
            Stage ss = CoreGUI.getStage();
            GameUI l = new GameUI();
            Platform.runLater(() -> {
                ss.setScene(l.getScene());
            });

            CoreGUI.getInstance().placeWorkers(workers);
        } else if (called == 1) {
            sendWorkerPlacement(selectedTiles);
        }
    }

    private ArrayList<String> selectedTiles;

    public void sendWorkerPlacement(ArrayList<String> tiles) {
        if(called == 0) {
            called++;
            selectedTiles=tiles;
            updateObservers(new AnswerWorkersPosition(tiles.get(0).charAt(0), tiles.get(0).charAt(1), playerInit));
        } else if (called==1) {
            GameUI.getConfirmButton().setDisable(true);
            Button[][] bs = GameUI.getBoardSlots();
            for (int a = 0; a < 5; a++) {
                for (int b = 0; b < 5; b++) {
                    bs[a][b].setDisable(true);
                }
            }

            updateObservers(new AnswerWorkersPosition(tiles.get(1).charAt(0), tiles.get(1).charAt(1), playerInit));
        }
    }

    public void getSelectedWorker(OptionSelection opt, boolean canPass) {

    }

    public void getWorkerSelection(OptionSelection opt, boolean canPass) {

    }

    public void displayGameEnd(char winnerInit) {

    }

    public void displayBoard() {

    }

    public void setPlayerInit(char init) {

    }

    public void waitingOpponent() {

    }
}
