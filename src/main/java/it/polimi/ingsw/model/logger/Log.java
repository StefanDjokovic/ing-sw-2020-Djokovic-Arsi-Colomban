package it.polimi.ingsw.model.logger;

import java.util.ArrayList;

public class Log {

    private int type = -1;
    private char playerInit;
    private ArrayList<Integer> action;


    // If there are no logs a Log of type -1 is returned
    public Log() { }

    // An action is type 0
    public Log(int XFrom, int YFrom, int XTo, int YTo, char playerInit) {
        action = new ArrayList<>();
        this.type = 0;
        action.add(XFrom);
        action.add(YFrom);
        action.add(XTo);
        action.add(YTo);
        this.playerInit = playerInit;
    }

    // Pass is type 1
    public Log(char playerInit) {
        this.type = 1;
        this.playerInit = playerInit;
    }

    @Override
    public String toString() {
        if (type == -1) {
            return "Nothing Logged";
        }
        else if (type == 0) {
            return playerInit + " : " + action.get(0) + " " + action.get(1) + " to " + action.get(2) + " " + action.get(3);
        }
        else if (type == 1) {
            return playerInit + " skipped";
        }
        else
            return "STH WRONG HERE";
    }

    public char getPlayerInit() {
        return playerInit;
    }

    public int getAction(int pos) {
        return action.get(pos);
    }

    public int getType() {
        return type;
    }
}
