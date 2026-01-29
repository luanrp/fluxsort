// FluxSort 1.2.1.3 - Java Translation
// Original C implementation by Igor van den Hoven ivdhoven@gmail.com
// Java translation - Simplified and adapted for Java

package com.fluxsort;

import java.util.Comparator;

/**
 * FluxSort is a stable quicksort/mergesort hybrid with exceptional performance.
 * It is stable, adaptive, and handles both random and partially sorted data efficiently.
 * 
 * This is a Java translation of the original C implementation.
 * 
 * Key features:
 * - Analyzes array for presortedness before partitioning
 * - Uses QuadSort for small arrays and sorted segments
 * - Intelligent pivot selection (median of 9)
 * - Worst-case O(n log n) time complexity
 * - Stable sorting
 */
public class FluxSort {
    
    private static final int FLUX_OUT = 128;  // Increased to reduce recursion overhead
    private static final int ANALYZE_THRESHOLD = 160;
    
    private final QuadSort quadSort;
    
    public FluxSort() {
        this.quadSort = new QuadSort();
    }
    
    /**
     * Sort an array of integers
     */
    public void sort(int[] array) {
        if (array == null || array.length < 2) return;
        sortInts(array, 0, array.length);
    }
    
    /**
     * Sort a portion of an integer array
     */
    private void sortInts(int[] array, int offset, int length) {
        if (length <= ANALYZE_THRESHOLD) {
            quadSort.sort(array, offset, length);
            return;
        }
        
        int[] swap = new int[length];
        fluxSortRecursive(array, offset, swap, 0, length);
    }
    
    /**
     * Recursive flux sort implementation
     */
    private void fluxSortRecursive(int[] array, int offset, int[] swap, int swapOffset, int length) {
        if (length <= FLUX_OUT) {
            quadSort.sort(array, offset, length);
            return;
        }
        
        // Check if already sorted or reverse sorted
        boolean sorted = true;
        boolean reverseSorted = true;
        for (int i = offset + 1; i < offset + length; i++) {
            if (array[i - 1] > array[i]) {
                sorted = false;
            }
            if (array[i - 1] < array[i]) {
                reverseSorted = false;
            }
            // Early exit if neither sorted nor reverse sorted
            if (!sorted && !reverseSorted) {
                break;
            }
        }
        if (sorted) return;
        
        // If reverse sorted, reverse the array in O(n) time
        if (reverseSorted) {
            int left = offset;
            int right = offset + length - 1;
            while (left < right) {
                int temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                left++;
                right--;
            }
            return;
        }
        
        // Select pivot using median of 9
        int pivot = medianOfNine(array, offset, length);
        
        // Partition into elements <= pivot and elements > pivot
        int left = offset;
        int right = 0;
        
        for (int i = offset; i < offset + length; i++) {
            if (array[i] <= pivot) {
                array[left++] = array[i];
            } else {
                swap[swapOffset + right++] = array[i];
            }
        }
        
        int leftSize = left - offset;
        int rightSize = right;
        
        // Copy right partition back to array
        System.arraycopy(swap, swapOffset, array, offset + leftSize, rightSize);
        
        // Handle degenerate cases (switch to quadsort if partition is very imbalanced)
        if (leftSize == 0 || rightSize == 0) {
            quadSort.sort(array, offset, length);
            return;
        }
        
        if (leftSize < length / 32 || rightSize < length / 32) {
            quadSort.sort(array, offset, length);
            return;
        }
        
        // Recursively sort partitions
        fluxSortRecursive(array, offset, swap, swapOffset, leftSize);
        fluxSortRecursive(array, offset + leftSize, swap, swapOffset, rightSize);
    }
    
    /**
     * Sort generic array with comparator
     */
    public <T> void sort(T[] array, Comparator<? super T> cmp) {
        if (array == null || array.length < 2 || cmp == null) return;
        sort(array, 0, array.length, cmp);
    }
    
    /**
     * Sort a portion of generic array
     */
    public <T> void sort(T[] array, int offset, int length, Comparator<? super T> cmp) {
        if (length <= ANALYZE_THRESHOLD) {
            quadSort.sort(array, offset, length, cmp);
            return;
        }
        
        @SuppressWarnings("unchecked")
        T[] swap = (T[]) new Object[length];
        fluxSortRecursive(array, offset, swap, 0, length, cmp);
    }
    
