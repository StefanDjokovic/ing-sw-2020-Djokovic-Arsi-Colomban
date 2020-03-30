package it.polimi.ingsw;

import it.polimi.ingsw.model.board.Board;
import it.polimi.ingsw.model.board.NonExistingTileException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void boardInitializationTest() {
        Board board = new Board();

        for (int y = 0 ; y < 5 ; y++) {
            for (int x = 0 ; x < 5 ; x++) {
                try {
                    assertEquals(0, board.getTile(x, y).getBuildingLevel());
                } catch(NonExistingTileException e) {
                    System.out.println(x+" "+y);
                    fail("There should be a tile here!");
                }
            }
        }
    }
}
