package pkgMisc;

import java.util.EventListener;

public interface EventMouseStateChangedListener extends EventListener {
	void handleMouseUpdated(EventMouseStateChanged event);
}
