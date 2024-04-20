package pkgMisc;

import java.util.EventListener;

public interface AnimationEventListener extends EventListener {
    void handleAnimationEvent(AnimationEvent event);
}
