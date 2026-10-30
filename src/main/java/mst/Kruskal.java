package mst;

import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.*;

public class Kruskal {
    public static long lastComparisons, lastDsuOps;

    public static MSTResult run(Graph g) {
        long start = System.nanoTime();
        int n = g.getVertices();
        List<Edge> sorted = new ArrayList<>(g.getEdges());
        long[] cmp = {0};
        sorted.sort((a, b) -> { cmp[0]++; return Integer.compare(a.weight, b.weight); });

        DSU dsu = new DSU(n);
        List<Edge> result = new ArrayList<>();
        long total = 0;
        for (Edge e : sorted) {
            if (dsu.union(e.from, e.to)) {
                result.add(e);
                total += e.weight;
                if (result.size() == n - 1) break;
            }
        }
        MSTResult r = new MSTResult();
        r.edges = result;
        r.totalWeight = total;
        r.connected = (n == 0) || result.size() == n - 1;
        lastComparisons = cmp[0];
        lastDsuOps = dsu.findCalls + dsu.unionCalls;
        r.operations = lastComparisons + lastDsuOps;
        r.timeNanos = System.nanoTime() - start;
        return r;
    }
}