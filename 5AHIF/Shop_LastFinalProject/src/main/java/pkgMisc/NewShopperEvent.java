package pkgMisc;

import pkgThread.Shopper;

import java.util.EventObject;

public class NewShopperEvent extends EventObject {
    private Shopper newShopper;
    public NewShopperEvent(Object source, Shopper newShopper) {
        super(source);
        this.newShopper = newShopper;
    }

    public Shopper getNewShopper() {
        return newShopper;
    }
}
