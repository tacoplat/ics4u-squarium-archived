package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;

public class ModeSelectionController {

    //Instance fields
    FXMLLoader loader = new FXMLLoader();
    URL fxmlURL;

    public static void main(String[] args) {

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
    private void openClassicEasy(ActionEvent event) {
        GameController.movePerSecond = 2;
        GameController.keyPressPerSecond = 4;
        changeScreen("fxml-layouts/classic-easy.fxml/", event);
    }

    @FXML
    private void openClassicMedium(ActionEvent event) {
        changeScreen("", event);
    }
    @FXML
    private void openClassicHard(ActionEvent event) {
        changeScreen("", event);
    }

}
