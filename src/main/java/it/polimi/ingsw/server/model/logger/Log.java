package it.polimi.ingsw.server.model.logger;

import java.util.ArrayList;

public class Log {

    private int type = -1;
    private char playerInit;
    private ArrayList<Integer> action;

    /**
     * Constructor method if there are no logs, a Log of type -1 is returned
     */
    // If there are no logs a Log of type -1 is returned
    public Log() { }

    /**
     * Constructor method that logs a turn step from a starting tile to a destination tile (type 0)
     * @param XFrom x coordinate of the starting tile
     * @param YFrom y coordinate of the starting tile
     * @param XTo x coordinate of the destination tile
     * @param YTo y coordinate of the destination tile
     * @param playerInit player's initial
     */
    public Log(int XFrom, int YFrom, int XTo, int YTo, char playerInit) {
        action = new ArrayList<>();
        this.type = 0;
        action.add(XFrom);
        action.add(YFrom);
        action.add(XTo);
        action.add(YTo);
        this.playerInit = playerInit;
    }

    /**
     * Constructor method that logs a pass (type 1)
     * @param playerInit player's initial
     */
    public Log(char playerInit) {
        this.type = 1;
        this.playerInit = playerInit;
    }

    /**
     * Overriden method that returns what has been logged
     * @return Nothing logged" if there was no log, the coordinates of starting tile and destination tile or "skipped" if the turn has been skipped
     */
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

    /**
     *
     * @return
     */
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
