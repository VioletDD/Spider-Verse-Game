package ui;

import java.io.FileNotFoundException;

public class Main {
    static SpiderManGame game;

    public static void main(String[] args) {
        try {
            game = new SpiderManGame();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found!");
        }
    }
}
