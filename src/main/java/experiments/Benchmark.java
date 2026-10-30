package experiments;

import connectivity.SCC;
import connectivity.TopologicalSort;
import graph.Graph;
import graph.GraphGenerator;
import mst.Kruskal;
import mst.Prim;
import shortestpath.Dijkstra;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.LongSupplier;

public class Benchmark {
    static final int RUNS = 5, WARMUP = 3;
    static final int[][] SIZES = {{10, 30}, {100, 500}, {1000, 5000}};
    static final String[] NAMES = {"Small", "Medium", "Large"};

    static void measure(PrintWriter out, String algo, String name, int n, int m, LongSupplier task) {
        long time = 0, ops = 0;
        for (int i = -WARMUP; i < RUNS; i++) {
            long t0 = System.nanoTime();
            long o = task.getAsLong();
            long t1 = System.nanoTime();
            if (i >= 0) { time += t1 - t0; ops += o; }
        }
        out.println(String.format(Locale.US, "%s,%s,%d,%d,%.4f,%d",
                algo, name, n, m, time / (double) RUNS / 1e6, ops / RUNS));
    }

    public static void main(String[] args) throws Exception {
        Files.createDirectories(Paths.get("results/tables"));
        try (PrintWriter out = new PrintWriter("results/tables/results.csv")) {
            out.println("algorithm,graph,V,E,avg_time_ms,avg_operations");
            for (int s = 0; s < 3; s++) {
                int n = SIZES[s][0], m = SIZES[s][1];
                String name = NAMES[s];
                Graph und = GraphGenerator.connectedUndirected(n, m, 42);
                Graph dag = GraphGenerator.directedDAG(n, m, 42);
                Graph cyc = GraphGenerator.directedWithCycles(n, m, 42);

                measure(out, "Kruskal", name, n, m, () -> Kruskal.run(und).operations);
                measure(out, "Kruskal_DSU_ops", name, n, m, () -> { Kruskal.run(und); return Kruskal.lastDsuOps; });
                measure(out, "Prim", name, n, m, () -> Prim.run(und).operations);
                measure(out, "TopoSort_DAG", name, n, m, () -> { TopologicalSort.sort(dag); return TopologicalSort.operations; });
                measure(out, "TopoSort_Cyclic", name, n, m, () -> { TopologicalSort.sort(cyc); return TopologicalSort.operations; });
                measure(out, "SCC_Tarjan", name, n, m, () -> { SCC.find(cyc); return SCC.operations; });
                measure(out, "Dijkstra", name, n, m, () -> { Dijkstra d = new Dijkstra(); d.run(cyc, 0); return d.operations; });
            }
        }
        System.out.println("Done: results/tables/results.csv");
    }
}