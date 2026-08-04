package game;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Deck;
import model.Player;

//gives the card sto the players
public class Dealer {

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
