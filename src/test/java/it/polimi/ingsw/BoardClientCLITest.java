package it.polimi.ingsw;
import it.polimi.ingsw.server.model.BoardView;
import it.polimi.ingsw.server.model.board.Board;
import it.polimi.ingsw.server.model.board.NonExistingTileException;
import it.polimi.ingsw.server.model.board.NotBuildableException;
import it.polimi.ingsw.server.model.player.Player;
import it.polimi.ingsw.server.model.player.Worker;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardClientCLITest {
    @Test
        public void genericTest() {

        BoardView bv1 = new BoardView();
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                assertEquals(0, bv1.getBoardView()[x][y].getBuildingLevel());
            }
        }

        Board board = new Board();
        for(int a = 0 ; a < 5 ; a++) {
            for (int b = 0; b < 5; b++) {
                try {
                    board.getTile(a, b).buildUp();
                    assertEquals(1, board.getTile(a, b).getBuildingLevel());
                } catch (NotBuildableException|NonExistingTileException e) {
                    fail();
                }
            }
        }
        try {
            board.getTile(1, 1).setWorker(new Worker(new Player("test", 't'), board.getTile(1, 1)));
        } catch (NonExistingTileException e) {
            fail();
        }

        BoardView bv2 = new BoardView(board);
        for(int x = 0 ; x < 5 ; x++) {
            for(int y = 0 ; y < 5 ; y++) {
                assertEquals(1, bv2.getBoardView()[x][y].getBuildingLevel());
            }
        }

        assertEquals('t', bv2.getBoardView()[1][1].getInitWorker());
    }
}
