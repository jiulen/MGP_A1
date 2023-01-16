package com.sdm.mgp2022; //by jiu len

public class LeaderboardSorter {
    public final static LeaderboardSorter Instance = new LeaderboardSorter();

    // Uses merge sort
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    void merge(PlayerLeaderboardInfo arr[], int first, int middle, int last)
    {
        // Find sizes of two subarrays to be merged
        int n1 = middle - first + 1;
        int n2 = last - middle;

        /* Create temp arrays (Left and Right arrays)*/
        PlayerLeaderboardInfo Left[] = new PlayerLeaderboardInfo[n1];
        PlayerLeaderboardInfo Right[] = new PlayerLeaderboardInfo[n2];

        /*Copy data to temp arrays*/
        for (int i = 0; i < n1; ++i)
            Left[i] = arr[first + i];
        for (int j = 0; j < n2; ++j)
            Right[j] = arr[middle + 1 + j];

        /* Merge the temp arrays */

        // Initial indexes of first and second subarrays
        int i = 0, j = 0;

        // Initial index of merged subarray array
        int k = first;
        while (i < n1 && j < n2) {
            if (Left[i].GetPlayerScore() >= Right[j].GetPlayerScore()) {
                arr[k] = Left[i];
                i++;
            }
            else {
                arr[k] = Right[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = Left[i];
            i++;
            k++;
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = Right[j];
            j++;
            k++;
        }
    }

    // Main function that sorts arr[l..r] using
    // merge()
    void mergeSort(PlayerLeaderboardInfo arr[], int first, int last)
    {
        if (first < last) {
            // Find the middle point
            int m = first + (last - first) / 2;

            // Sort first and second halves
            mergeSort(arr, first, m);
            mergeSort(arr, m + 1, last);

            // Merge the sorted halves
            merge(arr, first, m, last);
        }
    }

    /* A utility function to print array of size n */
    public void printArray(PlayerLeaderboardInfo arr[])
    {
        int n = arr.length;
        for (int i = 0; i < n; ++i)
            System.out.print(arr[i] + " ");
        System.out.println();
    }
}
