package pkgController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pkgMisc.DateFormatConverter;
import pkgModel.Car;
import pkgModel.Database;
import pkgModel.Repair;

public class MainController implements Initializable  {


    @FXML
    private DatePicker dateRepairDate;

    @FXML
    private Label lblMessage;

    @FXML
    private ListView<Car> listCars;

    @FXML
    private ListView<Repair> listRepairs;

    @FXML
    private MenuItem mitemAddCar;

    @FXML
    private MenuItem mitemAddRepair;

    @FXML
    private MenuItem mitemDeleteCar;

    @FXML
    private MenuItem mitemDeleteRepair;

    @FXML
    private MenuItem mitemLoadBin;

    @FXML
    private MenuItem mitemLoadJson;

    @FXML
    private MenuItem mitemStoreBin;

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
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
    		db = new Database();
			dateRepairDate.setPromptText(DateFormatConverter.getPattern());
			dateRepairDate.setConverter(new DateFormatConverter());
    	} catch (Exception e) {
			e.printStackTrace();    		
		}
	}

    @FXML
    void onMenuFileSelected(ActionEvent event) {
    	try {
    		if (event.getSource().equals(mitemStoreBin)) {
    			db.save();
				lblMessage.setText("Saved to " + Database.FILENAME + " rep id " + Repair.getRepairId());
    		} else if (event.getSource().equals(mitemLoadBin)) {
    			db.load();
				lblMessage.setText("Loaded from " + Database.FILENAME);
				refreshListView();
				txtRepairId.setText(Integer.toString(Repair.getRepairId()));
    		} else if (event.getSource().equals(mitemStoreJson)) {
    			db.saveToJson();
				refreshListView();
				lblMessage.setText("Data stored to " + Database.FILENAME + ".json");
    		} else if (event.getSource().equals(mitemLoadJson)) {
    			db.loadFromJson();
				refreshListView();
				txtRepairId.setText(Integer.toString(Repair.getRepairId()));
				lblMessage.setText("data resotred <== json (" + Repair.getRepairId() + " next repair id)");
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
	        	Car car = new Car(txtId.getText(), txtName.getText(), txtDate.getText());	        	
				db.addCar(car);
				refreshListView();
				lblMessage.setText("car added: " + car);
    		} else if (event.getSource().equals(mitemDeleteCar)) {
    			db.deleteCar(txtName.getText());
				refreshListView();
    		} else if (event.getSource().equals(mitemUpdateCar)) {
				db.update(txtName.getText(), txtId.getText(), txtDate.getText());
				refreshListView();
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
				Repair repair = new Repair(dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
				db.addRepairToCar(txtName.getText(), repair);
				Repair.incrementRepairId();
				txtRepairId.setText(Integer.toString(Repair.getRepairId()));
				refreshRepairListView();
			} else if (event.getSource().equals(mitemUpdateRepair)) {
				if (selectedRepair.getId() == Integer.parseInt(txtRepairId.getText()) && !selectedRepair.getDate().equals(dateRepairDate.getValue())) {
					throw new Exception("error: updating date is not allowed");
				}
				Repair repair = new Repair(dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
				db.updateRepair(txtName.getText(), repair);
				refreshRepairListView();
				// TODO: impelemnt
				lblMessage.setText("repair updated from " + selectedRepair.toString() + " => " + repair.toString());
			} else if (event.getSource().equals(mitemDeleteRepair)) {
				db.deleteRepair(txtName.getText(), dateRepairDate.getValue());
				refreshRepairListView();

			}
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}	
    }


    @FXML
    void onListEntrySelected(MouseEvent event) {
    	selectedCar = this.listCars.getSelectionModel().getSelectedItem();
    	if (selectedCar != null) {
    		this.txtId.setText(Integer.toString(selectedCar.getId()));
    		this.txtName.setText(selectedCar.getName());
    		this.txtDate.setText(selectedCar.getDateAsString());
        	lblMessage.setText("list selected: " + selectedCar);    		
			refreshRepairListView();
    	} else {
        	lblMessage.setText("no car selected");    		    	
    	}
    }

    @FXML
    void onRepairListEntrySelected(MouseEvent event) {
    	selectedRepair = this.listRepairs.getSelectionModel().getSelectedItem();
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

    private void refreshListView() {
    	listCars.getItems().clear();
    	listCars.getItems().addAll(db.getCars());
    }

    private void refreshRepairListView() {
    	listRepairs.getItems().clear();
    	try {
			listRepairs.getItems().addAll(db.getRepairs(txtName.getText()));
		} catch (Exception e) {
			lblMessage.setText("cannot get repairs of nonexistent car");
		}
    }

	/*
     * non GUI components
     */
    private Database db = null;
    private Car selectedCar = null;
    private Repair selectedRepair = null;
}
