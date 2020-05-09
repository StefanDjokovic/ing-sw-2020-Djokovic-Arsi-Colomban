package it.polimi.ingsw;
import it.polimi.ingsw.server.model.TileView;
import org.junit.Test;
import static org.junit.Assert.*;

public class TileViewTest {
    @Test
    public void genericTest() {
        TileView tv = new TileView(0, false, 't');
        assertEquals('t', tv.getInitWorker());
        assertEquals(0, tv.getBuildingLevel());
        assertFalse(tv.hasDome());
    }
}
