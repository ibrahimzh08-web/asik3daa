package connectivity;

import graph.Edge;
import graph.Graph;
import java.util.*;

public class SCC {
    public static long operations;
    private int[] ids, low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private int counter;
    private List<List<Integer>> components;
    private Graph g;

    public static List<List<Integer>> find(Graph graph) {
        SCC s = new SCC();
        operations = 0;
        int n = graph.getVertices();
        s.g = graph;
        s.ids = new int[n];
        s.low = new int[n];
        s.onStack = new boolean[n];
        s.stack = new ArrayDeque<>();
        s.components = new ArrayList<>();
        Arrays.fill(s.ids, -1);
        for (int i = 0; i < n; i++) if (s.ids[i] == -1) s.dfs(i);
        return s.components;
    }

    private void dfs(int u) {
        ids[u] = low[u] = counter++;
        stack.push(u);
        onStack[u] = true;
        for (Edge e : g.getNeighbors(u)) {
            operations++;
            int v = e.to;
            if (ids[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], ids[v]);
            }
        }
        if (low[u] == ids[u]) {
            List<Integer> comp = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                comp.add(v);
            } while (v != u);
            components.add(comp);
        }
    }
}