package pkgSubjects;

import pkgThread.Shopper;

public interface ShopElement {
    void addShopper(Shopper shopper);
    void removeShopper(Shopper shopper);

    String getName();
}
