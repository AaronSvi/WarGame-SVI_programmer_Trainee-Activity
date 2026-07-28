package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Deck;
import model.io.DeckFileReader;

/**
 * Everything involved in setting up a game before the first round: picking
 * a deck file (with validation and retry), and asking how many players and
 * how many shuffles to use. Kept separate from WarGame so "asking the
 * setup questions" is its own job, distinct from "running the rounds".
 */
public class GameSetup {

    private final Scanner in;

    public GameSetup(Scanner in) {
        this.in = in;
    }

    /** Lists the .txt files inside the deck folder, e.g. "input.txt". */
    private List<String> listAvailableTextFiles() {
        List<String> names = new ArrayList<>();
        File deckDir = new File(GameConfig.DECK_FOLDER);
        File[] files = deckDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files != null) {
            for (File f : files) {
                names.add(f.getName());
            }
        }
        return names;
    }

    /** Keeps asking for a deck file name until one is typed that loads successfully. */
    public Deck promptForDeckFile() {
        while (true) {
            List<String> available = listAvailableTextFiles();
            System.out.println("Available deck files in \"" + GameConfig.DECK_FOLDER + "\":");
            if (available.isEmpty()) {
                System.out.println("  (none found in \"" + GameConfig.DECK_FOLDER + "\")");
            } else {
                for (String name : available) {
                    System.out.println("  " + name);
                }
            }
            System.out.print("Enter the name of the deck file to use: ");
            String fileName = in.nextLine().trim();
            String fullPath = new File(GameConfig.DECK_FOLDER, fileName).getPath();

            try {
                Deck deck = DeckFileReader.read(fullPath);
                if (deck.isEmpty()) {
                    System.out.println("\"" + fileName + "\" contained no cards. Try again.\n");
                    continue;
                }
                return deck;
            } catch (IOException | IllegalArgumentException e) {
                System.out.println("Could not load \"" + fileName + "\": " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }

    public int promptForPlayerCount() {
        return promptForRangedInt("Enter number of players (2-8): ", 2, 8);
    }

    public int promptForShuffleCount() {
        return promptForRangedInt("Enter number of shuffles (1-9): ", 1, 9);
    }

    /**
     * Repeatedly prompts for a whole number between min and max (inclusive),
     * with a distinct error message for each way the input can be wrong:
     *   - not a whole number at all (letters, symbols, decimals, etc.)
     *   - a negative number
     *   - a valid whole number that's simply outside the allowed range
     */
    private int promptForRangedInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = in.nextLine().trim();

            int value;
            try {
                value = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input - please enter a positive integer only "
                        + "(no letters or symbols).");
                continue;
            }

            if (value < 0) {
                System.out.println("Please enter a positive value between " + min + " and " + max + ".");
                continue;
            }

            if (value < min || value > max) {
                System.out.println("Please enter a value between " + min + " and " + max + ".");
                continue;
            }

            return value;
        }
    }
}
