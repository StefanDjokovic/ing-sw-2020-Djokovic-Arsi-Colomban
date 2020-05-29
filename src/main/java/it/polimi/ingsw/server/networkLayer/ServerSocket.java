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
                updateObservers(new AnswerKillPlayer(playerInitial));
            }
        }).start();
    }


    public void readFromSocket() {
        try {
            System.out.println("Me, ServerSocket " + playerInitial + " received the answer");
            Object temp = inputStream.readObject();
            Answer answer = (Answer) temp;
            answer.setInitial(playerInitial);   // TODO: useless? to check
            updateObservers(answer);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\u001B[44m" + "Client " + playerInitial + " has Died. Will delete it from the Game" + "\u001B[0m");
            closeServerSocket();
            updateObservers(new AnswerKillPlayer(playerInitial));
        }
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
        asyncSend(gameInfo);
    }

    @Override
    public void run() {
        try {
            System.out.println("Running ServerSocket");
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Asking for player's name...");
            asyncSend(new RequestPlayerName("."));
            AnswerPlayerName playerName = (AnswerPlayerName) inputStream.readObject();
            System.out.println("Activating asyncRead");
            while (!server.lobby(this, playerName.getString())) {
                asyncSend(new RequestPlayerName("."));
                playerName = (AnswerPlayerName) inputStream.readObject();
                System.out.println("Activating asyncRead");
                asyncReadFromSocket();
            }
            System.out.println("\n\nServer.lobby finished\n\n");
            asyncReadFromSocket();
            System.out.println("\nNow would go here: server.lobby\n");
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
//                if (!request.isAsync()) {
//                    asyncReadFromSocket();
//                }
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
