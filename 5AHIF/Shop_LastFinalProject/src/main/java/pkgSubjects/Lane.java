package pkgSubjects;

import pkgThread.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Lane {
    private String name;
    private ArrayList<Customer.TYPE> allowed = new ArrayList<>();
    private ArrayList<Customer> carsInLane = new ArrayList<>();
    private Semaphore sem;
    private int count = 0;

    public Lane(String name, int maxCapacity, Customer.TYPE... allowed) {
        this.name = name;
        sem = new Semaphore(maxCapacity);
        this.allowed = new ArrayList<>(List.of(allowed));
    }

    public void addDriverToLane(Customer customer) throws InterruptedException {
        if (!allowed.contains(customer.getType())) {
            sem.acquire();
            carsInLane.add(customer);
            count++;
        } else {
            sem.release();
            carsInLane.add(customer);
            count++;
            sem.acquire();
        }
        System.out.println(getName() + ": added, current cuont is " + count);
    }

    public void removeDriverFromLane(Customer customer) {
        if (carsInLane.remove(customer)) {
            if (!allowed.contains(customer.getType())) {
                sem.release();
            }
            count--;
            System.out.println(getName() + ": removed, current cuont is " + count);
        }
    }

    public String getName() {
        return name;
    }
}
