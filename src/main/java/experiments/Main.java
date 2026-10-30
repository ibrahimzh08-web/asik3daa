package experiments;

import connectivity.*;
import graph.*;
import mst.*;
import shortestpath.Dijkstra;

public class Main {
    public static void main(String[] args) {
        Graph g = GraphGenerator.connectedUndirected(10, 30, 42);
        MSTResult k = Kruskal.run(g);
        MSTResult p = Prim.run(g);
        System.out.println("Kruskal: " + k.totalWeight + ", Prim: " + p.totalWeight
                + ", connected: " + k.connected);

        Graph dag = GraphGenerator.directedDAG(10, 30, 42);
        System.out.println("DAG order: " + TopologicalSort.sort(dag));

        Graph cyc = GraphGenerator.directedWithCycles(10, 30, 42);
        System.out.println("Cycle graph order: " + TopologicalSort.sort(cyc));
        System.out.println("SCC: " + SCC.find(cyc));

        Dijkstra d = new Dijkstra();
        d.run(cyc, 0);
        for (int v = 0; v < 10; v++)
            System.out.println("0 -> " + v + ": "
                    + (d.dist[v] == Dijkstra.INF ? "unreachable" : d.dist[v]) + " " + d.path(v));
    }
}