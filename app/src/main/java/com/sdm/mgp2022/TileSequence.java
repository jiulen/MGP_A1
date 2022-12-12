package com.sdm.mgp2022;

import java.lang.reflect.Type;

public class TileSequence {

    private Orientation s_orientation;
    TileEntity.TILE_TYPES typeid;
    private int s_size;
    private int row, col;

    enum Orientation
    {
        HORIZONTAL,
        VERTICAL
    }

    TileSequence(TileEntity.TILE_TYPES type, Orientation orientation, int startRow, int startCol, int size)
    {
        s_orientation = orientation;
        col = startCol;
        row = startRow;
        s_size = size;
        typeid = type;
    }

    public final TileEntity.TILE_TYPES getTypeId() {
        return typeid;
    }

    public final Orientation getOrientation() {
        return s_orientation;
    }

    public final int getSize() {
        return s_size;
    }

    public final int getStartRow() {
        return row;
    }

    public final int getStartCol() {
        return col;
    }
}
