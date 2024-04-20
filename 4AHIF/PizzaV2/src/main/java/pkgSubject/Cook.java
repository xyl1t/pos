package pkgSubject;

import java.util.concurrent.Semaphore;

public class Cook extends Thread implements Subject, Runnable {

    private final int MIN = 1000;
    private final int MAX = 4000;
    private String name;
    private Semaphore semaBarFree;
    private Semaphore semaPizzaOnBar;
    private Semaphore semaOrder;
    private boolean isEnd = false;
    Bar bar;
    Customer customer;

    public Cook(String name, Bar bar, Semaphore semaBarFree, Semaphore semaPizzaOnBar, Semaphore semaOrder){
        this.name = name;
        this.bar = bar;
        this.semaBarFree = semaBarFree;
        this.semaPizzaOnBar = semaPizzaOnBar;
        this.semaOrder = semaOrder;
    }

    @Override
    public void run() {
        try {
            while(!isEnd) {
                System.out.println("Cook " + name + ": is waiting for the next order");
                semaOrder.acquire();
                Order order = bar.takeOrder(this);
                System.out.println("Cook " + name + ": got order " + order);

                Pizza pizza = new Pizza(this);
                System.out.println("Cook " + name + ": starts creating pizza " + pizza);
                Thread.sleep((long)(Math.random() * (MAX - MIN)) + MIN);
                System.out.println("Cook " + name + " finished " + pizza);
                order.setPizza(pizza);

                System.out.println("Cook " + name + ": starts waiting for free bar");
                semaBarFree.acquire();

                System.out.println("Cook " + name + ": laying on Bar " + pizza);
                semaPizzaOnBar.release();
                order.getCustomer().releaseSemaPizzaOnBar();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnd(){
        isEnd = true;
    }


    public String getCookName() {
        return name;
    }
}

