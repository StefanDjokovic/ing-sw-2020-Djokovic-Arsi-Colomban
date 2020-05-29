package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.answers.AnswerPowerCoordinates;
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
    //private Button[][] boardSlots;

    private LoginUI login;
    private GodSelectionUI selection;
    private GameUI game;

    public ClientGUI() {
        instance = this;
        //bv=new BoardView();
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
/*
    public void setBoardSlots(Button[][] bs) {
        this.boardSlots = bs;
    }

    public Button[][] getBoardSlots() {
        return this.boardSlots;
    }*/

    public void updateBoardView(BoardView bv) {
        this.bv=bv;
        if(game != null) {
            Platform.runLater(() -> {
                game.updateBoard(bv.getBoardView());
            });
        }
    }

    public void getPlayerInfo() {
        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            login = new LoginUI();
            ss.setScene(login.getScene());
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
            selection = new GodSelectionUI();
            ss.setScene(selection.getScene());
            selection.startGodSelection(options);
        });
    }

    // gui specific method
    public void sendGods(ArrayList<String> gods) {
        this.selectedGods = gods;
        updateObservers(new AnswerPlayerGod(gods.get(0), this.playerInit));
    }

    //private int called = 0;

    //TODO COMPLETE THE FUNCTION
    public void getWorkerPlacement(int[][] workers, char initial) {

        Stage ss = CoreGUI.getStage();
        Platform.runLater(() -> {
            game = new GameUI();
            ss.setScene(game.getScene());
            game.updateBoard(bv.getBoardView());
            game.placeWorkers(workers);
        });
    }

    //private ArrayList<String> selectedTiles;

    public void sendWorkerPlacement(ArrayList<String> tiles) {
//        GameUI.getConfirmButton().setDisable(true);
//        Button[][] bs = GameUI.getBoardSlots();
//        for (int a = 0; a < 5; a++) {
//            for (int b = 0; b < 5; b++) {
//                bs[a][b].setDisable(true);
//            }
//        }
        //System.out.println(tiles.get(0));
        //System.out.println(Integer.valueOf(tiles.get(0).charAt(0) - 48)+" "+Integer.valueOf(tiles.get(0).charAt(2) - 48));
        updateObservers(new AnswerWorkersPosition(Integer.valueOf(tiles.get(0).charAt(0) - 48), Integer.valueOf(tiles.get(0).charAt(2) - 48), playerInit));
    }

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

    public void sendPower(ArrayList<Integer> pow) {
        //send the movement
        //System.out.println(Integer.valueOf(mov.get(0).charAt(0) - 48)+Integer.valueOf(mov.get(0).charAt(2) - 48)+Integer.valueOf(mov.get(1).charAt(0) - 48)+Integer.valueOf(mov.get(1).charAt(2) - 48));
        //Answer answer = new AnswerPowerCoordinates(Integer.valueOf(mov.get(0).charAt(0) - 48), Integer.valueOf(mov.get(0).charAt(2) - 48), Integer.valueOf(mov.get(1).charAt(0) - 48), Integer.valueOf(mov.get(1).charAt(2) - 48));

        //System.out.println("ci sono!");

        System.out.println(pow.get(0)+" "+ pow.get(1)+" "+ pow.get(2)+" "+ pow.get(3));
        Answer answer = new AnswerPowerCoordinates(pow.get(0), pow.get(1), pow.get(2), pow.get(3));

        updateObservers(answer);
    }

    public void sendPass() {
        updateObservers(new AnswerPowerCoordinates());
    }

    public void getWorkerSelection(OptionSelection opt, boolean canPass) {

    }

    public void displayGameEnd(char winnerInit) {
        if(winnerInit == playerInit) {
            game.youWin();
        } else {
            game.youLose();
        }
    }

    public void displayBoard() {
//        System.out.println("ciao");
//        if(game != null && bv != null) {
//            game.updateBoard(bv.getBoardView());
//        }
    }

    public void setPlayerInit(char init) {

    }

    public void waitingOpponent() {

    }

    public void lobbyAndNameSelection(ArrayList<LobbyView> lobbies) { }
}
