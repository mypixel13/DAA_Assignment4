package daa;

import java.util.List;

public interface StringSearch {

    // start indices of all occurrences (overlapping too), in increasing order
    List<Integer> search(String text, String pattern);

    // number of character comparisons since the last reset()
    long getComparisons();

    void reset();

    String getName();
}
