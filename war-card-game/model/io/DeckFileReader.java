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


public class DeckFileReader {

    private static final int REQUIRED_SIZE = 52;



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
