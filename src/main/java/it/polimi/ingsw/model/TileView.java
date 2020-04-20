package it.polimi.ingsw.model;

public class TileView {

    private int buildingLevel;
    private boolean hasDome;
    private char initWorker;


    TileView(int buildingLevel, boolean hasDome, char initWorker) {
        this.buildingLevel = buildingLevel;
        this.hasDome = hasDome;
        this.initWorker = initWorker;
    }

    void setInitWorker(char initWorker) { this.initWorker = initWorker; }

    public char getInitWorker() { return initWorker; }

    public boolean hasDome() {
        return hasDome;
    }

    public int getBuildingLevel() {
        return buildingLevel;
    }

}