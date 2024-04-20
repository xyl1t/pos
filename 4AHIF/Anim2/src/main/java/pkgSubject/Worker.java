package pkgSubject;

import pkgMisc.AnimationParameters;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Worker extends Thread implements AnimationParameters {

    public enum WORKER_STATE {
        FORWARD1,
        FORWARD2,
        POSITION
    }

    private static final int MAX = 700;
    private static final int STEP = 20;

    private final PropertyChangeSupport supportPropertyCarMove;

    public Worker(PropertyChangeListener pcl) {
        supportPropertyCarMove = new PropertyChangeSupport(this);
        supportPropertyCarMove.addPropertyChangeListener(pcl);
    }

    @Override
    public void run() {
        for (int i = STEP; i <= MAX && !isInterrupted(); i++) {
            try {
                Thread.sleep(CAR_SLEEP_DURATION);
                int xpos = i;
                supportPropertyCarMove.firePropertyChange(WORKER_STATE.FORWARD1.toString(), null, xpos);
                String strTemp = "==>" + i + "<==";
                supportPropertyCarMove.firePropertyChange(WORKER_STATE.POSITION.toString(), null, strTemp);
                if(xpos % 50 == 0) {
                    supportPropertyCarMove.firePropertyChange(WORKER_STATE.FORWARD2.toString(), null, CAR2_DISTANCE);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
