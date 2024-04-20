package pkgSubjects;

import pkgThread.Shopper;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Shelf implements ShopElement {
    private String name;
    private int count = 0;
    private Semaphore sem;
    private ArrayList<Shopper> currentShoppers = new ArrayList<>();

    public Shelf(String name, int maxCapacity) {
        this.name = name;
        sem = new Semaphore(maxCapacity);

    }

    public void addShopper(Shopper shopper) {
        try {
            sem.acquire();
            currentShoppers.add(shopper);
            count++;
            System.out.println(getName() + ": added " + shopper.getShopperName() + ", current count is " + count);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeShopper(Shopper shopper) {
        if (currentShoppers.remove(shopper)) {
            count--;
            sem.release();
            System.out.println(getName() + ": removed " + shopper.getShopperName() + ", current count is " + count);
        }
    }

    public String getName() {
        return name;
    }
}
