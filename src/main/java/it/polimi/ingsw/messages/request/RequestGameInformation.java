package it.polimi.ingsw.messages.request;

import it.polimi.ingsw.client.view.ClientView;
import it.polimi.ingsw.messages.Request;

import java.util.ArrayList;

public class RequestGameInformation extends Request {

    private ArrayList<String> playersName = new ArrayList<>();
    private ArrayList<Character> playersInitial = new ArrayList<>();
    private ArrayList<String> godNames;
    private String playerName1;
    private String playerName2;
    private String playerName3;
    private char playerInit1;
    private char playerInit2;
    private char playerInit3;
    private int nPlayers;

    public RequestGameInformation(String playerName1, String playerName2,
                                  char playerInit1, char playerInit2) {
        playersName.add(playerName1);
        playersName.add(playerName2);
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        playersInitial.add(playerInit1);
        playersInitial.add(playerInit2);
        this.playerInit1 = playerInit1;
        this.playerInit2 = playerInit2;
        this.nPlayers = 2;
        this.isAsync = true;
        this.message = "This message contains game information";
    }

    public RequestGameInformation(String playerName1, String playerName2,
                                  String playerName3, char playerInit1, char playerInit2,
                                  char playerInit3) {
        playersName.add(playerName1);
        playersName.add(playerName2);
        playersName.add(playerName3);
        this.playerName1 = playerName1;
        this.playerName2 = playerName2;
        this.playerName3 = playerName3;
        playersInitial.add(playerInit1);
        playersInitial.add(playerInit2);
        playersInitial.add(playerInit3);
        this.playerInit1 = playerInit1;
        this.playerInit2 = playerInit2;
        this.playerInit3 = playerInit3;
        this.nPlayers = 3;
        this.isAsync = true;

    }

    public RequestGameInformation(String playerName1, String playerName2,
                                  String playerName3, char playerInit1, char playerInit2,
                                  char playerInit3, String god1, String god2, String god3) {
        playersName.add(playerName1);
        playersName.add(playerName2);
        playersName.add(playerName3);
        playersInitial.add(playerInit1);
        playersInitial.add(playerInit2);
        playersInitial.add(playerInit3);
        this.nPlayers = 3;
        this.isAsync = true;
        godNames = new ArrayList<>();
        godNames.add(god1);
        godNames.add(god2);
        godNames.add(god3);
    }

    public RequestGameInformation(String playerName1, String playerName2,
                                  char playerInit1, char playerInit2,
                                  String god1, String god2) {
        playersName.add(playerName1);
        playersName.add(playerName2);
        playersInitial.add(playerInit1);
        playersInitial.add(playerInit2);
        this.nPlayers = 3;
        this.isAsync = true;
        godNames = new ArrayList<>();
        godNames.add(god1);
        godNames.add(god2);
    }

    @Override
    public void accept(ClientView clientView) {
        if (godNames == null) {
            clientView.setGameInformation(playersName, playersInitial, nPlayers);
        }
        else {
            clientView.setGodInformation(playersName, playersInitial, godNames, nPlayers);
        }
    }

    public char getInitial() { return this.initial; }
}
