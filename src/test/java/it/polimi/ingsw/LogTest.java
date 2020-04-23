package it.polimi.ingsw;

import it.polimi.ingsw.model.logger.Log;
import org.junit.Test;
import static org.junit.Assert.*;

public class LogTest {
    @Test
    public void constructorTest() {
        Log log = new Log();
        assertEquals(-1, log.getType());

        Log log2 = new Log('t');
        assertEquals(1, log2.getType());

        Log log3 = new Log(0, 0, 0, 1, 'x');
        assertEquals(0, log3.getType());
    }

    @Test
    public void toStringTest() {
        Log log = new Log();
        assertEquals("Nothing Logged", log.toString());

        Log log2 = new Log('t');
        assertEquals("t skipped", log2.toString());

        Log log3 = new Log(0, 0, 0, 1, 'x');
        assertEquals("x : 0 0 to 0 1", log3.toString());
    }

    @Test
    public void getPlayerInitTest() {
        Log log2 = new Log('t');
        assertEquals('t', log2.getPlayerInit());
    }

    @Test
    public void getActionTest() {
        Log log = new Log(0, 0, 0, 1, 'x');
        assertEquals(0, log.getAction(0));
        assertEquals(0, log.getAction(1));
        assertEquals(0, log.getAction(2));
        assertEquals(1, log.getAction(3));
    }
}
