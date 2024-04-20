package pkgSubjects;

import pkgThread.CarDriver;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Lane {
    public static int MAX_CAPACITY = 2;
    private ArrayList<CarDriver> carsInLane = new ArrayList<>(MAX_CAPACITY);
    private Semaphore sem = new Semaphore(MAX_CAPACITY);
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public void addDriverToLane(CarDriver driver) throws InterruptedException {
        sem.acquire();
        if (carsInLane.size() < MAX_CAPACITY) {
            carsInLane.add(driver);
        }
//        else {
//            throw new Exception("error: lane is full; entering not possible");
//        }
    }

    public void removeDriverFromLane(CarDriver driver) {
        carsInLane.remove(driver);
        sem.release();
    }
}
