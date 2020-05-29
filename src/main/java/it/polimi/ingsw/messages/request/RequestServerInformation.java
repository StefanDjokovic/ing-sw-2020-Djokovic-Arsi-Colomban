package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;

public class RequestServerInformation extends Request {

    ArrayList<LobbyView> lobbies;

    // Note: lobbies can be null
    public RequestServerInformation(ArrayList<LobbyView> lobbies) {
        this.lobbies = lobbies;
        this.message = "This request contains Game Lobby Info";
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.lobbyAndNameSelection(lobbies);
    }
}
