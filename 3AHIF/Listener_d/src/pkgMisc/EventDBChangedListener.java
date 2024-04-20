package pkgMisc;

import java.util.EventListener;

public interface EventDBChangedListener extends EventListener {
	void handleEventDBChanged(EventDBChanged event);
}
