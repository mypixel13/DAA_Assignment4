package daa;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

// Runs the whole experiment and writes results/results.csv
// Run: mvn compile exec:java
public class Experiment {

    static final int RUNS = 5;                 // median of 5 runs
    static final double RECURSIVE_LIMIT_MS = 3000; // stop growing n for recursion after this

    // sizes for edit distance
    static final int[] SMALL_SIZES = {2, 4, 6, 8, 10, 11, 12, 13, 14};
    static final int[] BIG_SIZES = {250, 500, 1000, 2000, 4000};

    // sizes for string search (length of the text), pattern length is always 100
    static final int[] TEXT_SIZES = {100_000, 200_000, 400_000, 800_000, 1_600_000};
    static final int PATTERN_LENGTH = 100;

    public static void main(String[] args) throws IOException {
        new File("results/plots").mkdirs();
        PrintWriter out = new PrintWriter("results/results.csv");
        out.println("family,algorithm,input,n,m,time_ms,operations,memory_cells,result");

        runEditDistance(out);
        runSearch(out);

        out.close();
        System.out.println("Done, see results/results.csv");
    }

    // ---------- edit distance ----------

    static void runEditDistance(PrintWriter out) {
        List<EditDistance> algos = Arrays.asList(
                new RecursiveEditDistance(),
                new TableEditDistance(),
                new TwoRowsEditDistance());

        String[] inputTypes = {"random", "identical", "disjoint"};

        for (String input : inputTypes) {
            boolean recursiveTooSlow = false;

            // small sizes first (all three versions), then big sizes (only the DP ones)
            int[][] sizeGroups = {SMALL_SIZES, BIG_SIZES};
            for (int[] sizes : sizeGroups) {
                for (int n : sizes) {
                    String[] pair = makePair(input, n);
                    String a = pair[0];
                    String b = pair[1];

                    for (EditDistance algo : algos) {
                        boolean isRecursive = algo.getName().equals("recursive");
                        if (isRecursive && (recursiveTooSlow || n > 20)) {
                            continue;
                        }

                        // one run to read the counters and the answer
                        algo.reset();
                        int answer = algo.distance(a, b);
                        long ops = algo.getOperations();
                        long mem = algo.getMemoryCells();

                        double time = measureEditDistance(algo, a, b);

                        out.println("edit_distance," + algo.getName() + "," + input + "," + n + "," + n + ","
                                + String.format("%.4f", time).replace(',', '.') + "," + ops + "," + mem + "," + answer);
                        System.out.println("ED " + algo.getName() + " " + input + " n=" + n + " " + time + " ms");

                        if (isRecursive && time > RECURSIVE_LIMIT_MS) {
                            recursiveTooSlow = true;
                        }
                    }
                }
            }
        }
        out.flush();
    }

    static String[] makePair(String input, int n) {
        InputGenerator gen = new InputGenerator(InputGenerator.SEED + n);
        if (input.equals("random")) {
            // small alphabet so that letters match sometimes
            return new String[]{gen.randomString(n, 4), gen.randomString(n, 4)};
        } else if (input.equals("identical")) {
            String s = gen.randomString(n, 4);
            return new String[]{s, s};
        } else {
            // no letter of a is in b, so the recursion never takes the "equal" shortcut
            return new String[]{InputGenerator.repeat('a', n), InputGenerator.repeat('b', n)};
        }
    }

    static double measureEditDistance(EditDistance algo, String a, String b) {
        double[] times = new double[RUNS];
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            algo.distance(a, b);
            long end = System.nanoTime();
            times[r] = (end - start) / 1_000_000.0;
        }
        return median(times);
    }

    // ---------- string search ----------

    static void runSearch(PrintWriter out) {
        List<StringSearch> algos = Arrays.asList(new NaiveSearch(), new KMPSearch());
        String[] inputTypes = {"random26", "random2", "worst_naive", "all_match"};

        for (String input : inputTypes) {
            for (int n : TEXT_SIZES) {
                String[] pair = makeTextAndPattern(input, n);
                String text = pair[0];
                String pattern = pair[1];

                for (StringSearch algo : algos) {
                    algo.reset();
                    int matches = algo.search(text, pattern).size();
                    long comparisons = algo.getComparisons();

                    double time = measureSearch(algo, text, pattern);

                    out.println("search," + algo.getName() + "," + input + "," + n + "," + pattern.length() + ","
                            + String.format("%.4f", time).replace(',', '.') + "," + comparisons + ",0," + matches);
                    System.out.println("SEARCH " + algo.getName() + " " + input + " n=" + n + " " + time + " ms");
                }
            }
        }
        out.flush();
    }

    static String[] makeTextAndPattern(String input, int n) {
        InputGenerator gen = new InputGenerator(InputGenerator.SEED + n);
        int m = PATTERN_LENGTH;
        if (input.equals("random26")) {
            return new String[]{gen.randomString(n, 26), gen.randomString(m, 26)};
        } else if (input.equals("random2")) {
            return new String[]{gen.randomString(n, 2), gen.randomString(m, 2)};
        } else if (input.equals("worst_naive")) {
            // text is aaaa...a and pattern is aaa...ab: naive goes almost to the end every time
            return new String[]{InputGenerator.repeat('a', n), InputGenerator.repeat('a', m - 1) + "b"};
        } else {
            // everything matches, a lot of overlapping matches
            return new String[]{InputGenerator.repeat('a', n), InputGenerator.repeat('a', m)};
        }
    }

    static double measureSearch(StringSearch algo, String text, String pattern) {
        double[] times = new double[RUNS];
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            algo.search(text, pattern);
            long end = System.nanoTime();
            times[r] = (end - start) / 1_000_000.0;
        }
        return median(times);
    }

    static double median(double[] values) {
        double[] copy = values.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }
}
