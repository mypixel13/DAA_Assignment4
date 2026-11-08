package daa;

// One interface for all three versions, so the experiment can use one loop.
public interface EditDistance {

    int distance(String a, String b);

    // how much "work" was done since the last reset()
    // (recursive: number of calls, table / two rows: number of cells computed)
    long getOperations();

    // how many int cells were allocated for the table(s)
    long getMemoryCells();

    void reset();

    String getName();
}
