package pkgController;

import java.net.URL;
import java.time.LocalDate;
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
    			db.save(FILENAME + ".bin");
				lblMessage.setText("Data stored to " + FILENAME + " next Repair id " + Repair.getNextRepairId());
    		} else if (event.getSource().equals(mitemLoadBin)) {
    			db.load(FILENAME + ".bin");
				lblMessage.setText("Data restored from " + FILENAME + " next Repair id" + Repair.getNextRepairId());
				refreshListView();
				txtRepairId.setText(Integer.toString(Repair.getNextRepairId()));
    		} else if (event.getSource().equals(mitemStoreJson)) {
    			db.saveToJson(FILENAME + ".json");
				refreshListView();
    		} else if (event.getSource().equals(mitemLoadJson)) {
    			db.loadFromJson(FILENAME + ".json");
				refreshListView();
				txtRepairId.setText(Integer.toString(Repair.getNextRepairId()));
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
				db.updateCar(txtName.getText(), txtId.getText(), txtDate.getText());
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
				txtRepairId.setText(Integer.toString(Repair.getNextRepairId()));
				refreshRepairListView();
			} else if (event.getSource().equals(mitemUpdateRepair)) {
				db.updateRepair(txtName.getText(), txtRepairId.getText(), dateRepairDate.getValue(), txtRepairText.getText(), txtRepairAmount.getText());
				refreshRepairListView();
    		} else if (event.getSource().equals(mitemDeleteRepair)) {
    			db.deleteRepair(txtRepairId.getText());
    			refreshRepairListView();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}	
    }
    

    @FXML
    void onListEntrySelected(MouseEvent event) {
    	Car selectedCar = this.listCars.getSelectionModel().getSelectedItem();
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
    	Repair selectedRepair = this.listRepairs.getSelectionModel().getSelectedItem();
    	if(selectedRepair != null) {
    		this.txtRepairId.setText(Integer.toString(selectedRepair.getId()));
    		this.dateRepairDate.setValue(selectedRepair.getDate());
    		this.txtRepairText.setText(selectedRepair.getText());
    		this.txtRepairAmount.setText(String.valueOf(selectedRepair.getAmount()));
    		lblMessage.setText("Repair selected: " + selectedRepair);    	
    		refreshRepairListView();
    	} else {
        	lblMessage.setText("no Repair selected");    		    	
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
    private final String FILENAME = "db";
}
