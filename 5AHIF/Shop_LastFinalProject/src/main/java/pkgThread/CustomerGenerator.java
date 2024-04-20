package pkgThread;

import pkgMisc.*;
import pkgSubjects.Lane;

import java.time.LocalTime;
import java.util.ArrayList;

public class CustomerGenerator extends Thread implements AnimationParameters {
    private boolean running = true;
    private ArrayList<MessageEventListener> msgListeners = new ArrayList<>();
    private ArrayList<AnimationEventListener> animListeners = new ArrayList<>();

    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Lane> lanes;
    private long timeBetweenCars;

    public CustomerGenerator(int numCustomersPerMinute, long timeBetweenCars, float percentPassenger, float percentFirebrigade, ArrayList<Lane> lanes) {
        this.lanes = lanes;
        this.timeBetweenCars = timeBetweenCars;

        double yOffset = Y_COO;
        for (int i = 0; i < numCustomersPerMinute; i++) {
            float rand = (float)Math.random();
            if (rand >= 0 && rand < percentPassenger) {
                customers.add(new Customer(X_COO, yOffset, Customer.TYPE.PASSENGER, "passenger-" + String.valueOf(i), lanes));
            } else if (rand >= percentPassenger && rand < percentPassenger + percentFirebrigade) {
                customers.add(new Customer(X_COO, yOffset, Customer.TYPE.FIREBRIGADE, "firebrigade-" + String.valueOf(i), lanes));
            } else {
                customers.add(new Customer(X_COO, yOffset, Customer.TYPE.CAR, "car-" + String.valueOf(i), lanes));
            }
            yOffset += HEIGHT;
        }
    }

    public void end() {
        running = false;
    }

    @Override
    public void run() {
        for (Customer car : customers) {
            try {
                car.setMsgListeners(msgListeners);
                car.setAnimListeners(animListeners);
                car.start();
                LocalTime future = LocalTime.now().plusNanos(timeBetweenCars * 1000);
                notifyListeners(
                "=".repeat(30) + " next car in " +
                        future.getHour() + ":" +
                        future.getMinute() + ":" +
                        future.getSecond());
                Thread.sleep(timeBetweenCars);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void notifyListeners(String msg) {
        for (MessageEventListener el : msgListeners) {
            el.handleMessageEvent(new MessageEvent(this, msg));
        }
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void addMsgListener(MessageEventListener l) {
        msgListeners.add(l);
    }
    public void removeMsgListener(MessageEventListener l) {
        msgListeners.remove(l);
    }
    public void addAnimListener(AnimationEventListener l) {
        animListeners.add(l);
    }
    public void removeListener(AnimationEventListener l) {
        animListeners.remove(l);
    }

    public void stopAllCars() {
        this.interrupt();
        for (Customer car : customers) {
            car.interrupt();
        }
    }
}
