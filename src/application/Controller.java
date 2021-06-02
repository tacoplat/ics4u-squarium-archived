package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Controller {

	//Instance fields
	FXMLLoader loader = new FXMLLoader();
	URL fxmlURL;

	public static void main(String[] args) {


	}

	/**
	 * Changes the current scene to another FXML layout.
	 *
	 * @param path - The path to the FXML layout.
	 * @param event
	 */
	private void changeScreen(String path, ActionEvent event) {

		// Set the target path to the input from the parameter.
		fxmlURL = this.getClass().getResource(path);

		// Get the references of the scene and stage of the pressed button.
		Scene scene = ((Button) event.getTarget()).getScene();
		Stage stage = (Stage) scene.getWindow();

		try {
			// Load the new layout into a new root element.
			Parent root = loader.load(fxmlURL);

			// Create a new scene and set the root and stylesheet.
			Scene newScene = new Scene(root);
			newScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Set the scene.
			stage.setScene(newScene);

		} catch (IOException e) {
			System.out.println("Could not load the specified layout.");
			e.printStackTrace();
		}

	}

	/**
	 * Opens the instructions page.
	 * @param event
	 */
	@FXML
	private void openInstructions(ActionEvent event) {
		changeScreen("fxml-layouts/instructions.fxml/", event);
	}

	/**
	 * Opens the title screen.
	 * @param event
	 */
	@FXML
	private void openHomeScreen(ActionEvent event) {
		changeScreen("fxml-layouts/main-screen.fxml/", event);
	}

	/**
	 * Opens the sccoreboard.
	 * @param event
	 */
	@FXML
	private void openScoreboard(ActionEvent event) {
		changeScreen("fxml-layouts/scoreboard.fxml/", event);
	}

	/**
	 * Opens the Game Over screen.
	 * @param event
	 */
	@FXML
	private void openGameOver(ActionEvent event) {
		changeScreen("fxml-layouts/game-over.fxml/", event);
	}

	/**
	 * Exits the application.
	 * @param event
	 */
	@FXML
	private void quitApplication(ActionEvent event) {
		System.out.println("Quitting application.");
		Platform.exit();
	}

}
