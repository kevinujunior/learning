# Java Interview & Coding Cheat Sheet

## 1. Primitive Data Types

Java has eight primitive data types.

| Type    | Size (bits) | Default Value | Wrapper Class  | Common Pitfalls                                         |
| :------ | :---------- | :------------ | :------------- | :------------------------------------------------------ |
| `boolean` | 1           | `false`       | `Boolean`      | Not 0/1; use `true`/`false`.                          |
| `byte`    | 8           | `0`           | `Byte`         | Range -128 to 127; overflow/underflow.                |
| `short`   | 16          | `0`           | `Short`        | Range -32,768 to 32,767.                              |
| `char`    | 16          | `'\u0000'`    | `Character`    | Represents a single Unicode character, not a string.  |
| `int`     | 32          | `0`           | `Integer`      | Most common integer type.                             |
| `long`    | 64          | `0L`          | `Long`         | Use `L` suffix for literals (e.g., `123L`).           |
| `float`   | 32          | `0.0f`        | `Float`        | Use `f` suffix for literals (e.g., `3.14f`). Precision issues. |
| `double`  | 64          | `0.0d`        | `Double`       | Default for floating-point literals. Precision issues. |

### Autoboxing / Unboxing

*   **Autoboxing**: Automatic conversion from primitive to its corresponding wrapper class.
    *   `Integer i = 10;` (int to Integer)
*   **Unboxing**: Automatic conversion from wrapper class to its corresponding primitive.
    *   `int j = i;` (Integer to int)

**Common Pitfalls**:
*   `NullPointerException` during unboxing if wrapper object is `null`.
*   Performance overhead due to object creation/destruction.
*   Equality (`==`) with wrapper types compares object references, not values (unless autoboxing/unboxing makes it primitive). Use `.equals()`.

### Type Promotion and Casting Rules

*   **Promotion**: When operands of different types are in an expression, the "smaller" type is promoted to the "larger" type.
    *   `byte -> short -> int -> long -> float -> double`
    *   `char` promotes to `int`.
    *   Example: `int + long` results in `long`.
*   **Casting**: Explicit conversion of one data type to another.
    *   **Widening (Implicit)**: Smaller to larger type. No data loss.
        *   `int i = 10; long l = i;`
    *   **Narrowing (Explicit)**: Larger to smaller type. Possible data loss, requires cast.
        *   `double d = 10.5; int i = (int) d;` (i becomes 10)
        *   `long l = 1000L; byte b = (byte) l;` (b becomes -24, data loss due to overflow)

**Common Pitfalls**:
*   Data loss during narrowing conversions.
*   `ClassCastException` for object type casting (e.g., trying to cast a `String` to an `Integer`).

## 2. String & Text Utility Classes

### 2.1 String

*   **Declaration / Instantiation**: Immutable sequence of characters.
    ```java
    String s1 = "hello"; // String literal, stored in String Pool (preferred for fixed strings)
    String s2 = new String("world"); // New object on heap (less efficient for fixed strings)
    String s3 = "hello"; // s1 == s3 (same object reference from pool)
    ```
*   **Important Attributes**: Immutable, thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                              | Description                                         | Example Usage                                      |
    | :------------------------------------- | :-------------------------------------------------- | :------------------------------------------------- |
    | `char charAt(int index)`               | Returns char at specified index.                    | `s1.charAt(0)` -> 'h'                              |
    | `int length()`                         | Returns length of string.                           | `s1.length()` -> 5                                 |
    | `boolean equals(Object anotherString)` | Compares string content.                            | `s1.equals(s3)` -> true                            |
    | `boolean equalsIgnoreCase(String other)` | Compares string content, ignoring case.             | `"Hello".equalsIgnoreCase("hello")` -> true        |
    | `int compareTo(String anotherString)`  | Lexicographically compares two strings.             | `"a".compareTo("b")` -> < 0                        |
    | `int indexOf(String str)`              | Returns index of first occurrence of substring.     | `s1.indexOf("ll")` -> 2                            |
    | `int lastIndexOf(String str)`          | Returns index of last occurrence.                   | `"banana".lastIndexOf("an")` -> 3                  |
    | `boolean contains(CharSequence s)`     | Checks if string contains sequence.                 | `s1.contains("ell")` -> true                       |
    | `boolean startsWith(String prefix)`    | Checks if string starts with prefix.                | `s1.startsWith("he")` -> true                      |
    | `boolean endsWith(String suffix)`      | Checks if string ends with suffix.                  | `s1.endsWith("lo")` -> true                        |
    | `String substring(int beginIndex)`     | Returns new string from beginIndex to end.          | `s1.substring(2)` -> "llo"                         |
    | `String substring(int begin, int end)` | Returns new string from begin (incl) to end (excl). | `s1.substring(1, 3)` -> "el"                       |
    | `String replace(char oldChar, char newChar)` | Replaces all occurrences of a char.                 | `s1.replace('l', 'p')` -> "heppo"                  |
    | `String replaceAll(String regex, String replacement)` | Replaces all substrings matching regex.             | `"hi mom".replaceAll("m", "d")` -> "hi dod"        |
    | `String[] split(String regex)`         | Splits string into array by delimiter (regex).      | `"a,b,c".split(",")` -> {"a", "b", "c"}            |
    | `String toLowerCase()`                 | Converts to lowercase.                              | `"HELLO".toLowerCase()` -> "hello"                 |
    | `String toUpperCase()`                 | Converts to uppercase.                              | `"hello".toUpperCase()` -> "HELLO"                 |
    | `String trim()`                        | Removes leading/trailing whitespace.                | `"  hello  ".trim()` -> "hello"                    |
    | `static String valueOf(type t)`        | Converts various types to string.                   | `String.valueOf(123)` -> "123"                     |
    | `boolean isEmpty()`                    | Checks if length is 0.                              | `"".isEmpty()` -> true                            |
    | `boolean isBlank()`                    | Checks if empty or contains only whitespace (Java 11+). | `"   ".isBlank()` -> true                          |

*   **Interview Notes**:
    *   **Immutability**: Why? Thread-safety, security, performance (String Pool).
    *   `+` operator for concatenation creates new `String` objects, inefficient in loops. Use `StringBuilder`/`StringBuffer`.
    *   `==` vs `.equals()`: `==` compares references, `.equals()` compares content. Always use `.equals()` for content comparison.

### 2.2 StringBuilder

*   **Declaration / Instantiation**: Mutable sequence of characters, **not thread-safe**. Preferred for single-threaded string manipulation.
    ```java
    StringBuilder sb = new StringBuilder(); // Default capacity 16
    StringBuilder sb2 = new StringBuilder("initial");
    ```
*   **Most Commonly Used Methods**:

    | Signature                               | Description                                         | Example Usage                                       |
    | :-------------------------------------- | :-------------------------------------------------- | :-------------------------------------------------- |
    | `StringBuilder append(type t)`          | Appends various types to the sequence.              | `sb.append("Hello").append(123)`                    |
    | `StringBuilder insert(int offset, type t)` | Inserts various types at specified offset.          | `sb.insert(5, " World")`                            |
    | `StringBuilder replace(int start, int end, String str)` | Replaces substring.                                 | `sb.replace(0, 5, "Hi")`                            |
    | `StringBuilder delete(int start, int end)` | Deletes characters from start (incl) to end (excl). | `sb.delete(0, 2)`                                   |
    | `StringBuilder reverse()`               | Reverses the character sequence.                    | `sb.reverse()`                                      |
    | `int length()`                          | Returns current length.                             | `sb.length()`                                       |
    | `char charAt(int index)`                | Returns char at specified index.                    | `sb.charAt(0)`                                      |
    | `String toString()`                     | Converts to `String`.                               | `sb.toString()`                                     |

*   **Time Complexity**: Most operations (append, insert, delete) are amortized O(1) or O(N) in worst case (due to array resizing).

### 2.3 StringBuffer

