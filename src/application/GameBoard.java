package application;

import java.util.ArrayList;
import java.util.List;

import static application.Common.HIGH;
import static application.Common.WIDE;

public class GameBoard {

    // User HashMap to store tetromino blocks parked on the game board
    // The location of the blocks were flattened to one dimension by using index = rowNum * WIDE + columnNum
    private List<TetroBox> parkedBlocks;

    public GameBoard() {
        parkedBlocks = new ArrayList<>();
    }

    public void addToBoard(Tetromino parked) {
        var blocks = parked.getBlocks();
        parkedBlocks.addAll(blocks);
    }

    public int[][] getGrid() {
        int[][] grid = new int[HIGH][WIDE];
        for (TetroBox block : parkedBlocks) {
            grid[block.getRowNum()][block.getColumnNum()] = 1;
        }
        return grid;
    }

    public List<TetroBox> getParkedBlocks() {
        return parkedBlocks;
    }

    public List<Integer> getIndexList() {
        List<Integer> indexList = new ArrayList<>();
        for (TetroBox block : parkedBlocks) {
            indexList.add(block.getIndex());
        }
        return indexList;
    }

    public TetroBox findBlockByIndex(int index) {
        for (TetroBox block : parkedBlocks) {
            if (block.getIndex() == index) return block;
        }
        return null;
    }

}


