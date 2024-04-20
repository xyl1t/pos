package pkgController;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import pkgSubjects.Pupil;


public class MainController implements Initializable  {
	@FXML
	private Button btn_start;

	@FXML
	private Button btn_stopNext;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pupils.add(new Pupil("Adam", 1000));
		pupils.add(new Pupil("Marat", 2000));
		pupils.add(new Pupil("Gustav", 3000));
	}

	@FXML
	void onBtnClick(ActionEvent event) {
		if (event.getSource().equals(btn_start)) {
			for (Pupil p : pupils) {
				p.start();
			}
		} else {
			pupils.get(nextPupilIdx++).end();
		}
	}

	private int nextPupilIdx = 0;
	private ArrayList<Pupil> pupils = new ArrayList<>();
}

