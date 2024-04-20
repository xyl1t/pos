package org.restaurant.pkgController;

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
import org.restaurant.pkgData.Database;
import org.restaurant.pkgData.Owner;
import org.restaurant.pkgData.Restaurant;
import org.restaurant.pkgMisc.DateFormatConverter;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    public MenuItem mitemOpenLocationGui;

    @FXML
    private MenuItem mitemOpen;

    @FXML
    private MenuItem mitemClose;

    @FXML
    private MenuItem mitemCreateIndex;

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
    private MenuItem mitemFilterTextRestaurant;

    @FXML
    private MenuItem mitemRegularGuestRestaurant;

    @FXML
    private MenuItem mitemAssignOwner;

    @FXML
    private MenuItem mitemUpdateOwner;

    @FXML
    private MenuItem mitemFindOwner;

    @FXML
    private MenuItem mitemDeleteOwner;

    @FXML
    private TextField txtNameRestaurant;

    @FXML
    private TextField txtLocationRestaurant;

    @FXML
    private TextField txtFoundedRestaurant;

    @FXML
    private TextArea txtDescriptionRestaurant;

    @FXML
    private DatePicker dateOwner;

    @FXML
    private TextField txtOwnerName;

    @FXML
    private TextArea txtOwnerDesc;

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

        dateOwner.setPromptText(DateFormatConverter.getPattern());
        dateOwner.setConverter(new DateFormatConverter());
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
                txtFoundedRestaurant.setText("");
                txtLocationRestaurant.setText("");
                txtDescriptionRestaurant.setText("");
                txtNameRestaurant.setText("");
                txtOwnerName.setText("");
                txtOwnerDesc.setText("");
                dateOwner.setValue(null);
                loadTable(new ArrayList<>());
                db.close();
                lblMessage.setText("Connection to database closed");
            } else if (event.getSource().equals(mitemCreateIndex)) {
                db.createIndex();
                lblMessage.setText("Created index");
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
            } else if (event.getSource().equals(mitemFilterTextRestaurant)) {
                ArrayList<Restaurant> filtered = db.filterText(txtDescriptionRestaurant.getText());
                loadTable(filtered);
            } else if (event.getSource().equals(mitemRegularGuestRestaurant)) {

                GuestController.setRestaurant(selectedRestaurant);

                Stage stage = new Stage();
                Parent rootframe = FXMLLoader.load(getClass().getClassLoader().getResource("org/restaurant/GuestView.fxml"));
                Scene scene = new Scene(rootframe);

                stage.setScene(scene);
                stage.setTitle("Test window");
                stage.initModality(Modality.APPLICATION_MODAL);

                stage.showAndWait();
                loadTable(db.getFilteredRestaurants());
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblMessage.setText("error: " + e.getMessage());
        }
    }

    @FXML
    void onSelectMenuOwner(ActionEvent event) throws Exception {
        if (event.getSource().equals(mitemAssignOwner)) {
//            dateOwner.getEditor().getText()
            Owner owner = new Owner(txtOwnerName.getText(), txtOwnerDesc.getText(), dateOwner.getEditor().getText());
            db.assignRestaurantOwner(selectedRestaurant, owner);
            lblMessage.setText("Assigned owner: " + owner + " => " + selectedRestaurant.getName());
            loadTable(db.getAllRestaurants());
        } else if (event.getSource().equals(mitemUpdateOwner)) {
            Owner oldOwner = selectedRestaurant.getOwner();
            Owner owner = new Owner(txtOwnerName.getText(), txtOwnerDesc.getText(), dateOwner.getEditor().getText());
            db.updateRestaurantOwner(selectedRestaurant, owner);
            lblMessage.setText("Owner update done: " + oldOwner + " => " + owner);
            loadTable(db.getAllRestaurants());
        } else if (event.getSource().equals(mitemFindOwner)) {
            Owner owner = new Owner(txtOwnerName.getText(), txtOwnerDesc.getText(), dateOwner.getEditor().getText());
            ArrayList<Restaurant> restaurants = db.findRestaurantOwners(owner);
            loadTable(restaurants);
            lblMessage.setText("Owner " + owner + " found in " + restaurants);
        } else if (event.getSource().equals(mitemDeleteOwner)) {
            db.deleteRestaurantOwner(selectedRestaurant);
            loadTable(db.getAllRestaurants());
            txtOwnerName.setPromptText("Currently no owner");
            txtOwnerName.setText("");
            txtOwnerDesc.setText("");
            dateOwner.setValue(LocalDate.now());
            lblMessage.setText("Delete owner done");
        }
    }

    @FXML
    public void onSelectMenuLocation(ActionEvent actionEvent) throws Exception {
        if (actionEvent.getSource().equals(mitemOpenLocationGui)) {
            Stage stage = new Stage();
            Parent rootframe = FXMLLoader.load(getClass().getClassLoader().getResource("org/restaurant/LocationView.fxml"));
            Scene scene = new Scene(rootframe);

            stage.setScene(scene);
            stage.setTitle("Test window");
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
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

        if (res.hasOwner()) {
            txtOwnerName.setPromptText("Owner name");
            txtOwnerName.setText(res.getOwner().getName());
            txtOwnerDesc.setText(res.getOwner().getCv());
            dateOwner.setValue(res.getOwner().getBirthday());
        } else {
            txtOwnerName.setPromptText("Currently no owner");
            txtOwnerName.setText("");
            txtOwnerDesc.setText("");
            dateOwner.setValue(LocalDate.now());
        }
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
