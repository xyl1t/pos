package pkgController;

import pkgModel.Rent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import pkgMisc.DateFormatConverter;
import pkgModel.Car;
import pkgModel.Database;
import pkgModel.DatabaseOracle;
import pkgModel.Repair;

public class MainController implements Initializable  {

	@FXML
	private DatePicker dateRepairDate;

	@FXML
	private Label lblMessage;

	@FXML
	private MenuItem mitemAddCar;

	@FXML
	private MenuItem mitemAddRepair;

	@FXML
	private MenuItem mitemDeleteCar;

	@FXML
	private MenuItem mitemDeleteRepair;

	@FXML
	private MenuItem mitemConnection;

	@FXML
	private MenuItem mitemLoadOracle;

	@FXML
	private MenuItem mitemStoreJson;

	@FXML
	private MenuItem mitemUpdateCar;

	@FXML
	private MenuItem mitemUpdateRepair;

	@FXML
	private TextField txtDate;

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtRepairAmount;

	@FXML
	private TextField txtRepairId;

	@FXML
	private TextArea txtRepairText;

	@FXML
	private TableView<Car> tblCars;

	@FXML
	private TableColumn<Car, String> colCarDate;

	@FXML
	private TableColumn<Car, Integer> colCarId;

	@FXML
	private TableColumn<Car, String> colCarName;

	@FXML
	private TableView<Repair> tblRepairs;

	@FXML
	private TableColumn<Repair, BigDecimal> colRepairAmount;

	@FXML
	private TableColumn<Repair, LocalDate> colRepairDate;

	@FXML
	private TableColumn<Repair, Integer> colRepairId;

	@FXML
	private TableColumn<Repair, String> colRepairText;

	@FXML
	private PasswordField txtPasswort;

	@FXML
	private TextField txtUser;

    @FXML
    private ListView<Rent> listRent;

    @FXML
    private MenuItem mitemAddRent;

    @FXML
    private MenuItem mitemDeleteRent;

    @FXML
    private TextField txtRentAmount;

    @FXML
    private TextField txtRentCustomer;

    @FXML
    private DatePicker dateStartRent;

    @FXML
    private DatePicker dateEndRent;

    @FXML
    private ImageView imgCar;

    @FXML
    private MenuItem mitemLoadImage;

    @FXML
    private MenuItem mitemStoreImage;

	@FXML
	private Label lblSumRentings;

	@FXML
	private Label lblSumRepairs;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			db = Database.getInstance();
			dateRepairDate.setPromptText(DateFormatConverter.getPattern());
			dateStartRent.setPromptText(DateFormatConverter.getPattern());
			dateEndRent.setPromptText(DateFormatConverter.getPattern());
			dateRepairDate.setConverter(new DateFormatConverter());
			dateStartRent.setConverter(new DateFormatConverter());
			dateEndRent.setConverter(new DateFormatConverter());
			assignClassAttributesTotableCells();
			
