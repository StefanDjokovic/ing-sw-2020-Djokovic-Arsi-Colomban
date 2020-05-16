package it.polimi.ingsw.server.networkLayer;

import it.polimi.ingsw.Observable;
import it.polimi.ingsw.Observer;
import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.messages.Request;
import it.polimi.ingsw.server.model.player.Player;


public class VirtualView extends Observable implements Observer  {

    private Player player;
    private ServerSocket clientConnection;

    private class MessageReceiver implements Observer {

        @Override
        public void update(Answer answer) {
            System.out.println("Answer inbound!");
            updateObservers(answer);
        }

        @Override
        public void update(Request request) {
            System.out.println("Controller shouldn't receive Requests");
        }
    }

    public VirtualView(Player player, ServerSocket clientConnection) {
        this.player = player;
        this.clientConnection = clientConnection;
        clientConnection.addObserver(new MessageReceiver());
    }

    @Override
    public void update(Request request) {
        if(player.getName().charAt(0) == request.getInitial()) {
            clientConnection.send(request);
        }
    }


    @Override
    public void update(Answer answer) {
       System.out.println("clientCLI shouldn't receive answers");
    }
}
