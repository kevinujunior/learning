# SDE-2 Interview Prep: Graph Algorithms Study Template

## 1. Breadth-First Search (BFS)

*   **Basic Algorithm:**
    1.  Start with a source node, mark it as visited, and enqueue it.
    2.  While the queue is not empty:
        *   Dequeue a node `u`.
        *   Process `u`.
        *   For each unvisited neighbor `v` of `u`:
            *   Mark `v` as visited and enqueue `v`.

*   **Time Complexity:** O(V + E) where V is the number of vertices and E is the number of edges, as each vertex and each edge is processed at most once.
*   **Space Complexity:** O(V) for the queue and visited array.
*   **Use Cases:**
    *   Finding the shortest path in an unweighted graph.
    *   Level order traversal of a tree.
    *   Social networking (finding people within k degrees of separation).
    *   Web crawlers.
    *   Garbage collection (Mark and Sweep).

## 2. Depth-First Search (DFS)

*   **Basic Algorithm:**
    1.  Start with a source node, mark it as visited.
    2.  Recursively visit all unvisited neighbors of the current node.
    3.  If using an iterative approach, use a stack.
        *   Push the source node onto the stack and mark it as visited.
        *   While the stack is not empty:
            *   Pop a node `u`.
            *   Process `u`.
            *   For each unvisited neighbor `v` of `u`:
                *   Mark `v` as visited and push `v` onto the stack.

*   **Time Complexity:** O(V + E) as each vertex and each edge is processed at most once.
*   **Space Complexity:** O(V) for the recursion stack (or explicit stack) and visited array.
*   **Use Cases:**
    *   Topological sorting.
    *   Cycle detection in graphs.
    *   Finding connected components.
    *   Solving mazes and puzzles (backtracking).
    *   Pathfinding.

## 3. Topological Sort

*   **Description:** A linear ordering of vertices such that for every directed edge `u -> v`, vertex `u` comes before `v` in the ordering. Applicable only to Directed Acyclic Graphs (DAGs).
*   **Kahn's Algorithm (BFS-based):**
    1.  Compute in-degrees for all vertices.
    2.  Initialize a queue with all vertices having an in-degree of 0.
    3.  While the queue is not empty:
        *   Dequeue a vertex `u`. Add `u` to the topological order.
        *   For each neighbor `v` of `u`:
            *   Decrement in-degree of `v`.
            *   If in-degree of `v` becomes 0, enqueue `v`.
    4.  If the number of vertices in the topological order is less than V, a cycle exists.
*   **DFS-based Algorithm:**
    1.  Perform DFS on all unvisited vertices.
    2.  During DFS, when a node `u` has no unvisited neighbors (or all its children have been visited), add `u` to the front of a list (or push onto a stack).
    3.  The final list (or popped elements from the stack) represents the topological order.
*   **Time Complexity:** O(V + E).
*   **Space Complexity:** O(V) for in-degree array/recursion stack.
*   **Use Cases:**
    *   Task scheduling (dependencies between tasks).
    *   Build systems (compiling modules in correct order).
    *   Course prerequisites.

## 4. Dijkstra’s Algorithm

*   **Description:** Finds the shortest paths from a single source vertex to all other vertices in a graph with non-negative edge weights.
*   **Basic Algorithm:**
    1.  Initialize distances to all vertices as infinity and source distance as 0.
    2.  Use a min-priority queue to store `(distance, vertex)` pairs. Add `(0, source)` to the queue.
    3.  While the priority queue is not empty:
        *   Extract the vertex `u` with the minimum distance.
        *   If `u` has already been processed, continue.
        *   Mark `u` as processed.
        *   For each neighbor `v` of `u`:
            *   If `dist[u] + weight(u, v) < dist[v]`:
                *   Update `dist[v] = dist[u] + weight(u, v)`.
                *   Add `(dist[v], v)` to the priority queue.
