package it.polimi.ingsw.model.logger;

import it.polimi.ingsw.model.logger.Log;

import java.util.ArrayList;

public class Logger {
    private ArrayList<Log> logHistory;

    public Logger() {
        logHistory = new ArrayList<>();
    }

    public Log getLastLog() {
        if (logHistory.size() > 0)
            return logHistory.get(logHistory.size() - 1);
        else
            return new Log();
    }

    public Log getSecondToLastLog() {
        if (logHistory.size() > 1)
            return logHistory.get(logHistory.size() - 2);
        else
            return new Log();
    }

    public void addNewLog(int XFrom, int YFrom, int XTo, int YTo, char playerInit) {
        logHistory.add(new Log(XFrom, YFrom, XTo, YTo, playerInit));
    }

    public void addNewLog(char playerInit) {
        logHistory.add(new Log(playerInit));
    }

    @Override
    public String toString() {
        if (logHistory.size() > 0) {
            StringBuilder toReturn = new StringBuilder();
            toReturn.append("Logged:\n");
            for (Log l: logHistory) {
                toReturn.append(l.toString()).append("\n");
            }

            return String.valueOf(toReturn);
        }
        else
            return "Nothing Logged yet";

    }
}
