package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;

public class Controller {

	//Instance fields
	FXMLLoader loader = new FXMLLoader();
	URL fxmlURL;

	@FXML
	private Button instructionsBtn;

	public static void main(String[] args) {
		

	}
	
	public void init() {
		
	}

	@FXML
	private void openInstructions(ActionEvent event) {

		fxmlURL = this.getClass().getResource("fxml-layouts/instructions.fxml");
		Scene scene = instructionsBtn.getScene();

		try {
			Parent root = loader.load(fxmlURL);
			scene.setRoot(root);

		} catch (IOException e) {
			e.printStackTrace();
		}


	}


}
