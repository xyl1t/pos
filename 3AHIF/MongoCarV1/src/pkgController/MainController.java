package pkgController;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.bson.types.ObjectId;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import pkgModel.Car;
import pkgModel.Database;

public class MainController implements Initializable   {

    @FXML
    private Label lblMessage;

    @FXML
    private ListView<Car> lstCars;

    @FXML
    private MenuItem mitemCarDelete;

    @FXML
    private MenuItem mitemCarFindAll;

    @FXML
    private MenuItem mitemCarFindRelevance;

    @FXML
    private MenuItem mitemCarFindYearHp;

    @FXML
    private MenuItem mitemCarInsert;

    @FXML
    private MenuItem mitemCarReplace;

    @FXML
    private MenuItem mitemCarUpdate;

    @FXML
    private MenuItem mitemDatabaseClose;

    @FXML
    private MenuItem mitemDatabaseConnect;

    @FXML
    private MenuItem mitemDatabaseCreateTextIndex;

    @FXML
    private TextField txtYear;

    @FXML
    private TextArea txtAreaDescription;

    @FXML
    private TextField txtHp;

    @FXML
    private TextField txtIp;

    @FXML
    private TextField txtName;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			
			lstCars.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			    if (newSelection != null) {
					try {
						selectedCar = newSelection;
						txtName.setText(selectedCar.getName());
						txtYear.setText(Integer.toString(selectedCar.getYear()));
						txtHp.setText(Integer.toString(selectedCar.getHp()));
						txtAreaDescription.setText(selectedCar.getDescription());
						
						Car dbCar = db.getCarById(selectedCar.getId());
						lblMessage.setText("Found " + dbCar.toString());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					lblMessage.setText("no car selected");
				}
			});
			
		} catch (Exception e) {
			lblMessage.setText("Fail: " + e.getMessage());
			e.printStackTrace();
		}
	}

    @FXML
    void onMenuItemCarClick(ActionEvent event) {
    	try {
    		if (event.getSource().equals(mitemCarInsert)) {
				ObjectId carId = db.insertCar(new Car(txtName.getText(), Integer.parseInt(txtHp.getText()), Integer.parseInt(txtYear.getText()), txtAreaDescription.getText()));
				lblMessage.setText("Car inserted with id: " + carId);
    		} else if (event.getSource().equals(mitemCarDelete)) {
    			long num = db.deleteCar(selectedCar);
    			lblMessage.setText(num + " car(s) deleted");
    		} else if (event.getSource().equals(mitemCarUpdate)) {
    			Car replacer = new Car(txtName.getText(), Integer.parseInt(txtHp.getText()), Integer.parseInt(txtYear.getText()), txtAreaDescription.getText());
				replacer.setId(selectedCar.getId());
    			long deletedCount = db.updateCar(replacer);
				lblMessage.setText("Updated " + deletedCount + " line(s)");
    		} else if (event.getSource().equals(mitemCarReplace)) {
    			Car replacer = new Car(txtName.getText(), Integer.parseInt(txtHp.getText()), Integer.parseInt(txtYear.getText()), txtAreaDescription.getText());
				replacer.setId(selectedCar.getId());
    			long num = db.replaceCar(replacer);
    			lblMessage.setText(num + " car(s) replaced");
    		} else if (event.getSource().equals(mitemCarFindAll)) {
    			lstCars.getItems().clear();
    			lstCars.getItems().addAll(db.getAllCars());
    		} else if (event.getSource().equals(mitemCarFindYearHp)) {
    			lstCars.getItems().clear();

//		Car replacer = new Car(txtName.getText(), Integer.parseInt(txtHp.getText()), Integer.parseInt(txtYear.getText()), txtAreaDescription.getText());
				ArrayList<Car> filteredCars = db.getCarsByYearHp(txtYear.getText(), txtHp.getText());
    			lstCars.getItems().addAll(filteredCars);
    		} else if (event.getSource().equals(mitemCarFindRelevance)) {
    			lstCars.getItems().clear();
    			ArrayList<Car> cars = db.getAllCarsOrderByRelevance(txtAreaDescription.getText());
    			lstCars.getItems().addAll(cars);
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }

    private Car getCarFromInput() {
		Car replacer = new Car(txtName.getText(), Integer.parseInt(txtHp.getText()), Integer.parseInt(txtYear.getText()), txtAreaDescription.getText());
//		replacer.setId(selectedCar.getId());
		return replacer;
	}

	@FXML
    void onMenuItemDatabaseClick(ActionEvent event) {
    	try {
    		if (event.getSource().equals(mitemDatabaseConnect)) {
				db = Database.getInstance(txtIp.getText());
				lblMessage.setText("connection seems ok: " + db.toString());
    		} else if (event.getSource().equals(mitemDatabaseClose)) {
    		} else if (event.getSource().equals(mitemDatabaseCreateTextIndex)) {
    			db.createTextIndex();
    			lblMessage.setText("Index created");
    		}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
    }
	
	public Database db;
	private Car selectedCar;
}
