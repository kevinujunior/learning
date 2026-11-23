# Java CP Cheat Sheet: Ultra-Short Syntax

## Contents
1. [Arrays & 2D Matrices](#1-arrays--2d-matrices)
2. [Characters](#2-characters)
3. [Strings](#3-strings)
4. [StringBuilder](#4-stringbuilder)
5. [List & Collections Utility](#5-list--collections-utility)
6. [Set & TreeSet](#6-set--treeset)
7. [Map & TreeMap](#7-map--treemap)
8. [Queue, Stack & Deque](#8-queue-stack--deque)
9. [PriorityQueue](#9-priorityqueue)
10. [Custom Comparators & Objects](#10-custom-comparators--objects)
11. [Streams, Lambdas & Optional](#11-streams-lambdas--optional)
12. [Conversions](#12-conversions)
13. [Math, Overflow & Misc CP Tricks](#13-math-overflow--misc-cp-tricks)

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
int[] b = a.clone();                      // 2D arrays: clone each row (shallow otherwise)

// Reverse (for Integer[])
Collections.reverse(Arrays.asList(A));

// Swap
int t = a[i]; a[i] = a[j]; a[j] = t;

// Compare & Debug
Arrays.equals(a, b);                      // also works for anagram check on freq arrays
System.out.println(Arrays.toString(a));
System.out.println(Arrays.deepToString(g));
```

---

## 2. Characters

### Type Checks, Case & Conversion
```java
// Type checks
Character.isDigit(c); Character.isLetter(c); Character.isLetterOrDigit(c);
Character.isAlphabetic(c); Character.isWhitespace(c);
Character.isUpperCase(c); Character.isLowerCase(c);

// Case & conversion
Character.toUpperCase(c); Character.toLowerCase(c);
Character.getNumericValue(c);  // '7' -> 7, but 'a' -> 10 (letters give 10..35!)
Character.digit(c, 10);        // '7' -> 7, non-digit -> -1
Character.forDigit(7, 10);     // 7 -> '7'
Character.toString(c);         // char -> String
Character.compare(a, b);
```

### ASCII Tricks
```java
int idx = c - 'a';             // 'a'->0 ... 'z'->25
int val = c - '0';             // digit char -> int
char cDigit = (char) (val + '0'); // int -> digit char
char next = (char) (c + 1);
char fromIdx = (char) ('a' + i);
char flip = (char) (c ^ 32);   // toggle case (letters only)
boolean lower = c >= 'a' && c <= 'z';
int[] freq128 = new int[128]; freq128[c]++;  // frequency over any ASCII char
```

---

## 3. Strings

### Initialization & Iteration
```java
String s = "abc";
char[] chars = s.toCharArray();
String s2 = new String(chars);
String s3 = String.valueOf(chars);
String s4 = String.valueOf(chars, off, len);

for (int i = 0; i < s.length(); i++) { char c = s.charAt(i); }
for (char c : s.toCharArray()) { }
```

### Core Methods
```java
s.length(); s.isEmpty(); s.isBlank();     // isBlank: Java 11+
s.substring(l, r); // [l, r)
s.contains("ab");
s.indexOf('c'); s.indexOf("sub"); s.indexOf('c', from); s.lastIndexOf('c');
s.startsWith("pre"); s.startsWith("pre", offset); s.endsWith("suf");
s.toLowerCase(); s.toUpperCase();
s.trim(); s.strip();                      // strip: Java 11+, Unicode-aware
s.stripLeading(); s.stripTrailing();      // Java 11+
s.repeat(3);                              // Java 11+
s.concat("x");
s.codePointAt(i);
String.format("%d %s %.2f", i, str, d);
String.join(",", arr);
String.join(",", list);
```

### Comparing (PITFALL: never use `==` for Strings)
```java
s.equals(t); s.equalsIgnoreCase(t);
s.compareTo(t); s.compareToIgnoreCase(t); // lexicographic: <0, 0, >0
```

### Replace, Split & Regex
```java
s.replace('a', 'b');
s.replaceAll("[^a-z]", "");           // regex replace (e.g. keep only letters)
s.replaceFirst("a", "b");
s.matches("[a-z]+");                  // full regex match
s.split(",");
s.split("\\s+");                      // split on whitespace
s.split(",", -1);                     // keep trailing empty strings
s.split("");                          // split into single-char strings
```

### Chars as Stream
```java
long digits = s.chars().filter(Character::isDigit).count();
int vowels = (int) s.chars().filter(ch -> "aeiou".indexOf(ch) >= 0).count();
```

### Common String Patterns
```java
// Reverse & palindrome check (simple)
String rev = new StringBuilder(s).reverse().toString();
boolean pal = s.equals(rev);

// Sort characters of a string (anagram key)
char[] cs = s.toCharArray(); Arrays.sort(cs); String key = new String(cs);

// Frequency array (lowercase letters)
int[] cnt = new int[26];
for (char c : s.toCharArray()) cnt[c - 'a']++;

// Build string from freq
for (int i = 0; i < 26; i++) sb.append(String.valueOf((char) ('a' + i)).repeat(cnt[i]));
```

---

## 4. StringBuilder

### Initialization & Ops
```java
StringBuilder sb = new StringBuilder();
StringBuilder sb2 = new StringBuilder("hello");
StringBuilder sbCap = new StringBuilder(50);

sb.length(); sb.charAt(i); sb.isEmpty();        // isEmpty: Java 15+
sb.append(c); sb.append(i); sb.append("x"); sb.append(s, l, r); // char / int / String / substring [l, r)
sb.setCharAt(i, 'z');
sb.insert(i, "x");              // insert at index (O(n))
sb.insert(0, c);                // prepend
sb.deleteCharAt(i);             // remove one char
sb.delete(l, r);                // remove [l, r)
sb.replace(l, r, "new");        // replace [l, r)
sb.setLength(sb.length() - 1);  // pop last char (backtracking / stack-like)
sb.setLength(0);                // clear
sb.indexOf("sub"); sb.lastIndexOf("sub");
sb.substring(l, r);
sb.reverse();                   // PITFALL: mutates sb in place
sb.toString();
sb.compareTo(sb2);              // Java 11+
// PITFALL: sb.equals(sb2) compares references; use sb.toString().equals(...)
```

---

## 5. List & Collections Utility

### Initialization
```java
List<Integer> list = new ArrayList<>();
List<Integer> listCap = new ArrayList<>(100);
List<Integer> listVals = new ArrayList<>(List.of(1, 2, 3));
List<Integer> modifiableList = new ArrayList<>(Arrays.asList(1, 2, 3));
List<Integer> l = Arrays.asList(1, 2, 3); // fixed-size
List<Integer> lCopies = new ArrayList<>(Collections.nCopies(n, 0));
List<List<Integer>> adj = new ArrayList<>(); // Graph adjacency list
for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
```

### Core Methods
```java
list.add(x); list.add(i, x);        // append / insert at index
list.get(i); list.set(i, x);        // set returns old value
list.remove(i);                     // by index
list.remove(Integer.valueOf(x));    // by value
list.size(); list.isEmpty(); list.clear();
list.contains(x);
list.indexOf(x); list.lastIndexOf(x); // -1 if absent
list.addAll(other); list.addAll(i, other);
list.subList(l, r);                 // [l, r), a VIEW (changes reflect in original)
new ArrayList<>(list.subList(l, r)); // independent copy
List<Integer> copy = new ArrayList<>(list);
List<Integer> imm = List.copyOf(list);   // immutable copy
list.replaceAll(x -> x * 2);        // in-place map
list.equals(other);                 // element-wise comparison
list.getFirst(); list.getLast();    // Java 21+ (all Lists)
list.reversed();                    // Java 21+ (view)
```

### Iteration & Safe Removal
```java
for (int i = 0; i < list.size(); i++) list.get(i); // ArrayList only!
for (int x : list) { }

// Safe removal
Iterator<Integer> it = list.iterator();
while (it.hasNext()) { if (it.next() == val) it.remove(); }
list.removeIf(x -> x == val);

// ListIterator (modify while iterating)
ListIterator<Integer> li = list.listIterator();
while (li.hasNext()) { int v = li.next(); if (v < 0) li.set(0); }
```

### Sorting & Searching
```java
Collections.sort(list);
Collections.sort(list, cmp);
list.sort(null); // ascending
list.sort(Integer::compare);                   // ascending (overflow-safe)
list.sort((a, b) -> Integer.compare(b, a));    // descending (overflow-safe)
list.sort(Comparator.reverseOrder());          // descending
Collections.reverse(list);

int idx = Collections.binarySearch(list, target); // list MUST be sorted
```

### Collections Utility Methods
```java
Collections.max(list); Collections.min(list);
Collections.swap(list, i, j);
Collections.frequency(list, x);
Collections.shuffle(list);
Collections.rotate(list, k);        // rotate right by k
Collections.fill(list, 0);
Collections.addAll(list, 1, 2, 3);
Collections.unmodifiableList(list);
Collections.emptyList();
Collections.singletonList(x);
```

### Pitfalls
```java
// 1. list.remove(0) removes by INDEX 0. Use list.remove(Integer.valueOf(val)) to remove by value.
// 2. Integer compare: list.get(i) == list.get(j) FAILS beyond 127. Use .equals() or .intValue().
// 3. Removing inside for-each throws ConcurrentModificationException. Use iterator or removeIf.
// 4. Comparator (a, b) -> a - b can overflow; use Integer.compare(a, b).
// 5. list.remove(i) in a loop shifts elements; iterate backwards or use removeIf.
// 6. ArrayList.add(0, x) / remove(0) are O(n); use ArrayDeque for queue behavior.
```

---

## 6. Set & TreeSet

### HashSet / LinkedHashSet
```java
Set<String> set = new HashSet<>();
Set<String> immSet = Set.of("A", "B", "C"); // Immutable
Set<String> mutSet = new HashSet<>(Set.of("A", "B", "C")); // Mutable from immutable
Set<Integer> capacitySet = new HashSet<>(expectedSize);
Set<String> orderedSet = new LinkedHashSet<>(); // maintains insertion order

set.add("A"); // returns false if duplicate
set.contains("A");
set.remove("A");

for (String x : set) { }

// Set Math (each mutates s1)
s1.addAll(s2);    // Union
s1.retainAll(s2); // Intersection
s1.removeAll(s2); // Difference

// Sorted views
TreeSet<String> sortedSet = new TreeSet<>(set);
List<String> sortedList = new ArrayList<>(set); sortedList.sort(null);
```

### TreeSet (Sorted Set, O(log N))
```java
TreeSet<Integer> ts = new TreeSet<>();
TreeSet<Integer> descTs = new TreeSet<>(Comparator.reverseOrder());

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

## 7. Map & TreeMap

### HashMap / LinkedHashMap
```java
Map<String, Integer> map = new HashMap<>(expectedSize);
Map<String, Integer> lhm = new LinkedHashMap<>(); // insertion order
Map<Integer, String> mutableMap = new HashMap<>(Map.of(1, "A", 2, "B")); // Map.of: up to 10 pairs
Map<Integer, String> mapEntries = Map.ofEntries(
    Map.entry(1, "Value1"),
    Map.entry(2, "Value2")
);

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

### Counting & Grouping Idioms
```java
map.merge(key, 1, Integer::sum);                           // frequency count
map.put(key, map.getOrDefault(key, 0) + 1);                // same, longer
map.computeIfAbsent(key, k -> new ArrayList<>()).add(val); // group values by key
```

### TreeMap (Sorted by Key, O(log N))
```java
TreeMap<Integer, String> tm = new TreeMap<>();

for (var k : tm.keySet()) { }

tm.firstKey(); tm.lastKey();
tm.floorKey(k);   // greatest key <= k
tm.ceilingKey(k); // smallest key >= k
tm.lowerKey(k);   // greatest key < k
tm.higherKey(k);  // smallest key > k
tm.pollFirstEntry(); tm.pollLastEntry();
```

---

## 8. Queue, Stack & Deque

### Initialization
```java
Queue<Integer> q = new ArrayDeque<>();      // FIFO Queue
Deque<Integer> stack = new ArrayDeque<>();  // LIFO Stack (prefer over legacy Stack)
Deque<Integer> dq = new ArrayDeque<>();     // Double-Ended Queue
// LinkedList also implements Queue/Deque (allows null, but slower than ArrayDeque)
```

### Ops & Iteration
```java
// Queue
q.offer(x); q.poll(); q.peek();

// Stack
stack.push(x); stack.pop(); stack.peek();

// Deque
dq.offerFirst(x); dq.offerLast(x);
dq.pollFirst(); dq.pollLast();
dq.peekFirst(); dq.peekLast();
// Throwing variants (also on LinkedList):
// addFirst / addLast / removeFirst / removeLast / getFirst / getLast

// Iteration (Non-destructive)
for (int x : dq) { }

// Destructive Drain (BFS / Processing)
while (!q.isEmpty()) { int cur = q.poll(); }
```

---

## 9. PriorityQueue

### Initialization
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // min-heap default
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); // by col 0 (use a[1] for col 1)
PriorityQueue<int[]> maxPq = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0])); // descending
```

### Ops
```java
pq.offer(x); // O(log N)
pq.poll();   // O(log N) - removes min/max
pq.peek();   // O(1)

// PITFALL: for (int x : pq) does NOT iterate in sorted heap order! 
// You must poll() in a while loop to extract elements in sorted order.
while (!pq.isEmpty()) { int val = pq.poll(); }
```

---

## 10. Custom Comparators & Objects

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

### Separate Comparator Class
```java
// Sorts products by price in ascending order (Product has a double field `price`)
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

## 11. Streams, Lambdas & Optional

### Array Streams
```java
int[] uniq = Arrays.stream(a).distinct().toArray();
int[] pos  = Arrays.stream(a).filter(x -> x > 0).toArray();
int[] sq   = Arrays.stream(a).map(x -> x * x).toArray();

int max = Arrays.stream(a).max().getAsInt();
int min = Arrays.stream(a).min().getAsInt();
long sum = Arrays.stream(a).asLongStream().sum(); // long: avoids int overflow

int sumRange = IntStream.rangeClosed(1, n).sum();
```

### List Streams
```java
List<Integer> list = List.of(5, 3, 8, 1, 9, 2);

List<Integer> evens = list.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
long total = list.stream().mapToLong(Integer::longValue).sum();
List<Integer> sorted = list.stream().sorted().collect(Collectors.toList());
List<Integer> top3 = list.stream().sorted(Comparator.reverseOrder()).limit(3).collect(Collectors.toList());
Optional<Integer> mx = list.stream().max(Integer::compare);
String joined = list.stream().map(String::valueOf).collect(Collectors.joining(", "));
long count = list.stream().filter(n -> n > 4).count();
int product = list.stream().reduce(1, (a, b) -> a * b);

Map<Boolean, List<Integer>> parts = list.stream().collect(Collectors.partitioningBy(n -> n > 4));
Map<Integer, Long> freq = list.stream().collect(Collectors.groupingBy(x -> x, Collectors.counting()));
// for int[] a: Arrays.stream(a).boxed().collect(...)

list.forEach(System.out::println);
```

### Lambdas & Functional Interfaces
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

### Optional
```java
Optional<String> o = Optional.of("x");
Optional<String> none = Optional.empty();
o.isPresent();                                   // true
o.orElse("default");                             // "x"
none.orElse("default");                          // "default"
o.ifPresent(System.out::println);
```

---

## 12. Conversions

### String <-> Primitive
```java
// String -> Primitive
int i = Integer.parseInt(str);
long l = Long.parseLong(str);
double d = Double.parseDouble(str);

// Primitive -> String
String str1 = "" + i;
String str2 = String.valueOf(i);
String str3 = Integer.toString(i);

// int <-> Integer boxing
Integer xObj = i;
int iPrim = xObj;

// char <-> int: see "ASCII Tricks" in section 2
// String <-> char[] / StringBuilder: see sections 3 and 4
```

### Array <-> List <-> Set
```java
// int[] <-> List<Integer>
List<Integer> listConv = Arrays.stream(a).boxed().collect(Collectors.toList());
int[] arr = listConv.stream().mapToInt(Integer::intValue).toArray();

// Integer[] / String[] <-> List
List<Integer> fromBoxed = Arrays.asList(A);              // fixed-size view of Integer[]
List<String> fromStrArr = Arrays.asList(strArray);       // fixed-size
Integer[] boxed = listConv.toArray(new Integer[0]);
String[] strArrayFromList = list.toArray(new String[0]);
Object[] objectArray = list.toArray();

// List <-> Set
Set<Integer> setConv = new HashSet<>(listConv);
List<Integer> listFromSet = new ArrayList<>(setConv);
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
for (int i = 0; i < n; i++) pre[i + 1] = pre[i] + a[i];
//usage (left to right - inclusive)
int rangeSum = pre[right + 1] - pre[left];

// GCD & LCM (long-safe)
long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
long lcm(long a, long b) { return a / gcd(a, b) * b; }

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
int INF = Integer.MAX_VALUE;   // PITFALL: INF + x overflows; use 1e9 as INF when adding distances
int NEG = Integer.MIN_VALUE;
long LINF = Long.MAX_VALUE;

// nCr (exact, iterative; result must fit in long)
int nCr(int n, int r) {
    long ans = 1;
    if (r > n - r) {
        r = n - r;
    }
    for (int i = 1; i <= r; i++) {
        ans = ans * (n - r + i) / i;
    }
    return (int)ans;
}
```