package com.sdm.mgp2022;

import android.service.quicksettings.Tile;

import java.util.ArrayList;
import java.util.List;

public class AdjacentTiles {
//
//    public static void checkAndAddIdenticalElements(TileEntity[][] arr, List<IdenticalElementsInfo> identicalElements, int row, int col, int dx, int dy, int minLength) {
//        int length = 1;
//        int x = col + dx;
//        int y = row + dy;
//
//        // Iterate to the direction specified by dx and dy
//        while (arr[y][x] != null && x >= 0 && x < arr[0].length && y >= 0 && y < arr.length && arr[y][x].tileType == arr[row][col].tileType) {
//            length++;
//            x += dx;
//            y += dy;
//        }
//        // If the number of identical elements is greater than or equal to the minimum length, add it to the list
//        if (length >= minLength) {
//            identicalElements.add(new IdenticalElementsInfo(length, row, col));
//        }
//    }
//
//    public static List<IdenticalElementsInfo> findIdenticalAdjacentElements(TileEntity[][] arr, int minLength) {
//        List<IdenticalElementsInfo> identicalElements = new ArrayList<>();
//        // Iterate through each element in the array
//        for (int row = 0; row < arr.length; row++) {
//            for (int col = 0; col < arr[0].length; col++) {
//                if(arr[row][col] != null)
//                {
//                    checkAndAddIdenticalElements(arr, identicalElements, row, col, 1, 0, minLength);
//                    checkAndAddIdenticalElements(arr, identicalElements, row, col, -1, 0, minLength);
//                    checkAndAddIdenticalElements(arr, identicalElements, row, col, 0, 1, minLength);
//                    checkAndAddIdenticalElements(arr, identicalElements, row, col, 0, -1, minLength);
//                }
//            }
//        }
//        return identicalElements;
//    }
}
