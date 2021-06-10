package application;

/**
 * Class containing static variables and general functions.
 */
public class Common {

    // Size of square box that tetromino is composed of
    public static final int SIZE = 25;

    // Game Board Grid total columns
    public static final int WIDE = 12;

    // Game Board Grid total rows
    public static final int HIGH = 26;

    // Max length size of game screen
    public static final int XMAX = SIZE * WIDE;

    // Max  witdh size of game screen
    public static final int YMAX = SIZE * HIGH;

    /**
     * Calculates index of each block in the game board
     * @param rowNum The row number
     * @param columnNum The column number
     * @return The calculated index number for the block
     */
    public static int calculateIndex(int rowNum, int columnNum) {
        return rowNum * WIDE + columnNum;
    }

    /**
     * Rotates the tetromino
     * @param matrix 2D array for rotation
     * @param clockwise If true, rotates clockwise. If false, rotates counter-clockwise.
     */
    public static void rotate(int[][] matrix, boolean clockwise) {
        if (clockwise) {
            transpose(matrix);
            reverseRows(matrix);
        } else {
            transpose(matrix);
            reverseColumns(matrix);
        }
    }

    /**
     * Method to transpose the matrix
     * @param matrix 2D array that will be transformed
     */
    public static void transpose(int[][] matrix) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                int tmp = matrix[j][i];
                matrix[j][i] = matrix[i][j];
                matrix[i][j] = tmp;
            }
        }
    }

    /**
     * Method to reverse the elements in each column
     * @param matrix 2D array that will be transformed
     */
    public static void reverseColumns(int[][] matrix) {
        int n = matrix.length;
        int temp;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n / 2; i++) {
                temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][j];
                matrix[n - 1 - i][j] = temp;
            }
        }
    }

    /**
     * Method to reverse the elements in each row
     * @param matrix 2D array that will be transformed
     */
    public static void reverseRows(int[][] matrix) {
        int n = matrix.length;
        int tmp;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n / 2; j++) {
                tmp = matrix[i][j];
                matrix[i][j] = matrix[i][n - j - 1];
                matrix[i][n - j - 1] = tmp;
            }
        }
    }
}
