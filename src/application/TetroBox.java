package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.Common.SIZE;
import static application.Common.WIDE;

/**
 * Class containing functions and variables to do with the squares making up the tetromino
 */
public class TetroBox {

    private int rowNum;
    private int columnNum;
    private int offsetRow;
    private int offsetColumn;
    private Color colorFill;
    private Rectangle uiBox;

    /**
     * Constructor for tetrobox
     */
    public TetroBox() {
        rowNum = 0;
        columnNum = 0;
        offsetRow = 0;
        offsetColumn = 0;
        colorFill = Color.LIGHTGRAY;
        uiBox = new Rectangle(SIZE - 1, SIZE - 1);
        uiBox.setFill(colorFill);
    }

    /**
     * Constructor for tetro box
     * @param rowNum The row the box is in
     * @param columnNum The column the box is in
     * @param offsetRow The number of rows the box is offset on grid
     * @param offsetColumn The number of column the box is offset on grid
     * @param colorFill Fills piece with the assigned color
     */
    public TetroBox(int rowNum, int columnNum, int offsetRow, int offsetColumn, Color colorFill) {
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
        this.colorFill = colorFill;
        uiBox = new Rectangle(SIZE - 1, SIZE - 1);
        uiBox.setFill(colorFill);
        setPosition(rowNum, columnNum);
    }

    /**
     * Updates the x and y position of the block
     */
    public void updateBlock() {
        uiBox.setX(columnNum * SIZE);
        uiBox.setY(rowNum * SIZE);
    }

    /**
     * Sets the position of block using the row and column
     * @param rowNum The row number to assign block to
     * @param columnNum The column number to assign block to
     */
    public void setPosition(int rowNum, int columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        updateBlock();
    }

    /**
     * Moves block down
     */
    public void move() {
        // Move down
        this.rowNum = rowNum + 1;
        updateBlock();
    }

    /**
     * Getter for the current index of the block
     * @return Returns current index of block
     */
    public int getIndex() {
        return rowNum * WIDE + columnNum;
    }

    /**
     * Getter for row number of block
     * @return
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * Getter for column number of block
     * @return
     */
    public int getColumnNum() {
        return columnNum;
    }

    /**
     * Getter for row offset
     * @return
     */
    public int getOffsetRow() {
        return offsetRow;
    }

    /**
     * Getter for column offset
     * @return
     */
    public int getOffsetColumn() {
        return offsetColumn;
    }

    /**
     * Getter for row offset
     * @param offsetRow  the number of rows the box is offset by
     */
    public void setOffsetRow(int offsetRow) {
        this.offsetRow = offsetRow;
    }

    /**
     * Getter for column offset
     * @param offsetColumn  the number of columns the box is offset by
     */
    public void setOffsetColumn(int offsetColumn) {
        this.offsetColumn = offsetColumn;
    }

    /**
     * Getter for the base row
     * @return Returns value of base row
     */
    public int getBaseRow(){
       return getRowNum() - getOffsetRow();
    }

    /**
     * Getter for base column
     * @return Returns value of base column
     */
    public int getBaseColumn(){
        return getColumnNum() - getOffsetColumn();
    }

    /**
     * Getter for ui box rectangle
     * @return Returns the ui box for rectangle
     */
    public Rectangle getUiBox() {
        return uiBox;
    }

    /**
     * Setter for row number
     * @param rowNum Returns row number of the block
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * Setter for column number
     * @param columnNum Returns column number of the block
     */
    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }
}
