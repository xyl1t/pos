package org.restaurant.pkgController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.restaurant.pkgData.Database;
import org.restaurant.pkgData.Guest;
import org.restaurant.pkgData.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GuestController implements Initializable {
    @FXML
    public MenuItem mitemAdd;
    @FXML
    public MenuItem mitemRemove;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtBirth;
    @FXML
    public TextField txtLocation;
    @FXML
    public TextField txtAmount;
    @FXML
    public ListView<Guest> listView;
    @FXML
    public Label lblMessage;

    private Database db;

    private static Restaurant res;
    private static ArrayList<Guest> copy = new ArrayList<>();
    public static void setRestaurant(Restaurant restaurant) {
        res = restaurant;
        if (res.getGuests() != null) {
            copy.addAll(res.getGuests());
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            db = Database.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(res.getGuests());
        if (res.getGuests() != null) {
            listView.getItems().addAll(res.getGuests());
        }

        lblMessage.setText("selected restaurant: " + res);
    }

    @FXML
    public void onGuestMenuClick(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(mitemAdd)) {
            Guest newGuest = new Guest(txtName.getText(), txtBirth.getText(), txtLocation.getText(), txtAmount.getText());
            System.out.println(newGuest);
            db.addGuestToRestaurant(res, newGuest);
            listView.getItems().add(newGuest);

            lblMessage.setText("add done: " + newGuest);
        }
    }

    public void onListSelect(MouseEvent mouseEvent) {
        Guest selected = listView.getSelectionModel().getSelectedItem();
        txtName.setText(selected.getName());
        txtBirth.setText(selected.getBirth().format(Guest.DTF));
        txtLocation.setText(selected.getLocation());
        txtAmount.setText(selected.getTotalAmount().toString());
    }
}
