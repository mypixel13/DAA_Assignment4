package daa;

public class TwoRowsEditDistance implements EditDistance {

    private long cellsComputed = 0;
    private long memoryCells = 0;

    // DP design (C. two rows)
    // State: prev[j] = dp[i-1][j] and curr[j] = dp[i][j], so only the last two rows are kept.
    // Transition: same as the table, curr[j] depends on prev[j], curr[j-1] and prev[j-1].
    // Base case: row 0 is prev[j] = j, and every new row starts with curr[0] = i.
    // Time O(n*m), memory O(m).
    @Override
    public int distance(String a, String b) {
        int n = a.length();
        int m = b.length();

        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        memoryCells += 2L * (m + 1);

        for (int j = 0; j <= m; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                cellsComputed++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    int best = Math.min(prev[j], curr[j - 1]);
                    best = Math.min(best, prev[j - 1]);
                    curr[j] = 1 + best;
                }
            }
            // the current row becomes the previous one
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        // after the last swap the answer is in prev
        // (if a is empty the loop did not run and prev is still row 0, so prev[m] = m. also correct)
        return prev[m];
    }

    @Override
    public long getOperations() {
        return cellsComputed;
    }

    @Override
    public long getMemoryCells() {
        return memoryCells;
    }

    @Override
    public void reset() {
        cellsComputed = 0;
        memoryCells = 0;
    }

    @Override
    public String getName() {
        return "two_rows";
    }
}
