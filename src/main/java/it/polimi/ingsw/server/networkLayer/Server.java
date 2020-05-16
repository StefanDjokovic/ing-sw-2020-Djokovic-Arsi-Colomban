package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;
import it.polimi.ingsw.messages.answers.AnswerWorkersPosition;
import it.polimi.ingsw.messages.request.RequestDisplayBoard;
import it.polimi.ingsw.messages.request.RequestGameInformation;
import it.polimi.ingsw.messages.request.RequestUpdateBoardView;
import it.polimi.ingsw.messages.request.RequestWorkerPlacement;
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

    private static final int nPlayers = 2;

    public static final int PORT = 4568;
    private java.net.ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(64);
    private Map<String, ServerSocket> waitingConnection = new HashMap<>();
    private Map<ServerSocket, ServerSocket> playingConnection = new HashMap<>();
    private Map<String, String> nameGodLogicMap = new HashMap<>();

    public Server() throws IOException    {
        this.serverSocket = new java.net.ServerSocket(PORT);
    }

    public synchronized void deregisterConnection(ServerSocket connection) {    // TODO: make it work for 3 players
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

    public synchronized void removeSocketConnection(ServerSocket connection) {
        game.removeObserver(connection);
    }

    public Game game;
    public synchronized void lobby(ServerSocket connection, String name) {
        waitingConnection.put(name, connection);
        if (waitingConnection.size() == nPlayers) {
            System.out.println("Are we at least here?");
            ArrayList<String> keys = new ArrayList<>(waitingConnection.keySet());
            ServerSocket c1 = waitingConnection.get(keys.get(0));
            ServerSocket c2 = waitingConnection.get(keys.get(1));
            String name1 = keys.get(0);
            String name2 = keys.get(1);
            System.out.println(name1 + " and " + name2);
            game = new Game();
            Controller controller = new Controller(game);
            char player1Init = controller.initPlayer(name1);
            c1.setPlayerInitial(player1Init);
            char player2Init = controller.initPlayer(name2);
            c2.setPlayerInitial(player2Init);
            c1.sendGameInformation(new RequestGameInformation(name1, name2, player1Init, player2Init));
            c2.sendGameInformation(new RequestGameInformation(name2, name1, player2Init, player1Init));

            // Set controller as Observer of view, set view as Observer of game
            game.addObserver(c1);
            game.addObserver(c2);
            c1.addObserver(controller);
            c2.addObserver(controller);

            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);

            controller.initProcess();

            c1.close();
            c2.close();
        }
    }
//
//    // This function gets created when one player is connect and it's waiting for another one. WIP: works only with 2 players
//    public synchronized void oldUselessLobby(ServerSocket connection, String name) {
//        //nameGodLogicMap.put(name, godName);
//
//        waitingConnection.put(name, connection);
//        if (waitingConnection.size() == 2) {  // Right now only works with 2 players
//            System.out.println("Are we at least here?");
//            ArrayList<String> keys = new ArrayList<>(waitingConnection.keySet());
//            ServerSocket c1 = waitingConnection.get(keys.get(0));
//            ServerSocket c2 = waitingConnection.get(keys.get(1));
//            String name1 = keys.get(0);
//            String name2 = keys.get(1);
//            System.out.println(name1 + " and " + name2);
//            Game game = new Game();
//            Controller controller = new Controller(game);
//            controller.initPlayer(name1);
//            controller.initPlayer(name2);
//
//            // Set controller as Observer of view, set view as Observer of game
//            game.addObserver(c1);
//            game.addObserver(c2);
//            c1.addObserver(controller);
//            c2.addObserver(controller);
//
//            //c1.updateObservers(new AnswerPlayerName(player1.getName()));
//            //c2.updateObservers(new AnswerPlayerName(player2.getName()));
//
//            playingConnection.put(c1, c2);
//            playingConnection.put(c2, c1);
//            waitingConnection.clear();
//
//            // Initializing the game components and states
//            System.out.println("hello world before setting the gods");
//            game.getPrintableGameStatus();
//            System.out.println("\n\n" + nameGodLogicMap.get(name1) + " and the other is " + nameGodLogicMap.get(name2) + "\n\n");
////            game.setPlayerGod(nameGodLogicMap.get(name1), name1); THIS SHOULD NOT EXIST
////            game.setPlayerGod(nameGodLogicMap.get(name2), name2);
//            System.out.println("Hello world after setting the gods");
//            game.getPrintableGameStatus();
//            for(int i = 0; i < 2; i++) {
//                System.out.println("So... not sending for some reason?");
//                c1.send(new RequestWorkerPlacement(game.getAllWorkersAsMatrix(), 'A')); // He is now A
//                System.out.println("Sent!");
//                AnswerWorkersPosition answer = (AnswerWorkersPosition) c1.readPosition();
//                System.out.println("Answer received");
//                controller.update(answer);
//                c1.send(new RequestUpdateBoardView(game.getBoard()));
//                c1.send(new RequestDisplayBoard());
//            }
//            for(int i = 0; i < 2; i++){
//                c2.send(new RequestWorkerPlacement(game.getAllWorkersAsMatrix(), 'B')); // He is now B
//                AnswerWorkersPosition answer = (AnswerWorkersPosition) c2.readPosition();
//                System.out.println("Answer received");
//                controller.update(answer);
//                c2.send(new RequestUpdateBoardView(game.getBoard()));
//                c2.send(new RequestDisplayBoard());
//            }
//            game.init();
//
//
//
//            // Start the game
//            game.gameStart();
//        }
//    }

//    public void readAnswer(Answer answer) {
//        controller.
//    }

    public void run() {
        int i = 0;
        while (i < nPlayers) {
            try {
                Socket clientSocket = serverSocket.accept(); // The server accepts a connection
                ServerSocket connection = new ServerSocket(clientSocket, this);
                executor.submit(connection); // Creates new thread
                i += 1;
            } catch (IOException e){
                System.err.println("This" + e.getMessage());
            }
        }
    }

}