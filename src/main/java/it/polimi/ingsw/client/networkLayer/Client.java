package it.polimi.ingsw.client.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.messages.request.RequestPlayerName;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Observer {

    private String ip;
    private int port;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean isActiveFlag = true;
    private View view;

    public Client(String ip, int port, View view) {
        this.ip = ip;
        this.port = port;
        this.view = view;
    }

    public boolean isActive() {
        return isActiveFlag;
    }

    public void setActive(boolean flag) {
        this.isActiveFlag = flag;
    }

    public Thread asyncSocketRead(final ObjectInputStream inputStream) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(isActive()) {
                        Request request = (Request) inputStream.readObject();
                        request.accept(view);
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
        outputStream.writeObject(answer);
        outputStream.flush();
    }

    public void run() throws IOException {
        view.addObserver(this);

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
        }
    }

    @Override
    public void update(Request request) {
        System.out.println("Error: controller shouldn't receive requests");
    }

    @Override
    public void update(Answer answer) {
        try {
            this.socketWrite(answer);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}