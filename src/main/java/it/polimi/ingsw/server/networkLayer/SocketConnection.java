package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.request.RequestPlayerName;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketConnection extends Observable implements Runnable{

    private Socket socket;
    private ServerHandler server;
    private ObjectOutputStream outputStream;
    private boolean isActiveFlag = true;
    private String name;

    public SocketConnection(Socket socket, ServerHandler server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive() {
        return isActiveFlag;
    }

    public synchronized void send(Request request) throws IOException{
        outputStream.reset();
        outputStream.writeObject(request);
        outputStream.flush();
    }

    public void asyncSend(final Request request){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    send(request);
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
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
        ObjectInputStream inputStream;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            // send("Input player name");
            send(new RequestPlayerName("."));
            AnswerPlayerName playerName = (AnswerPlayerName) inputStream.readObject();
            updateObservers(playerName);
            server.lobby(this, playerName.getString());

            while(isActive()) {
                Answer answer = (Answer) inputStream.readObject();
                updateObservers(answer);
            }
        }  catch(IOException | ClassNotFoundException e){
        System.err.println(e.getMessage());
        } finally {
            close();
        }
    }
}
