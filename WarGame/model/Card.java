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
     * compare cards. if positive winner p1 if nega p2
     */
    public int compareStrength(Card other) {
        int rankDiff = this.rank.getValue() - other.rank.getValue();
        if (rankDiff != 0) {
            return rankDiff;
        }
        return this.suit.getValue() - other.suit.getValue();
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
