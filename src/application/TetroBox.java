package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import static application.Common.SIZE;
import static application.Common.WIDE;

public class TetroBox {

    private int rowNum;
    private int columnNum;
    private int offsetRow;
    private int offsetColumn;
    private Color colorFill;
    private Rectangle uiBox;

    public TetroBox() {
        rowNum = 0;
        columnNum = 0;
        offsetRow = 0;
        offsetColumn = 0;
        colorFill = Color.LIGHTGRAY;
        uiBox = new Rectangle(SIZE - 1, SIZE - 1);
        uiBox.setFill(colorFill);
    }

    public TetroBox(int rowNum, int columnNum, int offsetRow, int offsetColumn, Color colorFill) {
        this.offsetRow = offsetRow;
        this.offsetColumn = offsetColumn;
        this.colorFill = colorFill;
        uiBox = new Rectangle(SIZE - 1, SIZE - 1);
        uiBox.setFill(colorFill);
        setPosition(rowNum, columnNum);
    }

    public void updateBlock() {
        uiBox.setX(columnNum * SIZE);
        uiBox.setY(rowNum * SIZE);
    }

    public void setPosition(int rowNum, int columnNum) {
        this.rowNum = rowNum;
        this.columnNum = columnNum;
        updateBlock();
    }

    public void move() {
        // Move down
        this.rowNum = rowNum + 1;
        updateBlock();
    }

    public int getIndex() {
        return rowNum * WIDE + columnNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public int getColumnNum() {
        return columnNum;
    }

    public int getOffsetRow() {
        return offsetRow;
    }

    public int getOffsetColumn() {
        return offsetColumn;
    }

    public void setOffsetRow(int offsetRow) {
        this.offsetRow = offsetRow;
    }

    public void setOffsetColumn(int offsetColumn) {
        this.offsetColumn = offsetColumn;
    }

    public int getBaseRow(){
       return getRowNum() - getOffsetRow();
    }

    public int getBaseColumn(){
        return getColumnNum() - getOffsetColumn();
    }

    public Rectangle getUiBox() {
        return uiBox;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }
}
