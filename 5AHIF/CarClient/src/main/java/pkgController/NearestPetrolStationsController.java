package pkgController;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.types.ObjectId;
import pkgData.PetrolStation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class NearestPetrolStationsController implements Initializable {
    @FXML
    public TableView<PetrolStation> tbl;
    @FXML
    public TableColumn<PetrolStation, ObjectId> colId;
    @FXML
    public TableColumn<PetrolStation, String> colName;
    @FXML
    public TableColumn<PetrolStation, Double> colLat;
    @FXML
    public TableColumn<PetrolStation, Double> colLong;

    private ArrayList<PetrolStation> petrolStations = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("stationName"));
        colLong.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPosition().getPosition().getValues().get(0)));
        colLat.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPosition().getPosition().getValues().get(1)));

        tbl.getItems().addAll(petrolStations);
    }

    public void setPetrolStations(ArrayList<PetrolStation> petrolStations) {
        this.petrolStations = petrolStations;
        tbl.getItems().addAll(petrolStations);
    }
}
