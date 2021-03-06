package it.polimi.ingsw.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class LobbyView implements Serializable {

    private static final long serialVersionUID = 6529685098267757630L;

    int lobbyNumber;
    int nPlayers;
    ArrayList<String> playersName;

    public LobbyView(int lobbyNumber, int nPlayers, ArrayList<String> playersName) {
        this.lobbyNumber = lobbyNumber;
        this.nPlayers = nPlayers;
        this.playersName = playersName;
    }

    public int getLobbyNumber() { return lobbyNumber; }
    public int getNPlayers() { return nPlayers; }
    public ArrayList<String> getPlayersName() { return playersName; }

}
