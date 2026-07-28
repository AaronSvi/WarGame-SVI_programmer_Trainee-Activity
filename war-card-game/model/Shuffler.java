package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Knows how to riffle-shuffle a Deck: cut it exactly in half, then
 * interleave cards alternately from the top half and the bottom half
 * (top-half card first). Kept separate from Deck so the shuffling
 * algorithm can be changed or replaced without touching how a deck stores
 * or manages its cards.
 */
public class Shuffler {

    //private Shuffler() {
        // utility class — every method is static, so this is never instantiated
    //}

    public static void riffleShuffle(Deck deck) {
        List<Card> all = deck.asList();
        int mid = all.size() / 2;
        Deque<Card> firstHalf = new ArrayDeque<>(all.subList(0, mid));
        Deque<Card> secondHalf = new ArrayDeque<>(all.subList(mid, all.size()));

        List<Card> shuffled = new ArrayList<>();
        while (!firstHalf.isEmpty() || !secondHalf.isEmpty()) {
            if (!firstHalf.isEmpty()) {
                shuffled.add(firstHalf.pollFirst());
            }
            if (!secondHalf.isEmpty()) {
                shuffled.add(secondHalf.pollFirst());
            }
        }
        deck.replaceCards(shuffled);
    }
}
