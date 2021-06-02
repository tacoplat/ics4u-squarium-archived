package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Tetromino {
    // Initialize each block/square making up tetromino piece
    Rectangle[] blocks;

    Color color;
    // Name of tetromino piece based on their shape
    private String pieceName;
    public int orientation = 1;

    // Constructor
    public Tetromino(Rectangle[] blocks) {
        this.blocks = blocks;
    }

    public Tetromino(Rectangle[] blocks, String pieceName) {
        this.blocks = blocks;
        this.pieceName = pieceName;
    }

    public void setColor() {
        switch (pieceName) {
            case "j":
                color = color.SLATEGRAY;
                break;
            case "l":
                color = color.DARKGOLDENROD;
                break;
            case "o":
                color = color.INDIANRED;
                break;
            case "s":
                color = color.FORESTGREEN;
                break;
            case "t":
                color = color.DARKBLUE;
                break;
            case "z":
                color = color.HOTPINK;
                break;
        }

        // Set color for each block
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setFill(color);
        }

    }

    // Getter
    public String getPieceName() {
        return pieceName;
    }

    public void changeOrientation() {
        if (orientation != 4) {
            orientation++;
        } else {
            orientation = 1;
        }
    }



    // Set color of each rectangle making up tetromino
}
