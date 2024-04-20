package pkgController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.util.Duration;

public class MainController implements Initializable  {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnStop;

    @FXML
    private ImageView imgCar;

    @FXML
    private Label lblMsg;

    @FXML
    private Pane pane;

	/*
	 * Other properties
	 */
	private FadeTransition ft = null;
	private PathTransition pt = null;
	private static double START_X = 20d;
	private static double START_Y = 20d;
	private static double WIDTH = 600d;
	private static double HEIGHT = 250d;
	private static double WIDTH_RTN = 400d;
	private static double HEIGHT_RTN = 200d;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

    @FXML
    void onButtonClick(ActionEvent event) {
		try {
			if (event.getSource() == btnStart) {
				doAnimationFading();
				doAnimationMoving();
				lblMsg.setText("start of animation");
			} else if (event.getSource() == btnStop) {
				doAnimationStop();
				lblMsg.setText("stop animation");
			}
		} catch(Exception ex) {
			lblMsg.setText("error: " + ex.getMessage());
		}
    }

	private void doAnimationMoving() {
		Path path = new Path();
		// set start position
		path.getElements().add(new MoveTo(START_X, START_Y));
		path.getElements().add(new HLineTo(START_X + WIDTH));
		path.getElements().add(new VLineTo(START_Y + HEIGHT));
		path.getElements().add(new HLineTo(START_X + WIDTH - WIDTH_RTN));
		path.getElements().add(new ArcTo(1d, 1d, 0d, START_X, START_Y + HEIGHT_RTN, false, false));
		path.getElements().add(new VLineTo(START_Y));

		pt = new PathTransition();
		pt.setDuration(Duration.millis(4000));
		pt.setPath(path);
		pt.setNode(imgCar);
		pt.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pt.setCycleCount(Timeline.INDEFINITE);
		pt.setAutoReverse(true);
		pt.play();
	}

	private void doAnimationFading() {
		ft = new FadeTransition(Duration.millis(3000), imgCar);
		ft.setFromValue(1.0);
		ft.setToValue(0.1);
		ft.setCycleCount(Timeline.INDEFINITE);
		ft.setAutoReverse(true);
		ft.play();
	}

	private void doAnimationStop() {
		pt.stop();
		ft.stop();
	}
}

