package it.polimi.ingsw.client.networkLayer;

import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.view.ClientGUI;
import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

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
                    System.out.println("message: " + r.getMessage());
                    r.accept(clientView);
                    if (r.getMessage().equals("END"))
                        noWinners = false;
                }
            } catch (SocketException e) {
                System.out.println("Wasn't able to send the message, the server appears to be down");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("DISCONNECTED FROM THE SERVER");
            }

        });
        thread.start();
        return thread;
    }

    public synchronized void socketWrite(Answer answer) throws IOException {
        outputStream.reset();
        outputStream.writeObject(answer);
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
            ClientGUI.getInstance().displayLostConnection();
        }
    }

    @Override
    public void update(Request request) {
        System.out.println("Error: client should not send Requests");
    }

    @Override
    public void update(Answer answer) {
        try {
            answer.printMessage();
            this.socketWrite(answer);
        } catch (IOException e) {
            System.out.println("Server appears to be down, closing the connection");
        }
    }
}