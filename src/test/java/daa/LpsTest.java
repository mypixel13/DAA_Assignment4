package daa;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class LpsTest {

    private final KMPSearch kmp = new KMPSearch();

    @Test
    void firstReferenceArray() {
        assertArrayEquals(new int[]{0, 1, 2, 0, 1, 2, 3, 3}, kmp.lps("AAACAAAA"));
    }

    @Test
    void secondReferenceArray() {
        assertArrayEquals(new int[]{0, 0, 1, 2, 0, 1, 2, 3, 4}, kmp.lps("ABABCABAB"));
    }

    @Test
    void oneLetterAndNoRepeats() {
        assertArrayEquals(new int[]{0}, kmp.lps("A"));
        assertArrayEquals(new int[]{0, 0, 0}, kmp.lps("ABC"));
    }
}
