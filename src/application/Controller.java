package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;

public class Controller {

	//Instance fields
	FXMLLoader loader = new FXMLLoader();
	URL fxmlURL;

	@FXML
	private Label keyLabel;

	@FXML
	private Text keyDescription;

	public static void main(String[] args) {
		

	}
	
	public void init() {
		
	}

	private void changeScreen(String path, ActionEvent event) {

		fxmlURL = this.getClass().getResource(path);
		Scene scene = ((Button) event.getTarget()).getScene();

		try {
			Parent root = loader.load(fxmlURL);
			scene.setRoot(root);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void setKeyDescription(ActionEvent event) {
		Node node = (Node) event.getSource();
		String button = (String) node.getUserData();

		String message = "";

		switch (button) {
			case "up":
				message = "Rotates the current shape clockwise.";
				break;
			case "down":
				message = "Slowly drops the current shape downwards.";
				break;
			case "left":
				message = "Moves the current shape to the left.";
				break;
			case "right":
				message = "Moves the current shape to the right.";
				break;
			case "space":
				message = "Drops the shape to the bottommost row instantaneously.";
				break;
			case "z":
				message = "Rotates the current shape counter-clockwise.";
				break;
			case "x":
				message = "Rotates the current shape clockwise.";
				break;
			case "c":
				message = "Holds the current shape. One shape can be held in storage at any time.";
				break;
			default:
				break;
		}

		keyLabel.setText("Controls: " + button.toUpperCase() + " key");
		keyDescription.setText(message);

	}

	@FXML
	private void openInstructions(ActionEvent event) {
		changeScreen("fxml-layouts/instructions.fxml/", event);
	}

	@FXML
	private void openHomeScreen(ActionEvent event) {
		changeScreen("fxml-layouts/main-screen.fxml/", event);
	}

	@FXML
	private void openGameOver(ActionEvent event) {
		changeScreen("fxml-layouts/game-over.fxml/", event);
	}

	@FXML
	private void quitApplication(ActionEvent event) {
		System.out.println("Quitting application.");
		Platform.exit();
	}


}
