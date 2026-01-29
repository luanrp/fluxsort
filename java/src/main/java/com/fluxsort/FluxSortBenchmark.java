// FluxSort Benchmark - Performance comparison with Arrays.sort
// Compares FluxSort with Java's built-in Arrays.sort across different data patterns and sizes

package com.fluxsort;

import java.util.Arrays;
import java.util.Random;

/**
 * Benchmark program comparing FluxSort performance with Java's Arrays.sort
 * Tests various array sizes and data patterns
 */
public class FluxSortBenchmark {
    
    private static final int WARMUP_ITERATIONS = 3;
    private static final int BENCHMARK_ITERATIONS = 5;
    
    public static void main(String[] args) {
        System.out.println("FluxSort vs Arrays.sort Benchmark");
        System.out.println("=================================\n");
        
        // Test different array sizes
        int[] sizes = {1_000, 10_000, 100_000, 1_000_000};
        
        for (int size : sizes) {
            System.out.println("Array size: " + String.format("%,d", size));
            System.out.println("-".repeat(60));
            
            benchmarkRandomData(size);
            benchmarkSortedData(size);
            benchmarkReverseSortedData(size);
            benchmarkPartiallySortedData(size);
            benchmarkManyDuplicates(size);
            
            System.out.println();
        }
        
        System.out.println("\nBenchmark completed successfully!");
    }
    
    /**
     * Benchmark with random data
     */
    private static void benchmarkRandomData(int size) {
        System.out.println("Pattern: Random");
        
        Random random = new Random(42);
        int[] original = new int[size];
        for (int i = 0; i < size; i++) {
            original[i] = random.nextInt(size);
        }
        
        runComparison(original, "  ");
    }
    
    /**
     * Benchmark with already sorted data
     */
    private static void benchmarkSortedData(int size) {
        System.out.println("Pattern: Already Sorted");
        
        int[] original = new int[size];
        for (int i = 0; i < size; i++) {
            original[i] = i;
        }
        
        runComparison(original, "  ");
    }
    
    /**
     * Benchmark with reverse sorted data
     */
    private static void benchmarkReverseSortedData(int size) {
        System.out.println("Pattern: Reverse Sorted");
        
        int[] original = new int[size];
        for (int i = 0; i < size; i++) {
            original[i] = size - i;
        }
        
        runComparison(original, "  ");
    }
    
    /**
     * Benchmark with partially sorted data (80% sorted)
     */
    private static void benchmarkPartiallySortedData(int size) {
        System.out.println("Pattern: Partially Sorted (80%)");
        
        int[] original = new int[size];
        for (int i = 0; i < size; i++) {
            original[i] = i;
        }
        
        // Shuffle 20% of the array
        Random random = new Random(42);
        int shuffleCount = size / 5;
        for (int i = 0; i < shuffleCount; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = original[idx1];
            original[idx1] = original[idx2];
            original[idx2] = temp;
        }
        
        runComparison(original, "  ");
    }
    
    /**
     * Benchmark with many duplicate values (low cardinality)
     */
    private static void benchmarkManyDuplicates(int size) {
        System.out.println("Pattern: Many Duplicates (100 unique values)");
        
        Random random = new Random(42);
        int[] original = new int[size];
        for (int i = 0; i < size; i++) {
            original[i] = random.nextInt(100);
        }
        
        runComparison(original, "  ");
    }
    
    /**
     * Run comparison between FluxSort and Arrays.sort
     */
    private static void runComparison(int[] original, String indent) {
        FluxSort fluxSort = new FluxSort();
        
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            int[] arr = original.clone();
            fluxSort.sort(arr);
            
            arr = original.clone();
            Arrays.sort(arr);
        }
        
        // Benchmark FluxSort
        long fluxSortTotal = 0;
        int[] lastFluxSortResult = null;
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            int[] arr = original.clone();
            long start = System.nanoTime();
            fluxSort.sort(arr);
            long end = System.nanoTime();
            fluxSortTotal += (end - start);
            lastFluxSortResult = arr;
        }
        double fluxSortAvg = fluxSortTotal / (double) BENCHMARK_ITERATIONS / 1_000_000.0;
        
        // Verify correctness of FluxSort
        if (!isSorted(lastFluxSortResult)) {
            System.out.println(indent + "ERROR: FluxSort did not sort correctly!");
        }
        
        // Benchmark Arrays.sort
        long arraysSortTotal = 0;
        int[] lastArraysSortResult = null;
        for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
            int[] arr = original.clone();
            long start = System.nanoTime();
            Arrays.sort(arr);
            long end = System.nanoTime();
            arraysSortTotal += (end - start);
            lastArraysSortResult = arr;
        }
        double arraysSortAvg = arraysSortTotal / (double) BENCHMARK_ITERATIONS / 1_000_000.0;
        
        // Verify correctness of Arrays.sort
        if (!isSorted(lastArraysSortResult)) {
            System.out.println(indent + "ERROR: Arrays.sort did not sort correctly!");
        }
        
        // Calculate speedup
        double speedup = arraysSortAvg / fluxSortAvg;
        String speedupStr = speedup > 1.0 ? 
            String.format("%.2fx faster", speedup) : 
            String.format("%.2fx slower", 1.0 / speedup);
        
        // Print results
        System.out.printf("%sFluxSort:    %8.2f ms\n", indent, fluxSortAvg);
        System.out.printf("%sArrays.sort: %8.2f ms\n", indent, arraysSortAvg);
        System.out.printf("%sSpeedup:     %s\n", indent, speedupStr);
    }
    
    /**
     * Check if array is sorted
     */
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
}
