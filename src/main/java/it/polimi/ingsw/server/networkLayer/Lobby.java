package it.polimi.ingsw.server.networkLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lobby {

    int lobbyNumber;
    final int nPlayers;
    private Map<String, ServerSocket> waitingConnection = new HashMap<>();

    /**
     * Constructor method
     * @param lobbyNumber lobby identifier
     * @param nPlayers number of players the lobby can accept
     * @param playerName name of the player that created the lobby
     * @param playerSocket connection between the server and the player's client
     */
    public Lobby(int lobbyNumber, int nPlayers, String playerName, ServerSocket playerSocket) {
        this.lobbyNumber = lobbyNumber;
        this.nPlayers = nPlayers;
        waitingConnection.put(playerName, playerSocket);
    }

    /**
     * Getter method for the players' names
     * @return the names of all the players in the lobby
     */
    public ArrayList<String> getPlayersName() {
        return new ArrayList<>(waitingConnection.keySet()); // suggested by IntelliJ lol
    }

    /**
     * Tells the caller if the specified player is already in the lobby
     * @param playerName
     * @return true if the player is not in the lobby, false if it already is
     */
    public boolean isAvailable(String playerName) {
        return !getPlayersName().contains(playerName);
    }

    /**
     * Getter method for waitingConnection
     * @return a map of all the waiting connections
     */
    public Map<String, ServerSocket> getWaitingConnection() { return waitingConnection; }

    /**
     * Adds a player to the lobby (by adding it to the map of waitingConnection-s)
     * @param playerName new player's name
     * @param playerSocket connection between the server and the new player's client
     */
    public void addPlayer(String playerName, ServerSocket playerSocket) {
        waitingConnection.put(playerName, playerSocket);
    }

    /**
     * Tells the caller if the lobby is full of not
     * @return true if the player cap has been reached, false if not
     */
    public boolean isFull() {
        return nPlayers == waitingConnection.size();
    }

    /**
     * Gets the number of players that the lobby can contain
     * @return number of players the lobby can contain
     */
    public int getNPlayers() { return nPlayers; }

    /**
     * Deletes a player (from the map of waitingConnections)
     * @param playerName deleted player's name
     */
    public void deletePlayer(String playerName) {
        waitingConnection.remove(playerName);
    }

}
