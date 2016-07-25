package com.yuzhouwan.hacker.algorithms;

/**
 * Created by Benedict Jin on 2015/9/21.
 */
public class InsertionSort {

    public int[] insertionSort(int[] unSort) {

        for (int j = 1; j < unSort.length; j++) {

            int key = unSort[j];
            int i = j - 1;
            while (i >= 0 && unSort[i] > key) {

                unSort[i + 1] = unSort[i];
                i--;
            }
            unSort[i + 1] = key;
        }
        return unSort;
    }

}
