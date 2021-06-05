package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.w3c.dom.css.Rect;

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
    private Button btnPlay;

    // private Scene scene = new Scene(group, XMAX + 150, YMAX); // Scene will have extra space on right for score, lines, etc
    private boolean isRunning;
    private GameBoard gameBoard;
    private Tetromino nextObj;

    // Counter for score and number of lines cleared
    private int lineNum;
    private int score;

    private int top;

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
        top = 0;
        score = 0;
        lineNum = 0;
        isRunning = false;
        ses = Executors.newScheduledThreadPool(1);

        gameBoard = new GameBoard();
        nextObj = new Tetromino();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // Init delay = 0, repeat the task every 100 millisecond
            scheduledFuture = ses.scheduleAtFixedRate(taskUpdating, 0, 100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }

        // Create first Tetromino
        generateNextTetromino();
    }

    public void cleanupGrid(){
        int[][] grid = gameBoard.getGrid();
        Rectangle[][] filledBlocks = gameBoard.getFilledBlocks();
        int top = GameBoard.HIGH;
        for(int i=0; i<GameBoard.WIDE; i++){
            if(Arrays.stream(grid[i]).anyMatch(idx -> idx == 1)) {
                if(top == GameBoard.HIGH) top = i;
                if (Arrays.stream(grid[i]).allMatch(idx -> idx == 1)) {
                    // Delete the row and move the blocks on top one row down
                    for (Rectangle block : filledBlocks[i]) {
                        leftPane.getChildren().remove(block);
                    }
                    for(int k=i; k>=top; k--){
                        for(Rectangle block: filledBlocks[k]){
                           block.setY(block.getY() + Tetromino.SIZE);
                        }
                    }
                }
            }
        }
    }

    public void generateNextTetromino(){
        if(tetromino != null){
            // Add parked tetromino to the game board
            gameBoard.addToGrid(tetromino);

        }

        tetromino = nextObj;
        for (Rectangle block : tetromino.getBlocks()) {
            leftPane.getChildren().add(block);
        }

        // Todo: Display the next Tetromino shape on the right Pane
        nextObj = new Tetromino();
    }

    /**
     * Run the tasks with one millisecond delay
     */
    private Runnable taskUpdating = () -> {
        counter++;
        if (counter > 1000000) counter = 0;

        // Run sort steps if autorun is enabled
        // run one step per second or two steps per second if current step is greater than speed up step
        if (isRunning && counter % 10 == 1) {
            if (debug) System.out.println("Moving --------------");
            Platform.runLater(() -> {
                for (Rectangle block : tetromino.getBlocks()) {
                    if (block.getY() == 0) {
                        top++;
                    } else {
                        top = 0;
                    }
                }

                if (top == 2) {
                    // GAME OVER -- MAKE SURE TO CHANGE LATER TO SWITCH TO GAMEOVER SCREEN
                    if (debug) System.out.println("GAME OVER --------------");
                    Text over = new Text("GAME OVER");
                    over.setFill(Color.RED);
                    over.setStyle("-fx-font: 70 arial;");
                    over.setY(250);
                    over.setX(10);
                    leftPane.getChildren().add(over);
                    isRunning = false;
                }

                // Exit
                if (top == 15) {
                    System.exit(0);
                }

                int movable = tetromino.move(gameBoard.getGrid(), "down");
                if(movable == 2 || movable == 3) {
                    // Touched the bottom or other blocks
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
        }
    }


//    private void MoveTurn(Tetromino tetromino) {
//        int t = tetromino.tetromino;
//        Rectangle blocks;
//        for (int i = 0; i < tetromino.blocks.length; i++) {
//            blocks = tetromino.blocks[i];
//        }
//        switch (tetromino.getPieceName()) {
//            if ((t == 1 &&) )
//        }
//    }

}
