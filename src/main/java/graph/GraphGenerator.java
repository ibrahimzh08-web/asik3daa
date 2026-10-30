package graph;

import java.util.*;

public class GraphGenerator {

    private static long key(int u, int v, int n) {
        return (long) Math.min(u, v) * n + Math.max(u, v);
    }

    public static Graph connectedUndirected(int n, int m, long seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(n, false);
        Set<Long> used = new HashSet<>();
        for (int i = 1; i < n; i++) {
            int j = rnd.nextInt(i);
            g.addEdge(i, j, 1 + rnd.nextInt(100));
            used.add(key(i, j, n));
        }
        while (g.getEdgeCount() < m) {
            int u = rnd.nextInt(n), v = rnd.nextInt(n);
            if (u == v || !used.add(key(u, v, n))) continue;
            g.addEdge(u, v, 1 + rnd.nextInt(100));
        }
        return g;
    }

    public static Graph directedDAG(int n, int m, long seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();
        while (g.getEdgeCount() < m) {
            int u = rnd.nextInt(n), v = rnd.nextInt(n);
            if (u >= v || !used.add((long) u * n + v)) continue;
            g.addEdge(u, v, 1 + rnd.nextInt(100));
        }
        return g;
    }

    public static Graph directedWithCycles(int n, int m, long seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(n, true);
        Set<Long> used = new HashSet<>();
        while (g.getEdgeCount() < m) {
            int u = rnd.nextInt(n), v = rnd.nextInt(n);
            if (u == v || !used.add((long) u * n + v)) continue;
            g.addEdge(u, v, 1 + rnd.nextInt(100));
        }
        return g;
    }
}