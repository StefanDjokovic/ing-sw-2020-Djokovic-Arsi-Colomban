package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPlayerName;
import it.polimi.ingsw.messages.request.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSocket extends Observable implements Runnable, Observer {

    private Socket socket;
    private Server server;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private boolean isActiveFlag = true;
    private char playerInitial;

    // TODO: fix initials for the Socket, so that it can understand to whom it shall send the message

    public ServerSocket(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

//    private synchronized boolean isActive() {
//        return isActiveFlag;
//    }

    public void send(Request request) {
        try {
            System.out.println("write socket in SocketConnection");
            outputStream.reset();
            System.out.print("Message before sending: ");
            request.printMessage();
            outputStream.writeObject(request);
            outputStream.flush();
            System.out.println("flushed ServerSocket");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
//
//    public void asyncSend(final Request request){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                    send(request);
//            }
//        }).start();
//    }

    public Answer readFromSocket() {
        try {
            System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
            Object temp = inputStream.readObject();
            Answer answer = (Answer) temp;
            answer.setInitial(playerInitial);
            System.out.println("AAAAAAAAAAAAAAAAAAAA\n");
            updateObservers(answer);
            //server.readAnswer(answer);
            //return answer;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Thread asyncReadTurn(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while(true) {
                        System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
                        Object temp = inputStream.readObject();
                        Answer answer = (Answer) temp;
                        answer.setInitial(playerInitial);
                        System.out.println("AAAAAAAAAAAAAAAAAAAA\n");
                        updateObservers(answer);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        return thread;
    }
//
//    public synchronized void readTurn() {
//        try{
//            while(true) {
//                AnswerPowerCoordinates answer = (AnswerPowerCoordinates) inputStream.readObject();
//                updateObservers(answer);
//            }
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//    }

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

    public void waitingOpponentMove() {
        send(new RequestWaitOpponentMove());
    }

    public void setPlayerInitial(char playerInitial) {
        this.playerInitial = playerInitial;
    }

    public void sendGameInformation(RequestGameInformation gameInfo) {
        System.out.println("Sending game information");
        send(gameInfo);
    }

    @Override
    public void run(){
        try {
            System.out.println("Running ServerSocket");
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Asking for player's name...");
            send(new RequestPlayerName("."));
            AnswerPlayerName playerName = (AnswerPlayerName) inputStream.readObject();

            System.out.println("\nNow would go here\n");
            server.lobby(this, playerName.getString());


        }  catch(Exception e){
            e.printStackTrace();
        } finally {
           //close();
        }
    }

    @Override
    public void update(Request request) {
        if (this.playerInitial == request.getInitial() || request.getInitial() == '*') {
            request.printMessage();
            System.out.println("Request sent to " + request.getInitial());
            send(request);
            if (!request.isAsync()) {
                try {
                    Thread t = asyncReadTurn();
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void update(Answer answer) {
        System.out.println("clientCLI shouldn't receive answers");
    }
}
