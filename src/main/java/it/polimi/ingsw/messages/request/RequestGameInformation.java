package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

public class RequestGameInformation extends Request {

    private String playerName1;
    private String playerName2;
    private String playerName3;
    private char playerInit1;
    private char playerInit2;
    private char playerInit3;
    private int n_players;

    public RequestGameInformation(String playerName1, String playerName2,
                                  char playerInit1, char playerInit2) {
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        this.playerInit1 = playerInit1;
        this.playerInit2 = playerInit2;
        this.n_players = 2;
        this.isAsync = true;
        this.message = "This message contains game information";
    }

    public RequestGameInformation(String playerName1, String playerName2,
                                  String playerName3, char playerInit1, char playerInit2,
                                  char playerInit3) {
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        this.playerName3 = playerName3;
        this.playerInit1 = playerInit1;
        this.playerInit2 = playerInit2;
        this.playerInit3 = playerInit3;
        this.n_players = 3;
        this.isAsync = true;

    }

    // TODO: this method can pass the information about the other players, currently sets only playerInit1
    @Override
    public void accept(ClientView clientView) {
        clientView.setPlayerInit(playerInit1);
    }

    public char getInitial() { return this.initial; }
}
