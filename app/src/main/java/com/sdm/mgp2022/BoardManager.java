package com.sdm.mgp2022;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class BoardManager {
    private int numRows = 11;
    private int numCols = 6;
    private int startGarbage = 3;
    private int m_numTileTypes;
    private static final List<TileEntity.TILE_TYPES> TILES = Collections.unmodifiableList(Arrays.asList(TileEntity.TILE_TYPES.values()));
    private static final int tilesSize = TILES.size();
    private static final Random RANDOM = new Random();

    Vector<TileSequence> matchingSequences;

    TileEntity.TILE_TYPES[][] grid;

    void BoardModel(int numRows, int numCols, int numTileTypes)
    {
        fillBoard();
    }

    public static TileEntity.TILE_TYPES randomTile()
    {
        return TILES.get(RANDOM.nextInt(tilesSize));
    }

    public void fillBoard()
    {
        boolean boardReady = false;

        // Fill board with random tiles.
        for(int i = 0; i < startGarbage; ++i) {
            for(int j = 0; j < numCols; ++j) {
                grid[i][j] = randomTile();
            }
        }

        // While there was any match in the board, fix the causing tile with a new random tile and check again
        while(!boardReady) {
            boardReady = true;
            // Note that we must NOT iterate until m_numRows-2, as this will cause us to miss tiles
            // the isBeginningOfSequence method checks if the row is in boundary.
            for(int i = 0; i < numRows; ++i) {
                for(int j = 0; j < numCols; ++j) {
                    if(isBeginningOfSequence(i, j)) {
                        boardReady = false;
                        grid[i][j] = randomTile();
                    }
                }
            }
        }
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
                if(grid[i][j] == TileEntity.TILE_TYPES.TILE_EMPTY) {
                    // Found empty tile - move all tiles above it back.
                    for(int k = i; k > 0; --k) {
                        if(grid[k - 1][j] != TileEntity.TILE_TYPES.TILE_EMPTY) {
                            // Moved back a non-empty tile - board no gravity
                            found = true;
                        }
                        grid[k][j] = grid[k - 1][j];
                        grid[k - 1][j] = TileEntity.TILE_TYPES.TILE_EMPTY;
                    }
                    break;
                }
            }
        }
        return found;
    }

    boolean dropNewTilesRow() {
        boolean dropped = false;
        for(int j = 0; j < numCols; ++j) {
            if(grid[0][j] == TileEntity.TILE_TYPES.TILE_EMPTY) {
                dropped = true;
                grid[0][j] = randomTile();
            }
        }
        return dropped;
    }

    // change to only swap on 1 column
    boolean swapTiles(int row1, int col1, int row2, int col2) {
        if((col1 == col2 && (row1 == row2-1 || row1 == row2+1)) ||
                (row1 == row2 && (col1 == col2-1 || col1 == col2+1)))
        {
            if(grid[row1][col1] == TileEntity.TILE_TYPES.TILE_EMPTY || grid[row2][col2] == TileEntity.TILE_TYPES.TILE_EMPTY ||
                    grid[row1][col1] ==  grid[row2][col2]) {
                return false; // if either tile is empty or they have same color - no need to swap
            }
            TileEntity.TILE_TYPES tmp = grid[row1][col1];
            grid[row1][col1] = grid[row2][col2];
            grid[row2][col2] = tmp;
            return true;
        }
        return false; // tiles are not adjacent.
    }


    boolean hasMoreMoves() {
        boolean hasMove = false;
        // Horizontal moves - pass on every column and switch each tile with the one to its right.
        for(int j = 0; j < numCols-1; ++j) {
            for(int i = 0; i < numRows; ++i) {
                if(hasMove) {
                    return true;
                }

                if(swapTiles(i, j, i, j+1)) {
                    if(hasSequencesProximity(i, j)) {
                        hasMove=true;
                    }
                    // Swap occurred - swap back to preserve board state.
                    swapTiles(i, j, i, j+1);
                }
            }
        }

        // Vertical moves - pass on every row and switch each tile with the one to below it.
        for(int i = 0; i< numRows - 1; ++i) {
            for(int j = 0; j < numCols; ++j) {
                if(hasMove) {
                    return true;
                }

                if(swapTiles(i, j, i+1, j)) {
                    if(hasSequencesProximity(i, j)) {
                        hasMove=true;
                    }
                    // Swap occurred - swap back to preserve board state.
                    swapTiles(i, j, i+1, j);
                }
            }
        }
        return hasMove;
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
                if (grid[i][j] == TileEntity.TILE_TYPES.TILE_EMPTY) {
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
                                new TileSequence(grid[i][j],
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
                        new TileSequence(grid[i][numCols - 1],
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
                if (grid[i][j] == TileEntity.TILE_TYPES.TILE_EMPTY) {
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
                                new TileSequence(grid[i][j],
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
                        new TileSequence(grid[numRows - 1][j],
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
                grid[sequence.getStartRow()][j] = TileEntity.TILE_TYPES.TILE_EMPTY;
            }
        }
        else if (sequence.getOrientation() == TileSequence.Orientation.VERTICAL) {
            for (int i = sequence.getStartRow();
                 i < sequence.getStartRow() + sequence.getSize();
                 ++i)
            {
                grid[i][sequence.getStartCol()] = TileEntity.TILE_TYPES.TILE_EMPTY;
            }
        }
    }

    // change to 4
    boolean isBeginningOfSequence(int i, int j) {
        return ((j >= 0 && j < numCols-2 && grid[i][j] == grid[i][j+1] && grid[i][j] == grid[i][j+2]) ||
                (i >= 0 && i < numRows-2 && grid[i][j] == grid[i+1][j] && grid[i][j] == grid[i+2][j]));
    }


    boolean isEndOfSequence(int i,int j) {
        return ((j < numCols && j>=2 && grid[i][j-1] == grid[i][j] && grid[i][j-2] == grid[i][j]) ||
                (i < numRows && i>=2 && grid[i-1][j] == grid[i][j] && grid[i-2][j] == grid[i][j]));
    }
}
