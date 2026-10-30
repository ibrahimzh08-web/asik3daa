package experiments;

import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Edge;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

import java.util.*;

public class Tests {
    static int passed = 0, failed = 0;

    static void check(String name, boolean ok) {
        if (ok) passed++; else failed++;
        System.out.println((ok ? "PASS " : "FAIL ") + name);
    }

    static boolean validTopo(Graph g, List<Integer> order) {
        if (order == null || order.size() != g.getVertices()) return false;
        int[] pos = new int[g.getVertices()];
        for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;
        for (Edge e : g.getEdges()) if (pos[e.from] >= pos[e.to]) return false;
        return true;
    }

    static boolean validScc(Graph g, List<List<Integer>> comps) {
        boolean[] seen = new boolean[g.getVertices()];
        int total = 0;
        for (List<Integer> c : comps)
            for (int v : c) {
                if (seen[v]) return false;
                seen[v] = true;
                total++;
            }
        return total == g.getVertices();
    }

    public static void main(String[] args) {

        Graph small = new Graph(4, false);
        small.addEdge(0, 1, 1);
        small.addEdge(1, 2, 2);
        small.addEdge(2, 3, 3);
        small.addEdge(0, 3, 4);
        small.addEdge(0, 2, 5);
        check("Small graph: Kruskal weight = 6", Kruskal.run(small).totalWeight == 6);
        check("Small graph: Prim weight = 6", Prim.run(small).totalWeight == 6);
        check("Small graph: connected", Kruskal.run(small).connected && Prim.run(small).connected);


        Graph disc = new Graph(4, false);
        disc.addEdge(0, 1, 1);
        disc.addEdge(2, 3, 2);
        check("Disconnected: Kruskal detects", !Kruskal.run(disc).connected);
        check("Disconnected: Prim detects", !Prim.run(disc).connected);


        Graph tri = new Graph(3, false);
        tri.addEdge(0, 1, 1);
        tri.addEdge(1, 2, 1);
        tri.addEdge(0, 2, 1);
        check("Multiple MSTs: same weight = 2",
                Kruskal.run(tri).totalWeight == 2 && Prim.run(tri).totalWeight == 2);


        Graph dup = new Graph(4, false);
        dup.addEdge(0, 1, 5);
        dup.addEdge(1, 2, 5);
        dup.addEdge(2, 3, 5);
        dup.addEdge(3, 0, 5);
        dup.addEdge(0, 2, 5);
        check("Duplicate weights: Kruskal = Prim = 15",
                Kruskal.run(dup).totalWeight == 15 && Prim.run(dup).totalWeight == 15);


        Graph one = new Graph(1, false);
        check("Single vertex: MST weight 0, connected",
                Kruskal.run(one).totalWeight == 0 && Kruskal.run(one).connected
                        && Prim.run(one).totalWeight == 0 && Prim.run(one).connected);
        check("Single vertex: topological order [0]", TopologicalSort.sort(one).equals(List.of(0)));
        check("Single vertex: one SCC", SCC.find(one).size() == 1);
        Dijkstra d1 = new Dijkstra();
        d1.run(one, 0);
        check("Single vertex: Dijkstra dist = 0", d1.dist[0] == 0);


        boolean same = true;
        int[][] sizes = {{10, 30}, {100, 500}, {1000, 5000}};
        for (int[] s : sizes)
            for (long seed = 1; seed <= 10; seed++) {
                Graph g = GraphGenerator.connectedUndirected(s[0], s[1], seed);
                if (Kruskal.run(g).totalWeight != Prim.run(g).totalWeight) same = false;
                if (!Kruskal.run(g).connected || !Prim.run(g).connected) same = false;
            }
        check("Random connected graphs: Kruskal weight = Prim weight", same);


        boolean dagOk = true;
        for (int[] s : sizes)
            for (long seed = 1; seed <= 10; seed++) {
                Graph dag = GraphGenerator.directedDAG(s[0], s[1], seed);
                if (!validTopo(dag, TopologicalSort.sort(dag))) dagOk = false;
            }
        check("Topological order valid for all generated DAGs", dagOk);


        Graph cyc = new Graph(3, true);
        cyc.addEdge(0, 1, 1);
        cyc.addEdge(1, 2, 1);
        cyc.addEdge(2, 0, 1);
        check("Cycle: topological sort returns null", TopologicalSort.sort(cyc) == null);
        check("Cycle: one SCC of size 3", SCC.find(cyc).size() == 1 && SCC.find(cyc).get(0).size() == 3);


        boolean sccOk = true;
        for (int[] s : sizes) {
            Graph g = GraphGenerator.directedWithCycles(s[0], s[1], 42);
            if (!validScc(g, SCC.find(g))) sccOk = false;
        }
        check("SCC: every vertex in exactly one component", sccOk);

        Graph dagScc = GraphGenerator.directedDAG(10, 30, 42);
        check("SCC on DAG: every vertex is its own component", SCC.find(dagScc).size() == 10);


        Graph dg = new Graph(5, true);
        dg.addEdge(0, 1, 4);
        dg.addEdge(0, 2, 1);
        dg.addEdge(2, 1, 2);
        dg.addEdge(1, 3, 1);
        Dijkstra d = new Dijkstra();
        d.run(dg, 0);
        check("Dijkstra distances: 0, 3, 1, 4",
                d.dist[0] == 0 && d.dist[1] == 3 && d.dist[2] == 1 && d.dist[3] == 4);
        check("Dijkstra path to 3 = [0, 2, 1, 3]", d.path(3).equals(List.of(0, 2, 1, 3)));
        check("Dijkstra: vertex 4 unreachable", d.dist[4] == Dijkstra.INF && d.path(4).isEmpty());


        Graph neg = new Graph(2, true);
        neg.addEdge(0, 1, -5);
        boolean thrown = false;
        try {
            new Dijkstra().run(neg, 0);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check("Dijkstra rejects negative weights", thrown);

        System.out.println();
        System.out.println("Passed: " + passed + ", Failed: " + failed);
    }
}