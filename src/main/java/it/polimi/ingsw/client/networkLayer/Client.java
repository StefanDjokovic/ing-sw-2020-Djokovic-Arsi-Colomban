package it.polimi.ingsw.client.networkLayer;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.answers.AnswerPing;
import it.polimi.ingsw.messages.request.RequestPing;
import it.polimi.ingsw.messages.request.RequestWaitOpponentMove;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class Client implements Observer {

    private String ip;
    private int port;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ClientView clientView;

    public Client(String ip, int port, ClientView clientView) {
        this.ip = ip;
        this.port = port;
        this.clientView = clientView;
    }

    public boolean noWinners = true;
    public Thread asyncSocketRead(final ObjectInputStream inputStream) {
        Thread thread = new Thread(() -> {
            try {
                while(noWinners) {
                    Object request = inputStream.readObject();
                    Request r = (Request)request ;
                    //System.out.println("message: " + r.getMessage());
                    if (r instanceof RequestPing) {
                        socketWrite(new AnswerPing());
                        pinged = true;
                    }
                    if (r instanceof RequestWaitOpponentMove && !activated) {
                        activated = true;
                        pingMech();
                    }
                    accept(r);

                    if (r.getMessage().equals("END"))
                        noWinners = false;
                }
            } catch (IOException e) {
                System.out.println("Wasn't able to send the message, the server appears to be down");
            } catch (ClassNotFoundException e) {
                System.out.println("That message cannot be read");
            }

        });
        thread.start();
        return thread;
    }

    public void accept(Request request) {
        new Thread(() -> {
            request.accept(clientView);
        }).start();
    }

    /**
     * Every 5 seconds the socket sends a ping and expected an answer in the next 5 seconds, otherwise it kills the socket
     * and corresponding player
     */
    private boolean pinged = true;
    private boolean activated = false;
    public void pingMech() {
        new Thread(() -> {
            while (noWinners && pinged) {
                pinged = false;
                // Set a ping arrived to false and send a ping
                // wait 10 seconds
                try {
                    /* simulate a complex computation */
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException ignored) { }
                // if it arrived the variable would be set to true by the other reader
                // else, update with the kill player if this socket is still active

            }
            if (!pinged) {
                System.out.println("F for the server");
                clientView.displayLostConnection();
                noWinners = false;
            }
            // HERE WE HAVE TO DECLARE THE SERVER DEAD

        }).start();
    }



    public synchronized void socketWrite(Answer answer) {
        try {
            outputStream.reset();
            outputStream.writeObject(answer);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println("Error while sending through the socket");
        }
    }

    public void run(){
        clientView.addObserver(this);
        connect();
    }

    public void connect() {
        Socket socket = null;
        try {
            socket = new Socket(ip, port);
            System.out.println("Connection established");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Something went wrong while connecting");
        }

        try {
            Thread t = asyncSocketRead(inputStream);
            t.join();
        } catch (InterruptedException e){
            System.out.println("Connection closed from the client side");
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                if (socket != null)
                    socket.close();
                clientView.displayLostConnection();
            } catch (IOException e) {
                System.out.println("Closure didn't work that great");
            }

        }
    }

    @Override
    public void update(Request request) {
        System.out.println("Error: client should not send Requests");
    }

    @Override
    public void update(Answer answer) {
        answer.printMessage();
        this.socketWrite(answer);
    }
}