package it.polimi.ingsw;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import it.polimi.ingsw.model.board.Tile;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void genericTest() {
        Player p = new Player("test", 't');
        Tile t = new Tile(0,0, new Board());
        Worker worker = new Worker(p, t);
        assertEquals("0 0",worker.toString());
        assertEquals(t, worker.getPosTile());
        assertEquals(p, worker.getOwner());
        worker.delete();
        assertNull(t.getWorker());
    }

    @Test
    public void positionTest() {
        Player p = new Player("test", 't');
        /*Worker worker = new Worker(p);
        assertThrows(0, worker.getPosX());
        assertThrows(0, worker.getPosY());*/

        Tile t = new Tile(1,2, new Board());
        Worker worker2 = new Worker(p, t);
        assertEquals(1, worker2.getPosX());
        assertEquals(2, worker2.getPosY());

        Board board = new Board();
        try {
            Worker worker3 = new Worker(new Player("test",'t'), board.getTile(1,1));
            assertEquals(worker3, board.getTile(1,1).getWorker());
            worker3.changePosition(board.getTile(2,2));
            assertEquals(worker3, board.getTile(2,2).getWorker());
        } catch (NonExistingTileException e) {
            e.printStackTrace();
        }
    }
}
