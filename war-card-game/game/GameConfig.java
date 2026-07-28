package game;

/** Shared configuration values used across the game package. */
public class GameConfig {

    /** Folder (relative to where the program is run from) that holds the deck .txt files. */
    public static final String DECK_FOLDER = "deck_of_card";

    private GameConfig() {
        // utility class — every member is static, so this is never instantiated
    }
}
