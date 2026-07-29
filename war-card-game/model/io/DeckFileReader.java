package model.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import model.Card;
import model.Deck;
import model.Rank;
import model.Suit;

/**
 * Reads a Deck from a text file formatted as comma-separated SUIT-RANK
 * tokens, e.g. "D-A,D-K,D-Q,...". Per the requirements doc, parsing is done
 * with StringTokenizer. Cards appear in the deck in the same top-to-bottom
 * order they appear in the file.
 *
 * Kept separate from Deck itself so file I/O doesn't have to live inside
 * the class that just manages a pile of cards.
 */
public class DeckFileReader {

    /** A valid game deck must contain exactly this many cards, with no duplicates. */
    private static final int REQUIRED_SIZE = 52;

  //  private DeckFileReader() {
        // utility class — every method is static, so this is never instantiated
  //  }

    /**
     * The file must describe exactly 52 unique cards — anything else throws
     * an IOException describing what was wrong, instead of silently
     * starting a game with a broken deck.
     */
    public static Deck read(String fileName) throws IOException {
        String content = Files.readString(Path.of(fileName), StandardCharsets.UTF_8);
        StringTokenizer tokenizer = new StringTokenizer(content.trim(), ",");
        Deck deck = new Deck();
        Set<Card> seen = new HashSet<>();

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) {
                continue;
            }
            String[] parts = token.split("-");
            if (parts.length != 2) {
                throw new IOException("Malformed card entry \"" + token + "\" in " + fileName);
            }
            Suit suit = Suit.fromCode(parts[0].trim());
            Rank rank = Rank.fromCode(parts[1].trim());
            Card card = new Card(suit, rank);

            if (!seen.add(card)) {
                throw new IOException("Duplicate card \"" + card + "\" found in " + fileName
                        + " - every card must appear exactly once.");
            }
            deck.addToBottom(card);
        }

        if (deck.size() != REQUIRED_SIZE) {
            throw new IOException(fileName + " contains " + deck.size() + " card(s), but a deck must "
                    + "contain exactly " + REQUIRED_SIZE + ".");
        }
        return deck;
    }
}
