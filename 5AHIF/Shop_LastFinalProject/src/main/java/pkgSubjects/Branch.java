package pkgSubjects;

import pkgThread.Shopper;

public class Branch implements ShopElement {
    private String name;
    private ShopElement a;
    private ShopElement b;
    private ShopElement chosen;
    private double ratio;

    public Branch(String name, ShopElement a, ShopElement b, double ratio) {
        this.name = name;
        this.a = a;
        this.b = b;
        this.ratio = ratio;
    }

    @Override
    public void addShopper(Shopper shopper) {
        if (Math.random() < ratio) {
            chosen = a;
        } else {
            chosen = b;
        }

        chosen.addShopper(shopper);
    }

    @Override
    public void removeShopper(Shopper shopper) {
        chosen.removeShopper(shopper);
    }

    @Override
    public String getName() {
        return "Branch between " + a.getName() + " and " + b.getName() + " at " + ratio + " ratio";
    }
}
