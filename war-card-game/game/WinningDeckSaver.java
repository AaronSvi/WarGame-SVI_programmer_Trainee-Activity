package game;

import java.io.File;
import java.io.IOException;

import model.Player;
import model.io.DeckFileWriter;

/**
 * Saves the winner's final deck to its own numbered file inside the deck
 * folder — "winning deck(1).txt", "(2)", and so on — never overwriting a
 * previous game's result.
 */
public class WinningDeckSaver {

    private WinningDeckSaver() {
        // utility class — every method is static, so this is never instantiated
    }

    public static void save(Player winner) {
        String outputFile = nextAvailableWinningDeckFile();
        try {
            DeckFileWriter.write(winner.getHand(), outputFile);
            System.out.println("Winner's final card arrangement saved to " + outputFile);
        } catch (IOException e) {
            System.out.println("Could not save winner's deck: " + e.getMessage());
        }
    }

    /**
     * Finds a filename that doesn't exist yet, so each game's winning deck
     * is kept instead of overwriting the previous one.
     */
    private static String nextAvailableWinningDeckFile() {
        int n = 1;
        File candidate;
        do {
            candidate = new File(GameConfig.DECK_FOLDER, "winning deck(" + n + ").txt");
            n++;
        } while (candidate.exists());
        return candidate.getPath();
    }
}
