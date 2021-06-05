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

    public static int getPositionX(Rectangle block){
        return ((int)block.getX() / Tetromino.SIZE);
    }

    public static int getPositionY(Rectangle block){
        return ((int)block.getY() / Tetromino.SIZE);
    }

    public GameBoard() {
        grid = new int[WIDE][HIGH];
        filledBlocks = new Rectangle[WIDE][HIGH];
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
            int xPosition = GameBoard.getPositionX(block);
            int yPosition = GameBoard.getPositionY(block);
            grid[xPosition][yPosition] = 1;
            filledBlocks[xPosition][yPosition] = block;
        }
    }
}


