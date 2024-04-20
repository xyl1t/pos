package pkgPizza;

import java.util.concurrent.Semaphore;

public class Customer extends Thread implements Subject, Runnable{
    private final int MIN = 4000;
    private final int MAX = 6000;
    private String name;
    private Semaphore semaBarFree;
    private Semaphore semaPizzaOnBar;
    private boolean isEnd = false;
    Bar bar;

    public Customer(String name, Bar bar, Semaphore semaBarFree, Semaphore semaPizzaOnBar){
        this.name = name;
        this.bar = bar;
        this.semaBarFree = semaBarFree;
        this.semaPizzaOnBar = semaPizzaOnBar;
    }

    @Override
    public void run() {
        try {
            while(!isEnd) {
                System.out.println("Customer " + name + ": starts waiting for pizza");
                semaPizzaOnBar.acquire();
                Pizza pizza = bar.takePizza();
                System.out.println("Customer " + name + ": got Pizza " + pizza);
                System.out.println("Customer " + name + ": eats Pizza " + pizza);
                Thread.sleep( (long)Math.random() * (MAX-MIN) + MIN);
                System.out.println("Customer " + name + ": finished eating Pizza " + pizza);
                semaBarFree.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setEnd(){
        isEnd = true;
        semaPizzaOnBar.release();
    }
}

