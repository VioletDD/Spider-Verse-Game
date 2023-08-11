package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniverseTest {
    private SpiderMan spiderMan1;
    private SpiderMan spiderMan2;
    private SpiderMan spiderMan3;
    private SpiderMan spiderMan4;

    private Universe universe1;
    private Universe universe2;
    private Universe universe3;

    @BeforeEach
    void runBefore() {
        spiderMan1 = new SpiderMan("Mary",0,true);
        spiderMan2 = new SpiderMan("Terry",0,false);
        spiderMan3 = new SpiderMan("Shivansh",1,false);
        spiderMan4 = new SpiderMan("Danni",2,true);

        universe1 = new Universe(0);
        universe2 = new Universe(1);
        universe3 = new Universe(2);
    }

    @Test
    void testConstructor() {
        assertEquals(0, universe1.getUniverseID());
        assertEquals(1, universe2.getUniverseID());
        assertEquals(2, universe3.getUniverseID());

        assertEquals(true, universe1.revealResult());
        assertEquals(0, universe1.getNumSupporter());
        assertEquals(0, universe1.getNumOpponent());
        assertEquals(0, universe1.getSpiderMen().size());
    }

    @Test
    void testAddSpiderMan() {
        universe1.addSpiderMan(spiderMan1);
        universe1.addSpiderMan(spiderMan2);
        universe2.addSpiderMan(spiderMan3);
        universe3.addSpiderMan(spiderMan4);
        //universe1
        assertEquals(2, universe1.getSpiderMen().size());
        assertTrue(universe1.getSpiderMen().contains(spiderMan1));
        assertTrue(universe1.getSpiderMen().contains(spiderMan2));
        assertFalse(universe1.getSpiderMen().contains(spiderMan3));
        assertFalse(universe1.getSpiderMen().contains(spiderMan4));
        //universe2
        assertEquals(1, universe2.getSpiderMen().size());
        assertFalse(universe2.getSpiderMen().contains(spiderMan1));
        assertFalse(universe2.getSpiderMen().contains(spiderMan2));
        assertTrue(universe2.getSpiderMen().contains(spiderMan3));
        assertFalse(universe2.getSpiderMen().contains(spiderMan4));
        //universe3
        assertEquals(1, universe3.getSpiderMen().size());
        assertFalse(universe3.getSpiderMen().contains(spiderMan1));
        assertFalse(universe3.getSpiderMen().contains(spiderMan2));
        assertFalse(universe3.getSpiderMen().contains(spiderMan3));
        assertTrue(universe3.getSpiderMen().contains(spiderMan4));
    }

    @Test
    void testGetNumSupporterOpponent() {
        universe1.addSpiderMan(spiderMan1);
        universe1.addSpiderMan(spiderMan2);
        universe2.addSpiderMan(spiderMan3);
        universe3.addSpiderMan(spiderMan4);

        assertEquals(1, universe1.getNumSupporter());
        assertEquals(1, universe1.getNumOpponent());
        assertEquals(0, universe2.getNumSupporter());
        assertEquals(1, universe2.getNumOpponent());
        assertEquals(1, universe3.getNumSupporter());
        assertEquals(0, universe3.getNumOpponent());
    }

    @Test
    void testRevealResult() {
        universe1.addSpiderMan(spiderMan1);
        universe1.addSpiderMan(spiderMan2);
        universe2.addSpiderMan(spiderMan3);
        universe3.addSpiderMan(spiderMan4);

        assertTrue(universe1.revealResult());
        assertTrue(universe2.revealResult());
        assertTrue(universe3.revealResult());
    }

    @Test
    void testRevealResultCollapsed() {
        spiderMan1 = new SpiderMan("Mary",0,true);
        spiderMan2 = new SpiderMan("Terry",0,true);
        spiderMan3 = new SpiderMan("Shivansh",0,false);

        universe1.addSpiderMan(spiderMan1);
        universe1.addSpiderMan(spiderMan2);
        universe1.addSpiderMan(spiderMan3);

        assertFalse(universe1.revealResult());

        spiderMan4 = new SpiderMan("Jack",0,false);
        universe1.addSpiderMan(spiderMan4);
        assertFalse(universe1.revealResult());
    }

    @Test
    void testRevealResultSafe() {
        spiderMan1 = new SpiderMan("Mary",0,false);
        spiderMan2 = new SpiderMan("Terry",0,false);
        spiderMan3 = new SpiderMan("Shivansh",0,true);

        universe1.addSpiderMan(spiderMan1);
        universe1.addSpiderMan(spiderMan2);
        universe1.addSpiderMan(spiderMan3);

        assertTrue(universe1.revealResult());

        spiderMan4 = new SpiderMan("Jack",0,true);
        SpiderMan spiderMan5 = new SpiderMan("Tom",0,true);
        universe1.addSpiderMan(spiderMan4);
        universe1.addSpiderMan(spiderMan5);
        assertFalse(universe1.revealResult());
    }
}
