package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;

// Used only by Server to organize the lobby
public class AnswerLobbyAndName extends Answer {

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
    public void act(Controller controller) {
        System.out.println("This Answer should not go into Controller");
    }
}
