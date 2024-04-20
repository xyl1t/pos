package pkgController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pkgSubjects.Lane;
import pkgThread.CustomerGenerator;
import pkgThread.Watch;

public class MainController implements Initializable {
	@FXML
	private Button btnStart;

	@FXML
	private Button btnStop;

	@FXML
	private Label lblMsg;

	@FXML
	private TextField txtNumCars;

	@FXML
	private TextField txtPercentFirebrigade;

	@FXML
	private TextField txtPercentPassenger;

	@FXML
	private TextField txtTimeBetweenCars;

	private SimController simController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	void onBtnClick(ActionEvent event) throws IOException {
		if (event.getSource().equals(btnStart)) {
			lblMsg.setText("Opening Mekki!");
			Stage stage = new Stage();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pkgView/SimGui.fxml"));
			Parent rootframe = fxmlLoader.load();
			Scene scene = new Scene(rootframe);
			stage.setScene(scene);
			stage.setTitle("Test");

			simController = fxmlLoader.getController();
			simController.setSettings(
				txtTimeBetweenCars.getText(),
				txtNumCars.getText(),
				txtPercentFirebrigade.getText(),
				txtPercentPassenger.getText()
			);
			simController.setStageAndLoad(stage);
			stage.setOnHidden(e -> {
				simController.stop();
			});
			stage.show();
		} else {
			if (simController != null) {
				simController.stop();
			}
			lblMsg.setText("Closing Mekki!");
		}
	}

	private Watch watch;
	private Lane lane;
	private CustomerGenerator cdg;
}
