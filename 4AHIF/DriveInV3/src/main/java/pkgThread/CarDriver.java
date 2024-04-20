package pkgThread;

import pkgMain.App;
import pkgSubjects.Lane;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class CarDriver extends Thread {
    private String name;
    private Lane lane;
    private long totalWaitingTime;
    private PropertyChangeSupport changes = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener l) {
        changes.addPropertyChangeListener(l);
    }

    public void removeListener(PropertyChangeListener l) {
        changes.removePropertyChangeListener(l);
    }

    public CarDriver(String name, Lane lane) {
        this.name = name;
        this.lane = lane;
        this.totalWaitingTime = App.random.longs(6, 9).findFirst().getAsLong();
    }

    @Override
    public void run() {

        changes.firePropertyChange(this.getClass().getName(), null, name + ": hungry => arriving to Meki's DriveIn");
        changes.firePropertyChange(this.getClass().getName(), null, name + ": waiting for free lane");
        try {
            lane.addDriverToLane(this);
            changes.firePropertyChange(this.getClass().getName(), null, name + ": driving on free lane");
            changes.firePropertyChange(this.getClass().getName(), null, name + ": placing order, takes " + totalWaitingTime + " sec");
            Thread.sleep(totalWaitingTime * 1000);
            changes.firePropertyChange(this.getClass().getName(), null, name + ": leaving Meki's DriveIn; total time wasted => " + totalWaitingTime + " sec");
            lane.removeDriverFromLane(this);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
//            System.err.println(name + ": error lane is full; never going to Meki again");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarDriver carDriver = (CarDriver) o;
        return Objects.equals(name, carDriver.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
