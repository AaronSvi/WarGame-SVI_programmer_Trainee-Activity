package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * A pile of cards, backed by java.util.ArrayDeque (Java Collections Framework,
 * per the requirements doc).
 *
 * Convention used everywhere in this program: the FRONT of the deque is the
 * TOP of the pile (the card that gets played/dealt next), and the BACK of the
 * deque is the BOTTOM of the pile (where round winnings get returned).
 *
 * This class only knows how to hold and rearrange cards. Related jobs live
 * in their own focused classes instead of being crammed in here too:
 *   - model.io.DeckFileReader / DeckFileWriter — reading/writing deck files
 *   - model.Shuffler                          — the riffle-shuffle algorithm
 *   - model.DeckFormatter                     — turning a deck into readable text
 */
public class Deck {

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
     * Replaces the entire pile with a new top-to-bottom order. Used by
     * Shuffler to install a freshly shuffled order without handing out the
     * internal Deque itself to other classes.
     */
    public void replaceCards(List<Card> newOrder) {
        cards.clear();
        cards.addAll(newOrder);
    }

    /** Plain comma-separated format (delegates to DeckFormatter). */
    @Override
    public String toString() {
        return DeckFormatter.toFileFormat(this);
    }
}
