package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

//represents the universe in multi-universe world with universeID
public class Universe implements Writable {
    private int universeID;                // the ID for the universe
    private boolean ending;                // the ending for the universe, true for safe, false for collapsed
    private int numSupporter;              // the number of supporters in the universe
    private int numOpponent;               // the number of opponents in the universe

    private List<SpiderMan> spiderMen;     // list of spider-men in the universe

    /*
     * REQUIRES: universeID is non-negative
     * EFFECTS: connects to the universe with given universeID, the universe is safe at the beginning
     */
    public Universe(int universeID) {
        this.universeID = universeID;
        this.ending = true;
        this.numOpponent = 0;
        this.numSupporter = 0;
        this.spiderMen = new ArrayList<>();
    }

    /*
     * MODIFIES: this
     * EFFECTS: adds spider-man to the universe, change the counts for supporters or opponents
     */
    public void addSpiderMan(SpiderMan spiderMan) {
        this.spiderMen.add(spiderMan);
        if (spiderMan.getStance()) {
            this.numSupporter++;
        } else {
            this.numOpponent++;
        }
        String stance = spiderMan.getStance() ? "Supporter" : "Opponent";
        String string = "\tCreated a new character: " + spiderMan.getName()
                + ", Universe ID: " + spiderMan.getUniverseID()
                + ", Stance: " + stance;
        EventLog.getInstance().logEvent(new Event(string));
    }

    public int getUniverseID() {
        return this.universeID;
    }

    public int getNumSupporter() {
        return this.numSupporter;
    }

    public int getNumOpponent() {
        return this.numOpponent;
    }

    public List<SpiderMan> getSpiderMen() {
        return this.spiderMen;
    }

    /*
     * MODIFIES: this
     * EFFECTS: reveals the universe's ending, safe or collapsed, if there are
     * 2 or more spider heroes and number of supporters > number of opponents, the universe will be collapsed
     */
    public boolean revealResult() {
        if (!this.ending) {
            //do nothing
        } else if (this.spiderMen.size() >= 3 && this.numOpponent < this.numSupporter) {
            this.ending = false;
            String string = "\tUniverse - " + this.universeID + " is now collapsed!";
            EventLog.getInstance().logEvent(new Event(string));
        } else {
            this.ending = true;
        }

        return this.ending;
    }

    // EFFECTS: returns this as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Universe ID", this.universeID);
        json.put("spider hero(es)", spidersToJson());
        return json;
    }

    // EFFECTS: returns all spider heroes in this universe as a JSON array
    private JSONArray spidersToJson() {
        JSONArray jsonArray = new JSONArray();

        for (SpiderMan spider : this.spiderMen) {
            jsonArray.put(spider.toJson());
        }
        return jsonArray;
    }
}
