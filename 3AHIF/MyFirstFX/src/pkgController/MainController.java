package pkgController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController implements Initializable  {
    @FXML
    private Label lblMessage;
    @FXML
    private Button btnClick;
    @FXML
    void onBtnClickSelected(ActionEvent event) {
    	if(event.getSource().equals(btnClick)) {
    		lblMessage.setText("button clicked");
    	}
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}
}
