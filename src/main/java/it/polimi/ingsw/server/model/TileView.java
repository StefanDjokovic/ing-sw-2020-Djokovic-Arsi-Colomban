package it.polimi.ingsw.server.model;

public class TileView {

    private int buildingLevel;
    private boolean hasDome;
    private char initWorker;

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

    public int getBuildingLevel() {
        return buildingLevel;
    }

}
