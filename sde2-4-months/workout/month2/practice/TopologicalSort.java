import java.util.*;

public class TopologicalSort {

    // Adjacency list representation of the directed graph
    List<List<Integer>> adj;

    // Number of vertices
    int V;

    // Constructor to initialize graph with V vertices
    TopologicalSort(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Add a directed edge u -> v
    void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    /**
     * DFS helper for topological sort
     * vis states:
     * 0 = unvisited
     * 1 = currently in recursion stack (visiting)
     * 2 = fully processed (visited)
     */
    void dfs(int v, int[] vis, Deque<Integer> st) {
        vis[v] = 1; // mark node as visiting

        // Explore all outgoing edges
        for (int n : adj.get(v)) {
            if (vis[n] == 0) {
                dfs(n, vis, st);
            } 
            // Back-edge found → cycle exists
            else if (vis[n] == 1) {
                throw new IllegalStateException("Cycle detected");
            }
        }

        vis[v] = 2;   // mark node as fully processed
        st.push(v);   // push after processing all dependencies
    }

    /**
     * Topological sort using DFS
     * Works only for DAGs
     * Time: O(V + E), Space: O(V)
     */
    void dfsTopoSort() {
        int[] vis = new int[V];
        Deque<Integer> st = new ArrayDeque<>();

        // Run DFS from every unvisited node
        for (int i = 0; i < V; i++) {
            if (vis[i] == 0) {
                dfs(i, vis, st);
            }
        }

        // Pop stack to get topological order
        List<Integer> order = new ArrayList<>();
        while (!st.isEmpty()) {
            order.add(st.pop());
        }

        System.out.println(order);
    }

    /**
     * Topological sort using BFS (Kahn's Algorithm)
     * Uses in-degree array
     * Time: O(V + E), Space: O(V)
     */
    void bfsTopoSort() {
        Deque<Integer> q = new ArrayDeque<>();
        int[] inDegree = new int[V];

        // Compute in-degrees of all vertices
        for (int i = 0; i < V; i++) {
            for (int n : adj.get(i)) {
                inDegree[n]++;
            }
        }

        // Enqueue all nodes with in-degree 0
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                q.offer(i);
            }
        }

        List<Integer> order = new ArrayList<>();

        // Process nodes with in-degree 0
        while (!q.isEmpty()) {
            int curr = q.poll();
            order.add(curr);

            // Reduce in-degree of neighbors
            for (int nbr : adj.get(curr)) {
                if (--inDegree[nbr] == 0) {
                    q.offer(nbr);
                }
            }
        }

        // If not all nodes are processed, cycle exists
        if (order.size() != V) {
            throw new IllegalStateException("Cycle detected");
        }

        System.out.println(order);
    }

    public static void main(String[] args) {
        int V = 5;

        TopologicalSort tp = new TopologicalSort(V);

        // Define directed edges
        tp.addEdge(0, 1);
        tp.addEdge(2, 1);
        tp.addEdge(3, 2);
        tp.addEdge(4, 2);

        // DFS-based topological sort
        tp.dfsTopoSort();

        // BFS-based topological sort (Kahn's algorithm)
        tp.bfsTopoSort();
    }
} 