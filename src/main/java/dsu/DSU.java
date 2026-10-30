package dsu;

public class DSU {
    private final int[] parent;
    private final int[] rank;
    private int components;

    public long findCalls = 0;
    public long unionCalls = 0;
    public long pathSteps = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int x) {
        findCalls++;
        if (parent[x] != x) {
            pathSteps++;
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public boolean union(int a, int b) {
        unionCalls++;
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) return false;

        if (rank[ra] < rank[rb]) {
            parent[ra] = rb;
        } else if (rank[ra] > rank[rb]) {
            parent[rb] = ra;
        } else {
            parent[rb] = ra;
            rank[ra]++;
        }
        components--;
        return true;
    }

    public int getComponents() { return components; }
}