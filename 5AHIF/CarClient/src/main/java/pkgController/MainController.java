package pkgController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import pkgData.Book;
import pkgData.Car;
import pkgData.GeoPosition;
import pkgData.PetrolStation;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.UUID;

public class MainController implements Initializable {

    @FXML
    public Label lblMessage;

    @FXML
    public TextField txtUri;
    @FXML
    public Button btnConnection;
    @FXML
    public Button btnInit;
    @FXML
    public Button btnUpdatePos;

    @FXML
    public Button btnGetCars;
    @FXML
    public Button btnGetPetrolStations;


    @FXML
    public TableView<Car> tableCars;
    @FXML
    public TableColumn<Car, ObjectId> colCarId;
    @FXML
    public TableColumn<Car, String> colCarName;

    @FXML
    public TableView<GeoPosition> tableCarDetails;
    @FXML
    public TableColumn<GeoPosition, String> colCarDatetime;
//    public TableColumn<GeoPosition, LocalDateTime> colCarDatetime;
    @FXML
    public TableColumn<GeoPosition, Double> colCarLat;
    @FXML
    public TableColumn<GeoPosition, Double> colCarLong;

    @FXML
    public TableView<PetrolStation> tablePetrolStations;
    @FXML
    public TableColumn<PetrolStation, ObjectId> colPetId;
    @FXML
    public TableColumn<PetrolStation, String> colPetName;
    @FXML
    public TableColumn<PetrolStation, Double> colPetLat;
    @FXML
    public TableColumn<PetrolStation, Double> colPetLong;

    @FXML
    public Button btnGetNearest;
    @FXML
    public TextField txtDistance;
    @FXML
    public Button btnCreateIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCarId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCarName.setCellValueFactory(new PropertyValueFactory<>("carName"));

        colCarDatetime.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        colCarLong.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLocation().getPosition().getValues().get(0)));
        colCarLat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getLocation().getPosition().getValues().get(1)));

        colPetId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPetName.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        colPetLong.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPosition().getPosition().getValues().get(0)));
        colPetLat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPosition().getPosition().getValues().get(1)));
    }

    private RESTController restController;

    @FXML
    public void onBtnClick(ActionEvent actionEvent) {
        try {
            if (actionEvent.getSource().equals(btnConnection)) {
                restController = new RESTController(txtUri.getText());
                lblMessage.setText("Connection established with " + txtUri.getText());
            } else if (actionEvent.getSource().equals(btnInit)) {
                restController.initDb();
                updateCarTableViews();
                lblMessage.setText("Database initialized");
            } else if (actionEvent.getSource().equals(btnUpdatePos)) {
//                restController.updatePositions();
                restController.updateCarPositions(selectedCar.getId());
                updateCarTableViews();
                lblMessage.setText("Car positions updated");
            } else if (actionEvent.getSource().equals(btnGetCars)) {
                ArrayList<Car> cars = restController.getCars();
                tableCars.getItems().clear();
                tableCars.getItems().addAll(cars);
                lblMessage.setText("Cars loaded");
            } else if (actionEvent.getSource().equals(btnGetPetrolStations)) {
                ArrayList<PetrolStation> petrolStations = restController.getPetrolStations();
                tablePetrolStations.getItems().clear();
                tablePetrolStations.getItems().addAll(petrolStations);
                lblMessage.setText("Petrol stations loaded");
                System.out.println(restController.getPetrolStations());
            } else if (actionEvent.getSource().equals(btnCreateIndex)) {
                restController.createPetrolStationPositionIndex();
                lblMessage.setText("Petrol station position index created");
            } else if (actionEvent.getSource().equals(btnGetNearest)) {
                double distance = Double.parseDouble(txtDistance.getText());
                ArrayList<PetrolStation> nearest = restController.getNearestPetrolStations(selectedCar, distance);

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/NearestPetrolStationsView.fxml"));
                Parent rootFrame = loader.load();

                NearestPetrolStationsController controller = loader.getController();
                controller.setPetrolStations(nearest);

                Scene scene = new Scene(rootFrame);
                stage.setScene(scene);
                stage.setTitle("Neares petrol stations");
                stage.initModality(Modality.NONE);
                stage.show();

                lblMessage.setText("Nearest petrol stations loaded");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText(e.getMessage());
        }
    }

    private void updateCarTableViews() throws Exception {
        tableCars.getItems().clear();
        tableCars.getItems().addAll(restController.getCars());

        tableCars.getSelectionModel().select(selectedCarIndex);
        if (selectedCarIndex >= 0 && selectedCarIndex < tableCars.getItems().size()) {
            selectedCar = tableCars.getItems().get(selectedCarIndex);
            tableCarDetails.getItems().clear();
            tableCarDetails.getItems().addAll(selectedCar.getPositions());
        }
    }

    Car selectedCar;
    int selectedCarIndex;
    @FXML
    public void onCarSelected(MouseEvent mouseEvent) {
        selectedCar = this.tableCars.getSelectionModel().getSelectedItem();
        selectedCarIndex = this.tableCars.getSelectionModel().getSelectedIndex();
        if (selectedCar != null) {
            tableCarDetails.getItems().clear();
            tableCarDetails.getItems().addAll(selectedCar.getPositions());
            lblMessage.setText("Car selected: " + selectedCar.getCarName());
        } else {
            lblMessage.setText("No car selected");
        }
    }
}
