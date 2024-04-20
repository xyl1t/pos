package pkgController;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.util.Duration;
import pkgMisc.*;
import pkgSubjects.*;
import pkgThread.CustomerGenerator;
import pkgThread.Shopper;
import pkgThread.Watch;

public class MainController implements Initializable, NewShopperEventListener, MessageEventListener, AnimationEventListener, UpdateResultsEventListener {
	@FXML
	public TextField txtCustPerMinute;
	@FXML
	public TextField txtShoppingTime;
	@FXML
	public TextField txtCashingTime;
	@FXML
	public ListView listLog;
	@FXML
	public Label lblCustomers;
	@FXML
	public Label lblEmergencyExits;
	@FXML
	public Label lblAvgShopping;
	@FXML
	public Label lblAvgQueuing;
	@FXML
	public Label lblUtilization;

	@FXML
	private Button btnStart;

	@FXML
	private Button btnStop;

	@FXML
	private Label lblMsg;

	private SimController simController;

	private ShopSimulator shopSimulator;

	private HashMap<String, ImageView> shopperImageViews = new HashMap<>();

	Watch watch;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	@FXML
	void onBtnClick(ActionEvent event) throws IOException {
		if (event.getSource().equals(btnStart)) {
			lblMsg.setText("Starting.....");

			int customersPerMinute = Integer.parseInt(txtCustPerMinute.getText());
			double shoppingTime = Double.parseDouble(txtShoppingTime.getText());
			double cashingTime = Double.parseDouble(txtCashingTime.getText());

			ArrayList<ShopElement> shopElements = new ArrayList<>();
//			shopElements.add(new Shelf("Shelf 1", 6));
//			shopElements.add(new Shelf("Shelf 2", 8));
//			shopElements.add(new Shelf("Shelf 3", 7));
//			shopElements.add(new Shelf("Shelf 4", 5));
//			shopElements.add(
//				new Branch("Cash or Exit",
//					new CashRegister("Cash Register"),
//					new Exit(),
//					0.9
//				)
//			);

			shopSimulator = new ShopSimulator(customersPerMinute, shoppingTime, cashingTime, shopElements);

			shopSimulator.addAnimListener(this);
			shopSimulator.addMsgListener(this);
			shopSimulator.addNewShopperListener(this);

			shopSimulator.start();

			if (watch != null) watch.end();
			watch = new Watch(500);
			watch.addListener(this);
			watch.start();

//			s.setCashRegister(new CashRegister("Cash Register"));
//
//			s.setExit(new Exit());


//			Pane myPane = (Pane)(lblMsg.getScene().getRoot());
//
//			ImageView iv = new ImageView();
//
//			iv.setImage(new Image(getClass().getResourceAsStream("/pkgView/shop.png")));
//
//			iv.setFitWidth(500);
//			iv.setFitHeight(400);
//			iv.setX(400);
//			iv.setY(300);
//
//			myPane.getChildren().add(iv);

//			Stage stage = new Stage();
//			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pkgView/SimGui.fxml"));
//			Parent rootframe = fxmlLoader.load();
//			Scene scene = new Scene(rootframe);
//			stage.setScene(scene);
//			stage.setTitle("Test");
//
//			simController = fxmlLoader.getController();
//			simController.setSettings(
//				txtTimeBetweenCars.getText(),
//				txtNumCars.getText(),
//				txtPercentFirebrigade.getText(),
//				txtPercentPassenger.getText()
//			);
//			simController.setStageAndLoad(stage);
//			stage.setOnHidden(e -> {
//				simController.stop();
//			});
//			stage.show();
		} else if (event.getSource().equals(btnStop)) {
			shopSimulator.end();
			lblMsg.setText("Stopping simulation");
		}
	}

	private Lane lane;
	private CustomerGenerator cdg;

	@Override
	public void handleAnimationEvent(AnimationEvent event) {
		Platform.runLater(() -> {
			ImageView iv = shopperImageViews.get(((Shopper)event.getSource()).getShopperName());

//			System.out.println("oldX: " + event.getOldX() + ", oldY: " + event.getOldY() + ", newX: " + event.getNewX() + ", newY: " + event.getNewY());

			Path path = new Path();
			path.setTranslateX(32 / 2);
			path.setTranslateY(64 / 2);
			path.getElements().add(new MoveTo(event.getOldX(), event.getOldY()));
			path.getElements().add(new LineTo(event.getNewX(), event.getNewY()));
			PathTransition pt = new PathTransition(); // creates a new thread that is accepted by FX
			pt.setDuration(Duration.millis(1000));
			pt.setPath(path);
			pt.setNode(iv);
			pt.play();
		});
	}

	@Override
	public void handleMessageEvent(MessageEvent event) {
		Platform.runLater(() -> listLog.getItems().add(0, event.getMsg()));
	}

	@Override
	public void handleNewShopperEvent(NewShopperEvent event) {
		ImageView iv = new ImageView(new Image(getClass().getResourceAsStream("/pkgView/customer.png")));

		Shopper s = event.getNewShopper();

		iv.setFitWidth(32);
		iv.setFitHeight(64);
		iv.setX(s.getxPos());//event.getNewShopper().getX());
		iv.setY(s.getyPos());//event.getNewShopper().getY());
		Platform.runLater(() -> {
			((Pane)lblMsg.getScene().getRoot()).getChildren().add(iv);
		});

		shopperImageViews.put(event.getNewShopper().getShopperName(), iv);
	}

	@Override
	public void handleUpdateResultsEvent(UpdateResultsEvent event) {
		Platform.runLater(() -> {
			shopSimulator.calculateResults();

			NumberFormat formatter = new DecimalFormat("#0.00");

			this.lblCustomers.setText(shopSimulator.numGeneratedCustomers() + " customers");
			this.lblEmergencyExits.setText(shopSimulator.numExitUsers() + " emergency exits");
			this.lblAvgShopping.setText(formatter.format(shopSimulator.getAvgShoppingTime()) + " avg shopping duration");
			this.lblAvgQueuing.setText(formatter.format(shopSimulator.getAvgQueueingTime()) + " avg queuing time");
			this.lblUtilization.setText(formatter.format(shopSimulator.getUtilization() * 100) + "% utilization of cash-register");
		});
	}
}
