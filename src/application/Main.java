package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.net.URL;

/**
 * Responsible for launching the application.
 */
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			URL fxmlURL = this.getClass().getResource("fxml-layouts/profile-select.fxml");
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(fxmlURL);
			
			Parent root = loader.load();
			Scene scene = new Scene(root);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(new Image("file:src/application/appicons/squarium-small.png"));
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Squarium");
			primaryStage.show();

			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
