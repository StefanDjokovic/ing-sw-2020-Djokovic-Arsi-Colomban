package it.polimi.ingsw.server.controller;

import java.util.ArrayList;

public interface ControllerView {

    void setPlayerGod(ArrayList<String> godNameList, char initial);
    void setPlayerGod(String godName, char initial);
    void setWorker(int x, int y, char initial);
    char initPlayer(String name);

    void executePass();
    void executeMoveOrBuild(int posXFrom, int posYFrom, int posXTo, int posYTo);
    void killPlayer(char initial);

}
