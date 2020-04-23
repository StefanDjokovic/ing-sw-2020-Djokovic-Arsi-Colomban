package it.polimi.ingsw;

import it.polimi.ingsw.model.board.*;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.Worker;
import org.junit.Test;
import static org.junit.Assert.*;

//import java.util.ArrayList;
import java.util.List;


//import static org.junit.jupiter.api.Assertions.*;

public class TileTest {

    @Test
    public void buildLevelsBoundaries() {

        Tile tile = new Tile(0,0,new Board());

        assertEquals(0, tile.getBuildingLevel());
        assertFalse(tile.hasDome());
        assertTrue(tile.isWalkable());
        assertTrue(tile.isBuildable());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        //1
        try { tile.buildUp(); }
        catch (NotBuildableException e) { System.out.println("Could not build here!"); }

        assertEquals(1, tile.getBuildingLevel());
        assertFalse(tile.hasDome());
        assertTrue(tile.isWalkable());
        assertTrue(tile.isBuildable());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        //2
        try { tile.buildUp(); }
        catch (NotBuildableException e) { System.out.println("Could not build here!"); }

        assertEquals(2, tile.getBuildingLevel());
        assertFalse(tile.hasDome());
        assertTrue(tile.isWalkable());
        assertTrue(tile.isBuildable());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        //3
        try { tile.buildUp(); }
        catch (NotBuildableException e) { System.out.println("Could not build here!"); }

        assertEquals(3, tile.getBuildingLevel());
        assertFalse(tile.hasDome());
        assertTrue(tile.isWalkable());
        assertTrue(tile.isBuildable());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        //4
        try { tile.buildUp(); }
        catch (NotBuildableException e) { System.out.println("Could not build here!"); }

        assertEquals(3, tile.getBuildingLevel());
        assertTrue(tile.hasDome());
        assertFalse(tile.isWalkable());
        assertFalse(tile.isBuildable());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        //5
        try { tile.buildUp(); }
        catch (NotBuildableException e) { System.out.println("Could not build here!"); }

        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        if (tile.getBuildingLevel()>3) { fail("Upper boundaries broken."); }
        if (!tile.hasDome()) { fail("Dome not set."); }
        if (tile.isBuildable()) { fail("Should not be buildable."); }
        if (tile.isWalkable()) { fail("Should not be Walkable."); }

        Tile tile2 = new Tile(0,0,new Board());
        try { tile2.setBuildingLevel(10); }
        catch(NotBuildableException e) { System.out.println("Could not build here!"); }
        if(tile2.getBuildingLevel() == 10){ fail("Build gone wrong!"); }
    }

    @Test
    public void tileInititializationTest() {
        int a = 2;
        int b = 2;
        Board board = new Board();
        Tile tile = new Tile(a, b, board);

        assertEquals(0, tile.getBuildingLevel());
        assertFalse(tile.hasDome());
        assertFalse(tile.hasWorker());
        assertTrue(tile.isBuildable());
        assertTrue(tile.isWalkable());
        assertEquals(board, tile.getBoard());
    }

