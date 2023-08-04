package ui;

import model.SpiderMan;
import model.SpiderVerse;
import model.Universe;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SpiderManGameUI extends JFrame {
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


    public SpiderManGameUI()  throws FileNotFoundException {
        init();

        desktop = new JDesktopPane();
        desktop.addMouseListener(new DesktopFocusAction());
        controlPanel = new JInternalFrame("Control Panel", true, false, true, false);
        controlPanel.setLayout(new BorderLayout());

        setContentPane(desktop);
        setTitle("Spider-Verse Game");
        setSize(WIDTH, HEIGHT);

        addButtonPanel();

        controlPanel.pack();
        controlPanel.setVisible(true);
        desktop.add(controlPanel);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        centreOnScreen();
        setVisible(true);
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
     * Helper to centre main application window on desktop
     */
    private void centreOnScreen() {
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);
    }

    /**
     * Represents action to be taken when user clicks desktop
     * to switch focus. (Needed for key handling.)
     */
    private class DesktopFocusAction extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            SpiderManGameUI.this.requestFocusInWindow();
        }
    }

    /**
     * Helper to add control buttons.
     */
    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4,2));

        buttonPanel.add(new JButton(new AddSpiderHero()));
        buttonPanel.add(new JButton(new SaveSpiderVerse()));
        buttonPanel.add(new JButton(new LoadSpiderVerse()));
//        buttonPanel.add(createPrintCombo());

        controlPanel.add(buttonPanel, BorderLayout.WEST);
    }

    /**
     * Helper to create print options combo box
     * @return  the combo box
     */
    private JComboBox<String> createPrintCombo() {
        printCombo = new JComboBox<String>();
        return printCombo;
    }

    /**
     * Represents the action to be taken when the user wants to save the
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
     * Represents the action to be taken when the user wants to save the
     * current game status in the Spider-Verse.
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
     * Represents the action to be taken when the user wants to add a new
     * spider hero character to the Spider-Verse.
     */
    private class AddSpiderHero extends AbstractAction {
        Set<String> names = SpiderManGameUI.this.names;

        AddSpiderHero() {
            super("Create a New Spider Hero");

        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            JTextField name = new JTextField(5);
            JTextField universeID = new JTextField(5);
            JTextField stance = new JTextField(5);
            Object[] message = {
                    "Input the spider hero's name:\n", name,
                    "Input a universeID you want to live! (non-negative integer):\n", universeID,
                    "Choose to be supporter or opponent of the canon event rule! (1 for supporter, 0 for opponent)\n",
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
            boolean stance;
            if (numStance != 1 && numStance != 0) {
                throw new Exception("Stance input not valid, 1 for supporter, 0 for opponent!");
            }
            return numStance == 1;
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        try {
            new SpiderManGameUI();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "File Not Found Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
