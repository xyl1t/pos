package pkgThread;

import pkgMisc.MessageEvent;
import pkgMisc.MessageEventListener;

import java.time.LocalTime;
import java.util.ArrayList;

public class Watch extends Thread {
    private boolean running = true;
    private long sleepTime;
    private long secondsPassed;
    private ArrayList<MessageEventListener> msgListeners = new ArrayList<>();

    public void addListener(MessageEventListener l) {
        msgListeners.add(l);
    }
    public void removeListener(MessageEventListener l) {
        msgListeners.remove(l);
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
                notifyListeners(".".repeat(80) + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond());
                System.out.flush();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void notifyListeners(String msg) {
        for (MessageEventListener el : msgListeners) {
            el.handleMessageEvent(new MessageEvent(this, msg));
        }
    }
}
