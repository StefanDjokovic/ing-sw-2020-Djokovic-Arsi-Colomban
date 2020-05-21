package it.polimi.ingsw.client.view;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.server.model.BoardView;

import java.util.ArrayList;

public interface ClientView {
    public void updateBoardView(BoardView boardView);
    public void getPlayerInfo();
    public void getPlayerGod(char initial, ArrayList<String> options);
    public void getWorkerPlacement(int[][] workers, char initial);
    public void getSelectedWorker(OptionSelection opt, boolean canPass);
    public void getWorkerSelection(OptionSelection opt, boolean canPass);
    public void displayGameEnd(char winnerInit);
}
