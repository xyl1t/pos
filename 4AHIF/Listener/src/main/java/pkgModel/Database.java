package pkgModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import pkgController.ListenerController;
import pkgMisc.*;
import pkgMisc.EventDBChanged.EVENTDBTYPE;

public class Database {
	private static Database db = null;
	private ArrayList<Song> songs = new ArrayList<>();
	// private ArrayList<EventDBChangedListener> listeners = new ArrayList<>();
	// private ArrayList<EventDBChangedListener> updateListeners = new ArrayList<>();
    private final PropertyChangeSupport supportPropertySongTitleListener;
    private final PropertyChangeSupport supportPropertySongSingerListener;

	public static Database getInstance() {
        if (db == null) {
            db = new Database();
        }

        return db;
    }

	private Database() {
        super();
        supportPropertySongTitleListener  = new PropertyChangeSupport(this);
        supportPropertySongSingerListener = new PropertyChangeSupport(this);
    }

//	public void addListener(EventDBChangedListener l) {
//		listeners.add(l);
//	}
//
//	public void addUpdateListener(ListenerController l) {
//		updateListeners.add(l);
//	}
//
//	private void notifyListeners(EVENTDBTYPE type) {
//		for (EventDBChangedListener el : listeners) {
//			el.handleEventDBChanged(new EventDBChanged(this, type));
//		}
//	}
//
//	private void notifyUpdateListeners(Song old, Song newSong) {
//		for (EventDBChangedListener el : updateListeners) {
//			el.handleEventDBChanged(new EventDBChanged(this, EVENTDBTYPE.UPDATE, old, newSong));
//		}
//	}

	public void insert(Song s) {
		songs.add(s);
        supportPropertySongTitleListener.firePropertyChange("Song-Title", null, s.getName());
        supportPropertySongSingerListener.firePropertyChange("Song-Singer", null, s.getAuthor());
		
		// notifyListeners(EVENTDBTYPE.INSERT);
	}

//	public void deleteFirst() throws Exception {
//		if (songs.size() > 0) {
//			songs.remove(0);
//			
//			notifyListeners(EVENTDBTYPE.DELETE);
//		} else {
//			throw new Exception("nothing to delete");
//		}
//	}
//
//	public void updateFirst(Song song) throws Exception {
//		if (songs.size() > 0) {
//			Song oldSong = songs.get(0);
//			songs.remove(0);
//			songs.add(0, song);
//			Song newSong = songs.get(0);
//
//			notifyUpdateListeners(oldSong, newSong);
//		} else {
//			throw new Exception("nothing to delete");
//		}
//	}

	public List<Song> getAllSongs() {
		return songs;
	}

    public void deleteFirstSong() throws Exception {
        Song s;
        if (songs.size() > 0) {
            s = songs.get(0);
            songs.remove(0);
            supportPropertySongTitleListener.firePropertyChange("Song-Title", s.getName(), null);
            supportPropertySongSingerListener.firePropertyChange("Song-Singer", s.getAuthor(), null);
        } else {
            throw new Exception("nothing to delete");
        }
    }

    public void addPropertySongTitleListener(PropertyChangeListener pcl) {
        supportPropertySongTitleListener.addPropertyChangeListener(pcl);
    }

    public void addPropertySongSingerListener(PropertyChangeListener pcl) {
        supportPropertySongSingerListener.addPropertyChangeListener(pcl);
    }
}
