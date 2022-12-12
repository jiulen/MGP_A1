package com.sdm.mgp2022;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class BoardManager {

    enum boardStates
    {
        READY,
        SELECT,
        DROP,
        SWAP,
        CLEARING,
        GENERATE,
        GRAVITATE,
        CHECKSEQ,
        LOSE
    }
    boardStates boardState;

    private int numRows = 13;
    private int numCols = 6;
    private int startGarbage = 4;
    private static final List<TileEntity.TILE_TYPES> TILES = Collections.unmodifiableList(Arrays.asList(TileEntity.TILE_TYPES.values()));
    private static final int tilesSize = TILES.size();
    private static final Random RANDOM = new Random();

    float yoffset = 0.f;

    boolean aButtonDown = false;
    boolean bButtonDown = false;
    int playercol = 2;
    boolean lose = false;
    boolean isPressed = false;
    Vector<TileSequence> matchingSequences = new Vector<TileSequence>();

    public TileEntity [][] grid = new TileEntity[numRows][numCols];

    public TileEntity selectedTile = null;

    void setButtonDownA(boolean state)
    {
        aButtonDown = state;
    }
    void setButtonDownB(boolean state)
    {
        bButtonDown = state;
    }
    void setPlayerCol(int col)
    {
        playercol = col;
    }

    public static TileEntity.TILE_TYPES randomTile()
    {
        return TILES.get(RANDOM.nextInt(tilesSize));
        //return TileEntity.TILE_TYPES.TILE_EMPTY;
    }

    public void fillBoard(int width)
    {
        boolean boardReady = false;

        // Fill board with random tiles.
        for(int i = 0; i < startGarbage; ++i) {
            for(int j = 0; j < numCols; ++j) {
                grid[i][j] = TileEntity.Create(randomTile(), width, width * (j + 0.5f), width * (i + 0.5f) - width);
            }
        }

        // While there was any match in the board, fix the causing tile with a new random tile and check again
        while(!boardReady) {
            boardReady = true;
            // Note that we must NOT iterate until m_numRows-2, as this will cause us to miss tiles
            // the isBeginningOfSequence method checks if the row is in boundary.
            for(int i = 0; i < startGarbage; ++i) {
                for(int j = 0; j < numCols; ++j) {
                    if(isBeginningOfSequence(i, j) || isEndOfSequence(i, j)) {
                        boardReady = false;
                        grid[i][j].SetIsDone(true);
                        grid[i][j] = TileEntity.Create(randomTile(), width,width * (j + 0.5f), width * (i + 0.5f) - width);
                    }
                }
            }
        }
        boardState = boardStates.READY;
    }

    void updateBoard(int level, int width, float dt)
    {
        if(!lose)
        {
            switch (boardState)
            {
                case READY: {
                    moveTilesDownGrid(level, width, dt);

                    if(aButtonDown)
                    {
                        if (selectedTile != null) {
                            boardState = boardStates.DROP;
                        }
                        else
                        {
                            boardState = boardStates.SELECT;
                        }
                        aButtonDown = false;
                    }
                    break;
                }
                case SELECT: {
                    if(!selectTile(playercol))
                    {
                        System.out.println("nothing to select");
                    }
                    boardState = boardStates.READY;
                    break;
                }
                case DROP:{
                    if(!dropTile(playercol))
                    {
                        System.out.println("Invalid drop location");
                    }
                    boardState = boardStates.READY;
                    break;
                }
                // if no sequences change board to ready
                case CHECKSEQ: {
                    if (!markAllSequencesOnBoard())
                        boardState = boardStates.READY;
                    else
                        boardState = boardStates.GRAVITATE;
                    break;
                }
                case GENERATE: {
                    dropNewTilesRow(width);
                    boardState = boardStates.READY;
                    break;
                }
                case GRAVITATE: {
                    gravitateBoardStep();
                    break;
                }
                case LOSE: {
                    System.out.println("LOSE");
                    lose = true;
                    break;
                }
            }
        }
    }

    void moveTilesDownGrid(int level,int width,float dt)
    {
        for (int i = 10; i >= 0 ; --i)
        {
            for (int j = 0; j < numCols; ++j)
            {
                // if tile reaches row 11 game ends
                if (grid[11][j] == null)
                {
                    // check if tiles exist on grid and that they are not being cleared to move them down the screen
                    if (grid[i][j] != null)
                    {
                        float prevpos = grid[i][j].GetPosY();
                        float newpos = prevpos + (level * 50 * dt);
                        grid[i][j].SetPosY(newpos);
                        yoffset = width - (newpos % width);
                        if((grid[i][j].GetPosY() + grid[i][j].GetWidth() * 0.5) / grid[i][j].GetWidth() > i)
                        {
                            grid[i + 1][j] = grid[i][j];
                            grid[i + 1][j].SetPosY(width * (i - 0.5f));
                            grid[i][j] = null;
                            boardState = boardStates.GENERATE;
                        }
                    }
//                    if(hasSequencesProximity(i, j))
//                    {
//                        markAllSequencesOnBoard(width);
//                        gravitateBoardStep();
//                        System.out.println("aaaaaaaaaaaaOSE");
//                    }
                }
                else
                {
                    boardState = boardStates.LOSE;
                }
            }
        }
    }

    public final boolean selectTile(int col)
    {
        for(int i = 10; i >= 0; --i)
        {
            if(grid[i][col] != null)
            {
                selectedTile = grid[i][col];
                grid[12][col] = selectedTile;
                grid[12][col].SetPosY(grid[12][col].GetWidth() * (10.5f));
                grid[12][col].SetPosX(grid[i][col].GetWidth() * (col + 0.5f));
                grid[i][col] = null;
                return true;
            }
        }
        return false;
    }
    public final boolean dropTile(int col)
    {
        for (int i = 10; i >= 1 ; --i)
        {
            if(grid[i][col] != null)
            {
                grid[i+1][col] = selectedTile;
                grid[i+1][col].SetPosY((grid[i][col].GetPosY() + selectedTile.GetWidth()));
                selectedTile = null;
                for (int j = 0; j < numCols; j++)
                    grid[12][j] = null;
                return true;
            }
        }
        return false;
    }
    public final boolean hasSequencesProximity(int row, int col) {
        // Check if one of the tiles to the left/above the current tile is a beginning of a sequence (and perhaps involving
        // the current tile)
        for(int i = Math.max(row-1, 0); i < Math.min(row+2, numRows); ++i) {
            for (int j = Math.max(col-1, 0); j < Math.min(col+2, numCols); ++j) {
                if (isBeginningOfSequence(i, j) || isEndOfSequence(i, j)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean gravitateBoardStep() {
        boolean found = false;
        for(int j = 0; j < numCols; ++j) {
            /// Gravitate up until the first row.
            for(int i = numRows - 1; i >0 ; --i) {
                if(grid[i][j] == null) {
                    // Found empty tile - move all tiles above it back.
                    for(int k = i; k > 0; --k) {
                        if(grid[k][j] != null) {
                            // Moved back a non-empty tile - board no gravity
                            float prevpos = grid[k][j].GetPosY();
                            grid[k-1][j] = grid[k][j];
                            grid[k][j] = null;
                            grid[k-1][j].SetPosY(prevpos - grid[k-1][j].GetWidth());

                            found = true;
                        }

                    }
                    break;
                }
            }
        }
        return found;
    }

    boolean dropNewTilesRow(int width) {
        boolean dropped = false;
        for(int j = 0; j < numCols; ++j) {
            if(grid[0][j] == null && grid[1][j] == null) {
                dropped = true;

                do {
                    if (grid[0][j] != null)
                        grid[0][j].SetIsDone(true);
                    grid[0][j] = TileEntity.Create(randomTile(), width,width * (j + 0.5f), width * (0.5f) - width);
                } while (isBeginningOfSequenceForZero(j) || isEndOfSequenceForZero(j));
            }
        }
        return dropped;
    }

    // swap on 1 column
    boolean swapTiles(int row, int col) {

        if(grid[row][col] == null || grid[row - 1][col] == null ||
                grid[row][col] == grid[row - 1][col]) {
            return false; // if either tile is empty or they have same color - no need to swap
        }
        TileEntity tmp = grid[row-1][col];
        grid[row - 1][col] = grid[row][col];
        grid[row][col] = tmp;
        return true;
    }

    boolean markAllSequencesOnBoard() {
        if (!findAllSequences()) {
            return false;
        }

        for ( int i = 0; i < matchingSequences.size(); i++)
        {
            markSequenceOnBoard(matchingSequences.get(i));
        }
        return true;
    }


    boolean findAllSequences() {
        matchingSequences.clear();

        // Find all the horizontal matches and create the matching sequence objects.
        int curMatchLen = 1;

        // Look for horizontal matches - check each tile with the one after it.
        for (int i = 0; i < numRows; ++i) {
            curMatchLen = 1;
            for (int j = 0; j < numCols - 1; ++j) {
                if (grid[i][j] == null) {
                    curMatchLen = 1;
                    continue;
                }

                // Found a sequence of same tiles in a row of length 2 at least.
                if (grid[i][j] == grid[i][j + 1]) {
                    curMatchLen++;
                }
                else {
                    // match sequence broken - check if previous sequence was more than 3
                    if (curMatchLen >= 3) {
                        matchingSequences.add(
                                new TileSequence(grid[i][j].tileType,
                                        TileSequence.Orientation.HORIZONTAL,
                                        i,
                                        j - (curMatchLen - 1), // Start column position for the sequence
                                        curMatchLen));
                    }
                    curMatchLen = 1;
                }
            }

            // Found a match up until the last item in current row.
            if (curMatchLen >= 3) {
                matchingSequences.add(
                        new TileSequence(grid[i][numCols - 1].tileType,
                                TileSequence.Orientation.HORIZONTAL,
                                i,
                                numCols - curMatchLen, // Start column position for the sequence
                                curMatchLen));
            }
        }

        // Look for vertical matches -  check each tile with the one after it.
        curMatchLen = 1;
        for (int j = 0; j < numCols; ++j) {
            curMatchLen = 1;
            for (int i = 0; i < numRows - 1; ++i) {
                if (grid[i][j] == null) {
                    curMatchLen = 1;
                    continue;
                }

                // Found a sequence of same tiles in a row of length 2 at least.
                if (grid[i][j] == grid[i + 1][j]) {
                    curMatchLen++;
                }
                else {
                    // match sequence broken - check if previous sequence was more than 3
                    if (curMatchLen >= 3) {
                        matchingSequences.add(
                                new TileSequence(grid[i][j].tileType,
                                        TileSequence.Orientation.VERTICAL,
                                        i - (curMatchLen - 1), // Start row position for the sequence
                                        j,
                                        curMatchLen));
                    }
                    curMatchLen = 1;
                }
            }

            // Found a match up until the last item in current row.
            if (curMatchLen >= 3) {
                matchingSequences.add(
                        new TileSequence(grid[numRows - 1][j].tileType,
                                TileSequence.Orientation.VERTICAL,
                                numRows - curMatchLen, // Start row position for the sequence
                                j,
                                curMatchLen));
            }
        }

        return !matchingSequences.isEmpty();
    }

    void markSequenceOnBoard(TileSequence sequence) {
        if (sequence.getOrientation() == TileSequence.Orientation.HORIZONTAL) {
            for (int j = sequence.getStartCol();
                 j < sequence.getStartCol() + sequence.getSize();
                 ++j)
            {
                grid[sequence.getStartRow()][j].isAttack = true;
            }
        }
        else if (sequence.getOrientation() == TileSequence.Orientation.VERTICAL) {
            for (int i = sequence.getStartRow();
                 i < sequence.getStartRow() + sequence.getSize();
                 ++i)
            {
                grid[i][sequence.getStartCol()].isAttack = true;
            }
        }
    }

    // change to 4
    boolean isBeginningOfSequence(int i, int j) {
        if (j >= 0 && j < numCols-3 && grid[i][j] != null && grid[i][j+1] != null && grid[i][j+2] != null && grid[i][j+3] != null)
        {
            if (grid[i][j].tileType == grid[i][j+1].tileType && grid[i][j].tileType == grid[i][j+2].tileType && grid[i][j].tileType == grid[i][j+3].tileType)
                return true; //continue if false
        }

        if (i >= 0 && i < numRows-3 && grid[i][j] != null && grid[i+1][j] != null)
        {
            if (grid[i][j].tileType == grid[i+1][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }


    boolean isEndOfSequence(int i,int j) {
        if (j < numCols && j>=3 && grid[i][j] != null && grid[i][j-1] != null && grid[i][j-2] != null && grid[i][j-3] != null)
        {
            if (grid[i][j-1].tileType == grid[i][j].tileType && grid[i][j-2].tileType == grid[i][j].tileType && grid[i][j-3].tileType == grid[i][j].tileType)
                return true; //continue if false
        }

        if (i < numRows-2 && i>=3 && grid[i][j] != null && grid[i-1][j] != null)
        {
            if (grid[i-1][j].tileType == grid[i][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }

    boolean isBeginningOfSequenceForZero(int j) //row always 0 for this (only use for spawning at row=0)
    {
        if (j >= 0 && j < numCols-3 && grid[0][j] != null && grid[0][j+1] != null && grid[0][j+2] != null && grid[0][j+3] != null)
        {
            if (grid[0][j].tileType == grid[0][j+1].tileType && grid[0][j].tileType == grid[0][j+2].tileType && grid[0][j].tileType == grid[0][j+3].tileType)
                return true; //continue if false
        }

        if (grid[0][j] != null && grid[2][j] != null)
        {
            if (grid[0][j].tileType == grid[2][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }

    boolean isEndOfSequenceForZero(int j) {
        if (j < numCols && j>=3 && grid[0][j] != null && grid[0][j-1] != null && grid[0][j-2] != null && grid[0][j-3] != null)
        {
            if (grid[0][j-1].tileType == grid[0][j].tileType && grid[0][j-2].tileType == grid[0][j].tileType && grid[0][j-3].tileType == grid[0][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }
}
