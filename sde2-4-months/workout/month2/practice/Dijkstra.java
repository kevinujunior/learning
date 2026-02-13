import java.util.*;

// Represents an edge to a vertex with a given weight
class Edge {
    int vertex; // destination vertex
    int dist;   // edge weight OR current distance (when used in heap)

    Edge(int vertex, int dist) {
        this.vertex = vertex;
        this.dist = dist;
    }
}

// Graph represented using adjacency list
class Graph {
    private int V; // number of vertices
    private List<List<Edge>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        // Initialize adjacency list for each vertex
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Adds an undirected weighted edge
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v, w));
        adj.get(v).add(new Edge(u, w));
    }

    public int size() {
        return V;
    }

    public List<List<Edge>> getAdj() {
        return adj;
    }
}

public class Dijkstra {

    private Graph graph;
    private int start; // source vertex

    Dijkstra(Graph graph, int start) {
        this.graph = graph;
        this.start = start;
    }

    // Time Complexity: O((V + E) log V)
    void dijkstra() {

        // Min-heap ordered by shortest distance found so far
        PriorityQueue<Edge> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(e -> e.dist));

        int[] dist = new int[graph.size()];     // shortest distance from source
        boolean[] vis = new boolean[graph.size()]; // marks finalized vertices
        Arrays.fill(dist, Integer.MAX_VALUE);

        List<List<Edge>> adj = graph.getAdj();

        // Initialize source
        dist[start] = 0;
        minHeap.offer(new Edge(start, 0));

        while (!minHeap.isEmpty()) {

            // Extract vertex with minimum distance
            Edge current = minHeap.poll();
            int u = current.vertex;

            // Skip if shortest path to u is already finalized
            if (vis[u]) continue;
            vis[u] = true;

            // Relax all adjacent edges of u
            for (Edge edge : adj.get(u)) {
                int v = edge.vertex;
                int weight = edge.dist;

                int newDist = dist[u] + weight;

                // Update distance if a shorter path is found
                if (!vis[v] && newDist < dist[v]) {
                    dist[v] = newDist;
                    minHeap.offer(new Edge(v, newDist));
                }
            }
        }

        // Final shortest distances from source
        System.out.println(Arrays.toString(dist));
    }

    public static void main(String[] args) {

        Graph g = new Graph(4);

        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 3);
        g.addEdge(3, 1, 7);
        g.addEdge(0, 2, 7);
        g.addEdge(0, 3, 4);

        // Run Dijkstra from source vertex 0
        Dijkstra d = new Dijkstra(g, 0);
        d.dijkstra();
    }
}
