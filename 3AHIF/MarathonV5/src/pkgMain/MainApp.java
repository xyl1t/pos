package pkgMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("/pkgView/MainGui.fxml"));
		primaryStage.setTitle("MarathonV4");
		Scene scene = new Scene(root);
		scene.getRoot().setStyle("-fx-font-family: 'helvetica'");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
