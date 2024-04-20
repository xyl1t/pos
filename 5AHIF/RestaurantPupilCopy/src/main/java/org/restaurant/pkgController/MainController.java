package org.restaurant.pkgController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.restaurant.pkgData.Database;
import org.restaurant.pkgData.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private MenuItem mitemOpen;

    @FXML
    private MenuItem mitemClose;

    @FXML
    private MenuItem mitemInsertRestaurant;

    @FXML
    private MenuItem mitemUpdateRestaurant;

    @FXML
    private MenuItem mitemReplaceRestaurant;

    @FXML
    private MenuItem mitemDeleteRestaurant;

    @FXML
    private MenuItem mitemFilterRestaurant;

    @FXML
    private TextField txtNameRestaurant;

    @FXML
    private TextField txtLocationRestaurant;

    @FXML
    private TextField txtFoundedRestaurant;

    @FXML
    private TextArea txtDescriptionRestaurant;

    @FXML
    private TableView<Restaurant> tblRestaurants;

    @FXML
    private TableColumn<Restaurant, String> colNameRestaurant;

    @FXML
    private TableColumn<Restaurant, String> colLocationRestaurant;

    @FXML
    private TableColumn<Restaurant, Integer> colFoundedRestaurant;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNameRestaurant.setCellValueFactory(new PropertyValueFactory<Restaurant, String>("name"));
        colLocationRestaurant.setCellValueFactory(new PropertyValueFactory<Restaurant, String>("location"));
        colFoundedRestaurant.setCellValueFactory(new PropertyValueFactory<Restaurant, Integer>("founded"));
    }

    @FXML
    void onSelectMenuDatabase(ActionEvent event) {
        try {
            if (event.getSource().equals(mitemOpen)) {
                db = Database.getInstance();
                System.out.println("################################open");

                lblMessage.setText("Connection to database established");
                loadTable(db.getAllRestaurants());
            } else if (event.getSource().equals(mitemClose)) {
                db.close();
                lblMessage.setText("Connection to database closed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText(e.getMessage());
        }
    }

    @FXML
    void onSelectMenuRestaurant(ActionEvent event) {
        try {
            if (event.getSource().equals(mitemInsertRestaurant)) {
                Restaurant restaurant = new Restaurant(txtNameRestaurant.getText(), txtLocationRestaurant.getText(), Integer.parseInt(txtFoundedRestaurant.getText()), txtDescriptionRestaurant.getText());
                db.insertPub(restaurant);
                lblMessage.setText("Inserted restaurant: " + restaurant);
                loadTable(db.getAllRestaurants());
            } else if (event.getSource().equals(mitemDeleteRestaurant)) {
                db.deleteRestaurant(selectedRestaurant);
                lblMessage.setText("Deleted restaurant");
                loadTable(db.getAllRestaurants());
            } else if (event.getSource().equals(mitemUpdateRestaurant)) {
                Restaurant res = new Restaurant(txtNameRestaurant.getText(), txtLocationRestaurant.getText(), Integer.parseInt(txtFoundedRestaurant.getText()), txtDescriptionRestaurant.getText());
                res.setId(selectedRestaurant.getId());
                db.updateRestaurant(res);
                lblMessage.setText("Updated restaurant");
                loadTable(db.getAllRestaurants());
            } else if (event.getSource().equals(mitemReplaceRestaurant)) {
                Restaurant res = new Restaurant(txtNameRestaurant.getText(), txtLocationRestaurant.getText(), Integer.parseInt(txtFoundedRestaurant.getText()), txtDescriptionRestaurant.getText());
                res.setId(selectedRestaurant.getId());
                db.replaceRestaurant(res);
                lblMessage.setText("Replaced restaurant");
                loadTable(db.getAllRestaurants());
            } else if (event.getSource().equals(mitemFilterRestaurant)) {
                lblMessage.setText("Filter clicked");
                db.filterLocationAndYear(txtLocationRestaurant.getText(), txtFoundedRestaurant.getText());
                loadTable(db.getFilteredRestaurants());
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("error: " + e.getMessage());
        }
    }

    @FXML
    void onTableRestaurantsSelected(MouseEvent event) {
        Restaurant res = tblRestaurants.getSelectionModel().getSelectedItem();
        selectedRestaurant = res;

        txtNameRestaurant.setText(res.getName());
        txtLocationRestaurant.setText(res.getLocation());
        txtFoundedRestaurant.setText(String.valueOf(res.getFounded()));
        txtDescriptionRestaurant.setText(res.getDescription());
    }

    private void loadTable(ArrayList<Restaurant> restaurants) {
        try {
            tblRestaurants.getItems().clear();
            tblRestaurants.getItems().addAll(restaurants);
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("Cannot populate table, error: " + e.getMessage());
        }
    }

    /*
     * non gui things
     */
    Database db = null;
    Restaurant selectedRestaurant = null;
}
