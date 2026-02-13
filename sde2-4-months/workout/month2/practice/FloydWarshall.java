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

public class FloydWarshall {
    static final int INF = Integer.MAX_VALUE; // Represents no direct path

    List<Edge> edgeList = new ArrayList<>();
    int V;            // number of vertices
    int[][] dist;     // distance matrix

    FloydWarshall(List<Edge> edgeList, int V) {
        this.edgeList = edgeList;
        this.V = V;
    }

    // Adds a directed edge to the graph
    void addEdge(int src, int dst, int wt) {
        edgeList.add(new Edge(src, dst, wt));
    }

    // Builds initial distance matrix from edge list
    void builtMatrix() {
        dist = new int[V][V];

        // Initialize distances
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                dist[i][j] = (i == j) ? 0 : INF;
            }
        }

        // Set direct edge weights
        for (Edge edge : edgeList) {
            dist[edge.src][edge.dst] = edge.wt;
        }
    }

    // Time Complexity: O(V^3)
    // Computes all-pairs shortest paths
    void floydWarshall() {

        // Try every vertex as an intermediate node
        for (int k = 0; k < V; k++) {
            for (int i = 0; i < V; i++) {
                for (int j = 0; j < V; j++) {

                    // Relax path i -> k -> j if valid
                    if (dist[i][k] != INF && dist[k][j] != INF) {
                        dist[i][j] =
                                Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }

        // Final shortest distances between all pairs
        System.out.println(Arrays.deepToString(dist));
    }

    public static void main(String[] args) {
        List<Edge> edgeList = new ArrayList<>();

        FloydWarshall f = new FloydWarshall(edgeList, 4);

        // Directed weighted edges
        f.addEdge(0, 1, 2);
        f.addEdge(1, 2, 3);
        f.addEdge(2, 3, 3);
        f.addEdge(3, 1, 7);
        f.addEdge(0, 2, 7);
        f.addEdge(0, 3, 4);

        f.builtMatrix();     // initialize distance matrix
        f.floydWarshall();   // run algorithm
    }
}
