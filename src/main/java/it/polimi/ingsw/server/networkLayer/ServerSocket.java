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


    /**
     * Constructor
     * @param socket socket the object refers to
     * @param server already intialized server
     */
    public ServerSocket(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * Sends a Request object to the client synchronously
     * @param request
     */
    public void send(Request request) {
        try {
            outputStream.reset();
            outputStream.writeObject(request);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("IO Exception on send, you are dumb");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Async read from the socket's objectInputStream, updates the object's observers every time
     * an answer arrives.
     */
    public void asyncReadFromSocket() {
        new Thread(() -> {
            try {
                while (true) {
                    Object temp = inputStream.readObject();
                    Answer answer = (Answer) temp;
                    answer.setInitial(playerInitial);
                    updateObservers(answer);
                }
            } catch (IOException e) {
                System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete from the Game" + "\u001B[0m");
                e.printStackTrace();
                System.out.println("Is it here?!");
                closeServerSocket();
                // If there are observers it means the game has started; otherwise just take away this user from the lobby
                if (getObserversSize() != 0)
                    updateObservers(new AnswerKillPlayer(playerInitial));
                else {
                    playingLobby.deletePlayer(playerName);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete from the Game" + "\u001B[0m");
                e.printStackTrace();
                System.out.println("Or here?!");
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

    /**
     * Asks the client for lobby number, player name and number of players,
     * if it catches an  IOException or a ClassNotFound exception it will consider the client dead,
     * it will close the socket and delete the player by updating the object's observers with a AnswerKillPlayer object
     * @return answer object with the lobby identifier, player's name and number of players in the lobby
     */
    public AnswerLobbyAndName readFromSocketPlayerLobbyAndName() {
        try {
            Object temp = inputStream.readObject();
            return (AnswerLobbyAndName) temp;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete it from the Game" + "\u001B[0m");
            e.printStackTrace();
            closeServerSocket();
            updateObservers(new AnswerKillPlayer(playerInitial));
        }
        return null;
    }

    /**
     * Closes the ServerSocket by setting the isActive flag to false
     */
    public void closeServerSocket() {
        isActiveFlag = false;
    }


    /**
     * Sets player's initial
     * @param playerInitial player's initial
     */
    public void setPlayerInitial(char playerInitial) {
        this.playerInitial = playerInitial;
    }

    /**
     * Sends game information to the client
     * @param gameInfo object that contains all the names and initials of the players in the game
     */
    public void sendGameInformation(RequestGameInformation gameInfo) {
        System.out.println("Sending game information");
        send(gameInfo);
    }

    /**
     * Runs the thread, asking the client to select a lobby and then waiting for the opponent's move by sending
     * a RequestWaitOpponent message to the client
     */
    @Override
    public void run() {
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Lobby unSelected = null;
        int n = 0;
        while (unSelected == null) {
            send(server.getRequestServerInformation(n));
            AnswerLobbyAndName lobbyAndName = readFromSocketPlayerLobbyAndName();
            int lobbyNumber = lobbyAndName.getLobbyNumber();
            playerName = lobbyAndName.getName();
            // lobbyNumber 0 is the lobby number for "refresh" options
            if (lobbyNumber != 0) {
                unSelected = server.isAvailable(lobbyNumber, playerName, this, lobbyAndName.getNPlayers());
                n = 1;
            }
            else
                n = 0;
        }
        playingLobby = unSelected;
        System.out.println("Joined the lobby!");
        send(new RequestWaitOpponentMove());

        asyncReadFromSocket();
    }

    /**
     * Called after receiving an update from one of the observed objects, it sends the requests to the client
     * @param request message sent by an observed object and sent to the client
     */
    @Override
    public void update(Request request) {
        if (isActiveFlag) {
            if (this.playerInitial == request.getInitial() || request.getInitial() == '*') {
                send(request);
            }
            else {
                send(new RequestWaitOpponentMove());
            }
        }
    }

    /**
     * Prints an error message, no Answer class objects should be sent to the client
     * @param answer message sent by an observed object
     */
    @Override
    public void update(Answer answer) {
        System.out.println("ServerSocket doesn't deal with Answers");
    }
}
