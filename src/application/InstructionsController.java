package application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InstructionsController extends Controller {

    @FXML
    private Label keyLabel;

    @FXML
    private Text keyDescription;

    @FXML private Button ctrlBtnUP;
    @FXML private Button ctrlBtnLEFT;
    @FXML private Button ctrlBtnDOWN;
    @FXML private Button ctrlBtnRIGHT;
    @FXML private Button ctrlBtnZ;
    @FXML private Button ctrlBtnX;
    @FXML private Button ctrlBtnC;
    @FXML private Button ctrlBtnSPACE;

    private List<Button> btnList;
    private int currentInd = 0;

    public void initialize() {
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        setKeyPressHandlers();

                        btnList = new ArrayList<Button>(
                                Arrays.asList(
                                        ctrlBtnUP, ctrlBtnLEFT, ctrlBtnDOWN, ctrlBtnRIGHT,
                                        ctrlBtnZ, ctrlBtnX, ctrlBtnC, ctrlBtnSPACE
                                )
                        );
                    }
                }
        );
    }

    @FXML
    private void setKeyPressHandlers() {
        Scene scene = ctrlBtnUP.getScene();
        
        scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {

            btnList.get(currentInd).getStyleClass().remove("ab-active");

            assignIndex(e);

            btnList.get(currentInd).fire();
            btnList.get(currentInd).getStyleClass().add("ab-active");
            e.consume();
        });


    }

    private void assignIndex(KeyEvent e) {
        if (e.getCode() == KeyCode.UP) currentInd = 0;
        else if (e.getCode() == KeyCode.LEFT) currentInd = 1;
        else if (e.getCode() == KeyCode.DOWN) currentInd = 2;
        else if (e.getCode() == KeyCode.RIGHT) currentInd = 3;
        else if (e.getCode() == KeyCode.Z) currentInd = 4;
        else if (e.getCode() == KeyCode.X) currentInd = 5;
        else if (e.getCode() == KeyCode.C) currentInd = 6;
        else if (e.getCode() == KeyCode.SPACE) currentInd = 7;
    }

    /**
     * Sets the key descriptions on the instructions screen.
     *
     * @param event
     */
    @FXML
    private void setKeyDescription(ActionEvent event) {

        // Get the FXML UserData property of the pressed button.
        Node node = (Node) event.getSource();
        String button = (String) node.getUserData();

        String message = "";

        // Set the message to display.
        switch (button) {
            case "up":
            case "x":
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
            case "c":
                message = "Holds the current shape. One shape can be held in storage at any time.";
                break;
            default:
                break;
        }

        // Update the screen elements.
        keyLabel.setText("Controls: " + button.toUpperCase() + " key");
        keyDescription.setText(message);

    }
}
