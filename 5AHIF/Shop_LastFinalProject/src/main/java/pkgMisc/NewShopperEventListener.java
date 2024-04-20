package pkgMisc;

import java.util.EventListener;

public interface NewShopperEventListener extends EventListener {
    void handleNewShopperEvent(NewShopperEvent event);
}
