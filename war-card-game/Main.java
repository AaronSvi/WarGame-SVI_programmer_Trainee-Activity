import game.WarGame;

/**
 * Entry point. This class does nothing but start the game — all the actual
 * game logic lives in game/WarGame.java.
 */
public class Main {

    public static void main(String[] args) {
        WarGame game = new WarGame();
        game.play();
    }
}
