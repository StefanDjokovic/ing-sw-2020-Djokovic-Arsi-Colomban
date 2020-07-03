package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;
import it.polimi.ingsw.server.controller.ControllerView;

// Used only by Server to organize the lobby
public class AnswerLobbyAndName extends Answer {

    private static final long serialVersionUID = 6529685098267757601L;

    private int lobbyNumber;
    private int nPlayers;
    private String name;


    public AnswerLobbyAndName(int lobbyNumber, String name, int nPlayers) {
        this.lobbyNumber = lobbyNumber;
        this.name = name;
        this.nPlayers = nPlayers;
        message = "Answer Lobby number and Player's name";
    }

    public String getName() {
        return this.name;
    }

    public int getNPlayers() { return nPlayers; }

    public int getLobbyNumber() {
        return this.lobbyNumber;
    }

    @Override
    public void act(ControllerView controller) {
        System.out.println("This Answer should not go into Controller");
    }
}
