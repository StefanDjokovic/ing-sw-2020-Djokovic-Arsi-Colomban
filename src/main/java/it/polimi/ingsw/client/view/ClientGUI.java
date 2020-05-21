package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.TileView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ClientGUI extends Observable implements Observer, ClientView {
    private TileView[][] boardView;
    private static ClientGUI instance;
    private String playerName;
    private ArrayList<String> players;
    private int playersNum;
    private char playerInit;
    private ArrayList<String> selectedGods;

    public ClientGUI() {
        instance = this;
        ThreadGUI tg = new ThreadGUI();
        tg.start();
    }

    @Override
    public void update(Request request) {
        request.printMessage();
        //request.accept(this);
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

    public void updateBoardView(BoardView bv) {

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
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            GodSelectionUI l = new GodSelectionUI();
            ss.setScene(l.getScene());
        });
    }

    // gui specific method
    public void sendGods(ArrayList<String> gods) {
        //TODO add possibility to send list of gods, not only 1 god
        this.selectedGods = gods;
        //updateObservers(new AnswerPlayerGod(gods, this.playerInit));
    }

    public void getWorkerPlacement(int[][] workers, char initial) {

    }

    public void getSelectedWorker(OptionSelection opt, boolean canPass) {

    }

    public void getWorkerSelection(OptionSelection opt, boolean canPass) {

    }

    public void displayGameEnd(char winnerInit) {

    }
}
