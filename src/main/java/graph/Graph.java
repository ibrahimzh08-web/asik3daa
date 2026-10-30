package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int vertices;
    private final boolean directed;
    private final List<List<Edge>> adj = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        for (int i = 0; i < vertices; i++) adj.add(new ArrayList<>());
    }

    public void addEdge(int u, int v, int w) {
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        if (!directed) adj.get(v).add(new Edge(v, u, w));
    }

    public int getVertices() { return vertices; }
    public int getEdgeCount() { return edges.size(); }
    public boolean isDirected() { return directed; }
    public List<Edge> getEdges() { return edges; }
    public List<Edge> getNeighbors(int v) { return adj.get(v); }
}