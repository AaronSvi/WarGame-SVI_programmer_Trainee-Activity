package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import model.Card;
import model.Deck;
import model.Player;

/**
 *
 * This class holds all the game logic.
 */
public class WarGame {

    /** for txt deck. */
    private static final String DECK_FOLDER = "deck of card";

    private final Scanner in = new Scanner(System.in);

    /** Set to true by the background listener once the user presses Enter mid-game. */
    private volatile boolean skipDelay = false;

    /** Runs one complete game, start to finish. This is what Main.main() calls. */
    public void play() {
        Deck deck = promptForDeckFile();

        System.out.println("Deck content before shuffle:");
        System.out.println(deck.toDisplayString());

        int numPlayers = promptForPlayerCount();
        int shuffleCount = promptForShuffleCount();

        for (int i = 0; i < shuffleCount; i++) {
            deck.riffleShuffle();
        }
        System.out.println("Deck content after shuffle:");
        System.out.println(deck.toDisplayString());

        List<Player> activePlayers = dealCards(deck, numPlayers);

        System.out.println();
        System.out.println("Each round will pause for 3 seconds so you can read it. "
                + "Press ENTER at any time to skip the remaining pauses and finish the game instantly.");
        startEnterListener();

        int roundNumber = playGame(activePlayers);

        Player winner = activePlayers.get(0);
        System.out.println();
        System.out.println("GAME OVER - " + winner + " has all 52 cards!");
        System.out.println("Total rounds played: " + roundNumber);

        saveWinnerArrangement(winner);
    }

    // ------------------------------------------------------------------
    // Setup: deck file selection
    // ------------------------------------------------------------------

    private List<String> listAvailableTextFiles() {
        List<String> names = new ArrayList<>();
        File deckDir = new File(DECK_FOLDER);
        File[] files = deckDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        if (files != null) {
            for (File f : files) {
                names.add(f.getName());
            }
        }
        return names;
    }

