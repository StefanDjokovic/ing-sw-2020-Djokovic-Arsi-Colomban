package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.messages.request.RequestGameInformation;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class Server {

    private final int nPlayers;

    public static final int PORT = 4568;
    private java.net.ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(64);
    private Map<String, ServerSocket> waitingConnection = new HashMap<>();
    private Map<ServerSocket, ServerSocket> playingConnection = new HashMap<>();
    private Map<String, String> nameGodLogicMap = new HashMap<>();


    public Server(int nPlayers) throws IOException    {
        this.serverSocket = new java.net.ServerSocket(PORT);
        this.nPlayers = nPlayers;
    }

    public synchronized void deregisterConnection(ServerSocket connection) {
        ServerSocket opponent = playingConnection.get(connection);
        if(opponent != null) {
            opponent.closeConnection();
        }
        playingConnection.remove(connection);
        playingConnection.remove(opponent);
        Iterator<String> iterator = waitingConnection.keySet().iterator();
        while(iterator.hasNext()){
            if(waitingConnection.get(iterator.next())==connection){
                iterator.remove();
            }
        }
    }

    public synchronized boolean lobby(ServerSocket connection, String name) {
        if (!waitingConnection.containsKey(name))
            waitingConnection.put(name, connection);
        else
            return false;
        // Will go trough only when all the ServerSocket have given all the necessary information
        if (waitingConnection.size() == nPlayers) {
            System.out.println("Joining Lobby");
            Game game;
            ArrayList<String> keys = new ArrayList<>(waitingConnection.keySet());
            ServerSocket c1 = waitingConnection.get(keys.get(0));
            String name1 = keys.get(0);
            ServerSocket c2 = waitingConnection.get(keys.get(1));
            String name2 = keys.get(1);
            ServerSocket c3 = null; // initialized in the later if 3 players are selected

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

                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
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

                playingConnection.put(c1, c2);
                playingConnection.put(c2, c3);
                playingConnection.put(c3, c1);
            }
            System.out.println();

            // Set controller as Observer of view, set view as Observer of game
            game.addObserver(c1);
            game.addObserver(c2);
            c1.addObserver(controller);
            c2.addObserver(controller);

            // Starting the game (first asks for gods, than worker placement, and then starts with the turn structure
            System.out.println("\n\n\nClontroller.startGame()\n\n\n");
            controller.startGame();
            System.out.println("\n\n\n\n\nClontroller CLOSED\n\n\n\n\n");
//            c1.close();
//            c2.close();
//            if (c3 != null)
//                c3.close();
        }
        return true;
    }

    // The server tries to get the nPlayers amount set from the command line
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