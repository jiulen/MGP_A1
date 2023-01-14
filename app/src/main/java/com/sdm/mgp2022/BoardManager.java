package com.sdm.mgp2022;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.ArrayList;

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
    private final int MIN_LENGTH = 4;
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

    public TileEntity [][] grid = new TileEntity[numRows][numCols];
    public TileEntity selectedTile = null;

    private Vector<TileEntity> garbageVector = new Vector<TileEntity>(); //stores all garbage tiles

    private final float clearDelayTotal = 0.5f;
    private final float clearAnimStart = 0.2f;
    private float clearTime = 0;
    private boolean clearAnimStarted = false;

    public int clearedTilesNum = 0;
    public boolean attackSent = true;

    public int tileMoveSpeed = 15;
    public int moveSpeedMultiplier = 1;

    enum playerAnimation {
        DROP,
        SELECT,
        NONE
    }
    public playerAnimation playerAnim = playerAnimation.NONE;

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
                    if(MarkIdenticalTilesVar(4)) {
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
                    if (emptyLastRow) {
                        moveSpeedMultiplier = 1; //reset moveSpeedMultiplier when next row of tiles spawns
                        boardState = boardStates.GENERATE;
                    }

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
                    else
                        playerAnim = playerAnimation.SELECT;

                    boardState = boardStates.READY;
                    break;
                }
                case DROP:{
                    if(!dropTile(playercol))
                        System.out.println("Invalid drop location");
                    else
                    {
                        clearedTilesNum = 0; //only need reset clearedTiles when tile added/moved
                        playerAnim = playerAnimation.DROP;
                    }

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
                    if (!MarkIdenticalTiles())
                    {
                        attackSent = false;
                        boardState = boardStates.READY;
                    }
                    else
                    {
                        CheckCleanGarbage();

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
                        float newpos = prevpos + ((0.5f + 0.5f * level) * tileMoveSpeed * moveSpeedMultiplier * dt);
                        grid[i][j].SetPosY(newpos);
                        yoffset = width - (newpos % width);

                        // jiulen helped optimise if statement
                        if((grid[i][j].GetPosY() + grid[i][j].GetWidth() * 0.5) / grid[i][j].GetWidth() > i)
                        {
                            grid[i + 1][j] = grid[i][j];
                            grid[i + 1][j].SetPosY(width * (i - 0.5f));
                            grid[i][j] = null;
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
                } while (MarkIdenticalTilesVar(2));
            }
        }
        return dropped;
    }

    // by jiu len
    // similar to dropNewTilesRow but for enemy attacks (so y pos is slightly different)
    // return row where tiles are dropped
    public int dropNewTilesRowEarly(int width) {
        int rowDropped = -1;
        for(int j = 0; j < numCols; ++j) {
            if (grid[0][j] != null)
                return rowDropped; //invalid

            if (grid[1][j] != null) //drop in row 0
                rowDropped = 0;
            else
                rowDropped = 1;

            do {
                if (grid[rowDropped][j] != null)
                    grid[rowDropped][j].SetIsDone(true);

                float dropYPos;
                if (rowDropped == 1)
                {
                    dropYPos = width * (0.5f) - width; //change this to find a reference tile
                }
                else
                    dropYPos = grid[rowDropped + 1][j].GetPosY() - width;

                grid[rowDropped][j] = TileEntity.Create(randomTile(), width,width * (j + 0.5f), dropYPos);
            } while (MarkIdenticalTilesVar(2));
        }
        return rowDropped;
    }

    // swap on 1 column
    // by jonathan
    boolean swapTiles(int col) {
        for(int row = 10; row > 0; --row) {
            if (grid[row][col] != null && grid[row - 1][col] != null) {
                TileEntity tmp = grid[row-1][col];

                // setpos fixed by jiulen
                grid[row - 1][col] = grid[row][col];
                grid[row - 1][col].SetPosY(grid[row - 1][col].GetPosY() - grid[row - 1][col].GetWidth());

                grid[row][col] = tmp;
                grid[row][col].SetPosY(grid[row][col].GetPosY() + grid[row][col].GetWidth());

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

    // By Jonathan
    // Implementation of depth-first search (dfs) algorithm to find chains of identical elements. The dfs algorithm starts at each element in the
    // 2D array, and checks its adjacent elements recursively. If it finds an element that is identical to the current element, it adds it to a list
    // of identical elements. When it finishes searching a chain of identical elements, it checks if the length of the chain is greater than or equal
    // to the minimum length.
    public int dfs(int i, int j, TileEntity.TILE_TYPES type, boolean[][] visited, ArrayList<Integer[]> identicalElements) {
        if (i < 0 || i >= numRows || j < 0 || j >= numCols || visited[i][j] || grid[i][j] == null || grid[i][j].tileType != type || grid[i][j].isGarbage) {
            return 0;
        }
        visited[i][j] = true;
        identicalElements.add(new Integer[]{i, j});
        return 1 + dfs(i - 1, j, type, visited, identicalElements)
                + dfs(i + 1, j, type, visited, identicalElements) 
                + dfs(i, j - 1, type, visited, identicalElements)
                + dfs(i, j + 1, type, visited, identicalElements);
    }

    // uses dfs
    public boolean MarkIdenticalTiles()
    {
        boolean[][] visited = new boolean[numRows][numCols];
        ArrayList<Integer[]> identicalElements = new ArrayList<>();
        ArrayList<Integer[]> garbageElements = new ArrayList<>();
        boolean hasattack = false;
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!visited[i][j]) {
                    if(grid[i][j] != null) {
                        int length = dfs(i, j, grid[i][j].tileType, visited, identicalElements);
                        if (length >= MIN_LENGTH) {
                            for (Integer[] element : identicalElements) {
                                // Mark identical elements that form a chain of minimum length
                                grid[element[0]][element[1]].isAttack = true;

                                // remove garbage status from adjacent tiles from the ones that got cleared
//                                dfsGarbage(element[0],element[1],garbageElements);
//                                for (Integer[] garbage : garbageElements) {
//                                    grid[garbage[0]][garbage[1]].isGarbage = false;
//                                }

                            }
                            hasattack = true;
                        }
                    }
                    garbageElements.clear();
                    identicalElements.clear();
                }
            }
        }
        return hasattack;
    }

    // similar to MarkIdenticalTiles but numRows checked can be changed
    public boolean MarkIdenticalTilesVar(int numRowsChecked)
    {
        boolean[][] visited = new boolean[numRows][numCols];
        ArrayList<Integer[]> identicalElements = new ArrayList<>();
        boolean hasattack = false;
        for (int i = 0; i < numRowsChecked; i++) {
            for (int j = 0; j < numCols; j++) {
                if (!visited[i][j]) {
                    if(grid[i][j] != null) {
                        int length = dfs(i, j, grid[i][j].tileType, visited, identicalElements);
                        if (length >= MIN_LENGTH) {
                            hasattack = true;
                        }
                    }
                    identicalElements.clear();
                }
            }
        }
        return hasattack;
    }

