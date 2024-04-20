package pkgMisc;

import java.util.EventListener;

public interface UpdateResultsEventListener extends EventListener {
    void handleUpdateResultsEvent(UpdateResultsEvent event);
}
