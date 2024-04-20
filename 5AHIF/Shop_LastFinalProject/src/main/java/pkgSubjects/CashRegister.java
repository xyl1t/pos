package pkgSubjects;

import pkgThread.Shopper;

import java.util.concurrent.Semaphore;

public class CashRegister implements ShopElement {
    private String name;
    private Semaphore sem;

    public CashRegister(String name) {
        this.name = name;
        sem = new Semaphore(1);
    }

    public boolean isAvailable() {
        return sem.availablePermits() > 0;
    }

    public String getName() {
        return name;
    }

    @Override
    public void addShopper(Shopper shopper) {
        try {
            sem.acquire();
            System.out.println(getName() + ": added " + shopper.getShopperName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeShopper(Shopper shopper) {
        sem.release();
        System.out.println(getName() + ": removed " + shopper.getShopperName());
    }
}
