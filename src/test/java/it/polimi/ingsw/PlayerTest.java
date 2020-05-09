package it.polimi.ingsw;

import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.Tile;
import it.polimi.ingsw.server.model.logger.Logger;
import it.polimi.ingsw.server.model.player.Player;
import org.junit.Test;
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
}
