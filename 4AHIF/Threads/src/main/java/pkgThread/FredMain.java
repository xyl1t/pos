package pkgThread;

import java.util.ArrayList;
import java.util.List;

public class FredMain {

    private static List<Thread> threads = new ArrayList<>();

    public static void main( String[] args ) throws InterruptedException {
        System.out.println( "Hello I'm fred" );

        startThreads();
        awaitThreads();

        System.out.println( "I (fred) am going home" );
    }

    public static void startThreads() {
        threads.add(new Person("Theo", 2, 4));
        threads.add(new Person("Hugo", 1, 5));
        threads.add(new Person("Franz", 3, 5));
        threads.add(new Person("Gustav", 2, 5));

        threads.add(new Dog("Lucky", 2, 4));
        threads.add(new Dog("Barbarossa", 1, 5));
        threads.add(new Dog("Hund", 3, 5));
        threads.add(new Dog("Lussie", 2, 5));

        for (Thread t : threads) {
            t.start();
        }
    }
    public static void awaitThreads() throws InterruptedException {
        for (Thread t : threads) {
            t.join();
        }
    }
}
