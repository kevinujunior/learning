# Competitive Programming - Problem Type Cheat Sheet

Quick guide to algorithm selection based on problem type

---

## Graph Problems

### Shortest Path Problems

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Single pair, no weights | BFS (0-1 weight) | `bfs()` or `bfsDistance()` |
| Single source, weighted (no negatives) | Dijkstra | `dijkstra()` |
| Single source, with negative edges | Bellman-Ford | `bellmanFord()` |
| All pairs | Floyd-Warshall | `floydWarshall()` |

### Connectivity Problems

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Connected components | DFS or DSU | `dfs()` or `DSU.union()` |
| Cycle detection (undirected) | DFS | `hasCycleUndirected()` |
| Cycle detection (directed) | DFS 3-color | `hasCycleDirected()` |
| Bipartite check | BFS 2-coloring | [implement bipartite check] |

### Spanning Tree Problems

| Problem | Algorithm | Function |
|---------|-----------|----------|
| MST weight/edges | Kruskal or Prim | `kruskal()` or `prim()` |
| Better with weights sorted | Kruskal | `kruskal()` |
| Better with adjacency list | Prim | `prim()` |

### Special Graph Problems

| Problem | Algorithm | Function |
|---------|-----------|----------|
| SCC in directed graph | Kosaraju | `SCCKosaraju.findSCC()` |
| Topological order | DFS | `topologicalSort()` |
| LCA in tree | Binary lifting | `TreeLCA` |
| Bipartite matching | Hungarian algorithm | [not in template] |

---

## Dynamic Programming

### String DP

| Problem | Function |
|---------|----------|
| Longest Common Subsequence | `lcs()` |
| Edit Distance (Levenshtein) | `editDistance()` |
| Longest Palindromic Subsequence | [modify lcs] |
| Longest Repeating Subsequence | [similar to lcs] |

### Array DP

| Problem | Function |
|---------|----------|
| Longest Increasing Subsequence | `lis()` |
| Maximum Subarray | Kadane's algorithm [not in template] |
| Longest Alternating Subsequence | [modify lis] |

### Knapsack DP

| Problem | Function |
|---------|----------|
| 0/1 Knapsack | `knapsack()` |
| Unbounded Knapsack | [modify knapsack] |
| Multiple knapsack | [modify knapsack] |
| Coin change (min coins) | `coinChange()` |

### Interval DP

| Problem | Function |
|---------|----------|
| Matrix chain multiplication | [implement matrixChainMultiplication] |
| Burst balloons | [similar pattern] |

### Digit DP

| Problem | Function |
|---------|----------|
| Count numbers with specific digit property | [implement custom] |
| Examples: count numbers ≤ N with property P | [custom implementation] |

### Tree DP

| Problem | Function |
|---------|----------|
| Maximum path sum | [implement dfsTree with recursion] |
| Diameter of tree | [modify dfsTree] |
| Subtree sum | [modify dfsTree] |

---

## String Problems

### Pattern Matching

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Find all occurrences | KMP or Z-Algorithm | `kmp()` or `zAlgorithm()` |
| Count occurrences | KMP then count | KMP then count results |
| Multiple patterns | Aho-Corasick | [not in template] |

### String Matching (Advanced)

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Longest common prefix | String hashing | `StringHash` |
| Shortest repeating cycle | KMP failure function | KMP function |
| Suffix array problems | Suffix array | [not in template] |

### Anagrams & Permutations

| Problem | Function |
|---------|----------|
| Check if anagrams | Sort and compare or `charFreq()` |
| Group anagrams | `charFreq()` with sorting |
| Generate permutations | [use Collections.sort with nextPermutation] |

### Palindromes

| Problem | Function |
|---------|----------|
| Is palindrome | `isPalindromeStr()` |
| Longest palindromic substring | [implement] |
| Minimum cuts | DP [not in template] |

### Dictionary/Autocomplete

| Problem | Function |
|---------|----------|
| Word existence | `Trie.search()` |
| Prefix search | `Trie.startsWith()` |
| Autocomplete | Trie with DFS |
| Spell correction | Trie with edit distance |

---

## Number Problems

### Prime Numbers

| Problem | Function |
|---------|----------|
| Check if prime | `isPrime()` |
| Count primes up to N | `sieve()` |
| Prime factorization | `primeFactors()` |
| Sieve with additional info | [extend sieve()] |

### Divisors & Factors

| Problem | Function |
|---------|----------|
| Get all divisors | `divisors()` |
| Count divisors | [modify divisors] |
| GCD/LCM | `gcd()`, `lcm()` |
| Euler's totient | `phi()` |

