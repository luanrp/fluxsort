// FluxSort Demo - Java Translation
// Demonstrates the usage of FluxSort in Java

package com.fluxsort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Demo program for FluxSort Java implementation
 */
public class FluxSortDemo {
    
    public static void main(String[] args) {
        System.out.println("FluxSort Java Implementation Demo");
        System.out.println("==================================\n");
        
        // Test 1: Sort integers
        testIntegerSort();
        
        // Test 2: Sort strings
        testStringSort();
        
        // Test 3: Sort custom objects
        testCustomObjectSort();
        
        // Test 4: Performance comparison
        performanceTest();
        
        System.out.println("\nAll tests completed successfully!");
    }
    
    /**
     * Test sorting integers
     */
    private static void testIntegerSort() {
        System.out.println("Test 1: Sorting Integers");
        System.out.println("-------------------------");
        
        int[] array = {64, 34, 25, 12, 22, 11, 90, 88, 45, 50, 23, 36, 18, 77};
        System.out.println("Original array: " + Arrays.toString(array));
        
        FluxSort sorter = new FluxSort();
        sorter.sort(array);
        
        System.out.println("Sorted array:   " + Arrays.toString(array));
        System.out.println("Verification:   " + (isSorted(array) ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    /**
     * Test sorting strings
     */
    private static void testStringSort() {
        System.out.println("Test 2: Sorting Strings");
        System.out.println("-----------------------");
        
        String[] array = {"banana", "apple", "cherry", "date", "elderberry", 
                         "fig", "grape", "kiwi", "lemon", "mango"};
        System.out.println("Original array: " + Arrays.toString(array));
        
        FluxSort sorter = new FluxSort();
        sorter.sort(array, Comparator.naturalOrder());
        
        System.out.println("Sorted array:   " + Arrays.toString(array));
        System.out.println("Verification:   " + (isSorted(array, Comparator.naturalOrder()) ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    /**
     * Test sorting custom objects
     */
    private static void testCustomObjectSort() {
        System.out.println("Test 3: Sorting Custom Objects (Person by age)");
        System.out.println("-----------------------------------------------");
        
        Person[] people = {
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35),
            new Person("David", 28),
            new Person("Eve", 32)
        };
        
        System.out.println("Original array:");
        for (Person p : people) {
            System.out.println("  " + p);
        }
        
        FluxSort sorter = new FluxSort();
        sorter.sort(people, Comparator.comparingInt(p -> p.age));
        
        System.out.println("Sorted by age:");
        for (Person p : people) {
            System.out.println("  " + p);
        }
        System.out.println("Verification:   " + 
            (isSorted(people, Comparator.comparingInt(p -> p.age)) ? "PASS" : "FAIL"));
        System.out.println();
    }
    
    /**
     * Performance test comparing different array sizes
     */
    private static void performanceTest() {
        System.out.println("Test 4: Performance Test");
        System.out.println("------------------------");
        
        int[] sizes = {1000, 10000, 100000};
        FluxSort sorter = new FluxSort();
        Random random = new Random(42);
        
        for (int size : sizes) {
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(size);
            }
            
            long startTime = System.nanoTime();
            sorter.sort(array);
            long endTime = System.nanoTime();
            
            double timeMs = (endTime - startTime) / 1_000_000.0;
            boolean sorted = isSorted(array);
            
            System.out.printf("Array size %,7d: %.2f ms - %s\n", 
                size, timeMs, sorted ? "PASS" : "FAIL");
        }
    }
    
    /**
     * Check if integer array is sorted
     */
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Check if array is sorted according to comparator
     */
    private static <T> boolean isSorted(T[] array, Comparator<? super T> cmp) {
        for (int i = 1; i < array.length; i++) {
            if (cmp.compare(array[i - 1], array[i]) > 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Simple Person class for testing custom object sorting
     */
    private static class Person {
        String name;
        int age;
        
        Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
        
        @Override
        public String toString() {
            return String.format("%s (%d years)", name, age);
        }
    }
}
