package game;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.DeckFormatter;
import model.Player;

/**
 * Plays a single round: every active player lays their top card, the
 * strongest card wins, and the winner collects the whole pot onto the
 * bottom of their deck (winning card first). Everything about deciding and
 * resolving ONE round lives here — the loop that repeats rounds and checks
 * for eliminations stays in WarGame, since that's a different job (running
 * the overall game) from this one (refereeing a single round).
 */
public class RoundReferee {

    private RoundReferee() {
        // utility class — every method is static, so this is never instantiated
    }

    public static void playRound(List<Player> activePlayers, int roundNumber) {
        System.out.println();
        System.out.println("Game Start - Round " + roundNumber);

        // Each player lays their top card (the "pot" for this round).
        List<Card> pot = new ArrayList<>();
        for (Player player : activePlayers) {
            System.out.println(player + " hand: " + DeckFormatter.toHandString(player.getHand()));
            Card played = player.getHand().dealTop();
            pot.add(played);
            System.out.println(player + " plays " + played);
        }

        // Compare rank first; only tie-break on suit when ranks match.
        int winnerIndex = 0;
        for (int i = 1; i < pot.size(); i++) {
            if (pot.get(i).isStrongerThan(pot.get(winnerIndex))) {
                winnerIndex = i;
            }
        }
        Player winner = activePlayers.get(winnerIndex);
        Card highestCard = pot.get(winnerIndex);
        System.out.println(winner + " wins round " + roundNumber + " with " + highestCard);



        // Winner takes the round: the winning card goes to the bottom of
        // their deck first (so it ends up on top of the group just won),
        // followed by the rest of the pot's cards in play order.
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
