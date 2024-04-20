package pkgMisc;

import java.util.EventObject;

public class MessageEvent extends EventObject {
    private String msg;

    public MessageEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
