package pkgController;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pkgModel.Database;
import pkgModel.Song;
import pkgMisc.EventMouseStateChanged;
import pkgMisc.EventMouseStateChangedListener;
import javafx.fxml.FXML;

public class MainController implements Initializable, EventMouseStateChangedListener {
    @FXML
    private MenuItem mitemApplCreateListener;

    @FXML
    private MenuItem mitemSongDeleteFirst;

    @FXML
    private MenuItem mitemSongInsert;

    @FXML
    private TextField txtAuthor;

    @FXML
    private TextField txtName;

    @FXML
    private Label lblMessage;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			db = Database.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    @FXML
    void onApplMenuClick(ActionEvent event) {
		try {
			if (event.getSource().equals(mitemApplCreateListener)) {
				
				Stage stage = new Stage();    
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/pkgView/ListenerGui.fxml"));
				Parent rootframe = loader.load();
				
				ListenerController ctrl = loader.getController();
					ctrl.addEventMouseStateChangedListener(this);
				
				Scene scene = new Scene(rootframe);    
											 
				stage.setScene(scene);        
				stage.setTitle("Listener of Songs #" + listenerCounter++);    
				stage.initModality(Modality.NONE);    
				stage.show();

				lblMessage.setText("listener created #" + (listenerCounter - 1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @FXML
    void onSongMenuClick(ActionEvent event) throws Exception {
    	if (event.getSource().equals(mitemSongInsert)) {
    		db.insert(new Song(txtName.getText(), txtAuthor.getText()));
    	} else if (event.getSource().equals(mitemSongDeleteFirst)) {
    		db.deleteFirst();
    	}
    }


    private int listenerCounter = 1;
    private Database db;

	@Override
	public void handleMouseUpdated(EventMouseStateChanged event) {
		ListenerController ctrl = null;
		if (event.getEventSource() instanceof ListenerController) {
			ctrl = (ListenerController)event.getEventSource();
		}
		lblMessage.setText("event: " + ctrl==null? " " : ctrl.getWindowTitle() + " => " + event.getEventType());
		
	}
}
