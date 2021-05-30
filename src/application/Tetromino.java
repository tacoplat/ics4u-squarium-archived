package application;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Tetromino {
    // Initialize each block/square making up tetromino piece
    Rectangle a;
    Rectangle b;
    Rectangle c;
    Rectangle d;
    Color color;
    // Name of tetromino piece based on their shape
    private String pieceName;
    public int orientation = 1;

    // Constructor
    public Tetromino(Rectangle a, Rectangle b, Rectangle c, Rectangle d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public Tetromino(Rectangle a, Rectangle b, Rectangle c, Rectangle d, String pieceName) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
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

        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
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
