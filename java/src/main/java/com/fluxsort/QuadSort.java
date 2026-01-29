// QuadSort 1.2.1.3 - Java Translation
// Original C implementation by Igor van den Hoven ivdhoven@gmail.com
// Java translation - Simplified version focusing on core functionality

package com.fluxsort;

import java.util.Comparator;

/**
 * QuadSort is a stable adaptive mergesort that provides the foundation for FluxSort.
 * This is a simplified Java translation focusing on core sorting functionality.
 */
public class QuadSort {
    
    /**
     * Sort an array of integers
     */
    public void sort(int[] array, int offset, int length) {
        if (length <= 1) return;
        if (length == 2) {
            if (array[offset] > array[offset + 1]) {
                int temp = array[offset];
                array[offset] = array[offset + 1];
                array[offset + 1] = temp;
            }
            return;
        }
        
        if (length <= 32) {
            insertionSort(array, offset, length);
            return;
        }
        
        int[] swap = new int[length];
        quadSortSwap(array, offset, swap, 0, length, length);
    }
    
    /**
     * Sort using provided swap space
     */
    public void quadSortSwap(int[] array, int offset, int[] swap, int swapOffset, 
                              int swapSize, int nmemb) {
        if (nmemb <= 32) {
            insertionSort(array, offset, nmemb);
            return;
        }
        
        // Split and recursively sort
        int half = nmemb / 2;
        quadSortSwap(array, offset, swap, swapOffset, swapSize, half);
        quadSortSwap(array, offset + half, swap, swapOffset, swapSize, nmemb - half);
        
        // Merge the two halves
        if (array[offset + half - 1] <= array[offset + half]) {
            return; // Already sorted
        }
        
        merge(array, offset, swap, swapOffset, half, nmemb - half);
        System.arraycopy(swap, swapOffset, array, offset, nmemb);
    }
    
    /**
     * Sort an array with a comparator
     */
    public <T> void sort(T[] array, int offset, int length, Comparator<? super T> cmp) {
        if (length <= 1) return;
        if (length == 2) {
            if (cmp.compare(array[offset], array[offset + 1]) > 0) {
                T temp = array[offset];
                array[offset] = array[offset + 1];
                array[offset + 1] = temp;
            }
            return;
        }
        
        if (length <= 32) {
            insertionSort(array, offset, length, cmp);
            return;
        }
        
        @SuppressWarnings("unchecked")
        T[] swap = (T[]) new Object[length];
        quadSortSwap(array, offset, swap, 0, length, length, cmp);
    }
    
    /**
     * Sort using provided swap space with comparator
     */
    public <T> void quadSortSwap(T[] array, int offset, T[] swap, int swapOffset,
                                   int swapSize, int nmemb, Comparator<? super T> cmp) {
        if (nmemb <= 32) {
            insertionSort(array, offset, nmemb, cmp);
            return;
        }
        
        int half = nmemb / 2;
        quadSortSwap(array, offset, swap, swapOffset, swapSize, half, cmp);
        quadSortSwap(array, offset + half, swap, swapOffset, swapSize, nmemb - half, cmp);
        
        if (cmp.compare(array[offset + half - 1], array[offset + half]) <= 0) {
            return;
        }
        
        merge(array, offset, swap, swapOffset, half, nmemb - half, cmp);
        System.arraycopy(swap, swapOffset, array, offset, nmemb);
    }
    
    /**
     * Reverse a section of an array
     */
    public void reverse(int[] array, int offset, int length) {
        int left = offset;
        int right = offset + length - 1;
        while (left < right) {
            int temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
    
    /**
     * Reverse a section of an array with generic type
     */
    public <T> void reverse(T[] array, int offset, int length) {
        int left = offset;
        int right = offset + length - 1;
        while (left < right) {
            T temp = array[left];
            array[left] = array[right];
            array[right] = temp;
            left++;
            right--;
        }
    }
    
    /**
     * Cross merge two sections (quad1 and quad2)
     */
    public void crossMerge(int[] dest, int destOffset, int[] src, int srcOffset, 
                            int len1, int len2) {
        int i = srcOffset;
        int j = srcOffset + len1;
        int k = destOffset;
        int end1 = srcOffset + len1;
        int end2 = srcOffset + len1 + len2;
        
        while (i < end1 && j < end2) {
            if (src[i] <= src[j]) {
                dest[k++] = src[i++];
            } else {
                dest[k++] = src[j++];
            }
        }
        
        while (i < end1) {
            dest[k++] = src[i++];
        }
        
        while (j < end2) {
            dest[k++] = src[j++];
        }
    }
    
    /**
     * Cross merge with comparator
     */
    public <T> void crossMerge(T[] dest, int destOffset, T[] src, int srcOffset,
                                int len1, int len2, Comparator<? super T> cmp) {
        int i = srcOffset;
        int j = srcOffset + len1;
        int k = destOffset;
        int end1 = srcOffset + len1;
        int end2 = srcOffset + len1 + len2;
        
        while (i < end1 && j < end2) {
            if (cmp.compare(src[i], src[j]) <= 0) {
                dest[k++] = src[i++];
            } else {
                dest[k++] = src[j++];
            }
        }
        
        while (i < end1) {
            dest[k++] = src[i++];
        }
        
        while (j < end2) {
            dest[k++] = src[j++];
        }
    }
    
    /**
     * Merge two sorted sections
     */
    private void merge(int[] src, int srcOffset, int[] dest, int destOffset, 
                       int len1, int len2) {
        int i = srcOffset;
        int j = srcOffset + len1;
        int k = destOffset;
        int end1 = srcOffset + len1;
        int end2 = srcOffset + len1 + len2;
        
        while (i < end1 && j < end2) {
            if (src[i] <= src[j]) {
                dest[k++] = src[i++];
            } else {
                dest[k++] = src[j++];
            }
        }
        
        while (i < end1) {
            dest[k++] = src[i++];
        }
        
        while (j < end2) {
            dest[k++] = src[j++];
        }
    }
    
    /**
     * Merge with comparator
     */
    private <T> void merge(T[] src, int srcOffset, T[] dest, int destOffset,
                            int len1, int len2, Comparator<? super T> cmp) {
        int i = srcOffset;
        int j = srcOffset + len1;
        int k = destOffset;
        int end1 = srcOffset + len1;
        int end2 = srcOffset + len1 + len2;
        
        while (i < end1 && j < end2) {
            if (cmp.compare(src[i], src[j]) <= 0) {
                dest[k++] = src[i++];
            } else {
                dest[k++] = src[j++];
            }
        }
        
        while (i < end1) {
            dest[k++] = src[i++];
        }
        
        while (j < end2) {
            dest[k++] = src[j++];
        }
    }
    
    /**
     * Insertion sort for small arrays
     */
    private void insertionSort(int[] array, int offset, int length) {
        for (int i = offset + 1; i < offset + length; i++) {
            int key = array[i];
            int j = i - 1;
            
            while (j >= offset && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
    
    /**
     * Insertion sort with comparator
     */
    private <T> void insertionSort(T[] array, int offset, int length, 
                                     Comparator<? super T> cmp) {
        for (int i = offset + 1; i < offset + length; i++) {
            T key = array[i];
            int j = i - 1;
            
            while (j >= offset && cmp.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
