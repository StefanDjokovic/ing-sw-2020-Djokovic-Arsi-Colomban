package it.polimi.ingsw.client.view;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.TileView;

import java.util.ArrayList;

public class clientGUI extends Observable implements Observer {
    private TileView[][] boardView;
    private static clientCLI instance = null;
    private String playerName;
    private ArrayList<String> players;
    private int playersNum;
    private char playerInit;
    private ArrayList<String> selectedGods;

    public clientGUI() {

    }

    @Override
    public void update(Request request) {
        //TODO FIX REQUEST ISSUE NOT ACCEPTING CLIENTGUI
        request.printMessage();
        //request.accept(this);
    }
}
