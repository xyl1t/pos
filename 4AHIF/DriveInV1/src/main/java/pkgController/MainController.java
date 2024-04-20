package pkgController;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pkgSubjects.Lane;
import pkgThread.CarDriverGenerator;
import pkgThread.Watch;


public class MainController implements Initializable  {
	@FXML
	private Button btn_start;

	@FXML
	private Button btn_stop;

	@FXML
	private TextField txt_from;

	@FXML
	private TextField txt_to;

	@FXML
	private TextField txt_updateInterval;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	void onBtnClick(ActionEvent event) {
		if (event.getSource().equals(btn_start)) {
			watch = new Watch(Integer.parseInt(txt_updateInterval.getText()));
			lane = new Lane();
			cdg = new CarDriverGenerator(lane, Integer.parseInt(txt_from.getText()), Integer.parseInt(txt_to.getText())+1);
			watch.start();
			cdg.start();
		} else {
			cdg.end();
			watch.end();
		}
	}

	private Watch watch;
	private Lane lane;
	private CarDriverGenerator cdg;
}
