package mst;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Prim {
    public static MSTResult run(Graph g) {
        long start = System.nanoTime();
        int n = g.getVertices();
        boolean[] inTree = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        List<Edge> result = new ArrayList<>();
        long total = 0, ops = 0;

        if (n > 0) {
            inTree[0] = true;
            for (Edge e : g.getNeighbors(0)) { pq.add(e); ops += 2; }
        }
        while (!pq.isEmpty() && result.size() < n - 1) {
            Edge e = pq.poll();
            ops++;
            if (inTree[e.to]) continue;
            inTree[e.to] = true;
            result.add(e);
            total += e.weight;
            for (Edge next : g.getNeighbors(e.to)) {
                ops++;
                if (!inTree[next.to]) { pq.add(next); ops++; }
            }
        }
        MSTResult r = new MSTResult();
        r.edges = result;
        r.totalWeight = total;
        r.connected = (n == 0) || result.size() == n - 1;
        r.operations = ops;
        r.timeNanos = System.nanoTime() - start;
        return r;
    }
}