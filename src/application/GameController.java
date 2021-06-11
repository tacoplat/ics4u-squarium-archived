package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static application.Common.*;

/**
 * Controller for the game board
 */
public class GameController implements Initializable {

    // Initialize variables

    @FXML
    private SplitPane splitPane;

    @FXML
    private AnchorPane leftPane;

    @FXML
    private AnchorPane rightPane;

    @FXML
    private Pane displayNextPane;

    @FXML
    private Pane displayHoldPane;

    @FXML
    public Label displayScore;

    @FXML
    public Label displayLineCleared;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnBack;

    public static int keyPressPerSecond = 2;
    public static int movePerSecond = 1;
    public static int lockMode = 0;
    public static int difficulty = 0;

    private int lockModeCounter;
    private int lockedMaxNumber;
    private String lockedPieceName;
    private boolean gameOver;
    private boolean isRunning;
    private int ignoreKeyPressCounter;
    private boolean clearRowLock;
    private boolean hardDrop;
    private GameBoard gameBoard;
    private Tetromino tetromino;
    private Tetromino nextObj;
    private Tetromino holdObj;

    // Counter for score and number of lines cleared
    private int lineNum;
    private int score;

    // Timer counter
    private int counter;
    // Scheduled Executor Service to execute tasks repeatedly with a fixed interval of time
    private ScheduledExecutorService ses;
    // ScheduledFuture can be used to get time left before next task execution
    private ScheduledFuture<?> scheduledFuture;

    private Random random;
    private boolean debug;

    /**
     * Constructor for game controller
     */
    public GameController() {
        setDefault();
    }

    /**
     * Method that sets the default settings
     */
    private void setDefault() {
        debug = true;
        lockModeCounter = 0;
        lockedMaxNumber = 0;
        lockedPieceName = "";
        score = 0;
        lineNum = 0;
        gameOver = false;
        isRunning = false;
        ignoreKeyPressCounter = 0;
        clearRowLock = false;
        hardDrop = false;
        random = new Random();

        gameBoard = new GameBoard();
        tetromino = null;
        nextObj = null;
        holdObj = null;
    }

    private void resetGame(){
        setDefault();
        leftPane.getChildren().clear();
        displayNextPane.getChildren().clear();
        displayHoldPane.getChildren().clear();
        displayScore.setText("");
        displayLineCleared.setText("");
        btnPlay.setText("PLAY");
    }

