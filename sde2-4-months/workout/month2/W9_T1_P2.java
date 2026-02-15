import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Graph {
    private int V; // Number of vertices
    private List<List<Integer>> adj; // Adjacency list

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; ++i) {
            adj.add(new ArrayList<>());
        }
    }

    // Function to add an edge into the graph
    public void addEdge(int v, int w) {
        adj.get(v).add(w);
        // For an undirected graph, add: adj.get(w).add(v);
    }

    // DFS traversal
    public boolean findPathDFS(int src, int dst){
        boolean[] vis = new boolean[V];
        return DFSUtil(src,dst,vis);
    }

    private boolean DFSUtil(int src, int dst, boolean[] vis){
        if(src==dst) return true;
        vis[src] = true;
        for(Integer n : adj.get(src)){
            if(!vis[n]){
                if(DFSUtil(n,dst,vis)) return true;
            }
        }
        return false;
    }

    // BFS traversal
    public boolean findPathBFS(int src, int dst) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        visited[src] = true;
        queue.add(src);

        while (queue.size() != 0) {
            int curr = queue.poll();
            if(curr==dst) return true;
            

            for (int neighbor : adj.get(curr)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }

    
}

public class W9_T1_P2 {

     public static void main(String[] args) {
        Graph g = new Graph(4); // 4 vertices (0 to 3)

        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        System.out.println("DFS path from vertex 2 to 1:");
        System.out.println(g.findPathDFS(2,1));  // Output: 2 0 1 3 (order may vary slightly depending on adjacency list implementation)
        System.out.println("\nBFS starting from vertex 2:");
         System.out.println(g.findPathBFS(2,1)); // Output: 2 0 1 3
    }
    
}


