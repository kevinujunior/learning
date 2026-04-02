# Java CP Cheat Sheet: Ultra-Short Syntax

---

##  Arrays

```java
// 1D Init
int[] a = new int[n];
boolean[] b = new boolean[n];

// 1D Init with values
int[] a = {1, 2, 3};
Arrays.fill(a, 7);
Arrays.fill(a, l, r, 7);

// 2D Init
int[][] g = new int[m][n];
int[][] g = {
    {1, 2, 3},
    {4, 5, 6}
};

// 2D Fill
for (int[] row : g) Arrays.fill(row, -1);

// Copy
int[] b = Arrays.copyOf(a, a.length);
int[] b = Arrays.copyOfRange(a, l, r);

// Reverse
Collections.reverse(Arrays.asList(a)); // only Integer[]
```

---

##  Initializations (Pre-filled & Capacity)

```java
// Arrays
int[] nums = new int[n];
int[][] matrix = new int[n][m];
int[] prefilled = {1, 2, 3};

// List
List<Integer> list = Arrays.asList(1, 2, 3);
List<Integer> modifiableList = new ArrayList<>(Arrays.asList(1, 2, 3));
List<Integer> l = new ArrayList<>();
List<Integer> l2 = new ArrayList<>(List.of(1,2,3));

// Set
Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
Set<String> set = Set.of("A", "B", "C"); //immuatable
Set<String> set = new HashSet<>(Set.of("A", "B", "C")); //mutable
Set<Integer> capacitySet = new HashSet<>(expectedSize);
Set<Integer> s = new LinkedHashSet<>();
Set<Integer> ts = new TreeSet<>();

// Map
Map<Integer, Integer> map = new HashMap<>(expectedSize);
Map<Integer,Integer> m = new HashMap<>();
Map<Integer, String> mutableMap = new HashMap<>(Map.of(1, "A", 2, "B")); //mutable (less than 10 pairs)
Map<Integer, String> map = Map.ofEntries(
    entry(1, "Value1"),
    entry(2, "Value2")
); //wrap around HashMap
Map<Integer,Integer> tm = new TreeMap<>();

// Queue / Deque
Queue<Integer> queue = new LinkedList<>();
Deque<Integer> stack = new ArrayDeque<>();
Deque<Integer> dq = new ArrayDeque<>();

// PriorityQueue
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
PriorityQueue<Integer> maxH = new PriorityQueue<>(Collections.reverseOrder());
PriorityQueue<int[]> pq = new PriorityQueue<>((a,b) -> a[0]-b[0]);

// StringBuilder
StringBuilder sb = new StringBuilder();
StringBuilder sb2 = new StringBuilder("hello");
```

---

##  Conversions: Arrays & Collections

```java
// int[] -> List<Integer>
List<Integer> list = Arrays.stream(nums).boxed().collect(Collectors.toList());

// List<Integer> -> int[]
int[] nums = list.stream().mapToInt(i -> i).toArray();

// List <-> Set
Set<Integer> set = new HashSet<>(list);
List<Integer> list = new ArrayList<>(set);

// Array -> List
List<Integer> l = Arrays.asList(new Integer[]{1,2,3});
List<String> l2 = Arrays.asList(strArr);

// String[] <-> List<String>
List<String> list = Arrays.asList(strArray);
String[] strArray = list.toArray(new String[0]);

// Collection -> Object[]
Object[] arr = list.toArray();
```

---

##  Conversions: Strings & Primitives

```java
// String -> Primitive
int i = Integer.parseInt(str);
long l = Long.parseLong(str);
double d = Double.parseDouble(str);

// Primitive -> String
String str = "" + i;
String str = String.valueOf(i);
String str = Integer.toString(i);

// String <-> char[]
char[] chars = str.toCharArray();
String str = new String(chars);
String str = String.valueOf(chars);

// String <-> StringBuilder
StringBuilder sb = new StringBuilder(str);
String str = sb.toString();

// char <-> int
int val = c - '0';
char c = (char)(val + '0');

// int <-> Integer
Integer x = i;
int i = x;
```

---

##  Strings

```java
String s = "";

// Ops
s.charAt(i);
s.substring(l, r);
s.indexOf('c');
s.contains("ab");
s.replace('a','b');
s.split(",");
String.join(",", arr);
String.join(",", list);
s.strip();
s.repeat(3);
```

---

##  Sorting & Searching & imp ops

```java
// Arrays
Arrays.sort(nums);
Arrays.sort(nums, start, end);

// Descending convert to Integer[] first)
Integer[] A = Arrays.stream(nums).boxed().toArray(Integer[]::new);
Arrays.sort(A, Collections.reverseOrder());


//reverse a list
Collections.reverse(list); 

// 2D array sort
Arrays.sort(a, (x,y) -> x[0]-y[0]);

// List
Collections.sort(list);
list.sort(null);
list.sort((a, b) -> a - b);
list.sort((a, b) -> b - a);

// Binary Search
int idx = Arrays.binarySearch(a, target);
int idx = Collections.binarySearch(list, target);

// Note
// Prefer Integer.compare(a,b)
```

---

##  Common One-Liners

```java
// Fill
Arrays.fill(nums, -1);
for(int[] row : matrix) Arrays.fill(row, -1);

// Max / Min
int max = Arrays.stream(nums).max().getAsInt();
int min = Arrays.stream(nums).min().getAsInt();
int mx = Collections.max(list);
int mn = Collections.min(list);

// Sum
long sum = Arrays.stream(nums).summaryStatistics().getSum();
long sum2 = Arrays.stream(nums).sum();

// Frequency Map
map.put(key, map.getOrDefault(key, 0) + 1);
Map<Integer,Long> freq = Arrays.stream(a).boxed()
    .collect(Collectors.groupingBy(x->x, Collectors.counting()));

// Prefix Sum
int[] pre = new int[n+1];
for (int i = 0; i < n; i++) pre[i+1] = pre[i] + a[i];

// Swap
int t = a[i]; a[i] = a[j]; a[j] = t;

// Debug
System.out.println(Arrays.toString(nums));
System.out.println(Arrays.deepToString(matrix));
```

---

##  Functional Shortcodes (Java 8+)

```java
// RemoveIf
list.removeIf(n -> n % 2 == 0);

// ForEach
list.forEach(System.out::println);

// Map Ops
map.computeIfAbsent(key, k -> new ArrayList<>()).add(val);
map.forEach((k, v) -> System.out.println(k + ":" + v));
```

---

## Misc CP Tricks

```java
// Streams
int[] uniq = Arrays.stream(a).distinct().toArray();
int[] pos  = Arrays.stream(a).filter(x -> x>0).toArray();
int[] sq   = Arrays.stream(a).map(x -> x*x).toArray();

// Collections.nCopies
List<Integer> l = new ArrayList<>(Collections.nCopies(n, 0));

// GCD / LCM
int gcd(int a, int b){ return b==0?a:gcd(b,a%b); }
long lcm(long a, long b){ return a/gcd(a,b)*b; }

// BigInteger GCD
int g = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();

// Limits
int INF = Integer.MAX_VALUE;
int NEG = Integer.MIN_VALUE;
long LINF = Long.MAX_VALUE;

```
