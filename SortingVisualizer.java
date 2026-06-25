
import java.util.*;

public class SortingVisualizer {

    private static final int BAR_HEIGHT = 20;
    private static final String[] ALGORITHM_NAMES = {
        "Bubble Sort", "Selection Sort", "Insertion Sort",
        "Merge Sort",  "Quick Sort",    "Heap Sort"
    };

    // ─────────────────────────────────────────────
    //  PUBLIC MODES
    // ─────────────────────────────────────────────

    // "throws InterruptedException" might be quite confusing
    // here's the situation, we create animation effect using thread.sleep(delay)
    // but while sleeping anything could "FORCEFULLY"(interrupting the thread) wake this thread up
    // at the waking we throw InterruptedException which would throw to main program
    // ultimately crashing program
    // java forces two options either you handle it using try/catch or just throw
    // if you throw then whoever called this function would have to handle exception
    public void runVisualizeMode(Scanner scanner) throws InterruptedException {
        System.out.println("\nChoose algorithm:");
        for (int i = 0; i < ALGORITHM_NAMES.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, ALGORITHM_NAMES[i]);
        }
        System.out.print("Choice: ");
        int algoIdx = readInt(scanner, 1, ALGORITHM_NAMES.length) - 1;

        System.out.print("Array size (5–40): ");
        int size = readInt(scanner, 5, 40);

        System.out.print("Delay between steps in ms (0–500): ");
        int delay = readInt(scanner, 0, 500);

        int[] array = generateRandom(size);

        System.out.println("\nInitial array:");
        printBars(array, -1, -1);
        Thread.sleep(800);

        SortResult result = sort(algoIdx, array, delay);