    /**
     * Recursive flux sort for generic types
     */
    private <T> void fluxSortRecursive(T[] array, int offset, T[] swap, int swapOffset, 
                                         int length, Comparator<? super T> cmp) {
        if (length <= FLUX_OUT) {
            quadSort.sort(array, offset, length, cmp);
            return;
        }
        
        // Check if already sorted or reverse sorted
        boolean sorted = true;
        boolean reverseSorted = true;
        for (int i = offset + 1; i < offset + length; i++) {
            if (cmp.compare(array[i - 1], array[i]) > 0) {
                sorted = false;
            }
            if (cmp.compare(array[i - 1], array[i]) < 0) {
                reverseSorted = false;
            }
            // Early exit if neither sorted nor reverse sorted
            if (!sorted && !reverseSorted) {
                break;
            }
        }
        if (sorted) return;
        
        // If reverse sorted, reverse the array in O(n) time
        if (reverseSorted) {
            int left = offset;
            int right = offset + length - 1;
            while (left < right) {
                T temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                left++;
                right--;
            }
            return;
        }
        
        // Select pivot using median of 9
        T pivot = medianOfNine(array, offset, length, cmp);
        
        // Partition
        int left = offset;
        int right = 0;
        
        for (int i = offset; i < offset + length; i++) {
            if (cmp.compare(array[i], pivot) <= 0) {
                array[left++] = array[i];
            } else {
                swap[swapOffset + right++] = array[i];
            }
        }
        
        int leftSize = left - offset;
        int rightSize = right;
        
        // Copy right partition back
        System.arraycopy(swap, swapOffset, array, offset + leftSize, rightSize);
        
        // Handle degenerate cases
        if (leftSize == 0 || rightSize == 0) {
            quadSort.sort(array, offset, length, cmp);
            return;
        }
        
        if (leftSize < length / 32 || rightSize < length / 32) {
            quadSort.sort(array, offset, length, cmp);
            return;
        }
        
        // Recursively sort
        fluxSortRecursive(array, offset, swap, swapOffset, leftSize, cmp);
        fluxSortRecursive(array, offset + leftSize, swap, swapOffset, rightSize, cmp);
    }
    
    /**
     * Calculate median of 9 elements for pivot selection
     */
    private int medianOfNine(int[] array, int offset, int length) {
        if (length < 9) {
            return array[offset + length / 2];
        }
        
        int[] samples = new int[9];
        int step = length / 9;
        
        for (int i = 0; i < 9; i++) {
            samples[i] = array[offset + i * step];
        }
        
        // Sort groups of 4
        trimFour(samples, 0);
        trimFour(samples, 4);
        
        samples[0] = samples[5];
        samples[3] = samples[8];
        
        trimFour(samples, 0);
        
        samples[0] = samples[6];
        
        // Branchless median of 3
        boolean x = samples[0] > samples[1];
        boolean y = samples[0] > samples[2];
        boolean z = samples[1] > samples[2];
        
        int idx = (x == y ? 1 : 0) + ((y ^ z) ? 1 : 0);
        return samples[idx];
    }
    
    /**
     * Generic median of 9
     */
    private <T> T medianOfNine(T[] array, int offset, int length, Comparator<? super T> cmp) {
        if (length < 9) {
            return array[offset + length / 2];
        }
        
        @SuppressWarnings("unchecked")
        T[] samples = (T[]) new Object[9];
        int step = length / 9;
        
        for (int i = 0; i < 9; i++) {
            samples[i] = array[offset + i * step];
        }
        
        // Sort groups of 4
        trimFour(samples, 0, cmp);
        trimFour(samples, 4, cmp);
        
        samples[0] = samples[5];
        samples[3] = samples[8];
        
        trimFour(samples, 0, cmp);
        
        samples[0] = samples[6];
        
        // Median of 3
        int x = cmp.compare(samples[0], samples[1]) > 0 ? 1 : 0;
        int y = cmp.compare(samples[0], samples[2]) > 0 ? 1 : 0;
        int z = cmp.compare(samples[1], samples[2]) > 0 ? 1 : 0;
        
        int idx = (x == y ? 1 : 0) + ((y != z) ? 1 : 0);
        return samples[idx];
    }
    
    /**
     * Sort and trim 4 elements - keeps middle 2
     */
    private void trimFour(int[] arr, int offset) {
        // Sort pairs
        if (arr[offset] > arr[offset + 1]) {
            int temp = arr[offset];
            arr[offset] = arr[offset + 1];
            arr[offset + 1] = temp;
        }
        if (arr[offset + 2] > arr[offset + 3]) {
            int temp = arr[offset + 2];
            arr[offset + 2] = arr[offset + 3];
            arr[offset + 3] = temp;
        }
        
        // Keep middle elements
        if (arr[offset] <= arr[offset + 2]) {
            arr[offset + 2] = arr[offset];
        }
        
        if (arr[offset + 1] > arr[offset + 3]) {
            arr[offset] = arr[offset + 3];
        } else {
            arr[offset] = arr[offset + 1];
        }
    }
    
    /**
     * Generic trim four
     */
    private <T> void trimFour(T[] arr, int offset, Comparator<? super T> cmp) {
        // Sort pairs
        if (cmp.compare(arr[offset], arr[offset + 1]) > 0) {
            T temp = arr[offset];
            arr[offset] = arr[offset + 1];
            arr[offset + 1] = temp;
        }
        if (cmp.compare(arr[offset + 2], arr[offset + 3]) > 0) {
            T temp = arr[offset + 2];
            arr[offset + 2] = arr[offset + 3];
            arr[offset + 3] = temp;
        }
        
        // Keep middle elements
        if (cmp.compare(arr[offset], arr[offset + 2]) <= 0) {
            arr[offset + 2] = arr[offset];
        }
        
        if (cmp.compare(arr[offset + 1], arr[offset + 3]) > 0) {
            arr[offset] = arr[offset + 3];
        } else {
            arr[offset] = arr[offset + 1];
        }
    }
}
