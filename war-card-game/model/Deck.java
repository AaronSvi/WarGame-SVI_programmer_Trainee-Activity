package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


public class Deck {

    private final Deque<Card> cards = new ArrayDeque<>();

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card dealTop() {
        return cards.pollFirst();
    }

    public void addToBottom(Card card) {
        cards.addLast(card);
    }


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
