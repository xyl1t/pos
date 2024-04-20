package pkgThread;

import pkgMain.App;
import pkgSubjects.Lane;

import java.util.Objects;

public class CarDriver extends Thread {
    private String name;
    private Lane lane;
    private long totalWaitingTime;

    public CarDriver(String name, Lane lane) {
        this.name = name;
        this.lane = lane;
        this.totalWaitingTime = App.random.longs(6, 9).findFirst().getAsLong();
    }

    @Override
    public void run() {
        System.out.println(name + ": hungry => arriving to Meki's DriveIn");
        System.out.println(name + ": waiting for free lane");
        try {
            lane.addDriverToLane(this);
            System.out.println(name + ": driving on free lane");
            System.out.println(name + ": placing order, takes " + totalWaitingTime + " sec");
            Thread.sleep(totalWaitingTime * 1000);
            System.out.println(name + ": leaving Meki's DriveIn; total time wasted => " + totalWaitingTime + " sec");
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
