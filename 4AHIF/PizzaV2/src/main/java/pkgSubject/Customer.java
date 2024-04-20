package pkgSubject;

import java.util.concurrent.Semaphore;

public class Customer extends Thread implements Subject, Runnable{
    private final int MIN = 4000;
    private final int MAX = 6000;
    private String name;
    private Semaphore semaBarFree;
    private Semaphore semaPizzaOnBar;
    private Semaphore semaOrder;
    private boolean isEnd = false;
    Bar bar;

    public Customer(String name, Bar bar, Semaphore semaBarFree, Semaphore semaPizzaOnBar, Semaphore semaOrder){
        this.name = name;
        this.bar = bar;
        this.semaBarFree = semaBarFree;
        this.semaPizzaOnBar = new Semaphore(0);
        this.semaOrder = semaOrder;
    }

    public void releaseSemaPizzaOnBar() {
        semaPizzaOnBar.release();
    }

    @Override
    public void run() {
        try {
            while(!isEnd) {
                System.out.println("Customer " + name + ": is hungry and creates order");
                Order order = bar.createOrder(this);
                System.out.println("Customer " + name + ": created order " + order);
                semaOrder.release();

                semaPizzaOnBar.acquire();
                Pizza pizza = order.getPizza();
                System.out.println("Customer " + name + ": got Pizza from order " + order);

                System.out.println("Customer " + name + ": starts eating " + pizza);
                Thread.sleep( (long)Math.random() * (MAX-MIN) + MIN);
                System.out.println("Customer " + name + ": finished " + pizza);
                semaBarFree.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setEnd(){
        isEnd = true;
    }

    @Override
    public String toString() {
        return name + " -" + getId() + "-";
    }

    public String getCustomerName() {
        return name;
    }
}

