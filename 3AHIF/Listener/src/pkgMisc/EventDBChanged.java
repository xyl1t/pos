package pkgMisc;

import java.util.*;

public class EventDBChanged extends EventObject {
	public enum EVENTDBTYPE {INSERT, DELETE, UNDEFINED};
	private final EVENTDBTYPE eventDBtype;
	
	
	public EventDBChanged(Object source) {
		super(source);
		eventDBtype = EVENTDBTYPE.UNDEFINED;
	}
	
	public EventDBChanged(Object source, EVENTDBTYPE eventDBtype) {
		super(source);
		this.eventDBtype = eventDBtype;
	}
	
	public EVENTDBTYPE getEventDBtype() {
		return eventDBtype;
	}
}
