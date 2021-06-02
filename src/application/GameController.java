package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
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
    private Button btnMove;

    // private Scene scene = new Scene(group, XMAX + 150, YMAX); // Scene will have extra space on right for score, lines, etc
    private boolean isRunning;
    private  GameBoard gameBoard;
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
        isRunning = true;
        ses = Executors.newScheduledThreadPool(1);

        gameBoard = new GameBoard();
        nextObj = gameBoard.makeRect();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // public void start(Stage stage) throws Exception {
        for (int[] cells : gameBoard.getGrid()) {
            Arrays.fill(cells, 0);
        }

        // Create score and level text
        Line line = new Line(GameBoard.XMAX, 0, GameBoard.XMAX, GameBoard.YMAX); // Line separating game play screen and score/line count
        Text scoretext = new Text("Score: ");
        scoretext.setStyle("-fx-font: 20 arials;");
        scoretext.setY(50);
        scoretext.setX(GameBoard.XMAX + 5);
        Text level = new Text("Lines: ");
        level.setStyle("-fx-font: 20 arials;");
        level.setY(100);
        level.setX(GameBoard.XMAX + 5);
        level.setFill(Color.GREEN);
        rightPane.getChildren().addAll(scoretext, line, level);

        // Create first block and stage
        tetromino = nextObj;
        leftPane.getChildren().addAll(tetromino.blocks[0], tetromino.blocks[1], tetromino.blocks[2], tetromino.blocks[3]);
        //moveOnKeyPress(tetromino);
        nextObj = gameBoard.makeRect();
//        stage.setScene(scene);
//        stage.setTitle("SILLY SHAPES - CLASSIC: EASY MODE");
//        stage.show();

        splitPane.setFocusTraversable(true);

        try {
            // Init delay = 0, repeat the task every 1 second
            scheduledFuture = ses.scheduleAtFixedRate(taskUpdating, 0, 100, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            System.out.println("Exception:" + e.getMessage());
        }

    }

    /**
     * Run the tasks with one millisecond delay
     */
    private Runnable taskUpdating = () -> {
        counter++;
        if(counter > 1000000) counter = 0;

        // Run sort steps if autorun is enabled
        // run one step per second or two steps per second if current step is greater than speed up step
        if(counter%10 == 1) {
            System.out.println("runnable --------------");
            Platform.runLater(() -> {
                for (int i = 0; i < tetromino.blocks.length; i++) {
                    if (tetromino.blocks[i].getY() == 0)
                        top++;
                    else
                        top = 0;
                }

                if (top == 2) {
                    // GAME OVER -- MAKE SURE TO CHANGE LATER TO SWITCH TO GAMEOVER SCREEN
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

                if (isRunning) {
                    // GameController.move(tetromino, "down");
                    // scoretext.setText("Score: " + Integer.toString(score));
                    // level.setText("Lines: " + Integer.toString(lineNum));
                }
            });
        }
    };

    /**
     * Shut down ScheduledExecutorService when the app is exited
     */
    public void shutdownSes(){
        if(debug) System.out.println("Shutdown ScheduledExecutorService");
        scheduledFuture.cancel(true);
        ses.shutdown();
    }


    // Handles arrow key press to move
    @FXML
    void handleKeyPress(KeyEvent event) {
        System.out.println("debug..." + event.getText());
        switch (event.getCode()) {
            case RIGHT:
            case KP_RIGHT:
                gameBoard.move(tetromino, "right");
                break;
            case LEFT:
            case KP_LEFT:
                gameBoard.move(tetromino, "left");
        }
    }
//
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
