package model;

import java.util.Objects;

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
