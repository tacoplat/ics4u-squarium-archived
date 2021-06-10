package application;

import java.util.ArrayList;
import java.util.List;

import static application.Common.HIGH;
import static application.Common.WIDE;

/**
 * Class that contains the parked blocks
 */
public class GameBoard {

    // User HashMap to store tetromino blocks parked on the game board
    // The location of the blocks were flattened to one dimension by using index = rowNum * WIDE + columnNum
    private List<TetroBox> parkedBlocks;

    /**
     * Constructor for game board
     */
    public GameBoard() {
        parkedBlocks = new ArrayList<>();
    }

    /**
     * Method to add parked tetromino blocks to the game board
     * @param parked The parked tetromino
     */
    public void addToBoard(Tetromino parked) {
        var blocks = parked.getBlocks();
        parkedBlocks.addAll(blocks);
    }

    /**
     * Method to get the game board grid
     * @return Returns grid with the parked blocks
     */
    public int[][] getGrid() {
        int[][] grid = new int[HIGH][WIDE];
        for (TetroBox block : parkedBlocks) {
            grid[block.getRowNum()][block.getColumnNum()] = 1;
        }
        return grid;
    }

    /**
     * Getter for parked blocks in the grid
     * @return Returns the parked blocks
     */
    public List<TetroBox> getParkedBlocks() {
        return parkedBlocks;
    }

    /**
     * Getter for index list of parked blocks
     * @return Returns a list of indices for parked blocks
     */
    public List<Integer> getIndexList() {
        List<Integer> indexList = new ArrayList<>();
        for (TetroBox block : parkedBlocks) {
            indexList.add(block.getIndex());
        }
        return indexList;
    }

    /**
     * Method to find the tetromino block by using index number
     * @param index The index number
     * @return Returns tetro box if found, returns null if nothing is found
     */
    public TetroBox findBlockByIndex(int index) {
        for (TetroBox block : parkedBlocks) {
            if (block.getIndex() == index) return block;
        }
        return null;
    }

}


