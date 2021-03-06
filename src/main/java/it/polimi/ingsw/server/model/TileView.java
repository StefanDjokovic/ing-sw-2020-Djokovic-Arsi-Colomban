package it.polimi.ingsw.server.model;

import java.io.Serializable;

public class TileView implements Serializable {

    private int buildingLevel;
    private boolean hasDome;
    private char initWorker;    // '?' if there is no worker

    //set to public, otherwise not testable
    public TileView(int buildingLevel, boolean hasDome, char initWorker) {
        this.buildingLevel = buildingLevel;
        this.hasDome = hasDome;
        this.initWorker = initWorker;
    }

    void setInitWorker(char initWorker) { this.initWorker = initWorker; }

    public char getInitWorker() { return initWorker; }

    public boolean hasDome() {
        return hasDome;
    }

    public boolean hasWorker() {
        return this.initWorker != '?';
    }

    public int getBuildingLevel() {
        return buildingLevel;
    }

}
