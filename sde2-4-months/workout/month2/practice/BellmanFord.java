import java.util.*;

// Represents a directed weighted edge (src -> dst)
class Edge {
    int src;
    int dst;
    int wt;

    Edge(int src, int dst, int wt) {
        this.src = src;
        this.dst = dst;
        this.wt = wt;
    }
}

public class BellmanFord {

    List<Edge> edgeList = new ArrayList<>(); // stores all edges

    BellmanFord(List<Edge> edgeList) {
        this.edgeList = edgeList;
    }

    // Adds a directed edge to the graph
    void addEdge(int src, int dst, int wt) {
        edgeList.add(new Edge(src, dst, wt));
    }

    // Time Complexity: O(V * E)
    // Computes shortest paths from source and detects negative cycles
    void bellmanFord(int src, int V) {

        int[] dist = new int[V]; // distance from source
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        // Relax all edges (V - 1) times
        for (int i = 0; i < V - 1; i++) {
            for (Edge edge : edgeList) {
                int u = edge.src;
                int v = edge.dst;
                int w = edge.wt;

                // Update distance if a shorter path is found
                if (dist[u] != Integer.MAX_VALUE && dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        // One more pass to detect negative weight cycle
        for (Edge edge : edgeList) {
            int u = edge.src;
            int v = edge.dst;
            int w = edge.wt;

            if (dist[u] != Integer.MAX_VALUE && dist[v] > dist[u] + w) {
                System.out.println("Graph has negative cycle");
                return;
            }
        }

        // Final shortest distances from source
        System.out.println(Arrays.toString(dist));
    }

    public static void main(String[] args) {
        List<Edge> edgeList = new ArrayList<>();
        BellmanFord b = new BellmanFord(edgeList);

        // Directed weighted edges
        b.addEdge(0, 1, 2);
        b.addEdge(1, 2, 3);
        b.addEdge(2, 3, 3);
        b.addEdge(3, 1, 7);
        b.addEdge(0, 2, 7);
        b.addEdge(0, 3, 4);

        // Run Bellman-Ford from source vertex 0
        b.bellmanFord(0, 4);
    }
}
