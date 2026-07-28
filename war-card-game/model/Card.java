package model;

import java.util.Objects;

/** A single playing card: a Suit + a Rank. Immutable. */
public class Card {

    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    /**
     * Decides whether this card beats another card, per the flowchart's
     * "InitialPlayer_rank == NextPlayer_rank" check: rank decides first;
     * only when ranks tie does suit break the tie.
     *
     * Uses Math.max instead of subtracting values: for two different
     * numbers, whichever one equals Math.max(a, b) is the larger one. This
     * avoids ever computing a (possibly negative) difference just to check
     * its sign.
     *
     * @return true if this card is strictly stronger than other, false otherwise.
     */
    public boolean isStrongerThan(Card other) {
        int thisRank = this.rank.getValue();
        int otherRank = other.rank.getValue();

        if (thisRank != otherRank) {
            return Math.max(thisRank, otherRank) == thisRank;
        }

        int thisSuit = this.suit.getValue();
        int otherSuit = other.suit.getValue();
        return thisSuit != otherSuit && Math.max(thisSuit, otherSuit) == thisSuit;
    }

    /** Text form used both for on-screen display and for the deck text-file format. */
    @Override
    public String toString() {
        return suit.getCode() + "-" + rank.getCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Card)) {
            return false;
        }
        Card other = (Card) o;
        return suit == other.suit && rank == other.rank;
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
