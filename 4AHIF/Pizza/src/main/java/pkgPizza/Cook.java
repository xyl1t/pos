package pkgPizza;

import java.util.concurrent.Semaphore;

public class Cook extends Thread implements Subject, Runnable {

    private final int MIN = 1000;
    private final int MAX = 4000;
    private String name;
    private Semaphore semaBarFree;
    private Semaphore semaPizzaOnBar;
    private boolean isEnd = false;
    Bar bar;


    public Cook(String name, Bar bar, Semaphore semaBarFree, Semaphore semaPizzaOnBar){
        this.name = name;
        this.bar = bar;
        this.semaBarFree = semaBarFree;
        this.semaPizzaOnBar = semaPizzaOnBar;
    }

    @Override
    public void run() {
        try {
            while(!isEnd) {
                Pizza pizza = new Pizza("pizza");
                System.out.println("Cook " + name + ": starts creating pizza " + pizza);
                Thread.sleep((long)(Math.random() * (MAX - MIN)) + MIN);
                System.out.println("Cook " + name + ": starts waiting for free bar");
                semaBarFree.acquire();
                System.out.println("Cook " + name + ": laying on Bar " + pizza);
                bar.addPizza(pizza);
                semaPizzaOnBar.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setEnd(){
        isEnd = true;
        semaBarFree.release();
    }
}

