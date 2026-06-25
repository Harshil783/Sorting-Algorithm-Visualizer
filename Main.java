
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        SortingVisualizer viz = new SortingVisualizer();

        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║    Java Sorting Algorithm Visualizer  ║");
        System.out.println("╚══════════════════════════════════════╝");

        while (true) {
            System.out.println("\nSelect mode:");
            System.out.println("  1. Visualize a single algorithm (step-by-step)");
            System.out.println("  2. Benchmark all algorithms");
            System.out.println("  3. Exit");
            System.out.print("\nChoice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> viz.runVisualizeMode(scanner);
                case "2" -> viz.runBenchmarkMode(scanner);
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }
}
