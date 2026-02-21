import java.util.*;

public class CycleDetectionDFS {

    // Adjacency list representation of the graph
    List<List<Integer>> adj;

    // Number of vertices
    int V;

    // Constructor to initialize graph with V vertices
    CycleDetectionDFS(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Add a directed edge u -> v
    void addDirectedEdge(int u, int v) {
        adj.get(u).add(v);
    }

    // Add an undirected edge between u and v
    void addUndirectedEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    /*
     * DFS helper for DIRECTED graph cycle detection
     * vis[] states:
     * 0 -> unvisited
     * 1 -> currently in recursion stack (processing)
     * 2 -> fully processed
     */
    boolean dfsDirect(int v, int[] vis) {
        // Mark current node as visiting
        vis[v] = 1;

        // Explore all neighbors
        for (int n : adj.get(v)) {
            // If neighbor is unvisited, DFS on it
            if (vis[n] == 0) {
                if (dfsDirect(n, vis))
                    return true;
            }
            // If neighbor is in recursion stack, cycle exists
            else if (vis[n] == 1) {
                return true;
            }
        }

        // Mark node as fully processed
        vis[v] = 2;
        return false;
    }

    // Detect cycle in a directed graph
    boolean hasCycleInDirectedGraph() {
        int[] vis = new int[V];

        // Run DFS from every unvisited node
        for (int i = 0; i < V; i++) {
            if (vis[i] == 0) {
                if (dfsDirect(i, vis))
                    return true;
            }
        }
        return false;
    }

    /*
     * DFS helper for UNDIRECTED graph cycle detection
     * parent is used to avoid considering the edge back to parent as a cycle
     */
    boolean dfsUndirect(int v, int[] vis, int parent) {
        // Mark current node as visited
        vis[v] = 1;

        // Traverse all adjacent vertices
        for (int n : adj.get(v)) {
            // If neighbor is unvisited, DFS on it
            if (vis[n] == 0) {
                if (dfsUndirect(n, vis, v))
                    return true;
            }
            // If visited neighbor is not parent, cycle exists
            else if (n != parent) {
                return true;
            }
        }
        return false;
    }

    // Detect cycle in an undirected graph
    boolean hasCycleInUndirectedGraph() {
        int[] vis = new int[V];

        // Run DFS from every unvisited node
        for (int i = 0; i < V; i++) {
            if (vis[i] == 0) {
                if (dfsUndirect(i, vis, -1))
                    return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        /*
         * =========================
         * TEST CASE 1: Directed graph WITH cycle
         * Cycle: 0 -> 1 -> 2 -> 0
         * =========================
         */
        CycleDetectionDFS d1 = new CycleDetectionDFS(5);
        d1.addDirectedEdge(0, 1);
        d1.addDirectedEdge(1, 2);
        d1.addDirectedEdge(2, 0); // cycle edge
        d1.addDirectedEdge(3, 4);

        System.out.println("Directed (with cycle): " + d1.hasCycleInDirectedGraph());

        /*
         * =========================
         * TEST CASE 2: Directed graph WITHOUT cycle
         * =========================
         */
        CycleDetectionDFS d2 = new CycleDetectionDFS(5);
        d2.addDirectedEdge(0, 1);
        d2.addDirectedEdge(2, 1);
        d2.addDirectedEdge(3, 2);
        d2.addDirectedEdge(4, 2);

        System.out.println("Directed (no cycle): " + d2.hasCycleInDirectedGraph());

        /*
         * =========================
         * TEST CASE 3: Undirected graph WITH cycle
         * Cycle: 0 - 1 - 2 - 0
         * =========================
         */
        CycleDetectionDFS u1 = new CycleDetectionDFS(5);
        u1.addUndirectedEdge(0, 1);
        u1.addUndirectedEdge(1, 2);
        u1.addUndirectedEdge(2, 0); // cycle edge
        u1.addUndirectedEdge(3, 4);

        System.out.println("Undirected (with cycle): " + u1.hasCycleInUndirectedGraph());

        /*
         * =========================
         * TEST CASE 4: Undirected graph WITHOUT cycle
         * =========================
         */
        CycleDetectionDFS u2 = new CycleDetectionDFS(5);
        u2.addUndirectedEdge(0, 1);
        u2.addUndirectedEdge(2, 1);
        u2.addUndirectedEdge(3, 2);
        u2.addUndirectedEdge(4, 2);

        System.out.println("Undirected (no cycle): " + u2.hasCycleInUndirectedGraph());
    }
}
