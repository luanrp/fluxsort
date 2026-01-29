// Advanced FluxSort Examples
// Demonstrates more complex usage scenarios

package com.fluxsort;

import java.util.*;

/**
 * Advanced examples demonstrating various use cases of FluxSort
 */
public class AdvancedExamples {
    
    public static void main(String[] args) {
        System.out.println("FluxSort Advanced Examples");
        System.out.println("==========================\n");
        
        // Example 1: Sorting with different data distributions
        demonstrateDataDistributions();
        
        // Example 2: Sorting with reverse comparator
        demonstrateReverseSort();
        
        // Example 3: Multi-field sorting
        demonstrateMultiFieldSort();
        
        // Example 4: Stability test
        demonstrateStability();
    }
    
    /**
     * Example 1: Performance on different data distributions
     */
    private static void demonstrateDataDistributions() {
        System.out.println("Example 1: Different Data Distributions");
        System.out.println("----------------------------------------");
        
        FluxSort sorter = new FluxSort();
        int size = 10000;
        
        // Already sorted
        int[] sorted = new int[size];
        for (int i = 0; i < size; i++) sorted[i] = i;
        long start = System.nanoTime();
        sorter.sort(sorted);
        long time = System.nanoTime() - start;
        System.out.printf("Already sorted:     %.2f ms\n", time / 1_000_000.0);
        
        // Reverse sorted
        int[] reverse = new int[size];
        for (int i = 0; i < size; i++) reverse[i] = size - i;
        start = System.nanoTime();
        sorter.sort(reverse);
        time = System.nanoTime() - start;
        System.out.printf("Reverse sorted:     %.2f ms\n", time / 1_000_000.0);
        
        // Random
        int[] random = new int[size];
        Random rand = new Random(42);
        for (int i = 0; i < size; i++) random[i] = rand.nextInt(size);
        start = System.nanoTime();
        sorter.sort(random);
        time = System.nanoTime() - start;
        System.out.printf("Random:             %.2f ms\n", time / 1_000_000.0);
        
        // Mostly sorted (10% swapped)
        int[] mostlySorted = new int[size];
        for (int i = 0; i < size; i++) mostlySorted[i] = i;
        for (int i = 0; i < size / 10; i++) {
            int idx1 = rand.nextInt(size);
            int idx2 = rand.nextInt(size);
            int temp = mostlySorted[idx1];
            mostlySorted[idx1] = mostlySorted[idx2];
            mostlySorted[idx2] = temp;
        }
        start = System.nanoTime();
        sorter.sort(mostlySorted);
        time = System.nanoTime() - start;
        System.out.printf("Mostly sorted:      %.2f ms\n", time / 1_000_000.0);
        
        System.out.println();
    }
    
    /**
     * Example 2: Sorting in reverse order
     */
    private static void demonstrateReverseSort() {
        System.out.println("Example 2: Reverse Order Sorting");
        System.out.println("---------------------------------");
        
        Integer[] numbers = {5, 2, 8, 1, 9, 3, 7, 4, 6};
        System.out.println("Original:  " + Arrays.toString(numbers));
        
        FluxSort sorter = new FluxSort();
        sorter.sort(numbers, Comparator.reverseOrder());
        
        System.out.println("Descending: " + Arrays.toString(numbers));
        System.out.println();
    }
    
    /**
     * Example 3: Multi-field sorting
     */
    private static void demonstrateMultiFieldSort() {
        System.out.println("Example 3: Multi-Field Sorting");
        System.out.println("-------------------------------");
        
        Employee[] employees = {
            new Employee("Alice", "Engineering", 75000),
            new Employee("Bob", "Sales", 65000),
            new Employee("Charlie", "Engineering", 80000),
            new Employee("David", "Sales", 70000),
            new Employee("Eve", "Engineering", 75000),
        };
        
        System.out.println("Original:");
        for (Employee e : employees) System.out.println("  " + e);
        
        // Sort by department, then by salary (descending), then by name
        FluxSort sorter = new FluxSort();
        sorter.sort(employees, 
            Comparator.comparing(Employee::getDepartment)
                      .thenComparing(Comparator.comparing(Employee::getSalary).reversed())
                      .thenComparing(Employee::getName));
        
        System.out.println("\nSorted by department, salary (desc), name:");
        for (Employee e : employees) System.out.println("  " + e);
        System.out.println();
    }
    
    /**
     * Example 4: Verify stability of the sort
     */
    private static void demonstrateStability() {
        System.out.println("Example 4: Sort Stability Test");
        System.out.println("-------------------------------");
        
        Record[] records = {
            new Record(3, "First-3"),
            new Record(1, "First-1"),
            new Record(2, "First-2"),
            new Record(1, "Second-1"),
            new Record(3, "Second-3"),
            new Record(2, "Second-2"),
        };
        
        System.out.println("Original (with insertion order):");
        for (Record r : records) System.out.println("  " + r);
        
        FluxSort sorter = new FluxSort();
        sorter.sort(records, Comparator.comparingInt(r -> r.key));
        
        System.out.println("\nAfter sorting by key (should maintain relative order):");
        for (Record r : records) System.out.println("  " + r);
        
        // Verify stability
        boolean stable = true;
        if (records[0].label.equals("First-1") && records[1].label.equals("Second-1") &&
            records[2].label.equals("First-2") && records[3].label.equals("Second-2") &&
            records[4].label.equals("First-3") && records[5].label.equals("Second-3")) {
            System.out.println("\n✓ Sort is STABLE - relative order preserved!");
        } else {
            System.out.println("\n✗ Sort is UNSTABLE - relative order changed!");
            stable = false;
        }
        System.out.println();
    }
    
    // Helper classes
    
    static class Employee {
        String name;
        String department;
        int salary;
        
        Employee(String name, String department, int salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
        
        String getName() { return name; }
        String getDepartment() { return department; }
        int getSalary() { return salary; }
        
        @Override
        public String toString() {
            return String.format("%-10s %-12s $%,d", name, department, salary);
        }
    }
    
    static class Record {
        int key;
        String label;
        
        Record(int key, String label) {
            this.key = key;
            this.label = label;
        }
        
        @Override
        public String toString() {
            return String.format("Key: %d, Label: %s", key, label);
        }
    }
}
