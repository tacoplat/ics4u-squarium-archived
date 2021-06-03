package application;

import javafx.scene.shape.*;

public class GameBoard {


    // Size of square that tetromino is composed of
    public static final int SIZE = 25;
    // Amount tetromino moves down (should be same as size)
    public static final int MOVE = 25;

    public static final int WIDE = 12;
    public static final int HIGH = 26;

    // Max length and witdh size of game screen
    public static final int XMAX = SIZE * 12;
    public static final int YMAX = SIZE * 26;

    // Divides game screen into grid with the size each box of grid equal to SIZE
    private int[][] grid;

    public GameBoard() {
        grid = new int[XMAX / SIZE][YMAX / SIZE];
    }

    public int[][] getGrid() {
        return grid;
    }

    // Creating the squares/blocks that compose the tetromino
    public Tetromino makeRect() {

        // Pick random color between 0-100
        int randNum = (int) (Math.random() * 100);
        String pieceName;

        Rectangle[] blocks = new Rectangle[4];
        for (int i = 0; i < blocks.length; i++) {
            // Subtract 1 from size to create border/whitespace between each square so each individual square can be seen in tetromino
            blocks[i] = new Rectangle(SIZE - 1, SIZE - 1);
        }

        // If randomly generated number is less than 15, generate "j" piece
        if (randNum < 15) {
            pieceName = "j";
            blocks[0].setX(XMAX / 2 - SIZE);
            blocks[1].setX(XMAX / 2 - SIZE);
            blocks[1].setY(SIZE);
            blocks[2].setX(XMAX / 2);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2 + SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 30) {
            pieceName = "l";
            blocks[0].setX(XMAX / 2 + SIZE);
            blocks[1].setX(XMAX / 2 - SIZE);
            blocks[1].setY(SIZE);
            blocks[2].setX(XMAX / 2);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2 + SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 45) {
            pieceName = "o";
            blocks[0].setX(XMAX / 2 - SIZE);
            blocks[1].setX(XMAX / 2);
            blocks[2].setX(XMAX / 2 - SIZE);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2);
            blocks[3].setY(SIZE);

        } else if (randNum < 60) {
            pieceName = "s";
            blocks[0].setX(XMAX / 2 + SIZE);
            blocks[1].setX(XMAX / 2);
            blocks[2].setX(XMAX / 2);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2 - SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 75) {
            pieceName = "t";
            blocks[0].setX(XMAX / 2 - SIZE);
            blocks[1].setX(XMAX / 2);
            blocks[2].setX(XMAX / 2);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2 + SIZE);

        } else if (randNum < 90) {
            pieceName = "z";
            blocks[0].setX(XMAX / 2 + SIZE);
            blocks[1].setX(XMAX / 2);
            blocks[2].setX(XMAX / 2 + SIZE);
            blocks[2].setY(SIZE);
            blocks[3].setX(XMAX / 2 + SIZE + SIZE);
            blocks[3].setY(SIZE);

        } else {
            pieceName = "i";
            blocks[0].setX(XMAX / 2 - SIZE - SIZE);
            blocks[1].setX(XMAX / 2 - SIZE);
            blocks[2].setX(XMAX / 2);
            blocks[3].setX(XMAX / 2 + SIZE);

        }

        // Calls Tetromino constructor to create tetromino object
        return new Tetromino(blocks, pieceName);
    }

    public boolean isGridCellEmpty(int moveLocation) {
        if (moveLocation == 0) {
            return true;
        }
        return false;
    }

    public boolean isMovable(Tetromino tetromino, String direction) {
        int xLocation = 0;
        int yLocation = 0;

        // Check to see if spot to move in the grid is empty (0) or occupied (1)
        for (int i = 0; i < tetromino.blocks.length; i++) {
            xLocation = ((int) tetromino.blocks[i].getX() / SIZE);
            yLocation = ((int) tetromino.blocks[i].getY() / SIZE);

            if (direction == "right") {
                xLocation += 1;
                if (xLocation >= WIDE) {
                    return false;
                }

            } else if (direction == "left") {
                xLocation -= 1;
                if (xLocation < 0) {
                    return false;
                }
            } else {
                yLocation += 1;
                if (yLocation >= HIGH) {
                    return false;
                }
            }

            // Check if other blocks exist at the move location
            if (grid[xLocation][yLocation] == 1) {
                return false;
            }
        }

        return true;
    }

    // Moving the blocks
    public void move(Tetromino tetromino, String direction) {
        if (isMovable(tetromino, direction)) {
            double moveLocation = 0;

            for (int i = 0; i < tetromino.blocks.length; i++) {
                if (direction == "right") {
                    moveLocation = tetromino.blocks[i].getX() + MOVE;
                    tetromino.blocks[i].setX(moveLocation);
                } else if (direction == "left") {
                    moveLocation = tetromino.blocks[i].getX() - MOVE;
                    tetromino.blocks[i].setX(moveLocation);
                } else {
                    moveLocation = tetromino.blocks[i].getY() + MOVE;
                    tetromino.blocks[i].setY(moveLocation);
                }

            }
        }
    }
}


