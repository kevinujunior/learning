# Java CP Cheat Sheet: Ultra-Short Syntax

## Contents
1. [Arrays & 2D Matrices](#1-arrays--2d-matrices)
2. [Strings & StringBuilder](#2-strings--stringbuilder)
3. [List (ArrayList / LinkedList)](#3-list-arraylist--linkedlist)
4. [Set (HashSet / LinkedHashSet)](#4-set-hashset--linkedhashset)
5. [TreeSet (Sorted Set)](#5-treeset-sorted-set)
6. [Map (HashMap / LinkedHashMap)](#6-map-hashmap--linkedhashmap)
7. [TreeMap (Sorted Map)](#7-treemap-sorted-map)
8. [Queue, Stack & Deque (ArrayDeque)](#8-queue-stack--deque-arraydeque)
9. [PriorityQueue (Min / Max Heap)](#9-priorityqueue-min--max-heap)
10. [Custom Comparators & Objects (Comparable)](#10-custom-comparators--objects-comparable)
11. [Functional Shortcodes & One-Liners (Java 8+)](#11-functional-shortcodes--one-liners-java-8)
12. [Conversions (Primitives, Strings & Numbers)](#12-conversions-primitives-strings--numbers)
13. [Math, Overflow & Misc CP Tricks](#13-math-overflow--misc-cp-tricks)
14. [Lambdas, streams, Optional](#14-lambdas-streams-optional)

---

## 1. Arrays & 2D Matrices

### Initialization & Filling
```java
int[] a = new int[n];
boolean[] b = new boolean[n];
int[] a = {1, 2, 3};
int[][] g = new int[m][n];
int[][] g = {
    {1, 2, 3},
    {4, 5, 6}
};
int[][] jagged = new int[n][];

Arrays.fill(a, 7);
Arrays.fill(a, l, r, 7); // [l, r)
for (int[] row : g) Arrays.fill(row, -1);
```

### Iteration
```java
for (int i = 0; i < a.length; i++) { }
for (int x : a) { }
for (int[] row : g) for (int val : row) { }
```

### Sorting, Searching, Copying & Ops
```java
Arrays.sort(a);
Arrays.sort(a, start, end);
Arrays.sort(g, (x, y) -> Integer.compare(x[0], y[0])); // 2D sort by col 0

// Descending sort for primitives (convert to Integer[] first)
Integer[] A = Arrays.stream(a).boxed().toArray(Integer[]::new);
Arrays.sort(A, Collections.reverseOrder());

int idx = Arrays.binarySearch(a, target); // Array MUST be sorted

int[] b = Arrays.copyOf(a, a.length);
int[] b = Arrays.copyOfRange(a, l, r);
int[] b = a.clone();

// Reverse (for Integer[])
Collections.reverse(Arrays.asList(A));

// Swap
int t = a[i]; a[i] = a[j]; a[j] = t;
```

---

## 2. Strings & StringBuilder

### Initialization & Conversion
```java
String s = "abc";
char[] chars = s.toCharArray();
String s2 = new String(chars);
String s3 = String.valueOf(chars);
StringBuilder sb = new StringBuilder();
StringBuilder sb2 = new StringBuilder("hello");
StringBuilder sbCap = new StringBuilder(50);
```

### Iteration & Ops
```java
for (int i = 0; i < s.length(); i++) char c = s.charAt(i);
for (char c : s.toCharArray()) { }

s.charAt(i);
s.substring(l, r); // [l, r)
s.indexOf('c'); s.indexOf("sub");
s.contains("ab"); s.startsWith("pre");
s.replace('a', 'b');
s.split(",");
String.join(",", arr);
String.join(",", list);
List<String> listFromArray = Arrays.asList(strArray);
String[] strArrayFromList = list.toArray(new String[0]);
Object[] objectArray = list.toArray();
s.strip();
s.repeat(3); // Java 11+

sb.append("x"); sb.setCharAt(i, 'z'); sb.reverse();
```

---

## 3. List (ArrayList / LinkedList)

### Initialization
```java
List<Integer> list = new ArrayList<>();
List<Integer> listCap = new ArrayList<>(100);
List<Integer> listVals = new ArrayList<>(List.of(1, 2, 3));
List<Integer> modifiableList = new ArrayList<>(Arrays.asList(1, 2, 3));
List<Integer> l = Arrays.asList(1, 2, 3); // fixed-size
List<List<Integer>> adj = new ArrayList<>(); // Graph adjacency list
for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
```

### Iteration & Safe Removal
```java
for (int i = 0; i < list.size(); i++) list.get(i); // ArrayList only!
for (int x : list) { }

// Safe removal
Iterator<Integer> it = list.iterator();
while (it.hasNext()) { if (it.next() == val) it.remove(); }
list.removeIf(x -> x == val);
```

### Sorting, Searching & Pitfalls
```java
Collections.sort(list);
list.sort(null); // ascending
list.sort((a, b) -> a - b);
list.sort((a, b) -> b - a);
list.sort(Comparator.reverseOrder()); // descending
Collections.reverse(list);

int idx = Collections.binarySearch(list, target);

// PITFALL: list.remove(0) removes by index 0! 
// Use list.remove(Integer.valueOf(val)) to remove by value.
```

---

## 4. Set (HashSet / LinkedHashSet)

### Initialization & Ops
```java
Set<String> set = new HashSet<>();
Set<String> immSet = Set.of("A", "B", "C"); // Immutable
Set<String> mutSet = new HashSet<>(Set.of("A", "B", "C")); // Mutable from immutable
Set<Integer> setFromList = new HashSet<>(Arrays.asList(1, 2, 3));
Set<Integer> capacitySet = new HashSet<>(expectedSize);
Set<String> orderedSet = new LinkedHashSet<>(); // maintains insertion order

set.add("A"); // returns false if duplicate
set.contains("A");
set.remove("A");

// Set Math
s1.addAll(s2);   // Union
s1.retainAll(s2); // Intersection
s1.removeAll(s2); // Difference
```

### Iteration & Sorting
```java
for (String x : set) { }

// Convert to sorted list or TreeSet
List<String> sortedList = new ArrayList<>(set); sortedList.sort(null);
List<Integer> listFromSet = new ArrayList<>(set);
```

---

## 5. TreeSet (Sorted Set)

### Initialization
```java
TreeSet<Integer> ts = new TreeSet<>(); // sorted O(log N)
TreeSet<Integer> descTs = new TreeSet<>(Comparator.reverseOrder());
```

### Iteration & Navigation ($O(\log N)$)
```java
for (int x : ts) { }
for (int x : ts.descendingSet()) { } // reverse order

ts.first(); ts.last();
ts.floor(x);   // greatest <= x
ts.ceiling(x); // smallest >= x
ts.lower(x);   // greatest < x
ts.higher(x);  // smallest > x
ts.pollFirst(); ts.pollLast(); // remove & return min/max
```

---

## 6. Map (HashMap / LinkedHashMap)

### Initialization
```java
Map<String, Integer> map = new HashMap<>(expectedSize);
Map<String, Integer> lhm = new LinkedHashMap<>(); // insertion order
Map<Integer, String> mutableMap = new HashMap<>(Map.of(1, "A", 2, "B")); // < 10 pairs
Map<Integer, String> mapEntries = Map.ofEntries(
    entry(1, "Value1"),
    entry(2, "Value2")
);
```

### Iteration & Ops
```java
map.put("A", 1);
map.putIfAbsent("A", 1);
map.getOrDefault("A", 0);
map.containsKey("A");
map.containsValue(1);

// Fastest iteration
for (var e : map.entrySet()) {
    var k = e.getKey();
    var v = e.getValue();
}
map.forEach((k, v) -> { });
```

---

## 7. TreeMap (Sorted Map)

### Initialization
```java
TreeMap<Integer, String> tm = new TreeMap<>(); // sorted by key O(log N)
```

### Iteration & Navigation ($O(\log N)$)
```java
for (var k : tm.keySet()) { }

tm.firstKey(); tm.lastKey();
tm.floorKey(k);   // greatest key <= k
tm.ceilingKey(k); // smallest key >= k
tm.lowerKey(k);   // greatest key < k
tm.higherKey(k);  // smallest key > k
tm.pollFirstEntry(); tm.pollLastEntry();
```

---

## 8. Queue, Stack & Deque (ArrayDeque)

### Initialization
```java
Queue<Integer> queue = new LinkedList<>();  // Queue
Queue<Integer> q = new ArrayDeque<>();      // FIFO Queue
Deque<Integer> stack = new ArrayDeque<>();  // LIFO Stack (prefer over legacy Stack)
Deque<Integer> dq = new ArrayDeque<>();     // Double-Ended Queue
```

### Iteration & Ops
```java
// Queue
q.offer(x); q.poll(); q.peek();

// Stack
stack.push(x); stack.pop(); stack.peek();

// Deque
dq.offerFirst(x); dq.offerLast(x);
dq.pollFirst(); dq.pollLast();

// Iteration (Non-destructive)
for (int x : dq) { }

// Destructive Drain (BFS / Processing)
while (!q.isEmpty()) { int cur = q.poll(); }
```

---

## 9. PriorityQueue (Min / Max Heap)

### Initialization
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min-heap default
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
PriorityQueue<Integer> maxH = new PriorityQueue<>(Collections.reverseOrder());
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> Integer.compare(x[1], y[1]));
```

### Iteration & Ops
```java
pq.offer(x); // O(log N)
pq.poll();   // O(log N) - removes min/max
pq.peek();   // O(1)

// PITFALL: for (int x : pq) does NOT iterate in sorted heap order! 
// You must poll() in a while loop to extract elements in sorted order.
while (!pq.isEmpty()) { int val = pq.poll(); }
```

---

## 10. Custom Comparators & Objects (Comparable)

### Custom Class Template (`Comparable` + `equals`/`hashCode`)
```java
static class Pt implements Comparable<Pt> {
    int x, y;
    Pt(int x, int y) { this.x = x; this.y = y; }

    @Override
    public int compareTo(Pt o) {
        return this.x != o.x ? Integer.compare(this.x, o.x) : Integer.compare(this.y, o.y);
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pt p)) return false;
        return x == p.x && y == p.y;
    }
    @Override
    public int hashCode() { return Objects.hash(x, y); }
}
```


```java
// Sorts products by price in ascending order
class PriceComparator implements Comparator<Product> {
    @Override
    public int compare(Product p1, Product p2) {
        return Double.compare(p1.price, p2.price);
    }
}

// Usage:
// Collections.sort(productList, new PriceComparator());
// PriorityQueue<Product> pq = new PriorityQueue<>(new PriceComparator());
```

### Lambda & Chained Comparators
```java
// Lambda (y desc, then x asc)
Comparator<Pt> cmp = (a, b) -> a.y != b.y ? Integer.compare(b.y, a.y) : Integer.compare(a.x, b.x);

// Method Chaining
Comparator<Pt> chain = Comparator.comparingInt((Pt p) -> p.y).reversed().thenComparingInt(p -> p.x);

// Pass to DS
TreeSet<Pt> ts = new TreeSet<>(cmp);
PriorityQueue<Pt> customPq = new PriorityQueue<>(cmp);
```

---

## 11. Functional Shortcodes & One-Liners (Java 8+)

```java
// Conversions: Arrays <-> Collections
List<Integer> listConv = Arrays.stream(nums).boxed().collect(Collectors.toList());
int[] numsArr = listConv.stream().mapToInt(i -> i).toArray();
Set<Integer> setConv = new HashSet<>(listConv);

// Stream Filters & Streams Ops
int[] uniq = Arrays.stream(a).distinct().toArray();
int[] pos  = Arrays.stream(a).filter(x -> x > 0).toArray();
int[] sq   = Arrays.stream(a).map(x -> x * x).toArray();

// Max / Min / Sum
int max = Arrays.stream(nums).max().getAsInt();
int min = Arrays.stream(nums).min().getAsInt();
int mx = Collections.max(listConv);
int mn = Collections.min(listConv);
long sum = Arrays.stream(nums).summaryStatistics().getSum();
long sum2 = Arrays.stream(nums).sum();

// Frequency Map 1-liner
map.merge(key, 1, Integer::sum);
map.put(key, map.getOrDefault(key, 0) + 1);
Map<Integer, Long> freq = Arrays.stream(a).boxed()
    .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

// Map Atomic Compute
map.computeIfAbsent(key, k -> new ArrayList<>()).add(val);

// IntStream Ranges
int sumRange = IntStream.rangeClosed(1, n).sum();

// Collection nCopies
List<Integer> lCopies = new ArrayList<>(Collections.nCopies(n, 0));

// RemoveIf & ForEach
listConv.removeIf(n -> n % 2 == 0);
listConv.forEach(System.out::println);
```

---

## 12. Conversions (Primitives, Strings & Numbers)

```java
// String -> Primitive
int i = Integer.parseInt(str);
long l = Long.parseLong(str);
double d = Double.parseDouble(str);

// Primitive -> String
String str1 = "" + i;
String str2 = String.valueOf(i);
String str3 = Integer.toString(i);

// String <-> char[]
char[] chars = str.toCharArray();
String str4 = new String(chars);
String str5 = String.valueOf(chars);

// String <-> StringBuilder
StringBuilder sbConv = new StringBuilder(str);
String str6 = sbConv.toString();

// char <-> int
int val = c - '0';
char cChar = (char)(val + '0');

// int <-> Integer boxing
Integer xObj = i;
int iPrim = xObj;
```

---

## 13. Math, Overflow & Misc CP Tricks

```java
// Modulo & Overflow Safety
static final long MOD = 1_000_000_007L;
long safeMult = 1L * a * b; // prevent int overflow before multiplying!
int posMod = Math.floorMod(a, m); // handles negative numbers correctly (-7 % 3 = -1, floorMod = 2)

// Prefix Sum
int[] pre = new int[n + 1];
for (int idx = 0; idx < n; idx++) pre[idx + 1] = pre[idx] + a[idx];

// GCD & LCM
int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(long a, long b) { return a / gcd((int) a, (int) b) * b; }

// BigInteger GCD
int bigGcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();

// Labeled Break (Exit nested loops)
outer:
for (int r = 0; r < n; r++) {
    for (int col = 0; col < m; col++) {
        if (g[r][col] == target) break outer;
    }
}

// Limits
int INF = Integer.MAX_VALUE;
int NEG = Integer.MIN_VALUE;
long LINF = Long.MAX_VALUE;

// Debug Prints
System.out.println(Arrays.toString(nums));
System.out.println(Arrays.deepToString(matrix));
```

## 14. Lambdas, streams, Optional

**Lambdas and functional interfaces**
```java
Function<Integer, Integer> square = x -> x * x;
System.out.println(square.apply(5));                         // 25

BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
Predicate<String> isEmpty = String::isEmpty;                 // method reference
Consumer<String> printer = System.out::println;
Supplier<Double> random = Math::random;
UnaryOperator<String> upper = String::toUpperCase;
BinaryOperator<Integer> mul = (a, b) -> a * b;

Runnable task = () -> System.out.println("run");
task.run();
```

**Streams**
```java
List<Integer> nums = List.of(5, 3, 8, 1, 9, 2);

List<Integer> evens = nums.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
int total = nums.stream().mapToInt(Integer::intValue).sum();
List<Integer> sorted = nums.stream().sorted().collect(Collectors.toList());
List<Integer> top3 = nums.stream().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toList());
Optional<Integer> max = nums.stream().max(Integer::compare);
String joined = nums.stream().map(String::valueOf).collect(Collectors.joining(", "));
long count = nums.stream().filter(n -> n > 4).count();
int product = nums.stream().reduce(1, (a, b) -> a * b);

Map<Boolean, List<Integer>> parts = nums.stream().collect(Collectors.partitioningBy(n -> n > 4));
Map<Integer, Long> byMod3 = nums.stream().collect(Collectors.groupingBy(n -> n % 3, Collectors.counting()));

int sumRange = IntStream.rangeClosed(1, 5).sum();            // 15
```

**Optional**
```java
Optional<String> o = Optional.of("x");
Optional<String> none = Optional.empty();
o.isPresent();                                   // true
o.orElse("default");                             // "x"
none.orElse("default");                          // "default"
o.ifPresent(System.out::println);
```
