package pkgMisc;

import java.util.EventListener;

public interface MessageEventListener extends EventListener {
    void handleMessageEvent(MessageEvent event);
}
