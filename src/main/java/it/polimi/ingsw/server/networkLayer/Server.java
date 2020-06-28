package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.request.RequestGameInformation;
import it.polimi.ingsw.messages.request.RequestServerInformation;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {


    private java.net.ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(64);
    private static Map<Integer, Lobby> lobbies = new HashMap<>();
    public static int newAvailableLobbyNumber = 1;

    /**
     * Constructor method, creates the server on the specified port
     * @param port port where the server needs to be initialized
     * @throws IOException
     */
    public Server(int port) throws IOException    {
        this.serverSocket = new java.net.ServerSocket(port);
    }


    public synchronized RequestServerInformation getRequestServerInformation(int n) {
        if (lobbies.size() != 0) {
            ArrayList<LobbyView> lobbyViews = new ArrayList<>();
            for (Lobby l : lobbies.values()) {
                lobbyViews.add(new LobbyView(l.lobbyNumber, l.nPlayers, l.getPlayersName()));
            }
            return new RequestServerInformation(lobbyViews, n);
        }
        else
            return new RequestServerInformation(null, n);
    }


    /**
     *
     * @param lobbyNumber lobby's identifier
     * @param playerName player's name
     * @param playerSocket player's client connection
     * @param nPlayers number of players the lobby can accept
     * @return
     */
    public synchronized Lobby isAvailable(int lobbyNumber, String playerName, ServerSocket playerSocket, int nPlayers) {
        System.out.println("Joining in is available");
        if (lobbyNumber == -2 || lobbyNumber == -3) {
            System.out.println("I'm entering the automatic selection of the lobby");
            // Auto-selects an open 2 player or 3 player lobby, creates a new one if not available
            for (Lobby l: lobbies.values()) {
                if (!l.isFull() && l.getNPlayers() == -lobbyNumber && l.isAvailable(playerName)) {
                    System.out.println("I have found an open lobby!");
                    l.addPlayer(playerName, playerSocket);
                    if (l.isFull()) {
                        System.out.println("Starting the Lobby " + lobbyNumber);
                        startLobbyGame(l);
                    }
                    return l;
                }
            }

            int lobbyNumb = getNewAvailableLobbyNumber();
            Lobby newLobby = new Lobby(lobbyNumb, -lobbyNumber, playerName, playerSocket);
            lobbies.put(lobbyNumb, newLobby);
            System.out.println("LOBBY " + lobbyNumb + " SUCCESSFULLY CREATED");
            return newLobby;
        }
        if (lobbyNumber == -1 || lobbies.get(lobbyNumber) == null) {
            // it is available, automatic insertion
            int lobbyNumb = getNewAvailableLobbyNumber();
            Lobby newLobby = new Lobby(lobbyNumb, nPlayers, playerName, playerSocket);
            lobbies.put(lobbyNumb, newLobby);
            System.out.println("LOBBY " + lobbyNumb + " SUCCESSFULLY CREATED");
            return newLobby;
        }
        else {
            // there is already a lobby with that number, check if the playerName is unique
            if (!lobbies.get(lobbyNumber).isFull() && lobbies.get(lobbyNumber).isAvailable(playerName)) {
                lobbies.get(lobbyNumber).addPlayer(playerName, playerSocket);
                if (lobbies.get(lobbyNumber).isFull()) {
                    System.out.println("Starting the Lobby " + lobbyNumber);
                    startLobbyGame(lobbies.get(lobbyNumber));
                }
                return lobbies.get(lobbyNumber);
            }
        }
        return null;
    }

    /**
     * Gets the index of the first available lobby
     * @return the index of the first available lobby
     */
    public synchronized int getNewAvailableLobbyNumber() {
        int lobbyNumb = newAvailableLobbyNumber;
        newAvailableLobbyNumber++;
        return lobbyNumb;
    }

    /**
     * Initializes the specified lobby, initializing all the components of the game (Game, Controller, and a "virtual view" for each player)
     * and then starts the game, asking first for the players' gods and then starting the turn routine
     * @param lobby lobby that needs to be initialized (it has reached its specified number of players)
     */
    public synchronized void startLobbyGame(Lobby lobby) {
    // Will go trough only when all the ServerSocket have given all the necessary information
        int nPlayers = lobby.getNPlayers();
        System.out.println("Joining Lobby");
        Game game;
        ArrayList<String> keys = lobby.getPlayersName();
        Map<String, ServerSocket> waitingConnection = lobby.getWaitingConnection();
        String name1 = keys.get(0);
        ServerSocket c1 = waitingConnection.get(keys.get(0));
        String name2 = keys.get(1);
        ServerSocket c2 = waitingConnection.get(keys.get(1));
        ServerSocket c3; // set later if 3 players are selected

        // Initializing initial model structures
        game = new Game();
        Controller controller = new Controller(game);
        char player1Init = controller.initPlayer(name1);
        c1.setPlayerInitial(player1Init);
        char player2Init = controller.initPlayer(name2);
        c2.setPlayerInitial(player2Init);

        if (nPlayers == 2) {
            c1.sendGameInformation(new RequestGameInformation(name1, name2, player1Init, player2Init));
            c2.sendGameInformation(new RequestGameInformation(name2, name1, player2Init, player1Init));
        }

        System.out.print(name1 + " and " + name2 + " ");
        // Initialization for 3 players game
        if (nPlayers == 3) {
            c3 = waitingConnection.get(keys.get(2));
            String name3 = keys.get(2);
            System.out.println("and " + name3);
            char player3Init = controller.initPlayer(name3);
            c3.setPlayerInitial(player3Init);
            c1.sendGameInformation(new RequestGameInformation(name1, name2, name3, player1Init, player2Init, player3Init));
            c2.sendGameInformation(new RequestGameInformation(name2, name3, name1, player2Init, player3Init, player1Init));
            c3.sendGameInformation(new RequestGameInformation(name3, name1, name2, player3Init, player1Init, player2Init));

            game.addObserver(c3);
            c3.addObserver(controller);

        }
        System.out.println();

        // Set controller as Observer of view, set view as Observer of game
        game.addObserver(c1);
        game.addObserver(c2);
        c1.addObserver(controller);
        c2.addObserver(controller);

        // Starting the game (first asks for gods, than worker placement, and then starts with the turn structure
        controller.initGods();

    }

    /**
     * The server stays active for new player connections client connections
     */
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept(); // The server accepts a connection
                ServerSocket connection = new ServerSocket(clientSocket, this);
                executor.submit(connection); // Creates new thread
                System.out.println("Accepted new client");
            } catch (IOException e){
                System.err.println("This" + e.getMessage());
            }
        }
    }

}