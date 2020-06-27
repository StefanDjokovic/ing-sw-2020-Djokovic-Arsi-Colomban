package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.BoardView;

import java.util.ArrayList;

public abstract class ClientView extends Observable implements Observer {
    public abstract void updateBoardView(BoardView boardView);
    public abstract void getPlayerInfo();
    public abstract char getPlayerInitial();
    public abstract void getPlayerGod(char initial, ArrayList<String> options);
    public abstract void getWorkerPlacement(int[][] workers, char initial);
    public abstract void getSelectedWorker(OptionSelection opt, boolean canPass);
    public abstract void getWorkerSelection(OptionSelection opt, boolean canPass);
    public abstract void displayGameEnd(char winnerInit);
    public abstract void displayGameEnd();  // On loss
    public abstract void waitingOpponent();
    public abstract void setPlayerInit(char init);
    public abstract void displayBoard();
    public abstract void lobbyAndNameSelection(ArrayList<LobbyView> lobbies, int error);
}
