package pkgController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import pkgSubject.SimulationMain;

public class MainController implements Initializable {

    @FXML
    private Button btn_start;

    @FXML
    private Button btn_stop;

    @FXML
    private TextField txt_capacityOfBar;

    @FXML
    private TextField txt_cooktime;

    @FXML
    private TextField txt_eatingtime;

    @FXML
    private TextField txt_nCooks;

    @FXML
    private TextField txt_nCustomers;

    private SimulationMain sm = new SimulationMain();

    @Override
	public void initialize(URL location, ResourceBundle resources) {

	}

    @FXML
    void btnClick(ActionEvent event) throws InterruptedException {
        if (event.getSource().equals(btn_start)) {
            sm.start(txt_nCooks.getText(), txt_nCustomers.getText(), txt_capacityOfBar.getText(), txt_cooktime.getText(), txt_eatingtime.getText());
        } else {
            sm.stop();
        }
    }
}

