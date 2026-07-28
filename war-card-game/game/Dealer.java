package game;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Deck;
import model.Player;

/** Deals a whole Deck out to a set of Players, round-robin, one card at a time. */
public class Dealer {

    private Dealer() {
        // utility class — every method is static, so this is never instantiated
    }

    public static List<Player> deal(Deck deck, int numPlayers) {
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
}
