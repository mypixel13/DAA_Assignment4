package daa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

public class SearchTest {

    private final StringSearch[] methods = {new NaiveSearch(), new KMPSearch()};

    @Test
    void referenceMatches() {
        for (StringSearch s : methods) {
            assertEquals(List.of(10), s.search("ABABDABACDABABCABAB", "ABABCABAB"), s.getName());
            assertEquals(List.of(0, 1, 2, 3), s.search("AAAAA", "AA"), s.getName());
            assertEquals(List.of(0, 2, 4), s.search("ABABABA", "ABA"), s.getName());
        }
    }

    @Test
    void noMatchGivesEmptyList() {
        for (StringSearch s : methods) {
            assertTrue(s.search("ABCDEF", "XYZ").isEmpty(), s.getName());
        }
    }

    @Test
    void emptyPatternThrows() {
        for (StringSearch s : methods) {
            assertThrows(IllegalArgumentException.class, () -> s.search("abc", ""), s.getName());
        }
    }

    @Test
    void patternLongerThanTextGivesEmptyList() {
        for (StringSearch s : methods) {
            assertTrue(s.search("abc", "abcd").isEmpty(), s.getName());
            assertTrue(s.search("", "a").isEmpty(), s.getName());
        }
    }

    @Test
    void naiveAndKmpAgreeOnRandomTexts() {
        Random random = new Random(7);
        NaiveSearch naive = new NaiveSearch();
        KMPSearch kmp = new KMPSearch();

        for (int t = 0; t < 100; t++) {
            String text = randomString(random, random.nextInt(60), 2 + random.nextInt(2));
            String pattern = randomString(random, 1 + random.nextInt(5), 2 + random.nextInt(2));

            assertEquals(naive.search(text, pattern), kmp.search(text, pattern), text + " / " + pattern);
        }
    }

    private String randomString(Random random, int length, int alphabet) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(alphabet)));
        }
        return sb.toString();
    }
}
