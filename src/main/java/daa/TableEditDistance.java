package daa;

public class TableEditDistance implements EditDistance {

    private long cellsComputed = 0;
    private long memoryCells = 0;

    // DP design (B. table)
    // State: dp[i][j] = edit distance between the first i chars of a and the first j chars of b.
    // Transition: same as in the recursive version, but we fill the table row by row,
    //             so dp[i-1][j], dp[i][j-1] and dp[i-1][j-1] are already known.
    // Base case: dp[i][0] = i (first column) and dp[0][j] = j (first row).
    // Time O(n*m), memory O(n*m).
    @Override
    public int distance(String a, String b) {
        int n = a.length();
        int m = b.length();

        int[][] dp = new int[n + 1][m + 1];
        memoryCells += (long) (n + 1) * (m + 1);

        for (int i = 0; i <= n; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= m; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                cellsComputed++;
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int best = Math.min(dp[i - 1][j], dp[i][j - 1]);
                    best = Math.min(best, dp[i - 1][j - 1]);
                    dp[i][j] = 1 + best;
                }
            }
        }

        return dp[n][m];
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
        return "table";
    }
}
