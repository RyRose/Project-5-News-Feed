package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("GUI Version 1.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root,800,825);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			//Setting window title
			primaryStage.setTitle("RSS Reader");
			primaryStage.getIcons().add(new Image( Main.class.getResourceAsStream( "project5logo.png" ))); 
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