### Modular Arithmetic

| Problem | Function |
|---------|----------|
| Large exponentiation | `power(base, exp, mod)` |
| Modular inverse | `modInverse(a, mod)` |
| Division in modulo | `modDiv(a, b, mod)` |
| Safe operations | `modAdd`, `modSub`, `modMul` |

### Combinatorics

| Problem | Function |
|---------|----------|
| nCr, nPr | `Combinatorics.nCr()` or `.nPr()` |
| Catalan numbers | [use combinatorics] |
| Derangements | [use combinatorics] |

---

## Array Problems

### Range Queries

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Static array, sum queries | Prefix sum | `prefixSum()` + `rangeSum()` |
| Dynamic array, point updates | Segment Tree or Fenwick Tree | `SegmentTree` or `FenwickTree` |
| Range updates | Segment Tree with lazy propagation | [extend SegmentTree] |
| 2D range queries | 2D Segment Tree | [not in template] |

### Inversions & Order Stats

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Count inversions | Merge sort or Fenwick Tree | Merge sort or `FenwickTree` |
| K-th smallest | Quickselect | [not in template] |
| Median in stream | Two heaps | [not in template] |

### Subarray Problems

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Maximum subarray sum | Kadane | [not in template] |
| Max XOR subarray | Trie of bits | [use trie of bits] |
| All subarrays with property | Sliding window or two pointers | [implement] |
| Subarray sum equals K | Hash map | [not in template] |

---

## 2D Grid Problems

### Traversal

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Distance from single source | BFS | `bfs2D()` |
| Distance from multiple sources | Multi-source BFS | `multiSourceBFS()` |
| Connected components in grid | DFS on grid cells | DFS on grid |
| Number of islands | DFS or BFS on grid | DFS or BFS |

### Pathfinding

| Problem | Algorithm | Function |
|---------|-----------|----------|
| Shortest path (unweighted) | BFS | `bfs2D()` |
| With obstacles | BFS with obstacle check | BFS with check |
| With weights | Dijkstra adapted for grid | Dijkstra |

### Special Grid Problems

| Problem | Algorithm |
|---------|-----------|
| Rotting oranges | Multi-source BFS |
| Knight moves | BFS from source |
| Surrounded regions | DFS or BFS |

---

## Bit Manipulation Problems

### Single Number

| Problem | Function |
|---------|----------|
| Find single non-duplicate | `xorAll()` |
| Find two non-duplicates | XOR then partition |
| Find single with duplicates | Bit counting [implement] |

### Subset Problems

| Problem | Function |
|---------|----------|
| Generate all subsets | Bit iteration [not in template] |
| Maximum XOR pair | Trie of bits [extend Trie] |
| Subset sum | DP [not in template] |

---

## Geometry Problems

### Distance/Length

| Problem | Function |
|---------|----------|
| Distance between points | `Point.distance()` |
| Perimeter/Area | [implement geometry formulas] |
| Closest points | Divide and conquer [not in template] |

### Orientation/Intersection

| Problem | Function |
|---------|----------|
| Segment intersection | `doSegmentsIntersect()` |
| Point in polygon | Ray casting [not in template] |
| Convex hull | Graham scan [not in template] |

---

## Special Algorithms

### Union-Find Uses

| Problem | Function |
|---------|----------|
| Connected components | `DSU.union()` + `DSU.find()` |
| Cycle detection | Check if edge connects same component |
| Kruskal's MST | `kruskal()` [uses DSU internally] |
| LCA with weights | Weighted DSU [extend DSU] |

### Binary Search Uses

| Problem | Function |
|---------|----------|
| Find exact element | `bsLeft()` or `bsRight()` |
| Find first occurrence ≥ X | `bsLeft(arr, X)` |
| Find last occurrence ≤ X | `bsRight(arr, X)` |
| Binary search on answer | [search on answer value] |

### Segment Tree Uses

| Problem | Function |
|---------|----------|
| Range sum queries | `SegmentTree.query()` |
| Point updates | `SegmentTree.update()` |
| Range min/max | [modify SegmentTree] |
| Lazy propagation | [extend SegmentTree] |

### Fenwick Tree Uses

| Problem | Function |
|---------|----------|
| Prefix sums | `FenwickTree.query()` |
| Range sum with point updates | `FenwickTree.rangeQuery()` |
| Inversions counting | [use with DFS/merge sort] |
| Coordinate compression | [use before FenwickTree] |

