package it.polimi.ingsw.messages.answers;

import it.polimi.ingsw.messages.Answer;
import it.polimi.ingsw.server.controller.Controller;

public class AnswerLobbyAndName extends Answer {

    int lobbyNumber;
    int nPlayers;
    String name;


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
        System.out.println("With this method it should not be activated");
    }
}
