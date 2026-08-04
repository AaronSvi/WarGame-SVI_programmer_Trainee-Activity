package model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Shuffler {


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