        System.out.printf("%n✔  %s complete!  Comparisons: %,d  |  Swaps: %,d  |  Time: %d ms%n",
            ALGORITHM_NAMES[algoIdx], result.comparisons, result.swaps, result.elapsedMs);
    }

    public void runBenchmarkMode(Scanner scanner) throws InterruptedException {
        System.out.print("\nArray size for benchmark (10–5000): ");
        int size = readInt(scanner, 10, 5000);

        System.out.println("\nRunning benchmarks on " + size + " elements...\n");
        System.out.printf("%-18s %12s %12s %12s%n", "Algorithm", "Comparisons", "Swaps", "Time (ms)");
        System.out.println("─".repeat(58));

        int[] original = generateRandom(size);

        for (int i = 0; i < ALGORITHM_NAMES.length; i++) {
            int[] copy = Arrays.copyOf(original, original.length);
            SortResult r = sort(i, copy, 0);
            System.out.printf("%-18s %12s %12s %12s%n",
                ALGORITHM_NAMES[i],
                String.format("%,d", r.comparisons),
                String.format("%,d", r.swaps),
                r.elapsedMs);
        }
        System.out.println("─".repeat(58));
        System.out.println("\nBenchmark complete!");
    }

    // ─────────────────────────────────────────────
    //  SORT DISPATCHER
    // ─────────────────────────────────────────────

    private SortResult sort(int algoIdx, int[] arr, int delay) throws InterruptedException {
        long[] stats = {0, 0}; // [comparisons, swaps]
        long start = System.currentTimeMillis();

        switch (algoIdx) {
            case 0 -> bubbleSort(arr, stats, delay);
            case 1 -> selectionSort(arr, stats, delay);
            case 2 -> insertionSort(arr, stats, delay);
            case 3 -> mergeSort(arr, 0, arr.length - 1, stats, delay);
            case 4 -> quickSort(arr, 0, arr.length - 1, stats, delay);
            case 5 -> heapSort(arr, stats, delay);
        }

        return new SortResult(stats[0], stats[1], System.currentTimeMillis() - start);
    }

    // ─────────────────────────────────────────────
    //  ALGORITHMS
    // ─────────────────────────────────────────────

    private void bubbleSort(int[] arr, long[] stats, int delay) throws InterruptedException {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                stats[0]++;
                if (arr[j] > arr[j + 1]) {
                    swap(arr, j, j + 1, stats);
                    renderStep(arr, j, j + 1, delay);
                }
            }
        }
        if (delay > 0) printBars(arr, -1, -1);
    }

    private void selectionSort(int[] arr, long[] stats, int delay) throws InterruptedException {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                stats[0]++;
                if (arr[j] < arr[minIdx]) minIdx = j;
            }
            if (minIdx != i) {
                swap(arr, i, minIdx, stats);
                renderStep(arr, i, minIdx, delay);
            }
        }
        if (delay > 0) printBars(arr, -1, -1);
    }

    private void insertionSort(int[] arr, long[] stats, int delay) throws InterruptedException {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                stats[0]++;
                arr[j + 1] = arr[j];
                stats[1]++;
                j--;
                renderStep(arr, j + 1, i, delay);
            }
            arr[j + 1] = key;
        }
        if (delay > 0) printBars(arr, -1, -1);
    }

    private void mergeSort(int[] arr, int l, int r, long[] stats, int delay) throws InterruptedException {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m, stats, delay);
            mergeSort(arr, m + 1, r, stats, delay);
            merge(arr, l, m, r, stats, delay);
        }
    }

    private void merge(int[] arr, int l, int m, int r, long[] stats, int delay) throws InterruptedException {
        int[] left  = Arrays.copyOfRange(arr, l, m + 1);
        int[] right = Arrays.copyOfRange(arr, m + 1, r + 1);
        int i = 0, j = 0, k = l;
        while (i < left.length && j < right.length) {
            stats[0]++;
            if (left[i] <= right[j]) arr[k++] = left[i++];
            else                      arr[k++] = right[j++];
            stats[1]++;
            renderStep(arr, k - 1, -1, delay);
        }
        while (i < left.length)  { arr[k++] = left[i++];  stats[1]++; }
        while (j < right.length) { arr[k++] = right[j++]; stats[1]++; }
    }

    private void quickSort(int[] arr, int low, int high, long[] stats, int delay) throws InterruptedException {
        if (low < high) {
            int pi = partition(arr, low, high, stats, delay);
            quickSort(arr, low, pi - 1, stats, delay);
            quickSort(arr, pi + 1, high, stats, delay);
        }
    }

    private int partition(int[] arr, int low, int high, long[] stats, int delay) throws InterruptedException {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            stats[0]++;
            if (arr[j] < pivot) {
                i++;
                swap(arr, i, j, stats);
                renderStep(arr, i, j, delay);
            }
        }
        swap(arr, i + 1, high, stats);
        renderStep(arr, i + 1, high, delay);
        return i + 1;
    }

    private void heapSort(int[] arr, long[] stats, int delay) throws InterruptedException {
        int n = arr.length;
        for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i, stats, delay);
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i, stats);
            renderStep(arr, 0, i, delay);
            heapify(arr, i, 0, stats, delay);
        }
        if (delay > 0) printBars(arr, -1, -1);
    }

    private void heapify(int[] arr, int n, int i, long[] stats, int delay) throws InterruptedException {
        int largest = i, l = 2 * i + 1, r = 2 * i + 2;
        if (l < n) { stats[0]++; if (arr[l] > arr[largest]) largest = l; }
        if (r < n) { stats[0]++; if (arr[r] > arr[largest]) largest = r; }
        if (largest != i) {
            swap(arr, i, largest, stats);
            renderStep(arr, i, largest, delay);
            heapify(arr, n, largest, stats, delay);
        }
    }

    // ─────────────────────────────────────────────
    //  RENDERING
    // ─────────────────────────────────────────────

    private void renderStep(int[] arr, int hi1, int hi2, int delay) throws InterruptedException {
        if (delay > 0) {
            clearScreen();
            printBars(arr, hi1, hi2);
            Thread.sleep(delay);
        }
    }

    private void printBars(int[] arr, int hi1, int hi2) {
        int max = Arrays.stream(arr).max().orElse(1);
        StringBuilder sb = new StringBuilder();

        for (int row = BAR_HEIGHT; row >= 1; row--) {
            for (int i = 0; i < arr.length; i++) {
                int barHeight = (int) Math.round((double) arr[i] / max * BAR_HEIGHT);
                if (barHeight >= row) {
                    if (i == hi1 || i == hi2) sb.append("█");  // █ this is custom btw, hehe! took me a while to find this
                    else                       sb.append("▒"); // ▒ this is custom btw, hehe! took me a while to find this
                } else {
                    sb.append(" ");
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        // index ruler
        for (int i = 0; i < arr.length; i++) sb.append("─ ");
        sb.append("\n");

        System.out.print(sb);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J"); // this actually creates animation effect, ansi code for wiping, haha! neat little trick
        System.out.flush();
    }

    // ─────────────────────────────────────────────
    //  HELPERS
    // ─────────────────────────────────────────────

    private void swap(int[] arr, int i, int j, long[] stats) {
        int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        stats[1]++;
    }

    private int[] generateRandom(int size) {
        Random rng = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = rng.nextInt(100) + 1;
        return arr;
    }

    private int readInt(Scanner sc, int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
            } catch (NumberFormatException ignored) {}
            System.out.printf("Please enter a number between %d and %d: ", min, max);
        }
    }

    // ─────────────────────────────────────────────
    //  RESULT RECORD
    // ─────────────────────────────────────────────

    record SortResult(long comparisons, long swaps, long elapsedMs) {}
}
