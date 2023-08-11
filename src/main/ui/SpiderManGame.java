package ui;

import model.SpiderMan;
import model.SpiderVerse;
import model.Universe;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

//Spider Man Game App
public class SpiderManGame {
    private static final String JSON_STORE = "./data/spiderVerse.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Scanner input;
    private SpiderVerse spiderVerse;
    private Set<String> names;

    // EFFECTS: runs the spider-man game application
    public SpiderManGame() throws FileNotFoundException {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runSpiderMan();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runSpiderMan() {
        boolean keepGoing = true;
        String command = null;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // EFFECTS: displays top-level menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> create new spider hero!");
        System.out.println("\t2 -> view the list of all spider heroes in a specific universe");
        System.out.println("\t3 -> view the list of all spider heroes in all universes");
        System.out.println("\t4 -> view the list of supporters in all universes");
        System.out.println("\t5 -> view the list of opponents in all universes");
        System.out.println("\t6 -> view the list of safe universes");
        System.out.println("\t7 -> view the list of collapsed universes");
        System.out.println("\t8 -> view the list of all universes");
        System.out.println("\t9 -> save created spider hero(es) to file");
        System.out.println("\t10 -> load created spider hero(es) from file");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes top-level user command
    private void processCommand(String command) {
        if (command.equals("1")) {
            doCreateCharacter();
        } else if (command.equals("2")) {
            doViewSpiderManOne();
        } else if (command.equals("3")) {
            doViewSpiderMan();
        } else if (command.equals("4")) {
            doViewSupporter();
        } else if (command.equals("5")) {
            doViewOpponent();
        } else if (command.equals("6")) {
            doViewSafeUniverses();
        } else if (command.equals("7")) {
            doViewCollapsedUniverses();
        } else if (command.equals("8")) {
            doViewAllUniverses();
        } else if (command.equals("9")) {
            saveSpiderVerse();
        } else if (command.equals("10")) {
            loadSpiderVerse();
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: loads spider-verse from file, add character names to the names list in case duplicate
    private void loadSpiderVerse() {
        try {
            spiderVerse = jsonReader.read();
            System.out.println("Loaded " + spiderVerse.getName() + " from " + JSON_STORE);
            for (SpiderMan spider : spiderVerse.getAllSpiderMen()) {
                this.names.add(spider.getName());
            }
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: saves the spider-verse to file
    private void saveSpiderVerse() {
        try {
            jsonWriter.open();
            jsonWriter.write(spiderVerse);
            jsonWriter.close();
            System.out.println("Saved " + spiderVerse.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all universes
    private void doViewAllUniverses() {
        System.out.println("\nAll existing universes:");
        for (Universe universe : spiderVerse.getAllUniverses()) {
            String status = universe.revealResult() ? " (safe)" : " (collapsed)";
            System.out.println("\nUniverse-" + universe.getUniverseID() + status);
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all collapsed universes
    private void doViewCollapsedUniverses() {
        System.out.println("\nUniverses that are collapsed:");
        for (Universe universe : spiderVerse.getCollapsedUniverses()) {
            System.out.println("\nUniverse-" + universe.getUniverseID());
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of safe universes
    private void doViewSafeUniverses() {
        System.out.println("\nUniverses that are safe:");
        for (Universe universe : spiderVerse.getSafeUniverses()) {
            System.out.println("\nUniverse-" + universe.getUniverseID());
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all opponent spider heroes
    private void doViewOpponent() {
        System.out.println("\nSpider heroes that are opponents in all Universes:");
        for (SpiderMan spiderMan : spiderVerse.getOpponent()) {
            System.out.println("\n" + spiderMan.getName() + " in Universe-" + spiderMan.getUniverseID());
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all supporter spider heroes
    private void doViewSupporter() {
        System.out.println("\nSpider heroes that are supporters in all Universes:");
        for (SpiderMan spiderMan : spiderVerse.getSupporter()) {
            System.out.println("\n" + spiderMan.getName() + " in Universe-" + spiderMan.getUniverseID());
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all spider heroes
    private void doViewSpiderMan() {
        System.out.println("\nSpider heroes in all Universes:");
        for (SpiderMan spiderMan : spiderVerse.getAllSpiderMen()) {
            String stance = spiderMan.getStance() ? "supporter" : "opponent";
            int universeID = spiderMan.getUniverseID();
            System.out.println("\n" + spiderMan.getName() + "'s stance: " + stance + ", in universe: " + universeID);
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all spider heroes in a specific universe
    private void doViewSpiderManOne() {
        Universe universe;
        int universeID;
        System.out.println("\nInput universeID: (non-negative integer)");
        universeID = input.nextInt();
        universe = checkUniverse(universeID);
        if (universe != null) {
            System.out.println("\nSpider heroes in Universe-" + universeID);
            for (SpiderMan spiderMan : universe.getSpiderMen()) {
                String stance = spiderMan.getStance() ? "supporter" : "opponent";
                System.out.println("\n" + spiderMan.getName() + "'s stance:" + stance);
            }
        }
    }

    // EFFECTS: check if the universe with universeID exists.
    //          if exists, returns the Universe object, otherwise return null
    private Universe checkUniverse(int universeID) {
        Universe currentUniverse = null;
        for (Universe universe : spiderVerse.getAllUniverses()) {
            if (universe.getUniverseID() == universeID) {
                currentUniverse = universe;
                break;
            }
        }
        if (currentUniverse != null) {
            return currentUniverse;
        } else {
            System.out.println("\nUniverse-" + universeID + " doesn't have any spider heroes yet!");
            return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: create your new spider hero character with given name
    private void doCreateCharacter() {
        String name;
        int universeID;
        boolean stance;
        System.out.println("\nCreate a name");
        name = input.next();
        checkName(name);
        this.names.add(name);
        universeID = checkUniverseID();
        stance = checkStance();
        Universe universe = spiderVerse.addCharacter(name,universeID,stance);
        spiderVerse.sortUniverse(universe);
        System.out.println("\nDone! Successfully created a new character!");
    }

    //MODIFIES: this
    //EFFECTS: check if the input character's name is already exists or not, if already exists, prompt
    //         the user to input another one
    private void checkName(String name) {
        while (this.names.contains(name)) {
            System.out.println("Name already exists, please choose another one!");
            name = input.next();
        }
    }

    //MODIFIES: this
    //EFFECTS: check if the input stance is valid or no, has to be either 0 or 1. If out of range, prompt
    //         the user to input again
    private boolean checkStance() {
        boolean stance;
        try {
            System.out.println("\nChoose whether to be supporter or opponent of the canon event rule!");
            System.out.println("\n1 for supporter, 0 for opponent!");
            int numStance = input.nextInt();
            while (numStance != 1 && numStance != 0) {
                System.out.println("\nInput not valid, 1 for supporter, 0 for opponent!");
                numStance = input.nextInt();
            }
            return numStance == 1;
        } catch (InputMismatchException e) {
            System.out.println("\nInput type invalid!");
            input.next();
            stance = checkStance();
        }
        return stance;
    }

    //MODIFIES: this
    //EFFECTS: check if the input universe ID is valid or no, has to be non-negative integer. If not valid, prompt
    //         the user to input again
    private int checkUniverseID() {
        int universeID;
        try {
            System.out.println("\nInput a universeID you want to live! (non-negative integer)");
            universeID = input.nextInt();
            while (universeID < 0) {
                System.out.println("\nInput not valid, choose a non-negative integer!");
                universeID = input.nextInt();
            }
            return universeID;
        } catch (InputMismatchException e) {
            System.out.println("\nInput type invalid!");
            input.next();
            universeID = checkUniverseID();
        }
        return universeID;
    }

    // MODIFIES: this
    // EFFECTS: initializes spider-verse
    private void init() {
        spiderVerse = new SpiderVerse("Danni's spider-verse");
        names = new HashSet<>();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }
}



