package game;

import java.io.File;
import java.io.IOException;

import model.Player;
import model.io.DeckFileWriter;


public class WinningDeckSaver {


    public static void save(Player winner) {
        String outputFile = nextAvailableWinningDeckFile();
        try {
            DeckFileWriter.write(winner.getHand(), outputFile);
            System.out.println("Winner's final card arrangement saved to " + outputFile);
        } catch (IOException e) {
            System.out.println("Could not save winner's deck: " + e.getMessage());
        }
    }


    private static String nextAvailableWinningDeckFile() {
        int n = 1;
        File candidate;
        do {
            candidate = new File(GameConfig.DECK_FOLDER, "winning_deck"+n+".txt");
            n++;
        } while (candidate.exists());
        return candidate.getPath();
    }
}
