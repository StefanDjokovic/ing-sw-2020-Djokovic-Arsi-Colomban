package it.polimi.ingsw.server.networkLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lobby {

    int lobbyNumber;
    final int nPlayers;
    private Map<String, ServerSocket> waitingConnection = new HashMap<>();

    public Lobby(int lobbyNumber, int nPlayers, String playerName, ServerSocket playerSocket) {
        this.lobbyNumber = lobbyNumber;
        this.nPlayers = nPlayers;
        waitingConnection.put(playerName, playerSocket);
    }

    public ArrayList<String> getPlayersName() {
        return new ArrayList<>(waitingConnection.keySet()); // suggested by IntelliJ lol
    }

    public boolean isAvailable(String playerName) {
        return !getPlayersName().contains(playerName);
    }

    public Map<String, ServerSocket> getWaitingConnection() { return waitingConnection; }

    public void addPlayer(String playerName, ServerSocket playerSocket) {
        waitingConnection.put(playerName, playerSocket);
    }

    public boolean isFull() {
        return nPlayers == waitingConnection.size();
    }

    public int getNPlayers() { return nPlayers; }

    public void deletePlayer(String playerName) {
        waitingConnection.remove(playerName);
    }

}