*   **Time Complexity:** O(E log V) or O(E + V log V) with a Fibonacci heap. With a binary heap, it's typically O(E log V).
*   **Space Complexity:** O(V + E) for adjacency list, distances, and priority queue.
*   **Use Cases:**
    *   GPS navigation (finding the shortest route).
    *   Network routing protocols.
    *   Finding shortest paths in maps.

## 5. Bellman–Ford Algorithm

*   **Description:** Finds the shortest paths from a single source vertex to all other vertices, even with negative edge weights. Can detect negative cycles.
*   **Basic Algorithm:**
    1.  Initialize distances to all vertices as infinity and source distance as 0.
    2.  Repeat V-1 times:
        *   For each edge `(u, v)` with weight `w`:
            *   If `dist[u] + w < dist[v]`:
                *   `dist[v] = dist[u] + w`.
    3.  Check for negative cycles:
        *   For each edge `(u, v)` with weight `w`:
            *   If `dist[u] + w < dist[v]`:
                *   A negative cycle exists (shortest path not well-defined).
*   **Time Complexity:** O(V * E).
*   **Space Complexity:** O(V) for distances.
*   **Use Cases:**
    *   Routing in networks that might have negative costs.
    *   Arbitrage detection in financial markets.

## 6. Floyd–Warshall Algorithm

*   **Description:** Finds the shortest paths between all pairs of vertices in a weighted graph. Can handle negative edge weights but not negative cycles (if a negative cycle is detected, the distance becomes negative infinity).
*   **Basic Algorithm:**
    1.  Initialize a distance matrix `dist[i][j]` where `dist[i][j]` is the weight of the edge from `i` to `j`, or infinity if no direct edge. `dist[i][i] = 0`.
    2.  For `k` from 1 to V (intermediate vertices):
        *   For `i` from 1 to V (source vertices):
            *   For `j` from 1 to V (destination vertices):
                *   `dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])`.
*   **Time Complexity:** O(V^3).
*   **Space Complexity:** O(V^2) for the distance matrix.
*   **Use Cases:**
    *   Finding all-pairs shortest paths.
    *   Transitive closure of a graph.
    *   Computational geometry problems.

## 7. Prim’s Algorithm

*   **Description:** Finds a Minimum Spanning Tree (MST) for a connected, undirected, weighted graph. It grows the MST from an arbitrary starting vertex.
*   **Basic Algorithm:**
    1.  Start with an arbitrary vertex, add it to the MST.
    2.  Maintain a min-priority queue of edges that connect a vertex in the MST to a vertex not yet in the MST.
    3.  While the MST does not include all vertices:
        *   Extract the minimum weight edge `(u, v)` from the priority queue where `u` is in MST and `v` is not.
        *   Add `v` and the edge `(u, v)` to the MST.
        *   For all neighbors `w` of `v` that are not yet in the MST:
            *   Add edge `(v, w)` to the priority queue.
*   **Time Complexity:** O(E log V) with a binary heap, or O(E + V log V) with a Fibonacci heap.
*   **Space Complexity:** O(V + E) for adjacency list, distances, and priority queue.
*   **Use Cases:**
    *   Network design (connecting cities with minimum cable).
    *   Cluster analysis.
    *   Image segmentation.

## 8. Kruskal’s Algorithm

*   **Description:** Finds a Minimum Spanning Tree (MST) for a connected, undirected, weighted graph. It adds edges in increasing order of weight, as long as they don't form a cycle.
*   **Basic Algorithm:**
    1.  Sort all edges in non-decreasing order of their weights.
    2.  Initialize an empty MST and a Disjoint Set Union (DSU) structure where each vertex is in its own set.
    3.  For each edge `(u, v)` with weight `w` from the sorted list:
        *   If `find(u)` != `find(v)` (i.e., `u` and `v` are in different sets):
            *   Add `(u, v)` to the MST.
            *   `union(u, v)` (merge their sets).
    4.  Stop when MST has V-1 edges.
