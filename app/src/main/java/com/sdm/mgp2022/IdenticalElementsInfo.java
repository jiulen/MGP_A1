package com.sdm.mgp2022;

public class IdenticalElementsInfo {
    int count;
    int row;
    int col;

    public IdenticalElementsInfo(int _count, int _row, int _col) {
        count = _count;
        row = _row;
        col = _col;
    }

    public int getPositionX() {
        return row;
    }

    public int getPositionY() {
        return col;
    }
}
