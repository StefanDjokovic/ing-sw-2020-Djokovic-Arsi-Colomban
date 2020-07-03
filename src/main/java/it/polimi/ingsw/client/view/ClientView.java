package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.BoardView;

import java.util.ArrayList;

public abstract class ClientView extends Observable implements Observer {
    public abstract void updateBoardView(BoardView boardView);
    public abstract char getPlayerInitial();
    public abstract void getPlayerGod(char initial, ArrayList<String> options, int nPicks);
    public abstract void getWorkerPlacement(int[][] workers, char initial);
    public abstract void getSelectedWorker(OptionSelection opt, boolean canPass);
    public abstract void getWorkerSelection(OptionSelection opt, boolean canPass);
    public abstract void displayGameEnd(char winnerInit);
    public abstract void displayGameEnd();  // When one player loses
    public abstract void waitingOpponent(char opponentInitial);
    public abstract void setGameInformation(ArrayList<String> playersName, ArrayList<Character> playersInitial, int nPlayers);
    public abstract void setGodInformation(ArrayList<String> playersName, ArrayList<Character> playersInitial, ArrayList<String> godNames, int nPlayers);
    public abstract void displayBoard();
    public abstract void lobbyAndNameSelection(ArrayList<LobbyView> lobbies, int error);
    public abstract void displayLostConnection();
}
