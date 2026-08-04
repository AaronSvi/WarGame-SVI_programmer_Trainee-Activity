package game;

import java.util.Scanner;


public class RoundDelay {

    private final Scanner in;

    private volatile boolean skipped = false;

    public RoundDelay(Scanner in) {
        this.in = in;
    }


    public void startListening() {
        Thread listener = new Thread(() -> {
            try {
                in.nextLine();
                skipped = true;
            } catch (Exception ignored) {

            }
        });
        listener.setDaemon(true);
        listener.start();
    }

    public boolean isSkipped() {
        return skipped;
    }


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
