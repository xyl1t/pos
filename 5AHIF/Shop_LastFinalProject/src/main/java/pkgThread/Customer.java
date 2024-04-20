package pkgThread;

import pkgMisc.*;
import pkgSubjects.Lane;

import java.util.ArrayList;
import java.util.Objects;

public class Customer extends Thread implements AnimationParameters {

    public enum TYPE {
        CAR,
        FIREBRIGADE,
        PASSENGER,
    }

    private String customerName;
    private ArrayList<Lane> lanes;
    private ArrayList<MessageEventListener> msgListeners = new ArrayList<>();
    private ArrayList<AnimationEventListener> animListeners = new ArrayList<>();
    private TYPE type;
    private double xPos;
    private double yPos;
    private int id;
    private static int idCounter = 0;

    public void setMsgListeners(ArrayList<MessageEventListener> msgListeners) {
        this.msgListeners = msgListeners;
    }
    public void setAnimListeners(ArrayList<AnimationEventListener> animListeners) { this.animListeners = animListeners; }
    public void addMsgListener(MessageEventListener l) {
        msgListeners.add(l);
    }
    public void removeMsgListener(MessageEventListener l) {
        msgListeners.remove(l);
    }
    public void addAnimListener(AnimationEventListener l) {
        animListeners.add(l);
    }
    public void removeAnimListener(AnimationEventListener l) {
        animListeners.remove(l);
    }

    public Customer(double xPos, double yPos, TYPE type, String name, ArrayList<Lane> lanes) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
        this.customerName = name;
        this.lanes = lanes;
        this.id = idCounter++;
    }

    @Override
    public void run() {
        try {
            notifyMsgListeners(customerName + ": hungry => arriving to Meki's DriveIn");
            lanes.get(0).addDriverToLane(this);
            notifyMsgListeners(customerName + ": driving on " + lanes.get(0).getName());
            notifyAnimListeners(xPos, yPos, xPos + CAR_DISTANCE, yPos);
            Thread.sleep(CAR_SLEEP_DURATION);
            xPos += CAR_DISTANCE;
            for (int i = 1; i < lanes.size(); i++) {
                lanes.get(i).addDriverToLane(this);
                notifyMsgListeners(customerName + ": leaving " + lanes.get(i-1).getName());
                lanes.get(i-1).removeDriverFromLane(this);

                notifyMsgListeners(customerName + ": driving on " + lanes.get(i).getName());
                notifyAnimListeners(xPos, yPos, xPos + CAR_DISTANCE, yPos);
                Thread.sleep(CAR_SLEEP_DURATION);
                xPos += CAR_DISTANCE;
            }
            notifyMsgListeners(customerName + ": leaving Meki's DriveIn");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public double getX() {
        return xPos;
    }

    public double getY() {
        return yPos;
    }

    public String getCustomerName() {
        return customerName;
    }

    public TYPE getType() {
        return type;
    }

    public int getCustomerId() {
        return id;
    }

    private void notifyMsgListeners(String msg) {
        for (MessageEventListener el : msgListeners) {
            el.handleMessageEvent(new MessageEvent(this, msg));
        }
    }
    private void notifyAnimListeners(double oldX, double oldY, double newX, double newY) {
        for (AnimationEventListener el : animListeners) {
            el.handleAnimationEvent(new AnimationEvent(this, oldX, oldY, newX, newY));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerName, customer.customerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerName);
    }
}
