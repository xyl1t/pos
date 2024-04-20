package pkgThread;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalTime;
import java.util.ArrayList;

public class Watch extends Thread {
    private boolean running = true;
    private long sleepTime;
    private long secondsPassed;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

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
                changes.firePropertyChange(
                        this.getClass().getName(),
                        null,
                ".".repeat(80) +
                        LocalTime.now().getHour() + ":" +
                        LocalTime.now().getMinute() + ":" +
                        LocalTime.now().getSecond());
                System.out.flush();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