*   **Time Complexity:** O(E log E) or O(E log V) (since E <= V^2, log E is roughly log V^2 = 2 log V), dominated by sorting edges. Plus O(E * α(V)) for DSU operations, where α is the inverse Ackermann function, which is nearly constant.
*   **Space Complexity:** O(V + E) for storing edges, MST, and DSU structure.
*   **Use Cases:**
    *   Similar to Prim's for MST applications.

## 9. Union–Find (Disjoint Set Union)

*   **Description:** A data structure that maintains a collection of disjoint (non-overlapping) sets. It supports two primary operations: `find` (determine which set an element belongs to) and `union` (merge two sets into one).
*   **Key Optimizations:**
    *   **Path Compression:** During `find(i)`, make every node on the path from `i` to its root directly point to the root.
    *   **Union by Rank/Size:** When performing `union(i, j)`, attach the shorter/smaller tree under the root of the taller/larger tree to keep trees flat.
*   **Basic Operations (with optimizations):**
    *   `makeSet(i)`: Creates a new set containing only element `i`. `parent[i] = i`, `rank[i] = 0` (or `size[i] = 1`).
    *   `find(i)`:
        *   If `parent[i] == i`, return `i`.
        *   Else, `parent[i] = find(parent[i])` (path compression).
        *   Return `parent[i]`.
    *   `union(i, j)`:
        *   `root_i = find(i)`, `root_j = find(j)`.
        *   If `root_i != root_j`:
            *   If `rank[root_i] < rank[root_j]`, `parent[root_i] = root_j`.
            *   Else if `rank[root_i] > rank[root_j]`, `parent[root_j] = root_i`.
            *   Else, `parent[root_j] = root_i`, `rank[root_i]++`.
*   **Time Complexity:** Nearly constant time (amortized O(α(N))) for `find` and `union` operations, where α is the inverse Ackermann function.
*   **Space Complexity:** O(N) for parent and rank/size arrays.
*   **Use Cases:**
    *   Kruskal's algorithm for MST.
    *   Detecting cycles in an undirected graph.
    *   Finding connected components.
    *   Percolation problems.

## 10. Cycle Detection in Undirected Graph

*   **Description:** Determines if an undirected graph contains any cycles.
*   **BFS/DFS Based:**
    1.  Perform a traversal (BFS or DFS) starting from each unvisited node.
    2.  During traversal, keep track of visited nodes and their parents.
    3.  If you encounter an already visited node `v` that is not the direct parent of the current node `u`, then a cycle is detected.
*   **DSU Based:**
    1.  Initialize a DSU structure where each vertex is in its own set.
    2.  For each edge `(u, v)` in the graph:
        *   If `find(u)` == `find(v)`:
            *   A cycle is detected (adding this edge would connect two vertices already in the same component).
        *   Else:
            *   `union(u, v)`.
*   **Time Complexity:** O(V + E) for BFS/DFS. O(E * α(V)) for DSU.
*   **Space Complexity:** O(V) for visited array/parent array (BFS/DFS) or DSU structure.
*   **Use Cases:**
    *   Ensuring graph properties.
    *   Kruskal's algorithm.

## 11. Cycle Detection in Directed Graph

*   **Description:** Determines if a directed graph contains any cycles.
*   **DFS Based:**
    1.  Maintain three states for each node:
        *   `unvisited`: Node has not been visited yet.
        *   `visiting` (or `recursion stack`): Node is currently in the recursion stack (being processed).
        *   `visited`: Node has been fully processed (all its descendants visited).
    2.  Perform DFS. If during DFS, we encounter a node that is in the `visiting` state, a cycle is detected.
*   **Kahn's Algorithm (BFS-based, for topological sort):**
    1.  Compute in-degrees.
    2.  Enqueue all nodes with in-degree 0.
    3.  Dequeue nodes and decrement in-degrees of neighbors. Increment a `count` of processed nodes.
    4.  If at the end, `count` is less than V, then a cycle exists.
*   **Time Complexity:** O(V + E).
*   **Space Complexity:** O(V) for visited arrays/recursion stack.
*   **Use Cases:**
    *   Topological sorting.
    *   Deadlock detection in operating systems.

