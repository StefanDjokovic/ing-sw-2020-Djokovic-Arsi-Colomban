package it.polimi.ingsw;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.Worker;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void genericTest() {
        Player p = new Player("test", 't');
        assertEquals("test",p.getName());
        assertEquals('t',p.getInitial());

        Board b = new Board();
        p.addWorker(new Tile(1,1, b));
        p.addWorker(new Tile(1,2, b));
        assertEquals(2,p.getWorkersSize());

        assertEquals(2, p.getWorkers().stream().map(x -> 1).reduce(0, Integer::sum), 0);

        p.setInitial('b');
        assertEquals('b',p.getInitial());
    }

    @Test
    public void GodLogicTest() {
        Player p = new Player("test", 't');
        Logger l = new Logger();
        Board b = new Board();
        p.setGodLogic("Build", l, b);
        assertEquals("Build", p.getGodLogic().getGodLogicName());
    }

    @Test
    public void deleteTest() {
        Board b = new Board();
        Player p = new Player("test", 't');
        assertEquals(0, p.getWorkersSize());
        try {
            p.addWorker(b.getTile(0, 0));
            assertEquals(1, p.getWorkersSize());
            p.addWorker(b.getTile(1, 0));
            assertEquals(2, p.getWorkersSize());
            p.delete();
            assertEquals(0, p.getWorkers().stream().map(x -> x.getPosTile().getWorker()==null ? 0 : 1).reduce(0, Integer::sum), 0);
        } catch (NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void nWorkersTest() {
        Board b = new Board();
        Player p = new Player("test", 't');

        assertEquals(0, p.nWorkers());

        try {
            b.getTile(1, 1).setWorker(new Worker(p, b.getTile(1, 1)));
            p.addWorker(b.getTile(1, 1));
        } catch (NonExistingTileException e) {
            fail();
        }

        assertEquals(1, p.nWorkers());
    }

    @Test
    public void getPlayerOptionsTest() {
        Board b = new Board();
        Tile t;
        Player p = new Player("test", 't');

        try {
            b.getTile(1, 1).setWorker(new Worker(p, b.getTile(1, 1)));
            p.addWorker(b.getTile(1, 1));
            ArrayList<ArrayList<Integer>> l = new ArrayList<>();
            ArrayList<Integer> list = new ArrayList<>();
            l.add(list);
            list.add(1);
            list.add(1);
            list.add(0);
            list.add(0);
            list.add(1);
            list.add(0);
            list.add(2);
            list.add(0);
            list.add(2);
            list.add(1);
            list.add(2);
            list.add(2);
            list.add(1);
            list.add(2);
            list.add(0);
            list.add(2);
            list.add(0);
            list.add(1);
            assertEquals(l, p.getOptionsPlayer(1, 0, false, null).getValues());
        } catch (NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void toStringTest() {
        Board b = new Board();
        Tile t;
        Player p = new Player("test", 't');
        p.setGodLogic("Artemis", null, b);
        try {
            b.getTile(1, 1).setWorker(new Worker(p, b.getTile(1, 1)));
            p.addWorker(b.getTile(1, 1));
            b.getTile(3, 3).setWorker(new Worker(p, b.getTile(3, 3)));
            p.addWorker(b.getTile(3, 3));
            String ris = new String("Name: test; Initial: t\nSelected God: Artemis\nWorker 1 at: 1 1\nWorker 2 at: 3 3");

            assertEquals(ris, p.toString());
        } catch (NonExistingTileException e) {
            fail();
        }
    }
}
