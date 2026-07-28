package model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * A pile of cards, backed by java.util.ArrayDeque
 * the FRONT of the deque is the TOP of the pile (the card that gets played/dealt next), and the BACK of the
 * deque is the BOTTOM of the pile (where round winnings get returned).
 */
public class Deck {

    /** A valid game deck must contain exactly this many cards, with no duplicates. */
    private static final int REQUIRED_SIZE = 52;

    private final Deque<Card> cards = new ArrayDeque<>();

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /** Removes and returns the top card, or null if the deck is empty. */
    public Card dealTop() {
        return cards.pollFirst();
    }

    /** Adds a card to the bottom of the deck (where round winnings go). */
    public void addToBottom(Card card) {
        cards.addLast(card);
    }

    /** Adds a card to the top of the deck (used only while dealing/loading). */
    public void addToTop(Card card) {
        cards.addFirst(card);
    }

    /** Snapshot of the current order, top-to-bottom. */
    public List<Card> asList() {
        return new ArrayList<>(cards);
    }

    /**
     * Reads a deck from a text file formatted as comma-separated SUIT-RANK
     * tokens = "D-A,D-K,D-Q,...". Cards appear in the deck in the same
     * top-to-bottom order they appear in the file.
     *
     * The file must describe exactly 52 cards with no repeats
     * if card is lacking, output the total numnber of the deck
     */
    public static Deck loadFromFile(String fileName) throws IOException {
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

    /**
     * Saves this deck's current top-to-bottom order to a text file, in the
     * same "D-A,D-K,..." format used for loading a deck. Used at game end to
     * record the winner's final card arrangement.
     */
    public void saveToFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        Iterator<Card> it = cards.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(fileName), StandardCharsets.UTF_8)) {
            writer.write(sb.toString());
        }
    }

   //shuffle
    public void riffleShuffle() {
        List<Card> all = new ArrayList<>(cards);
        int mid = all.size() / 2;
        Deque<Card> firstHalf = new ArrayDeque<>(all.subList(0, mid));
        Deque<Card> secondHalf = new ArrayDeque<>(all.subList(mid, all.size()));

        Deque<Card> shuffled = new ArrayDeque<>();
        while (!firstHalf.isEmpty() || !secondHalf.isEmpty()) {
            if (!firstHalf.isEmpty()) {
                shuffled.addLast(firstHalf.pollFirst());
            }
            if (!secondHalf.isEmpty()) {
                shuffled.addLast(secondHalf.pollFirst());
            }
        }
        cards.clear();
        cards.addAll(shuffled);
    }

    /** Text form used for the deck text-file format (and internally by saveToFile). */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Card> it = cards.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    //display deck on terminal
    public String toDisplayString() {
        List<Card> list = asList();
        int cardsPerRow = 13;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(String.format("%-6s", list.get(i)));
            boolean endOfRow = (i + 1) % cardsPerRow == 0;
            boolean lastCard = i == list.size() - 1;
            if (endOfRow || lastCard) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
