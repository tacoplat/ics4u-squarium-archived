package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;

import java.util.Arrays;

public class GameBoard {


    public static final int WIDE = 12;
    public static final int HIGH = 26;

    // Max length and witdh size of game screen
    public static final int XMAX = Tetromino.SIZE * 12;
    public static final int YMAX = Tetromino.SIZE * 26;

    // Divides game screen into grid with the size each box of grid equal to SIZE
    private int[][] grid;
    private Rectangle[][] filledBlocks;

    public static int getRowIndex(Rectangle block){
        return ((int)block.getX() / Tetromino.SIZE);
    }

    public static int getColunmIndex(Rectangle block){
        return ((int)block.getY() / Tetromino.SIZE);
    }

    public GameBoard() {
        grid = new int[HIGH][WIDE];
        filledBlocks = new Rectangle[HIGH][WIDE];
    }

    public int[][] getGrid() {
        return grid;
    }

    public Rectangle[][] getFilledBlocks() {
        return filledBlocks;
    }

    public void addToGrid(Tetromino parked){
        Rectangle[] parkedBlocks = parked.getBlocks();
        for (Rectangle block: parkedBlocks) {
            int row = GameBoard.getRowIndex(block);
            int column = GameBoard.getColunmIndex(block);
            grid[column][row] = 1;
            filledBlocks[column][row] = block;
        }
    }
}


