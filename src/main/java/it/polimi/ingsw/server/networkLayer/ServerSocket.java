package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerKillPlayer;
import it.polimi.ingsw.messages.answers.AnswerLobbyAndName;
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
    private Lobby playingLobby;
    private String playerName;


    public ServerSocket(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    public void asyncSend(Request request) {
        new Thread(() -> {
            System.out.println("Async send is being called");
            try {
                //outputStream.reset();
                System.out.print("Message before sending: ");
                request.printMessage();
                outputStream.writeObject(request);
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void send(Request request) {
        try {
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

    public void asyncReadFromSocket() {
        new Thread(() -> {
            System.out.println("Async read");
            try {
                while (true) {
                    System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
                    Object temp = inputStream.readObject();
                    Answer answer = (Answer) temp;
                    answer.setInitial(playerInitial);   // TODO: useless? to check
                    updateObservers(answer);
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete it from the Game" + "\u001B[0m");
                closeServerSocket();
                // If there are observers it means the game has started; otherwise just take away this user from the lobby
                if (getObserversSize() != 0)
                    updateObservers(new AnswerKillPlayer(playerInitial));
                else {
                    playingLobby.deletePlayer(playerName);
                }
            }
        }).start();
    }

    public AnswerLobbyAndName readFromSocketPlayerLobbyAndName() {
        try {
            System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
            Object temp = inputStream.readObject();
            return (AnswerLobbyAndName) temp;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete it from the Game" + "\u001B[0m");
            closeServerSocket();
            updateObservers(new AnswerKillPlayer(playerInitial));
        }
        return null;
    }

    public void closeServerSocket() {
        isActiveFlag = false;
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
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            Lobby unSelected = null;
            int n = 0;
            while (unSelected == null) {
                send(server.getRequestServerInformation(n));
                AnswerLobbyAndName lobbyAndName = readFromSocketPlayerLobbyAndName();
                int lobbyNumber = lobbyAndName.getLobbyNumber();
                playerName = lobbyAndName.getName();
                // lobbyNumber 0 is the lobby number for "refresh" options
                if (lobbyNumber != 0)
                    unSelected = server.isAvailable(lobbyNumber, playerName, this, lobbyAndName.getNPlayers());
                n = 1;
            }
            playingLobby = unSelected;
            System.out.println("Joined the lobby!");
            send(new RequestWaitOpponentMove());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Starting asyncRead");
        asyncReadFromSocket();
    }


    @Override
    public void update(Request request) {
        if (isActiveFlag) {
            if (this.playerInitial == request.getInitial() || request.getInitial() == '*') {
                request.printMessage();
                System.out.println("Request sent to " + request.getInitial());
                send(request);
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
