import java.io.*;
import java.util.*;

/**
 * =====================================================================
 * ULTIMATE COMPETITIVE PROGRAMMING TEMPLATE FOR JAVA
 * =====================================================================
 * 
 * This is a complete, production-ready template containing:
 * - Fast I/O utilities
 * - All essential data structures
 * - Graph algorithms (DFS, BFS, Dijkstra, Floyd-Warshall, etc.)
 * - String algorithms (KMP, Z-algorithm, Trie, Hashing)
 * - Dynamic Programming patterns
 * - Number theory and modular arithmetic
 * - Geometry basics
 * - Advanced trees (Segment Tree, Fenwick Tree)
 * - And much more!
 * 
 * USAGE: Just copy paste methods you need into your solution.
 * =====================================================================
 */

public class UltimateTemplate {

    // ========================================================================
    // CONFIGURATION & CONSTANTS
    // ========================================================================
    static final long MOD = 1_000_000_007L;
    static final long MOD2 = 998_244_353L;
    static final long INF = Long.MAX_VALUE / 2;
    static final int MAXN = 100_005;
    
    // ========================================================================
    // FAST I/O
    // ========================================================================
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;
        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try { st = new StringTokenizer(br.readLine()); } catch (IOException e) { e.printStackTrace(); }
            }
            return st.nextToken();
        }
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        double nextDouble() { return Double.parseDouble(next()); }
        String nextLine() {
            String str = "";
            try { str = br.readLine(); } catch (IOException e) { e.printStackTrace(); }
            return str;
        }
    }

    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(System.out);

    // ========================================================================
    // MAIN TEMPLATE
    // ========================================================================
    public static void main(String[] args) {
        solve();
        out.flush();
    }

    static void solve() {
        // int t = sc.nextInt();
        // while (t-- > 0) {
        //     // Your code here
        // }
    }

    // ========================================================================
    // 1. GRAPH REPRESENTATIONS & TRAVERSALS
    // ========================================================================

    // Adjacency list representation
    static class Graph {
        List<List<int[]>> adj;
        int n;
        
        Graph(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }
        
        void addEdge(int u, int v, int w) {
            adj.get(u).add(new int[]{v, w});
        }
        
        void addUndirectedEdge(int u, int v, int w) {
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }
    }

    // ========================================================================
    // 1.1 DEPTH-FIRST SEARCH (DFS)
    // ========================================================================

    static void dfs(int u, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        // Process node u here
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfs(v, adj, visited);
            }
        }
    }

    // DFS with parent (for trees)
    static void dfsTree(int u, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (v != parent && !visited[v]) {
                dfsTree(v, u, adj, visited);
            }
        }
    }

    // Topological sort using DFS
    static void topologicalSort(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        Stack<Integer> stk = new Stack<>();
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsTopological(i, adj, visited, stk);
            }
        }
        
        while (!stk.isEmpty()) {
            out.print(stk.pop() + " ");
        }
    }

    static void dfsTopological(int u, List<List<Integer>> adj, boolean[] visited, Stack<Integer> stk) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                dfsTopological(v, adj, visited, stk);
            }
        }
        stk.push(u);
    }

    // Find connected components
    static void findConnectedComponents(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        int componentCount = 0;
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                componentCount++;
                dfs(i, adj, visited);
            }
        }
        out.println("Connected components: " + componentCount);
    }

    // Detect cycle in undirected graph
    static boolean hasCycleUndirected(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (dfsCycleUndirected(i, -1, adj, visited)) return true;
            }
        }
        return false;
    }

    static boolean dfsCycleUndirected(int u, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                if (dfsCycleUndirected(v, u, adj, visited)) return true;
            } else if (v != parent) {
                return true;
            }
        }
        return false;
    }

    // Detect cycle in directed graph (using colors)
    static boolean hasCycleDirected(int n, List<List<Integer>> adj) {
        int[] color = new int[n]; // 0: white, 1: gray, 2: black
        
        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                if (dfsCycleDirected(i, adj, color)) return true;
            }
        }
        return false;
    }

    static boolean dfsCycleDirected(int u, List<List<Integer>> adj, int[] color) {
        color[u] = 1; // Gray
        for (int v : adj.get(u)) {
            if (color[v] == 1) return true; // Back edge
            if (color[v] == 0 && dfsCycleDirected(v, adj, color)) return true;
        }
        color[u] = 2; // Black
        return false;
    }

    // ========================================================================
    // 1.2 BREADTH-FIRST SEARCH (BFS)
    // ========================================================================

    static void bfs(int start, List<List<Integer>> adj) {
        int n = adj.size();
        boolean[] visited = new boolean[n];
        Queue<Integer> q = new LinkedList<>();
        q.offer(start);
        visited[start] = true;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            // Process node u
            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    visited[v] = true;
                    q.offer(v);
                }
            }
        }
    }

    // BFS with distance
    static int[] bfsDistance(int start, List<List<Integer>> adj) {
        int n = adj.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        Queue<Integer> q = new LinkedList<>();
        q.offer(start);
        dist[start] = 0;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.offer(v);
                }
            }
        }
        return dist;
    }

    // BFS on 2D grid
    static int[][] bfs2D(char[][] grid, int sr, int sc, char block) {
        int rows = grid.length, cols = grid[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, -1);
        
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{sr, sc});
        dist[sr][sc] = 0;
        
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int r = curr[0], c = curr[1];
            
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && dist[nr][nc] == -1 && grid[nr][nc] != block) {
                    dist[nr][nc] = dist[r][c] + 1;
                    q.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    // Multi-source BFS
    static int[][] multiSourceBFS(char[][] grid, char source) {
        int rows = grid.length, cols = grid[0].length;
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        
        Queue<int[]> q = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == source) {
                    q.offer(new int[]{i, j});
                    dist[i][j] = 0;
                }
            }
        }
        
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int r = curr[0], c = curr[1];
            
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && dist[nr][nc] > dist[r][c] + 1) {
                    dist[nr][nc] = dist[r][c] + 1;
                    q.offer(new int[]{nr, nc});
                }
            }
        }
        return dist;
    }

    // ========================================================================
    // 1.3 SHORTEST PATH ALGORITHMS
    // ========================================================================

    // Dijkstra's algorithm (single-source shortest path)
    static long[] dijkstra(int start, List<List<int[]>> adj, int n) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[start] = 0;
        
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.offer(new long[]{0, start});
        
        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            long d = curr[0];
            int u = (int)curr[1];
            
            if (d > dist[u]) continue;
            
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new long[]{dist[v], v});
                }
            }
        }
        return dist;
    }

    // Floyd-Warshall (all-pairs shortest path)
    static void floydWarshall(long[][] dist, int n) {
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }
    }

    // Bellman-Ford (handles negative edges, detects negative cycles)
    static long[] bellmanFord(int start, List<long[]> edges, int n) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[start] = 0;
        
        for (int i = 0; i < n - 1; i++) {
            for (long[] edge : edges) {
                int u = (int)edge[0], v = (int)edge[1];
                long w = edge[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }
        
        // Check for negative cycle
        for (long[] edge : edges) {
            int u = (int)edge[0], v = (int)edge[1];
            long w = edge[2];
            if (dist[u] != INF && dist[u] + w < dist[v]) {
                out.println("Negative cycle detected!");
            }
        }
        
        return dist;
    }

    // ========================================================================
    // 1.4 MINIMUM SPANNING TREE
    // ========================================================================

    // Kruskal's algorithm using DSU
    static long kruskal(List<long[]> edges, int n) {
        Collections.sort(edges, (a, b) -> Long.compare(a[2], b[2]));
        DSU dsu = new DSU(n);
        long mstWeight = 0;
        int edgeCount = 0;
        
        for (long[] edge : edges) {
            int u = (int)edge[0], v = (int)edge[1];
            long w = edge[2];
            
            if (dsu.union(u, v)) {
                mstWeight += w;
                edgeCount++;
                if (edgeCount == n - 1) break;
            }
        }
        
        return edgeCount == n - 1 ? mstWeight : -1;
    }

    // Prim's algorithm
    static long prim(List<List<int[]>> adj, int n) {
        boolean[] inMST = new boolean[n];
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        pq.offer(new long[]{0, 0});
        
        long mstWeight = 0;
        int edgeCount = 0;
        
        while (!pq.isEmpty()) {
            long[] curr = pq.poll();
            long w = curr[0];
            int u = (int)curr[1];
            
            if (inMST[u]) continue;
            inMST[u] = true;
            mstWeight += w;
            edgeCount++;
            
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                int weight = edge[1];
                if (!inMST[v]) {
                    pq.offer(new long[]{weight, v});
                }
            }
        }
        
        return edgeCount == n ? mstWeight : -1;
    }

    // ========================================================================
    // 1.5 STRONGLY CONNECTED COMPONENTS (SCC)
    // ========================================================================

    // Kosaraju's algorithm for SCC
    static class SCCKosaraju {
        int n;
        List<List<Integer>> adj, radj;
        boolean[] visited;
        Stack<Integer> stk;
        
        SCCKosaraju(int n) {
            this.n = n;
            adj = new ArrayList<>();
            radj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
                radj.add(new ArrayList<>());
            }
            visited = new boolean[n];
            stk = new Stack<>();
        }
        
        void addEdge(int u, int v) {
            adj.get(u).add(v);
            radj.get(v).add(u);
        }
        
        void dfs1(int u) {
            visited[u] = true;
            for (int v : adj.get(u)) {
                if (!visited[v]) dfs1(v);
            }
            stk.push(u);
        }
        
        void dfs2(int u) {
            visited[u] = true;
            for (int v : radj.get(u)) {
                if (!visited[v]) dfs2(v);
            }
        }
        
        int findSCC() {
            for (int i = 0; i < n; i++) {
                if (!visited[i]) dfs1(i);
            }
            
            Arrays.fill(visited, false);
            int sccCount = 0;
            
            while (!stk.isEmpty()) {
                int u = stk.pop();
                if (!visited[u]) {
                    dfs2(u);
                    sccCount++;
                }
            }
            
            return sccCount;
        }
    }

    // ========================================================================
    // 2. UNION-FIND (DISJOINT SET UNION)
    // ========================================================================

    static class DSU {
        int[] parent, rank, size;
        
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        boolean union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return false;
            
            if (rank[px] < rank[py]) {
                parent[px] = py;
                size[py] += size[px];
            } else if (rank[px] > rank[py]) {
                parent[py] = px;
                size[px] += size[py];
            } else {
                parent[py] = px;
                size[px] += size[py];
                rank[px]++;
            }
            return true;
        }
        
        int getSize(int x) {
            return size[find(x)];
        }
    }

    // ========================================================================
    // 3. TRIE (PREFIX TREE)
    // ========================================================================

    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        boolean isEnd = false;
        int count = 0; // For counting prefixes
    }

    static class Trie {
        TrieNode root = new TrieNode();
        
        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                node.count++;
            }
            node.isEnd = true;
        }
        
        boolean search(String word) {
            TrieNode node = findNode(word);
            return node != null && node.isEnd;
        }
        
        boolean startsWith(String prefix) {
            return findNode(prefix) != null;
        }
        
        int countWordsWithPrefix(String prefix) {
            TrieNode node = findNode(prefix);
            return node != null ? node.count : 0;
        }
        
        void delete(String word) {
            deleteHelper(root, word, 0);
        }
        
        private boolean deleteHelper(TrieNode node, String word, int idx) {
            if (idx == word.length()) {
                if (!node.isEnd) return false;
                node.isEnd = false;
                return node.children.isEmpty();
            }
            
            char c = word.charAt(idx);
            TrieNode child = node.children.get(c);
            if (child == null) return false;
            
            boolean shouldDelete = deleteHelper(child, word, idx + 1);
            if (shouldDelete) {
                node.children.remove(c);
                return node.children.isEmpty() && !node.isEnd;
            }
            return false;
        }
        
        private TrieNode findNode(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                if (!node.children.containsKey(c)) return null;
                node = node.children.get(c);
            }
            return node;
        }
    }

    // ========================================================================
    // 4. STRING ALGORITHMS
    // ========================================================================

    // KMP (Knuth-Morris-Pratt) Pattern Matching
    static int[] computeLPS(String pattern) {
        int n = pattern.length();
        int[] lps = new int[n];
        int len = 0, i = 1;
        
        while (i < n) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                lps[i++] = ++len;
            } else {
                if (len != 0) len = lps[len - 1];
                else lps[i++] = 0;
            }
        }
        return lps;
    }

    static List<Integer> kmp(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        int[] lps = computeLPS(pattern);
        int i = 0, j = 0;
        
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }
            
            if (j == pattern.length()) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < text.length() && text.charAt(i) != pattern.charAt(j)) {
                if (j != 0) j = lps[j - 1];
                else i++;
            }
        }
        
        return matches;
    }

    // Z-Algorithm
    static int[] zAlgorithm(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;
        int l = 0, r = 0;
        
        for (int i = 1; i < n; i++) {
            if (i > r) {
                l = r = i;
                while (r < n && s.charAt(r - l) == s.charAt(r)) r++;
                z[i] = r - l;
                r--;
            } else {
                int k = i - l;
                if (z[k] < r - i + 1) {
                    z[i] = z[k];
                } else {
                    l = i;
                    while (r < n && s.charAt(r - l) == s.charAt(r)) r++;
                    z[i] = r - l;
                    r--;
                }
            }
        }
        
        return z;
    }

    // String hashing
    static class StringHash {
        long[] hash, pow;
        long mod, base;
        
        StringHash(String s, long base, long mod) {
            this.base = base;
            this.mod = mod;
            int n = s.length();
            hash = new long[n + 1];
            pow = new long[n + 1];
            pow[0] = 1;
            
            for (int i = 0; i < n; i++) {
                hash[i + 1] = (hash[i] * base + s.charAt(i)) % mod;
                pow[i + 1] = (pow[i] * base) % mod;
            }
        }
        
        long getHash(int l, int r) {
            long h = (hash[r + 1] - hash[l] * pow[r - l + 1]) % mod;
            return (h + mod) % mod;
        }
    }

    // ========================================================================
    // 5. SEGMENT TREE
    // ========================================================================

    static class SegmentTree {
        long[] tree;
        int n;
        
        SegmentTree(long[] arr) {
            n = arr.length;
            tree = new long[4 * n];
            build(arr, 0, 0, n - 1);
        }
        
        private void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node + 1, start, mid);
                build(arr, 2 * node + 2, mid + 1, end);
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
        
        long query(int l, int r) {
            return query(0, 0, n - 1, l, r);
        }
        
        private long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) return tree[node];
            
            int mid = (start + end) / 2;
            return query(2 * node + 1, start, mid, l, r) +
                   query(2 * node + 2, mid + 1, end, l, r);
        }
        
        void update(int idx, long val) {
            update(0, 0, n - 1, idx, val);
        }
        
        private void update(int node, int start, int end, int idx, long val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (idx <= mid) {
                    update(2 * node + 1, start, mid, idx, val);
                } else {
                    update(2 * node + 2, mid + 1, end, idx, val);
                }
                tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
            }
        }
    }

    // ========================================================================
    // 6. FENWICK TREE (BINARY INDEXED TREE)
    // ========================================================================

    static class FenwickTree {
        long[] tree;
        int n;
        
        FenwickTree(int n) {
            this.n = n;
            tree = new long[n + 1];
        }
        
        void update(int idx, long delta) {
            for (int i = idx; i <= n; i += i & (-i)) {
                tree[i] += delta;
            }
        }
        
        long query(int idx) {
            long sum = 0;
            for (int i = idx; i > 0; i -= i & (-i)) {
                sum += tree[i];
            }
            return sum;
        }
        
        long rangeQuery(int l, int r) {
            return query(r) - query(l - 1);
        }
    }

    // ========================================================================
    // 7. DYNAMIC PROGRAMMING PATTERNS
    // ========================================================================

    // Longest Common Subsequence (LCS)
    static int lcs(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }

    // Edit Distance (Levenshtein)
    static int editDistance(String a, String b) {
        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
                }
            }
        }
        
        return dp[m][n];
    }

    // 0/1 Knapsack
    static int knapsack(int[] weights, int[] values, int capacity) {
        int n = weights.length;
        int[] dp = new int[capacity + 1];
        
        for (int i = 0; i < n; i++) {
            for (int w = capacity; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], dp[w - weights[i]] + values[i]);
            }
        }
        
        return dp[capacity];
    }

    // Coin change
    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (coin <= i) {
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // Longest Increasing Subsequence
    static int lis(int[] arr) {
        int n = arr.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        
        return Arrays.stream(dp).max().orElse(0);
    }

    // ========================================================================
    // 8. NUMBER THEORY
    // ========================================================================

    static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    static long lcm(long a, long b) {
        return (a / gcd(a, b)) * b;
    }

    static boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    static boolean[] sieve(int n) {
        boolean[] prime = new boolean[n + 1];
        Arrays.fill(prime, true);
        prime[0] = prime[1] = false;
        
        for (int i = 2; i * i <= n; i++) {
            if (prime[i]) {
                for (int j = i * i; j <= n; j += i) {
                    prime[j] = false;
                }
            }
        }
        
        return prime;
    }

    static List<Long> primeFactors(long n) {
        List<Long> factors = new ArrayList<>();
        
        for (long i = 2; i * i <= n; i++) {
            while (n % i == 0) {
                factors.add(i);
                n /= i;
            }
        }
        
        if (n > 1) factors.add(n);
        return factors;
    }

    static List<Long> divisors(long n) {
        List<Long> divs = new ArrayList<>();
        
        for (long i = 1; i * i <= n; i++) {
            if (n % i == 0) {
                divs.add(i);
                if (i != n / i) divs.add(n / i);
            }
        }
        
        return divs;
    }

    // Euler's totient
    static long phi(long n) {
        long result = n;
        
        for (long p = 2; p * p <= n; p++) {
            if (n % p == 0) {
                while (n % p == 0) n /= p;
                result -= result / p;
            }
        }
        
        if (n > 1) result -= result / n;
        return result;
    }

    // ========================================================================
    // 9. MODULAR ARITHMETIC
    // ========================================================================

    static long power(long base, long exp, long mod) {
        long res = 1;
        base %= mod;
        
        while (exp > 0) {
            if ((exp & 1) == 1) res = (res * base) % mod;
            base = (base * base) % mod;
            exp >>= 1;
        }
        
        return res;
    }

    static long modInverse(long a, long mod) {
        return power(a, mod - 2, mod);
    }

    static long modAdd(long a, long b, long mod) {
        return ((a % mod) + (b % mod)) % mod;
    }

    static long modSub(long a, long b, long mod) {
        return ((a % mod) - (b % mod) + mod) % mod;
    }

    static long modMul(long a, long b, long mod) {
        return ((a % mod) * (b % mod)) % mod;
    }

    static long modDiv(long a, long b, long mod) {
        return modMul(a, modInverse(b, mod), mod);
    }

    // ========================================================================
    // 10. COMBINATORICS
    // ========================================================================

    static class Combinatorics {
        long[] fact, invFact;
        long mod;
        
        Combinatorics(int n, long mod) {
            this.mod = mod;
            fact = new long[n + 1];
            invFact = new long[n + 1];
            fact[0] = 1;
            
            for (int i = 1; i <= n; i++) {
                fact[i] = (fact[i - 1] * i) % mod;
            }
            
            invFact[n] = power(fact[n], mod - 2, mod);
            for (int i = n - 1; i >= 0; i--) {
                invFact[i] = (invFact[i + 1] * (i + 1)) % mod;
            }
        }
        
        long nCr(int n, int r) {
            if (r > n || r < 0) return 0;
            return (fact[n] * invFact[r] % mod) * invFact[n - r] % mod;
        }
        
        long nPr(int n, int r) {
            if (r > n || r < 0) return 0;
            return (fact[n] * invFact[n - r]) % mod;
        }
    }

    // ========================================================================
    // 11. BIT MANIPULATION
    // ========================================================================

    static boolean isBitSet(long n, int k) {
        return ((n >> k) & 1) == 1;
    }

    static long setBit(long n, int k) {
        return n | (1L << k);
    }

    static long clearBit(long n, int k) {
        return n & ~(1L << k);
    }

    static long toggleBit(long n, int k) {
        return n ^ (1L << k);
    }

    static int countSetBits(long n) {
        return Long.bitCount(n);
    }

    static boolean isPowerOfTwo(long n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    static long turnOffRightmost(long n) {
        return n & (n - 1);
    }

    // ========================================================================
    // 12. GEOMETRY
    // ========================================================================

    static class Point {
        double x, y;
        Point(double x, double y) { this.x = x; this.y = y; }
        
        double distance(Point p) {
            return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
        }
    }

    static double crossProduct(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }

    static boolean onSegment(Point p, Point q, Point r) {
        return q.x <= Math.max(p.x, r.x) && q.x >= Math.min(p.x, r.x) &&
               q.y <= Math.max(p.y, r.y) && q.y >= Math.min(p.y, r.y);
    }

    static boolean doSegmentsIntersect(Point p1, Point q1, Point p2, Point q2) {
        double d1 = crossProduct(p2, q2, p1);
        double d2 = crossProduct(p2, q2, q1);
        double d3 = crossProduct(p1, q1, p2);
        double d4 = crossProduct(p1, q1, q2);
        
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
            ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        }
        
        return onSegment(p1, p2, q1) || onSegment(p1, q2, q1) ||
               onSegment(p2, p1, q2) || onSegment(p2, q1, q2);
    }

    // ========================================================================
    // 13. ARRAY & NUMBER UTILITIES
    // ========================================================================

    static int[] getDigits(long num) {
        if (num == 0) return new int[]{0};
        List<Integer> digits = new ArrayList<>();
        while (num > 0) {
            digits.add((int)(num % 10));
            num /= 10;
        }
        int[] result = new int[digits.size()];
        for (int i = 0; i < digits.size(); i++) result[i] = digits.get(i);
        return result;
    }

    static long sumDigits(long num) {
        long sum = 0;
        while (num > 0) {
            sum += num % 10;
            num /= 10;
        }
        return sum;
    }

    static long reverseNum(long num) {
        long rev = 0;
        while (num > 0) {
            rev = rev * 10 + num % 10;
            num /= 10;
        }
        return rev;
    }

    static boolean isPalindrome(long num) {
        return num == reverseNum(num);
    }

    static String reverseStr(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    static boolean isPalindromeStr(String s) {
        return s.equals(new StringBuilder(s).reverse().toString());
    }

    static Map<Character, Integer> charFreq(String s) {
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : s.toCharArray()) {
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        return freq;
    }

    static long maxArr(long[] arr) {
        long mx = Long.MIN_VALUE;
        for (long x : arr) mx = Math.max(mx, x);
        return mx;
    }

    static long minArr(long[] arr) {
        long mn = Long.MAX_VALUE;
        for (long x : arr) mn = Math.min(mn, x);
        return mn;
    }

    static long sumArr(long[] arr) {
        long sum = 0;
        for (long x : arr) sum += x;
        return sum;
    }

    static long[] prefixSum(long[] arr) {
        long[] pre = new long[arr.length];
        pre[0] = arr[0];
        for (int i = 1; i < arr.length; i++) {
            pre[i] = pre[i - 1] + arr[i];
        }
        return pre;
    }

    static long rangeSum(long[] pre, int l, int r) {
        if (l == 0) return pre[r];
        return pre[r] - pre[l - 1];
    }

    // ========================================================================
    // 14. BINARY SEARCH
    // ========================================================================

    static int bsLeft(int[] arr, int target) {
        int l = 0, r = arr.length;
        while (l < r) {
            int m = l + (r - l) / 2;
            if (arr[m] < target) l = m + 1;
            else r = m;
        }
        return l;
    }

    static int bsRight(int[] arr, int target) {
        int l = 0, r = arr.length - 1, res = -1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (arr[m] <= target) {
                res = m;
                l = m + 1;
            } else {
                r = m - 1;
            }
        }
        return res;
    }

    // ========================================================================
    // 15. MATRIX OPERATIONS
    // ========================================================================

    static long[][] matrixMultiply(long[][] a, long[][] b, long mod) {
        int n = a.length;
        long[][] c = new long[n][n];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    c[i][j] = (c[i][j] + (a[i][k] * b[k][j]) % mod) % mod;
                }
            }
        }
        
        return c;
    }

    static long[][] matrixPower(long[][] a, long p, long mod) {
        int n = a.length;
        long[][] result = new long[n][n];
        
        for (int i = 0; i < n; i++) result[i][i] = 1;
        
        while (p > 0) {
            if ((p & 1) == 1) result = matrixMultiply(result, a, mod);
            a = matrixMultiply(a, a, mod);
            p >>= 1;
        }
        
        return result;
    }

    // ========================================================================
    // 16. LCA & TREE UTILITIES
    // ========================================================================

    static class TreeLCA {
        int n, logn;
        int[][] parent;
        int[] depth;
        List<List<Integer>> adj;
        
        TreeLCA(int n, List<List<Integer>> adj) {
            this.n = n;
            this.adj = adj;
            this.logn = 20;
            this.parent = new int[n][logn];
            this.depth = new int[n];
            dfs(0, -1, 0);
            buildLCA();
        }
        
        void dfs(int u, int p, int d) {
            parent[u][0] = p;
            depth[u] = d;
            for (int v : adj.get(u)) {
                if (v != p) dfs(v, u, d + 1);
            }
        }
        
        void buildLCA() {
            for (int j = 1; j < logn; j++) {
                for (int i = 0; i < n; i++) {
                    if (parent[i][j - 1] != -1) {
                        parent[i][j] = parent[parent[i][j - 1]][j - 1];
                    }
                }
            }
        }
        
        int lca(int u, int v) {
            if (depth[u] < depth[v]) {
                int t = u; u = v; v = t;
            }
            
            for (int i = 0; i < logn; i++) {
                if (((depth[u] - depth[v]) & (1 << i)) != 0) {
                    u = parent[u][i];
                }
            }
            
            if (u == v) return u;
            
            for (int i = logn - 1; i >= 0; i--) {
                if (parent[u][i] != parent[v][i]) {
                    u = parent[u][i];
                    v = parent[v][i];
                }
            }
            
            return parent[u][0];
        }
        
        int distance(int u, int v) {
            int l = lca(u, v);
            return depth[u] + depth[v] - 2 * depth[l];
        }
    }

    // ========================================================================
    // 17. UTILITY FUNCTIONS
    // ========================================================================

    static void printArr(long[] arr) {
        for (long x : arr) out.print(x + " ");
        out.println();
    }

    static void printArr(int[] arr) {
        for (int x : arr) out.print(x + " ");
        out.println();
    }

    static void printMatrix(long[][] matrix) {
        for (long[] row : matrix) {
            for (long x : row) out.print(x + " ");
            out.println();
        }
    }

    static <T> void printList(List<T> list) {
        for (T x : list) out.print(x + " ");
        out.println();
    }

    // ========================================================================
    // QUICK REFERENCE GUIDE
    // ========================================================================
    /*
     * 
     * ========== GRAPH ALGORITHMS ==========
     * DFS: dfs(start, adj, visited)
     * BFS: bfs(start, adj) or bfsDistance(start, adj)
     * Dijkstra: dijkstra(start, adj, n)
     * Floyd-Warshall: floydWarshall(dist, n)
     * Bellman-Ford: bellmanFord(start, edges, n)
     * Topological Sort: topologicalSort(n, adj)
     * SCC: SCCKosaraju
     * Cycle Detection (Undirected): hasCycleUndirected(n, adj)
     * Cycle Detection (Directed): hasCycleDirected(n, adj)
     * MST (Kruskal): kruskal(edges, n)
     * MST (Prim): prim(adj, n)
     * 
     * ========== STRING ALGORITHMS ==========
     * KMP: kmp(text, pattern)
     * Z-Algorithm: zAlgorithm(s)
     * String Hashing: StringHash(s, base, mod)
     * Trie: Trie, TrieNode
     * 
     * ========== DATA STRUCTURES ==========
     * Union-Find: DSU
     * Segment Tree: SegmentTree
     * Fenwick Tree: FenwickTree
     * Graph: Graph
     * TreeLCA: TreeLCA
     * 
     * ========== DYNAMIC PROGRAMMING ==========
     * LCS: lcs(a, b)
     * Edit Distance: editDistance(a, b)
     * 0/1 Knapsack: knapsack(weights, values, capacity)
     * Coin Change: coinChange(coins, amount)
     * LIS: lis(arr)
     * 
     * ========== NUMBER THEORY ==========
     * GCD: gcd(a, b)
     * LCM: lcm(a, b)
     * Prime Check: isPrime(n)
     * Sieve: sieve(n)
     * Prime Factors: primeFactors(n)
     * Divisors: divisors(n)
     * Euler's Totient: phi(n)
     * 
     * ========== MODULAR ARITHMETIC ==========
     * Power: power(base, exp, mod)
     * Modular Inverse: modInverse(a, mod)
     * Safe Operations: modAdd, modSub, modMul, modDiv
     * 
     * ========== BIT MANIPULATION ==========
     * Check Bit: isBitSet(n, k)
     * Set/Clear/Toggle: setBit, clearBit, toggleBit
     * Count Set Bits: countSetBits(n)
     * Power of 2: isPowerOfTwo(n)
     * 
     * ========== UTILITY ==========
     * Get Digits: getDigits(num)
     * Sum/Reverse: sumDigits, reverseNum, reverseStr
     * Palindrome: isPalindrome, isPalindromeStr
     * Array Operations: maxArr, minArr, sumArr, prefixSum
     * Binary Search: bsLeft(arr, target), bsRight(arr, target)
     * Character Frequency: charFreq(s)
     * 
     */
}
