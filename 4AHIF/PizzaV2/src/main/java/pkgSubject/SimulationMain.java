package pkgSubject;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class SimulationMain
{
    private static Bar bar;
    private static Semaphore semaBarFree;
    private static Semaphore semaPizzaOnBar;
    private static Semaphore semaOrder;

    private ArrayList<Customer> customers = new ArrayList();
    private ArrayList<Cook> cooks = new ArrayList();

    public SimulationMain() {

    }

    public void start(String numCooks, String numCust, String barCapacity, String cookTime, String eatingTime) throws InterruptedException {
        bar = new Bar(3);
        semaBarFree = new Semaphore(bar.getCapacity());
        semaPizzaOnBar = new Semaphore(0);
        semaOrder = new Semaphore(0);
        ArrayList<Thread> threads = new ArrayList<>();

        Customer customerA = new Customer("Ameise", bar, semaBarFree, semaPizzaOnBar, semaOrder);
        threads.add(new Thread(customerA));

        Customer customerB = new Customer("Bmeise", bar, semaBarFree, semaPizzaOnBar, semaOrder);
        threads.add(new Thread(customerB));

        Cook cookA = new Cook("Seppl", bar, semaBarFree, semaPizzaOnBar, semaOrder);
        threads.add(new Thread(cookA));

        Cook cookB = new Cook("Hansi", bar, semaBarFree, semaPizzaOnBar, semaOrder);
        threads.add(new Thread(cookB));

        for(Thread thread : threads){
            thread.start();
        }

        Thread.sleep(10000);

        System.out.println("********* set end of simulation *********************");

        customerA.setEnd();
        customerB.setEnd();
        cookA.setEnd();
        cookB.setEnd();

        System.out.println("********* waiting for end of simulation *********************");

        for (Thread thread : threads){
            thread.join();
        }

        System.out.println("********* end of simulation *********************");
    }

    public void stop() {

    }

}
