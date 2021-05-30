package application;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

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
    public static final int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE];

    private Pane group = new Pane();
    private Tetromino object;

    private Scene scene = new Scene(group, XMAX + 150, YMAX); // Scene will have extra space on right for score, lines, etc
    private boolean game = true;
    private Tetromino nextObj = GameController.makeRect();

    // Counter for score and number of lines cleared
    private int lineNum = 0;
    // public int score = 0;


}
