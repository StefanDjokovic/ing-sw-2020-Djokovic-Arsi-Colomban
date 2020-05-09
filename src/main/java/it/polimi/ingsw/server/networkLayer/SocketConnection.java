package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.request.RequestPlayerGod;
import it.polimi.ingsw.messages.request.RequestPlayerName;
import it.polimi.ingsw.messages.request.RequestWorkerPlacement;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
            // send("Input player name");
            send(new RequestPlayerName("."));
            AnswerPlayerName playerName = (AnswerPlayerName) inputStream.readObject();
            //updateObservers(playerName);
            this.playerName = playerName.getString();
            server.lobby(this, playerName.getString());

            while(isActive()) {
                Answer answer = (Answer) inputStream.readObject();
                updateObservers(answer);
                System.out.println("test");
            }
        }  catch(IOException | ClassNotFoundException e){
            System.err.println(e.getMessage());
        } finally {
            close();
        }
    }

    @Override
    public void update(Request request) {
        send(request);
    }

    public void update(RequestPlayerGod request) {
        if (request.getInitial() == playerName.charAt(0)) {
            send(request);
        }
    }

    public void update(RequestWorkerPlacement request) {
        if (request.getInitial() == playerName.charAt(0)) {
            send(request);
        }
    }

    @Override
    public void update(Answer answer) {
        System.out.println("View shouldn't receive answers");
    }
}
