package application;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class ClassicEasyMain {

    // Initialize variables

    // Size of square that tetromino is composed of
    public static final int SIZE = 25;
    // Amount tetromino moves down (should be same as size)
    public static final int MOVE = 25;

    // Max length and witdh size of game screen
    public static final int XMAX = SIZE * 12;
    public static final int YMAX = SIZE * 24;

    // Divides game screen into grid with the size each box of grid equal to SIZE
    public static int[][] grid = new int[XMAX / SIZE][YMAX / SIZE];

    private Pane group = new Pane();
    private Tetromino object;

    private Scene scene = new Scene(group, XMAX + 150, YMAX); // Scene will have extra space on right for score, lines, etc
    private boolean game = true;
    private Tetromino nextObj = GameController.makeRect();

    // Counter for score and number of lines cleared
    private int lineNum = 0;
    public int score = 0;

    public int top = 0;

    public void start(Stage stage) throws Exception {
        for (int[] cells : grid) {
            Arrays.fill(cells, 0);
        }

        // Create score and level text
        Line line = new Line(XMAX, 0, XMAX, YMAX); // Line separating game play screen and score/line count
        Text scoretext = new Text("Score: ");
        scoretext.setStyle("-fx-font: 20 arials;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 5);
        Text level = new Text("Lines: ");
        level.setStyle("-fx-font: 20 arials;");
        level.setY(100);
        level.setX(XMAX + 5);
        level.setFill(Color.GREEN);
        group.getChildren().addAll(scoretext, line, level);

         // Create first block and stage
        Tetromino tetromino = nextObj;
        group.getChildren().addAll(tetromino.blocks[0], tetromino.blocks[1], tetromino.blocks[2], tetromino.blocks[3]);
        moveOnKeyPress(tetromino);
        nextObj = GameController.makeRect();
        stage.setScene(scene);
        stage.setTitle("SILLY SHAPES - CLASSIC: EASY MODE");
        stage.show();

        // Timer
        Timer fall = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        for (int i = 0; i < object.blocks.length; i++) {
                            if (object.blocks[i].getY() == 0)
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
                            group.getChildren().add(over);
                            game = false;
                        }
                        // Exit
                        if (top == 15) {
                            System.exit(0);
                        }

//                        if (game) {
//                            MoveDown(object);
//                            scoretext.setText("Score: " + Integer.toString(score));
//                            level.setText("Lines: " + Integer.toString(lineNum));
//                        }
                    }
                });
            }
        };


        fall.schedule(task, 0, 300);  // period 300 is Speed 300

    }

    // Handles arrow key press to move
    private void moveOnKeyPress(Tetromino tetromino) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()) {
                    case RIGHT:
                        GameController.move(tetromino, "right");
                        break;
                    case LEFT:
                        GameController.move(tetromino, "left");
                }
            }
        });
    }

}