*   **Declaration / Instantiation**: Mutable sequence of characters, **thread-safe**. Slower than `StringBuilder` due to synchronization overhead. Use in multi-threaded environments.
    ```java
    StringBuffer sbuf = new StringBuffer();
    ```
*   **Most Commonly Used Methods**: Same methods as `StringBuilder`, but all methods are `synchronized`.
*   **Time Complexity**: Same as `StringBuilder`, but with synchronization overhead.

### 2.4 CharSequence

*   **Interface**: Represents a readable sequence of `char` values. `String`, `StringBuilder`, `StringBuffer` all implement `CharSequence`.
*   **Purpose**: Allows methods to accept any of these types without needing separate overloads. E.g., `String.contains(CharSequence s)`.

### 2.5 StringJoiner (Java 8+)

*   **Declaration / Instantiation**: Constructs a sequence of characters separated by a delimiter, with an optional prefix and suffix.
    ```java
    StringJoiner sj = new StringJoiner(", ", "[", "]"); // delimiter, prefix, suffix
    StringJoiner sj2 = new StringJoiner("-"); // only delimiter
    ```
*   **Most Commonly Used Methods**:

    | Signature                       | Description                         | Example Usage                               |
    | :------------------------------ | :---------------------------------- | :------------------------------------------ |
    | `StringJoiner add(CharSequence newElement)` | Adds an element.                    | `sj.add("apple").add("banana")`             |
    | `String toString()`             | Returns the joined string.          | `sj.toString()` -> "[apple, banana]"        |
    | `int length()`                  | Returns the current length.         | `sj.length()` -> 16                         |
    | `StringJoiner merge(StringJoiner other)` | Merges with another `StringJoiner`. | `sj.merge(new StringJoiner("-").add("cherry"))` |

### 2.6 Formatter

*   **Purpose**: Provides `printf`-style formatting using conversion specifiers.
*   **Example Usage**:
    ```java
    String name = "Alice";
    int age = 30;
    String formattedString = String.format("Name: %s, Age: %d", name, age); // "Name: Alice, Age: 30"
    // Or with System.out.printf()
    System.out.printf("Pi: %.2f%n", Math.PI); // "Pi: 3.14"
    ```
*   **Common Format Specifiers**:
    *   `%s`: String
    *   `%d`: Decimal integer
    *   `%f`: Floating-point
    *   `%c`: Character
    *   `%b`: Boolean
    *   `%n`: New line (platform-independent)

### 2.7 Pattern & Matcher

*   **Purpose**: Classes for regular expression (regex) operations. `Pattern` compiles a regex, `Matcher` performs operations on input using that pattern.
*   **Declaration / Instantiation**:
    ```java
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    String text = "My phone number is 123-456-7890.";
    String regex = "\\d{3}-\\d{3}-\\d{4}"; // Regex for phone number

    Pattern pattern = Pattern.compile(regex); // Compile the regex
    Matcher matcher = pattern.matcher(text);  // Create a matcher for the input text
    ```
*   **Most Commonly Used Methods**:

    | Signature                           | Description                                     | Example Usage                                       |
    | :---------------------------------- | :---------------------------------------------- | :-------------------------------------------------- |
    | `boolean matches()` (Matcher)       | Attempts to match entire region.                | `matcher.matches()` -> false (text is longer)       |
    | `boolean find()` (Matcher)          | Attempts to find next subsequence matching.     | `matcher.find()` -> true                            |
    | `String group()` (Matcher)          | Returns the input subsequence matched by previous `find()`. | `matcher.group()` -> "123-456-7890"               |
    | `String group(int group)` (Matcher) | Returns the subsequence captured by given group. | If regex `(\\d{3})-(\\d{3})-(\\d{4})`, `matcher.group(1)` -> "123" |
    | `int start()` (Matcher)             | Returns the start index of the match.           | `matcher.start()` -> 19                             |
    | `int end()` (Matcher)               | Returns the end index + 1 of the match.         | `matcher.end()` -> 31                               |
    | `String replaceAll(String replacement)` (Matcher) | Replaces all matches with replacement.          | `matcher.replaceAll("XXX-XXX-XXXX")`                |
    | `static boolean matches(String regex, CharSequence input)` (Pattern) | Convenience method for single match check.        | `Pattern.matches("\\d+", "123")` -> true          |

*   **Interview Notes**:
    *   `Pattern` is immutable and thread-safe. Compile patterns once if used multiple times.
    *   `Matcher` is stateful and not thread-safe. Create a new `Matcher` for each input string.

## 3. Objects, Arrays, Collections Utility Classes

### 3.1 `java.util.Objects` (Java 7+)

*   **Purpose**: Provides utility methods for operating on objects, often for null-safe checks.
*   **Most Commonly Used Methods**:

    | Signature                         | Description                                     | Example Usage                                |
    | :-------------------------------- | :---------------------------------------------- | :------------------------------------------- |
    | `static boolean equals(Object a, Object b)` | Returns `true` if objects are equal (or both null). | `Objects.equals("hi", null)` -> false        |
    | `static int hash(Object... values)` | Generates a hash code for a sequence of input values. | `Objects.hash("a", 1, true)`                 |
    | `static <T> T requireNonNull(T obj)` | Checks if object is null, throws `NullPointerException` if so. | `Objects.requireNonNull(myObj)`              |
    | `static <T> T requireNonNull(T obj, String message)` | Same, with custom message.                      | `Objects.requireNonNull(myObj, "obj is null")` |
    | `static String toString(Object o)`  | Returns `o.toString()` or "null" if `o` is null. | `Objects.toString(null)` -> "null"           |

### 3.2 `java.util.Arrays`

*   **Purpose**: Provides utility methods for arrays (sorting, searching, filling, converting).
*   **Most Commonly Used Methods**:

    | Signature                                       | Description                                     | Example Usage                                   |
    | :---------------------------------------------- | :---------------------------------------------- | :---------------------------------------------- |
    | `static <T> List<T> asList(T... a)`             | Returns a fixed-size `List` backed by array.    | `List<String> l = Arrays.asList("a", "b");`     |
    | `static int binarySearch(type[] a, type key)`   | Searches for element using binary search (must be sorted). | `Arrays.binarySearch(new int[]{1,5,9}, 5)` -> 1 |
    | `static void sort(type[] a)`                    | Sorts array in ascending order.                 | `Arrays.sort(new int[]{3,1,2})` -> {1,2,3}       |
    | `static void sort(T[] a, Comparator<? super T> c)` | Sorts array using a custom `Comparator`.        | `Arrays.sort(strArr, String::compareToIgnoreCase)` |
    | `static void fill(type[] a, type val)`          | Fills array with specified value.               | `Arrays.fill(new int[5], 0)` -> {0,0,0,0,0}      |
    | `static boolean equals(type[] a, type[] b)`     | Checks if two arrays are equal (element-wise).  | `Arrays.equals(new int[]{1}, new int[]{1})` -> true |
    | `static String toString(type[] a)`              | Returns string representation of array.         | `Arrays.toString(new int[]{1,2})` -> "[1, 2]"    |
    | `static int[] copyOf(int[] original, int newLength)` | Copies array to new length.                     | `Arrays.copyOf(new int[]{1,2}, 4)` -> {1,2,0,0}  |
    | `static IntStream stream(int[] array)` (Java 8+) | Returns a sequential `IntStream`.               | `Arrays.stream(arr).sum()`                      |

### 3.3 `java.util.Collections`

