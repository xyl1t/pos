package pkgController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import pkgModel.Car;
import pkgModel.Database;

public class MainController implements Initializable  {
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;
    @FXML
    private Label lblMessage;
    @FXML
    private ListView<Car> listCars;
    @FXML
    private TextField txtDate;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
    		db = new Database();
    	} catch (Exception e) {
			e.printStackTrace();    		
		}
	}

    @FXML
    void onButtonClick(ActionEvent event) {
    	try {
    		if (event.getSource().equals(btnAdd)) {
	        	Car car = new Car(txtId.getText(), txtName.getText(), txtDate.getText());	        	
				db.addCar(car);
				refreshListView();
				lblMessage.setText("car added: " + car);
    		} else if (event.getSource().equals(btnDelete)) {
    			db.deleteCar(txtName.getText());
				refreshListView();
    		} else if (event.getSource().equals(btnSave)) {
    			db.save(FILENAME);
				lblMessage.setText("Saved to " + FILENAME);
    		} else if (event.getSource().equals(btnLoad)) {
    			db.load(FILENAME);
				lblMessage.setText("Loaded from " + FILENAME);
				refreshListView();
    		} else if (event.getSource().equals(btnUpdate)) {
				db.update(txtName.getText(), txtId.getText(), txtDate.getText());
				refreshListView();
    		}
		} catch (Exception e) {
			e.printStackTrace();
			lblMessage.setText("Error: " + e.getMessage());
		}	
    	
       	//lblMessage.setText("button clicked");
    }

    @FXML
    void onListEntrySelected(MouseEvent event) {
    	Car selectedCar = this.listCars.getSelectionModel().getSelectedItem();
    	if (selectedCar != null) {
    		this.txtId.setText(Integer.toString(selectedCar.getId()));
    		this.txtName.setText(selectedCar.getName());
    		this.txtDate.setText(selectedCar.getDateAsString());
        	lblMessage.setText("list selected: " + selectedCar);    		
    	} else {
        	lblMessage.setText("no car selected");    		    	
    	}
    }
    
    private void refreshListView() {
    	listCars.getItems().clear();
    	listCars.getItems().addAll(db.getCars());
    }

	/*
     * non GUI components
     */
    private Database db = null;
    private final String FILENAME = "db.bin";
}
