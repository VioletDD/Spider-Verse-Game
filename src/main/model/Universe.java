package model;

import java.util.ArrayList;
import java.util.List;

//represents the universe in multi-universe world with universeID
public class Universe {
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
     * EFFECTS: adds spider-man to the universe, change the counts for supporters or opponents
     */
    public void addSpiderMan(SpiderMan spiderMan) {
        this.spiderMen.add(spiderMan);
        if (spiderMan.getStance()) {
            this.numSupporter++;
        } else {
            this.numOpponent++;
        }
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
     * EFFECTS: reveals the universe's ending
     */
    public boolean revealResult() {
        //to be edited later
        //if more numOpponents, the universe is safe
        if (this.numOpponent >= this.numSupporter) {
            this.ending = true;
        } else {
            this.ending = false;
        }
        return this.ending;
    }
}
