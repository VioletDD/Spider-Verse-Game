package persistence;

import model.SpiderMan;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//reference: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonTest {
    protected boolean checkSpider(String name, int universeID, boolean stance, List<SpiderMan> spiders) {
        boolean found = false;
        for (SpiderMan spider : spiders) {
            if (spider.getName().equals(name)) {
                found = true;
                assertEquals(universeID, spider.getUniverseID());
                assertEquals(stance, spider.getStance());
            }
        }
        return found;
    }
}
