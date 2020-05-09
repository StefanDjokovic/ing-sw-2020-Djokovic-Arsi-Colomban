/*
package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.client.view.View;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.model.Game;
import it.polimi.ingsw.server.model.player.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class VirtualView extends Observable implements Observer  {

    private Player player;
    private SocketConnection clientConnection;

    private class MessageReceiver implements Observer {

        @Override
        public void update(Answer answer) {
            try{

            }catch(IllegalArgumentException e){
                // clientConnection.asyncSend("Error!");
            }
        }

        @Override
        public void update(Request request) {
            System.out.println("No");
        }
    }


    public VirtualView(Player player, SocketConnection clientConnection) {
        this.player = player;
        this.clientConnection = clientConnection;
        clientConnection.addObserver(new MessageReceiver());
    }

    @Override
    public void update(Request request) {
        clientConnection.send(request);
    }


    @Override
    public void update(Answer answer) {
       System.out.println("View shouldn't receive answers");
    }
}
*/