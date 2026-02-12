import java.util.*;

class Node {
    int vertex;
    int dist;

    Node(int vertex, int dist) {
        this.vertex = vertex;
        this.dist = dist;
    }
}

class Graph {
    private int V;
    private List<List<int[]>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Adds an undirected weighted edge (u <-> v)
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new int[] { v, w });
        adj.get(v).add(new int[] { u, w });
    }

    public int size() {
        return V;
    }

    public List<List<int[]>> getAdj() {
        return adj;
    }
}

public class Dijkstra {

    private Graph graph;
    private int start;

    Dijkstra(Graph graph, int start) {
        this.graph = graph;
        this.start = start;
    }

    void dijkstra() {

        // Min-heap based on distance
        Queue<Node> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));

        int[] dist = new int[graph.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);

        List<List<int[]>> adj = graph.getAdj();

        minHeap.offer(new Node(start, 0));
        dist[start] = 0;

        while (!minHeap.isEmpty()) {
            Node current = minHeap.poll();
            int v = current.vertex;
            int currentDist = current.dist;

            // Skip outdated entries
            if (currentDist > dist[v]) continue;

            // Relax all neighbors
            for (int[] edge : adj.get(v)) {
                int u = edge[0];
                int weight = edge[1];
                
                //if distance from path u to v is lesser than from any other path
                int newDist = currentDist + weight;
                if (newDist < dist[u]) {
                    dist[u] = newDist;
                    minHeap.offer(new Node(u, newDist));
                }
            }
        }

        System.out.println(Arrays.toString(dist));
    }

    public static void main(String[] args) {

        Graph g = new Graph(5);

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 8);
        g.addEdge(1, 4, 6);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 2);
        g.addEdge(3, 4, 10);

        Dijkstra d = new Dijkstra(g, 0);
        d.dijkstra();
    }
}
