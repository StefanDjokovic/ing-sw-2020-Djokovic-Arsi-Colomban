package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;
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
            System.out.println("IO Exception on send, you are dumb");
            System.err.println(e.getMessage());
        }
    }


    public Answer readFromSocket() {
        try {
            System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
            Object temp = inputStream.readObject();
            Answer answer = (Answer) temp;
            answer.setInitial(playerInitial);
            System.out.println("AAAAAAAAAAAAAAAAAAAA\n");
            updateObservers(answer);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete it from the Game" + "\u001B[0m");
            closeServerSocket();
            updateObservers(new AnswerKillPlayer(playerInitial));
        }
        return null;
    }


    public synchronized void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        isActiveFlag = false;
    }

    public void closeServerSocket() {
        isActiveFlag = false;
    }

    public void close() {
        closeConnection();
        System.out.println("De-registering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }


    public void setPlayerInitial(char playerInitial) {
        this.playerInitial = playerInitial;
    }

    public void sendGameInformation(RequestGameInformation gameInfo) {
        System.out.println("Sending game information");
        send(gameInfo);
    }

    @Override
    public void run() {
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
            //close();
        } finally {
           //close();
        }
    }

    @Override
    public void update(Request request) {
        if (isActiveFlag) {
            if (this.playerInitial == request.getInitial() || request.getInitial() == '*') {
                request.printMessage();
                System.out.println("Request sent to " + request.getInitial());
                send(request);
                if (!request.isAsync()) {
                    readFromSocket();
                }
                System.out.println("Terminating update ServerSocket " + playerInitial);
            }
            else {
                send(new RequestWaitOpponentMove());
            }
        }
    }


    @Override
    public void update(Answer answer) {
        System.out.println("clientCLI shouldn't receive answers");
    }
}
