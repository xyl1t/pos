package pkgMisc;

import java.util.*;

public class EventMouseStateChanged extends EventObject {
	public enum EVENTMOUSETYPE {DRAGGED, EXITED, UNDEFINED};
	private final EVENTMOUSETYPE eventType;

	public EventMouseStateChanged(Object source) {
		super(source);
		eventType = EVENTMOUSETYPE.UNDEFINED;
	}

	public EventMouseStateChanged(Object source, EVENTMOUSETYPE eventMouseType) {
		super(source);
		this.eventType = eventMouseType;
	}

	public EVENTMOUSETYPE getEventType() {
		return eventType;
	}
	
	public Object getEventSource() {
		return source;
	}

}
