package pkgController;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import pkgMisc.DateFormatConverter;
import pkgModel.Car;
import pkgModel.Database;
import pkgModel.DatabaseOracle;
import pkgModel.Repair;

public class MainController implements Initializable  {

	@FXML
	private Button btnConnect;

	@FXML
	private Button btnLogin;

	@FXML
	private Button btnLoginInj;

	@FXML
	private Label lblMessage;

	@FXML
	private TextField txtPassInj;

	@FXML
	private PasswordField txtPasswort;

	@FXML
	private TextField txtUser;

	@FXML
	private TextField txtUserInj;

    @FXML
    private Pane paneInj;


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			try {
				db = Database.getInstance();
				paneInj.setVisible(false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	@FXML
	void onButtonClick(ActionEvent event) {
		try {
			if (event.getSource().equals(btnConnect)) {
				oradb = DatabaseOracle.getInstance(txtUser.getText(), txtPasswort.getText());
				lblMessage.setText("Connection set");
				paneInj.setVisible(true);
			} else if (event.getSource().equals(btnLogin)) {
				oradb.login(txtUserInj.getText(), txtPassInj.getText());
				lblMessage.setText("Login successful");
			} else if (event.getSource().equals(btnLoginInj)) {

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			paneInj.setVisible(false);
			lblMessage.setText(e.getMessage());
		}
	}

	/*
	 * non GUI components
	 */
	private Database db = null;
	private DatabaseOracle oradb = null;
	private Car selectedCar = null;
	private Repair selectedRepair = null;
}
