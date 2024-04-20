package pkgController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import pkgMisc.EventDBChanged;
import pkgMisc.EventDBChanged.EVENTDBTYPE;
import pkgMisc.EventDBChangedListener;
import pkgMisc.EventMouseStateChanged;
import pkgMisc.EventMouseStateChangedListener;
import pkgModel.Database;
import pkgModel.Song;

public class ListenerController implements Initializable, EventDBChangedListener  {

    @FXML
    private Label lblMessage;

    @FXML
    private Button btnSubToSingerUpdate;

    @FXML
    private ListView<Song> lstSongs;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			db = Database.getInstance();
			db.addListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @FXML
    void OnMouseDrag(MouseEvent event) {
    	this.notifyListeners(EventMouseStateChanged.EVENTMOUSETYPE.DRAGGED);
    	lblMessage.setText("drag done");
    }

    @FXML
    void OnMouseExit(MouseEvent event) {
    	this.notifyListeners(EventMouseStateChanged.EVENTMOUSETYPE.EXITED);
    	lblMessage.setText("exit done");
    }

    @FXML
    void onBtnClick(ActionEvent event) {
    	db.addUpdateListener(this);
    }

	private Database db;

	@Override
	public void handleEventDBChanged(EventDBChanged event) {
		if (event.getEventDBtype() == EVENTDBTYPE.UPDATE) {
			Song oldSong = event.getOldSong();
			Song newSong = event.getNewSong();
			boolean nameChanged = !oldSong.getName().equals(newSong.getName());
			boolean authorChanged = !oldSong.getAuthor().equals(newSong.getAuthor());
			String msg = "";
			if (nameChanged) {
				msg += "Title changed: " + oldSong.getName() + " => " + newSong.getName();
			}
			if (authorChanged) {
				msg += (nameChanged ? ", " : "") + "Author changed: " + oldSong.getAuthor() + " => " + newSong.getAuthor();
			}
			lblMessage.setText(msg);
		} else {
			lblMessage.setText("received db changed " + event.getEventDBtype() + "; " + event.getSource());
		}
		this.lstSongs.getItems().clear();
		lstSongs.getItems().addAll(db.getAllSongs());
	}
	
	public String getWindowTitle() {
		Stage stage = (Stage)lblMessage.getScene().getWindow();
		return stage.getTitle();
	}
	
    private void notifyListeners(EventMouseStateChanged.EVENTMOUSETYPE eventdbtype) {
		for (EventMouseStateChangedListener el : collListeners) {
			el.handleMouseUpdated(new EventMouseStateChanged(this, eventdbtype));
		}
    }

	public void addEventMouseStateChangedListener(EventMouseStateChangedListener l) throws Exception {
		collListeners.add(l);
	}

    private final ArrayList<EventMouseStateChangedListener> collListeners = new ArrayList<>();

}
