package model;

import org.json.JSONObject;
import persistence.Writable;

//represents spider-man with name, universeID, stance for the "canon event"
public class SpiderMan implements Writable {
    private String name;                   // the spider-man name
    private int universeID;                // the ID for the universe
    private boolean stance;                // the stance for "canon event", true for supporter, false for opponent

    /*
     * REQUIRES: name has a non-zero length, universeID is non-negative
     * EFFECTS: creates a spider-man character with given name, lives in the universe with given universeID,
     * adds the spider-man to the supporter or opponent team based on given stance
     */
    public SpiderMan(String name, int universeID, boolean stance) {
        this.name = name;
        this.universeID = universeID;
        this.stance = stance;
    }

    public String getName() {
        return this.name;
    }

    public int getUniverseID() {
        return this.universeID;
    }

    public boolean getStance() {
        return this.stance;
    }

    // EFFECTS: returns the spider hero's name and stance as a JSON array
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        json.put("stance", getStance() ? "supporter" : "opponent");
        return json;
    }
}