    /**
     * Method to initialize fx components
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ses = Executors.newScheduledThreadPool(1);
        try {
            // Init delay = 0, repeat the task every 1 millisecond
            scheduledFuture = ses.scheduleAtFixedRate(taskUpdating, 0, 1, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    public void clearParkedRows() {
        if (!clearRowLock) {
            // Meke sure no more clearParkedRows actions before the current call was finished
            clearRowLock = true;

            // Disable any movement until the clearParkedRows process finished
            boolean isRunningCached = false;
            if (isRunning) {
                isRunningCached = true;
                isRunning = false;
            }

            // Record the lines of parked blocks was removed by current action
            int lineRemoved = 0;

            int[][] grid = gameBoard.getGrid();
            var parkedBlocks = gameBoard.getParkedBlocks();
            int topRowIdx = HIGH;
            for (int row = 0; row < HIGH; row++) {
                if (Arrays.stream(grid[row]).anyMatch(idx -> idx == 1)) {
                    if (topRowIdx == HIGH) topRowIdx = row;
                    if (Arrays.stream(grid[row]).allMatch(idx -> idx == 1)) {
                        // Delete the row and move the blocks on top one row down
                        for (int m = 0; m < WIDE; m++) {
                            int key = row * WIDE + m;
                            var block = gameBoard.findBlockByIndex(key);
                            leftPane.getChildren().remove(block.getUiBox());
                            parkedBlocks.remove(block);
                        }

                        // Increase the number of total lines removed
                        lineNum++;

                        // Increase the number of lines removed at current move action
                        lineRemoved++;

                        displayLineCleared.setText(String.valueOf(lineNum));
                        Arrays.fill(grid[row], 0);

                        for (int k = row - 1; k >= topRowIdx; k--) {
                            for (int m = 0; m < WIDE; m++) {
                                if (grid[k][m] == 1) {
                                    TetroBox block = gameBoard.findBlockByIndex(Common.calculateIndex(k, m));
                                    block.move();
                                    grid[k][m] = 0;
                                    grid[k + 1][m] = 1;
                                }
                            }
                        }
                    }
                }
            }
            // Reference: https://www.codewars.com/kata/5da9af1142d7910001815d32
            // Increase scores based on the lines removed and the game diff
            switch (lineRemoved){
                case 1:
                    score += difficulty * 80;
                    break;
                case 2:
                    score += difficulty * 200;
                    break;
                case 3:
                    score += difficulty * 600;
                    break;
                case 4:
                    score += difficulty * 2400;
                    break;
            }
            displayScore.setText(String.valueOf(score));

            if (isRunningCached) {
                isRunning = true;
            }

            clearRowLock = false;
        }
    }

    public void addNextTetromino() {
        if(nextObj == null) generateNextObj();

        tetromino = new Tetromino(nextObj.getPieceName(), true);
        // Display the new added Tetromino shape on the Game Board
        for (TetroBox block : tetromino.getBlocks()) {
            leftPane.getChildren().add(block.getUiBox());
        }

        if (nextObj != null) {
            // Remove the next Tetromino shape from the Right Pane
            for (TetroBox block : nextObj.getBlocks()) {
                displayNextPane.getChildren().remove(block.getUiBox());
            }
        }

        generateNextObj();

        // Display the next Tetromino shape on the Right Pane
        for (TetroBox block : nextObj.getBlocks()) {
            displayNextPane.getChildren().add(block.getUiBox());
        }

    }

    private void holdTetromino() {
        if (tetromino != null) {
            String pieceNameTetromino = tetromino.getPieceName();
            if (holdObj != null) {
                // Remove the current Tetromino shape from the Game Board Pane
                for (TetroBox block : tetromino.getBlocks()) {
                    leftPane.getChildren().remove(block.getUiBox());
                }
                tetromino.changeShape(holdObj.getPieceName());
                // Display the new added Tetromino shape on the Game Board
                for (TetroBox block : tetromino.getBlocks()) {
                    leftPane.getChildren().add(block.getUiBox());
                }

                // Remove the previous hold Tetromino shape from the Right Pane
                for (TetroBox block : holdObj.getBlocks()) {
                    displayHoldPane.getChildren().remove(block.getUiBox());
                }
                // Create the Tetromino for hold based on the piece name
                holdObj = new Tetromino(pieceNameTetromino, false);
                // Display the updated hold Tetromino shape on the Game Board
                for (TetroBox block : holdObj.getBlocks()) {
                    displayHoldPane.getChildren().add(block.getUiBox());
                }
            } else {
                holdObj = new Tetromino(pieceNameTetromino, false);
                // Display the updated hold Tetromino shape on the Game Board
                for (TetroBox block : holdObj.getBlocks()) {
                    displayHoldPane.getChildren().add(block.getUiBox());
                }

                // Remove the current Tetromino shape from the Game Board Pane
                for (TetroBox block : tetromino.getBlocks()) {
                    leftPane.getChildren().remove(block.getUiBox());
                }
                tetromino.changeShape(nextObj.getPieceName());
                // Display the new added Tetromino shape on the Game Board
                for (TetroBox block : tetromino.getBlocks()) {
                    leftPane.getChildren().add(block.getUiBox());
                }

                // Remove the next shape from the Game Board Pane
                for (TetroBox block : nextObj.getBlocks()) {
                    displayNextPane.getChildren().remove(block.getUiBox());
                }
                generateNextObj();
                // Display the next Tetromino shape on the Game Board
                for (TetroBox block : nextObj.getBlocks()) {
                    displayNextPane.getChildren().add(block.getUiBox());
                }
            }
        }
    }

    private void generateNextObj(){
        if(lockMode == 1) {
            if(lockModeCounter == 0){
                // Generate random number between 4 to 8
                lockedMaxNumber = random.nextInt(8 - 4 + 1) + 4;
                lockedPieceName = Tetromino.generateRandomPieceName();
                if (debug) System.out.println("lockedMaxNumber: " + lockedMaxNumber + "  lockedPieceName: " + lockedPieceName);
                lockModeCounter++;
            } else {
                if (debug) System.out.println("lockModeCounter: " + lockModeCounter + "  lockedPieceName: " + lockedPieceName);
                lockModeCounter++;
                if(lockModeCounter>lockedMaxNumber) lockModeCounter = 0;
            }
            nextObj = new Tetromino(lockedPieceName, false);
        }else {
            nextObj = new Tetromino();
        }
    }

    public void addParkedTetromino() {
        if (tetromino != null) {
            // Add parked tetromino to the Game Board
            gameBoard.addToBoard(tetromino);
            clearParkedRows();
        }
    }

    /**
     * Check if the parked blocks reached the Gameboard top
     * @return true: at least one parked blocks the top; false: no parked block at the top.
     */
    private boolean reachedTop() {
        return Arrays.stream(gameBoard.getGrid()[0]).anyMatch(idx -> idx == 1);
    }

    private void processGameOver() {
        isRunning = false;
        gameOver = true;
        btnPlay.setText("RESTART");
        btnBack.setText("Check Results");

        Text over = new Text("GAME OVER");
        over.setFill(Color.RED);
        over.setStyle("-fx-font: 42 arial;");
        over.setY(250);
        over.setX(36);
        leftPane.getChildren().add(over);
    }

