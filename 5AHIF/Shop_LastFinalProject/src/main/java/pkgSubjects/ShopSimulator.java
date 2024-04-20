package pkgSubjects;

import pkgMisc.AnimationEventListener;
import pkgMisc.MessageEventListener;
import pkgMisc.NewShopperEventListener;
import pkgThread.Shopper;

import java.util.ArrayList;

public class ShopSimulator extends Thread {
    private int customersPerMinute;
    private double shoppingTime;
    private double cashingTime;
    public static double EXIT_CHANCE = 0.1;
    private ArrayList<ShopElement> shopElements = new ArrayList<>();
    private ArrayList<Shopper> shoppers = new ArrayList<>();

    private ArrayList<MessageEventListener> msgListeners = new ArrayList<>();
    private ArrayList<AnimationEventListener> animListeners = new ArrayList<>();
    private ArrayList<NewShopperEventListener> newShopperListeners = new ArrayList<>();


    private Shelf shelf = new Shelf("shelf", 999999);
    private CashRegister cashRegister = new CashRegister("cash register");
    private Exit exit = new Exit();
    private long cash_start;
    private long cash_total;
    private double utilization;
    private long simulation_start;
    private long lastSystemTime;

    public ShopSimulator(int customersPerMinute, double shoppingTime, double cashingTime, ArrayList<ShopElement> shopElements) {
        this.customersPerMinute = customersPerMinute;
        this.shoppingTime = shoppingTime;
        this.cashingTime = cashingTime;
        this.shopElements = shopElements;
    }

//    public void setCashRegister(CashRegister cashRegister) {
//        this.cashRegister = cashRegister;
//    }
//
//    public void setExit(Exit exit) {
//        this.exit = exit;
//    }


    private int count = 0;
    private int usedEmergencyExitCount = 0;
    private double avgShoppingTime = 0;

    public double getAvgShoppingTime() {
        return avgShoppingTime;
    }

    public double getAvgCashingTime() {
        return avgCashingTime;
    }

    public double getAvgQueueingTime() {
        return avgQueueingTime;
    }

    private double avgCashingTime = 0;
    private double avgQueueingTime = 0;
    private boolean running = true;

    @Override
    public void run() {
        shoppers.clear();
        count = 0;
        cash_total = 0;
        simulation_start = System.currentTimeMillis();
        while (running) {
            Shopper shopper = new Shopper("Shopper " + count++, this);
            shopper.setAnimListeners(this.animListeners);
            shopper.setMsgListeners(this.msgListeners);
            shopper.setNewShopperListeners(this.newShopperListeners);
            shoppers.add(shopper);
            shopper.start();

            lastSystemTime = System.currentTimeMillis();

            try {
                Thread.sleep((long) (1000.0 / customersPerMinute * 60));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            for (Shopper shopper : shoppers) {
                shopper.join();
            }
            lastSystemTime = System.currentTimeMillis();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    public void calculateResults() {
        usedEmergencyExitCount = 0;
        long totalShoppingTime = 0;
        long totalCashingTime = 0;
        long totalQueueingTime = 0;
        for (Shopper shopper : shoppers) {
            if (shopper.usedEmergencyExit()) {
                usedEmergencyExitCount++;
            }
            totalShoppingTime += shopper.getShoppingTime();
            totalCashingTime += shopper.getCashTime();
            totalQueueingTime += shopper.getQueueTime();
        }

        avgShoppingTime = (double) totalShoppingTime / shoppers.size();
        avgCashingTime = (double) totalCashingTime / shoppers.size();
        avgQueueingTime = (double) totalQueueingTime / shoppers.size();
        utilization = (double) (cash_total  + (cashRegister.isAvailable() ? 0 : System.currentTimeMillis() - cash_start )) / (lastSystemTime - simulation_start);
    }

    public void end() {
        running = false;
    }

    public int numGeneratedCustomers() {
        return count;
    }

    public int numExitUsers() {
        return usedEmergencyExitCount;
    }

    public void startShopping(Shopper shopper) {
        shelf.addShopper(shopper);
    }

    public void stopShopping(Shopper shopper) {
        shelf.removeShopper(shopper);
    }

    public void startExitShop(Shopper shopper) {
        exit.addShopper(shopper);
    }

    public void stopExitShop(Shopper shopper) {
        exit.removeShopper(shopper);
    }

    public void startCash(Shopper shopper) {
        cashRegister.addShopper(shopper);
        cash_start = System.currentTimeMillis();
    }

    public void stopCash(Shopper shopper) {
        cashRegister.removeShopper(shopper);
        cash_total += System.currentTimeMillis() - cash_start;
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
    public void removeListener(AnimationEventListener l) { animListeners.remove(l); }
    public void addNewShopperListener(NewShopperEventListener l) { newShopperListeners.add(l); }
    public void removeNewShopperListener(NewShopperEventListener l) { newShopperListeners.remove(l); }

    public int getCustomersPerMinute() {
        return customersPerMinute;
    }

    public double getShoppingTime() {
        return shoppingTime;
    }

    public double getCashingTime() {
        return cashingTime;
    }

    public ArrayList<ShopElement> getShopElements() {
        return shopElements;
    }

    public ArrayList<Shopper> getShoppers() {
        return shoppers;
    }

    public int getCount() {
        return count;
    }

    public double getUtilization() {
        return utilization;
    }
}
