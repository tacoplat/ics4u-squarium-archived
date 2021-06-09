package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static application.Common.*;

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
    private Button btnPlay;

    public static int keyPressPerSecond = 2;
    public static int movePerSecond = 1;

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

    private boolean debug;

    public GameController() {
        setDefault();
    }

    private void setDefault() {
        debug = true;
        score = 0;
        lineNum = 0;
        gameOver = false;
        isRunning = false;
        ignoreKeyPressCounter = 0;
        clearRowLock = false;
        hardDrop = false;

        gameBoard = new GameBoard();
        tetromino = null;
        nextObj = new Tetromino();
        holdObj = null;
    }

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
            // Todo: block the button click and keypress
            clearRowLock = true;
            boolean isRunningCached = false;
            if (isRunning) {
                isRunningCached = true;
                isRunning = false;
            }

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
                        lineNum++;
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
            if (isRunningCached) {
                isRunning = true;
            }

            clearRowLock = false;
        }
    }

    public void addNextTetromino() {
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

        nextObj = new Tetromino();

        // Display the next Tetromino shape on the Right Pane
        for (TetroBox block : nextObj.getBlocks()) {
            displayNextPane.getChildren().add(block.getUiBox());
        }

    }

    private void holdTetromino() {
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
            nextObj = new Tetromino();
            // Display the next Tetromino shape on the Game Board
            for (TetroBox block : nextObj.getBlocks()) {
                displayNextPane.getChildren().add(block.getUiBox());
            }
        }
    }

    public void addPardedTetromino() {
        if (tetromino != null) {
            // Add parked tetromino to the Game Board
            gameBoard.addToBoard(tetromino);
            clearParkedRows();
        }
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

        // Run one step per second or two steps per second if current step is greater than speed up step
        if (isRunning && (counter % (1000 / movePerSecond) == 1 || hardDrop)) {
            if (debug) System.out.println("Running --------------");
            Platform.runLater(() -> {

                // Todo: Game over: reahed top or new generate can't move down
                if (Arrays.stream(gameBoard.getGrid()[0]).anyMatch(idx -> idx == 1)) {
                    // Game over
                    if (debug) System.out.println("GAME OVER --------------");
                    isRunning = false;
                    gameOver = true;
                    btnPlay.setText("GAME OVER");

                    Text over = new Text("GAME OVER");
                    over.setFill(Color.RED);
                    over.setStyle("-fx-font: 70 arial;");
                    over.setY(250);
                    over.setX(10);
                    leftPane.getChildren().add(over);
                }

                if (tetromino == null) addNextTetromino();

                boolean moved = tetromino.move(gameBoard.getIndexList(), "down");
                if (!moved) {
                    // Touched the bottom or other blocks
                    hardDrop = false;
                    // Generate new Tetromino
                    addPardedTetromino();
                    clearParkedRows();
                    addNextTetromino();
                }
            });
        }
    };

    // Handles mouse click
    @FXML
    public void handleMouseClick(MouseEvent mouseEvent) {
        isRunning = !isRunning;
        if (isRunning) {
            btnPlay.setText("PAUSE");
        } else {
            btnPlay.setText("PLAY");
        }
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
                    tetromino.move(gameBoard.getIndexList(), "right");
                }
                break;
            case LEFT:      // Non-numpad left arrow key pressed
            case KP_LEFT:   // Numeric keypad left arrow key pressed
                if (isRunning) {
                    tetromino.move(gameBoard.getIndexList(), "left");
                }
                break;
            case DOWN:     // Non-numpad down arrow key pressed
            case KP_DOWN:  // Numeric keypad down arrow key pressed
                if (isRunning) {
                    boolean moved = tetromino.move(gameBoard.getIndexList(), "down");
                    if (!moved) {
                        // Touched the bottom or other blocks
                        // Generate new Tetromino
                        addPardedTetromino();
                        // Todo: double check and add back
                        clearParkedRows();
                        addNextTetromino();
                    }
                }
                break;
            case X:
            case UP:
            case KP_UP:
                if (isRunning) {
                    tetromino.rotate(gameBoard.getIndexList(), true);
                }
                break;
            case Z:
                if (isRunning) {
                    tetromino.rotate(gameBoard.getIndexList(), false);
                }
                break;
            case SPACE:
                if (isRunning) {
                    this.hardDrop = true;
                }
                break;
            case C:
                if (isRunning) {
                    if (tetromino != null) {
                        holdTetromino();
                    }
                }
                break;
            case ENTER:    // Enter key pressed
                if (gameOver) {
                    btnPlay.setText("PLAY");
                    setDefault();
                } else {
                    isRunning = !isRunning;
                    if (isRunning) {
                        btnPlay.setText("PAUSE");
                    } else {
                        btnPlay.setText("PLAY");
                    }
                }
                break;
        }
    }

}
