package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SpiderVerseTest {
    private SpiderVerse spiderVerse;
    private Universe universe1;
    private Universe universe2;
    private Universe universe3;
    private Universe universe4;


    @BeforeEach
    void runBefore() {
        spiderVerse = new SpiderVerse("Danni's spider-verse");
    }

    @Test
    void testConstructor() {
        assertEquals(0,spiderVerse.getAllUniverses().size());
        assertEquals(0,spiderVerse.getSafeUniverses().size());
        assertEquals(0,spiderVerse.getCollapsedUniverses().size());
        assertEquals(0,spiderVerse.getAllSpiderMen().size());
        assertEquals(0,spiderVerse.getSupporter().size());
        assertEquals(0,spiderVerse.getOpponent().size());
    }

    @Test
    void testAddCharacter() {
        universe1 = spiderVerse.addCharacter("Mary",0,true);
        universe2 = spiderVerse.addCharacter("Terry",0,false);
        universe3 = spiderVerse.addCharacter("Shivansh",1,false);
        universe4 = spiderVerse.addCharacter("Danni",2,true);

        assertEquals(universe1, universe2);
        assertEquals(0, universe1.getUniverseID());
        assertEquals(0, universe2.getUniverseID());
        assertEquals(1, universe3.getUniverseID());
        assertEquals(2, universe4.getUniverseID());

        assertEquals(3, spiderVerse.getAllUniverses().size());
        assertEquals(4, spiderVerse.getAllSpiderMen().size());
        assertEquals(2, spiderVerse.getSupporter().size());
        assertEquals(2, spiderVerse.getOpponent().size());
    }

    @Test
    void testSortUniverse() {
        universe1 = spiderVerse.addCharacter("Mary",0,true);
        universe2 = spiderVerse.addCharacter("Terry",0,true);
        universe3 = spiderVerse.addCharacter("Shivansh",0,false);
        universe4 = spiderVerse.addCharacter("Danni",1,true);

        spiderVerse.sortUniverse(universe1);
        spiderVerse.sortUniverse(universe2);
        spiderVerse.sortUniverse(universe3);
        spiderVerse.sortUniverse(universe4);

        assertEquals(1, spiderVerse.getCollapsedUniverses().size());
        assertEquals(1, spiderVerse.getSafeUniverses().size());
    }
}
