import java.util.*;

class Graph {
    // Adjacency list representation of the graph
    List<List<Integer>> adj;

    // Number of vertices
    int V;

    // Constructor to initialize graph with V vertices
    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Add an undirected edge between u and v
    void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    void dfs(int i, boolean[] vis){
        vis[i] = true;

        for(var n : adj.get(i)){
            if(!vis[n]){
                dfs(n,vis);
            }
        }
    }

    int countConnectedComponents(){
        boolean[] vis = new boolean[V];
        int count = 0;


        for(int i=0;i<V;i++){
            if(!vis[i]){
                dfs(i,vis);
                count++;
            }
        }

        return count;
    }

}

public abstract class W9_T1_P3 {

    public static void main(String[] args) {
        Graph g = new Graph(5); // 5 vertices
        g.addEdge(0, 1);
        g.addEdge(2, 3);
        

        System.out.println(g.countConnectedComponents());
    }

}
