import java.util.*;

class Edge {
    int vertex;
    int dist;

    Edge(int vertex, int dist) {
        this.vertex = vertex;
        this.dist = dist;
    }
}

class Graph {
    private int V;
    private List<List<Edge>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    // Adds an undirected weighted edge (u <-> v)
    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(v,w));
        adj.get(v).add(new Edge(u,w));
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
    private int start;

    Dijkstra(Graph graph, int start) {
        this.graph = graph;
        this.start = start;
    }

    void dijkstra() {

        // Min-heap based on distance
        Queue<Edge> minHeap =
                new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));

        int[] dist = new int[graph.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);

        List<List<Edge>> adj = graph.getAdj();

        minHeap.offer(new Edge(start, 0));
        dist[start] = 0;

        while (!minHeap.isEmpty()) {
            Edge current = minHeap.poll();
            int u = current.vertex;
            int currentDist = current.dist;

            // Skip outdated entries
            if (currentDist > dist[u]) continue;

            // Relax all neighbors
            for (Edge edge : adj.get(u)) {
                int v = edge.vertex;
                int weight = edge.dist;
                
                //if distance from path u to v is lesser than from any other path
                int newDist = currentDist + weight;
                if (newDist < dist[v]) {
                    dist[v] = newDist;
                    minHeap.offer(new Edge(v, newDist));
                }
            }
        }

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

        Dijkstra d = new Dijkstra(g, 0);
        d.dijkstra();
    }
}
