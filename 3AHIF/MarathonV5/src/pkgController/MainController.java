package pkgController;

import java.io.File;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import pkgMisc.DateFormatConverter;
import pkgModel.DatabaseOracle;
import pkgModel.Racer;
import pkgModel.Race;

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
    private ChoiceBox<Race> cbRace;

    @FXML
    private MenuItem mitemRacerAdd;

    @FXML
    private MenuItem mitemRacerDelete;

    @FXML
    private MenuItem mitemRacerUpdate;

    @FXML
    private MenuItem mitemRacerStoreImage;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			assignClassAttributesTotableCells();
			oradb = DatabaseOracle.getInstance("d3a09", "7Abu#Anas11");
			
			cbRace.setItems(FXCollections.observableArrayList(oradb.getAllRaces()));
			cbRace.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			    if (newSelection != null) {
			    	selectedRace = newSelection;
					tblRacers.getItems().clear();
					try {
						tblRacers.getItems().addAll(oradb.getRacersFromRace(selectedRace.getId()));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					lblMessage.setText("no racer selected");
				}
			});
			cbRace.getSelectionModel().selectFirst();

			
			cbType.setItems(FXCollections.observableArrayList("Full", "Half", "Quarter", "Undefined"));
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
		tblRacers.getItems().addAll(oradb.getRacersFromRace(selectedRace.getId()));
    }

    @FXML
    void onMenuRacerClick(ActionEvent event) {
    	try {
			if (event.getSource().equals(mitemRacerAdd)) {
				// oradb.addRacer(getRacerFromInput());
				oradb.addParticipantToRace(getRacerFromInput(), selectedRace);
				lblMessage.setText("assign " + getRacerFromInput().toString() + " => " + selectedRace.toString());
			} else if (event.getSource().equals(mitemRacerDelete)) {
				// oradb.deleteRacer(selectedRacer);
				oradb.removeParticipantFromRace(getRacerFromInput(), selectedRace);
				lblMessage.setText("removing " + getRacerFromInput().toString() + " from " + selectedRace.toString());
			} else if (event.getSource().equals(mitemRacerUpdate)) {
				oradb.updateRacer(selectedRacer, getRacerFromInput());
				lblMessage.setText("update " + getRacerFromInput().toString());
			} else if (event.getSource().equals(mitemRacerStoreImage)) {
				FileChooser fch = new FileChooser();
				fch.setTitle("select image file");
				File file = fch.showOpenDialog(lblMessage.getScene().getWindow());
				if (file != null) {
					oradb.storeRacerImage(selectedRacer, file.getAbsolutePath());
					lblMessage.setText("Racer image stored..." + file.getAbsolutePath());
				}
			}
			refreshRacersTable();
    	} catch (Exception e) {
    		e.printStackTrace();
    		lblMessage.setText(e.getMessage());
    	}
    }

    @FXML
    void onRacerSelected(MouseEvent event) throws IOException {
    	if (event.getClickCount() == 2) {
    		
    		RacerController.setRacer(selectedRacer);
            lblMessage.setText("racer " + this.selectedRacer.getName() + " should be shown");
            Stage stage = new Stage();
            Parent rootframe = FXMLLoader.load(getClass().getResource("/pkgView/RacerGui.fxml"));
            Scene scene = new Scene(rootframe);

            stage.setScene(scene);
            stage.setTitle("Raceparticipatipons of " + this.selectedRacer.getName());
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            
            
    	}
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
			selectedRacer.getId(),
			txtName.getText(),
			ckIsMale.isSelected(),
			cbType.getSelectionModel().getSelectedItem(),
			LocalTime.parse(txtTime.getText()),
			null
		);
    }


    private Racer selectedRacer = null;
    private Race selectedRace = null;
//    private int selectedRaceId = 0;
	private DatabaseOracle oradb = null;
}
