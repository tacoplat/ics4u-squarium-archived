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


/**
 * Controller class for mode selection screen
 */
public class ModeSelectionController extends Controller {
    // Variable to identify which of the "other modes" has been selected ot play
    public static String modeSelect = "";

    //Instance fields
    FXMLLoader loader = new FXMLLoader();
    URL fxmlURL;

    /**
     * Opens game with easy settings
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void openClassicEasy(ActionEvent event) {
        modeSelect = "classic-Easy";
        GameController.movePerSecond = 2;
        GameController.keyPressPerSecond = 6;
        GameController.lockMode = 0;
        GameController.difficulty = 1;
        changeScreen("fxml-layouts/game-main.fxml/", event);
    }

    /**
     * Opens game with medium settings
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void openClassicMedium(ActionEvent event) {
        modeSelect = "classic-Medium";
        GameController.movePerSecond = 6;
        GameController.keyPressPerSecond = 8;
        GameController.lockMode = 0;
        GameController.difficulty = 2;
        changeScreen("fxml-layouts/game-main.fxml/", event);
    }

    /**
     * Opens game with hard/difficult settings
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void openClassicHard(ActionEvent event) {
        modeSelect = "classic-Hard";
        GameController.movePerSecond = 15;
        GameController.keyPressPerSecond = 25;
        GameController.lockMode = 0;
        GameController.difficulty = 3;
        changeScreen("fxml-layouts/game-main.fxml/", event);
    }

    /**
     * Opens game with inverted settings
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void openModeInverted(ActionEvent event) {
        modeSelect = "inverted";
        GameController.movePerSecond = 6;
        GameController.keyPressPerSecond = 25;
        GameController.lockMode = 0;
        GameController.difficulty = 3;
        changeScreen("fxml-layouts/game-main.fxml/", event);
    }

    /**
     * Opens game with lock game mode settings
     * @param event JavaFX ActionEvent
     */
    @FXML
    private void openModeLock(ActionEvent event) {
        modeSelect = "lock";
        GameController.movePerSecond = 6;
        GameController.keyPressPerSecond = 25;
        GameController.lockMode = 1;
        GameController.difficulty = 4;
        changeScreen("fxml-layouts/game-main.fxml/", event);
    }

}
