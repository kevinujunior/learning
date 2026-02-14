import java.util.*;

// Represents an edge in the graph OR a node entry in the priority queue
class Edge {
    int vertex; // destination vertex
    int dist;   // edge weight (when in adjacency list) OR key value (when in PQ)

    Edge(int vertex, int dist) {
        this.vertex = vertex;
        this.dist = dist;
    }
}

// Graph represented using an adjacency list
class Graph {
    private int V; // number of vertices
    private List<List<Edge>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);

        // Initialize empty adjacency list for each vertex
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Adds an undirected weighted edge (u <-> v)
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w)); // edge from u to v
        adj.get(v).add(new Edge(u, w)); // edge from v to u
    }

    // Returns number of vertices
    public int size() {
        return V;
    }

    // Returns adjacency list
    public List<List<Edge>> getAdj() {
        return adj;
    }
}

public class Prim {

    Graph g;

    Prim(Graph g) {
        this.g = g;
    }

    // Prim's Algorithm using Min-Heap (Priority Queue)
    // Time Complexity: O((V + E) log V)
    public void prims(int start) {

        int mst = 0; // total weight of Minimum Spanning Tree
        List<List<Edge>> adjList = g.getAdj();

        // Marks whether a vertex is already included in MST
        boolean[] vis = new boolean[g.size()];

        // Min-heap based on edge weight (key value)
        PriorityQueue<Edge> pq =
                new PriorityQueue<>(Comparator.comparingInt(e -> e.dist));

        // Start from the given source vertex with cost 0
        pq.offer(new Edge(start, 0));

        // Continue until all reachable vertices are processed
        while (!pq.isEmpty()) {

            // Extract edge with minimum weight
            Edge e = pq.poll();
            int u = e.vertex;

            // If vertex already included in MST, skip
            if (vis[u])
                continue;

            // Include vertex u in MST
            vis[u] = true;
            mst += e.dist; // add edge weight to MST total

            // Explore all adjacent edges of u
            for (Edge n : adjList.get(u)) {
                int v = n.vertex;

                // Push only edges leading to non-MST vertices
                if (!vis[v]) {
                    pq.offer(n);
                }
            }
        }

        // Final MST weight
        System.out.println(mst);
    }

    public static void main(String[] args) {

        Graph g = new Graph(4);

        // Undirected weighted graph
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 3);
        g.addEdge(3, 1, 7);
        g.addEdge(0, 2, 7);
        g.addEdge(0, 3, 4);

        // Run Prim's Algorithm starting from vertex 0
        Prim p = new Prim(g);
        p.prims(0);
    }
}
