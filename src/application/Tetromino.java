package application;

import javafx.scene.paint.Color;

import java.sql.SQLOutput;
import java.util.*;

import static application.Common.HIGH;
import static application.Common.WIDE;

public class Tetromino {

    // Name of tetromino piece based on their shape
    private String pieceName;
    private Color colorFill;

    // The size of the matrix contains the blocks
    private int baseGridSize;

    // The blocks/Tiles making up tetromino piece
    private List<TetroBox> blocks;

    public Tetromino() {
        this.pieceName = generateRandomPieceName();
        initialize();
        // Generate Blocks by default. Not centred for GameBoard
        GenerateBlocks(false);
    }

    public Tetromino(String pieceName, boolean forGameBoard) {
        this.pieceName = pieceName;
        initialize();
        GenerateBlocks(forGameBoard);
    }

    public void initialize() {
        baseGridSize = 3;
        blocks = new ArrayList<>();
        setColor();
    }

    private String generateRandomPieceName() {
        String pieceName;
        // Pick random color between 0-100
        int randNum = (int) (Math.random() * 100);

        if (randNum < 15) {
            pieceName = "j";
        } else if (randNum < 30) {
            pieceName = "l";
        } else if (randNum < 45) {
            pieceName = "o";
        } else if (randNum < 60) {
            pieceName = "s";
        } else if (randNum < 75) {
            pieceName = "t";
        } else if (randNum < 90) {
            pieceName = "z";
        } else {
            pieceName = "i";
        }

        return pieceName;
    }

    /**
     * @param forGameBoard true: For GameBoard; false: For Display on the RightPane
     */
    private void GenerateBlocks(boolean forGameBoard) {
        int baseColumnNum = 0;
        switch (pieceName) {
            case "o":
            case "j":
            case "s":
            case "t":
            case "z":
                baseColumnNum = forGameBoard ? 5 : 0;
                break;
            case "l":
            case "i":
                baseColumnNum = forGameBoard ? 4 : 0;
                break;
        }

        GenerateBlocks(0, baseColumnNum);
    }

    private void GenerateBlocks(int baseRow, int baseColumn) {
        switch (pieceName) {
            case "o":
                // X X
                // X X
                baseGridSize = 2;
                addTetroBox(baseRow, baseColumn,0, 0);
                addTetroBox(baseRow, baseColumn,0, 1);
                addTetroBox(baseRow, baseColumn,1, 0);
                addTetroBox(baseRow, baseColumn,1, 1);
                break;
            case "j":
                // X
                // X X X
                addTetroBox(baseRow, baseColumn, 0, 0);
                addTetroBox(baseRow, baseColumn,1, 0);
                addTetroBox(baseRow, baseColumn,1, 1);
                addTetroBox(baseRow, baseColumn,1, 2);
                break;
            case "l":
                //     X
                // X X X
                addTetroBox(baseRow, baseColumn,0, 2);
                addTetroBox(baseRow, baseColumn,1, 0);
                addTetroBox(baseRow, baseColumn,1, 1);
                addTetroBox(baseRow, baseColumn,1, 2);
                break;
            case "s":
                //   X X
                // X X
                addTetroBox(baseRow, baseColumn,0, 1);
                addTetroBox(baseRow, baseColumn,0, 2);
                addTetroBox(baseRow, baseColumn,1, 0);
                addTetroBox(baseRow, baseColumn,1, 1);
                break;
            case "t":
                // X X X
                //   X
                addTetroBox(baseRow, baseColumn,0, 0);
                addTetroBox(baseRow, baseColumn,0, 1);
                addTetroBox(baseRow, baseColumn,0, 2);
                addTetroBox(baseRow, baseColumn,1, 1);
                break;
            case "z":
                // X X
                //   X X
                addTetroBox(baseRow, baseColumn,0, 0);
                addTetroBox(baseRow, baseColumn,0, 1);
                addTetroBox(baseRow, baseColumn,1, 1);
                addTetroBox(baseRow, baseColumn,1, 2);
                break;
            case "i":
                // X X X X
                baseGridSize = 4;
                    addTetroBox(baseRow, baseColumn,0, 0);
                    addTetroBox(baseRow, baseColumn,0, 1);
                    addTetroBox(baseRow, baseColumn,0, 2);
                    addTetroBox(baseRow, baseColumn,0, 3);
                break;
        }
    }

    private void addTetroBox(int baseRow, int baseColumn, int offsetRow, int offsetColumn) {
        int row = baseRow + offsetRow;
        int col = baseColumn + offsetColumn;
        blocks.add(new TetroBox(row, col, offsetRow, offsetColumn, colorFill));
    }

    /**
     * Set color for fill the rectangle making up tetromino
     */
    private void setColor() {
        switch (pieceName) {
            case "j":
                colorFill = Color.BLUE;
                break;
            case "l":
                colorFill = Color.ORANGE;
                break;
            case "o":
                colorFill = Color.YELLOW;
                break;
            case "s":
                colorFill = Color.GREEN;
                break;
            case "t":
                colorFill = Color.PURPLE;
                break;
            case "z":
                colorFill = Color.RED;
                break;
            case "i":
                colorFill = Color.CYAN;
                break;
        }
    }

