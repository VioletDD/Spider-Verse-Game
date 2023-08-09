package ui;


import model.Event;
import model.EventLog;
import model.SpiderMan;
import model.SpiderVerse;
import model.Universe;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

//GUI application of Spider-Man Game
public class SpiderManGameUI extends JFrame implements WindowListener {
    private static final String JSON_STORE = "./data/spiderVerse.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private SpiderVerse spiderVerse;
    private Universe universe;
    private Set<String> names;

    private JComboBox<String> printCombo;
    private JDesktopPane desktop;
    private JInternalFrame controlPanel;
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private BufferedImage img;

    //EFFECTS: initialize the Spider Game GUI window with functions displayed in buttons
    public SpiderManGameUI()  throws FileNotFoundException {
        init();
        readImage();
        initializeBackground();
        desktop.addMouseListener(new DesktopFocusAction());
        controlPanel = new JInternalFrame("Control Panel", true, false, true, false);
        controlPanel.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Spider-Verse Game");
        setSize(WIDTH, HEIGHT);

        addButtonPanel();

        EventLog.getInstance();

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        addWindowListener(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event next : EventLog.getInstance()) {
            System.out.println(next.toString());
        }
        //A pause so user can see the message before
        //the window actually closes.
        ActionListener task = new ActionListener() {
            boolean alreadyDisposed = false;
            public void actionPerformed(ActionEvent e) {
                if (controlPanel.isDisplayable()) {
                    alreadyDisposed = true;
                    controlPanel.dispose();
                }
            }
        };
        Timer timer = new Timer(500, task); //fire every half second
        timer.setInitialDelay(2000);        //first delay 2 seconds
        timer.setRepeats(false);
        timer.start();
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    //MODIFIES: this
    //EFFECTS: initialize the application background
    private void initializeBackground() {
        desktop = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                grphcs.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(img.getWidth(), img.getHeight());
            }
        };
    }

    //EFFECTS: check if the image link is valid
    private void readImage() {
        try {
            img = ImageIO.read(new URL("https://cdn.vox-cdn.com/thumbor/GuoZxxBBbui_-FPPHeykUPiX0sA=/1400x1050/filters:format(jpeg)/cdn.vox-cdn.com/uploads/chorus_asset/file/24698555/AcrossSpiderVerseReview_Getty_SonyPicturesAnimation_Ringer.jpg"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes spider-verse
    private void init() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        spiderVerse = new SpiderVerse("Danni's spider-verse");
        names = new HashSet<>();
    }

    /**
     * MODIFIES: this
     * EFFECTS: Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }


    /**
     * EFFECTS: Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            SpiderManGameUI.this.requestFocusInWindow();
        }
    }

    /**
     * EFFECTS: Helper to add control buttons.
     */
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0,1));
        buttonPanel.setSize(new Dimension(0, 0));

        buttonPanel.add(new JButton(new AddSpiderHero()));
        buttonPanel.add(new JButton(new SaveSpiderVerse()));
        buttonPanel.add(new JButton(new LoadSpiderVerse()));
        buttonPanel.add(new JButton(new ViewSpiderHero()));
        buttonPanel.add(new JButton(new ViewSpiderHeroes()));
        buttonPanel.add(new JButton(new ViewSupporters()));
        buttonPanel.add(new JButton(new ViewOpponents()));
        buttonPanel.add(new JButton(new ViewSafeUniverse()));
        buttonPanel.add(new JButton(new ViewCollapsedUniverse()));
        buttonPanel.add(new JButton(new ViewAllUniverses()));

        controlPanel.add(buttonPanel, BorderLayout.CENTER);
    }

    /**
     * MODIFIES: this
     * EFFECTS: Represents the action to be taken when the user wants to add a new
     * spider hero character to the Spider-Verse.
     */
    private class AddSpiderHero extends AbstractAction {
        Set<String> names = SpiderManGameUI.this.names;

        AddSpiderHero() {
            super("Create a new character");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JTextField name = new JTextField(5);
            JTextField universeID = new JTextField(5);
            JTextField stance = new JTextField(5);
            Object[] message = {
                    "Input the spider hero's name:\n", name,
                    "Input a universeID you want to live! (non-negative integer):\n", universeID,
                    "Choose to be supporter or opponent of the canon event rule! (1 for supporter, 0 for opponent):\n",
                    stance
            };
            int option = JOptionPane.showConfirmDialog(null, message, "Create a New Spider Hero",
                    JOptionPane.OK_CANCEL_OPTION);

            try {
                if (option == JOptionPane.OK_OPTION) {
                    checkCharacterInfo(name, universeID, stance);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                actionPerformed(evt);
            }
        }


        //EFFECTS: check if the input character's information is valid
        private void checkCharacterInfo(JTextField name, JTextField universeID, JTextField stance) throws Exception {
            try {
                String namestr = name.getText();
                checkName(namestr);
                int id = Integer.parseInt(universeID.getText());
                checkUniverseID(id);
                int numStance = Integer.parseInt(stance.getText());
                boolean stancebl;
                stancebl = checkStance(numStance);
                universe = spiderVerse.addCharacter(namestr, id, stancebl);
                spiderVerse.sortUniverse(universe);
                JOptionPane.showMessageDialog(null, "Done! Successfully created a new character!", "Done",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                throw new Exception("Universe ID/Stance must be integer!");
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }

        //EFFECTS: check if the input character's name is already exists or not, if already exists, prompt
        //         the user to input another one
        private void checkName(String name) throws Exception {
            if (this.names.contains(name)) {
                throw new Exception("Name already exists, please choose another one!");
            }
            this.names.add(name);
        }

        //EFFECTS: check if the input universe ID is valid or no, has to be non-negative integer. If invalid,
        //         throws Exception
        private void checkUniverseID(int universeID) throws Exception {
            if (universeID < 0) {
                throw new Exception("UniverseID not valid, please choose a non-negative integer!");
            }

        }

        //EFFECTS: check if the input stance is valid or no, has to be either 0 or 1. If out of range, throws Exception
        private boolean checkStance(int numStance) throws Exception {
            if (numStance != 1 && numStance != 0) {
                throw new Exception("Stance input not valid, 1 for supporter, 0 for opponent!");
            }
            return numStance == 1;
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: Represents the action to be taken when the user wants to save the
     * current game status in the Spider-Verse.
     */
    private class SaveSpiderVerse extends AbstractAction {
        SaveSpiderVerse() {
            super("Save game");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                jsonWriter.open();
                jsonWriter.write(spiderVerse);
                jsonWriter.close();
                String succeedMssg = "Saved " + spiderVerse.getName() + " to " + JSON_STORE;
                JOptionPane.showMessageDialog(null, succeedMssg, "Done",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException e) {
                String errorMssg = "Unable to write to file: " + JSON_STORE;
                JOptionPane.showMessageDialog(null, errorMssg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: Represents the action to be taken when the user wants to load the
     * previously saved game status in the Spider-Verse.
     */
    private class LoadSpiderVerse extends AbstractAction {
        Set<String> names = SpiderManGameUI.this.names;

        LoadSpiderVerse() {
            super("Load game");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                spiderVerse = jsonReader.read();
                for (SpiderMan spider : spiderVerse.getAllSpiderMen()) {
                    this.names.add(spider.getName());
                }
                String loadMssg = "Loaded " + spiderVerse.getName() + " from " + JSON_STORE;
                JOptionPane.showMessageDialog(null, loadMssg, "Done",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                String errorMssg = "Unable to read from file: " + JSON_STORE;
                JOptionPane.showMessageDialog(null, errorMssg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the existing spider heroes in a specific universe
     */
    private class ViewSpiderHero extends AbstractAction {
        ViewSpiderHero() {
            super("View spider heroes by universe ID");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            try {
                JTextField id = new JTextField(5);
                Object[] message = {"Input universeID (non-negative integer):\n", id};
                int option = JOptionPane.showConfirmDialog(null, message, "View Spider Heroes",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    Universe universe;
                    int universeID;
                    universeID = Integer.parseInt(id.getText());
                    universe = checkUniverse(universeID);
                    if (universe != null) {
                        printInfo(universe);
                    }
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Universe ID must be integer!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                actionPerformed(evt);

            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                actionPerformed(evt);
            }
        }

        // EFFECTS: check if the universe with universeID exists.
        //          if exists, returns the Universe object, otherwise return null
        private Universe checkUniverse(int universeID) throws RuntimeException {
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
                throw new RuntimeException("\nUniverse-" + universeID + " doesn't have any spider heroes yet!");
            }
        }

        // EFFECTS: print out the existing spider hero(es) in the given universe
        private void printInfo(Universe universe) {
            int universeID = universe.getUniverseID();
            StringBuilder sb = new StringBuilder("Spider hero(es) in Universe - " + universeID + "\n");
            for (SpiderMan spiderMan : universe.getSpiderMen()) {
                String stance = spiderMan.getStance() ? "supporter" : "opponent";
                sb.append("\n" + spiderMan.getName() + "'s stance: " + stance);
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the existing spider heroes in all existing universes
     */
    private class ViewSpiderHeroes extends AbstractAction {
        ViewSpiderHeroes() {
            super("View spider heroes in all universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the existing spider hero(es) in the given universe
        private void printInfo() {
            StringBuilder sb = new StringBuilder("Spider heroes in all Universes:\n");
            for (SpiderMan spiderMan : spiderVerse.getAllSpiderMen()) {
                String stance = spiderMan.getStance() ? "supporter" : "opponent";
                int universeID = spiderMan.getUniverseID();
                sb.append("\n" + spiderMan.getName() + "'s stance: " + stance + ", in universe: " + universeID);
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the all the spider heroes that are supporters for the canon event
     */
    private class ViewSupporters extends AbstractAction {
        ViewSupporters() {
            super("View supporters in all universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the all the spider hero(es) that are supporters
        private void printInfo() {
            StringBuilder sb = new StringBuilder("Spider heroes that are supporters in all Universes:\n");
            for (SpiderMan spiderMan : spiderVerse.getSupporter()) {
                sb.append("\n" + spiderMan.getName() + " in Universe - " + spiderMan.getUniverseID());
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the all the spider heroes that are opponents for the canon event
     */
    private class ViewOpponents extends AbstractAction {
        ViewOpponents() {
            super("View opponents in all universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the all the spider hero(es) that are opponents
        private void printInfo() {
            StringBuilder sb = new StringBuilder("Spider heroes that are opponents in all Universes:\n");
            for (SpiderMan spiderMan : spiderVerse.getOpponent()) {
                sb.append("\n" + spiderMan.getName() + " in Universe - " + spiderMan.getUniverseID());
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the safe universe(s)
     */
    private class ViewSafeUniverse extends AbstractAction {
        ViewSafeUniverse() {
            super("View safe universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the safe universes
        private void printInfo() {
            StringBuilder sb = new StringBuilder("Universes that are safe:\n");
            for (Universe universe : spiderVerse.getSafeUniverses()) {
                sb.append("\nUniverse - " + universe.getUniverseID());
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * the collapsed universe(s)
     */
    private class ViewCollapsedUniverse extends AbstractAction {
        ViewCollapsedUniverse() {
            super("View collapsed universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the safe universes
        private void printInfo() {
            StringBuilder sb = new StringBuilder("Universes that are collapsed:\n");
            for (Universe universe : spiderVerse.getCollapsedUniverses()) {
                sb.append("\nUniverse - " + universe.getUniverseID());
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * EFFECTS: Represents the action to be taken when the user wants to view
     * all the existing universes
     */
    private class ViewAllUniverses extends AbstractAction {
        ViewAllUniverses() {
            super("View all universes");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            printInfo();
        }

        // EFFECTS: print out the safe universes
        private void printInfo() {
            StringBuilder sb = new StringBuilder("All universes:\n");
            for (Universe universe : spiderVerse.getAllUniverses()) {
                String status = universe.revealResult() ? " (safe)" : " (collapsed)";
                sb.append("\nUniverse - " + universe.getUniverseID() + status);
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Results",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //EFFECTS: start the Spider-Man Game application with a splash image
    public static void main(String[] args) throws FileNotFoundException {
        try {
            SplashJava splash = new SplashJava();
            // Make JWindow appear for 10 seconds before disappear
            Thread.sleep(3000);
            splash.dispose();
            new SpiderManGameUI();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "File Not Found Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //EFFECTS: Get an image to show in the splash window
    public static class SplashJava extends JWindow {
        Image splashScreen;
        ImageIcon imageIcon;

        public SplashJava() {
            try {
                splashScreen = Toolkit.getDefaultToolkit().getImage(new URL("https://media.newyorker.com/photos/64889828ef09d4a0e04c759c/master/w_2560%2Cc_limit/Bert-Spider-Verse.jpg"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            // Create ImageIcon from Image
            imageIcon = new ImageIcon(splashScreen);
            // Set JWindow size from image size
            setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            // Get current screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            // Get x coordinate on screen for make JWindow locate at center
            int x = (screenSize.width - getSize().width) / 2;
            // Get y coordinate on screen for make JWindow locate at center
            int y = (screenSize.height - getSize().height) / 2;
            // Set new location for JWindow
            setLocation(x, y);
            // Make JWindow visible
            setVisible(true);
        }

        // EFFECTS: Paint image onto JWindow
        public void paint(Graphics g) {
            super.paint(g);
            g.drawImage(splashScreen, 0, 0, this);
        }
    }

}
