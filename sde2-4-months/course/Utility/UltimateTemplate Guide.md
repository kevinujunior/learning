# ULTIMATE JAVA COMPETITIVE PROGRAMMING TEMPLATE
## Complete Reference & Usage Guide

---

## TABLE OF CONTENTS
1. [Graph Algorithms](#graph-algorithms)
2. [String Algorithms](#string-algorithms)
3. [Data Structures](#data-structures)
4. [Dynamic Programming](#dynamic-programming)
5. [Number Theory](#number-theory)
6. [Modular Arithmetic](#modular-arithmetic)
7. [Combinatorics](#combinatorics)
8. [Bit Manipulation](#bit-manipulation)
9. [Geometry](#geometry)
10. [Trees & LCA](#trees--lca)
11. [Array Utilities](#array-utilities)
12. [Binary Search](#binary-search)
13. [Quick Reference](#quick-reference)

---

## GRAPH ALGORITHMS

### 1.1 Depth-First Search (DFS)

**Use for:**
- Connected components
- Topological sorting
- Cycle detection
- Backtracking problems
- Path finding in graphs

**Time Complexity:** O(V + E)

```java
// Basic DFS
List<List<Integer>> adj = new ArrayList<>();
boolean[] visited = new boolean[n];
dfs(start, adj, visited);

// DFS on Tree (with parent)
dfsTree(start, -1, adj, visited);

// Topological Sort
topologicalSort(n, adj);
```

### 1.2 Breadth-First Search (BFS)

**Use for:**
- Shortest path (unweighted)
- Level-order traversal
- Bipartite checking
- Connected components
- Multi-source problems

**Time Complexity:** O(V + E)

```java
// Basic BFS
bfs(start, adj);

// BFS with distance
int[] dist = bfsDistance(start, adj);

// BFS on 2D Grid
int[][] dist = bfs2D(grid, sr, sc, blockChar);

// Multi-source BFS
int[][] dist = multiSourceBFS(grid, sourceChar);
```

### 1.3 Shortest Path Algorithms

**Dijkstra's Algorithm**
- **Use for:** Single-source shortest path in weighted graphs (no negative edges)
- **Time:** O((V + E) log V)

```java
long[] dist = dijkstra(start, adj, n);
// dist[i] = shortest distance from start to node i
```

**Floyd-Warshall**
- **Use for:** All-pairs shortest path
- **Time:** O(V³)

```java
floydWarshall(dist, n);
// dist[i][j] = shortest path from i to j
```

**Bellman-Ford**
- **Use for:** Single-source shortest path with negative edges, detects negative cycles
- **Time:** O(V*E)

```java
long[] dist = bellmanFord(start, edges, n);
// Also detects and reports negative cycles
```

### 1.4 Minimum Spanning Tree

**Kruskal's Algorithm** (using DSU)
- **Time:** O(E log E)

```java
long mstWeight = kruskal(edges, n);
// Returns total weight of MST, -1 if not connected
```

**Prim's Algorithm**
- **Time:** O(E log V)

```java
long mstWeight = prim(adj, n);
// Returns total weight of MST
```

### 1.5 Strongly Connected Components (SCC)

**Kosaraju's Algorithm**
- **Time:** O(V + E)
- **Use for:** Finding SCCs in directed graphs

```java
SCCKosaraju scc = new SCCKosaraju(n);
scc.addEdge(u, v);
int sccCount = scc.findSCC();
```

### 1.6 Cycle Detection

**Undirected Graph:**
```java
boolean hasCycle = hasCycleUndirected(n, adj);
```

**Directed Graph:**
```java
boolean hasCycle = hasCycleDirected(n, adj);
// Uses 3-coloring: white (0), gray (1), black (2)
```

---

## STRING ALGORITHMS

### 2.1 KMP (Knuth-Morris-Pratt)

**Use for:** Pattern matching, finding all occurrences of pattern in text

**Time Complexity:** O(n + m) where n = text length, m = pattern length

```java
List<Integer> matches = kmp(text, pattern);
// Returns list of starting indices of all matches
```

**Example:**
```java
List<Integer> positions = kmp("ABABCABAB", "ABAB");
// Returns [0, 5]
```

### 2.2 Z-Algorithm

**Use for:** Pattern matching, finding all occurrences

**Time Complexity:** O(n)

```java
int[] z = zAlgorithm(s);
// z[i] = length of longest substring starting from s[i] which is also prefix
```

### 2.3 String Hashing

**Use for:** Quick equality checks, pattern matching with hash comparison

```java
StringHash hash = new StringHash(s, base, mod);
long h1 = hash.getHash(l, r); // Hash of s[l:r+1]
```

**Common values:**
- base = 31 or 37 (for strings)
- mod = 10^9 + 7 or 10^9 + 9

### 2.4 Trie (Prefix Tree)

**Use for:**
- Autocomplete
- IP routing
- Spell checking
- Dictionary operations

**Time:** O(m) for most operations where m = word length

```java
Trie trie = new Trie();
trie.insert("hello");
trie.search("hello");        // true
trie.startsWith("hel");      // true
trie.countWordsWithPrefix("hel");  // 1
trie.delete("hello");
```

---

## DATA STRUCTURES

### 3.1 Union-Find (DSU)

**Use for:**
- Connected components
- Cycle detection
- Kruskal's algorithm
- LCA queries (with binary lifting)

**Time:** O(α(n)) amortized per operation

```java
DSU dsu = new DSU(n);
dsu.union(x, y);     // Unite sets containing x and y
dsu.find(x);         // Find root of x
dsu.getSize(x);      // Get size of component containing x
```

### 3.2 Segment Tree

**Use for:**
- Range queries (sum, min, max, etc.)
- Point updates
- Range updates (with lazy propagation)

**Time:** O(log n) per query/update

```java
SegmentTree st = new SegmentTree(arr);
long sum = st.query(l, r);      // Sum in range [l, r]
st.update(idx, newValue);       // Update single element
```

### 3.3 Fenwick Tree (Binary Indexed Tree)

**Use for:**
- Prefix sums
- Range queries with point updates
- Counting inversions

**Time:** O(log n) per operation

```java
FenwickTree ft = new FenwickTree(n);
ft.update(idx, delta);          // Add delta to position idx
long sum = ft.query(idx);       // Sum from 1 to idx
long rangeSum = ft.rangeQuery(l, r);  // Sum in range [l, r]
```

### 3.4 Graph Representation

```java
Graph g = new Graph(n);
g.addEdge(u, v, weight);           // Directed edge
g.addUndirectedEdge(u, v, weight); // Undirected edge
```

### 3.5 Trie Node

Already covered in String Algorithms section.

---

## DYNAMIC PROGRAMMING

### 4.1 Longest Common Subsequence (LCS)

**Use for:** Finding longest common subsequence of two strings

**Time:** O(m * n)

```java
int length = lcs("ABCDGH", "AEDFHR");
// Returns 3 (ADH is one possible LCS)
```

### 4.2 Edit Distance (Levenshtein)

**Use for:** Finding minimum edits to convert one string to another

**Time:** O(m * n)

```java
int distance = editDistance("horse", "ros");
// Returns 3 (insert h, delete e)
```

### 4.3 0/1 Knapsack

**Use for:** Selecting items with maximum value within weight capacity

**Time:** O(n * W) where W = capacity

```java
int maxValue = knapsack(weights, values, capacity);
```

### 4.4 Coin Change

**Use for:** Minimum coins needed to make a given amount

**Time:** O(n * amount)

```java
int minCoins = coinChange(coins, amount);
// Returns -1 if impossible
```

### 4.5 Longest Increasing Subsequence (LIS)

**Use for:** Finding longest increasing subsequence

**Time:** O(n²) or O(n log n) with binary search

```java
int length = lis(arr);
```

---

## NUMBER THEORY

### 5.1 GCD & LCM

```java
long g = gcd(48, 18);      // 6
long l = lcm(48, 18);      // 144
```

### 5.2 Prime Checking

```java
boolean prime = isPrime(17);  // true
```

### 5.3 Sieve of Eratosthenes

**Time:** O(n log log n)

```java
boolean[] primes = sieve(100);
// primes[i] = true if i is prime
```

### 5.4 Prime Factorization

```java
List<Long> factors = primeFactors(12);
// [2, 2, 3]
```

### 5.5 Divisors

```java
List<Long> divs = divisors(12);
// [1, 2, 3, 4, 6, 12]
```

### 5.6 Euler's Totient Function

**φ(n) = count of numbers ≤ n that are coprime with n**

```java
long phiValue = phi(12);  // 4
```

---

## MODULAR ARITHMETIC

### 6.1 Modular Exponentiation

**Use for:** Computing (a^b) % mod efficiently

**Time:** O(log b)

```java
long result = power(2, 100, MOD);
// Returns 2^100 % MOD
```

### 6.2 Modular Inverse

**Use for:** Computing (a^-1) % mod

```java
long inv = modInverse(7, MOD);
// Works when MOD is prime
```

### 6.3 Safe Modular Operations

**Prevent overflow in large computations:**

```java
long sum = modAdd(a, b, MOD);
long diff = modSub(a, b, MOD);
long prod = modMul(a, b, MOD);
long quot = modDiv(a, b, MOD);  // a / b % MOD = a * b^-1 % MOD
```

---

## COMBINATORICS

### 7.1 Precomputed Factorials

**Use for:** Computing nCr, nPr efficiently

```java
Combinatorics comb = new Combinatorics(100000, MOD);
long nCr = comb.nCr(100, 50);
long nPr = comb.nPr(100, 50);
```

---

## BIT MANIPULATION

### 8.1 Basic Operations

```java
isBitSet(13, 2);       // Check if 2nd bit is set
setBit(5, 1);          // Set 1st bit
clearBit(5, 0);        // Clear 0th bit
toggleBit(5, 0);       // Toggle 0th bit
countSetBits(13);      // Count number of 1s
isPowerOfTwo(8);       // true
turnOffRightmost(12);  // 12 & (12-1) = 8
```

### 8.2 Bit Tricks

```
x & (x-1)              // Turn off rightmost set bit
x & 1                  // Check if odd
x | (x-1)              // Turn on all bits below rightmost
(x & -x)               // Isolate rightmost set bit
~x                     // NOT (flip all bits)
```

---

## GEOMETRY

### 9.1 Point & Distance

```java
Point a = new Point(0, 0);
Point b = new Point(3, 4);
double dist = a.distance(b);  // 5.0
```

### 9.2 Cross Product & Orientation

```java
double cp = crossProduct(a, b, c);  // Check orientation
```

### 9.3 Segment Intersection

```java
boolean intersect = doSegmentsIntersect(p1, q1, p2, q2);
```

---

## TREES & LCA

### 10.1 Lowest Common Ancestor (LCA)

**Use for:** Finding LCA in O(log n) time with O(n log n) preprocessing

```java
TreeLCA lca = new TreeLCA(n, adj);
int ancestor = lca.lca(u, v);
int dist = lca.distance(u, v);
```

---

## ARRAY UTILITIES

### 11.1 Digit Extraction

```java
int[] digits = getDigits(12345);    // [5, 4, 3, 2, 1]
long sum = sumDigits(12345);        // 15
long reversed = reverseNum(12345);  // 54321
```

### 11.2 String Operations

```java
String rev = reverseStr("hello");        // "olleh"
boolean pal = isPalindromeStr("racecar"); // true
Map<Character, Integer> freq = charFreq("aabbcc");
```

### 11.3 Array Operations

```java
long max = maxArr(arr);
long min = minArr(arr);
long sum = sumArr(arr);
long[] prefix = prefixSum(arr);
long rangeSum = rangeSum(prefix, l, r);
```

---

## BINARY SEARCH

### 12.1 Binary Search Variants

**Find leftmost position where value >= target:**
```java
int pos = bsLeft(arr, target);
```

**Find rightmost position where value <= target:**
```java
int pos = bsRight(arr, target);
```

---

## QUICK REFERENCE

### Time Complexities

| Algorithm | Best | Average | Worst | Space |
|-----------|------|---------|-------|-------|
| DFS/BFS | O(V+E) | O(V+E) | O(V+E) | O(V) |
| Dijkstra | O((V+E)logV) | O((V+E)logV) | O((V+E)logV) | O(V) |
| Floyd-Warshall | O(V³) | O(V³) | O(V³) | O(V²) |
| Bellman-Ford | O(VE) | O(VE) | O(VE) | O(V) |
| KMP | O(n+m) | O(n+m) | O(n+m) | O(m) |
| Z-Algorithm | O(n) | O(n) | O(n) | O(n) |
| Segment Tree | O(logN) | O(logN) | O(logN) | O(N) |
| Fenwick Tree | O(logN) | O(logN) | O(logN) | O(N) |
| DSU | O(α(N)) | O(α(N)) | O(α(N)) | O(N) |
| Trie Insert | O(m) | O(m) | O(m) | O(m) |
| Binary Search | O(logN) | O(logN) | O(logN) | O(1) |

### Common MOD Values

```java
static final long MOD = 1_000_000_007L;   // 10^9 + 7 (most common)
static final long MOD2 = 998_244_353L;    // 998244353 (NTT friendly)
```

### Direction Arrays for Grid

```java
// 4-directional
int[][] DIRS4 = {{0,1}, {1,0}, {0,-1}, {-1,0}};

// 8-directional (with diagonals)
int[][] DIRS8 = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
```

### Problem Type Mapping

| Problem Type | Algorithm |
|--------------|-----------|
| Shortest Path (no negatives) | Dijkstra |
| Shortest Path (with negatives) | Bellman-Ford |
| All-Pairs Shortest Path | Floyd-Warshall |
| Connected Components | DFS/BFS or DSU |
| Cycle Detection | DFS or DSU |
| Topological Order | DFS |
| MST | Kruskal or Prim |
| Pattern Matching | KMP or Z-Algorithm |
| Word Dictionary | Trie |
| LCS | DP |
| Edit Distance | DP |
| Range Queries | Segment Tree or Fenwick Tree |
| LCA | Binary Lifting |
| Permutations/Combinations | Combinatorics with factorials |

---

## TEMPLATE USAGE EXAMPLE

```java
public class Solution {
    static FastReader sc = new FastReader();
    static PrintWriter out = new PrintWriter(System.out);
    
    public static void main(String[] args) {
        solve();
        out.flush();
    }
    
    static void solve() {
        int n = sc.nextInt();
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        
        for (int i = 0; i < n - 1; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        
        // Use algorithms from UltimateTemplate
        boolean[] visited = new boolean[n];
        dfs(0, adj, visited);
    }
}
```

---

## IMPORTANT TIPS

1. **Always flush output:** `out.flush();`
2. **Watch for overflow:** Use modular arithmetic for large numbers
3. **Index carefully:** Arrays are 0-indexed, trees are often 1-indexed
4. **MOD operations:** `(a % mod + mod) % mod` for negative numbers
5. **Memory:** Watch out for large arrays/matrices
6. **TLE prevention:** Use fast I/O, optimize algorithms
7. **Edge cases:** Empty input, single element, negative numbers
8. **Test locally:** Before submitting

---

Good luck in competitive programming! 🚀
