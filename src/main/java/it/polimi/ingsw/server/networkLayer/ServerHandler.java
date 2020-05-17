package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.answers.AnswerWorkersPosition;
import it.polimi.ingsw.messages.request.RequestDisplayBoard;
import it.polimi.ingsw.messages.request.RequestUpdateBoardView;
import it.polimi.ingsw.messages.request.RequestWorkerPlacement;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler{

    private final int port = 4567;
    private ServerSocket serverSocket;
    private ExecutorService executor = Executors.newFixedThreadPool(64);
    private Map<String, SocketConnection> waitingConnection = new HashMap<>();
    private Map<SocketConnection, SocketConnection> playingConnection = new HashMap<>();
    private Map<String, String> nameGodLogicMap = new HashMap<>();

    public ServerHandler() throws IOException    {
        this.serverSocket = new ServerSocket(4568);
    }

    public synchronized void deregisterConnection(SocketConnection connection) {    //TODO: make it work for 3 players
        SocketConnection opponent = playingConnection.get(connection);
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

    // This function gets created when one player is connect and it's waiting for another one. WIP: works only with 2 players
    public synchronized void lobby(SocketConnection connection, String name, String godName) {
        nameGodLogicMap.put(name, godName);
        waitingConnection.put(name, connection);
        if (waitingConnection.size() == 2) {  // Right now only works with 2 players
            ArrayList<String> keys = new ArrayList<>(waitingConnection.keySet());
            SocketConnection c1 = waitingConnection.get(keys.get(0));
            SocketConnection c2 = waitingConnection.get(keys.get(1));
            String name1 = keys.get(0);
            String name2 = keys.get(1);
            Player player1 = new Player(name1, '*');
            Player player2 = new Player(name2,'*');
            VirtualView virtualView1 = new VirtualView(player1, c1);
            VirtualView virtualView2 = new VirtualView(player2, c2);
            Game game = new Game(player1, player2);
            Controller controller = new Controller(game);

            // Set controller as Observer of view, set view as Observer of game
            game.addObserver(c1);
            game.addObserver(c2);
            c1.addObserver(controller);
            c2.addObserver(controller);

            //c1.updateObservers(new AnswerPlayerName(player1.getName()));
            //c2.updateObservers(new AnswerPlayerName(player2.getName()));

            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingConnection.clear();

            // Initializing the game components and states
            game.setPlayerGod(nameGodLogicMap.get(name1), name1.charAt(0));
            game.setPlayerGod(nameGodLogicMap.get(name2), name2.charAt(0));
            for(int i = 0; i < 2; i++){
                c1.send(new RequestWorkerPlacement(game.getAllWorkersAsMatrix(), name1.charAt(0)));
                AnswerWorkersPosition answer = c1.readPosition();
                System.out.println("Answer received");
                controller.update(answer);
                c1.send(new RequestUpdateBoardView(game.getBoard()));
                c1.send(new RequestDisplayBoard());
            }
            for(int i = 0; i < 2; i++){
                c2.send(new RequestWorkerPlacement(game.getAllWorkersAsMatrix(), name2.charAt(0)));
                c2.readPosition();
                c2.send(new RequestUpdateBoardView(game.getBoard()));
                c2.send(new RequestDisplayBoard());
            }
            game.init();



            // Start the game
            game.gameStart();
        }
    }

    public void run() {
        while(true) {
            try {
                Socket clientSocket = serverSocket.accept(); // The server accepts a connection
                SocketConnection connection = new SocketConnection(clientSocket, this);
                executor.submit(connection); // Creates new thread
            } catch (IOException e){
                System.err.println("This" + e.getMessage());
            }
        }
    }


}