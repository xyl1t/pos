package pkgController;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pkgModel.DatabaseOracle;
import pkgModel.Participant;
import pkgModel.Racer;

public class RacerController implements Initializable {


    @FXML
    private TableColumn<Participant, LocalDate> colRacerDate;

    @FXML
    private TableColumn<Participant, String> colRacerName;

    @FXML
    private TableColumn<Participant, Integer> colRacerRank;

    @FXML
    private TableColumn<Participant, LocalTime> colRacerTime;

    @FXML
    private TableColumn<Participant, String> colRacerType;

    @FXML
    private Label lblMessage;

    @FXML
    private TableView<Participant> tblRacers;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			oradb = DatabaseOracle.getInstance("d3a09", "7Abu#Anas11");

			tblRacers.getItems().clear();
			tblRacers.getItems().addAll(oradb.getRacersRaces(selectedRacer.getRacerId()));
			lblMessage.setText("# of participations: " + tblRacers.getItems().size());
			assignClassAttributesTotableCells();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void assignClassAttributesTotableCells() {
		colRacerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colRacerDate.setCellValueFactory(new PropertyValueFactory<>("date"));
		colRacerTime.setCellValueFactory(new PropertyValueFactory<>("time"));
		colRacerType.setCellValueFactory(new PropertyValueFactory<>("type"));
		colRacerRank.setCellValueFactory(new PropertyValueFactory<>("rank"));
	}

	public static void setRacer(Racer selectedRacer) {
		RacerController.selectedRacer = selectedRacer;
	}

	private DatabaseOracle oradb = null;
	private static Racer selectedRacer = null;
}
