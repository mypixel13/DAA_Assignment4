package daa;

import java.util.ArrayList;
import java.util.List;

public class NaiveSearch implements StringSearch {

    private long comparisons = 0;

    @Override
    public List<Integer> search(String text, String pattern) {
        if (pattern.isEmpty()) {
            throw new IllegalArgumentException("pattern must not be empty");
        }

        List<Integer> result = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        if (m > n) {
            return result;
        }

        for (int start = 0; start <= n - m; start++) {
            int k = 0;
            while (k < m) {
                comparisons++;
                if (text.charAt(start + k) != pattern.charAt(k)) {
                    break;
                }
                k++;
            }
            if (k == m) {
                result.add(start);
            }
        }

        return result;
    }

    @Override
    public long getComparisons() {
        return comparisons;
    }

    @Override
    public void reset() {
        comparisons = 0;
    }

    @Override
    public String getName() {
        return "naive";
    }
}
