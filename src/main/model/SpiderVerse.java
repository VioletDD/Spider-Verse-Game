package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//represents the whole multi-universe world, include both universes and spider-men
public class SpiderVerse {
    private Set<Universe> allUniverses;    // list of all universes
    private Set<Universe> safe;            // list of universes that have safe ending
    private Set<Universe> collapsed;       // list of universes that have collapsed ending

    private List<SpiderMan> allSpiderMen;  // list of all spider-men
    private List<SpiderMan> supporter;     // list of spider-men that are supporter for "canon event"
    private List<SpiderMan> opponent;      // list of spider-men that are opponent for "canon event"

    /*
     * EFFECTS: collects all spider-men and universes information into the multi-universe world
     */
    public SpiderVerse() {
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
        } else {
            this.collapsed.add(universe);
        }
    }

    public List<SpiderMan> getAllSpiderMen() {
        return this.allSpiderMen;
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
}
