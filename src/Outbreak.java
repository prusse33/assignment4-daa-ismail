import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n];
        int size = 0;
        int[] stack = new int[n];
        int top = 0;
        stack[top++] = 0;
        parent[0] = 0;
        while (top > 0) {
            int v = stack[--top];
            order[size++] = v;
            for (int u : nb[v]) if (u != parent[v]) {
                parent[u] = v;
                stack[top++] = u;
            }
        }

        long[] yes = new long[n]; // minimum cost if this person is tested
        long[] no = new long[n];  // minimum cost if this person is not tested
        for (int i = n - 1; i >= 0; i--) {
            int v = order[i];
            yes[v] = cost[v];
            no[v] = 0;
            for (int u : nb[v]) if (parent[u] == v) {
                yes[v] += Math.min(yes[u], no[u]);
                no[v] += yes[u];
            }
        }

        boolean[] tested = new boolean[n];
        stack[top++] = 0;
        tested[0] = yes[0] <= no[0];
        while (top > 0) {
            int v = stack[--top];
            for (int u : nb[v]) if (parent[u] == v) {
                tested[u] = !tested[v] || yes[u] <= no[u];
                stack[top++] = u;
            }
        }
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n <= 1) return 0;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        int[] order = new int[n], stack = new int[n];
        int top = 0, size = 0;
        stack[top++] = 0;
        parent[0] = 0;
        while (top > 0) {
            int v = stack[--top];
            order[size++] = v;
            for (int u : nb[v]) if (u != parent[v]) {
                parent[u] = v;
                stack[top++] = u;
            }
        }
        int[] down = new int[n];
        int best = 0;
        for (int i = size - 1; i >= 0; i--) {
            int v = order[i];
            int first = 0, second = 0;
            for (int u : nb[v]) if (parent[u] == v) {
                int length = down[u] + 1;
                if (length > first) { second = first; first = length; }
                else if (length > second) second = length;
            }
            down[v] = first;
            best = Math.max(best, first + second);
        }
        return best;
    }
}
