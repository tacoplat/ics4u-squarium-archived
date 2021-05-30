package application;

import javafx.scene.shape.*;

public class GameController {

    // Get numbers and MESH from ClassicEasyMain class
    public static final int MOVE = ClassicEasyMain.MOVE;
    public static final int SIZE = ClassicEasyMain.SIZE;
    public static final int XMAX = ClassicEasyMain.XMAX;
    public static final int YMAX = ClassicEasyMain.YMAX;
    public static int[][] MESH = ClassicEasyMain.MESH;

    // Creating the squares/blocks that compose the tetromino
    public static Tetromino makeRect() {

        // Pick random color between 0-100
        int block = (int) (Math.random() * 100);
        String pieceName;

        Rectangle a = new Rectangle(SIZE - 1, SIZE - 1), // Subtract 1 from size to create border/whitespace between each square so each individual square can be seen in tetromino
                b = new Rectangle(SIZE - 1, SIZE - 1),
                c = new Rectangle(SIZE - 1, SIZE - 1),
                d = new Rectangle(SIZE - 1, SIZE - 1);

        // If randomly generated number is less than 15, generate "j" piece
        if (block < 15) {
            pieceName = "j";
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);

        } else if (block < 30) {
            pieceName = "l";
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);

        } else if (block < 45) {
            pieceName = "o";
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 - SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2);
            d.setY(SIZE);

        } else if (block < 60) {
            pieceName = "s";
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 - SIZE);
            d.setY(SIZE);

        } else if (block < 75) {
            pieceName = "t";
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);

        } else if (block < 90) {
            pieceName = "z";
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 + SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE + SIZE);
            d.setY(SIZE);

        } else {
            pieceName = "i";
            a.setX(XMAX / 2 - SIZE - SIZE);
            b.setX(XMAX / 2 - SIZE);
            c.setX(XMAX / 2);
            d.setX(XMAX / 2 + SIZE);

        }

        return new Tetromino(a, b, c, d, pieceName);
    }

    // Moving the blocks right
    public static void MoveRight(Tetromino tetromino) {
        // Checks to see if space tetromino is going to move to is outside of limit, if not then proceed
        if ((tetromino.a.getX() + MOVE <= XMAX - SIZE) && (tetromino.b.getX() + MOVE <= XMAX - SIZE) && (tetromino.c.getX() + MOVE <= XMAX - SIZE) && (tetromino.d.getX() + MOVE <= XMAX - SIZE)) {
            int moveA = MESH[((int) tetromino.a.getX() / SIZE) + 1][((int) tetromino.a.getY() / SIZE)];
            int moveB = MESH[((int) tetromino.b.getX() / SIZE) + 1][((int) tetromino.a.getY() / SIZE)];
            int moveC = MESH[((int) tetromino.c.getX() / SIZE) + 1][((int) tetromino.a.getY() / SIZE)];
            int moveD = MESH[((int) tetromino.d.getX() / SIZE) + 1][((int) tetromino.a.getY() / SIZE)];

            if ((moveA == 0) && (moveA == moveB) && (moveB == moveC) && (moveC == moveD)) {
                tetromino.a.setX(tetromino.a.getX() + MOVE);
                tetromino.b.setX(tetromino.b.getX() + MOVE);
                tetromino.c.setX(tetromino.c.getX() + MOVE);
                tetromino.d.setX(tetromino.d.getX() + MOVE);
            }
        }
    }

    // Moving the blocks left
    public static void MoveLeft(Tetromino tetromino) {
        // Checks to see if space tetromino is going to move to is outside of limit, if not then proceed
        if ((tetromino.a.getX() - MOVE >= 0) && (tetromino.b.getX() - MOVE >= 0) && (tetromino.c.getX() - MOVE >= 0) && (tetromino.d.getX() - MOVE >= 0)) {
            int moveA = MESH[((int) tetromino.a.getX() / SIZE) - 1][((int) tetromino.a.getY() / SIZE)];
            int moveB = MESH[((int) tetromino.b.getX() / SIZE) - 1][((int) tetromino.a.getY() / SIZE)];
            int moveC = MESH[((int) tetromino.c.getX() / SIZE) - 1][((int) tetromino.a.getY() / SIZE)];
            int moveD = MESH[((int) tetromino.d.getX() / SIZE) - 1][((int) tetromino.a.getY() / SIZE)];

            if ((moveA == 0) && (moveA == moveB) && (moveB == moveC) && (moveC == moveD)) {
                tetromino.a.setX(tetromino.a.getX() - MOVE);
                tetromino.b.setX(tetromino.b.getX() - MOVE);
                tetromino.c.setX(tetromino.c.getX() - MOVE);
                tetromino.d.setX(tetromino.d.getX() - MOVE);
            }
        }
    }

}