    @Test
    public void getNeighborsTest() {
        Board board = new Board();

        try { assertEquals(3, board.getTile(0, 0).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }

        try { assertEquals(5, board.getTile(0, 2).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }

        try { assertEquals(8, board.getTile(2, 2).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }

        try { assertEquals(3, board.getTile(0, 4).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }

        try { assertEquals(3, board.getTile(4, 0).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }

        try { assertEquals(3, board.getTile(4, 4).getNeighbors().size()); }
        catch(NonExistingTileException e) { fail(); }
    }

    @Test
    public void deleteWorkerTest() {
        Tile tile = new Tile(0,0, new Board());

        assertFalse(tile.hasWorker());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());

        Player p = new Player("abc", 'a');
        Worker w = new Worker(p, tile);
        tile.setWorker(w);
        //tile.setWorkerOwner(p);

        assertTrue(tile.hasWorker());
        //ssertSame(p, tile.getWorkerOwner());
        assertSame(w, tile.getWorker());

        tile.deleteWorkerAndOwner();

        assertFalse(tile.hasWorker());
        assertNull(tile.getWorker());
        //assertNull(tile.getWorkerOwner());
    }

    @Test
    public void getWalkableNeighborsTest() {
        try { assertEquals(3, new Board().getTile(0,0).getWalkableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoWalkableTilesException e) { fail(); }

        try { assertEquals(5, new Board().getTile(0,2).getWalkableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoWalkableTilesException e) { fail(); }

        try { assertEquals(8, new Board().getTile(2,2).getWalkableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoWalkableTilesException e) { fail(); }


        Board board = new Board();
        try {
            board.getTile(0, 1).buildUp().buildUp().buildUp().buildUp();
        } catch (NotBuildableException | NonExistingTileException e) {
            fail();
        }
        try {
            board.getTile(1, 1).buildUp().buildUp().buildUp().buildUp();
        } catch (NotBuildableException | NonExistingTileException e) {
            fail();
        }
        try {
            Player p = new Player("test", 'a');
            board.getTile(1, 0).setWorker(new Worker(p, board.getTile(1, 0)));
            //board.getTile(1, 0).setWorkerOwner(p);
        } catch (NonExistingTileException e) {
            fail();
        }

        try {
            List<Tile> l = board.getTile(0, 0).getWalkableNeighbors();
        } catch (NoWalkableTilesException | NonExistingTileException e) {
            return;
        }
        fail();
    }

    @Test
    public void getBuildableNeighborsTest() {
        try { assertEquals(3, new Board().getTile(0,0).getBuildableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoBuildableTilesException e) { fail(); }

        try { assertEquals(5, new Board().getTile(0,2).getBuildableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoBuildableTilesException e) { fail(); }

        try { assertEquals(8, new Board().getTile(2,2).getBuildableNeighbors().stream().map(x -> 1).reduce(0, Integer::sum), 0); }
        catch(NonExistingTileException | NoBuildableTilesException e) { fail(); }


        Board board = new Board();
        try {
            board.getTile(0, 1).buildUp().buildUp().buildUp().buildUp();
        } catch (NotBuildableException | NonExistingTileException e) {
            fail();
        }
        try {
            board.getTile(1, 1).buildUp().buildUp().buildUp().buildUp();
        } catch (NotBuildableException | NonExistingTileException e) {
            fail();
        }
        try {
            Player p = new Player("test", 'a');
            board.getTile(1, 0).setWorker(new Worker(p, board.getTile(1, 0)));
            //board.getTile(1, 0).setWorkerOwner(p);
        } catch (NonExistingTileException e) {
            fail();
        }

        try {
            List<Tile> l = board.getTile(0, 0).getBuildableNeighbors();
        } catch (NoBuildableTilesException | NonExistingTileException e) {
            return;
        }
        fail();
    }

    @Test
    public void isWalkableTest() {
        Tile tile = new Tile(2, 2, new Board());
        assertTrue(tile.isWalkable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isWalkable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isWalkable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isWalkable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertFalse(tile.isWalkable());
    }

    @Test
    public void isBuildableTest() {
        Tile tile = new Tile(2, 2, new Board());
        assertTrue(tile.isBuildable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isBuildable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isBuildable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertTrue(tile.isBuildable());
        try { tile.buildUp(); } catch(NotBuildableException e) { System.out.println("Could not build."); }
        assertFalse(tile.isBuildable());
    }

    @Test
    public void getterSetterTest() {
        Board board = new Board();
        for (int x = 0 ; x < 5 ; x++) {
            for (int y = 0 ; y < 5 ; y++) {
                try {
                    assertEquals(x, board.getTile(x, y).getX());
                    assertEquals(y, board.getTile(x, y).getY());
                } catch (NonExistingTileException e) {
                    fail();
                }
            }
        }
    }

    @Test
    public void setDomeTest() {
        Board board = new Board();
        try {
            board.getTile(0, 0).setDome(true);

            assertTrue(board.getTile(0, 0).hasDome());
            assertFalse(board.getTile(0, 0).isWalkable());
            assertFalse(board.getTile(0, 0).isBuildable());
        } catch (NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void getNeighborsOptimizedTest() {
        Board board = new Board();
        try {
            Tile tile = board.getTile(0, 0);
            List<Tile> list1 = tile.getNeighbors();
            List<Tile> list2 = tile.getNeighborsOptimized();
            for (int x = 0 ; x < list1.size() ; x++) {
                assertEquals(list1.get(x), list2.get(x));
            }
        } catch (NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void toStringTest() {
        Board board = new Board();
        try {
            assertEquals("0 0", board.getTile(0, 0).toString());
        } catch (NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void setBuildingLevelTest() {
        Board board = new Board();
        String res = "";
        try {
            board.getTile(0, 0).setBuildingLevel(10);
        } catch (NotBuildableException e) {
            res = "1";
        } catch (NonExistingTileException e) {
            fail();
        }
        assertEquals("1", res);

        try {
            board.getTile(0, 0).setBuildingLevel(3);
            assertEquals(3, board.getTile(0, 0).getBuildingLevel());
        } catch (NotBuildableException | NonExistingTileException e) {
            fail();
        }
    }

    @Test
    public void isMovableToTest() {
        Board board = new Board();
        Tile tile1;
        Tile tile2;
        try {
            tile1 = board.getTile(0, 0);
            tile2 = board.getTile(0, 1);
            assertTrue(tile1.isMovableTo(tile1, tile2, 1, 99));
            tile2.buildUp().buildUp().buildUp().buildUp();
            assertFalse(tile1.isMovableTo(tile1, tile2, 1, 99));
        } catch (NonExistingTileException | NotBuildableException e) {
            fail();
        }
    }

    @Test
    public void getMovableToNeighTest() {
        Board board = new Board();
        Tile tile1;
        try {
            tile1 = board.getTile(0, 0);
            assertEquals(3, tile1.getMovableToNeigh(1, 3).size());
            board.getTile(0, 1).buildUp().buildUp().buildUp().buildUp();
            assertEquals(2, tile1.getMovableToNeigh(1, 3).size());
            board.getTile(1, 1).buildUp().buildUp().buildUp().buildUp();
            board.getTile(1, 0).setWorker(new Worker(new Player("ramses", 'r'), board.getTile(1, 0)));
            assertEquals(0, tile1.getMovableToNeigh(1, 3).size());
        } catch (NonExistingTileException | NotBuildableException e) {
            fail();
        }
    }
}