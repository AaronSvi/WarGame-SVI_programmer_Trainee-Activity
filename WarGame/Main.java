import game.WarGame;

/**
 * Only starts the gamne
 * game logic lives in game/WarGame.java.
 */
public class Main {

    public static void main(String[] args) {
        WarGame game = new WarGame();
        game.play();
    }
}
