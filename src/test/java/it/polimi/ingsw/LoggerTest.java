package it.polimi.ingsw;
import it.polimi.ingsw.model.logger.Logger;
import org.junit.Test;
import static org.junit.Assert.*;

public class LoggerTest {
    @Test
    public void genericTest() {
        Logger logger = new Logger();
        assertEquals(-1, logger.getLastLog().getType());
        assertEquals(-1, logger.getSecondToLastLog().getType());

        logger.addNewLog('t');
        assertEquals(1, logger.getLastLog().getType());
        assertEquals(-1, logger.getSecondToLastLog().getType());

        logger.addNewLog(0, 0, 1, 1, 't');
        assertEquals(0, logger.getLastLog().getType());
        assertEquals(1, logger.getSecondToLastLog().getType());
    }

    @Test
    public void toStringTest() {
        Logger logger = new Logger();
        assertEquals("Nothing Logged yet", logger.toString());

        logger.addNewLog('t');
        logger.addNewLog(0, 0, 1, 1, 't');
        assertEquals("Logged:\nt skipped\nt : 0 0 to 1 1\n", logger.toString());
    }
}
