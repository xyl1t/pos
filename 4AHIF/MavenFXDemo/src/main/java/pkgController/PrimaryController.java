package pkgController;

import java.io.IOException;
import javafx.fxml.FXML;
import pkgMain.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
