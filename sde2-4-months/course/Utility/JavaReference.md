# Java Language Reference

A single-file Java language reference covering core syntax, the collections framework, OOP basics, and standard utilities.

**How to use this file**

- Every section is a small, self-contained snippet. Paste it inside a method (e.g. `main`) unless it is marked **(class level)**.
- Snippets use `System.out.println` so they run anywhere. 
- Several snippets reuse variable names (`list`, `map`, `stack`, `a`, ...). They are separate on purpose; do not paste them into one method.
- Language level: most code is Java 8+ (collection factories like `List.of` need Java 9+). Features that need a newer version are marked (`var` = 10, `switch` arrows = 14, records / `instanceof` patterns = 16, text blocks = 15).

## Contents

**I. Core Language Syntax**
1. [Program structure and imports](#1-program-structure-and-imports)
2. [Primitive types, wrappers and casting](#2-primitive-types-wrappers-and-casting)
3. [Operators](#3-operators)
4. [Control flow](#4-control-flow)
5. [Methods](#5-methods)
6. [Exceptions](#6-exceptions)

**II. Object-Oriented Programming**
7. [Classes and OOP](#7-classes-and-oop)
8. [Generics and Pair](#8-generics-and-pair)

**III. Strings & Math**
9. [Strings, StringBuilder, Character](#9-strings-stringbuilder-character)
10. [Math & Misc Utilities](#10-math--misc-utilities)

**IV. Data Structures (Collections Framework)**
11. [Arrays](#11-arrays)
12. [Collections Basics & Utilities](#12-collections-basics--utilities)
13. [Lists](#13-lists)
14. [Sets](#14-sets)
15. [Maps](#15-maps)
16. [Stacks, Queues, and Deques](#16-stacks-queues-and-deques)

**V. Functional Java**
17. [Lambdas, streams, Optional](#17-lambdas-streams-optional)
18. [Custom comparators](#18-custom-comparators)

---

## I. Core Language Syntax

### 1. Program structure and imports

```java
import java.io.*;
import java.math.*;                          // BigInteger, BigDecimal, RoundingMode
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.*;                 // Function, Predicate, ...
import java.util.stream.*;                   // Collectors, IntStream, ...

// The file name must match the public class name.
// Online judges usually expect the class to be called "Main".
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

---

### 2. Primitive types, wrappers and casting

| Type | Size | Range / notes |
|---|---|---|
| `byte` | 8 bit | -128 to 127 |
| `short` | 16 bit | -32,768 to 32,767 |
| `int` | 32 bit | about ±2.1 × 10⁹ (`-2^31` to `2^31-1`) |
| `long` | 64 bit | about ±9.2 × 10¹⁸ |
| `float` | 32 bit | ~7 decimal digits, literal needs `f` |
| `double` | 64 bit | ~15 decimal digits (default for decimals) |
| `char` | 16 bit | unsigned, 0 to 65,535 (a UTF-16 code unit) |
| `boolean` | - | `true` / `false` |

```java
int i = 10;
long big = 10_000_000_000L;      // L suffix required above int range; _ is just a separator
double d = 3.14;
float f = 3.14f;
char c = 'A';
boolean flag = true;
byte by = 127;
short sh = 1000;

var inferred = 42;               // Java 10+: type inferred (int), still statically typed

// Limits
System.out.println(Integer.MAX_VALUE);   // 2147483647
System.out.println(Integer.MIN_VALUE);   // -2147483648
System.out.println(Long.MAX_VALUE);      // 9223372036854775807

// Casting
long widened = i;                // widening: implicit
int narrowed = (int) 3.9;        // narrowing: explicit, truncates -> 3
char next = (char) ('a' + 1);    // 'b'
int digit = '7' - '0';           // 7  (char arithmetic)
int idx = 'c' - 'a';             // 2  (alphabet index)

// Integer overflow (silent!)
int x = Integer.MAX_VALUE;
System.out.println(x + 1);       // -2147483648
int p = 100_000, q = 100_000;
long safe = 1L * p * q;          // promote to long BEFORE multiplying

// Integer division and modulo
System.out.println(7 / 2);       // 3
System.out.println(-7 / 2);      // -3 (truncates toward zero)
System.out.println(7 / 2.0);     // 3.5
System.out.println(-7 % 3);      // -1 (sign follows the dividend)
System.out.println(Math.floorMod(-7, 3)); // 2 (always non-negative for positive modulus)
```

**Wrapper classes**

```java
int n = Integer.parseInt("123");
Integer boxed = Integer.valueOf(123);          // autoboxing also works: Integer b = 5;
String s = Integer.toString(123);              // or String.valueOf(123)
int cmp = Integer.compare(3, 5);               // -1
int hex = Integer.parseInt("ff", 16);          // 255
String bin = Integer.toString(255, 2);         // "11111111"
String bin2 = Integer.toBinaryString(10);      // "1010"
String hex2 = Integer.toHexString(255);        // "ff"
long l = Long.parseLong("9999999999");
double dd = Double.parseDouble("3.5");

// Pitfall: Integer cache. == compares references, equals compares values.
Integer a1 = 127, a2 = 127;
Integer b1 = 128, b2 = 128;
System.out.println(a1 == a2);       // true  (cached range -128..127)
System.out.println(b1 == b2);       // false
System.out.println(b1.equals(b2));  // true  -> always use equals for wrapper objects
```

---

### 3. Operators

```java
// Arithmetic: + - * / %      Compound: += -= *= /= %=      Increment: i++ ++i i-- --i
int a = 10, b = 3;
a += 5;                          // a = a + 5

// Relational: == != < > <= >=
// Logical (short-circuit): && || !
boolean ok = (a > 5) && (b < 10);

// Ternary
int max = (a > b) ? a : b;

// instanceof (Java 16+ pattern matching)
Object o = "text";
if (o instanceof String str) {
    System.out.println(str.length());
}
```

---

### 4. Control flow

```java
// if / else if / else
int score = 72;
if (score >= 90) {
    System.out.println("A");
} else if (score >= 70) {
    System.out.println("B");
} else {
    System.out.println("C");
}

// classic switch (fall-through unless you break)
int day = 3;
switch (day) {
    case 1:
        System.out.println("Mon");
        break;
    case 2:
        System.out.println("Tue");
        break;
    default:
        System.out.println("Other");
}

// switch expression (Java 14+): arrows, no fall-through, returns a value
String type = switch (day) {
    case 6, 7 -> "Weekend";
    case 1, 2, 3, 4, 5 -> "Weekday";
    default -> "Invalid";
};

// switch expression with a block and yield
int len = switch (type) {
    case "Weekend" -> 7;
    default -> {
        int t = type.length();
        yield t;
    }
};

// for
for (int i = 0; i < 5; i++) {
    System.out.print(i + " ");
}

// enhanced for (arrays and Iterables)
int[] nums = {1, 2, 3};
for (int v : nums) {
    System.out.print(v + " ");
}

// while / do-while
int w = 0;
while (w < 3) { w++; }
do { w--; } while (w > 0);       // body runs at least once

// break / continue
for (int i = 0; i < 10; i++) {
    if (i == 2) continue;        // skip this iteration
    if (i == 5) break;           // leave the loop
}

// labeled break: leave nested loops at once
outer:
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i * j == 2) break outer;
    }
}
```

---

### 5. Methods

All of these are **(class level)**.

```java
// static method with a return value
static int add(int a, int b) {
    return a + b;
}

// overloading: same name, different parameters
static double add(double a, double b) {
    return a + b;
}

// varargs: arrives as an array inside the method
static int sum(int... nums) {
    int total = 0;
    for (int n : nums) total += n;
    return total;
}
// sum(), sum(1), sum(1, 2, 3)

// void method
static void greet(String name) {
    System.out.println("Hello, " + name);
}

// recursion
static long factorial(int n) {
    return n <= 1 ? 1 : n * factorial(n - 1);
}
```

Java is always **pass-by-value**. For objects and arrays the value is a copy of the *reference*, so a method can modify the object's contents but cannot make the caller's variable point somewhere else.

Returning values (handy one-liners):

```java
// return List.of(1, 2, 3);                     // immutable (Java 9+)
// return new ArrayList<>(List.of("A", "B", "C")); // mutable (Java 9+)
// return new HashSet<>(Set.of(1, 2, 3));       // (Java 9+)
// return Map.of("A", 1, "B", 2);               // (Java 9+)
// return new int[]{1, 2};
// return "Hello World";
// return new String[]{"Hello", "World"};
```

---

### 6. Exceptions

```java
// try / catch / finally
try {
    int x = Integer.parseInt("abc");
    int y = 10 / 0;
} catch (NumberFormatException e) {
    System.out.println("bad number: " + e.getMessage());
} catch (ArithmeticException | NullPointerException e) {   // multi-catch
    System.out.println("math/null problem");
} catch (Exception e) {                                    // most general last
    e.printStackTrace();
} finally {
    System.out.println("always runs");
}

// try-with-resources: the resource is closed automatically
try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}

// throwing
// throw new IllegalArgumentException("age must be >= 0");
```

Class-level declarations:

```java
// checked exceptions must be declared or caught
static void readFile() throws IOException {
    throw new IOException("disk error");
}

// custom exception
static class InsufficientFundsException extends Exception {
    InsufficientFundsException(String msg) { super(msg); }
}
```

| Kind | Examples | Must be handled? |
|---|---|---|
| Checked | `IOException`, custom `extends Exception` | Yes (catch or `throws`) |
| Unchecked | `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException` | No (extend `RuntimeException`) |


---

## II. Object-Oriented Programming

### 7. Classes and OOP

All declarations are **(class level)**. Non-public top-level classes can live in the same file.

```java
class Animal {
    protected String name;                        // visible to subclasses and the package

    Animal(String name) {                         // constructor
        this.name = name;
    }

    void speak() {
        System.out.println(name + " makes a sound");
    }

    @Override
    public String toString() {
        return "Animal(" + name + ")";
    }
}

// inheritance
class Dog extends Animal {
    Dog(String name) {
        super(name);                              // call parent constructor
    }

    @Override
    void speak() {
        System.out.println(name + " barks");
    }
}

// abstract class: cannot be instantiated, may contain abstract methods
abstract class Shape {
    abstract double area();
}

class Circle extends Shape {
    double r;
    Circle(double r) { this.r = r; }
    double area() { return Math.PI * r * r; }
}

// interface with default and static methods
interface Greeter {
    void greet(String who);
    default void hello() { greet("world"); }
    static Greeter polite() { return w -> System.out.println("Hello, " + w); }
}

// enum
enum Color { RED, GREEN, BLUE }

enum Level {
    LOW(1), HIGH(3);
    private final int value;
    Level(int value) { this.value = value; }
    int value() { return value; }
}

// record (Java 16+): immutable data carrier, auto-generates constructor,
// accessors, equals, hashCode and toString
record Point2(int x, int y) {}
```

Usage:

```java
Animal pet = new Dog("Rex");                     // polymorphism
pet.speak();                                     // Rex barks

Color c = Color.GREEN;
for (Color col : Color.values()) { System.out.println(col); }
System.out.println(Color.valueOf("RED").ordinal());    // 0

Point2 p = new Point2(1, 2);
System.out.println(p.x() + " " + p);             // 1 Point2[x=1, y=2]
```

**Static vs instance, `final`**

```java
static int counter = 0;                           // one copy shared by the class
final int MAX = 100;                              // cannot be reassigned
static final long MOD = 1_000_000_007;            // constant syntax example

static class Node {                               // static nested class: no outer instance needed
    int val;
    Node next;
    Node(int val) { this.val = val; }
}
```

**Access modifiers**

| Modifier | Same class | Same package | Subclass | Everywhere |
|---|---|---|---|---|
| `private` | yes | no | no | no |
| (default) | yes | yes | no | no |
| `protected` | yes | yes | yes | no |
| `public` | yes | yes | yes | yes |

If you override `equals`, also override `hashCode` (see [section 18](#18-custom-comparators)).

---

### 8. Generics and Pair

```java
// generic class (class level)
class Box<T> {
    private T val;
    Box(T val) { this.val = val; }
    T get() { return val; }
}

// generic method with a bound (class level)
static <T Comparable<T extends>> T maxOf(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}

// wildcard (class level): accepts List<Integer>, List<Double>, ...
static double sumAll(List<? extends Number> xs) {
    double s = 0;
    for (Number n : xs) s += n.doubleValue();
    return s;
}

// Box<String> b = new Box<>("hi");   maxOf(3, 7);   sumAll(List.of(1, 2.5));
```

**Pair**

Use a record as a pair (like C++ `pair<T,U>`). You can make a tuple by adding more components. **(class level)**

```java
record Pair<A, B>(A first, B second) {}
```

```java
// use case
Pair<Integer, String> p = new Pair<>(1, "one");
int key = p.first();
String value = p.second();
// Whatever component names you declare (here first and second) become accessor methods.
// You can also use a custom class instead.

// records implement equals/hashCode, so they work as HashMap keys / HashSet elements
Set<Pair<Integer, Integer>> visited = new HashSet<>();
visited.add(new Pair<>(0, 1));
System.out.println(visited.contains(new Pair<>(0, 1)));   // true
```

Class version, if you cannot use records (class level, must be `static` when nested):

```java
static class PairC<K, V> {
    K key;
    V value;
    PairC(K key, V value) {
        this.key = key;
        this.value = value;
    }
    // Override equals and hashCode if used in HashSet / HashMap
}
```

---

## III. Strings & Math

### 9. Strings, StringBuilder, Character

**String basics**

```java
String s1 = new String("Hello");
String s2 = new String("Hello");
System.out.println(s1 == s2);       // false, different objects
System.out.println(s1.equals(s2));  // true, same content

String s3 = "Hello";
String s4 = "Hello";
System.out.println(s3 == s4);       // true, same literal in the string pool
System.out.println(s3.equals(s4));  // true, same content
// Always compare strings with equals(), never ==.

String str1 = "hello";
char ch = str1.charAt(0);                // 'h'
int len = str1.length();                 // 5
String sub = str1.substring(1, 4);       // "ell" (end index exclusive)
boolean has = str1.contains("lo");       // true
String[] parts = "a,b,c".split(",");     // {"a", "b", "c"}
char[] charArray = str1.toCharArray();   // convert to char array

// substring example
String str = "UditDabsay";
String first = str.substring(0, 4);      // "Udit"
String last = str.substring(4);          // "Dabsay"
```

Important methods:
```
length(), charAt(int), substring(int), substring(int,int), indexOf(String),
lastIndexOf(String), contains(CharSequence), startsWith(String), endsWith(String),
equals(Object), equalsIgnoreCase(String), compareTo(String), isEmpty(),
toLowerCase(), toUpperCase(), trim(), replace(char,char),
replace(CharSequence,CharSequence), replaceAll(String,String), split(String),
concat(String), toCharArray(), valueOf(Object)
```

**More String utilities**

```java
String joined = String.join("-", "a", "b", "c");      // "a-b-c"
String repeated = "ab".repeat(3);                     // "ababab" (Java 11+)
String formatted = String.format("%d items, %.2f avg, %s", 3, 2.5, "ok");
System.out.printf("%5d|%-5d|%05d%n", 42, 42, 42);     // "   42|42   |00042"
boolean blank = "   ".isBlank();                      // true (Java 11+)
String stripped = "  hi  ".strip();                   // "hi"
int c = "apple".compareTo("banana");                  // negative (lexicographic)
String fromChars = String.valueOf(new char[]{'a', 'b'}); // "ab"

// Text block (Java 15+)
String json = """
        {
          "name": "Udit"
        }
        """;
```

**Iterating over a String**

```java
String text = "hello";

// 1. index loop
for (int i = 0; i < text.length(); i++) {
    char c1 = text.charAt(i);
    System.out.print(c1 + " ");
}

// 2. for-each over a char array
for (char c2 : text.toCharArray()) {
    System.out.print(c2 + " ");
}

// 3. streams
text.chars().forEach(c3 -> System.out.print((char) c3 + " "));
```

**StringBuilder (mutable, fast for string manipulation)**

```java
StringBuilder sb1 = new StringBuilder();            // empty
StringBuilder sb2 = new StringBuilder("hello");     // with initial text
StringBuilder sb3 = new StringBuilder(50);          // with initial capacity

StringBuilder sb = new StringBuilder();
sb.append("Competitive ").append("Programming");    // "Competitive Programming"
sb.insert(0, "Java ");                              // "Java Competitive Programming"
sb.delete(5, 9);                                    // removes "Comp" -> "Java etitive Programming"
sb.reverse();                                       // reverses the content
char ch2 = sb.charAt(1);                            // get character at an index
sb.setCharAt(0, 'a');                               // set character at a position
System.out.println(sb.toString());                  // convert back to String

// iterate (same styles as String)
for (int i = 0; i < sb.length(); i++) {
    System.out.print(sb.charAt(i) + " ");
}
```

**Character helpers**

```java
Character.isDigit('5');          // true
Character.isLetter('x');         // true
Character.isLetterOrDigit('_');  // false
Character.isUpperCase('A');      // true
Character.toUpperCase('a');      // 'A'
Character.getNumericValue('7');  // 7
```

---

### 10. Math & Misc Utilities

```java
Math.abs(-5);                    // 5
Math.min(3, 7);                  // 3
Math.max(3, 7);                  // 7
Math.sqrt(16);                   // 4.0
Math.cbrt(27);                   // 3.0
Math.pow(2, 10);                 // 1024.0  (returns double)
Math.log(Math.E);                // 1.0 (natural log)
Math.log10(1000);                // 3.0
Math.ceil(4.1);                  // 5.0
Math.floor(4.9);                 // 4.0
Math.round(4.5);                 // 5  (returns long for double input)
Math.hypot(3, 4);                // 5.0
Math.addExact(1, 2);             // throws ArithmeticException on overflow

Random rnd = new Random();
int r = rnd.nextInt(10);                         // 0..9

long start = System.nanoTime();                  // timing
long ms = System.currentTimeMillis();

Objects.equals(null, null);                      // null-safe equals -> true
Objects.hash(1, 2, 3);                           // for hashCode()

Scanner sc = new Scanner(System.in);             // simple input (slow for big inputs)
int n = sc.nextInt();
String word = sc.next();
String line = sc.nextLine();
```


---

## IV. Data Structures (Collections Framework)

### 11. Arrays

```java
int[] a = new int[5];                    // default values (0)
String[] names = {"A", "B", "C"};        // literal
double[] values = new double[]{1.1, 2.2, 3.3};
int[] arr2 = {1, 2, 3, 4, 5};

// important field -> length (no parentheses, unlike String.length())
System.out.println(arr2.length);

// 2D arrays
int[][] matrix = new int[3][4];
int[][] matrixLiteral = {{1, 2}, {3, 4}};
int[][] jagged = new int[3][];           // rows can have different lengths
jagged[0] = new int[]{1};
jagged[1] = new int[]{1, 2};

// filling a 2D array: Arrays.fill only fills one row at a time
for (int[] row : matrix) Arrays.fill(row, -1);

// sorting
Arrays.sort(arr2);                       // ascending
Arrays.sort(arr2, 1, 4);                 // sort only indexes 1..3

// descending order needs a boxed array (Integer[]), not int[]
Integer[] boxed = {5, 2, 9};
Arrays.sort(boxed, Comparator.reverseOrder());

// sort a 2D array by a column
int[][] intervals = {{3, 4}, {1, 2}, {2, 9}};
Arrays.sort(intervals, (x, y) -> Integer.compare(x[0], y[0]));

// filling, copying
Arrays.fill(a, 10);                                    // fills all elements with 10
int[] arr4 = Arrays.copyOf(arr2, arr2.length);         // full copy
int[] arr5 = Arrays.copyOfRange(arr2, 1, 3);           // index 1 (inclusive) to 3 (exclusive)
int[] arr6 = arr2.clone();                             // shallow copy

// comparing and searching
boolean same = Arrays.equals(arr2, arr4);              // true
boolean deepSame = Arrays.deepEquals(matrix, matrixLiteral);
int pos = Arrays.binarySearch(arr2, 3);                // array must be sorted

// printing (debugging)
System.out.println(Arrays.toString(arr2));             // 1D
System.out.println(Arrays.deepToString(matrix));       // 2D

// conversions
List<Integer> asList = Arrays.asList(boxed);           // fixed-size view backed by the array
List<Integer> fromPrimitive = Arrays.stream(arr2).boxed().collect(Collectors.toList());
int[] backToPrimitive = fromPrimitive.stream().mapToInt(Integer::intValue).toArray();
int maxVal = Arrays.stream(arr2).max().getAsInt();
// Pitfall: Arrays.asList(intArray) gives List<int[]>, not List<Integer>.
```

---

### 12. Collections Basics & Utilities

**Quick creation with `of` methods**
```java
List.of(1, 2, 3);                        // immutable list (Java 9+)
Set.of("udit", "rahul");                 // immutable set (Java 9+)
Map.of("udit", 1, "rahul", 7);           // immutable map (up to 10 pairs) (Java 9+)
Map.ofEntries(Map.entry("a", 1), Map.entry("b", 2));  // immutable map, any size (Java 9+)

// Immutable collections throw UnsupportedOperationException on add/remove.
// Wrap in a constructor to get a mutable copy:
List<Integer> mutable = new ArrayList<>(List.of(1, 2, 3));
Set<Integer> mutableSet = new HashSet<>(Set.of(1, 2, 3));

// Arrays.asList is fixed-size: set() works, add()/remove() throw.
List<String> fixed = Arrays.asList("A", "B");
```

**Standard Iteration & Safe Removal**
```java
List<String> items = new ArrayList<>(List.of("A", "B", "remove_me", "C"));

// Most collections (List, Set, Queue) support enhanced for-loops:
for (String item : items) {
    System.out.println(item);
}

// To safely remove elements while iterating, you MUST use an Iterator.
// Pitfall: removing inside a for-each loop throws ConcurrentModificationException.
Iterator<String> it = items.iterator();
while (it.hasNext()) {
    if (it.next().equals("remove_me")) {
        it.remove();
    }
}

// Alternatively (Java 8+ safe bulk removal): 
items.removeIf(item -> item.equals("remove_me"));
```

**Collections Utility Methods**
```java
List<Integer> nums = new ArrayList<>(List.of(5, 3, 8, 1));

Collections.sort(nums);                          // [1, 3, 5, 8]
Collections.reverse(nums);                       // [8, 5, 3, 1]
Collections.min(nums);                           // 1
Collections.max(nums);                           // 8
Collections.frequency(nums, 3);                  // counts occurrences -> 1
Collections.sort(nums);                          // binarySearch needs a sorted list
Collections.binarySearch(nums, 5);               // index 2 (negative if not found)
Collections.swap(nums, 0, 1);
Collections.shuffle(nums);
nums.sort(Collections.reverseOrder());           // descending
List<String> repeated = Collections.nCopies(3, "x");   // [x, x, x]
List<Integer> readOnly = Collections.unmodifiableList(nums);
```

---

### 13. Lists

**ArrayList (dynamic array)**
```java
List<String> names = new ArrayList<>();
names.add("Udit");
names.add("Dabsay");

// index-based iteration (fast random access)
for (int i = 0; i < names.size(); i++) {
    System.out.println(names.get(i));
}

List<Integer> list = new ArrayList<>();
list.add(10);
list.add(20);
list.add(0, 5);                          // add 5 at index 0        -> [5, 10, 20]
System.out.println(list.get(0));         // 5
list.set(1, 15);                         // replace index 1 (10)    -> [5, 15, 20]
list.remove(Integer.valueOf(20));        // remove by VALUE         -> [5, 15]
list.remove(0);                          // remove by INDEX         -> [15]

// Pitfall: with List<Integer>, remove(int) removes by index, remove(Object) by value.
```

**2D List**
```java
List<List<Integer>> list2d = new ArrayList<>();
list2d.add(new ArrayList<>(Arrays.asList(1, 2)));
list2d.add(new ArrayList<>(Arrays.asList(3, 4, 5)));

// adjacency list pattern (graphs)
int n = 5;
List<List<Integer>> adj = new ArrayList<>();
for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
adj.get(0).add(1);
```

**LinkedList (doubly linked list)**

Good for frequent insertions/deletions at the ends or at a known position (via iterator). Not good for random access: `get(index)` is O(N).
```java
LinkedList<String> linkedList = new LinkedList<>();
linkedList.add("first");
linkedList.addLast("last");
linkedList.addFirst("new first");
linkedList.removeFirst();
System.out.println(linkedList.peekFirst());
```

---

### 14. Sets

**HashSet (unordered, no duplicates, O(1) average)**
```java
Set<String> set = new HashSet<>();               // use the Set interface for declaration
set.add("Apple");
set.add("Banana");
set.add("Apple");                                // ignored, duplicates not allowed
System.out.println(set.contains("Banana"));      // true
set.remove("Apple");

// `addAll` = union, `retainAll` = intersection, `removeAll` = difference.
```

**LinkedHashSet**

Same as `HashSet` but iterates in insertion order.
```java
Set<Integer> ordered = new LinkedHashSet<>(List.of(3, 1, 2));   // iterates 3, 1, 2
```

**TreeSet (sorted, no duplicates, O(log N))**

Stores elements in sorted order. Useful for min/max and nearest-element queries. Declare it as `TreeSet` (not `Set`) to get the navigation methods.
```java
TreeSet<Integer> treeSet = new TreeSet<>();
treeSet.add(5);
treeSet.add(2);
treeSet.add(8);                                  // set = {2, 5, 8}
System.out.println(treeSet.first());             // 2
System.out.println(treeSet.last());              // 8
System.out.println(treeSet.lower(5));            // 2 (greatest element  < 5)
System.out.println(treeSet.higher(5));           // 8 (smallest element  > 5)
System.out.println(treeSet.floor(5));            // 5 (greatest element <= 5)
System.out.println(treeSet.ceiling(5));          // 5 (smallest element >= 5)

treeSet.pollFirst();                             // remove and return smallest
treeSet.pollLast();                              // remove and return largest
treeSet.headSet(5);                              // elements < 5   (view)
treeSet.tailSet(5);                              // elements >= 5  (view)
treeSet.descendingSet();                         // reverse-order view
```

---

### 15. Maps

**HashMap (unordered, O(1) average)**
```java
Map<String, Integer> map = new HashMap<>();     // use the Map interface for declaration
map.put("Apple", 10);
map.put("Banana", 20);
System.out.println(map.get("Apple"));                       // 10
System.out.println(map.getOrDefault("Mango", 0));           // 0
System.out.println(map.containsKey("Apple"));               // true
System.out.println(map.containsValue(20));                  // true
map.putIfAbsent("Cherry", 5);
map.remove("Banana");

// iterate over entries (key-value pairs)
for (var entry : map.entrySet()) {              // var = Java 10+, optional
    System.out.println(entry.getKey() + " = " + entry.getValue());
}
// explicit type: for (Map.Entry<String, Integer> entry : map.entrySet())

// common idioms
Map<Character, Integer> freq = new HashMap<>();
for (char c : "banana".toCharArray()) {
    freq.merge(c, 1, Integer::sum);                         // frequency count
    // or: freq.put(c, freq.getOrDefault(c, 0) + 1);
}

Map<String, List<Integer>> groups = new HashMap<>();
groups.computeIfAbsent("even", k -> new ArrayList<>()).add(2);   // multi-map
```

**TreeMap (sorted by key, O(log N))**
Declare it as `TreeMap` (not `Map`) to get the navigation methods.
```java
TreeMap<Integer, String> treeMap = new TreeMap<>();
treeMap.put(3, "C");
treeMap.put(1, "A");
treeMap.put(2, "B");
System.out.println(treeMap.firstKey());          // 1
System.out.println(treeMap.lastKey());           // 3
System.out.println(treeMap.floorKey(2));         // 2 (greatest key <= 2)
System.out.println(treeMap.ceilingKey(2));       // 2 (smallest key >= 2)
System.out.println(treeMap.lowerKey(2));         // 1 (greatest key <  2)
System.out.println(treeMap.higherKey(2));        // 3 (smallest key >  2)
```

**LinkedHashMap**
Like `HashMap` but iterates in insertion order.
```java
Map<String, Integer> ordered = new LinkedHashMap<>();
```

**ConcurrentHashMap (thread-safe)**
Does not allow `null` keys or values. Iterators are weakly consistent.
```java
ConcurrentHashMap<Integer, String> map2 = new ConcurrentHashMap<>(16);

// atomic operations
map2.putIfAbsent(1, "A");                        // {1=A}
map2.computeIfAbsent(2, k -> "B");               // {1=A, 2=B}
map2.compute(1, (k, v) -> v + "X");              // {1=AX, 2=B}  (v is null if the key is absent!)
map2.merge(3, "C", String::concat);              // {1=AX, 2=B, 3=C} (key absent -> puts "C"; present -> concatenates)
map2.computeIfPresent(2, (k, v) -> v + "!");     // {1=AX, 2=B!, 3=C}
```

---

### 16. Stacks, Queues, and Deques

**Stack (LIFO)**
Prefer `Deque` (`ArrayDeque`) over the legacy `Stack` class. `Stack` extends `Vector`, so every method is synchronized, which makes it slower.
```java
// Recommended
Deque<Integer> stack = new ArrayDeque<>();
stack.push(10);                                  // add to top
stack.push(20);
System.out.println(stack.peek());                // look at top (20)
System.out.println(stack.pop());                 // remove from top (20)
System.out.println(stack.isEmpty());

// Legacy Stack class
Stack<Integer> legacy = new Stack<>();
legacy.push(10);
legacy.push(20);
// iteration order of Stack is insertion order: BOTTOM -> TOP (10, 20)
```

**Queue (FIFO)**
```java
Queue<Integer> q = new LinkedList<>();           // works, allows null
Queue<Integer> q1 = new ArrayDeque<>();          // usually the best choice (no nulls)

Queue<Integer> queue = new ArrayDeque<>();
queue.offer(10);                                 // add to rear
queue.offer(20);
System.out.println(queue.peek());                // look at front (10)
System.out.println(queue.poll());                // remove from front (10)

// FIFO order loop
while (!queue.isEmpty()) {
    System.out.println(queue.poll());
}
```

**PriorityQueue (min-heap by default)**
Elements are ordered by natural order or a given `Comparator`.
```java
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.offer(5);
minHeap.offer(1);
minHeap.offer(10);
System.out.println(minHeap.poll());              // 1 (smallest)

// max-heap
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
maxHeap.offer(5);
maxHeap.offer(1);

// custom order on arrays, e.g. {node, distance} sorted by distance
PriorityQueue<int[]> pq = new PriorityQueue<>((x, y) -> Integer.compare(x[1], y[1]));
pq.offer(new int[]{0, 7});
pq.offer(new int[]{1, 3});
System.out.println(pq.poll()[0]);                // 1
```

**Deque (double-ended queue)**
```java
Deque<Integer> deque = new ArrayDeque<>();
deque.addFirst(1);                               // add to front
deque.addLast(2);                                // add to back
System.out.println(deque.peekFirst());           // 1
System.out.println(deque.peekLast());            // 2
deque.removeFirst();
deque.removeLast();
// pollFirst() / pollLast() return null on empty instead of throwing
```

---

## V. Functional Java

### 17. Lambdas, streams, Optional

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

---

### 18. Custom comparators

Natural ordering (`Comparable`), custom ordering (`Comparator`), and `equals`/`hashCode` for hash-based collections.

```java
// class level (named Pt to avoid clashing with geometry Point in the template)
static class Pt implements Comparable<Pt> {
    int x, y;

    Pt(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // natural ordering: sort by x, then by y
    @Override
    public int compareTo(Pt other) {
        if (this.x != other.x) {
            return Integer.compare(this.x, other.x);
        }
        return Integer.compare(this.y, other.y);
    }

    @Override
    public boolean equals(Object o) {            // important for hash-based collections
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pt point = (Pt) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {                      // important for hash-based collections
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
```

```java
List<Pt> points = new ArrayList<>();
points.add(new Pt(3, 5));
points.add(new Pt(1, 2));
points.add(new Pt(3, 1));
points.add(new Pt(2, 5));
points.add(new Pt(1, 2));                     // duplicate for set/map testing

// 1.1 sort using natural ordering (compareTo)
List<Pt> pointsNaturalSort = new ArrayList<>(points);       // copy
Collections.sort(pointsNaturalSort);
System.out.println(pointsNaturalSort);
// [(1, 2), (1, 2), (2, 5), (3, 1), (3, 5)]

// 1.2 custom Comparator: y descending, then x ascending
Comparator<Pt> customPointComparator = new Comparator<Pt>() {
    @Override
    public int compare(Pt p1, Pt p2) {
        if (p1.y != p2.y) {
            return Integer.compare(p2.y, p1.y);  // descending y
        }
        return Integer.compare(p1.x, p2.x);      // ascending x
    }
};

List<Pt> pointsCustomSort = new ArrayList<>(points);        // fresh copy
Collections.sort(pointsCustomSort, customPointComparator);
System.out.println(pointsCustomSort);
// [(2, 5), (3, 5), (1, 2), (1, 2), (3, 1)]

// the same comparator, written concisely
Comparator<Pt> sameThing = Comparator
        .comparingInt((Pt p) -> p.y).reversed()
        .thenComparingInt(p -> p.x);
// others: Comparator.comparing(...), .thenComparing(...), .reversed(), Comparator.naturalOrder()

// 1.3 PriorityQueue: min-heap by y, then x
PriorityQueue<Pt> pqMinY = new PriorityQueue<>(new Comparator<Pt>() {
    @Override
    public int compare(Pt p1, Pt p2) {
        if (p1.y != p2.y) {
            return Integer.compare(p1.y, p2.y);  // ascending y
        }
        return Integer.compare(p1.x, p2.x);      // ascending x
    }
});
pqMinY.addAll(points);
while (!pqMinY.isEmpty()) {
    System.out.print(pqMinY.poll() + " ");       // (3, 1) (1, 2) (1, 2) (2, 5) (3, 5)
}
System.out.println();

// 1.4 TreeSet with a comparator: y descending, then x ascending
TreeSet<Pt> treeSetCustom = new TreeSet<>(customPointComparator);
treeSetCustom.addAll(points);
System.out.println(treeSetCustom);
// [(2, 5), (3, 5), (1, 2), (3, 1)]
// The duplicate (1, 2) is dropped because the comparator returns 0 for it.
// TreeSet/TreeMap use the comparator, NOT equals/hashCode, to detect duplicates.
```