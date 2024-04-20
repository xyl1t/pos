package pkgMisc;

import java.util.*;

import pkgModel.Song;

public class EventDBChanged extends EventObject {
	public enum EVENTDBTYPE {INSERT, DELETE, UPDATE, UNDEFINED};
	private final EVENTDBTYPE eventDBtype;
	private Song oldSong = null;
	private Song newSong = null;
	
	public Song getOldSong() {
		return oldSong;
	}

	public Song getNewSong() {
		return newSong;
	}

	public EventDBChanged(Object source) {
		super(source);
		eventDBtype = EVENTDBTYPE.UNDEFINED;
	}
	
	public EventDBChanged(Object source, EVENTDBTYPE eventDBtype) {
		super(source);
		this.eventDBtype = eventDBtype;
	}

	public EventDBChanged(Object source, EVENTDBTYPE eventDBtype, Song oldSong, Song newSong) {
		this(source, eventDBtype);
		this.oldSong = oldSong;
		this.newSong = newSong;
	}
	
	public EVENTDBTYPE getEventDBtype() {
		return eventDBtype;
	}
}
