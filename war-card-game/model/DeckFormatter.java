package model;

import java.util.Iterator;
import java.util.List;


public class DeckFormatter {

    // Plain comma-separated format, e.g. "D-A,D-K,D-Q", used for saved/loaded deck files
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
