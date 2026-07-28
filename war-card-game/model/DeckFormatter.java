package model;

import java.util.Iterator;
import java.util.List;

/**
 * Turns a Deck's current cards into text, in the three formats this program
 * needs. Kept separate from Deck itself so "how a deck looks on screen or
 * in a file" doesn't have to live inside the class that just manages cards.
 */
public class DeckFormatter {

   // private DeckFormatter() {
        // utility class — every method is static, so this is never instantiated
   // }

    /** Plain comma-separated format, e.g. "D-A,D-K,D-Q", used for saved/loaded deck files. */
    public static String toFileFormat(Deck deck) {
        List<Card> list = deck.asList();
        StringBuilder sb = new StringBuilder();
        Iterator<Card> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * A human-readable, multi-line view for printing a whole deck to the
     * console: fixed-width cards, 13 per row, instead of one very long
     * comma-separated line.
     */
    public static String toDisplayString(Deck deck) {
        List<Card> list = deck.asList();
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

    /**
     * A compact, single-line, comma-and-space-separated list of the current
     * cards, e.g. "D-A, D-K, D-Q". Used to show a player's whole hand on one
     * line each round.
     */
    public static String toHandString(Deck deck) {
        List<Card> list = deck.asList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i < list.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
