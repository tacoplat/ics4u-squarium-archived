package application;

import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.NonInvertibleTransformException;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

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

    public Tetromino(Rectangle[] blocks, String pieceName, Color color) {
        orientation = 1;
        this.blocks = blocks;
        this.pieceName = pieceName;
        this.color = color;
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
//        switch (pieceName) {
//            case "j":
//                color = Color.SLATEGRAY;
//                break;
//            case "l":
//                color = Color.DARKGOLDENROD;
//                break;
//            case "o":
//                color = Color.INDIANRED;
//                break;
//            case "s":
//                color = Color.FORESTGREEN;
//                break;
//            case "t":
//                color = Color.DARKBLUE;
//                break;
//            case "z":
//                color = Color.HOTPINK;
//                break;
//            case "i":
//                color = Color.CYAN;
//                break;
//        }

        switch (pieceName) {
            case "j":
                color = Color.BLUE;
                break;
            case "l":
                color = Color.ORANGE;
                break;
            case "o":
                color = Color.YELLOW;
                break;
            case "s":
                color = Color.GREEN;
                break;
            case "t":
                color = Color.PURPLE;
                break;
            case "z":
                color = Color.RED;
                break;
            case "i":
                color = Color.CYAN;
                break;
        }

        // Set color for each block
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setFill(color);
        }

    }

    /**
     * @param grid
     * @param direction
     * @return 0: Can't move; 1: Movable; 2: Touched the bottom; 3: Touched other blocks
     */
    public int isMovable(int[][] grid, String direction) {
        int rowIndex = 0;
        int colunmIndex = 0;

        // Check to see if spot to move in the grid is empty (0) or occupied (1)
        for (Rectangle block : blocks) {
            rowIndex = GameBoard.getRowIndex(block);
            colunmIndex = GameBoard.getColunmIndex(block);

            if (direction == "right") {
                rowIndex += 1;
                if (rowIndex >= GameBoard.WIDE) {
                    return 0;
                }

            } else if (direction == "left") {
                rowIndex -= 1;
                if (rowIndex < 0) {
                    return 0;
                }
            } else {
                colunmIndex += 1;
                if (colunmIndex >= GameBoard.HIGH) {
                    // block touched the bottom
                    return 2;
                }
            }

            // Check if other blocks exist at the move location
            if (grid[colunmIndex][rowIndex] == 1) {
                return 3;
            }
        }

        return 1;
    }

    // Moving the blocks

    /**
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

    private void MoveDown(Rectangle block) {
        if (block.getY() + SIZE < GameBoard.YMAX)
            block.setY(block.getY() + SIZE);
    }

    private void MoveLeft(Rectangle block) {
        if (block.getX() - SIZE >= 0)
            block.setX(block.getX() - SIZE);
    }

    private void MoveRight(Rectangle block) {
        if (block.getX() + SIZE <= GameBoard.XMAX - SIZE)
            block.setX(block.getX() + SIZE);
    }

    private void MoveUp(Rectangle block) {
        if (block.getY() - SIZE > 0)
            block.setY(block.getY() - SIZE);
    }

    private boolean cB(int[][] grid, Rectangle rect, int x, int y) {
        boolean xb = false;
        boolean yb = false;
        if (x >= 0)
            xb = rect.getX() + x * SIZE <= GameBoard.XMAX - SIZE;
        if (x < 0)
            xb = rect.getX() + x * SIZE >= 0;
        if (y >= 0)
            yb = rect.getY() - y * SIZE > 0;
        if (y < 0)
            yb = rect.getY() + y * SIZE < GameBoard.YMAX;
        return xb && yb && grid[((int) rect.getY() / SIZE) - y][((int) rect.getX() / SIZE) + x] == 0;
    }

    private void updateOrientation() {
        if (orientation != 4) {
            orientation++;
        } else {
            orientation = 1;
        }
    }

    public void changeOrientation(int[][] grid) {
        switch (pieceName) {
            case "j":
                if (orientation == 1 && cB(grid, blocks[0], 1, -1) && cB(grid, blocks[2], -1, -1) && cB(grid, blocks[3], -2, -2)) {
                    MoveRight(blocks[0]);
                    MoveDown(blocks[0]);
                    MoveDown(blocks[2]);
                    MoveLeft(blocks[2]);
                    MoveDown(blocks[3]);
                    MoveDown(blocks[3]);
                    MoveLeft(blocks[3]);
                    MoveLeft(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[0], -1, -1) && cB(grid, blocks[2], -1, 1) && cB(grid, blocks[3], -2, 2)) {
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    MoveLeft(blocks[3]);
                    MoveLeft(blocks[3]);
                    MoveUp(blocks[3]);
                    MoveUp(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[0], -1, 1) && cB(grid, blocks[2], 1, 1) && cB(grid, blocks[3], 2, 2)) {
                    MoveLeft(blocks[0]);
                    MoveUp(blocks[0]);
                    MoveUp(blocks[2]);
                    MoveRight(blocks[2]);
                    MoveUp(blocks[3]);
                    MoveUp(blocks[3]);
                    MoveRight(blocks[3]);
                    MoveRight(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[0], 1, 1) && cB(grid, blocks[2], 1, -1) && cB(grid, blocks[3], 2, -2)) {
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    MoveRight(blocks[3]);
                    MoveRight(blocks[3]);
                    MoveDown(blocks[3]);
                    MoveDown(blocks[3]);
                    updateOrientation();
                    break;
                }
                break;
            case "l":
                if (orientation == 1 && cB(grid, blocks[0], 1, -1) && cB(grid, blocks[2], 1, 1) && cB(grid, blocks[1], 2, 2)) {
                    MoveRight(blocks[0]);
                    MoveDown(blocks[0]);
                    MoveUp(blocks[2]);
                    MoveRight(blocks[2]);
                    MoveUp(blocks[1]);
                    MoveUp(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveRight(blocks[1]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[0], -1, -1) && cB(grid, blocks[1], 2, -2) && cB(grid, blocks[2], 1, -1)) {
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveRight(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveDown(blocks[1]);
                    MoveDown(blocks[1]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[0], -1, 1) && cB(grid, blocks[2], -1, -1) && cB(grid, blocks[1], -2, -2)) {
                    MoveLeft(blocks[0]);
                    MoveUp(blocks[0]);
                    MoveDown(blocks[2]);
                    MoveLeft(blocks[2]);
                    MoveDown(blocks[1]);
                    MoveDown(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveLeft(blocks[1]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[0], 1, 1) && cB(grid, blocks[1], -2, 2) && cB(grid, blocks[2], -1, 1)) {
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveLeft(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveUp(blocks[1]);
                    MoveUp(blocks[1]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    updateOrientation();
                    break;
                }
                break;
            case "o":
                break;
            case "s":
                if (orientation == 1 && cB(grid, blocks[0], -1, -1) && cB(grid, blocks[2], -1, 1) && cB(grid, blocks[3], 0, 2)) {
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    MoveUp(blocks[3]);
                    MoveUp(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[0], 1, 1) && cB(grid, blocks[2], 1, -1) && cB(grid, blocks[3], 0, -2)) {
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    MoveDown(blocks[3]);
                    MoveDown(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[0], -1, -1) && cB(grid, blocks[2], -1, 1) && cB(grid, blocks[3], 0, 2)) {
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    MoveUp(blocks[3]);
                    MoveUp(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[0], 1, 1) && cB(grid, blocks[2], 1, -1) && cB(grid, blocks[3], 0, -2)) {
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    MoveDown(blocks[3]);
                    MoveDown(blocks[3]);
                    updateOrientation();
                    break;
                }
                break;
            case "t":
                if (orientation == 1 && cB(grid, blocks[0], 1, 1) && cB(grid, blocks[3], -1, -1) && cB(grid, blocks[2], -1, 1)) {
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveDown(blocks[3]);
                    MoveLeft(blocks[3]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[0], 1, -1) && cB(grid, blocks[3], -1, 1) && cB(grid, blocks[2], 1, 1)) {
                    MoveRight(blocks[0]);
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[3]);
                    MoveUp(blocks[3]);
                    MoveUp(blocks[2]);
                    MoveRight(blocks[2]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[0], -1, -1) && cB(grid, blocks[3], 1, 1) && cB(grid, blocks[2], 1, -1)) {
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveUp(blocks[3]);
                    MoveRight(blocks[3]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[0], -1, 1) && cB(grid, blocks[3], 1, -1) && cB(grid, blocks[2], -1, -1)) {
                    MoveLeft(blocks[0]);
                    MoveUp(blocks[0]);
                    MoveRight(blocks[3]);
                    MoveDown(blocks[3]);
                    MoveDown(blocks[2]);
                    MoveLeft(blocks[2]);
                    updateOrientation();
                    break;
                }
                break;
            case "z":
                if (orientation == 1 && cB(grid, blocks[1], 1, 1) && cB(grid, blocks[2], -1, 1) && cB(grid, blocks[3], -2, 0)) {
                    MoveUp(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    MoveLeft(blocks[3]);
                    MoveLeft(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[1], -1, -1) && cB(grid, blocks[2], 1, -1) && cB(grid, blocks[3], 2, 0)) {
                    MoveDown(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    MoveRight(blocks[3]);
                    MoveRight(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[1], 1, 1) && cB(grid, blocks[2], -1, 1) && cB(grid, blocks[3], -2, 0)) {
                    MoveUp(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveLeft(blocks[2]);
                    MoveUp(blocks[2]);
                    MoveLeft(blocks[3]);
                    MoveLeft(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[1], -1, -1) && cB(grid, blocks[2], 1, -1) && cB(grid, blocks[3], 2, 0)) {
                    MoveDown(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveRight(blocks[2]);
                    MoveDown(blocks[2]);
                    MoveRight(blocks[3]);
                    MoveRight(blocks[3]);
                    updateOrientation();
                    break;
                }
                break;
            case "i":
                if (orientation == 1 && cB(grid, blocks[0], 2, 2) && cB(grid, blocks[1], 1, 1) && cB(grid, blocks[3], -1, -1)) {
                    MoveUp(blocks[0]);
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveUp(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveDown(blocks[3]);
                    MoveLeft(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 2 && cB(grid, blocks[0], -2, -2) && cB(grid, blocks[1], -1, -1) && cB(grid, blocks[3], 1, 1)) {
                    MoveDown(blocks[0]);
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveDown(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveUp(blocks[3]);
                    MoveRight(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 3 && cB(grid, blocks[0], 2, 2) && cB(grid, blocks[1], 1, 1) && cB(grid, blocks[3], -1, -1)) {
                    MoveUp(blocks[0]);
                    MoveUp(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveRight(blocks[0]);
                    MoveUp(blocks[1]);
                    MoveRight(blocks[1]);
                    MoveDown(blocks[3]);
                    MoveLeft(blocks[3]);
                    updateOrientation();
                    break;
                }
                if (orientation == 4 && cB(grid, blocks[0], -2, -2) && cB(grid, blocks[1], -1, -1) && cB(grid, blocks[3], 1, 1)) {
                    MoveDown(blocks[0]);
                    MoveDown(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveLeft(blocks[0]);
                    MoveDown(blocks[1]);
                    MoveLeft(blocks[1]);
                    MoveUp(blocks[3]);
                    MoveRight(blocks[3]);
                    updateOrientation();
                    break;
                }
                break;
        }
    }

//    public void changeOrientation(boolean clockwise) {
//        double baseX = 0.0;
//        double baseY = 0.0;
//
//        double angle = clockwise ? 90.0 : -90.0;
//        orientation = clockwise ? orientation + 1 : orientation - 1;
//        if (orientation > 3) {
//            orientation = 1;
//        } else if (orientation < 0) {
//            orientation = 3;
//        }
//
//        switch (pieceName) {
//            case "j":
//            case "l":
//            case "s":
//                baseX = blocks[2].getX() + SIZE / 2.0;
//                baseY = blocks[2].getY() + SIZE / 2.0;
//                break;
//            case "t":
//                baseX = blocks[1].getX() + SIZE / 2.0;
//                baseY = blocks[1].getY() + SIZE / 2.0;
//                break;
//            case "z":
//                baseX = blocks[2].getX() + SIZE / 2.0;
//                baseY = blocks[2].getY();
//                break;
//            case "i":
//                baseX = blocks[2].getX();
//                baseY = blocks[2].getY() + SIZE / 2.0;
//                break;
//            case "o":
//                // No need to rotate
//                break;
//        }
//
//        for (Rectangle block : blocks) {
//            //creating the rotation transformation
//            Rotate rotate = new Rotate();
//            rotate.setAngle(angle);
//            //Setting pivot points for the rotation
//            rotate.setPivotX(baseX);
//            rotate.setPivotY(baseY);
//
//            block.getTransforms().addAll(rotate);
//            // block.getTransforms().add(Transform.rotate(angle, baseX, baseY));
//            // addRotate(block, baseX, baseY, angle);
//        }
//    }


    // Getter
    public String getPieceName() {
        return pieceName;
    }

    /**
     * @return
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter
     *
     * @return Array of Rectangle block
     */
    public Rectangle[] getBlocks() {
        return blocks;
    }
}
