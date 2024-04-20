package org.restaurant.pkgController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.restaurant.pkgData.Database;
import org.restaurant.pkgData.Pub;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label lblMessage;

    @FXML
    private MenuItem mitemOpen;

    @FXML
    private MenuItem mitemClose;

    @FXML
    private MenuItem mitemInsertPub;

    @FXML
    private MenuItem mitemUpdatePub;

    @FXML
    private MenuItem mitemReplacePub;

    @FXML
    private MenuItem mitemDeletePub;

    @FXML
    private TextField txtNamePub;

    @FXML
    private TextField txtLocationPub;

    @FXML
    private TextField txtFoundedPub;

    @FXML
    private TextArea txtDescriptionPub;

    @FXML
    private TableView<Pub> tblPubs;

    @FXML
    private TableColumn<Pub, String> colNamePub;

    @FXML
    private TableColumn<Pub, String> colLocationPub;

    @FXML
    private TableColumn<Pub, Integer> colFoundedPub;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNamePub.setCellValueFactory(new PropertyValueFactory<Pub, String>("name"));
        colLocationPub.setCellValueFactory(new PropertyValueFactory<Pub, String>("location"));
        colFoundedPub.setCellValueFactory(new PropertyValueFactory<Pub, Integer>("founded"));

    }

    @FXML
    void onSelectMenuDatabase(ActionEvent event) {
        try {
            if (event.getSource().equals(mitemOpen)) {
                db = Database.getInstance();
                System.out.println("################################open");

                lblMessage.setText("Connection to database established");
                tblPubs.getItems().clear();
                tblPubs.getItems().addAll(db.getAllRestaurants());
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
    void onSelectMenuPub(ActionEvent event) {
        try {
            if (event.getSource().equals(mitemInsertPub)) {
                Pub pub = new Pub(txtNamePub.getText(), txtLocationPub.getText(), Integer.parseInt(txtFoundedPub.getText()), txtDescriptionPub.getText());
                db.insertPub(pub);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTablePubsSelected(MouseEvent event) {
    }

     /*
     * non gui things
     */
    Database db = null;
    Pub selectedPub = null;
}