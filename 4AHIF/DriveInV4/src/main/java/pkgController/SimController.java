package pkgController;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import pkgMisc.*;
import pkgSubjects.Lane;
import pkgThread.Customer;
import pkgThread.CustomerGenerator;
import pkgThread.Watch;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SimController implements Initializable, MessageEventListener, AnimationEventListener, AnimationParameters {
	@FXML
    private Button btn_go;

    @FXML
    private Label lblMsg;

    @FXML
    private ListView<String> lstView;

	private Stage myStage;
	private long timeBetweenCars = 0;
	private int numCars = 0;
	private float percentFirebrigade = 0;
	private float percentPassenger = 0;
	private HashMap<Integer, ImageView> consumerImages = new HashMap<>();

	ArrayList<Lane> lanes = new ArrayList<>(5);
	Watch watch;
	CustomerGenerator cg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void setStageAndLoad(Stage stage) {
		myStage = stage;
		lanes.add(new Lane("is hungry", 9999));
		lanes.add(new Lane("is in lane", 2, Customer.TYPE.PASSENGER));
		lanes.add(new Lane("placing order", 2));
		lanes.add(new Lane("paying", 1, Customer.TYPE.FIREBRIGADE));
		lanes.add(new Lane("receive food", 2));
		lanes.add(new Lane("end of drivein", 9999));

		watch = new Watch(5);
		watch.addListener(this);

		cg = new CustomerGenerator(numCars, timeBetweenCars, percentPassenger, percentFirebrigade, lanes);
		cg.addMsgListener(this);
		cg.addAnimListener(this);
		loadImages();
	}

	private void loadImages() {
		for (Customer c : cg.getCustomers()) {
			Pane myPane = (Pane)(myStage.getScene().getRoot());
			ImageView iv = new ImageView();

			if (c.getType() == Customer.TYPE.PASSENGER) {
				iv.setImage(new Image(getClass().getResourceAsStream(IMGPATH_PASSENGER)));
			} else if (c.getType() == Customer.TYPE.FIREBRIGADE) {
				iv.setImage(new Image(getClass().getResourceAsStream(IMGPATH_FIRETUCK)));
			} else {
				iv.setImage(new Image(getClass().getResourceAsStream(IMGPATH_CAR)));
			}

			iv.setFitWidth(WIDTH);
			iv.setFitHeight(HEIGHT);
			iv.setX(c.getX());
			iv.setY(c.getY());

			consumerImages.put(c.getCustomerId(), iv);
			myPane.getChildren().add(iv);
		}
	}

	@FXML
	void onBtnClick(ActionEvent event) {
		if (event.getSource().equals(btn_go)) {
			watch.start();
			cg.start();
		}
	}

	@Override
	public void handleMessageEvent(MessageEvent event) {
		Platform.runLater(() -> lstView.getItems().add(event.getMsg()));
	}

	@Override
	public void handleAnimationEvent(AnimationEvent event) {
		Platform.runLater(() -> {
			ImageView iv = consumerImages.get(((Customer)event.getSource()).getCustomerId());

//			System.out.println("oldX: " + event.getOldX() + ", oldY: " + event.getOldY() + ", newX: " + event.getNewX() + ", newY: " + event.getNewY());

			Path path = new Path();
			path.setTranslateX(WIDTH / 2);
			path.setTranslateY(HEIGHT / 2);
			path.getElements().add(new MoveTo(event.getOldX(), event.getOldY()));
			path.getElements().add(new HLineTo(event.getNewX()));
			PathTransition pt = new PathTransition(); // creates a new thread that is accepted by FX
			pt.setDuration(Duration.millis(ANIMATION_DURATION));
			pt.setPath(path);
			pt.setNode(iv);
			pt.play();
		});
	}

	public void setSettings(String timeBetweenCars, String numCars, String percentFirebrigade, String percentPassenger) {
		this.timeBetweenCars = Long.parseLong(timeBetweenCars);
		this.numCars = Integer.parseInt(numCars);
		this.percentFirebrigade = Float.parseFloat(percentFirebrigade) / 100.f;
		this.percentPassenger = Float.parseFloat(percentPassenger) / 100.f;
	}

	public void stop() {
		watch.interrupt();
		cg.stopAllCars();
		myStage.close();
	}
}
