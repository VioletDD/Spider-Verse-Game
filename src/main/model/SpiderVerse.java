package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//represents the whole multi-universe world, include both universes and spider-men
public class SpiderVerse implements Writable {
    private String name;                   // name of this spider-verse
    private Set<Universe> allUniverses;    // list of all universes
    private Set<Universe> safe;            // list of universes that have safe ending
    private Set<Universe> collapsed;       // list of universes that have collapsed ending

    private List<SpiderMan> allSpiderMen;  // list of all spider-men
    private List<SpiderMan> supporter;     // list of spider-men that are supporter for "canon event"
    private List<SpiderMan> opponent;      // list of spider-men that are opponent for "canon event"

    /*
     * EFFECTS: collects all spider-men and universes information into the multi-universe world
     */
    public SpiderVerse(String name) {
        this.name = name;
        //universes
        this.allUniverses = new HashSet<>();
        this.safe = new HashSet<>();
        this.collapsed = new HashSet<>();
        //spider-men
        this.allSpiderMen = new ArrayList<>();
        this.supporter = new ArrayList<>();
        this.opponent = new ArrayList<>();
    }

    /*
     * REQUIRES: name has a non-zero length, universeID is non-negative
     * MODIFIES: this
     * EFFECTS: add new spider-man character to this multi-universe, create the corresponding universe, categorizes
     * spider-man based on stance
     */
    public Universe addCharacter(String name, int universeID, boolean stance) {
        SpiderMan spiderMan = new SpiderMan(name, universeID, stance);
        Universe currentUniverse = null;
        for (Universe universe : this.allUniverses) {
            if (universe.getUniverseID() == universeID) {
                currentUniverse = universe;
                break;
            }
        }
        if (currentUniverse == null) {
            currentUniverse = new Universe(universeID);
            this.allUniverses.add(currentUniverse);
        }
        currentUniverse.addSpiderMan(spiderMan);
        this.allSpiderMen.add(spiderMan);
        if (spiderMan.getStance()) {
            this.supporter.add(spiderMan);
        } else {
            this.opponent.add(spiderMan);
        }
        return currentUniverse;
    }

    //MODIFIES: this
    //EFFECTS: categorizes universe based on ending
    public void sortUniverse(Universe universe) {
        if (universe.revealResult()) {
            this.safe.add(universe);
            this.collapsed.remove(universe);
        } else {
            this.collapsed.add(universe);
            this.safe.remove(universe);
        }
    }

    public List<SpiderMan> getAllSpiderMen() {
        return this.allSpiderMen;
    }

    public int getNumSpiderMen() {
        return this.allSpiderMen.size();
    }

    public List<SpiderMan> getSupporter() {
        return this.supporter;
    }

    public List<SpiderMan> getOpponent() {
        return this.opponent;
    }

    public Set<Universe> getAllUniverses() {
        return this.allUniverses;
    }

    public Set<Universe> getSafeUniverses() {
        return this.safe;
    }

    public Set<Universe> getCollapsedUniverses() {
        return this.collapsed;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("universes", universesToJson());
        return json;
    }

    // EFFECTS: returns all universes in this spider-verse as a JSON array
    //          including the spider heroes within each universe
    private JSONArray universesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Universe universe : this.allUniverses) {
            jsonArray.put(universe.toJson());
        }

        return jsonArray;
    }
}