## 12. Strongly Connected Components (Kosaraju’s Algorithm)

*   **Description:** Finds strongly connected components (SCCs) in a directed graph. An SCC is a maximal subgraph where for any two vertices `u` and `v`, there is a path from `u` to `v` and a path from `v` to `u`.
*   **Basic Algorithm:**
    1.  Perform DFS on the original graph `G`. Keep track of the finishing times of each vertex (or push vertices to a stack in order of finishing time).
    2.  Compute the transpose (reverse) graph `G^T` by reversing all edges of `G`.
    3.  Perform DFS on `G^T`. When choosing the next starting vertex for DFS, always pick the vertex with the largest finishing time from step 1 (i.e., pop from the stack).
    4.  Each tree in the DFS forest of `G^T` represents an SCC.
*   **Time Complexity:** O(V + E) (two DFS traversals).
*   **Space Complexity:** O(V + E) for storing graph, transpose graph, and visited arrays/stack.
*   **Use Cases:**
    *   Analyzing dependencies in directed graphs.
    *   Finding deadlocks.
    *   Two-satisfiability problems.

## 13. Strongly Connected Components (Tarjan’s Algorithm)

*   **Description:** Finds SCCs in a directed graph using a single DFS pass. More efficient in practice than Kosaraju's as it avoids creating the transpose graph explicitly.
*   **Basic Algorithm:**
    1.  Perform DFS. For each vertex `u`, maintain:
        *   `disc[u]`: Discovery time of `u`.
        *   `low[u]`: Lowest discovery time reachable from `u` (including `u` itself) through `u`'s DFS subtree.
    2.  Maintain a stack of vertices currently in the DFS recursion stack.
    3.  During DFS for `u`:
        *   Set `disc[u] = low[u] = timer` and increment `timer`. Push `u` onto the stack.
        *   For each neighbor `v` of `u`:
            *   If `v` is unvisited:
                *   Recursively call DFS for `v`.
                *   `low[u] = min(low[u], low[v])`.
            *   Else if `v` is on the stack (back-edge):
                *   `low[u] = min(low[u], disc[v])`.
        *   If `low[u] == disc[u]` (i.e., `u` is the root of an SCC):
            *   Pop all vertices from the stack until `u` is popped, adding them to a new SCC.
*   **Time Complexity:** O(V + E) (single DFS traversal).
*   **Space Complexity:** O(V) for `disc`, `low` arrays, and stack.
*   **Use Cases:**
    *   Same as Kosaraju's, often preferred due to efficiency.

## 14. Shortest Path in Unweighted Graph

*   **Description:** Finds the shortest path (in terms of number of edges) from a source vertex to all other vertices in an unweighted graph.
*   **Basic Algorithm (BFS):**
    1.  Initialize distances to all vertices as infinity and source distance as 0.
    2.  Initialize a queue with the source node.
    3.  While the queue is not empty:
        *   Dequeue a node `u`.
        *   For each unvisited neighbor `v` of `u`:
            *   Set `dist[v] = dist[u] + 1`.
            *   Enqueue `v`.
            *   Mark `v` as visited.
*   **Time Complexity:** O(V + E).
*   **Space Complexity:** O(V) for queue and distances.
*   **Use Cases:**
    *   Finding the minimum number of moves in a game.
    *   Social network analysis (degrees of separation).

## 15. Shortest Path in DAG (Directed Acyclic Graph)

*   **Description:** Finds the shortest path from a source vertex to all other vertices in a directed acyclic graph. Can handle negative edge weights.
*   **Basic Algorithm:**
    1.  Perform a topological sort of the DAG.
    2.  Initialize distances to all vertices as infinity and source distance as 0.
    3.  Iterate through the vertices in topological order:
        *   For each vertex `u` in topological order:
            *   For each neighbor `v` of `u` with edge weight `w`:
                *   If `dist[u] + w < dist[v]`:
                    *   `dist[v] = dist[u] + w`.
