package com.sdm.mgp2022;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class BoardManager {

    // Done by jonathan
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
        LOSE,
        WIN
    }
    boardStates boardState;

    private int numRows = 13;
    private int numCols = 6;
    private int startGarbage = 4;
    private static final List<TileEntity.TILE_TYPES> TILES = Collections.unmodifiableList(Arrays.asList(TileEntity.TILE_TYPES.values()));
    private static final int tilesSize = TILES.size();
    private static final Random RANDOM = new Random();

    int enemyHealth = 25;
    int playercol = 2;
    float yoffset = 0.f;
    boolean aButtonDown = false;
    boolean bButtonDown = false;
    boolean lose = false;
    boolean win = false;

    public Vector<TileSequence> matchingSequences = new Vector<TileSequence>();
    public TileEntity [][] grid = new TileEntity[numRows][numCols];
    public TileEntity selectedTile = null;

    private final float clearDelayTotal = 0.5f;
    private final float clearAnimStart = 0.2f;
    private float clearTime = 0;
    private boolean clearAnimStarted = false;

    public int clearedTilesNum = 0;
    public boolean attackSent = true;

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
    void setEnemyHealth(int health)
    {
        enemyHealth = health;
    }

    // done by jonathan
    public static TileEntity.TILE_TYPES randomTile()
    {
        return TILES.get(RANDOM.nextInt(tilesSize));
    }

    // done by jonathan
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

    // by jonathan
    void updateBoard(int level, int width, float dt)
    {
        if(enemyHealth <= 0)
        {
            boardState = boardStates.WIN;
        }
        if(!lose && !win)
        {
            switch (boardState)
            {
                case READY: {
                    moveTilesDownGrid(level, width, dt);
                    //Check if row 1 all empty
                    boolean emptyLastRow = true;
                    for (int j = 0; j < numCols; ++j) {
                        if (grid[1][j] != null)
                            emptyLastRow = false;
                    }
                    if (emptyLastRow)
                        boardState = boardStates.GENERATE;

                    if (aButtonDown)
                    {
                        if (selectedTile != null)
                            boardState = boardStates.DROP;

                        else
                            boardState = boardStates.SELECT;
                        aButtonDown = false;
                    }
                    if (bButtonDown)
                    {
                        boardState = boardStates.SWAP;
                        bButtonDown = false;
                    }
                    break;
                }
                case SELECT: {
                    if(!selectTile(playercol))
                        System.out.println("nothing to select");
                    boardState = boardStates.READY;
                    break;
                }
                case DROP:{
                    if(!dropTile(playercol))
                        System.out.println("Invalid drop location");

                    else
                        clearedTilesNum = 0; //only need reset clearedTiles when tile added/moved

                    boardState = boardStates.CHECKSEQ;
                    break;
                }
                case SWAP:{
                    if(!swapTiles(playercol))
                        System.out.println("Invalid swap");

                    else
                        clearedTilesNum = 0; //only need reset clearedTiles when tile added/moved

                    boardState = boardStates.CHECKSEQ;
                    break;
                }
                // if no sequences change board to ready
                case CHECKSEQ: {
                    if (!markAllSequencesOnBoard())
                    {
                        attackSent = false;
                        boardState = boardStates.READY;
                    }
                    else
                    {
                        clearTime = 0;
                        boardState = boardStates.CLEARING;
                    }
                    break;
                }
                case GENERATE: {
                    dropNewTilesRow(width);
                    boardState = boardStates.READY;
                    break;
                }
                // by jiulen
                case CLEARING: {
                    if (clearTime < clearDelayTotal)
                    {
                        if (clearTime > clearAnimStart && !clearAnimStarted) //Start clear animation for all clearing tiles
                        {
                            for(int i = 1; i < numRows; ++i)
                            {
                                for(int j = 0; j< numCols; ++j)
                                {
                                    if(grid[i][j] != null)
                                    {
                                        if(grid[i][j].isAttack == true)
                                        {
                                            grid[i][j].isClearing = true;
                                        }
                                    }
                                }
                            }
                        }

                        clearTime += dt;
                    }
                    else {
                        //Clear tiles
                        clearedTilesNum += clearedTiles();
                        clearTime = 0;
                        clearAnimStarted = false;
                        boardState = boardStates.GRAVITATE;
                    }
                    break;
                }
                // jonathan
                case GRAVITATE: {
                    gravitateBoardStep();
                    boardState = boardStates.CHECKSEQ;
                    break;
                }
                case LOSE: {
                    System.out.println("LOSE");
                    lose = true;
                    break;
                }
                case WIN:{
                    System.out.println("WIN");
                    win = true;
                    break;
                }
            }
        }
    }

    // by jonathan
    void moveTilesDownGrid(int level,int width,float dt)
    {
        boolean lost = false;
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
                        float newpos = prevpos + (level * 15 * dt);
                        grid[i][j].SetPosY(newpos);
                        yoffset = width - (newpos % width);

                        // jiulen helped optimise if statement
                        if((grid[i][j].GetPosY() + grid[i][j].GetWidth() * 0.5) / grid[i][j].GetWidth() > i)
                        {
                            grid[i + 1][j] = grid[i][j];
                            grid[i + 1][j].SetPosY(width * (i - 0.5f));
                            grid[i][j] = null;
                            //boardState = boardStates.GENERATE;
                        }
                    }
                }
                else
                {
                    lost = true;
                }
            }
        }
        if (lost)
            boardState = boardStates.LOSE;
    }

    // by jonathan
    public final boolean selectTile(int col)
    {
        for(int i = 10; i >= 0; --i)
        {
            if(grid[i][col] != null)
            {
                selectedTile = grid[i][col];
                grid[12][col] = selectedTile;
                // set pos fixed by jiulen
                grid[12][col].SetPosY(grid[12][col].GetWidth() * (10.5f));
                grid[12][col].SetPosX(grid[i][col].GetWidth() * (col + 0.5f));
                grid[i][col] = null;
                return true;
            }
        }
        return false;
    }

    // by jonathan
    public final boolean dropTile(int col)
    {
        for (int i = 10; i >= 1 ; --i)
        {
            if(grid[i][col] != null)
            {
                grid[i+1][col] = selectedTile;
                // set pos fixed by jiulen
                grid[i+1][col].SetPosY((grid[i][col].GetPosY() + selectedTile.GetWidth()));
                selectedTile = null;
                for (int j = 0; j < numCols; j++)
                    grid[12][j] = null;
                return true;
            }
        }
        return false;
    }

    // by jonathan
    boolean gravitateBoardStep() {
        boolean toGravitate = false;
        for (int j = 0; j < numCols; ++j) {
            for (int i = 11; i > 0; --i) {
                // check if there is an empty space
                if (grid[i][j] == null) {
                    // if found, index it, check if there are tiles above empty space
                    for (int k = i; k < 11; k++) {
                        if (grid[k][j] != null) {
                            // Moved back a non-empty tile - board no gravity
                            float prevpos = grid[k][j].GetPosY();
                            grid[k - 1][j] = grid[k][j];
                            grid[k][j] = null;
                            // set pos fixed by jiulen
                            grid[k - 1][j].SetPosY(prevpos - grid[k - 1][j].GetWidth());

                            toGravitate = true;
                        }
                    }
                }
            }
        }
        return toGravitate;
    }

    // by jonathan
    boolean dropNewTilesRow(int width) {
        boolean dropped = false;
        for(int j = 0; j < numCols; ++j) {
            if(grid[0][j] == null && grid[1][j] == null) {
                dropped = true;
                do {
                    if (grid[1][j] != null)
                        grid[1][j].SetIsDone(true);
                    grid[1][j] = TileEntity.Create(randomTile(), width,width * (j + 0.5f), width * (0.5f) - width);
                } while (isBeginningOfSequence(1, j) || isEndOfSequence(1, j));
            }
        }
        return dropped;
    }

    // swap on 1 column
    // by jonathan
    boolean swapTiles(int col) {
        for(int row = 10; row > 0; --row) {
            if (grid[row][col] != null && grid[row - 1][col] != null) {
                if (grid[row][col].tileType != grid[row - 1][col].tileType)
                {
                    TileEntity tmp = grid[row-1][col];

                    // setpos fixed by jiulen
                    grid[row - 1][col] = grid[row][col];
                    grid[row - 1][col].SetPosY(grid[row - 1][col].GetPosY() - grid[row - 1][col].GetWidth());

                    grid[row][col] = tmp;
                    grid[row][col].SetPosY(grid[row][col].GetPosY() + grid[row][col].GetWidth());
                }
                return true;
            }
        }
        return false;
    }


    // by jonathan
    int clearedTiles(){
        int cleared = 0;
        for(int i = 1; i < numRows; ++i)
        {
            for(int j = 0; j< numCols; ++j)
            {
                if(grid[i][j] != null)
                {
                    if(grid[i][j].isAttack == true)
                    {
                        grid[i][j].SetIsDone(true);
                        grid[i][j] = null;
                        cleared++;
                    }
                }
            }
        }
        return cleared;
    }

    // by jonathan
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

    // by jonathan, start column position fixed by jiulen
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
                if(grid[i][j+1] != null)
                {
                    // Found a sequence of same tiles in a row of length 2 at least.
                    if (grid[i][j].tileType == grid[i][j + 1].tileType) {
                        curMatchLen++;
                    }
                    else {
                        // match sequence broken - check if previous sequence was more than 4
                        if (curMatchLen >= 4) {
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
                else {
                    // match sequence broken - check if previous sequence was more than 4
                    if (curMatchLen >= 4) {
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
            if (curMatchLen >= 4) {
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
            for (int i = 0; i < numRows - 2; ++i) {
                if (grid[i][j] == null) {
                    curMatchLen = 1;
                    continue;
                }
                if (grid[i + 1][j] != null)
                {
                    // Found a sequence of same tiles in a row of length 2 at least.
                    if (grid[i][j].tileType == grid[i + 1][j].tileType) {
                        curMatchLen++;
                    }
                    else {
                        // match sequence broken - check if previous sequence was more than 4
                        if (curMatchLen >= 4) {
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
                else {
                    // match sequence broken - check if previous sequence was more than 4
                    if (curMatchLen >= 4) {
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
            if (curMatchLen >= 4) {
                matchingSequences.add(
                        new TileSequence(grid[numRows - 2][j].tileType,
                                TileSequence.Orientation.VERTICAL,
                                numRows - 1 - curMatchLen, // Start row position for the sequence
                                j,
                                curMatchLen));
            }
        }

        return !matchingSequences.isEmpty();
    }

    // by jonathan
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
    // done by jonathan updated by jiulen
    boolean isBeginningOfSequence(int i, int j) {
        if (j >= 0 && j < numCols-3 && grid[i][j] != null && grid[i][j+1] != null && grid[i][j+2] != null && grid[i][j+3] != null)
        {
            if (grid[i][j].tileType == grid[i][j+1].tileType && grid[i][j].tileType == grid[i][j+2].tileType && grid[i][j].tileType == grid[i][j+3].tileType)
                return true; //continue if false
        }

        if (i >= 0 && i < numRows-3 && grid[i][j] != null && grid[i+1][j] != null && grid[i+2][j] != null && grid[i+3][j] != null)
        {
            if (grid[i][j].tileType == grid[i+1][j].tileType && grid[i][j].tileType == grid[i+2][j].tileType && grid[i][j].tileType == grid[i+3][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }

    // done by jonathan updated by jiulen
    boolean isEndOfSequence(int i,int j) {
        if (j < numCols && j>=3 && grid[i][j] != null && grid[i][j-1] != null && grid[i][j-2] != null && grid[i][j-3] != null)
        {
            if (grid[i][j-1].tileType == grid[i][j].tileType && grid[i][j-2].tileType == grid[i][j].tileType && grid[i][j-3].tileType == grid[i][j].tileType)
                return true; //continue if false
        }

        if (i < numRows-2 && i>=3 && grid[i][j] != null && grid[i-1][j] != null && grid[i-2][j] != null && grid[i-3][j] != null)
        {
            if (grid[i-1][j].tileType == grid[i][j].tileType && grid[i-2][j].tileType == grid[i][j].tileType && grid[i-3][j].tileType == grid[i][j].tileType)
                return true; //continue if false
        }

        return false; //false if both is true
    }

    //done by jiulen
//    boolean isBeginningOfSequenceForZero(int j) //row always 0 for this (only use for spawning at row=0)
//    {
//        if (j >= 0 && j < numCols-3 && grid[0][j] != null && grid[0][j+1] != null && grid[0][j+2] != null && grid[0][j+3] != null)
//        {
//            if (grid[0][j].tileType == grid[0][j+1].tileType && grid[0][j].tileType == grid[0][j+2].tileType && grid[0][j].tileType == grid[0][j+3].tileType)
//                return true; //continue if false
//        }
//
//        if (grid[0][j] != null && grid[2][j] != null)
//        {
//            if (grid[0][j].tileType == grid[2][j].tileType)
//                return true; //continue if false
//        }
//
//        return false; //false if both is true
//    }
//
//    boolean isEndOfSequenceForZero(int j) {
//        if (j < numCols && j>=3 && grid[0][j] != null && grid[0][j-1] != null && grid[0][j-2] != null && grid[0][j-3] != null)
//        {
//            if (grid[0][j-1].tileType == grid[0][j].tileType && grid[0][j-2].tileType == grid[0][j].tileType && grid[0][j-3].tileType == grid[0][j].tileType)
//                return true; //continue if false
//        }
//
//        return false; //false if both is true
//    }
}
