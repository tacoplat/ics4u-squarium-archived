package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Optional;

/**
 * The general FXML controller for the game.
 */
public class Controller {

	// Instance fields
	URL fxmlURL;
	public static Profile currentProfile;
	public static String defaultDataPath = "src/application/appdata/";
	public String[][] appInformation = {
			{"Author(s)", "NelsonHacks [C. Zhang, A. Zhen]"},
			{"App Name", "Squarium"},
			{"Version", "1.0"}
	};

	/**
	 * Default JavaFX initialize method.
	 */
	public void initialize() {
		updateDefaultPath();
	}

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
		configurePopupIcons(dialog);

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
				"Current Profile: " + currentProfile.getPlayerName() + " (ID: " + currentProfile.getId() + ")\n" +
				"Default Path: " + defaultDataPath
		);

		dialog.setHeaderText(null);
		dialog.setGraphic(null);
		configurePopupIcons(dialog);

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
		dc.setInitialDirectory(new File(Controller.getDefaultPath()));

		try {
			// Try to set the directory.
			File directory = dc.showDialog(currentStage);
			String newPath = directory.getAbsolutePath();
			writePathToConfigFile(newPath);
			updateDefaultPath();
			System.out.println("Game save directory set to " + newPath);
		} catch (NullPointerException e) {
			System.out.println("User did not choose a directory.");
		}
	}

	/**
	 * Update the default path from the config file.
	 */
	private void updateDefaultPath() {
		String path = "src/application/appdata/config.save";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);

			String str;

			// Read through the lines of the config file.
			while ((str = br.readLine()) != null) {

				// Split the line by the delimiter, store in a String array.
				String[] extract = str.split(">%");

				if (extract[0].equals("path")) {
					setDefaultPath(extract[1]);
				}
			}

			fr.close();

		} catch (FileNotFoundException e) {
			System.out.println("Cannot locate config file.");
			File newScoreFile = new File(path);
			try {
				newScoreFile.createNewFile(); // Attempt to create the new file.
				writePathToConfigFile("src/application/appdata/");
			} catch (IOException ex) {
				System.out.println("File already exists.");
				e.printStackTrace();
			}
		} catch (IOException e) {
			System.out.println("Could not read from the file.");
			e.printStackTrace();
		}
	}

	/**
	 * Write a new path to the config file.
	 * @param path
	 */
	private void writePathToConfigFile(String path) {
		try {
			FileWriter fw = new FileWriter("src/application/appdata/config.save");
			String entry = "\n" + "path" + ">%" + path;

			fw.write(entry);
			fw.close();

		} catch (IOException e) {
			System.out.println("Error reading from this file");
			e.printStackTrace();
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

	/**
	 * Changes the icon of all popup windows.
	 * @param popup - The alert object.
	 */
	public <T extends Dialog> void configurePopupIcons(T popup) {
		Stage stage = (Stage) popup.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:src/application/appicons/squarium-small.png"));
	}

	/**
	 * Set the default data path
	 * @param path - Path to the save file.
	 */
	public static void setDefaultPath(String path) {
		defaultDataPath = path;
	}

	/**
	 * Returns the default data directory.
	 * @return The default data path.
	 */
	public static String getDefaultPath() {
		return defaultDataPath;
	}
}