*   **Purpose**: Provides static utility methods that operate on or return collections. Includes sorting, searching, reversing, thread-safe wrappers, etc.
*   **Most Commonly Used Methods**:

    | Signature                                       | Description                                         | Example Usage                                          |
    | :---------------------------------------------- | :-------------------------------------------------- | :----------------------------------------------------- |
    | `static <T> void sort(List<T> list)`            | Sorts elements of a `List` (natural order).         | `Collections.sort(myList)`                             |
    | `static <T> void sort(List<T> list, Comparator<? super T> c)` | Sorts elements of a `List` with a `Comparator`.     | `Collections.sort(myList, Collections.reverseOrder())` |
    | `static <T> int binarySearch(List<? extends Comparable<? super T>> list, T key)` | Searches sorted `List` for element.                 | `Collections.binarySearch(myList, "element")`          |
    | `static void reverse(List<?> list)`             | Reverses order of elements in `List`.               | `Collections.reverse(myList)`                          |
    | `static void shuffle(List<?> list)`             | Randomly shuffles elements in `List`.               | `Collections.shuffle(myList)`                          |
    | `static <T> T min(Collection<? extends T> coll)` | Returns minimum element.                            | `Collections.min(myList)`                              |
    | `static <T> T max(Collection<? extends T> coll)` | Returns maximum element.                            | `Collections.max(myList)`                              |
    | `static <T> List<T> emptyList()`                | Returns an immutable empty list.                    | `Collections.emptyList()`                              |
    | `static <T> Set<T> emptySet()`                  | Returns an immutable empty set.                     | `Collections.emptySet()`                               |
    | `static <K, V> Map<K, V> emptyMap()`            | Returns an immutable empty map.                     | `Collections.emptyMap()`                               |
    | `static <T> List<T> singletonList(T o)`         | Returns an immutable list containing only the specified object. | `Collections.singletonList("item")`                    |
    | `static <T> List<T> unmodifiableList(List<? extends T> list)` | Returns an unmodifiable view of the specified list. | `Collections.unmodifiableList(myMutableList)`          |
    | `static <T> List<T> synchronizedList(List<T> list)` | Returns a thread-safe (synchronized) list.          | `Collections.synchronizedList(new ArrayList<>())`      |

## 4. Collections Framework (Core + Concurrent)

### 4.1 Iteration Techniques (General)

*   **for-loop (indexed)**: For `List` or arrays.
    ```java
    List<String> list = new ArrayList<>(Arrays.asList("A", "B"));
    for (int i = 0; i < list.size(); i++) {
        System.out.println(list.get(i));
    }
    ```
*   **enhanced for (forEach loop)**: For any `Iterable` (all collections). Simpler, read-only iteration.
    ```java
    for (String s : list) {
        System.out.println(s);
    }
    Set<Integer> set = new HashSet<>(Arrays.asList(1, 2));
    for (Integer i : set) {
        System.out.println(i);
    }
    ```
*   **Iterator**: Universal for all `Collection` types. Allows `remove()` during iteration.
    ```java
    Iterator<String> it = list.iterator();
    while (it.hasNext()) {
        String s = it.next();
        if (s.equals("A")) {
            it.remove(); // Safe removal
        }
        System.out.println(s);
    }
    ```
*   **ListIterator**: For `List` only. Bi-directional iteration, allows `add()`, `set()`, `remove()`.
    ```java
    ListIterator<String> lit = list.listIterator();
    while (lit.hasNext()) {
        String s = lit.next();
        if (s.equals("B")) {
            lit.set("C"); // Modify
        }
    }
    while (lit.hasPrevious()) { // Iterate backwards
        System.out.println(lit.previous());
    }
    ```
*   **forEach / Streams (Java 8+)**: Functional approach.
    ```java
    list.forEach(System.out::println); // Consumer
    list.stream()
        .filter(s -> s.startsWith("A"))
        .map(String::toUpperCase)
        .forEach(System.out::println); // Stream pipeline
    ```
