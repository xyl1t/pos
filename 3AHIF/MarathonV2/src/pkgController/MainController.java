package pkgController;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import pkgMisc.DateFormatConverter;
import pkgModel.DatabaseOracle;
import pkgModel.Racer;

public class MainController implements Initializable  {

    @FXML
    private Button btnSearch;

    @FXML
    private CheckBox ckIsMale;

    @FXML
    private TableView<Racer> tblRacers;

    @FXML
    private TableColumn<Racer, Integer> colRacerId;

    @FXML
    private TableColumn<Racer, String> colRacerName;

    @FXML
    private TableColumn<Racer, LocalTime> colRacerTime;

    @FXML
    private TableColumn<Racer, String> colRacerType;

    @FXML
    private TableColumn<Racer, Boolean> colRacerSex;

    @FXML
    private Label lblMessage;

    @FXML
    private TextField txtFilter;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtTime;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private ChoiceBox<String> cbRace;

    @FXML
    private MenuItem mitemRacerAdd;

    @FXML
    private MenuItem mitemRacerDelete;

    @FXML
    private MenuItem mitemRacerUpdate;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			oradb = DatabaseOracle.getInstance("d3a09", "7Abu#Anas11");
			assignClassAttributesTotableCells();
			
			cbRace.setItems(FXCollections.observableArrayList(oradb.getAllRaces()));
			cbRace.getSelectionModel().selectFirst();
			cbRace.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			    if (newSelection != null) {
					String selectedRace = newSelection;
					if (!selectedRace.isEmpty()) {
						String[] splits = selectedRace.split(",");
						if (splits.length > 0) {
							selectedRaceId= Integer.parseInt(splits[0]);
						}
					}
				} else {
					lblMessage.setText("no racer selected");
				}
			});
			cbRace.getSelectionModel().selectFirst();

			
			cbType.setItems(FXCollections.observableArrayList("Full", "Half", "Quarter"));
			cbType.getSelectionModel().selectFirst();

			tblRacers.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			    if (newSelection != null) {
					selectedRacer = this.tblRacers.getSelectionModel().getSelectedItem();
					if (selectedRacer != null) {
						txtName.setText(selectedRacer.getName());
						txtTime.setText(selectedRacer.getTime().toString());
						cbType.getSelectionModel().select(selectedRacer.getType());
						ckIsMale.setSelected(selectedRacer.isMale());
					} else {
						lblMessage.setText("no racer selected");
					}
			    }
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void assignClassAttributesTotableCells() {
		colRacerId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colRacerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colRacerTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		colRacerType.setCellValueFactory(new PropertyValueFactory<>("type"));
		colRacerSex.setCellValueFactory(c -> new SimpleBooleanProperty(c.getValue().isMale()));
		colRacerSex.setCellFactory(tc -> new CheckBoxTableCell<>());
	}

    @FXML
    void onSearchClick(ActionEvent event) {
    	try {
    		refreshRacersTable();
    	} catch (Exception e) {
    		e.printStackTrace();
    		lblMessage.setText(e.getMessage());
    	}
    }

    private void refreshRacersTable() throws Exception {
		tblRacers.getItems().clear();
		tblRacers.getItems().addAll(oradb.getRacers(txtFilter.getText(), selectedRaceId));
    }


    @FXML
    void onMenuRacerClick(ActionEvent event) {
    	try {
			if (event.getSource().equals(mitemRacerAdd)) {
				oradb.addRacer(getRacerFromInput());
			} else if (event.getSource().equals(mitemRacerDelete)) {
				oradb.deleteRacer(selectedRacer);
			} else if (event.getSource().equals(mitemRacerUpdate)) {
				oradb.updateRacer(selectedRacer, getRacerFromInput());
			}
    		refreshRacersTable();
    	} catch (Exception e) {
    		e.printStackTrace();
    		lblMessage.setText(e.getMessage());
    	}
    }

    @FXML
    void onRacerSelected(MouseEvent event) {
//		selectedRacer = this.tblRacers.getSelectionModel().getSelectedItem();
//		if (selectedRacer != null) {
//			txtName.setText(selectedRacer.getName());
//			txtTime.setText(selectedRacer.getTime().toString());
//			cbType.getSelectionModel().select(selectedRacer.getType());
//			ckIsMale.setSelected(selectedRacer.isMale());
//		} else {
//			lblMessage.setText("no racer selected");
//		}
    }


    private Racer getRacerFromInput() {
    	return new Racer(
			0,
			selectedRaceId,
			txtName.getText(),
			ckIsMale.isSelected(),
			cbType.getSelectionModel().getSelectedItem(),
			LocalTime.parse(txtTime.getText())
		);
    }


    private Racer selectedRacer = null;
    private int selectedRaceId = 0;
	private DatabaseOracle oradb = null;
}
