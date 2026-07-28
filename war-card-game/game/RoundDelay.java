package game;

import java.util.Scanner;

/**
 * Encapsulates the skippable 3-second pause between rounds: a background
 * thread waits for the user to press Enter, and once they do, every future
 * pause is skipped for the rest of the game.
 */
public class RoundDelay {

    private final Scanner in;

    /** Set to true by the background listener once the user presses Enter mid-game. */
    private volatile boolean skipped = false;

    public RoundDelay(Scanner in) {
        this.in = in;
    }

    /**
     * Starts a background thread that waits for the user to press Enter,
     * then marks this delay as skipped so every future round plays out with
     * no pause. Runs as a daemon thread so it never keeps the program alive
     * on its own.
     */
    public void startListening() {
        Thread listener = new Thread(() -> {
            try {
                in.nextLine();
                skipped = true;
            } catch (Exception ignored) {
                // Input stream closed (e.g. game already ended) — nothing to do.
            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    public boolean isSkipped() {
        return skipped;
    }

    /**
     * Waits up to millis milliseconds, but checks every 50ms whether the
     * user has pressed Enter in the meantime and returns early if so.
     */
    public void pause(long millis) {
        long end = System.currentTimeMillis() + millis;
        while (!skipped && System.currentTimeMillis() < end) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
