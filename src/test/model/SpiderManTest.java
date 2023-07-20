package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpiderManTest {
    private SpiderMan spiderMan1;
    private SpiderMan spiderMan2;
    private SpiderMan spiderMan3;

    @BeforeEach
    void runBefore() {
        spiderMan1 = new SpiderMan("Mary",0,true);
        spiderMan2 = new SpiderMan("Terry",0,false);
        spiderMan3 = new SpiderMan("Shivansh",1,false);
    }

    @Test
    void testGetters() {
        //spiderMan1
        assertEquals("Mary", spiderMan1.getName());
        assertEquals(0, spiderMan1.getUniverseID());
        assertEquals(true, spiderMan1.getStance());
        //spiderMan2
        assertEquals("Terry", spiderMan2.getName());
        assertEquals(0, spiderMan2.getUniverseID());
        assertEquals(false, spiderMan2.getStance());
        //spiderMan3
        assertEquals("Shivansh", spiderMan3.getName());
        assertEquals(1, spiderMan3.getUniverseID());
        assertEquals(false, spiderMan3.getStance());
    }

}
