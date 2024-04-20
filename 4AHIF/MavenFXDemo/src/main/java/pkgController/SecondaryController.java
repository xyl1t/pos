package pkgController;

import java.io.IOException;
import javafx.fxml.FXML;
import pkgMain.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}