package persistence;

import model.SpiderMan;
import model.SpiderVerse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

//reference:https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest extends JsonTest{
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            SpiderVerse sv = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySpiderVerse.json");
        try {
            SpiderVerse sv = reader.read();
            assertEquals("My spider-verse", sv.getName());
            assertEquals(0, sv.getNumSpiderMen());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSpiderVerse.json");
        try {
            SpiderVerse sv = reader.read();
            assertEquals("My spider-verse", sv.getName());
            List<SpiderMan> spiders = sv.getAllSpiderMen();
            assertEquals(4, spiders.size());
            assertTrue(checkSpider("Mary",0,true, spiders));
            assertTrue(checkSpider("Terry",0,false, spiders));
            assertTrue(checkSpider("Shivansh",1,false, spiders));
            assertTrue(checkSpider("Danni",2,true, spiders));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
