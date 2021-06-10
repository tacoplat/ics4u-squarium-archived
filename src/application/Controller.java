package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class Controller {

	// Instance fields
	URL fxmlURL;
	public static Profile currentProfile;
	public String[][] appInformation = {
			{"Author(s)", "NelsonHacks [C. Zhang, A. Zhen]"},
			{"App Name", ""},
			{"Version", "1.0"}
	};

	/**
	 * Changes the current scene to another FXML layout.
	 *
	 * @param path - The path to the FXML layout.
	 * @param event - JavaFX ActionEvent
	 */
	public void changeScreen(String path, ActionEvent event) {

		// Set the target path to the input from the parameter.
		fxmlURL = this.getClass().getResource(path);

		// Get the references of the scene and stage of the pressed button.
		Scene scene = ((Node) event.getTarget()).getScene();
		Stage stage = (Stage) scene.getWindow();

		try {
			// Load the new layout into a new root element.
			Parent root = FXMLLoader.load(fxmlURL);

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
	 * @param event - JavaFX ActionEvent
	 */
	@FXML
	private void openInstructions(ActionEvent event) {
		changeScreen("fxml-layouts/instructions.fxml/", event);
	}

	/**
	 * Opens the title screen.
	 * @param event - JavaFX ActionEvent
	 */
	@FXML
	private void openModeSelection(ActionEvent event) {
		changeScreen("fxml-layouts/mode-selection.fxml/", event);
	}

	@FXML
	private void openHomeScreen(ActionEvent event) {
		changeScreen("fxml-layouts/main-screen.fxml/", event);
	}

	/**
	 * Opens the scoreboard.
	 * @param event - JavaFX ActionEvent
	 */
	@FXML
	private void openScoreboard(ActionEvent event) {
		changeScreen("fxml-layouts/scoreboard.fxml/", event);
	}

	/**
	 * Opens the Game Over screen.
	 * @param event - JavaFX ActionEvent
	 */
	@FXML
	public void openGameOver(ActionEvent event) {
		changeScreen("fxml-layouts/game-over.fxml/", event);
	}

	/**
	 * Exits the application.
	 */
	@FXML
	private void quitApplication() {

		// Set up a confirmation dialog.
		Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
		dialog.setTitle("Quit Game");
		dialog.setContentText("Are you sure you want to close the application?");

		dialog.setHeaderText(null);
		dialog.setGraphic(null);

		((Button) dialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Quit"); // Relabel the ok button.

		Optional<ButtonType> result = dialog.showAndWait();

		// Button actions.
		if (result.isPresent() && result.get() == ButtonType.OK) {
			Platform.exit();
			System.out.println("Quitting application.");
		} else {
			dialog.close();
			System.out.println("User cancelled action.");
		}
	}

	/**
	 * Displays a dialog to show the app info, as well as some simple settings.
	 * @param event - JavaFX ActionEvent
	 */
	@FXML
	private void showAppInfo(ActionEvent event) {
		// Configure new confirmation dialog.
		Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
		dialog.setTitle("App Information");
		dialog.setContentText(
				appInformation[0][0] + ": " + appInformation[0][1] + "\n" +
				appInformation[1][0] + ": " + appInformation[1][1] + "\n" +
				appInformation[2][0] + ": " + appInformation[2][1] + "\n" +
				"Current Profile: " + currentProfile.getPlayerName() + " (ID: " + currentProfile.getId() + ")"
		);

		dialog.setHeaderText(null);
		dialog.setGraphic(null);

		ButtonType changePath = new ButtonType("Change Save Path");
		ButtonType changeProfile = new ButtonType("Change Profile");
		ButtonType close = ButtonType.CLOSE;

		dialog.getButtonTypes().setAll(changePath, changeProfile, close);

		Optional<ButtonType> result = dialog.showAndWait(); // Show dialog and wait for response.
		if (result.isPresent()) {
			if (result.get() == changePath) {
				pathChange(event);
			} else if (result.get() == changeProfile) {
				changeScreen("fxml-layouts/profile-select.fxml/", event);
			} else if (result.get() == close) {
				dialog.close();
			}
		}
	}

	/**
	 * Opens a dialog to set the game's default data directory.
	 * @param event - JavaFX ActionEvent
	 */
	private void pathChange(ActionEvent event) {

		// Get the current Stage.
		Stage currentStage = (Stage) ((Node) event.getTarget()).getScene().getWindow();

		// Configure a DirectoryChooser.
		DirectoryChooser dc = new DirectoryChooser();
		dc.setTitle("Set Default Game Save Path");
		dc.setInitialDirectory(new File(ScoreboardController.getDefaultScoreboardPath()));

		try {
			// Try to set the directory.
			File directory = dc.showDialog(currentStage);
			ScoreboardController.setDefaultScoreboardPath(directory.getAbsolutePath());
			System.out.println("Game save directory set to " + directory.getAbsolutePath());
		} catch (NullPointerException e) {
			System.out.println("User did not choose a file.");
		}
	}

	/**
	 * Set the current profile.
	 * @param profileIn - The current profile.
	 */
	public static void setProfile(Profile profileIn) {
		currentProfile = profileIn;
	}

	/**
	 * Get the ID of the current profile.
	 * @return The current profile's ID.
	 */
	public static Integer getCurrentProfileId() {
		return currentProfile.getId();
	}
}
