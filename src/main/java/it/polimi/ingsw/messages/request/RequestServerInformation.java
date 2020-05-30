package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.LobbyView;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;

public class RequestServerInformation extends Request {

    ArrayList<LobbyView> lobbies;
    int error;

    // Note: lobbies can be null
    public RequestServerInformation(ArrayList<LobbyView> lobbies, int error) {
        this.lobbies = lobbies;
        this.error = error;
        if (error == 0)
            this.message = "This request contains Game Lobby info";
        else
            this.message = "This request contains Game Lobby Info, resent because of some issue";
    }

    @Override
    public void accept(ClientView clientView) {
        clientView.lobbyAndNameSelection(lobbies, error);
    }
}