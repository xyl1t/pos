package pkgModel;

import java.util.ArrayList;
import java.util.List;

import pkgController.ListenerController;
import pkgMisc.*;
import pkgMisc.EventDBChanged.EVENTDBTYPE;

public class Database {
	private static Database db = null;
	private ArrayList<Song> songs = new ArrayList<>();
	private ArrayList<EventDBChangedListener> listeners = new ArrayList<>();
	private ArrayList<EventDBChangedListener> updateListeners = new ArrayList<>();

	public static Database getInstance() throws Exception {
		if(db == null) 
			db = new Database();

		return db;
	}

	private Database() { super(); }

	public void addListener(EventDBChangedListener l) {
		listeners.add(l);
	}

	public void addUpdateListener(ListenerController l) {
		updateListeners.add(l);
	}


	private void notifyListeners(EVENTDBTYPE type) {
		for (EventDBChangedListener el : listeners) {
			el.handleEventDBChanged(new EventDBChanged(this, type));
		}
	}

	private void notifyUpdateListeners(Song old, Song newSong) {
		for (EventDBChangedListener el : updateListeners) {
			el.handleEventDBChanged(new EventDBChanged(this, EVENTDBTYPE.UPDATE, old, newSong));
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

	public void updateFirst(Song song) throws Exception {
		if (songs.size() > 0) {
			Song oldSong = songs.get(0);
			songs.remove(0);
			songs.add(0, song);
			Song newSong = songs.get(0);

			notifyUpdateListeners(oldSong, newSong);
		} else {
			throw new Exception("nothing to delete");
		}
	}

	public List<Song> getAllSongs() {
		return songs;
	}
}