//    // for finding adjacent garbage tiles to turn them back to normal tiles
//    public int dfsGarbage(int i, int j, ArrayList<Integer[]> garbageTiles) {
//        if (i < 0 || i >= numRows || j < 0 || j >= numCols || grid[i][j] == null || !grid[i][j].isGarbage) {
//            return 0;
//        }
//
//        garbageTiles.add(new Integer[]{i, j});
//        return 1 + dfsGarbage(i - 1, j, garbageTiles)
//                + dfsGarbage(i + 1, j, garbageTiles)
//                + dfsGarbage(i, j - 1, garbageTiles)
//                + dfsGarbage(i, j + 1, garbageTiles);
//    }

    // By Jiu Len
    // for converting tiles to garbage tiles
    public boolean ConvertGarbage(int i, int j)
    {
        if(grid[i][j] != null) {
            grid[i][j].isGarbage = true;
            garbageVector.add(grid[i][j]);

            return true;
        }
        return false;
    }

    boolean CheckCleanGarbage()
    {
        boolean cleanedGarbage = false;
        //Clean garbage
        if (!garbageVector.isEmpty())
        {
            for (int g = garbageVector.size() - 1; g >= 0; --g)
            {
                //get coords of tile in grid
                TileEntity checkingGarbage = garbageVector.get(g);
                int garbX, garbY;
                garbX = Math.round((checkingGarbage.GetPosX() - checkingGarbage.GetWidth() * 0.5f) / checkingGarbage.GetWidth());
                garbY = Math.round((checkingGarbage.GetPosY() + checkingGarbage.GetWidth() * 0.5f) / checkingGarbage.GetWidth());

                //check for adjacent attack tiles
                boolean canClean = false;
                // check left
                if (garbX - 1 >= 0)
                {
                    if (grid[garbY][garbX - 1] != null)
                    {
                        if (grid[garbY][garbX - 1].isAttack)
                        {
                            cleanedGarbage = true;
                            canClean = true;
                        }
                    }
                }
                // check right
                if (!canClean && garbX + 1 < numCols)
                {
                    if (grid[garbY][garbX + 1] != null)
                    {
                        if (grid[garbY][garbX + 1].isAttack)
                        {
                            cleanedGarbage = true;
                            canClean = true;
                        }
                    }
                }
                // check up
                if (!canClean && garbY - 1 >= 0)
                {
                    if (grid[garbY - 1][garbX] != null)
                    {
                        if (grid[garbY - 1][garbX].isAttack)
                        {
                            cleanedGarbage = true;
                            canClean = true;
                        }
                    }
                }
                // check down
                if (!canClean && garbY + 1 < numRows - 1)
                {
                    if (grid[garbY + 1][garbX] != null)
                    {
                        if (grid[garbY + 1][garbX].isAttack)
                        {
                            cleanedGarbage = true;
                            canClean = true;
                        }
                    }
                }

                if (canClean)
                {
                    checkingGarbage.isGarbage = false;
                    garbageVector.remove(g);
                }
            }
        }

        return cleanedGarbage;
    }
}