    private List<Integer> getMovedIndexList(String direction) {
        List<Integer> indexList = new ArrayList<>();
        for (TetroBox block : blocks) {
            switch (direction) {
                case "right":
                    indexList.add(block.getIndex() + 1);
                    break;
                case "left":
                    indexList.add(block.getIndex() - 1);
                    break;
                default:
                    // Move Down
                    indexList.add(block.getIndex() + 12);
            }
        }
        return indexList;
    }

    /**
     * @param direction
     * @return true: free to move; false: blocked;
     */
    public boolean isMovable(List<Integer> parkedBlocksKeys, String direction) {
        if (isMovingAgainstEdge(direction)) return false;

        List<Integer> nextKeys = getMovedIndexList(direction);

        for (Integer parkedKey : parkedBlocksKeys) {
            if (nextKeys.contains(parkedKey)) return false;
        }

        return true;
    }

    /**
     * @param direction
     * @return false: free to move; true: Touched the edge;
     */
    public boolean isMovingAgainstEdge(String direction) {
        for (TetroBox tetroBox : blocks) {
            switch (direction) {
                case "right":
                    if (tetroBox.getColumnNum() == WIDE - 1) return true;
                    break;
                case "left":
                    if (tetroBox.getColumnNum() == 0) return true;
                    break;
                default:
                    if (tetroBox.getRowNum() == HIGH - 1) return true;
            }
        }

        return false;
    }

    /**
     * @param direction
     * @return 1: Moved successfully; 2: Touched the right edge; 3: Touched the left edge; 4: Touched the bottom;
     */
    public boolean move(List<Integer> parkedBlocksKeys, String direction) {
        boolean movable = isMovable(parkedBlocksKeys, direction);
        if (movable) {
            for (TetroBox block : blocks) {
                switch (direction) {
                    case "right":
                        block.setColumnNum(block.getColumnNum() + 1);
                        break;
                    case "left":
                        block.setColumnNum(block.getColumnNum() - 1);
                        break;
                    default:
                        // Move down
                        block.setRowNum(block.getRowNum() + 1);
                }
                block.updateBlock();
            }
        }
        return movable;
    }


    // Getter
    public String getPieceName() {
        return pieceName;
    }

    /**
     * @return
     */
    public Color getColorFill() {
        return colorFill;
    }

    /**
     * Getter
     *
     * @return List of TetroBox
     */
    public List<TetroBox> getBlocks() {
        return blocks;
    }

    private int[][] getBaseGrid() {
        int[][] baseGrid = new int[baseGridSize][baseGridSize];
        for (TetroBox block : blocks) {
            int row = block.getOffsetRow();
            int column = block.getOffsetColumn();
            baseGrid[row][column] = block.getIndex();
        }
        return baseGrid;
    }

    public boolean rotate(List<Integer> parkedBlocksKeys, boolean clockwise) {
        TetroBox firstBlock = blocks.get(0);
        int baseRowNum = firstBlock.getBaseRow();
        int baseColumnNum = firstBlock.getBaseColumn();
        if (baseRowNum < 0) {
            // Can't rotate if the shape i is at the top row.
            return false;
        }
        if (baseRowNum + baseGridSize >= HIGH) {
            // Can't rotate if the base grid cross the bottom edge.
            return false;
        }
        if (baseColumnNum + baseGridSize >= WIDE) {
            // Can't rotate if the base grid cross the right edge.
            return false;
        }
        if (baseColumnNum < 0) {
            // Can't rotate if the base grid cross the left edge.
            return false;
        }

        int[][] baseGrid = getBaseGrid();

        // rotate the baseGrid
        Common.rotate(baseGrid, clockwise);

        // Check if the new location conflict with the parked blocks
        for (int i = 0; i < baseGridSize; i++) {
            for (int j = 0; j < baseGridSize; j++) {
                if (baseGrid[i][j] > 0) {
                    if(parkedBlocksKeys.contains(Common.calculateIndex(baseRowNum + i, baseColumnNum + j))){
                        return false;
                    }
                }
            }
        }

        TetroBox block;
        // Move the blocks to the rotated location
        for (int i = 0; i < baseGridSize; i++) {
            for (int j = 0; j < baseGridSize; j++) {
                if (baseGrid[i][j] > 0) {
                    block = findBlockByIndex(baseGrid[i][j]);
                    block.setOffsetRow(i);
                    block.setOffsetColumn(j);
                    block.setPosition(baseRowNum + i, baseColumnNum + j);
                }
            }
        }
        return true;
    }

    private TetroBox findBlockByIndex(int index) {
        for (TetroBox block : blocks) {
            if (block.getIndex() == index) return block;
        }
        return null;
    }


    public List<Integer> getIndexList() {
        List<Integer> indexList = new ArrayList<>();
        for (TetroBox block : blocks) {
            indexList.add(block.getIndex());
        }
        return indexList;
    }
}
