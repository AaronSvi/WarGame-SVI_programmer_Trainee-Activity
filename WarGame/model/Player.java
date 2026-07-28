package model;

/** One player: an id (1-based, matches deal order) plus their hand of cards. */
public class Player {

    private final int id;
    private final Deck hand;

    public Player(int id, Deck hand) {
        this.id = id;
        this.hand = hand;
    }

    public int getId() {
        return id;
    }

    public Deck getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "Player " + id;
    }
}
