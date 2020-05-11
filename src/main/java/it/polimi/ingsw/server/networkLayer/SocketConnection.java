package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerGod;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.request.RequestPlayerGod;
import it.polimi.ingsw.messages.request.RequestPlayerName;
import it.polimi.ingsw.messages.request.RequestWorkerPlacement;
import it.polimi.ingsw.server.model.player.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketConnection extends Observable implements Runnable, Observer {

    private Socket socket;
    private ServerHandler server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isActiveFlag = true;
    private String playerName;

    public SocketConnection(Socket socket, ServerHandler server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive() {
        return isActiveFlag;
    }

    public synchronized void send(Request request) {
        try {
            outputStream.reset();
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void asyncSend(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                    send(request);
            }
        }).start();
    }

    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        isActiveFlag = false;
    }

    public void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void run(){
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Asking for player's name...");
            send(new RequestPlayerName("."));
            AnswerPlayerName playerName = (AnswerPlayerName) inputStream.readObject();
            //updateObservers(playerName);
            this.playerName = playerName.getString();

            // TODO: dirty fix, make it better and include it in Game
            ArrayList<String> opt = new ArrayList<>();
            opt.add("Basic");
            opt.add("Apollo");
            opt.add("Artemis");
            opt.add("Atlas");
            opt.add("Pan");
            opt.add("Demeter");
            opt.add("Hephaestus");
            opt.add("Minotaur");
            opt.add("Prometheus");
            opt.add("Athena");
            send(new RequestPlayerGod(this.playerName.charAt(0), opt));
            AnswerPlayerGod playerGod = (AnswerPlayerGod) inputStream.readObject();
            //TODO: end of the dirty fix

            server.lobby(this, playerName.getString(), playerGod.getGodName());

            while(true) {
                Answer answer = (Answer) inputStream.readObject();
                updateObservers(answer);
            }
        }  catch(Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
    }

    @Override
    public void update(Request request) {
        if(this.playerName.charAt(0) == request.getInitial()) {
            send(request);
        }
    }


    @Override
    public void update(Answer answer) {
        System.out.println("View shouldn't receive answers");
    }
}
