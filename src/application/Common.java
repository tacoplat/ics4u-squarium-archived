package application;

import java.util.List;

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

    public static int calculateIndex(int rowNum, int columnNum) {
        return rowNum * WIDE + columnNum;
    }

    public static void rotate(int[][] matrix, boolean clockwise) {
        if (clockwise) {
            transpose(matrix);
            reverseRows(matrix);
        } else {
            transpose(matrix);
            reverseColumns(matrix);
        }
    }

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

    public static void reverseColumns(int[][] matrix) {
        int n = matrix.length;
        int tmp;
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < n / 2; i++) {
                int temp = matrix[i][j];
                matrix[i][j] = matrix[n - 1 - i][j];
                matrix[n - 1 - i][j] = temp;
            }
        }
    }

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
