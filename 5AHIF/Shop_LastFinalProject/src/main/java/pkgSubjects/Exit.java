package pkgSubjects;

import pkgThread.Shopper;

import java.util.concurrent.Semaphore;

public class Exit implements ShopElement {

    private Semaphore sem = new Semaphore(9999);

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

    @Override
    public String getName() {
        return "Exit";
    }
}