    /**
     * Run the tasks with one millisecond delay
     */
    private Runnable taskUpdating = () -> {
        counter++;
        if (counter > 10000000) counter = 0;

        if (ignoreKeyPressCounter > 0) {
            ignoreKeyPressCounter--;
        }

        // If the game is running, move the tetromino down based on the movePerSecond setting,
        // or move the tetromino down at the full speed (one step per millisecond) to achieve the hardDrop effect
        if (isRunning && (counter % (1000 / movePerSecond) == 1 || hardDrop)) {
            Platform.runLater(() -> {
                btnPlay.requestFocus();
                processMoveDown(true);
            });
        }
    };

    public void processMoveDown(boolean auto){
        if (nextObj == null) {
            generateNextObj();
            return;
        }

        if(tetromino == null) return;

        int moved = tetromino.move(gameBoard.getIndexList(), "down");
        if (moved == 0) { // Touched the bottom or other blocks
            if(hardDrop) {
                hardDrop = false;
                // add score based on the row dropped and difficulty
                score += difficulty * (HIGH-tetromino.getBaseRowNumber());
                displayScore.setText(String.valueOf(score));
            }
            addParkedTetromino();
            clearParkedRows();

            // Check Game over: if parked blocks reached the top or new generated blocks can't move down
            if(reachedTop()) {
                processGameOver();
                return;
            }

            // Generate new Tetromino
            addNextTetromino();
        } else if(moved == 2) {  // Game over since new generated block can't move down
            hardDrop = false;
            processGameOver();
        } else if(!auto) {
            // if moved down by player, add score based on difficulty
            score += difficulty;
            displayScore.setText(String.valueOf(score));
        }
    }

    private void processAction(){
        if (gameOver) {
            btnPlay.setText("PLAY");
            btnBack.setText("Mode Selection");
            resetGame();
        } else {
            isRunning = !isRunning;
            if (isRunning) {
                if(nextObj == null) generateNextObj();
                if(tetromino == null) addNextTetromino();
                btnPlay.setText("PAUSE");
            } else {
                btnPlay.setText("PLAY");
            }
        }
    }

    @FXML
    public void handleBtnBackClick(MouseEvent mouseEvent) {
        if(gameOver){
            changeScreen("fxml-layouts/game-over.fxml/", mouseEvent);
        } else {
            changeScreen("fxml-layouts/mode-selection.fxml/", mouseEvent);
        }
    }

    // Handles mouse click
    @FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
        processAction();
    }

    // Handles arrow key press to move
    @FXML
    public void handleKeyPress(KeyEvent event) {

        if (debug) System.out.println("Keypress..." + event.getCode());

        if (ignoreKeyPressCounter > 0) {
            return;
        }

        ignoreKeyPressCounter = 1000 / keyPressPerSecond;

        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/KeyCode.html
        switch (event.getCode()) {
            case RIGHT:     // Non-numpad right arrow key pressed
            case KP_RIGHT:  // Numeric keypad right arrow key pressed
                if (isRunning) {
                    if (ModeSelectionController.modeSelect == "inverted") {
                        tetromino.move(gameBoard.getIndexList(), "left");
                    } else {
                        tetromino.move(gameBoard.getIndexList(), "right");
                    }
                }
                break;
            case LEFT:      // Non-numpad left arrow key pressed
            case KP_LEFT:   // Numeric keypad left arrow key pressed
                if (isRunning) {
                    if (ModeSelectionController.modeSelect == "inverted") {
                        tetromino.move(gameBoard.getIndexList(), "right");
                    } else {
                        tetromino.move(gameBoard.getIndexList(), "left");
                    }

                }
                break;
            case DOWN:     // Non-numpad down arrow key pressed
            case KP_DOWN:  // Numeric keypad down arrow key pressed
                if (isRunning) {
                    processMoveDown(false);
                }
                break;
            case X:
            case UP:
            case KP_UP:
                if (isRunning) {
                    if (ModeSelectionController.modeSelect == "inverted") {
                        tetromino.rotate(gameBoard.getIndexList(), false);
                    } else {
                        tetromino.rotate(gameBoard.getIndexList(), true);
                    }
                }
                break;
            case Z:
                if (isRunning) {
                    if (ModeSelectionController.modeSelect == "inverted") {
                        tetromino.rotate(gameBoard.getIndexList(), true);
                    } else {
                        tetromino.rotate(gameBoard.getIndexList(), false);
                    }
                }
                break;
            case SPACE:
                if (isRunning) {
                    this.hardDrop = true;
                }
                break;
            case C:
                if (isRunning) {
                    holdTetromino();
                }
                break;
            case ENTER:    // Enter key pressed
                processAction();
                break;
        }
    }

    /**
     * Changes the current scene to another FXML layout.
     *
     * @param path - The path to the FXML layout.
     * @param event - JavaFX ActionEvent
     */
    public void changeScreen(String path, MouseEvent event) {

        // Set the target path to the input from the parameter.
        URL fxmlURL = this.getClass().getResource(path);

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

}
