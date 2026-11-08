package daa;

public class RecursiveEditDistance implements EditDistance {

    private long calls = 0;

    // DP design (A. recursive)
    // State: f(i, j) = edit distance between the first i chars of a and the first j chars of b.
    // Transition: if a[i-1] == b[j-1] then f(i, j) = f(i-1, j-1),
    //             else f(i, j) = 1 + min(f(i-1, j) delete, f(i, j-1) insert, f(i-1, j-1) replace).
    // Base case: f(0, j) = j and f(i, 0) = i.
    // No memory, so the same f(i, j) is calculated again and again.
    @Override
    public int distance(String a, String b) {
        return calc(a, b, a.length(), b.length());
    }

    private int calc(String a, String b, int i, int j) {
        calls++; // every call is one unit of work

        if (i == 0) {
            return j;
        }
        if (j == 0) {
            return i;
        }

        if (a.charAt(i - 1) == b.charAt(j - 1)) {
            return calc(a, b, i - 1, j - 1);
        }

        int delete = calc(a, b, i - 1, j);
        int insert = calc(a, b, i, j - 1);
        int replace = calc(a, b, i - 1, j - 1);
        return 1 + Math.min(delete, Math.min(insert, replace));
    }

    @Override
    public long getOperations() {
        return calls;
    }

    @Override
    public long getMemoryCells() {
        return 0; // no table at all (only the call stack)
    }

    @Override
    public void reset() {
        calls = 0;
    }

    @Override
    public String getName() {
        return "recursive";
    }
}
