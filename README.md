# 🔢 Java Sorting Algorithm Visualizer

A terminal-based sorting algorithm visualizer with real-time ASCII bar chart animation and benchmarking.

## Algorithms Included
| Algorithm      | Avg Case     | Worst Case   |
|----------------|--------------|--------------|
| Bubble Sort    | O(n²)        | O(n²)        |
| Selection Sort | O(n²)        | O(n²)        |
| Insertion Sort | O(n²)        | O(n²)        |
| Merge Sort     | O(n log n)   | O(n log n)   |
| Quick Sort     | O(n log n)   | O(n²)        |
| Heap Sort      | O(n log n)   | O(n log n)   |

## Requirements
- Java 17+

## Build & Run

### javac
```bash
javac Main.java SortingVisualizer.java
java Main
```

## Modes

### 1. Visualize (step-by-step)
- Choose an algorithm, array size (5–40), and step delay
- Watch the bars move in real time — highlighted bars (█) show active comparisons
- Final stats: comparisons, swaps, elapsed time

### 2. Benchmark All
- Runs all 6 algorithms on the same random array
- Prints a comparison table of comparisons, swaps, and time

## Example Benchmark Output
```
Algorithm          Comparisons        Swaps    Time (ms)
──────────────────────────────────────────────────────────
Bubble Sort              4,950        2,431            2
Selection Sort           4,950          100            1
Insertion Sort           2,543        2,543            0
Merge Sort               4,344        3,976            1
Quick Sort               1,312          412            0
Heap Sort                2,988        1,024            0
──────────────────────────────────────────────────────────
```

## Project Structure
```
sorting-visualizer/
└── sortviz/
    ├── Main.java               # Entry point & menu
    └── SortingVisualizer.java  # Algorithms + rendering
```

## Potential Features
- Add **Shell Sort** or **Tim Sort**
- Export benchmark results to CSV
- Add a **nearly-sorted** or **reverse-sorted** input mode to show worst-case behavior
- Swap ASCII bars for a Swing/JavaFX GUI
