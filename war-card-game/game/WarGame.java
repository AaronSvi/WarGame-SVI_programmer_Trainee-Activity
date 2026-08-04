package game;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import model.Deck;
import model.DeckFormatter;
import model.Player;
import model.Shuffler;


public class WarGame {

    private final Scanner in = new Scanner(System.in);
    private final GameSetup setup = new GameSetup(in);
    private final RoundDelay roundDelay = new RoundDelay(in);

    public void play() {
        Deck deck = setup.promptForDeckFile();

        System.out.println("Deck content before shuffle:");
        System.out.println(DeckFormatter.toDisplayString(deck));

        int numPlayers = setup.promptForPlayerCount();
        int shuffleCount = setup.promptForShuffleCount();

        for (int i = 0; i < shuffleCount; i++) {
            Shuffler.riffleShuffle(deck);
        }
        System.out.println("Deck content after shuffle:");
        System.out.println(DeckFormatter.toDisplayString(deck));

        List<Player> activePlayers = Dealer.deal(deck, numPlayers);

        System.out.println();
        System.out.println("Each round will pause for 3 seconds so you can read it. "
                + "Press ENTER at any time to skip the remaining pauses and finish the game instantly.");
        roundDelay.startListening();

        int roundNumber = playGame(activePlayers);

        Player winner = activePlayers.get(0);
        System.out.println();
        System.out.println("GAME OVER - " + winner + " has all 52 cards!");
        System.out.println("Total rounds played: " + roundNumber);
        System.out.println();
        System.out.println("Winning deck:");
        System.out.println(DeckFormatter.toDisplayString(winner.getHand()));

        WinningDeckSaver.save(winner);
    }


    private int playGame(List<Player> activePlayers) {
        int roundNumber = 1;

        while (activePlayers.size() > 1) {
            RoundReferee.playRound(activePlayers, roundNumber);
            roundNumber++;

            Iterator<Player> it = activePlayers.iterator();
            while (it.hasNext()) {
                Player player = it.next();
                if (player.getHand().isEmpty()) {
                    System.out.println(player + " is eliminated.");
                    it.remove();
                }
            }


            if (activePlayers.size() > 1 && !roundDelay.isSkipped()) {
                roundDelay.pause(2000);
            }
        }

        return roundNumber - 1;
    }
}
