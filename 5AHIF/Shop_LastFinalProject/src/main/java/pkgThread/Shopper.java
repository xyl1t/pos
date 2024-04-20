package pkgThread;

import pkgMisc.*;
import pkgSubjects.ShopElement;
import pkgSubjects.ShopSimulator;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Shopper extends Thread {

    private String shopperName;
    private boolean usedEmergencyExit = false;
    private ShopSimulator s;

    private double xPos = -50;
    private double yPos = 200;

    public Shopper(String name, ShopSimulator s) {
        this.shopperName = name;
        this.s = s;
    }

    public Shopper(String shopperName) {
        this.shopperName = shopperName;
    }

    public String getShopperName() {
        return shopperName;
    }

    long shopTime = 0;
    long queueTime = 0;
    long cashTime = 0;

    public long getShopTime() { return shopTime/1000; }
    public long getShoppingTime() { return getShopTime() + getCashTime(); }
    public long getQueueTime() { return queueTime/1000; }
    public long getCashTime() { return cashTime/1000; }

    @Override
    public void run() {
        System.out.println(shopperName + ": arrived to the shop");
        try {
            // enter animation
            notifyNewShopperListeners(this);
            notifyAnimListeners( 100, 300);
            Thread.sleep((long)(1000 + Math.random() * 1000));

            // start shopping
            s.startShopping(this);
            long now = System.currentTimeMillis();
            notifyMsgListeners("started shopping");


            long sleepTime = (long)(s.getShoppingTime() * 1000);
            sleepTime += (long) (Math.random()/2 * sleepTime * (Math.random() < 0.5 ? -1 : 1));

            do {
                long sleep = (long)Math.min(Math.min(sleepTime,1000), 1000 + Math.random() * 2000);
                if (sleepTime - sleep < 1000) {
                    sleep = sleepTime;
                }
                sleepTime -= sleep;

                int randX = ThreadLocalRandom.current().nextInt(-20, 20);
                int randY = ThreadLocalRandom.current().nextInt(-20, 20);

                int anim = (int)(Math.random()*4);
                    if (anim == 0)
                        notifyAnimListeners(170 + randX, 260 + randY);
                    else if (anim == 1)
                        notifyAnimListeners(240 + randX, 220 + randY);
                    else if (anim == 2)
                        notifyAnimListeners(320 + randX, 220 + randY);
                    else if (anim == 3)
                        notifyAnimListeners(400 + randX, 250 + randY);

//                System.out.println("anim = " + anim);

                Thread.sleep(sleep);
            } while (sleepTime > 0);

//            Thread.sleep(sleepTime);


            s.stopShopping(this);
            shopTime = System.currentTimeMillis() - now;
            notifyMsgListeners("finished shopping");

            if (Math.random() < ShopSimulator.EXIT_CHANCE) {
                s.startExitShop(this);
                notifyMsgListeners("is fleeing to the exit");
                this.usedEmergencyExit = true;
                notifyAnimListeners(510, 290);
                Thread.sleep(1000);
                s.stopExitShop(this);
                notifyAnimListeners(850, yPos-350);
            } else {
                long q_now = System.currentTimeMillis();
                notifyMsgListeners("waiting for cash register");
                int randX = ThreadLocalRandom.current().nextInt(-20, 20);
                int randY = ThreadLocalRandom.current().nextInt(-20, 20);
                notifyAnimListeners(320 + randX, 340 + randY);

                Thread.sleep(1000);

                s.startCash(this);
                queueTime = System.currentTimeMillis() - q_now;
                long cashTime_now = System.currentTimeMillis();
                notifyMsgListeners("started cashing out");
                notifyAnimListeners(270, 380);

                Thread.sleep((long) (s.getCashingTime() * 1000) + (long)(s.getCashingTime() * 0.5 * (Math.random() < 0.5 ? -1 : 1)));
                cashTime = System.currentTimeMillis() - cashTime_now;
                s.stopCash(this);

                notifyMsgListeners("left the shop");
                notifyAnimListeners(-50, yPos);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<MessageEventListener> msgListeners = new ArrayList<>();
    private ArrayList<AnimationEventListener> animListeners = new ArrayList<>();
    private ArrayList<NewShopperEventListener> newShopperListeners = new ArrayList<>();

    public void setMsgListeners(ArrayList<MessageEventListener> msgListeners) {
        this.msgListeners = msgListeners;
    }
    public void setAnimListeners(ArrayList<AnimationEventListener> animListeners) { this.animListeners = animListeners; }
    public void setNewShopperListeners(ArrayList<NewShopperEventListener> newShopperListeners) { this.newShopperListeners = newShopperListeners; }

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
    public void addNewShopperListener(NewShopperEventListener l) { newShopperListeners.add(l); }
    public void removeNewShopperListener(NewShopperEventListener l) { newShopperListeners.remove(l); }


    private void notifyMsgListeners(String msg) {
        for (MessageEventListener el : msgListeners) {
            el.handleMessageEvent(new MessageEvent(this,DateTimeFormatter.ofPattern("HH:mm:ss").format(java.time.LocalTime.now()) + " " + shopperName + ": " + msg));
        }
    }

    private void notifyAnimListeners(double newX, double newY) {
        if (newX == xPos && newY == yPos) return;

        for (AnimationEventListener el : animListeners) {
            el.handleAnimationEvent(new AnimationEvent(this, xPos, yPos, newX, newY));
        }
        xPos = newX;
        yPos = newY;
    }

    private void notifyNewShopperListeners(Shopper shopper) {
        for (NewShopperEventListener el : newShopperListeners) {
            el.handleNewShopperEvent(new NewShopperEvent(this, shopper));
        }
    }
    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public boolean usedEmergencyExit() {
        return usedEmergencyExit;
    }
}
