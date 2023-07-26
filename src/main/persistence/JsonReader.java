package persistence;

import model.SpiderVerse;
import model.Universe;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

// Represents a reader that reads spider-verse from JSON data stored in file
//reference:https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads spider-verse from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SpiderVerse read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSpiderVerse(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses spider-verse from JSON object and returns it
    private SpiderVerse parseSpiderVerse(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        SpiderVerse sv = new SpiderVerse(name);
        addUniverses(sv, jsonObject);
        return sv;
    }

    // MODIFIES: sv
    // EFFECTS: parses universes from JSON object and adds them to spider-verse
    private void addUniverses(SpiderVerse sv, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("universes");
        for (Object json : jsonArray) {
            JSONObject nextUniverse = (JSONObject) json;
            addUniverse(sv, nextUniverse);
        }
    }

    // MODIFIES: sv
    // EFFECTS: parses universe with spider heroes from JSON object and adds them to spider-verse
    private void addUniverse(SpiderVerse sv, JSONObject jsonObject) {
        int universeID = jsonObject.getInt("Universe ID");
        JSONArray jsonArray = jsonObject.getJSONArray("spider hero(es)");
        for (Object json : jsonArray) {
            JSONObject nextSpider = (JSONObject) json;
            addSpider(universeID, sv, nextSpider);
        }
    }

    // MODIFIES: sv
    // EFFECTS: parses spider hero from JSON object and adds it to workroom
    private void addSpider(int universeID, SpiderVerse sv, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        boolean stance = jsonObject.getString("stance").equals("supporter");
        Universe universe = sv.addCharacter(name, universeID, stance);
        sv.sortUniverse(universe);
    }
}
