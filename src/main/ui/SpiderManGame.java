package ui;

import model.SpiderMan;
import model.SpiderVerse;
import model.Universe;

import java.util.Scanner;

//Spider Man Game App
public class SpiderManGame {
    private Scanner input;
    private SpiderVerse spiderVerse;
    private Universe universe;
    private Universe universe1;
    private Universe universe2;
    private Universe universe3;
    private Universe universe4;

    // EFFECTS: runs the spider-man game application
    public SpiderManGame() {
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
        } else {
            System.out.println("Selection not valid...");
        }
    }

    // MODIFIES: this
    // EFFECTS: view the list of all universes
    private void doViewAllUniverses() {
        System.out.println("\nAll existing universes:");
        for (Universe universe : spiderVerse.getAllUniverses()) {
            System.out.println("\nUniverse-" + universe.getUniverseID());
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
        System.out.println("\nInput a universeID you want to live!");
        universeID = input.nextInt();
        System.out.println("\nChoose whether to be supporter or opponent of the canon event rule!");
        System.out.println("\n1 for supporter, 0 for opponent!");
        stance = input.nextInt() == 1;
        universe = spiderVerse.addCharacter(name,universeID,stance);
        spiderVerse.sortUniverse(universe);
        System.out.println("\nDone! Successfully created a new character!");
    }

    // MODIFIES: this
    // EFFECTS: initializes spider-verse with several characters
    private void init() {
        spiderVerse = new SpiderVerse();
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        universe1 = spiderVerse.addCharacter("Mary",0,true);
        universe2 = spiderVerse.addCharacter("Terry",0,false);
        universe3 = spiderVerse.addCharacter("Shivansh",1,false);
        universe4 = spiderVerse.addCharacter("Danni",2,true);

        spiderVerse.sortUniverse(universe1);
        spiderVerse.sortUniverse(universe2);
        spiderVerse.sortUniverse(universe3);
        spiderVerse.sortUniverse(universe4);
    }
}