    /** Keeps asking for a deck file name ung nasa folder */
    private Deck promptForDeckFile() {
        while (true) {
            List<String> available = listAvailableTextFiles();
            System.out.println("Available deck files in \"" + DECK_FOLDER + "\":");
            if (available.isEmpty()) {
                System.out.println("  (none found in \"" + DECK_FOLDER + "\")");
            } else {
                for (String name : available) {
                    System.out.println("  " + name);
                }
            }
            System.out.print("Enter the name of the deck file to use: ");
            String fileName = in.nextLine().trim();
            String fullPath = new File(DECK_FOLDER, fileName).getPath();

            try {
                Deck deck = Deck.loadFromFile(fullPath);
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

    // ------------------------------------------------------------------
    // Setup: player count / shuffle count
    // ------------------------------------------------------------------

    private int promptForPlayerCount() {
        while (true) {
            System.out.print("Enter number of players (2-8): ");
            String line = in.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n >= 2 && n <= 8) {
                    return n;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message below
                //System.out.println("Input 2-8 players");
            }
            System.out.println("Input 2-8 players");
        }
    }

    private int promptForShuffleCount() {
        while (true) {
            System.out.print("Enter number of shuffles (1-9): ");
            String line = in.nextLine().trim();
            try {
                int n = Integer.parseInt(line);
                if (n > 0 && n < 10) {
                    return n;
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message below
            }
            System.out.println("Input 1-9 shuffles");
        }
    }

    // ------------------------------------------------------------------
    // Give card to each player
    // ------------------------------------------------------------------

    /** Deals the whole deck round-robin, one card at a time, to numPlayers players. */
    private List<Player> dealCards(Deck deck, int numPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player(i, new Deck()));
        }

        int turn = 0;
        while (!deck.isEmpty()) {
            Card dealt = deck.dealTop();
            players.get(turn).getHand().addToBottom(dealt);
            turn = (turn + 1) % numPlayers;
        }
        return players;
    }

    // ------------------------------------------------------------------
    // Main game loop
    // ------------------------------------------------------------------

    /**
     * Plays rounds until one player holds every card. Returns the number of
     * rounds actually played.
     */
    private int playGame(List<Player> activePlayers) {
        int roundNumber = 1;

        while (activePlayers.size() > 1) {
            System.out.println();
            System.out.println("Game Start - Round " + roundNumber);

            // Each player lays their top card (the "pot" for this round).
            List<Card> pot = new ArrayList<>();
            for (Player player : activePlayers) {
                Card played = player.getHand().dealTop();
                pot.add(played);
                System.out.println(player + " plays " + played);
            }

            // Compare rank first; only tie-break on suit when ranks match.
            int winnerIndex = 0;
            for (int i = 1; i < pot.size(); i++) {
                if (pot.get(i).compareStrength(pot.get(winnerIndex)) > 0) {
                    winnerIndex = i;
                }
            }
            Player winner = activePlayers.get(winnerIndex);
            Card highestCard = pot.get(winnerIndex);
            System.out.println(winner + " wins round " + roundNumber + " with " + highestCard);

            // Winner takes the round: the winning card goes to the bottom of
            // their deck first (so it ends up on top of the group just won),
            // followed by the rest of the pot's cards in play order.
            winner.getHand().addToBottom(highestCard);
            for (int i = 0; i < pot.size(); i++) {
                if (i != winnerIndex) {
                    winner.getHand().addToBottom(pot.get(i));
                }
            }

            System.out.println("Hand sizes after round " + roundNumber + ":");
            for (Player player : activePlayers) {
                System.out.println("  " + player + ": " + player.getHand().size() + " card(s)");
            }

            roundNumber++;

            // Eliminate any player left with no cards.
            Iterator<Player> it = activePlayers.iterator();
            while (it.hasNext()) {
                Player player = it.next();
                if (player.getHand().isEmpty()) {
                    System.out.println(player + " is eliminated.");
                    it.remove();
                }
            }

            //delay 3 seconds
            if (activePlayers.size() > 1 && !skipDelay) {
                waitWithSkip(3000);
            }
        }

        return roundNumber - 1;
    }

    // ------------------------------------------------------------------
    // Skippable 3-second delay between rounds
    // ------------------------------------------------------------------

    /**
     * Starts a background thread that waits for the user to press Enter,
     * then sets skipDelay so every future round plays out with no pause.
     * Runs as a daemon thread so it never keeps the program alive on its own.
     */
    private void startEnterListener() {
        Thread listener = new Thread(() -> {
            try {
                in.nextLine();
                skipDelay = true;
            } catch (Exception ignored) {
                // Input stream closed (e.g. game already ended) — nothing to do.
            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    /**
     * Waits up to millis milliseconds, but checks every 50ms whether
     * skipDelay has been set in the meantime and returns early if so.
     */
    private void waitWithSkip(long millis) {
        long end = System.currentTimeMillis() + millis;
        while (!skipDelay && System.currentTimeMillis() < end) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    // ------------------------------------------------------------------
    // End of game: save the winner's final card arrangement
    // ------------------------------------------------------------------

    private void saveWinnerArrangement(Player winner) {
        String outputFile = nextAvailableWinningDeckFile();
        try {
            winner.getHand().saveToFile(outputFile);
            System.out.println("Winner's final card arrangement saved to " + outputFile);
        } catch (IOException e) {
            System.out.println("Could not save winner's deck: " + e.getMessage());
        }
    }

    /**
     * Finds a filename that doesn't exist yet, so each game's winning deck is
     * kept instead of overwriting the previous one: "winning deck(1).txt",
     * "winning deck(2).txt", and so on.
     */
    private String nextAvailableWinningDeckFile() {
        int n = 1;
        File candidate;
        do {
            candidate = new File(DECK_FOLDER, "winning deck(" + n + ").txt");
            n++;
        } while (candidate.exists());
        return candidate.getPath();
    }
}
