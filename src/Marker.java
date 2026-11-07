import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int[] pi = new int[p.length()];
        for (int i = 1; i < p.length(); i++) {
            int j = pi[i - 1];
            while (j > 0 && p.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (p.charAt(i) == p.charAt(j)) j++;
            pi[i] = j;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) return new int[0];
        int[] pi = prefixFunction(p);
        ArrayList<Integer> positions = new ArrayList<>();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != p.charAt(j)) j = pi[j - 1];
            if (text.charAt(i) == p.charAt(j)) j++;
            if (j == p.length()) {
                positions.add(i - p.length() + 1);
                j = pi[j - 1];
            }
        }
        int[] result = new int[positions.size()];
        for (int i = 0; i < result.length; i++) result[i] = positions.get(i);
        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || k > a.length() || k > b.length()) return 0;
        final long mod = 1_000_000_007L;
        final long base = 5L;
        long power = 1;
        for (int i = 1; i < k; i++) power = (power * base) % mod;

        Map<Long, List<Integer>> windowsInB = new HashMap<>();
        long hash = 0;
        for (int i = 0; i < k; i++) hash = (hash * base + value(b.charAt(i))) % mod;
        windowsInB.computeIfAbsent(hash, x -> new ArrayList<>()).add(0);
        for (int start = 1; start + k <= b.length(); start++) {
            hash = (hash - value(b.charAt(start - 1)) * power % mod + mod) % mod;
            hash = (hash * base + value(b.charAt(start + k - 1))) % mod;
            windowsInB.computeIfAbsent(hash, x -> new ArrayList<>()).add(start);
        }

        int count = 0;
        hash = 0;
        for (int i = 0; i < k; i++) hash = (hash * base + value(a.charAt(i))) % mod;
        for (int start = 0; start + k <= a.length(); start++) {
            List<Integer> candidates = windowsInB.get(hash);
            if (candidates != null) {
                boolean equal = false;
                for (int other : candidates) {
                    if (a.regionMatches(start, b, other, k)) { equal = true; break; }
                }
                if (equal) count++;
            }
            if (start + k < a.length()) {
                hash = (hash - value(a.charAt(start)) * power % mod + mod) % mod;
                hash = (hash * base + value(a.charAt(start + k))) % mod;
            }
        }
        return count;
    }
}

    private static int value(char c) {
        return switch (c) { case 'A' -> 1; case 'C' -> 2; case 'G' -> 3; case 'T' -> 4; default -> c; };
    }
}