---

## Two Pointers Technique

| Problem | Implementation |
|---------|-----------------|
| Pair sum | `twoPointerSum()` |
| Container with most water | [implement] |
| Trapping rain water | [implement] |
| Sliding window maximum | [implement] |
| Remove duplicates | [implement] |

---

## Greedy Problems

| Problem | Approach |
|---------|----------|
| Activity selection | Sort by end time |
| Huffman coding | Min heap |
| Interval scheduling | Sort by start/end time |
| Fractional knapsack | Sort by value/weight ratio |
| Jumping game | [implement] |

---

## Template Selection Matrix

### Quick Problem → Algorithm Mapping

| Problem | Algorithm | Code |
|---------|-----------|------|
| Find shortest path in unweighted graph | BFS | `bfs()` or `bfsDistance()` |
| Find shortest path with weights, no negatives | Dijkstra | `dijkstra()` |
| Find shortest path with negative weights | Bellman-Ford | `bellmanFord()` |
| Find connected components | DFS or DSU | `dfs()` or `DSU` |
| Check if graph has cycle | DFS with color | `hasCycleDirected()` or `hasCycleUndirected()` |
| Find LCS of two strings | DP | `lcs()` |
| Find edit distance | DP | `editDistance()` |
| Find LIS | DP | `lis()` |
| 0/1 Knapsack problem | DP | `knapsack()` |
| Coin change (minimum coins) | DP | `coinChange()` |
| Find all prime factors | Trial division | `primeFactors()` |
| Sieve of primes | Sieve | `sieve()` |
| Find all divisors | Trial division | `divisors()` |
| Pattern matching in string | KMP or Z-Algorithm | `kmp()` or `zAlgorithm()` |
| Word search in dictionary | Trie | `Trie.search()` |
| Range sum query (static) | Prefix sum | `prefixSum()` + `rangeSum()` |
| Range sum query (dynamic) | Segment Tree or Fenwick Tree | `SegmentTree` or `FenwickTree` |
| MST of graph | Kruskal or Prim | `kruskal()` or `prim()` |
| Find SCC | Kosaraju | `SCCKosaraju.findSCC()` |
| Find LCA | Binary lifting | `TreeLCA.lca()` |
| Large number exponentiation | Modular exponentiation | `power(base, exp, mod)` |
| Calculate nCr | Precomputed factorials | `Combinatorics.nCr()` |
| Find XOR of all elements | XOR property | `xorAll()` |
| Check power of 2 | Bit trick | `isPowerOfTwo()` |
| Count set bits | Bit counting | `countSetBits()` |

---

## Common Pitfalls

1. **DFS/BFS on large graphs** → Stack overflow → Use iterative BFS
2. **Integer overflow** → Use long or modular arithmetic
3. **Negative modulo** → Use `(x % mod + mod) % mod`
4. **Not resetting global variables** → Reset between test cases
5. **0-indexed vs 1-indexed confusion** → Be careful with array access
6. **Missing edge cases** → Test with empty, single element, large values
7. **Wrong time complexity** → Choose algorithm based on constraints
8. **Memory limit exceeded** → Watch array sizes, use space-efficient DS
9. **TLE on large inputs** → Use fast I/O, optimize algorithms
10. **Integer comparison in binary search** → Use int comparison correctly

---

## Common Constraints to Algorithm Mapping

| Constraint | Algorithm | Time Complexity |
|-----------|-----------|-----------------|
| N ≤ 1,000 | DP, Floyd-Warshall | O(N²) |
| N ≤ 10,000 | Sorting, DP | O(N² log N), O(N² DP) |
| N ≤ 100,000 | Sorting, Dijkstra, Segment Tree | O(N log N) |
| N ≤ 1,000,000 | Linear, Linearithmic | O(N), O(N log N) |
| N ≤ 10^9 | Binary search, Factorization | O(log N), O(√N) |
| V + E ≤ 10^5 | All graph algorithms | O(V+E) + algo |
| V * E ≤ 10^8 | Dijkstra, BFS/DFS | O(E log V), O(V+E) |
| N * M ≤ 10^6 | 2D DP, 2D grid | O(N*M) |

---

## How to Use This Guide

1. **Identify your problem type** from the sections above
2. **Find the algorithm** recommended for your problem
3. **Look up the function** in UltimateTemplate.java
4. **Copy the code** and integrate into your solution
5. **Test** with provided examples
6. **Submit!**

---

**Remember:** When in doubt, check the Template Selection Matrix!