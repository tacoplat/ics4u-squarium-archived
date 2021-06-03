package application;

import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.WindowEvent;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            URL fxmlURL = this.getClass().getResource("fxml-layouts/main-screen.fxml");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(fxmlURL);

            Parent root = loader.load();
            Scene scene = new Scene(root);

            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(520);
            primaryStage.setMinHeight(670);
            primaryStage.setMaxWidth(520);
            primaryStage.setMaxHeight(670);
            primaryStage.setTitle("Silly Shapes");
            primaryStage.show();

            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
