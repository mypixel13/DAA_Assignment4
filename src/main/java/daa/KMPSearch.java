package daa;

import java.util.ArrayList;
import java.util.List;

public class KMPSearch implements StringSearch {

    // I count the comparisons in lps() too, because building the table is also part of KMP
    private long comparisons = 0;

    // lps[i] = length of the longest proper prefix of p[0..i] which is also a suffix of p[0..i]
    public int[] lps(String p) {
        int m = p.length();
        int[] lps = new int[m];
        if (m == 0) {
            return lps;
        }

        int len = 0; // length of the current matched prefix
        int i = 1;
        while (i < m) {
            comparisons++;
            if (p.charAt(i) == p.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else if (len > 0) {
                // try a shorter prefix, do not move i
                len = lps[len - 1];
            } else {
                lps[i] = 0;
                i++;
            }
        }
        return lps;
    }

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

        int[] table = lps(pattern);

        int i = 0; // position in the text, it never goes back
        int j = 0; // how many chars of the pattern are matched now
        while (i < n) {
            comparisons++;
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == m) {
                    result.add(i - m);
                    // keep going, the next match can overlap with this one
                    j = table[j - 1];
                }
            } else if (j > 0) {
                j = table[j - 1];
            } else {
                i++;
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
        return "kmp";
    }
}
