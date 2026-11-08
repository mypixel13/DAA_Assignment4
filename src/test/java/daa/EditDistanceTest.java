package daa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import org.junit.jupiter.api.Test;

public class EditDistanceTest {

    private final EditDistance[] versions = {
            new RecursiveEditDistance(),
            new TableEditDistance(),
            new TwoRowsEditDistance()
    };

    @Test
    void referenceValues() {
        for (EditDistance v : versions) {
            assertEquals(3, v.distance("kitten", "sitting"), v.getName());
            assertEquals(2, v.distance("flaw", "lawn"), v.getName());
            assertEquals(5, v.distance("intention", "execution"), v.getName());
            assertEquals(3, v.distance("", "abc"), v.getName());
            assertEquals(0, v.distance("abc", "abc"), v.getName());
        }
    }

    @Test
    void emptyStrings() {
        for (EditDistance v : versions) {
            assertEquals(0, v.distance("", ""), v.getName());
            assertEquals(4, v.distance("abcd", ""), v.getName());
            assertEquals(4, v.distance("", "abcd"), v.getName());
        }
    }

    @Test
    void allVersionsGiveTheSameAnswerOnRandomStrings() {
        Random random = new Random(42);
        for (int t = 0; t < 100; t++) {
            String a = randomString(random, random.nextInt(8));
            String b = randomString(random, random.nextInt(8));

            int expected = versions[0].distance(a, b);
            assertEquals(expected, versions[1].distance(a, b), a + " / " + b);
            assertEquals(expected, versions[2].distance(a, b), a + " / " + b);
        }
    }

    private String randomString(Random random, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(3)));
        }
        return sb.toString();
    }
}
