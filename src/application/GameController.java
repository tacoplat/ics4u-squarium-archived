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

    // private Scene scene = new Scene(group, XMAX + 150, YMAX); // Scene will have extra space on right for score, lines, etc
    private boolean isRunning;
    private boolean cleanupLock;
    private boolean hardDrop;
    private GameBoard gameBoard;
    private Tetromino nextObj;
    private Tetromino nextForDisplay;
    private Tetromino holdObj;
    private Tetromino holdForDisplay;

    // Counter for score and number of lines cleared
    private int lineNum;
    private int score;

    private Tetromino tetromino;

    // Timer counter
    private int counter;
    // Scheduled Executor Service to execute tasks repeatedly with a fixed interval of time
    private ScheduledExecutorService ses;
    // ScheduledFuture can be used to get time left before next task execution
    private ScheduledFuture<?> scheduledFuture;

    private boolean debug;

    public GameController() {
        debug = true;
        score = 0;
        lineNum = 0;
        isRunning = false;
        cleanupLock = false;
        hardDrop = false;
        ses = Executors.newScheduledThreadPool(1);

        gameBoard = new GameBoard();
        nextObj = new Tetromino();
        nextForDisplay = null;
        holdObj = null;
        holdForDisplay = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // Init delay = 0, repeat the task every 1 millisecond
            scheduledFuture = ses.scheduleAtFixedRate(taskUpdating, 0, 1, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }

        // Create first Tetromino
        generateNextTetromino();
    }

    public void cleanupGrid() {
        if (!cleanupLock) {
            // Todo: block the button click and keypress
            cleanupLock = true;
            boolean isRunningCache = false;
            if (isRunning) {
                isRunningCache = true;
                isRunning = false;
            }

            int[][] grid = gameBoard.getGrid();
            Rectangle[][] filledBlocks = gameBoard.getFilledBlocks();
            int topRowIdx = GameBoard.HIGH;
            for (int row = 0; row < GameBoard.HIGH; row++) {
                if (Arrays.stream(grid[row]).anyMatch(idx -> idx == 1)) {
                    if (topRowIdx == GameBoard.HIGH) topRowIdx = row;
                    if (Arrays.stream(grid[row]).allMatch(idx -> idx == 1)) {
                        // Delete the row and move the blocks on top one row down
                        for (Rectangle block : filledBlocks[row]) {
                            leftPane.getChildren().remove(block);
                        }
                        lineNum++;
                        Arrays.fill(grid[row], 0);
                        Arrays.fill(filledBlocks[row], null);
                        for (int k = row - 1; k >= topRowIdx; k--) {
                            var tmpRow = filledBlocks[k];
                            for (int m = 0; m < GameBoard.WIDE; m++) {
                                if (tmpRow[m] != null) {
                                    tmpRow[m].setY(tmpRow[m].getY() + Tetromino.SIZE);
                                    grid[k][m] = 0;
                                    grid[k + 1][m] = 1;
                                    filledBlocks[k + 1][m] = tmpRow[m];
                                    filledBlocks[k][m] = null;
                                }
                            }
                        }
                    }
                }
            }
            if (isRunningCache) {
                isRunning = true;
            }
        }
    }

    public void generateNextTetromino() {
        if (tetromino != null) {
            // Add parked tetromino to the game board
            gameBoard.addToGrid(tetromino);
            cleanupGrid();
        }

        tetromino = nextObj;
        for (Rectangle block : tetromino.getBlocks()) {
            leftPane.getChildren().add(block);
        }

        // Display the next Tetromino shape on the right Pane
        nextObj = new Tetromino();

        if (nextForDisplay != null) {
            for (Rectangle block : nextForDisplay.getBlocks()) {
                displayNextPane.getChildren().remove(block);
            }
        }
        Rectangle[] nextBlocksForDisplay = new Rectangle[4];
        int i = 0;
        for (Rectangle block : nextObj.getBlocks()) {
            double x = block.getX() - (GameBoard.XMAX / 2) + 65.0;
            double y = block.getY();

            nextBlocksForDisplay[i] = new Rectangle(x, y, Tetromino.SIZE, Tetromino.SIZE);
            displayNextPane.getChildren().add(nextBlocksForDisplay[i]);
            i++;
        }
        nextForDisplay = new Tetromino(nextBlocksForDisplay, nextObj.getPieceName(), nextObj.getColor());
    }

    /**
     * Run the tasks with one millisecond delay
     */
    private Runnable taskUpdating = () -> {
        counter++;
        if (counter > 1000000) counter = 0;

        // Run one step per second or two steps per second if current step is greater than speed up step
        if (isRunning && (counter % 1000 == 1 || hardDrop)) {
            if (debug) System.out.println("Moving --------------");
            Platform.runLater(() -> {

                if (Arrays.stream(gameBoard.getGrid()[0]).anyMatch(idx -> idx == 1)) {
                    // Game over
                    if (debug) System.out.println("GAME OVER --------------");
                    isRunning = false;

                    Text over = new Text("GAME OVER");
                    over.setFill(Color.RED);
                    over.setStyle("-fx-font: 70 arial;");
                    over.setY(250);
                    over.setX(10);
                    leftPane.getChildren().add(over);

                }

                int movable = tetromino.move(gameBoard.getGrid(), "down");
                if (movable == 2 || movable == 3) {
                    // Touched the bottom or other blocks
                    hardDrop = false;
                    // Generate new Tetromino
                    generateNextTetromino();
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
        int movable;
        if (debug) System.out.println("debug..." + event.getText());
        // https://openjfx.io/javadoc/16/javafx.graphics/javafx/scene/input/KeyCode.html
        switch (event.getCode()) {
            case RIGHT:     // Non-numpad right arrow key pressed
            case KP_RIGHT:  // Numeric keypad right arrow key pressed
                if (isRunning) {
                    tetromino.move(gameBoard.getGrid(), "right");
                }
                break;
            case LEFT:      // Non-numpad left arrow key pressed
            case KP_LEFT:   // Numeric keypad left arrow key pressed
                if (isRunning) {
                    tetromino.move(gameBoard.getGrid(), "left");
                }
                break;

            case DOWN:     // Non-numpad down arrow key pressed
            case KP_DOWN:  // Numeric keypad down arrow key pressed
                if (isRunning) {
                    movable = tetromino.move(gameBoard.getGrid(), "down");
                    if (movable == 2 || movable == 3) {
                        // Touched the bottom or other blocks
                        // Generate new Tetromino
                        generateNextTetromino();
                    }
                }
                break;
            case ENTER:    // Enter key pressed
                isRunning = !isRunning;
                if (isRunning) {
                    btnPlay.setText("PAUSE");
                } else {
                    btnPlay.setText("PLAY");
                }
                break;
            case UP:
            case KP_UP:
                tetromino.changeOrientation(gameBoard.getGrid());
                break;
            case SPACE:
                this.hardDrop = true;
                break;
            /*
            case C:
                if (tetromino != null) {
                    if(holdObj != null ){
                        Tetromino tmpObj = holdObj;
                        holdObj = tetromino;
                        tetromino = tmpObj;
                    }else{
                        holdObj = tetromino;
                        tetromino = nextObj;
                        nextObj = new Tetromino();
                    }

                    if (holdForDisplay != null) {
                        for (Rectangle block : holdForDisplay.getBlocks()) {
                            displayHoldPane.getChildren().remove(block);
                        }
                    }
                    Rectangle[] holdBlocksForDisplay = new Rectangle[4];
                    int i = 0;
                    for (Rectangle block : nextObj.getBlocks()) {
                        double x = block.getX() - (GameBoard.XMAX / 2) + 65.0;
                        double y = block.getY();

                        holdBlocksForDisplay[i] = new Rectangle(x, y, Tetromino.SIZE, Tetromino.SIZE);
                        displayHoldPane.getChildren().add(holdBlocksForDisplay[i]);
                        i++;
                    }
                    holdForDisplay = new Tetromino(holdBlocksForDisplay, holdObj.getPieceName(), holdObj.getColor());

                    for (Rectangle block : holdForDisplay.getBlocks()) {
                        displayNextPane.getChildren().remove(block);
                    }
                }
                break;
                */
        }
    }

}
