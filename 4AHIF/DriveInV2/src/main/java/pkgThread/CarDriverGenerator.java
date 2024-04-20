package pkgThread;

import pkgMain.App;
import pkgSubjects.Lane;

import java.time.LocalTime;

public class CarDriverGenerator extends Thread {
    private boolean running = true;
    private long minSleep;
    private long maxSleep;
    private int counter = 0;
    private Lane lane;

    public CarDriverGenerator(Lane lane, long minSleep, long maxSleep) {
        this.lane = lane;
        this.minSleep = minSleep;
        this.maxSleep = maxSleep;
    }

    public void end() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                long sleepTime = App.random.longs(minSleep, maxSleep).findFirst().getAsLong();
                new CarDriver("driver-" + counter, lane).start();
                counter++;
                LocalTime future = LocalTime.now().plusSeconds(sleepTime);
                System.out.println("=".repeat(80) + " next car in " +
                        future.getHour() + ":" +
                        future.getMinute() + ":" +
                        future.getSecond());
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
