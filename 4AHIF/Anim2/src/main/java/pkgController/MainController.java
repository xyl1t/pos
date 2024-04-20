package pkgController;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import pkgMisc.AnimationParameters;
import pkgSubject.Worker;

public class MainController implements Initializable, PropertyChangeListener, AnimationParameters {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private ImageView imgCar;

    @FXML
    private Label lblMsg1;

    @FXML
    private Label lblMsg2;

    private Worker w;
    private ImageView iv;
    private static Stage myStage;
    private double oldXCoo = X_COO;

    public static void setStage(Stage stage) {
        myStage = stage;
    }

    @Override
	public void initialize(URL location, ResourceBundle resources) {
	}

    private void createCar2() {
        Pane myPane = (Pane)(myStage.getScene().getRoot());
        iv = new ImageView();
        iv.setImage(new Image(getClass().getResourceAsStream(CAR2_FILEPATH)));

        iv.setFitWidth(WIDTH);
        iv.setPreserveRatio(true);
//        iv.setFitHeight(HEIGHT);
        iv.setX(X_COO);
        iv.setY(Y_COO);

        myPane.getChildren().add(iv);
    }

    private void moveCar2(long distance) throws Exception {
        // doesn't need any "runlater()"
        // (because pathTransition creates a thread which is accepted by the UI-thread)
        Path path = new Path();
        path.getElements().add(new MoveTo(oldXCoo, Y_COO));
        oldXCoo += distance;
        path.getElements().add(new HLineTo(oldXCoo));
        PathTransition pt = new PathTransition(); // creates a new thread that is accepted by FX
        pt.setDuration(Duration.millis(ANIMATION_DURATION));
        pt.setPath(path);
        pt.setNode(iv);
        pt.play();
    }

    @FXML
    void onButtonClick(ActionEvent event) {
        if (event.getSource() == btnStart) {
            createCar2();
            w = new Worker(this);
            w.start();
        } else if (event.getSource() == btnStop) {
            w.stop();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try{
            switch (Worker.WORKER_STATE.valueOf(evt.getPropertyName())) {
                case FORWARD1:
                    Platform.runLater(() -> imgCar.setX((int)evt.getNewValue()));
                    break;
                case FORWARD2:
                    moveCar2((long)evt.getNewValue());
                    break;
                case POSITION:
                    Platform.runLater(() -> lblMsg2.setText("   " + evt.getNewValue()));
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