*   **Interview Notes on Iteration**:
    *   **Fail-fast vs Fail-safe**: Most `java.util` collections are **fail-fast** (throw `ConcurrentModificationException` if modified structurally during iteration by another thread or the iterator itself, unless `it.remove()` is used). Concurrent collections are **fail-safe** (operate on a snapshot, don't throw exception, but may not reflect latest state).
    *   Avoid modifying collections in enhanced for-loops (except through methods that do not modify the underlying structure or using a new collection). Use `Iterator.remove()` for safe removal.

### 4.2 List

Ordered collection (sequence). Elements can be accessed by their integer index. Can contain duplicate elements.

#### 4.2.1 ArrayList

*   **Declaration / Instantiation**: Resizable array implementation. Best for frequent random access (`get(index)`).
    ```java
    List<String> al = new ArrayList<>(); // Preferred: interface on left, concrete on right
    ArrayList<String> concreteAl = new ArrayList<>(10); // Specify initial capacity
    ```
*   **Important Attributes**: Backed by an array, grows dynamically (doubles capacity typically).
*   **Null Handling**: Allows `null` elements.
*   **Thread-safety**: Not thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                              | Description                                         | Example Usage                          | Time Complexity |
    | :------------------------------------- | :-------------------------------------------------- | :------------------------------------- | :-------------- |
    | `boolean add(E e)`                     | Appends element to end.                             | `al.add("A")`                          | Amortized O(1)  |
    | `void add(int index, E element)`       | Inserts element at specified index.                 | `al.add(0, "B")`                       | O(N)            |
    | `E get(int index)`                     | Returns element at index.                           | `al.get(0)`                            | O(1)            |
    | `E set(int index, E element)`          | Replaces element at index.                          | `al.set(0, "C")`                       | O(1)            |
    | `E remove(int index)`                  | Removes element at index.                           | `al.remove(0)`                         | O(N)            |
    | `boolean remove(Object o)`             | Removes first occurrence of specified element.      | `al.remove("A")`                       | O(N)            |
    | `int indexOf(Object o)`                | Returns index of first occurrence, -1 if not found. | `al.indexOf("A")`                      | O(N)            |
    | `int size()`                           | Returns number of elements.                         | `al.size()`                            | O(1)            |
    | `void clear()`                         | Removes all elements.                               | `al.clear()`                           | O(N)            |
    | `boolean contains(Object o)`           | Returns true if list contains element.              | `al.contains("A")`                     | O(N)            |

*   **Interview Notes**:
    *   Good for `get` operations, bad for `add`/`remove` in middle (requires shifting elements).
    *   Prefer `List<String> list = new ArrayList<>();` over `ArrayList<String> list = new ArrayList<>();` for polymorphism.

#### 4.2.2 LinkedList

*   **Declaration / Instantiation**: Doubly-linked list implementation. Best for frequent insertions/deletions in middle.
    ```java
    List<String> ll = new LinkedList<>();
    LinkedList<String> concreteLl = new LinkedList<>(Arrays.asList("X", "Y"));
    ```
*   **Important Attributes**: Each element is a node with pointers to previous and next. Implements `List` and `Deque`.
*   **Null Handling**: Allows `null` elements.
*   **Thread-safety**: Not thread-safe.
*   **Most Commonly Used Methods**: Inherits most from `List`, but also has `Deque` methods.

    | Signature                              | Description                                         | Example Usage                          | Time Complexity |
    | :------------------------------------- | :-------------------------------------------------- | :------------------------------------- | :-------------- |
    | `boolean add(E e)`                     | Appends to end.                                     | `ll.add("A")`                          | O(1)            |
    | `void add(int index, E element)`       | Inserts at index.                                   | `ll.add(0, "B")`                       | O(N)            |
    | `E get(int index)`                     | Returns element at index.                           | `ll.get(0)`                            | O(N)            |
    | `E remove(int index)`                  | Removes element at index.                           | `ll.remove(0)`                         | O(N)            |
    | `void addFirst(E e)`                   | Adds to front.                                      | `ll.addFirst("F")`                     | O(1)            |
    | `void addLast(E e)`                    | Adds to end.                                        | `ll.addLast("L")`                      | O(1)            |
    | `E getFirst()`                         | Returns first element.                              | `ll.getFirst()`                        | O(1)            |
    | `E getLast()`                          | Returns last element.                               | `ll.getLast()`                         | O(1)            |
    | `E removeFirst()`                      | Removes and returns first element.                  | `ll.removeFirst()`                     | O(1)            |
    | `E removeLast()`                       | Removes and returns last element.                   | `ll.removeLast()`                      | O(1)            |

*   **Interview Notes**:
    *   Good for queue/stack implementations (`addFirst`, `removeFirst`, etc.).
    *   Bad for random access (`get(index)`) as it requires traversal.
    *   Higher memory overhead per element due to node objects.

#### 4.2.3 Vector

*   **Declaration / Instantiation**: Legacy class, similar to `ArrayList` (resizable array) but **thread-safe** (all methods synchronized). Rarely used in new code; prefer `ArrayList` with `Collections.synchronizedList()` or concurrent collections.
    ```java
    Vector<String> v = new Vector<>();
    ```
*   **Important Attributes**: Synchronized. Doubles its capacity by default when resizing.
*   **Null Handling**: Allows `null` elements.
*   **Time Complexity**: Similar to `ArrayList` but with overhead of synchronization.
*   **Interview Notes**:
    *   Mention it's an older synchronized alternative to `ArrayList`.
    *   Generally prefer `ArrayList` + `Collections.synchronizedList()` (if you need synchronization on specific operations) or `CopyOnWriteArrayList` (for specific concurrent use cases).

#### 4.2.4 CopyOnWriteArrayList

*   **Declaration / Instantiation**: A thread-safe variant of `ArrayList` where all mutative operations (add, set, remove, etc.) are implemented by making a fresh copy of the underlying array. Best for situations where reads vastly outnumber writes.
    ```java
    List<String> cowList = new CopyOnWriteArrayList<>();
    CopyOnWriteArrayList<String> concreteCowList = new CopyOnWriteArrayList<>();
    // Can initialize with an existing collection
    List<String> initialData = Arrays.asList("A", "B");
    CopyOnWriteArrayList<String> cowListFromCollection = new CopyOnWriteArrayList<>(initialData);
    ```
*   **Important Attributes**: Thread-safe by creating a new copy of the array on modification. Iterators do not throw `ConcurrentModificationException`. Iterators reflect the state of the list at the time the iterator was created.
*   **Null Handling**: Allows `null` elements.
*   **Thread-safety**: Thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                              | Description                                                                     | Example Usage                               | Time Complexity |
    | :------------------------------------- | :------------------------------------------------------------------------------ | :------------------------------------------ | :-------------- |
    | `boolean add(E e)`                     | Appends element to end. Creates a new array.                                    | `cowList.add("X")`                          | O(N)            |
    | `void add(int index, E element)`       | Inserts element at specified index. Creates a new array.                        | `cowList.add(0, "Y")`                       | O(N)            |
    | `boolean addIfAbsent(E e)`             | Adds the element if it's not already present. Creates a new array if added.     | `cowList.addIfAbsent("Z")`                  | O(N)            |
    | `E get(int index)`                     | Returns element at index.                                                       | `cowList.get(0)`                            | O(1)            |
    | `E set(int index, E element)`          | Replaces element at index. Creates a new array.                                 | `cowList.set(0, "W")`                       | O(N)            |
    | `E remove(int index)`                  | Removes element at index. Creates a new array.                                  | `cowList.remove(0)`                         | O(N)            |
    | `boolean remove(Object o)`             | Removes first occurrence of specified element. Creates a new array.             | `cowList.remove("X")`                       | O(N)            |
    | `int indexOf(Object o)`                | Returns index of first occurrence, -1 if not found.                             | `cowList.indexOf("Y")`                      | O(N)            |
    | `int size()`                           | Returns number of elements.                                                     | `cowList.size()`                            | O(1)            |
    | `void clear()`                         | Removes all elements. Creates a new empty array.                                | `cowList.clear()`                           | O(N)            |
    | `boolean contains(Object o)`           | Returns true if list contains element.                                          | `cowList.contains("Z")`                     | O(N)            |
    | `Iterator<E> iterator()`               | Returns an iterator that reflects the state of the list when created.           | `Iterator<String> it = cowList.iterator();` | O(1)            |

*   **Interview Notes**:
    *   Suitable when writes are rare, and reads are frequent, as reads don't require synchronization.
    *   Iterators are "snapshot" iterators; they operate on a copy of the array from when they were created and won't see subsequent modifications. This means no `ConcurrentModificationException`.
    *   Expensive for write operations due to array copying.
    *   Uses more memory than `ArrayList` because each write creates a new array.

### 4.3 Set

A collection that contains no duplicate elements. Models the mathematical set abstraction.

#### 4.3.1 HashSet

*   **Declaration / Instantiation**: Uses a hash table for storage. Offers constant time performance for basic operations (`add`, `remove`, `contains`, `size`), assuming good hash function.
    ```java
    Set<String> hs = new HashSet<>(); // Preferred
    HashSet<String> concreteHs = new HashSet<>(100); // Specify initial capacity
    ```
*   **Ordering**: No guaranteed order.
*   **Sorting**: Not sorted.
*   **Null Handling**: Allows one `null` element.
*   **Thread-safety**: Not thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                   | Description                                         | Example Usage         | Time Complexity |
    | :-------------------------- | :-------------------------------------------------- | :-------------------- | :-------------- |
    | `boolean add(E e)`          | Adds element if not already present.                | `hs.add("A")`         | O(1) average    |
    | `boolean remove(Object o)`  | Removes specified element.                          | `hs.remove("A")`      | O(1) average    |
    | `boolean contains(Object o)`| Returns true if set contains element.               | `hs.contains("A")`    | O(1) average    |
    | `int size()`                | Returns number of elements.                         | `hs.size()`           | O(1)            |
    | `void clear()`              | Removes all elements.                               | `hs.clear()`          | O(N)            |

*   **Interview Notes**:
    *   Requires correct `hashCode()` and `equals()` implementation for custom objects.
    *   Good for checking membership efficiently and storing unique items.
    *   Performance degrades with poor hash function or high collision rates.

#### 4.3.2 LinkedHashSet

*   **Declaration / Instantiation**: Hash table and a doubly-linked list implementation. Maintains insertion order.
    ```java
    Set<String> lhs = new LinkedHashSet<>();
    ```
*   **Ordering**: Maintains insertion order.
*   **Sorting**: Not sorted, but ordered by insertion.
*   **Null Handling**: Allows one `null` element.
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**: Slightly slower than `HashSet` due to maintaining the linked list, but still O(1) average for basic operations.
*   **Interview Notes**:
    *   Use when you need uniqueness *and* order of insertion.
    *   Useful for implementing LRU (Least Recently Used) caches (though `LinkedHashMap` is more direct).

#### 4.3.3 TreeSet

*   **Declaration / Instantiation**: Based on a `TreeMap` (Red-Black tree). Elements are stored in natural ordering or by a provided `Comparator`.
    ```java
    Set<String> ts = new TreeSet<>(); // Natural ordering (elements must be Comparable)
    Set<String> ts2 = new TreeSet<>(Comparator.reverseOrder()); // Custom comparator
    ```
*   **Ordering**: Natural ordering or by `Comparator`.
*   **Sorting**: Elements are always sorted.
*   **Null Handling**: Does **not** allow `null` elements (throws `NullPointerException`).
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**: O(log N) for `add`, `remove`, `contains` (due to tree traversal).
*   **Most Commonly Used Methods**: Same as `HashSet`, plus methods from `SortedSet` and `NavigableSet`.

    | Signature                           | Description                                         | Example Usage          | Time Complexity |
    | :---------------------------------- | :-------------------------------------------------- | :--------------------- | :-------------- |
    | `E first()`                         | Returns the first (lowest) element.                 | `ts.first()`           | O(log N)        |
    | `E last()`                          | Returns the last (highest) element.                 | `ts.last()`            | O(log N)        |
    | `E lower(E e)`                      | Returns largest element strictly less than `e`.     | `ts.lower("B")`        | O(log N)        |
    | `E higher(E e)`                     | Returns smallest element strictly greater than `e`. | `ts.higher("B")`       | O(log N)        |
    | `SortedSet<E> headSet(E toElement)` | Returns a view of the portion of this set whose elements are strictly less than `toElement`. | `ts.headSet("C")`      | O(log N)        |
    | `SortedSet<E> tailSet(E fromElement)` | Returns a view of the portion of this set whose elements are greater than or equal to `fromElement`. | `ts.tailSet("C")`      | O(log N)        |

*   **Interview Notes**:
    *   Elements must implement `Comparable` or a `Comparator` must be provided.
    *   Use when you need a sorted set or need range queries (`headSet`, `tailSet`).


#### 4.3.4 CopyOnWriteArraySet

*   **Declaration / Instantiation**: A thread-safe variant of `Set` that uses a `CopyOnWriteArrayList` internally. Best for sets that are rarely modified but frequently iterated.
    ```java
    Set<String> cowSet = new CopyOnWriteArraySet<>();
    CopyOnWriteArraySet<String> concreteCowSet = new CopyOnWriteArraySet<>();
    // Can initialize with an existing collection
    Set<String> initialSetData = new HashSet<>(Arrays.asList("A", "B"));
    CopyOnWriteArraySet<String> cowSetFromCollection = new CopyOnWriteArraySet<>(initialSetData);
    ```
*   **Important Attributes**: Thread-safe, backed by `CopyOnWriteArrayList`. Maintains insertion order. Iterators do not throw `ConcurrentModificationException` and reflect the state at creation.
*   **Null Handling**: Allows `null` elements.
*   **Thread-safety**: Thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                              | Description                                                                     | Example Usage                               | Time Complexity |
    | :------------------------------------- | :------------------------------------------------------------------------------ | :------------------------------------------ | :-------------- |
    | `boolean add(E e)`                     | Adds the specified element to this set if it is not already present.            | `cowSet.add("X")`                           | O(N)            |
    | `boolean addAll(Collection<? extends E> c)` | Adds all of the elements in the specified collection to this set.             | `cowSet.addAll(Arrays.asList("Y", "Z"))`    | O(N*M) - M is size of c |
    | `boolean remove(Object o)`             | Removes the specified element from this set if it is present.                   | `cowSet.remove("X")`                        | O(N)            |
    | `boolean contains(Object o)`           | Returns `true` if this set contains the specified element.                      | `cowSet.contains("Y")`                      | O(N)            |
    | `int size()`                           | Returns the number of elements in this set.                                     | `cowSet.size()`                             | O(1)            |
    | `void clear()`                         | Removes all of the elements from this set.                                      | `cowSet.clear()`                            | O(N)            |
    | `Iterator<E> iterator()`               | Returns an iterator over the elements in this set, reflecting its state at creation. | `Iterator<String> it = cowSet.iterator();`  | O(1)            |

*   **Interview Notes**:
    *   Similar characteristics to `CopyOnWriteArrayList` but enforces uniqueness like a `Set`.
    *   Ideal for event listeners or observer lists where changes are infrequent and iteration is common.
    *   Performance characteristics are dominated by the underlying `CopyOnWriteArrayList` – writes are expensive, reads are cheap.


### 4.4 Map

An object that maps keys to values. Cannot contain duplicate keys. Each key can map to at most one value.

#### 4.4.1 HashMap

*   **Declaration / Instantiation**: Hash table implementation. Provides constant time performance for basic operations (`get`, `put`, `remove`), assuming good hash function.
    ```java
    Map<String, Integer> hm = new HashMap<>(); // Preferred
    HashMap<String, Integer> concreteHm = new HashMap<>(16, 0.75f); // Initial capacity, load factor
    ```
*   **Ordering**: No guaranteed order.
*   **Null Handling**: Allows one `null` key and multiple `null` values.
*   **Thread-safety**: Not thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                   | Description                                         | Example Usage         | Time Complexity |
    | :-------------------------- | :-------------------------------------------------- | :-------------------- | :-------------- |
    | `V put(K key, V value)`     | Associates key with value. Returns old value or `null`. | `hm.put("A", 1)`      | O(1) average    |
    | `V get(Object key)`         | Returns value for key, or `null` if not found.      | `hm.get("A")`         | O(1) average    |
    | `V remove(Object key)`      | Removes mapping for key. Returns removed value.     | `hm.remove("A")`      | O(1) average    |
    | `boolean containsKey(Object key)` | Returns true if map contains key.                 | `hm.containsKey("A")` | O(1) average    |
    | `boolean containsValue(Object value)` | Returns true if map contains value.               | `hm.containsValue(1)` | O(N)            |
    | `int size()`                | Returns number of key-value mappings.               | `hm.size()`           | O(1)            |
    | `void clear()`              | Removes all mappings.                               | `hm.clear()`          | O(N)            |
    | `Set<K> keySet()`           | Returns a `Set` view of keys.                       | `hm.keySet()`         | O(1)            |
    | `Collection<V> values()`    | Returns a `Collection` view of values.              | `hm.values()`         | O(1)            |
    | `Set<Map.Entry<K, V>> entrySet()` | Returns a `Set` view of key-value mappings.       | `hm.entrySet()`       | O(1)            |
    | `V getOrDefault(Object key, V defaultValue)` (Java 8+) | Returns value for key or default if not found.      | `hm.getOrDefault("Z", 0)` | O(1)            |
    | `void forEach(BiConsumer<? super K, ? super V> action)` (Java 8+) | Performs action for each entry.                     | `hm.forEach((k,v) -> System.out.println(k+v))` | O(N)            |

*   **Iteration Techniques**:
    ```java
    // Iterate over keys
    for (String key : hm.keySet()) {
        System.out.println("Key: " + key + ", Value: " + hm.get(key));
    }
    // Iterate over entries (most efficient)
    for (Map.Entry<String, Integer> entry : hm.entrySet()) {
        System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
    }
    // Java 8+ forEach
    hm.forEach((key, value) -> System.out.println("Key: " + key + ", Value: " + value));
    ```
*   **Interview Notes**:
    *   Requires correct `hashCode()` and `equals()` for keys.
    *   Load factor and initial capacity affect performance. A load factor of 0.75 is a good default.
    *   Resizing operation can be expensive (rehashing all elements).

#### 4.4.2 LinkedHashMap

*   **Declaration / Instantiation**: Hash table and a doubly-linked list implementation. Maintains insertion order or access order.
    ```java
    Map<String, Integer> lhm = new LinkedHashMap<>(); // Maintains insertion order (default)
    // Access order: true for LRU cache behavior
    Map<String, Integer> lruCache = new LinkedHashMap<>(16, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
            return size() > 100; // Example: remove if cache exceeds 100 elements
        }
    };
    ```
*   **Ordering**: Insertion order (default) or access order (for LRU caches).
*   **Null Handling**: Allows one `null` key and multiple `null` values.
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**: Slightly slower than `HashMap` due to maintaining the linked list, but still O(1) average for basic operations.
*   **Interview Notes**:
    *   Excellent for implementing LRU caches when constructed with access order `true` and overriding `removeEldestEntry`.
    *   Use when you need uniqueness of keys *and* a predictable iteration order.

#### 4.4.3 TreeMap

*   **Declaration / Instantiation**: Red-Black tree implementation. Elements are stored in natural ordering of keys or by a provided `Comparator`.
    ```java
    Map<String, Integer> tm = new TreeMap<>(); // Natural ordering of keys
    Map<String, Integer> tm2 = new TreeMap<>(Comparator.reverseOrder()); // Custom comparator for keys
    ```
*   **Ordering**: Keys are always sorted.
*   **Null Handling**: Does **not** allow `null` keys (throws `NullPointerException`). Allows `null` values.
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**: O(log N) for `get`, `put`, `remove`.
*   **Most Commonly Used Methods**: Same as `HashMap`, plus methods from `SortedMap` and `NavigableMap`.

    | Signature                           | Description                                         | Example Usage          | Time Complexity |
    | :---------------------------------- | :-------------------------------------------------- | :--------------------- | :-------------- |
    | `K firstKey()`                      | Returns the first (lowest) key.                     | `tm.firstKey()`        | O(log N)        |
    | `K lastKey()`                       | Returns the last (highest) key.                     | `tm.lastKey()`         | O(log N)        |
    | `Map.Entry<K, V> firstEntry()`      | Returns the entry associated with the least key.    | `tm.firstEntry()`      | O(log N)        |
    | `Map.Entry<K, V> floorEntry(K key)` | Returns entry associated with the greatest key <= `key`. | `tm.floorEntry("B")`   | O(log N)        |
    | `K lowerKey(K key)`                 | Returns largest key strictly less than `key`.       | `tm.lowerKey("B")`     | O(log N)        |

*   **Interview Notes**:
    *   Keys must implement `Comparable` or a `Comparator` must be provided.
    *   Use when you need a sorted map or need to perform range queries on keys.

#### 4.4.4 Hashtable (Legacy)

*   **Declaration / Instantiation**: Legacy hash table implementation. Similar to `HashMap` but **thread-safe** (all methods synchronized). Does not allow `null` keys or values.
    ```java
    Hashtable<String, Integer> ht = new Hashtable<>();
    ```
*   **Ordering**: No guaranteed order.
*   **Null Handling**: Does **not** allow `null` keys or `null` values (throws `NullPointerException`).
*   **Thread-safety**: Thread-safe (synchronized).
*   **Time Complexity**: Similar to `HashMap` but with synchronization overhead.
*   **Interview Notes**:
    *   Older, slower alternative to `ConcurrentHashMap`. Rarely used in new code.
    *   Historical note: part of the original Java API, hence the name.


#### 4.4.5 ConcurrentHashMap

*   **Declaration / Instantiation**: A thread-safe, high-performance hash map that supports full concurrency of retrievals and high expected concurrency for updates. Does not require locking the entire map for most operations.
    ```java
    Map<String, Integer> chm = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, Integer> concreteChm = new ConcurrentHashMap<>();
    // Specify initial capacity
    ConcurrentHashMap<String, Integer> chmWithCapacity = new ConcurrentHashMap<>(16);
    ```
*   **Important Attributes**: Thread-safe without explicit synchronization for most operations. Allows multiple readers and writers concurrently. Iterators provide a "weakly consistent" view, reflecting elements existing at or since the creation of the iterator, and may or may not reflect insertions/updates since creation.
*   **Null Handling**: Does NOT allow `null` keys or `null` values.
*   **Thread-safety**: Thread-safe.
*   **Most Commonly Used Methods**:

    | Signature                                       | Description                                                                       | Example Usage                                       | Time Complexity |
    | :---------------------------------------------- | :-------------------------------------------------------------------------------- | :-------------------------------------------------- | :-------------- |
    | `V put(K key, V value)`                         | Maps the specified key to the specified value in this table.                      | `chm.put("A", 1)`                                   | Amortized O(1)  |
    | `V putIfAbsent(K key, V value)`                 | If the specified key is not already associated with a value, associates it.       | `chm.putIfAbsent("B", 2)`                           | Amortized O(1)  |
    | `V get(Object key)`                             | Returns the value to which the specified key is mapped, or `null` if not found.   | `chm.get("A")`                                      | O(1)            |
    | `V remove(Object key)`                          | Removes the key (and its corresponding value) from this map.                      | `chm.remove("A")`                                   | Amortized O(1)  |
    | `boolean remove(Object key, Object value)`      | Removes the entry for the specified key only if it is currently mapped to the specified value. | `chm.remove("B", 2)`                                | Amortized O(1)  |
    | `V replace(K key, V value)`                     | Replaces the entry for the specified key only if it is currently mapped to some value. | `chm.replace("C", 3)`                               | Amortized O(1)  |
    | `boolean replace(K key, V oldValue, V newValue)`| Replaces the entry for the specified key only if it is currently mapped to the specified old value. | `chm.replace("C", 3, 4)`                            | Amortized O(1)  |
    | `int size()`                                    | Returns the number of key-value mappings in this map.                             | `chm.size()`                                        | O(1) - best effort, may not reflect exact state |
    | `boolean containsKey(Object key)`               | Returns `true` if this map contains a mapping for the specified key.              | `chm.containsKey("A")`                              | O(1)            |
    | `boolean containsValue(Object value)`           | Returns `true` if this map maps one or more keys to the specified value.          | `chm.containsValue(1)`                              | O(N)            |
    | `void clear()`                                  | Removes all of the mappings from this map.                                        | `chm.clear()`                                       | O(N)            |
    | `Set<K> keySet()`                               | Returns a `Set` view of the keys contained in this map.                           | `Set<String> keys = chm.keySet();`                  | O(1)            |
    | `Collection<V> values()`                        | Returns a `Collection` view of the values contained in this map.                  | `Collection<Integer> values = chm.values();`        | O(1)            |
    | `Set<Map.Entry<K, V>> entrySet()`               | Returns a `Set` view of the mappings contained in this map.                       | `Set<Map.Entry<String, Integer>> entries = chm.entrySet();` | O(1)            |
    | `V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)` | Attempts to compute a mapping for the specified key and its current mapped value (or `null` if none is currently mapped). | `chm.compute("D", (k, v) -> (v == null) ? 1 : v + 1);` | O(1) amortized |
    | `V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction)` | If the specified key is not already associated with a value, attempts to compute its value using the given mapping function and enters it into this map unless `null`. | `chm.computeIfAbsent("E", k -> 5);`                 | O(1) amortized |
    | `V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction)` | If the specified key is already associated with a value, attempts to compute a new mapping given the key and its current mapped value. | `chm.computeIfPresent("D", (k, v) -> v + 1);`       | O(1) amortized |
    | `V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction)` | If the specified key is not already associated with a value or is associated with `null`, associates it with the given non-null value. Otherwise, replaces the associated value with the results of the given remapping function. | `chm.merge("F", 1, Integer::sum);`                  | O(1) amortized |

*   **Interview Notes**:
    *   The preferred concurrent map implementation in Java.
    *   Achieves high concurrency by internally segmenting the map (pre-Java 8) or using node-level locking (Java 8+).
    *   Does not allow `null` keys or values to avoid ambiguity in concurrent scenarios (e.g., `get(key)` returning `null` could mean no mapping or a `null` value).
    *   Iterators are "weakly consistent" – they reflect the state of the map at the time of iteration or changes made since. They are not guaranteed to reflect all modifications made after the iterator's creation but will not throw `ConcurrentModificationException`.
### 4.5 Queue / Deque

#### 4.5.1 PriorityQueue

*   **Declaration / Instantiation**: Unbounded priority queue based on a priority heap. Elements are ordered according to their natural ordering or by a `Comparator`.
    ```java
    Queue<Integer> pq = new PriorityQueue<>(); // Natural ordering (min-heap by default)
    Queue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder()); // Max-heap
    ```
*   **Ordering**: Elements are ordered by priority (smallest element first for natural order). Not necessarily fully sorted.
*   **Null Handling**: Does **not** allow `null` elements.
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**:
    *   `offer`, `poll`, `remove`, `add`: O(log N)
    *   `peek`, `size`: O(1)
*   **Most Commonly Used Methods**:

    | Signature                   | Description                                         | Example Usage         | Time Complexity |
    | :-------------------------- | :-------------------------------------------------- | :-------------------- | :-------------- |
    | `boolean add(E e)`          | Inserts specified element (throws exception if full). | `pq.add(5)`           | O(log N)        |
    | `boolean offer(E e)`        | Inserts specified element (returns false if full).  | `pq.offer(10)`        | O(log N)        |
    | `E remove()`                | Retrieves and removes head (throws exception if empty). | `pq.remove()`         | O(log N)        |
    | `E poll()`                  | Retrieves and removes head (returns `null` if empty). | `pq.poll()`           | O(log N)        |
    | `E element()`               | Retrieves head (throws exception if empty).         | `pq.element()`        | O(1)            |
    | `E peek()`                  | Retrieves head (returns `null` if empty).           | `pq.peek()`           | O(1)            |
    | `int size()`                | Returns number of elements.                         | `pq.size()`           | O(1)            |

*   **Interview Notes**:
    *   Used for problems requiring efficient retrieval of the minimum/maximum element (e.g., Dijkstra's, Huffman coding, top K elements).
    *   The head of the queue is the smallest element according to natural ordering or `Comparator`.

#### 4.5.2 ArrayDeque

*   **Declaration / Instantiation**: Resizable-array implementation of the `Deque` interface. Faster than `LinkedList` when used as a stack or queue.
    ```java
    Deque<String> ad = new ArrayDeque<>();
    ArrayDeque<String> concreteAd = new ArrayDeque<>(10); // Initial capacity
    ```
*   **Important Attributes**: Implements `Deque` (double-ended queue). Can be used as a stack (LIFO) or a queue (FIFO).
*   **Null Handling**: Does **not** allow `null` elements.
*   **Thread-safety**: Not thread-safe.
*   **Time Complexity**: O(1) for most operations (`addFirst`, `addLast`, `removeFirst`, `removeLast`, `peek`). O(N) for `remove(Object)`.
*   **Most Commonly Used Methods**:

    | Signature                   | Description                                         | Example Usage         | Time Complexity |
    | :-------------------------- | :-------------------------------------------------- | :-------------------- | :-------------- |
    | `void addFirst(E e)`        | Adds element to front.                              | `ad.addFirst("A")`    | O(1)            |
    | `void addLast(E e)`         | Adds element to end.                                | `ad.addLast("B")`     | O(1)            |
    | `E removeFirst()`           | Removes and returns first element.                  | `ad.removeFirst()`    | O(1)            |
    | `E removeLast()`            | Removes and returns last element.                   | `ad.removeLast()`     | O(1)            |
    | `E peekFirst()`             | Retrieves first element without removing.           | `ad.peekFirst()`      | O(1)            |
    | `E peekLast()`              | Retrieves last element without removing.            | `ad.peekLast()`       | O(1)            |
    | `boolean offerFirst(E e)`   | Adds to front (returns false if full, rare for AD). | `ad.offerFirst("C")`  | O(1)            |
    | `boolean offerLast(E e)`    | Adds to end.                                        | `ad.offerLast("D")`   | O(1)            |

*   **Interview Notes**:
    *   **Prefer over `LinkedList` for stack/queue**: Generally more efficient for these operations due to array-backing.
    *   Ideal for algorithms involving BFS (Breadth-First Search) or problems requiring efficient additions/removals from both ends.

#### 4.5.3 ConcurrentLinkedQueue (Concurrent)

*   **Declaration / Instantiation**: An unbounded, thread-safe, non-blocking FIFO (first-in, first-out) queue based on linked nodes.
    ```java
    import java.util.concurrent.ConcurrentLinkedQueue;
    ConcurrentLinkedQueue<String> clq = new ConcurrentLinkedQueue<>();
    ```
*   **Important Attributes**:
    *   **Thread-safe**: Achieved using CAS (Compare-And-Swap) operations instead of locks.
    *   **Non-blocking**: Operations don't block threads; instead, they might retry.
    *   **Fail-safe iterators**.
*   **Null Handling**: Does **not** allow `null` elements.
*   **Time Complexity**: O(1) for `offer`, `poll`, `peek`, `size` (amortized).
*   **Most Commonly Used Methods**: Similar to `Queue` interface.

    | Signature                   | Description                                         | Example Usage         | Time Complexity |
    | :-------------------------- | :-------------------------------------------------- | :-------------------- | :-------------- |
    | `boolean offer(E e)`        | Inserts element to end.                             | `clq.offer("job1")`   | O(1)            |
    | `E poll()`                  | Retrieves and removes head (returns `null` if empty). | `clq.poll()`          | O(1)            |
    | `E peek()`                  | Retrieves head (returns `null` if empty).           | `clq.peek()`          | O(1)            |

*   **Interview Notes**:
    *   **When to use**: High-throughput producer-consumer scenarios where threads need to add/remove elements without blocking each other.
    *   More scalable than `synchronizedList`/`Vector` for queue operations.

#### 4.5.4 BlockingQueue variants (Concurrent)

*   **Interface**: `java.util.concurrent.BlockingQueue`. Queues that additionally support operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element.
*   **Common Implementations**:
    *   `ArrayBlockingQueue`: Bounded, array-backed. FIFO.
    *   `LinkedBlockingQueue`: Optionally bounded, linked-list backed. FIFO.
    *   `PriorityBlockingQueue`: Unbounded, priority heap-backed. Priority-based.
    *   `DelayQueue`: Unbounded, elements can only be taken after a given delay.
    *   `SynchronousQueue`: A queue that can hold a single element. Each insert must wait for a remove, and vice versa.
*   **Null Handling**: Most `BlockingQueue` implementations do **not** allow `null` elements.
*   **Time Complexity**: Generally O(1) for `put`/`take` in `ArrayBlockingQueue`/`LinkedBlockingQueue`. O(log N) for `PriorityBlockingQueue`.
*   **Most Commonly Used Methods**:

    | Signature                   | Description                                         | Example Usage                               |
    | :-------------------------- | :-------------------------------------------------- | :------------------------------------------ |
    | `void put(E e)`             | Inserts element, waits if queue is full.            | `queue.put("task")`                         |
    | `E take()`                  | Retrieves and removes head, waits if queue is empty. | `String task = queue.take()`                |
    | `boolean offer(E e, long timeout, TimeUnit unit)` | Inserts, waits up to timeout if full.               | `queue.offer("task", 1, TimeUnit.SECONDS)`  |
    | `E poll(long timeout, TimeUnit unit)` | Retrieves, waits up to timeout if empty.            | `String task = queue.poll(1, TimeUnit.SECONDS)` |
    | `boolean add(E e)`          | Inserts, throws `IllegalStateException` if full.    | `queue.add("task")`                         |
    | `E remove()`                | Retrieves and removes, throws `NoSuchElementException` if empty. | `queue.remove()`                            |

*   **Interview Notes**:
    *   **When to use**: Classic producer-consumer patterns, thread pools, task scheduling.
    *   The "blocking" nature simplifies concurrency control by handling the waiting logic.
    *   `ArrayBlockingQueue` is useful when you need to control the maximum number of items in the queue to prevent resource exhaustion.
    *   `LinkedBlockingQueue` is often preferred for its higher throughput.

## 5. Concurrent Collections & Thread-Safety

### Why They Exist

Standard `java.util` collections (`ArrayList`, `HashMap`, `HashSet`, `LinkedList`, `TreeMap`, `TreeSet`, `PriorityQueue`, `ArrayDeque`) are **not thread-safe**. If multiple threads access and modify these collections concurrently without external synchronization, it can lead to:
*   **Data corruption**: Inconsistent state, lost updates.
*   **Unexpected behavior**: `ConcurrentModificationException` (fail-fast iterators).
*   **Infinite loops or crashes**.

Concurrent collections (from `java.util.concurrent` package) are designed to provide thread-safe operations with better performance than simply wrapping standard collections with `Collections.synchronized...` methods.

### Internal Behavior (Locks / CAS at high level)

*   **`Collections.synchronizedList`/`Map`/`Set`**:
    *   These are wrapper methods that return a synchronized (thread-safe) view of a non-thread-safe collection.
    *   They achieve thread-safety by synchronizing *every method call* using a single intrinsic lock (monitor).
    *   **Drawback**: This global lock becomes a performance bottleneck under high contention (many threads trying to access/modify simultaneously), as only one thread can access the collection at any given time.
*   **Concurrent Collections (`ConcurrentHashMap`, `ConcurrentLinkedQueue`, `CopyOnWriteArrayList`/`Set`)**:
    *   Employ more sophisticated concurrency control mechanisms to allow multiple threads to access parts of the collection concurrently.
    *   **`ConcurrentHashMap`**: Uses fine-grained locking or optimistic concurrency control (CAS operations). Instead of a single lock, it might divide the map into segments, each with its own lock, or use lock-free algorithms for reads and only lock specific bins for writes. This allows concurrent reads and concurrent writes to different parts of the map.
    *   **`ConcurrentLinkedQueue`**: Implements lock-free algorithms using CAS operations (`compareAndSet`) on atomic references. Threads attempt to update references; if the update fails due to another thread's concurrent modification, they retry. This avoids explicit locks altogether, providing very high throughput.
    *   **`CopyOnWriteArrayList`/`Set`**: Achieve thread-safety by making a *new copy* of the underlying array for every write operation. Read operations always work on the immutable (and thus safe) old array.
        *   **Pros**: Reads are very fast and don't require locking. Iterators are fail-safe and never throw `ConcurrentModificationException`.
        *   **Cons**: Writes are expensive (O(N) for copying). High memory consumption for frequent writes. Iterators reflect the state *at the time of creation*, not necessarily the most up-to-date state.

### When to Use Which Collection

| Collection Type     | When to Use                                                 | Preferred Over                                        |
| :------------------ | :---------------------------------------------------------- | :---------------------------------------------------- |
| **`ArrayList`**     | Default `List`. Frequent random access. Single-threaded.    | `LinkedList` for random access.                       |
| **`LinkedList`**    | Frequent insertions/deletions at ends/middle. Stack/Queue. Single-threaded. | `ArrayList` for middle insertions/deletions.          |
| **`HashSet`**       | Unique elements, fast lookups (O(1)). No order needed. Single-threaded. | `ArrayList` for unique checks.                        |
| **`LinkedHashSet`** | Unique elements, fast lookups (O(1)), *insertion order*. Single-threaded. | `HashSet` if order matters. `TreeSet` if natural order not suitable. |
| **`TreeSet`**       | Unique elements, *sorted order*. Range operations. Single-threaded. | `HashSet`/`LinkedHashSet` if sorting is required.     |
| **`HashMap`**       | Default `Map`. Key-value pairs, fast lookups (O(1)). No order. Single-threaded. | `Hashtable`. `TreeMap` if order not needed.           |
| **`LinkedHashMap`** | Key-value pairs, fast lookups (O(1)), *insertion/access order* (LRU). Single-threaded. | `HashMap` if order matters.                           |
| **`TreeMap`**       | Key-value pairs, *sorted order of keys*. Range operations. Single-threaded. | `HashMap`/`LinkedHashMap` if sorting is required.     |
| **`ArrayDeque`**    | Stack or Queue functionality. Faster than `LinkedList` for these. Single-threaded. | `LinkedList` as Stack/Queue.                          |
| **`PriorityQueue`** | Min-heap / Max-heap. Efficient retrieval of min/max element. Single-threaded. | `TreeSet` if only min/max needed (PQ is faster).      |
| **`Vector` / `Hashtable`** | **Avoid in new code.** Legacy, synchronized versions of `ArrayList` / `HashMap` with poor performance. | `ArrayList` / `HashMap` + `Collections.synchronized...` or concurrent collections. |
| **`ConcurrentHashMap`** | High-concurrency `Map` with many readers/writers. High throughput. No `null` keys/values. | `Collections.synchronizedMap(new HashMap<>())`, `Hashtable`. |
| **`ConcurrentLinkedQueue`** | High-concurrency FIFO queue (producer-consumer). Unbounded, non-blocking. No `null`. | `LinkedBlockingQueue` if blocking is not desired or unbounded. |
| **`CopyOnWriteArrayList`** | `List` with many readers, very few writers. Iterators must be fail-safe. | `Collections.synchronizedList(new ArrayList<>())` for many reads. |
| **`CopyOnWriteArraySet`** | `Set` with many readers, very few writers. Iterators must be fail-safe. | `Collections.synchronizedSet(new HashSet<>())` for many reads. |
| **`BlockingQueue`** | Producer-consumer pattern. Threads block on `put`/`take` if queue is full/empty. | `ConcurrentLinkedQueue` if blocking behavior is desired. |

### Performance Trade-offs

*   **`synchronizedList`/`Map`/`Set` (e.g., `Collections.synchronizedList(new ArrayList<>())`)**:
    *   **Pros**: Simple to use, provides thread-safety.
    *   **Cons**: Poor scalability due to single global lock (`Object` monitor). High contention leads to degraded performance. Iterators are fail-fast.
*   **`ConcurrentHashMap`**:
    *   **Pros**: Excellent scalability and high throughput for both reads and writes under contention due to fine-grained locking/CAS.
    *   **Cons**: Does not allow `null` keys/values. Iterators are fail-safe (may not show most up-to-date state).
*   **`ConcurrentLinkedQueue`**:
    *   **Pros**: Very high throughput, completely lock-free (uses CAS). Good for producer-consumer.
    *   **Cons**: Unbounded (can lead to `OutOfMemoryError` if producers are much faster than consumers). Does not allow `null`.
*   **`CopyOnWriteArrayList`/`Set`**:
    *   **Pros**: Reads are extremely fast and non-blocking. Iterators are fail-safe and guaranteed not to throw `ConcurrentModificationException`.
    *   **Cons**: Writes are very expensive (O(N) copy operation). High memory usage. Iterators provide a snapshot, so they may show stale data. Only suitable for collections with *very few* writes relative to reads.
*   **`BlockingQueue` (e.g., `ArrayBlockingQueue`, `LinkedBlockingQueue`)**:
    *   **Pros**: Provides built-in flow control for producer-consumer scenarios. Threads automatically wait when necessary.
    *   **Cons**: `put`/`take` operations can block, potentially increasing latency for individual operations.

### Interview Notes (General Collections)

*   **Choosing the Right Collection**: This is a frequent interview question. Understand the trade-offs:
    *   **Order?** (`ArrayList`, `LinkedList`, `LinkedHashSet`, `TreeMap`, `LinkedHashMap`)
    *   **Sorted?** (`TreeSet`, `TreeMap`, `PriorityQueue`)
    *   **Unique elements?** (`Set`, `Map` keys)
    *   **Thread-safe?** (`Concurrent*`, `Blocking*`, `Vector`, `Hashtable`)
    *   **Performance characteristics (Big-O)** for common operations (`add`, `get`, `remove`, `contains`).
    *   **Null handling?**
*   **`equals()` and `hashCode()`**: Critical for correct behavior of hash-based collections (`HashSet`, `HashMap`, `LinkedHashSet`, `LinkedHashMap`, `ConcurrentHashMap`).
    *   **Contract**: If two objects are equal according to the `equals(Object)` method, then calling the `hashCode()` method on each of the two objects must produce the same integer result.
    *   If `equals()` is overridden, `hashCode()` **must** also be overridden.
    *   If `hashCode()` is consistent, `HashMap` and `HashSet` provide O(1) average performance. If `hashCode()` is poor, performance can degrade to O(N).
*   **`Comparable` vs `Comparator`**:
    *   **`Comparable`**: Defines the *natural ordering* for a class (`compareTo` method).
    *   **`Comparator`**: Defines an *alternative ordering* (external comparison). Allows sorting objects that don't implement `Comparable` or providing different sort orders.
    *   `TreeSet` and `TreeMap` can use either. `Collections.sort()` and `Arrays.sort()` can use `Comparator`.
*   **Fail-fast vs Fail-safe Iterators**: Understand the implications in concurrent environments.
*   **`Iterator` vs `forEach` vs indexed `for` loop**:
    *   `Iterator`: Required for safe removal during iteration.
    *   `forEach`: Clean, concise for read-only iteration.
    *   Indexed `for`: For `List`s when index is needed or for iterating backwards.
*   **Generics**: Use generics (`List<String>`) to ensure type safety and avoid `ClassCastException` at runtime.
*   **Immutable Collections (Java 9+)**: `List.of()`, `Set.of()`, `Map.of()`, `Map.ofEntries()` for creating truly immutable collections. These are compact, memory-efficient, and implicitly thread-safe.



```java
    //Example usage
    List.of(1, 2, 3);
    Set.of("udit", "rahul");
    Map.of("udit", 1, "rahul", 7);
```

