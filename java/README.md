# FluxSort Java Implementation

This is a Java translation of the FluxSort sorting algorithm, originally implemented in C by Igor van den Hoven.

## About FluxSort

FluxSort is a stable quicksort/mergesort hybrid with exceptional performance. It is:
- **Stable**: Maintains the relative order of equal elements
- **Adaptive**: Performs well on partially sorted data
- **Branchless**: Uses branchless optimizations for better performance
- **Efficient**: O(n log n) worst-case time complexity with intelligent pivot selection

## Project Structure

```
java/
└── src/
    └── main/
        └── java/
            └── com/
                └── fluxsort/
                    ├── FluxSort.java          # Main FluxSort implementation
                    ├── QuadSort.java          # QuadSort (dependency)
                    ├── FluxSortDemo.java      # Demo and test program
                    ├── FluxSortBenchmark.java # Performance benchmark vs Arrays.sort
                    └── AdvancedExamples.java  # Advanced usage examples
```

## Features

- **Integer Sorting**: Optimized sorting for primitive integer arrays
- **Generic Sorting**: Support for any object type with custom Comparators
- **Adaptive Analysis**: Automatically detects sorted/partially sorted data
- **Small Array Optimization**: Uses QuadSort for small arrays (< 96 elements)
- **Intelligent Pivoting**: Median-of-9 for smaller arrays, cubic root median for larger ones

## Usage

### Sorting Integers

```java
import com.fluxsort.FluxSort;

int[] array = {64, 34, 25, 12, 22, 11, 90};
FluxSort sorter = new FluxSort();
sorter.sort(array);
```

### Sorting with Comparator

```java
import com.fluxsort.FluxSort;
import java.util.Comparator;

String[] array = {"banana", "apple", "cherry"};
FluxSort sorter = new FluxSort();
sorter.sort(array, Comparator.naturalOrder());
```

### Sorting Custom Objects

```java
import com.fluxsort.FluxSort;
import java.util.Comparator;

Person[] people = {...};
FluxSort sorter = new FluxSort();
sorter.sort(people, Comparator.comparingInt(p -> p.age));
```

## Compilation

Compile the Java files:

```bash
cd java/src/main/java
javac com/fluxsort/*.java
```

## Running the Demo

Run the demo program to see FluxSort in action:

```bash
cd java/src/main/java
java com.fluxsort.FluxSortDemo
```

Expected output:
```
FluxSort Java Implementation Demo
==================================

Test 1: Sorting Integers
-------------------------
Original array: [64, 34, 25, 12, 22, 11, 90, 88, 45, 50, 23, 36, 18, 77]
Sorted array:   [11, 12, 18, 22, 23, 25, 34, 36, 45, 50, 64, 77, 88, 90]
Verification:   PASS

Test 2: Sorting Strings
-----------------------
Original array: [banana, apple, cherry, date, elderberry, fig, grape, kiwi, lemon, mango]
Sorted array:   [apple, banana, cherry, date, elderberry, fig, grape, kiwi, lemon, mango]
Verification:   PASS

Test 3: Sorting Custom Objects (Person by age)
-----------------------------------------------
...

All tests completed successfully!
```

## Running the Benchmark

Run the benchmark to compare FluxSort performance with Java's Arrays.sort:

```bash
cd java/src/main/java
java com.fluxsort.FluxSortBenchmark
```

The benchmark tests various scenarios:
- **Random data**: Completely random integers
- **Already sorted**: Pre-sorted data (best case)
- **Reverse sorted**: Reverse-order data
- **Partially sorted**: 80% sorted data with 20% shuffled
- **Many duplicates**: Low cardinality data (100 unique values)

Each test runs across multiple array sizes (1K, 10K, 100K, 1M elements) and reports the average time in milliseconds along with the speedup factor compared to Arrays.sort.

## Key Differences from C Implementation

1. **Memory Management**: Java's automatic garbage collection replaces manual malloc/free
2. **Array Handling**: Java arrays are objects with bounds checking
3. **Generics**: Java's type system allows for type-safe generic implementations
4. **Comparators**: Uses Java's `Comparator` interface instead of function pointers
5. **Simplified**: Some low-level optimizations specific to C/gcc have been adapted for Java

## Algorithm Overview

FluxSort combines several techniques:

1. **Analyzer**: Detects presorted data and splits array into 4 segments
2. **Adaptive Switching**: Switches to QuadSort for sorted segments
3. **Partitioning**: Top-down quicksort-style partitioning with intelligent pivot selection
4. **Worst Case Handling**: Switches to QuadSort when partitions become imbalanced
5. **Merging**: Uses QuadSort's merging for combining sorted segments

## Performance

FluxSort provides excellent performance across different data distributions:
- Random data: O(n log n) with low constant factors
- Sorted data: O(n) detection and early exit
- Partially sorted: Adaptive behavior leverages existing order
- Generic data: Handles duplicates and low cardinality efficiently

## Credits

- **Original C Implementation**: Igor van den Hoven (ivdhoven@gmail.com)
- **Java Translation**: Translated from the C version available at https://github.com/scandum/fluxsort

## License

This implementation follows the same license as the original C implementation. See the LICENSE file in the repository root.

## References

- Original FluxSort: https://github.com/scandum/fluxsort
- QuadSort: https://github.com/scandum/quadsort
