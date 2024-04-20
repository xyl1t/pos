package pkgThread;

import java.util.ArrayList;

public class Watch extends Thread {
    private boolean running = true;
    private long sleepTime;
    private long secondsPassed;

    public Watch(long sleepInterval) {
        this.sleepTime = sleepInterval;
    }

    public void end() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(sleepTime * 1000);
                secondsPassed += sleepTime;
                System.out.println(".".repeat(80) + secondsPassed);
                System.out.flush();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
