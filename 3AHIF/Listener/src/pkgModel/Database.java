package pkgModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import pkgMisc.*;
import pkgMisc.EventDBChanged.EVENTDBTYPE;

public class Database {
	private static Database db = null;
	private ArrayList<Song> songs = new ArrayList<>();
	private ArrayList<EventDBChangedListener> listeners = new ArrayList<>();

	public static Database getInstance() throws Exception {
		if(db == null) 
			db = new Database();
		
		return db;
	}
	
	private Database() { super(); }
	
	public void addListener(EventDBChangedListener l) {
		listeners.add(l);
	}

	private void notifyListeners(EVENTDBTYPE type) {
		for (EventDBChangedListener el : listeners) {
			el.handleEventDBChanged(new EventDBChanged(this, type));
		}
	}

	public void insert(Song s) {
		songs.add(s);
		
		notifyListeners(EVENTDBTYPE.INSERT);
	}

	public void deleteFirst() throws Exception {
		if (songs.size() > 0) {
			songs.remove(0);
			
			notifyListeners(EVENTDBTYPE.DELETE);
		} else {
			throw new Exception("nothing to delete");
		}
	}

	public List<Song> getAllSongs() {
		return songs;
	}
	
}