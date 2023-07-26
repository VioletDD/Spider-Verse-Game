package persistence;

import model.SpiderMan;
import model.SpiderVerse;
import model.Universe;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//reference:https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest extends JsonTest{
    @Test
    void testWriterInvalidFile() {
        try {
            SpiderVerse sv = new SpiderVerse("My spider-verse");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            SpiderVerse sv = new SpiderVerse("My spider-verse");
            JsonWriter writer = new JsonWriter("./data/testWriterEmptySpiderVerse.json");
            writer.open();
            writer.write(sv);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptySpiderVerse.json");
            sv = reader.read();
            assertEquals("My spider-verse", sv.getName());
            assertEquals(0, sv.getNumSpiderMen());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            SpiderVerse sv = new SpiderVerse("My spider-verse");
            Universe universe1 = sv.addCharacter("Mary",0,true);
            Universe universe2 = sv.addCharacter("Terry",0,false);
            Universe universe3 = sv.addCharacter("Shivansh",1,false);
            Universe universe4 = sv.addCharacter("Danni",2,true);

            sv.sortUniverse(universe1);
            sv.sortUniverse(universe2);
            sv.sortUniverse(universe3);
            sv.sortUniverse(universe4);

            JsonWriter writer = new JsonWriter("./data/testWriterGeneralSpiderVerse.json");
            writer.open();
            writer.write(sv);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralSpiderVerse.json");
            sv = reader.read();
            assertEquals("My spider-verse", sv.getName());
            List<SpiderMan> spiders = sv.getAllSpiderMen();
            assertEquals(4, spiders.size());
            assertTrue(checkSpider("Mary",0,true, spiders));
            assertTrue(checkSpider("Terry",0,false, spiders));
            assertTrue(checkSpider("Shivansh",1,false, spiders));
            assertTrue(checkSpider("Danni",2,true, spiders));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
