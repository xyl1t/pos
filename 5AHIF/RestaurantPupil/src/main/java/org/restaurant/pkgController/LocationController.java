package org.restaurant.pkgController;

import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.restaurant.pkgData.Database;
import org.restaurant.pkgData.Location;
import org.restaurant.pkgData.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LocationController implements Initializable {
    @FXML
    public MenuItem mitemAddLocation;
    @FXML
    public MenuItem mitemDeleteLocation;
    @FXML
    public MenuItem mitemSpatialNear;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtLong;
    @FXML
    public TextField txtLat;
    @FXML
    public TextField txtDistance;
    @FXML
    public ListView<Location> listLocations;
    @FXML
    public ListView<Restaurant> listResults;
    @FXML
    public Label lblMessage;

    private static Database db;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            db = Database.getInstance();
            db.createLocationIndex();
            lblMessage.setText("Created location index");

            listLocations.getItems().addAll(db.getAllLocations());
        } catch (Exception e) {
            lblMessage.setText("Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onMenuLocationClick(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(mitemAddLocation)) {
            Point p = new Point(new Position(Double.parseDouble(txtLong.getText()), Double.parseDouble(txtLat.getText())));
            Location loc = new Location(txtName.getText(), p);
            db.addLocation(loc);
            listLocations.getItems().clear();
            listLocations.getItems().addAll(db.getAllLocations());
//            listLocations.getItems().add(loc);
            lblMessage.setText("Added Location: " + loc);
        } else if (actionEvent.getSource().equals(mitemDeleteLocation)) {
            Point p = new Point(new Position(Double.parseDouble(txtLat.getText()), Double.parseDouble(txtLong.getText())));
            Location loc = new Location(txtName.getText(), p);
            loc.setId(selectedLoc.getId());
            db.removeLocByName(loc);
            listLocations.getItems().clear();
            listLocations.getItems().addAll(db.getAllLocations());
//            listLocations.getItems().removeIf(l -> Objects.equals(l.getLocationName(), loc.getLocationName()));
            lblMessage.setText("Removed Location: " + loc);
        }
    }

    @FXML
    public void onMenuSpatialClick(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource().equals(mitemSpatialNear)) {
            double lng = Double.parseDouble(txtLong.getText());
            double lat = Double.parseDouble(txtLat.getText());
            double dst = Double.parseDouble(txtDistance.getText()) * 1000;
            ArrayList<Restaurant> restaurants = db.findRestaurantsWithin(new Point(new Position(lng, lat)), dst);
            listResults.getItems().clear();
            listResults.getItems().addAll(restaurants);
            lblMessage.setText("Found " + restaurants.size() + " restaurants");
        }
    }

    @FXML
    public void onListLocationSelect(MouseEvent mouseEvent) {
        this.selectedLoc = listLocations.getSelectionModel().getSelectedItem();
        this.txtName.setText(selectedLoc.getName());
        this.txtLong.setText(selectedLoc.getPosition().getPosition().getValues().get(0).toString());
        this.txtLat.setText(selectedLoc.getPosition().getPosition().getValues().get(1).toString());
    }

    private Location selectedLoc = null;
}