			Locale.setDefault(new Locale("de"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void assignClassAttributesTotableCells() {
		colCarId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colCarName.setCellValueFactory(new PropertyValueFactory<>("name"));
		colCarDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
		colCarId.setStyle("-fx-alignment: CENTER-RIGHT");

		colRepairId.setCellValueFactory(new PropertyValueFactory<>("id"));
		colRepairDate.setCellValueFactory(new PropertyValueFactory<>("dateAsString"));
		colRepairText.setCellValueFactory(new PropertyValueFactory<>("text"));
		colRepairAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
		colCarId.setStyle("-fx-alignment: CENTER-RIGHT");
	}

	private void updateRepairId() throws Exception {
		txtRepairId.setText(String.valueOf(oradb.getNextRepairId()));
	}

	@FXML
	void onMenuFileSelected(ActionEvent event) {
		try {
			if (event.getSource().equals(mitemConnection)) {
				oradb = DatabaseOracle.getInstance(txtUser.getText(), txtPasswort.getText());
				updateRepairId();
				lblMessage.setText("Connection set");
			} else if (event.getSource().equals(mitemStoreJson)) {
				db.saveToJson();
				refreshListView();
				lblMessage.setText("Data stored to " + Database.FILENAME + ".json");
			} else if (event.getSource().equals(mitemLoadOracle)) {
				refreshListView();
//				txtRepairId.setText(Integer.toString(Repair.getRepairId()));
//				lblMessage.setText("data resotred <== json (" + Repair.getRepairId() + " next repair id)");
			}
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}
	}

	@FXML
	void onMenuCarSelected(ActionEvent event) {
		try {
			if (event.getSource().equals(mitemAddCar)) {
				Car car = new Car(txtName.getText(), txtDate.getText());
				Car addedCar = oradb.addCar(car);
				refreshListView();
				lblMessage.setText("car added: " + addedCar);
			} else if (event.getSource().equals(mitemDeleteCar)) {
				oradb.deleteCar(Integer.parseInt(txtId.getText()));
				lblMessage.setText("Car with id " + txtId.getText() + " deleted");
				refreshListView();
			} else if (event.getSource().equals(mitemUpdateCar)) {
				// LocalDate date = LocalDate.parse(txtDate.getText(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
				// oradb.updateCar(Integer.parseInt(txtId.getText()), txtName.getText(), date);
				Car updatedCar = new Car(
						txtId.getText(),
						txtName.getText(),
						txtDate.getText()
					);
				Car oldCar = oradb.updateCar(updatedCar);
				// lblMessage.setText(selectedCar.toString() + " => " + updatedCar.toString());
				lblMessage.setText(oldCar.toString() + " => " + updatedCar.toString());
				refreshListView();
			} else if (event.getSource().equals(mitemLoadImage)) {
				byte arrByte[] = oradb.loadCarImage(selectedCar);
				imgCar.setImage(new Image(new ByteArrayInputStream(arrByte)));
				lblMessage.setText("car image displayed");
			} else if (event.getSource().equals(mitemStoreImage)) {
				FileChooser fch = new FileChooser();
				fch.setTitle("select image file");
				File file = fch.showOpenDialog(lblMessage.getScene().getWindow());
				if (file != null) {
					lblMessage.setText("car image stored..." + file.getAbsolutePath());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}	
	}

	@FXML
	void onMenuRepairSelected(ActionEvent event) {
		try {
			if (event.getSource().equals(mitemAddRepair)) {
				Repair repair = new Repair(Integer.parseInt(txtRepairId.getText()), dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
				int carId = Integer.valueOf(txtId.getText());
				oradb.addRepair(carId, repair);
				updateRepairId();
//				db.addRepairToCar(txtName.getText(), repair);
//				Repair.incrementRepairId();
//				txtRepairId.setText(Integer.toString(Repair.getRepairId()));
			} else if (event.getSource().equals(mitemUpdateRepair)) {
				Repair repair = new Repair(Integer.parseInt(txtRepairId.getText()), dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
				oradb.updateRepair(repair);
//				if (selectedRepair.getId() == Integer.parseInt(txtRepairId.getText()) && !selectedRepair.getDate().equals(dateRepairDate.getValue())) {
//					throw new Exception("error: updating date is not allowed");
//				}
//				Repair repair = new Repair(dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
//				db.updateRepair(txtName.getText(), repair);
//				refreshRepairListView();
//				lblMessage.setText("repair updated from " + selectedRepair.toString() + " => " + repair.toString());
			} else if (event.getSource().equals(mitemDeleteRepair)) {
				int repairId = Integer.valueOf(txtRepairId.getText());
				oradb.deleteRepair(repairId);
				updateRepairId();
//				db.deleteRepair(txtName.getText(), dateRepairDate.getValue());
//				refreshRepairListView();
			}
			refreshRepairListView();
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}	
	}


	@FXML
	void onCarSelected(MouseEvent event) {
		selectedCar = this.tblCars.getSelectionModel().getSelectedItem();
		if (selectedCar != null) {
			this.txtId.setText(Integer.toString(selectedCar.getId()));
			this.txtName.setText(selectedCar.getName());
			this.txtDate.setText(selectedCar.getDateAsString());
			lblMessage.setText("list selected: " + selectedCar);
			refreshRepairListView();
			refreshRentListView();
		} else {
			lblMessage.setText("no car selected");
		}
	}

	@FXML
	void onRepairSelected(MouseEvent event) {
		selectedRepair = this.tblRepairs.getSelectionModel().getSelectedItem();
		if (selectedRepair != null) {
			this.txtRepairId.setText(String.valueOf(selectedRepair.getId()));
			this.dateRepairDate.setValue(selectedRepair.getDate());
			this.txtRepairText.setText(selectedRepair.getText());
			this.txtRepairAmount.setText(selectedRepair.getAmount().toString());
			lblMessage.setText("repair list selected: " + selectedRepair);
		} else {
			lblMessage.setText("no repair selected");
		}
	}

	private void refreshListView() throws Exception {
		tblCars.getItems().clear();
		tblCars.getItems().addAll(oradb.getAllCars());
	}

	private void refreshRepairListView() {
		tblRepairs.getItems().clear();
		try {
			int carId = Integer.valueOf(txtId.getText());
			tblRepairs.getItems().addAll(oradb.getRepairs(carId));
			lblSumRepairs.setText("Sum of repairs: " + String.valueOf(oradb.getSumRepairs(carId)));
		} catch (Exception e) {
			lblMessage.setText("cannot get repairs of nonexistent car");
		}
	}

	private void refreshRentListView() {
		listRent.getItems().clear();
		try {
			int carId = Integer.parseInt(txtId.getText());
			// TreeMap<LocalDate, Rent> myMap = oradb.getRents(carId);
			// ArrayList<String> coll = new ArrayList<>();
			// for (Map.Entry<LocalDate, Rent> entry : myMap.entrySet()) {
			// 	System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
			// 	coll.add(entry.getValue().toString());
			// }

			Collection<Rent> myMap = oradb.getRents(carId);
			listRent.getItems().addAll(myMap);
			lblSumRentings.setText("Sum of rents: " + String.valueOf(oradb.getSumRents(carId)));
		} catch (Exception e) {
			lblMessage.setText("cannot get rents of nonexistent car: " + e.getMessage());
		}
	}

    @FXML
    void onRentSelected(MouseEvent event) {
		selectedRent = this.listRent.getSelectionModel().getSelectedItem();
		if (selectedRent != null) {
			this.txtRentCustomer.setText(selectedRent.getCustomer());
			this.dateStartRent.setValue(selectedRent.getStartDate());
			this.dateEndRent.setValue(selectedRent.getEndDate());
			this.txtRentAmount.setText(String.valueOf(selectedRent.getAmount()));
		} else {
			lblMessage.setText("no rent selected");
		}
    }

    @FXML
    void onMenuRentSelected(ActionEvent event) {
		try {
			if (event.getSource().equals(mitemAddRent)) {
				Rent newRent = new Rent(
					txtRentCustomer.getText(),
					dateStartRent.getValue(),
					dateEndRent.getValue(),
					Double.valueOf(txtRentAmount.getText())
				);
				oradb.addRent(Integer.valueOf(txtId.getText()), newRent);
			} else if (event.getSource().equals(mitemDeleteRent)) {
				oradb.deleteRent(dateStartRent.getValue());
			}
			refreshRentListView();
		} catch(Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}
	}

	/*
	 * non GUI components
	 */
	private Database db = null;
	private DatabaseOracle oradb = null;
	private Car selectedCar = null;
	private Repair selectedRepair = null;
	private Rent selectedRent = null;
}
