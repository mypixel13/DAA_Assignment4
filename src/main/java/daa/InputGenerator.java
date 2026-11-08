package daa;

import java.util.Random;

// My own generator. Same seed -> same strings, so the experiment is reproducible.
public class InputGenerator {

    public static final long SEED = 2026;

    private final Random random;

    public InputGenerator(long seed) {
        this.random = new Random(seed);
    }

    // random string, letters 'a', 'b', 'c'... (alphabetSize different letters)
    public String randomString(int length, int alphabetSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('a' + random.nextInt(alphabetSize)));
        }
        return sb.toString();
    }

    // the same letter many times, like "aaaaa"
    public static String repeat(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
