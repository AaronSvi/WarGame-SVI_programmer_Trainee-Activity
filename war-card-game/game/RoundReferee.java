package game;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.DeckFormatter;
import model.Player;


public class RoundReferee {



    public static void playRound(List<Player> activePlayers, int roundNumber) {
        System.out.println();
        System.out.println("Game Start - Round " + roundNumber);

        List<Card> pot = new ArrayList<>();
        for (Player player : activePlayers) {
            System.out.println(player + " hand: " + DeckFormatter.toHandString(player.getHand()));
            Card played = player.getHand().dealTop();
            pot.add(played);
            System.out.println(player + " plays " + played);
        }

        // Compare rank then suit.
        int winnerIndex = 0;
        for (int i = 1; i < pot.size(); i++) {
            if (pot.get(i).isStrongerThan(pot.get(winnerIndex))) {
                winnerIndex = i;
            }
        }
        Player winner = activePlayers.get(winnerIndex);
        Card highestCard = pot.get(winnerIndex);
        System.out.println(winner + " wins round " + roundNumber + " with " + highestCard);



        //winner takes the cards. First highest then the rest
        winner.getHand().addToBottom(highestCard);
        for (int i = 0; i < pot.size(); i++) {
            if (i != winnerIndex) {
                winner.getHand().addToBottom(pot.get(i));
            }
        }

        System.out.println(winner + " hand: " + DeckFormatter.toHandString(winner.getHand()));

        System.out.println("Hand sizes after round " + roundNumber + ":");
        for (Player player : activePlayers) {
            System.out.println("  " + player + ": " + player.getHand().size() + " card(s)");
        }
    }
}
