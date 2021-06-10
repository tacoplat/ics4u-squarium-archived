package application;

import javafx.scene.paint.Color;
import java.util.*;

import static application.Common.HIGH;
import static application.Common.WIDE;

/**
 * Class for the different tetrominos (that are composed of tetro boxes)
 */
public class Tetromino {

    // Name of tetromino piece based on their shape
    private String pieceName;
    private Color colorFill;

    // The size of the matrix contains the blocks
    private int baseGridSize;

    // The blocks/Tiles making up tetromino piece
    private List<TetroBox> blocks;

    /**
     * Constructor for tetromino
     */
    public Tetromino() {
        this.pieceName = generateRandomPieceName();
        initialize();
        // Generate Blocks by default. Not centred for GameBoard
        GenerateBlocks(false);
    }

    /**
     * Constructor for tetromino
     * @param pieceName Name of the piece
     * @param forGameBoard True if the peice is supposed to be put on the game board, false if not
     */
    public Tetromino(String pieceName, boolean forGameBoard) {
        this.pieceName = pieceName;
        initialize();
        GenerateBlocks(forGameBoard);
    }

    /**
     * Default JavaFX initialize method
     */
    public void initialize() {
        baseGridSize = 3;
        blocks = new ArrayList<>();
        setColor();
    }

    /**
     * Method that generates a random piece name
     * @return Returns the piece name that's randomly generated
     */
    public static String generateRandomPieceName() {
        String pieceName;

        // Java util object to generate random int number
        Random random = new Random();

        // Generate random number between 0-100
        int randNum = random.nextInt(100);

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
     * Method to generate the blocks for the specific piece
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
                baseColumnNum = forGameBoard ? 5 : 2;
                break;
            case "l":
            case "i":
                baseColumnNum = forGameBoard ? 4 : 1;
                break;
        }

        GenerateBlocks(0, baseColumnNum);
    }

    /**
     * Method that changes the tetromino piece
     * @param pieceName The name of the tetromino piece
     */
    public void changeShape(String pieceName){
        this.pieceName = pieceName;
        setColor();
        TetroBox firstBlock = blocks.get(0);
        int baseRow = firstBlock.getBaseRow();
        int baseColumn = firstBlock.getBaseColumn();
        blocks = new ArrayList<>();
        GenerateBlocks(baseRow, baseColumn);
    }

    /**
     * Method to generate the blocks in the correct shape depending on the piece
     * @param baseRow The bottom row of blocks of the shape
     * @param baseColumn The last column of blocks of the shape
     */
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

    /**
     * Method that adds individual square blocks/tetrobox
     * @param baseRow The bottom row of blocks of the shape
     * @param baseColumn The last column of blocks of the shape
     * @param offsetRow The number of rows the box is offset on grid
     * @param offsetColumn The number of rows the box is offset on grid
     */
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

    /**
     * Getter for list of the indices of where the blocks will be moved to
     * @param direction The move direction of the block
     * @return Returns index list of index of location of where block will be after it is moved
     */
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
     * Checks to see if tetromino is able to be moved
     * @param direction The direction the tetromino is to be moved
     * @return 0:can't move; 1: free to move; 2: Game over: new generated block can't move down;
     */
    public int isMovable(List<Integer> parkedBlocksKeys, String direction) {
        if (isMovingAgainstEdge(direction)) return 0;

        List<Integer> nextKeys = getMovedIndexList(direction);

        for (Integer parkedKey : parkedBlocksKeys) {
            if (nextKeys.contains(parkedKey)) {
                if(direction == "left" || direction == "right") {
                    return 0;
                } else {
                    if(parkedKey <24){
                        return 2;
                    }else{
                        return 0;
                    }

                }
            }
        }

        return 1;
    }

    /**
     * Checks to see if tetromino is against the edge
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
     * Method to move tetromino
     * @param direction The direction tetromino moves
     * @return 0:can't move; 1: Moved successfully; 2: Game over: new generated block can't move down;
     */
    public int move(List<Integer> parkedBlocksKeys, String direction) {
        int movable = isMovable(parkedBlocksKeys, direction);
        if (movable == 1) {
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


    /**
     * Getter for piece name of tetromino
     * @return Returns piece name
     */
    public String getPieceName() {
        return pieceName;
    }

    /**
     * Getter for the color that the piece will be
     * @return Returns the color shape will be filled
     */
    public Color getColorFill() {
        return colorFill;
    }

    /**
     * Getter for blocks
     *
     * @return List of TetroBox
     */
    public List<TetroBox> getBlocks() {
        return blocks;
    }

    /**
     * Getter for base grid of the peice
     * @return Returns the base grid of the piece
     */
    private int[][] getBaseGrid() {
        int[][] baseGrid = new int[baseGridSize][baseGridSize];
        for (TetroBox block : blocks) {
            int row = block.getOffsetRow();
            int column = block.getOffsetColumn();
            // if shape is t or i, move baseRowNum one row up
            if(pieceName == "t" || pieceName == "i" ){
                row += 1;
            }
            baseGrid[row][column] = block.getIndex();
        }
        return baseGrid;
    }

    /**
     * Method to rotate tetromino
     * @param parkedBlocksKeys  key for the parked blocks
     * @param clockwise Turning direction of block
     * @return True if able to rotate, false if not able to rotate
     */
    public boolean rotate(List<Integer> parkedBlocksKeys, boolean clockwise) {
        TetroBox firstBlock = blocks.get(0);
        int baseRowNum = firstBlock.getBaseRow();
        int baseColumnNum = firstBlock.getBaseColumn();
        // if shape is t or i, move baseRowNum one row up
        if(pieceName == "t" || pieceName == "i" ){
            baseRowNum -= 1;
        }
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
                    // if shape is t or i, reset offset
                    if(pieceName == "t" || pieceName == "i" ){
                        block.setOffsetRow(i-1);
                    }else {
                        block.setOffsetRow(i);
                    }
                    block.setOffsetColumn(j);
                    block.setPosition(baseRowNum + i, baseColumnNum + j);
                }
            }
        }
        return true;
    }

    /**
     * Method to find a block by index
     * @param index The index number of the block
     * @return Returns null if block not found, returns the block if index is found
     */
    private TetroBox findBlockByIndex(int index) {
        for (TetroBox block : blocks) {
            if (block.getIndex() == index) return block;
        }
        return null;
    }

    /**
     * Getter for base row number of piece
     * @return Returns the base row number
     */
    public int getBaseRowNumber(){
        if(blocks.size()>0) return blocks.get(0).getBaseRow();

        return 0;
    }

    /**
     * Getter for base column number of piece
     * @return Returns the base column number
     */
    public int getBaseColumnNumber(){
        if(blocks.size()>0) return blocks.get(0).getBaseColumn();

        return 0;
    }

    /**
     * Getter for index list of blocks
     * @return Returns the index list
     */
    public List<Integer> getIndexList() {
        List<Integer> indexList = new ArrayList<>();
        for (TetroBox block : blocks) {
            indexList.add(block.getIndex());
        }
        return indexList;
    }
}
