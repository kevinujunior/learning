import java.util.*;

// Disjoint Set Union (Union-Find) data structure
// Used to detect cycles efficiently in Kruskal's algorithm
class DSU {
    int[] rank;    // rank[i] ≈ height of tree rooted at i
    int[] parent;  // parent[i] = parent of i in the DSU tree

    // Initialize DSU with n elements
    public DSU(int n) {
        rank = new int[n];
        parent = new int[n];

        // Initially, every node is its own parent (separate set)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
    }

    // Find the representative (root) of the set containing x
    // Uses PATH COMPRESSION to flatten the tree
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // path compression
        }
        return parent[x];
    }

    // Union two sets using UNION BY RANK
    void union(int x, int y) {
        int xRoot = find(x);
        int yRoot = find(y);

        // If both nodes are already in the same set, do nothing
        if (xRoot == yRoot)
            return;

        // Attach smaller rank tree under larger rank tree
        if (rank[xRoot] < rank[yRoot]) {
            parent[xRoot] = yRoot;
        } else if (rank[yRoot] < rank[xRoot]) {
            parent[yRoot] = xRoot;
        } else {
            // If ranks are equal, attach one under the other
            parent[xRoot] = yRoot;
            rank[yRoot]++; // increase rank since height increases
        }
    }
}

// Represents an UNDIRECTED weighted edge in the graph
class Edge {
    int src; // source vertex
    int dst; // destination vertex
    int wt;  // edge weight

    Edge(int src, int dst, int wt) {
        this.src = src;
        this.dst = dst;
        this.wt = wt;
    }
}

public class Kruskal {

    // List to store all edges of the graph
    List<Edge> edgeList = new ArrayList<>();

    Kruskal(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    // Adds an undirected edge by adding both directions
    void addEdge(int src, int dst, int wt) {
        edgeList.add(new Edge(src, dst, wt));
        edgeList.add(new Edge(dst, src, wt));
    }

    // Kruskal's Algorithm to find Minimum Spanning Tree (MST)
    // Time Complexity: O(E log E)
    void kruskal(int V) {

        // Step 1: Sort all edges by increasing weight
        edgeList.sort(Comparator.comparingInt(e -> e.wt));

        // Step 2: Initialize DSU for cycle detection
        DSU dsu = new DSU(V);

        int mstWeight = 0; // total weight of MST
        int edgeCount = 0; // number of edges included in MST

        // Step 3: Process edges in increasing order
        for (Edge e : edgeList) {

            int u = e.src;
            int v = e.dst;
            int w = e.wt;

            // Find roots of both endpoints
            int pu = dsu.find(u);
            int pv = dsu.find(v);

            // If including this edge does not form a cycle
            if (pu != pv) {
                mstWeight += w;     // add edge weight to MST
                dsu.union(pu, pv); // union the two sets
                edgeCount++;

                // MST is complete when we have V - 1 edges
                if (edgeCount == V - 1)
                    break;
            }
        }

        // Final MST weight
        System.out.println(mstWeight);
    }

    public static void main(String[] args) {

        List<Edge> edgeList = new ArrayList<>();
        Kruskal k = new Kruskal(edgeList);

        int V = 4; // number of vertices

        // Undirected weighted graph edges
        k.addEdge(0, 1, 2);
        k.addEdge(1, 2, 3);
        k.addEdge(2, 3, 3);
        k.addEdge(3, 1, 7);
        k.addEdge(0, 2, 7);
        k.addEdge(0, 3, 4);

        // Run Kruskal's Algorithm to find MST
        k.kruskal(V);
    }
}
