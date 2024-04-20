package pkgThread;

import pkgMisc.MessageEvent;
import pkgMisc.UpdateResultsEvent;
import pkgMisc.UpdateResultsEventListener;

import java.util.ArrayList;

public class Watch extends Thread {
    private boolean running = true;
    private long sleepTime;
    private long secondsPassed;
    private ArrayList<UpdateResultsEventListener> listeners = new ArrayList<>();

    public void addListener(UpdateResultsEventListener l) {
        listeners.add(l);
    }
    public void removeListener(UpdateResultsEventListener l) {
        listeners.remove(l);
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
                Thread.sleep(sleepTime);
                secondsPassed += sleepTime;
                notifyListeners();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void notifyListeners() {
        for (UpdateResultsEventListener el : listeners) {
            el.handleUpdateResultsEvent(new UpdateResultsEvent(this));
        }
    }
}
