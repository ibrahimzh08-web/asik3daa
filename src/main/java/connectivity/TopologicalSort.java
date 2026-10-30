package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class TopologicalSort {
    public static long operations;

    // Возвращает порядок вершин или null, если в графе есть цикл
    public static List<Integer> sort(Graph g) {
        operations = 0;
        int n = g.getVertices();
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++)
            for (Edge e : g.getNeighbors(u)) { indeg[e.to]++; operations++; }

        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) queue.add(i);

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (Edge e : g.getNeighbors(u)) {
                operations++;
                if (--indeg[e.to] == 0) queue.add(e.to);
            }
        }
        return order.size() == n ? order : null;
    }
}