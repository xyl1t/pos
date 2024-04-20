package pkgThread;

import pkgMain.App;
import pkgSubjects.Lane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;

public class CarDriverGenerator extends Thread implements PropertyChangeListener{
    private boolean running = true;
    private long minSleep;
    private long maxSleep;
    private int counter = 0;
    private Lane lane;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

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
                CarDriver car = new CarDriver("driver-" + counter, lane);
                car.addListener(this);
                car.start();
                counter++;
                LocalTime future = LocalTime.now().plusSeconds(sleepTime);
                changes.firePropertyChange(
                        this.getClass().getName(),
                        null,
                        "=".repeat(30) + " next car in " +
                        future.getHour() + ":" +
                        future.getMinute() + ":" +
                        future.getSecond());
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        changes.firePropertyChange(e.getPropertyName(), e.getOldValue(), e.getNewValue());
    }
}
