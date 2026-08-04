package model;

public class Player {

    private final int id;
    private final Deck hand;

    public Player(int id, Deck hand) {
        this.id = id;
        this.hand = hand;
    }

    public Deck getHand() {
        return hand;
    }

    @Override
    public String toString() {
        return "Player " + id;
    }
}
