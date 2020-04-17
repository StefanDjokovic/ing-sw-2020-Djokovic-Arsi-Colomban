package it.polimi.ingsw.model.god.godPowers;

import it.polimi.ingsw.messages.OptionSelection;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.NotBuildableException;
import it.polimi.ingsw.model.god.*;
import it.polimi.ingsw.model.player.Worker;
import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.Tile;


import java.util.ArrayList;
import java.util.Scanner;

import static java.lang.Math.abs;

public class Build extends GodPower {

    public Build(GodLogic godLogic) {
        super(godLogic);
    }

    public void power() {
        System.out.println("test message: Build, Build, Build your boat");
    }

    public void power(int x, int y, Worker worker, Board board) throws NonExistingTileException, NotBuildableException, AlreadyHasDomeException, OccupiedTileException, OutOfReachException {  // Overloaded method, it takes the coordinates as arguments rather than from stdio
        Tile destination = board.getTile(x, y); // Tile the player wants build on
        Tile start = worker.getPosTile(); // Tile where the selected worker is standing
        if (abs(x - worker.getPosX()) <= 1 && abs(y - worker.getPosY()) <= 1) { // The worker can only build on adjacent tiles
            if (!destination.hasWorker()) {  // IN GENERAL, the build can only be completed if the destination has no worker on it
                if (!destination.hasDome()) {    // The worker can only build on tiles that don't already have a dome on them
                    if (destination.getBuildingLevel() < 3) { // If the building is not already 3 blocks tall, the building simply gets one level higher
                        int newLevel = destination.getBuildingLevel() + 1;
                        destination.setBuildingLevel(newLevel);
                    } else { // If the building is already 3 levels tall, the worker builds a dome
                        destination.setDome(true);
                    }
                } else throw new AlreadyHasDomeException("The tile you're trying to build on already has a dome");
            } else
                throw new OccupiedTileException("The destination is already occupied by another worker");  // if the tile is occupied, throws an exception
        } else throw new OutOfReachException("The tile you're trying to build on it's not adjacent to the worker");
    }

    public void power(Board board, int posXFrom, int posYFrom, int posXTo, int posYTo) {
        System.out.println("IM BUILDING");
        try {
            board.getTile(posXTo, posYTo).buildUp();
            System.out.println("Should have built... right?");
        } catch (NonExistingTileException | NotBuildableException e) {
            System.out.println("You failed!");
        }
    }

    @Override
    public OptionSelection getOptions(int lastWorkerUsed) {
        System.out.println("IM SELECTING FOR BUILDING");
        OptionSelection opt = getGodLogic().getOptionsGodLogic(99, 99, false);
        int lastWorkerUsedX = getGodLogic().lastWorkerUsedPosX(lastWorkerUsed);
        int lastWorkerUsedY = getGodLogic().lastWorkerUsedPosY(lastWorkerUsed);
        for (ArrayList<Integer> a: opt.getComb()) {
            if (a.get(0) == lastWorkerUsedX && a.get(1) == lastWorkerUsedY) {
                opt = new OptionSelection();
                opt.setOptions(a);
                break;
            }
        }
        System.out.println("yelp");
        System.out.println(opt);
        return opt;
    }

}
