package pkgSubjects;

import pkgThread.CarDriver;

import java.util.ArrayList;

public class Lane {
    public static int MAX_CAPACITY = 2;
    private ArrayList<CarDriver> carsInLane = new ArrayList<>(MAX_CAPACITY);

    public synchronized void addDriverToLane(CarDriver driver) throws Exception {
        if (carsInLane.size() < MAX_CAPACITY) {
            carsInLane.add(driver);
        } else {
            throw new Exception("error: lane is full; entering not possible");
        }
    }

    public synchronized void removeDriverFromLane(CarDriver driver) {
        carsInLane.remove(driver);
    }
}
