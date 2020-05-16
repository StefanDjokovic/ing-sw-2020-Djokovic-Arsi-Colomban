package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.clientCLI;
import it.polimi.ingsw.messages.Request;

public class RequestGameInformation extends Request {

    private String playerName1;
    private String playerName2;
    private String playerName3;
    private char playerInit1;
    private char playerInit2;
    private char playerInit3;
    private int n_players;

    // TODO: add second contructor for 3 players, let the view have these infos with accept
    public RequestGameInformation(String playerName1, String playerName2,
                                  char playerInit1, char playerInit2) {
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        this.playerInit1 = playerInit1;
        this.playerInit2 = playerInit2;
        this.n_players = 2;
        this.isAsync = true;

    }


    @Override
    public void accept(clientCLI clientCLI) {
        clientCLI.setPlayerInit(playerInit1);
    }

    public char getInitial() { return this.initial; }
}