*   **Time Complexity:** O(V + E) (for topological sort and single pass).
*   **Space Complexity:** O(V) for distances and topological sort array.
*   **Use Cases:**
    *   Project scheduling (critical path analysis).
    *   Finding longest paths in DAGs (by negating edge weights and finding shortest path).

## 16. Bipartite Graph Check

*   **Description:** Determines if a graph is bipartite (i.e., its vertices can be divided into two disjoint sets U and V such that every edge connects a vertex in U to one in V). A graph is bipartite if and only if it contains no odd-length cycles.
*   **BFS/DFS Based (Coloring):**
    1.  Start a traversal (BFS or DFS) from an unvisited vertex `s`.
    2.  Assign `s` to color 1.
    3.  For each neighbor `v` of `u`:
        *   If `v` is uncolored, assign it the opposite color of `u` and add it to the queue/stack.
        *   If `v` is colored and has the same color as `u`, the graph is not bipartite.
    4.  Repeat for all unvisited components.
*   **Time Complexity:** O(V + E).
*   **Space Complexity:** O(V) for color array and queue/stack.
*   **Use Cases:**
    *   Scheduling problems.
    *   Matching algorithms.
    *   Resource allocation.

## 17. Graph Coloring (Greedy)

*   **Description:** Assigns colors to vertices of a graph such that no two adjacent vertices have the same color. The goal is often to minimize the number of colors used (chromatic number), but the greedy approach doesn't guarantee this.
*   **Basic Algorithm:**
    1.  Initialize all vertices as uncolored.
    2.  Assign the first color (e.g., color 0) to the first vertex.
    3.  For each remaining vertex `u` (in some order, e.g., decreasing degree):
        *   Create a set of colors used by `u`'s neighbors.
        *   Assign `u` the smallest available color (i.e., the smallest non-negative integer not in the set of used neighbor colors).
*   **Time Complexity:** O(V + E) if iterating through neighbors to find minimum color. If using a min-priority queue to pick the vertex with highest uncolored neighbors, it can be higher.
*   **Space Complexity:** O(V) for color array and sets of used colors.
*   **Use Cases:**
    *   Scheduling (e.g., exam timetabling, register allocation in compilers).
    *   Map coloring.
    *   Frequency assignment.


## 18. Lowest Common Ancestor (Binary Lifting)

*   **Description:** Given a rooted tree and two nodes, find the deepest node that is an ancestor of both. Binary lifting is an efficient technique to answer LCA queries.
*   **Preprocessing:**
    1.  Perform a DFS traversal to calculate `depth[u]` for all nodes `u` and `parent[u][0]` (the immediate parent of `u`).
    2.  For `j` from 1 to `log(V)`:
        *   For each node `u` from 0 to `V-1`:
            *   `parent[u][j] = parent[parent[u][j-1]][j-1]` (i.e., the 2^j-th ancestor of `u` is the 2^(j-1)-th ancestor of `u`'s 2^(j-1)-th ancestor).
*   **LCA Query (`lca(u, v)`):**
    1.  If `depth[u] < depth[v]`, swap `u` and `v` to ensure `u` is deeper or at the same depth.
    2.  Lift `u` up by `depth[u] - depth[v]` levels so `u` and `v` are at the same depth. This is done by iterating `j` from `log(V)` down to 0: if `depth[u] - (1 << j) >= depth[v]`, then `u = parent[u][j]`.
    3.  If `u == v`, then `u` is the LCA.
    4.  Otherwise, lift `u` and `v` simultaneously. Iterate `j` from `log(V)` down to 0: if `parent[u][j] != parent[v][j]`, then `u = parent[u][j]` and `v = parent[v][j]`.
    5.  The LCA is `parent[u][0]` (the immediate parent of either `u` or `v` after lifting).
*   **Time Complexity:**
    *   Preprocessing: O(V log V).
    *   Query: O(log V).
*   **Space Complexity:** O(V log V) for the `parent` array.
*   **Use Cases:**
    *   Finding common ancestors in hierarchical data.
    *   Tree path queries (e.g., distance between two nodes in a tree).
    *   Network analysis.