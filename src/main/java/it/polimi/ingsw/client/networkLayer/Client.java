package it.polimi.ingsw.client.networkLayer;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.view.ClientCLI;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client implements Observer {

    private String ip;
    private int port;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean isActiveFlag = true;
    private ClientView clientView;

    public Client(String ip, int port, ClientView clientView) {
        this.ip = ip;
        this.port = port;
        this.clientView = clientView;
    }

    public boolean isActive() {
        return isActiveFlag;
    }

    public void setActive(boolean flag) {
        this.isActiveFlag = flag;
    }

    public boolean noWinners = true;
    public Thread asyncSocketRead(final ObjectInputStream inputStream) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(noWinners) {
                        System.out.println("Am I at waiting for an object?");
                        Object request = inputStream.readObject();
                        System.out.println("YES! reading!" + " " + request);
                        Request r = (Request)request ;
                        System.out.println("message: " + r.getMessage());
                        r.accept(clientView);
                        if (r.getMessage().equals("END"))
                            noWinners = false;
                    }
                } catch (Exception e) {
                    setActive(false);
                }

            }
        });
        thread.start();
        return thread;
    }

/*
    public Thread asyncSocketWrite(Scanner stdin, final ObjectOutputStream outputStream) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isActive()) {
                        String inputLine = stdin.nextLine();
                        outputStream.writeObject(inputLine);
                        outputStream.flush();
                    }
                } catch (Exception e) {
                    setActive(false);
                }
            }
        });
        thread.start();
        return thread;
    }
*/
    public synchronized void socketWrite(Answer answer) throws IOException {
        outputStream.reset();
        //System.out.println("Am I writing it at least?");
        outputStream.writeObject(answer);
        //System.out.println("Am I writing it at least 2?");
        outputStream.flush();
    }

    public void run() throws IOException {
        clientView.addObserver(this);

        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());

        try {
            Thread t = asyncSocketRead(inputStream);
            t.join();
        } catch (InterruptedException e){
            System.out.println("Connection closed from the client side");
        } finally {
            inputStream.close();
            outputStream.close();
            socket.close();
            System.out.println("DISCONNECTED FROM SERVER");
        }
    }

    @Override
    public void update(Request request) {
        System.out.println("Error: client should send Requests");
    }

    @Override
    public void update(Answer answer) {
        try {
            //System.out.println("Is this sent?");
            this.socketWrite(answer);
            System.out.println("Sent!?");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}