package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Tetromino {

    // Size of square that tetromino is composed of
    public static final int SIZE = 25;

    // Initialize each block/square making up tetromino piece
    private Rectangle[] blocks;

    // Name of tetromino piece based on their shape
    private String pieceName;

    private Color color;

    private int orientation;

    // Constructor
    public Tetromino() {
        orientation = 1;
        makeRect();
        setColor();
    }

    // Creating the squares/blocks that compose the tetromino
    public void makeRect() {

        // Pick random color between 0-100
        int randNum = (int) (Math.random() * 100);

        blocks = new Rectangle[4];
        for (int i = 0; i < blocks.length; i++) {
            // Subtract 1 from size to create border/whitespace between each square so each individual square can be seen in tetromino
            blocks[i] = new Rectangle(SIZE - 1, SIZE - 1);
        }

        int boardMidX = GameBoard.XMAX / 2;
        // If randomly generated number is less than 15, generate "j" piece
        if (randNum < 15) {
            pieceName = "j";
            blocks[0].setX(boardMidX - SIZE);
            blocks[1].setX(boardMidX - SIZE);
            blocks[1].setY(SIZE);
            blocks[2].setX(boardMidX);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX + SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 30) {
            pieceName = "l";
            blocks[0].setX(boardMidX + SIZE);
            blocks[1].setX(boardMidX - SIZE);
            blocks[1].setY(SIZE);
            blocks[2].setX(boardMidX);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX + SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 45) {
            pieceName = "o";
            blocks[0].setX(boardMidX - SIZE);
            blocks[1].setX(boardMidX);
            blocks[2].setX(boardMidX - SIZE);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX);
            blocks[3].setY(SIZE);

        } else if (randNum < 60) {
            pieceName = "s";
            blocks[0].setX(boardMidX + SIZE);
            blocks[1].setX(boardMidX);
            blocks[2].setX(boardMidX);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX - SIZE);
            blocks[3].setY(SIZE);

        } else if (randNum < 75) {
            pieceName = "t";
            blocks[0].setX(boardMidX - SIZE);
            blocks[1].setX(boardMidX);
            blocks[2].setX(boardMidX);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX + SIZE);

        } else if (randNum < 90) {
            pieceName = "z";
            blocks[0].setX(boardMidX + SIZE);
            blocks[1].setX(boardMidX);
            blocks[2].setX(boardMidX + SIZE);
            blocks[2].setY(SIZE);
            blocks[3].setX(boardMidX + SIZE + SIZE);
            blocks[3].setY(SIZE);

        } else {
            pieceName = "i";
            blocks[0].setX(boardMidX - SIZE - SIZE);
            blocks[1].setX(boardMidX - SIZE);
            blocks[2].setX(boardMidX);
            blocks[3].setX(boardMidX + SIZE);

        }
    }

    // Set color of each rectangle making up tetromino
    public void setColor() {
        switch (pieceName) {
            case "j":
                color = Color.SLATEGRAY;
                break;
            case "l":
                color = Color.DARKGOLDENROD;
                break;
            case "o":
                color = Color.INDIANRED;
                break;
            case "s":
                color = Color.FORESTGREEN;
                break;
            case "t":
                color = Color.DARKBLUE;
                break;
            case "z":
                color = Color.HOTPINK;
                break;
        }

        // Set color for each block
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setFill(color);
        }

    }

    /**
     *
     * @param grid
     * @param direction
     * @return 0: Can't move; 1: Movable; 2: Touched the bottom; 3: Touched other blocks
     */
    public int isMovable(int[][] grid, String direction) {
        int xPosition = 0;
        int yPosition = 0;

        // Check to see if spot to move in the grid is empty (0) or occupied (1)
        for (Rectangle block: blocks) {
            xPosition = GameBoard.getPositionX(block);
            yPosition = GameBoard.getPositionY(block);

            if (direction == "right") {
                xPosition += 1;
                if (xPosition >= GameBoard.WIDE) {
                    return 0;
                }

            } else if (direction == "left") {
                xPosition -= 1;
                if (xPosition < 0) {
                    return 0;
                }
            } else {
                yPosition += 1;
                if (yPosition >= GameBoard.HIGH) {
                    // block touched the bottom
                    return 2;
                }
            }

            // Check if other blocks exist at the move location
            if (grid[xPosition][yPosition] == 1) {
                return 3;
            }
        }

        return 1;
    }

    // Moving the blocks

    /**
     *
     * @param grid
     * @param direction
     * @return 0: Can't move; 1: Movable; 2: Touched the bottom; 3: Touched other blocks
     */
    public int move(int[][] grid, String direction) {
        int movable = isMovable(grid, direction);
        if (movable == 1) {
            double moveLocation = 0;

            for (int i = 0; i < blocks.length; i++) {
                if (direction == "right") {
                    moveLocation = blocks[i].getX() + SIZE;
                    blocks[i].setX(moveLocation);
                } else if (direction == "left") {
                    moveLocation = blocks[i].getX() - SIZE;
                    blocks[i].setX(moveLocation);
                } else {
                    moveLocation = blocks[i].getY() + SIZE;
                    blocks[i].setY(moveLocation);
                }
            }
        }
        return movable;
    }

    public void changeOrientation() {
        if (orientation != 4) {
            orientation++;
        } else {
            orientation = 1;
        }
    }

    // Getter
    public String getPieceName() {
        return pieceName;
    }

    /**
     *
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter
     * @return Array of Rectangle block
     */
    public Rectangle[] getBlocks() {
        return blocks;
    }
}
