package shortestpath;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class Dijkstra {
    public static final long INF = Long.MAX_VALUE;
    public long[] dist;
    public int[] prev;
    public long operations;

    public void run(Graph g, int source) {
        int n = g.getVertices();
        dist = new long[n];
        prev = new int[n];
        operations = 0;
        Arrays.fill(dist, INF);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new long[]{source, 0});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            operations++;
            int u = (int) cur[0];
            if (cur[1] > dist[u]) continue;
            for (Edge e : g.getNeighbors(u)) {
                operations++;
                if (e.weight < 0) throw new IllegalArgumentException("Negative weight");
                long nd = dist[u] + e.weight;
                if (nd < dist[e.to]) {
                    dist[e.to] = nd;
                    prev[e.to] = u;
                    pq.add(new long[]{e.to, nd});
                }
            }
        }
    }

    public List<Integer> path(int target) {
        List<Integer> p = new ArrayList<>();
        if (dist[target] == INF) return p;
        for (int v = target; v != -1; v = prev[v]) p.add(v);
        Collections.reverse(p);
        return p;
    }
}