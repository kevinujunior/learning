## Generics in Java

### Introduction to Generics

Generics were introduced in Java 5 to provide type safety at compile-time and eliminate the need for explicit casting. Before generics, collections stored `Object` types, leading to potential `ClassCastException` at runtime. Generics allow you to define classes, interfaces, and methods with type parameters, enabling them to operate on objects of various types while providing compile-time type checking.

**Why Generics Exist:**

*   **Type Safety:** Prevent `ClassCastException` by enforcing type checks at compile-time.
*   **Code Reusability:** Write algorithms that work on different types without code duplication.
*   **Readability:** Code becomes cleaner and easier to understand without numerous type casts.

**Internal Working (Type Erasure):**

Generics in Java are implemented using **type erasure**. This means that generic type information is only present at compile time and is removed during compilation. The JVM at runtime has no knowledge of generic types.

*   **How it works:**
    *   During compilation, all type parameters are replaced with their bounds (e.g., `Object` if no explicit bound).
    *   Casts are inserted by the compiler to ensure type safety when retrieving elements from generic collections.
    *   Bridge methods are generated to handle covariance with overridden methods that have different generic signatures due to erasure.

**Example of Type Erasure:**

```java
List<String> stringList = new ArrayList<>();
List<Integer> integerList = new ArrayList<>();

// After type erasure, both become:
// List stringList = new ArrayList();
// List integerList = new ArrayList();
```

**Common Pitfalls related to Type Erasure:**

*   **Cannot create instances of type parameters:** `new T()` is not allowed because `T` is erased to `Object` at runtime, and the compiler doesn't know what type to instantiate.
*   **Cannot use `instanceof` with type parameters:** `obj instanceof T` is not allowed because `T` is erased.
*   **Cannot create arrays of type parameters:** `new T[size]` is not allowed due to erasure. You can create `new E[size]` and then cast, but it leads to unchecked warnings.
*   **Overloading methods based on generic types is not possible:** `void myMethod(List<String> list)` and `void myMethod(List<Integer> list)` will result in a compile-time error because after erasure, their signatures become identical: `void myMethod(List list)`.

### Generic Classes and Interfaces

**Syntax:**

```java
public class Box<T> {
    private T content;

    public Box(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}

// Usage:
Box<String> stringBox = new Box<>("Hello Generics");
String content = stringBox.getContent(); // No cast needed

Box<Integer> integerBox = new Box<>(123);
Integer intContent = integerBox.getContent();
```

**Multiple Type Parameters:**

```java
public interface Pair<K, V> {
    K getKey();
    V getValue();
}

public class OrderedPair<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }
}
```

### Generic Methods and Constructors

Generic methods introduce their own type parameters, independent of the class's type parameters (if any).

**Syntax for Generic Methods:**

The type parameter declaration appears *before* the return type.

```java
public class Util {
    public static <T> boolean compare(T t1, T t2) {
        return t1.equals(t2);
    }

    // Usage:
    boolean isEqual = Util.<String>compare("hello", "world"); // Explicit type argument
    boolean isEqual2 = Util.compare(10, 10); // Type inference
}
```

**Generic Constructors:**

Similar to generic methods, constructors can also be generic.

```java
public class GenericConstructorExample {
    <T> GenericConstructorExample(T value) {
        System.out.println("Generic constructor called with value: " + value);
    }

    public static void main(String[] args) {
        new <String>GenericConstructorExample("Test"); // Explicit type argument
        new GenericConstructorExample(123); // Type inference
    }
}
```

### Bounded Type Parameters

Bounds restrict the types that can be used as type arguments. They are crucial for enabling specific method calls on generic types.

**Why Bounds Exist:**

To allow methods specific to the bounded type to be called on the type parameter `T`. Without bounds, `T` can only assume methods available on `Object`.

**Syntax:** `<?>` (unbounded wildcard), `<? extends Type>` (upper bound), `<? super Type>` (lower bound).

**Upper Bounded Wildcards (`<? extends Type>`):**

*   **`<? extends T>`:** Represents an unknown type that is `T` or a subtype of `T`.
*   **Usage:** Read-only access. You can get `T` or a supertype of `T` from a collection, but you cannot add anything to it (except `null`).
*   **Why?** If `List<? extends Number>` could take a `Float`, and you tried to add an `Integer`, it would be a compile-time error because `Float` is not guaranteed to be an `Integer`. The compiler cannot guarantee type safety for additions.
*   **Example:**

    ```java
    public static void printNumbers(List<? extends Number> list) {
        for (Number n : list) { // Can read Number or its subtypes
            System.out.println(n);
        }
        // list.add(new Integer(5)); // Compile-time error!
        // list.add(new Float(5.0f)); // Compile-time error!
        list.add(null); // This is allowed
    }

    List<Integer> integers = Arrays.asList(1, 2, 3);
    printNumbers(integers); // Works

    List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);
    printNumbers(doubles); // Works
    ```
**Cheat Note** 
* `public class Box<T extends Number>` T extends Number means that T must be Number itself or any class that extends Number. Because T is guaranteed to be a Number, you can safely call value.doubleValue() inside the Box class

**Lower Bounded Wildcards (`<? super Type>`):**

*   **`<? super T>`:** Represents an unknown type that is `T` or a supertype of `T`.
*   **Usage:** Write-only access (producer). You can add `T` or its subtypes to the collection.
*   **Why?** If `List<? super Integer>` means it could be `List<Integer>`, `List<Number>`, or `List<Object>`. If you add an `Integer`, it's valid for all these types. When you read, you can only assume it's an `Object` because that's the only guaranteed common supertype.
*   **Example:**

    ```java
    public static void addIntegers(List<? super Integer> list) {
        list.add(1); // OK
        list.add(new Integer(2)); // OK
        // Integer num = list.get(0); // Compile-time error! Can only get Object
        Object obj = list.get(0); // OK
    }

    List<Integer> intList = new ArrayList<>();
    addIntegers(intList); // Works (list becomes List<Integer>)

    List<Number> numList = new ArrayList<>();
    addIntegers(numList); // Works (list becomes List<Number>)

    List<Object> objList = new ArrayList<>();
    addIntegers(objList); // Works (list becomes List<Object>)
    ```
**Cheat Note**
*  `List<? super Integer>` means the list can contain `Integer` objects or any type that is a superclass of `Integer`. This is primarily useful for producers/consumers; `? super T` is used when you are adding elements to the list (it's a "consumer").

**Unbounded Wildcards (`<?>`):**

*   **`<?>`:** Represents an unknown type. It is equivalent to `<? extends Object>`.
*   **Usage:** When the type parameter itself doesn't matter (e.g., printing contents of any list) or when you want to call methods on `Object`. You cannot add anything (except `null`) to a list of unbounded wildcards, and you can only retrieve `Object` instances.
*   **Example:**

    ```java
    public static void printList(List<?> list) {
        for (Object o : list) { // Can read as Object
            System.out.println(o);
        }
        // list.add("item"); // Compile-time error!
        list.add(null); // Allowed
    }
    ```

### PECS (Producer-Extends, Consumer-Super) Rule

This is a fundamental mnemonic for when to use `extends` and `super` wildcards.

*   **Producer-Extends:** If your generic type produces `T` values (e.g., you want to iterate over elements and read them), use `<? extends T>`. This is an upper bound, meaning the list produces `T` or subtypes of `T`. You can read `T` (or `Object`) but not add to it.
    *   **Analogy:** A factory `<? extends Fruit>` produces `Fruit` (Apples, Oranges). You can take a `Fruit` out, but you can't tell the factory to `add(new Banana())` because it might be an `AppleFactory`.
*   **Consumer-Super:** If your generic type consumes `T` values (e.g., you want to add elements to it), use `<? super T>`. This is a lower bound, meaning the list consumes `T` or supertypes of `T`. You can add `T` (or its subtypes) but can only read `Object`.
    *   **Analogy:** A basket `<? super Apple>` can hold `Apple`s. It could be an `AppleBasket`, a `FruitBasket`, or an `ObjectBasket`. You can `add(new Apple())` to any of these, but when you `get()` something, you only know for sure it's an `Object`.

**When to use:**

*   **Methods that only read data:** `List<? extends E>`
*   **Methods that only add data:** `List<? super E>`
*   **Methods that both read and add data:** `List<E>` (no wildcards)

**Interview Trap:** Misunderstanding the purpose of `extends` and `super` and their implications on read/write operations.

### Variance (Covariance, Contravariance, Invariance)

These terms describe how subtyping relationships between complex types (like generic types) relate to the subtyping relationships of their component types.

*   **Invariance (Java Generics by default):**
    *   `List<String>` is **not** a subtype of `List<Object>`.
    *   This is the default behavior in Java and ensures compile-time type safety. If `List<String>` were a `List<Object>`, you could add an `Integer` to it, leading to a `ClassCastException` when retrieving a `String`.
*   **Covariance (Arrays in Java, `extends` wildcard):**
    *   `String[]` **is** a subtype of `Object[]`.
    *   Allows `Object[] objArr = new String[10];`. This is why arrays are not type-safe at compile-time for additions and can lead to `ArrayStoreException` at runtime.
    *   With generics, `List<? extends T>` allows for a form of limited covariance: `List<? extends Number>` can hold `List<Integer>`, `List<Double>`, etc. You can read, but not add.
*   **Contravariance (`super` wildcard):**
    *   If `S` is a subtype of `T`, then `Consumer<T>` is a subtype of `Consumer<S>`. (A consumer of `T` can consume `S` because `S` can be treated as a `T`).
    *   In Java generics, this is achieved with `<? super T>`. For example, `List<? super Integer>` can hold `List<Number>` because a `List<Number>` can consume `Integer`s. You can add, but not read safely (only `Object`).

**Practical Importance:** Understanding variance is crucial for correctly using wildcards and designing flexible yet type-safe APIs.

### Multiple Bounds

You can specify multiple bounds for a type parameter.

**Syntax:** `&` operator is used. The first type can be a class or interface, subsequent types *must* be interfaces.

```java
// T must be a subtype of Number and implement Comparable
public static <T extends Number & Comparable<T>> T findMax(List<T> list) {
    if (list == null || list.isEmpty()) {
        return null;
    }
    T max = list.get(0);
    for (T item : list) {
        if (item.compareTo(max) > 0) {
            max = item;
        }
    }
    return max;
}
```

**Why it exists:** To combine behaviors from multiple parent types. In the example above, `Number` provides numeric operations, and `Comparable` provides ordering.

**Interview Trap:** Remember that only one class can be specified in bounds, and it must come first.

### Raw Types

A raw type is the name of a generic class or interface without any type arguments.

**Example:** `List` instead of `List<String>`.

**When to use (or avoid):**

*   **Avoid:** Using raw types defeats the purpose of generics, leading to compile-time warnings (`unchecked call`) and potential `ClassCastException` at runtime.
*   **Legacy Code:** Raw types exist for backward compatibility with pre-Java 5 code.
*   **`Class` literals:** `String.class`, `Integer.class` are technically raw types.

### Important Classes and Interfaces (Generics Context)

*   **`java.util.List<E>`, `java.util.Set<E>`, `java.util.Map<K,V>`:** Core generic collection interfaces.
*   **`java.util.ArrayList<E>`, `java.util.HashSet<E>`, `java.util.HashMap<K,V>`:** Common generic collection implementations.
*   **`java.lang.Comparable<T>`:** Interface for natural ordering. `public int compareTo(T o);`
*   **`java.util.Comparator<T>`:** Interface for custom ordering. `public int compare(T o1, T o2);`
*   **`java.util.Optional<T>`:** Container object which may or may not contain a non-null value. Introduced to avoid `NullPointerException`.
*   **`java.util.function` package:** Contains functional interfaces, heavily generic.

## Functional Programming in Java

### Introduction to Functional Programming

Functional Programming (FP) is a programming paradigm that treats computation as the evaluation of mathematical functions and avoids changing state and mutable data. It emphasizes immutability, pure functions, and higher-order functions. Java introduced FP features with Java 8, primarily through lambda expressions and the Stream API.

**Key Concepts:**

*   **Pure Functions:**
    *   Given the same inputs, always return the same output.
    *   Have no side effects (do not modify external state or perform I/O).
*   **Immutability:** Data structures are not modified after creation. New data structures are created for changes.
*   **First-Class Functions:** Functions can be passed as arguments, returned from other functions, and assigned to variables. (Achieved via lambda expressions and method references in Java).
*   **Higher-Order Functions:** Functions that take other functions as arguments or return functions as results. (e.g., `map`, `filter`, `reduce` in Stream API).
*   **Referential Transparency:** An expression can be replaced with its value without changing the program's behavior. (A property of pure functions).
*   **Lazy Evaluation:** Expressions are evaluated only when their values are needed. (Stream API intermediate operations are lazy).

**Why Functional Programming in Java:**

*   **Concurrency:** Easier to parallelize operations on immutable data without worrying about race conditions.
*   **Readability & Maintainability:** Pure functions are easier to reason about, test, and debug.
*   **Expressiveness:** Concise code for common data processing tasks.

### Functional Interfaces

A functional interface is an interface that has exactly one abstract method. They are also known as Single Abstract Method (SAM) interfaces. They can have any number of default or static methods.

**Annotation:** `@FunctionalInterface` is optional but recommended. It tells the compiler to enforce the SAM rule and helps others understand the intent.

**Example:**

```java
@FunctionalInterface
interface MyFunctionalInterface {
    void doSomething(); // Single abstract method

    default void doDefault() {
        System.out.println("Default method");
    }

    static void doStatic() {
        System.out.println("Static method");
    }
}
```

**Important Standard Functional Interfaces (`java.util.function`):**

*   **`Predicate<T>`:** Represents a predicate (boolean-valued function) of one argument.
    *   Abstract method: `boolean test(T t)`
    *   Default methods: `and`, `or`, `negate`
    *   Static method: `isEqual`
    *   **Use case:** Filtering collections.
    *   `Predicate<String> startsWithA = s -> s.startsWith("A");`
*   **`Function<T, R>`:** Represents a function that accepts one argument and produces a result.
    *   Abstract method: `R apply(T t)`
    *   Default methods: `andThen`, `compose`
    *   **Use case:** Transforming one type to another.
    *   `Function<String, Integer> stringToInt = Integer::parseInt;`
*   **`Consumer<T>`:** Represents an operation that accepts a single input argument and returns no result.
    *   Abstract method: `void accept(T t)`
    *   Default method: `andThen`
    *   **Use case:** Performing an action on each element of a collection.
    *   `Consumer<String> printer = System.out::println;`
*   **`Supplier<T>`:** Represents a supplier of results.
    *   Abstract method: `T get()`
    *   **Use case:** Lazy initialization, factory methods.
    *   `Supplier<Double> randomValue = Math::random;`
*   **`UnaryOperator<T>`:** Represents an operation on a single operand that produces a result of the same type as its operand. (Extends `Function<T, T>`).
    *   Abstract method: `T apply(T t)`
    *   **Use case:** Performing a transformation that keeps the type.
    *   `UnaryOperator<String> toUpperCase = String::toUpperCase;`
*   **`BinaryOperator<T>`:** Represents an operation upon two operands of the same type, producing a result of the same type as the operands. (Extends `BiFunction<T, T, T>`).
    *   Abstract method: `T apply(T t1, T t2)`
    *   Static methods: `minBy`, `maxBy` (with a `Comparator`)
    *   **Use case:** Reducing two values to a single value of the same type.
    *   `BinaryOperator<Integer> sum = (a, b) -> a + b;`
*   **`BiFunction<T, U, R>`:** Represents a function that accepts two arguments and produces a result.
    *   Abstract method: `R apply(T t, U u)`
    *   **Use case:** Combining two inputs into one output.
    *   `BiFunction<Integer, Integer, Integer> multiplier = (a, b) -> a * b;`
*   **`BiConsumer<T, U>`:** Represents an operation that accepts two input arguments and returns no result.
    *   Abstract method: `void accept(T t, U u)`
    *   **Use case:** Iterating over `Map.Entry` for example.
    *   `BiConsumer<String, Integer> printEntry = (k, v) -> System.out.println(k + ": " + v);`

**Primitive Specializations:** For performance, there are primitive versions to avoid autoboxing/unboxing overhead (e.g., `IntPredicate`, `LongFunction`, `DoubleConsumer`, `IntSupplier`, `IntUnaryOperator`, `LongBinaryOperator`).

### Lambda Expressions

A concise way to represent an instance of a functional interface. They provide an anonymous function implementation.

**Syntax:** `(parameters) -> expression` or `(parameters) -> { statements; }`

**Examples:**

*   No parameters: `() -> System.out.println("Hello")`
*   One parameter (type optional): `s -> s.length()` or `(String s) -> s.length()`
*   Multiple parameters: `(a, b) -> a + b`
*   Block body: `(x, y) -> { System.out.println("Adding"); return x + y; }`

**Rules:**

*   The compiler infers parameter types from the functional interface.
*   Lambda expressions can access effectively final (or final) local variables from their enclosing scope (closure).
*   Cannot introduce new checked exceptions unless allowed by the functional interface's abstract method.

### Method References

A shorthand syntax for lambda expressions that simply call an existing method. They make code more readable when the lambda body just forwards to a method.

**Syntax:** `ClassName::methodName` or `objectName::instanceMethodName`

**Types of Method References:**

1.  **Static method reference:** `ClassName::staticMethodName`
    *   `Function<String, Integer> parser = Integer::parseInt;` (equivalent to `s -> Integer.parseInt(s)`)
2.  **Instance method reference of a particular object:** `object::instanceMethodName`
    *   `Consumer<String> printer = System.out::println;` (equivalent to `s -> System.out.println(s)`)
3.  **Instance method reference of an arbitrary object of a particular type:** `ClassName::instanceMethodName`
    *   `Function<String, String> toUpperCase = String::toUpperCase;` (equivalent to `s -> s.toUpperCase()`)
4.  **Constructor reference:** `ClassName::new`
    *   `Supplier<ArrayList<String>> listCreator = ArrayList::new;` (equivalent to `() -> new ArrayList<String>()`)
    *   `Function<Integer, int[]> intArrayCreator = int[]::new;` (equivalent to `size -> new int[size]`)

### Stream API

The Java Stream API provides a powerful way to process collections of objects in a functional style. Streams are sequences of elements that support sequential and parallel aggregate operations.

**Key Characteristics:**

*   **Not a data structure:** Streams don't store data; they operate on a source (e.g., `List`, `Set`, array, `IO channels`, `generate`, `iterate`).
*   **Functional in nature:** Operations produce a result without modifying the source.
*   **Lazy evaluation:** Intermediate operations are not executed until a terminal operation is invoked.
*   **Pipelines:** Operations can be chained together to form a pipeline.

**Stream Creation:**

*   `Collection.stream()` / `Collection.parallelStream()`
*   `Arrays.stream(array)`
*   `Stream.of(T... values)`
*   `Stream.iterate(seed, unaryOperator)`
*   `Stream.generate(supplier)`
*   `BufferedReader.lines()`
*   `Files.lines(path)`

**Intermediate Operations:** (Return a `Stream`, are lazy, can be chained)

*   `filter(Predicate<T> predicate)`: Returns a stream consisting of the elements of this stream that match the given predicate.
*   `map(Function<T, R> mapper)`: Returns a stream consisting of the results of applying the given function to the elements of this stream. (One-to-one transformation)
*   `flatMap(Function<T, Stream<R>> mapper)`: Returns a stream consisting of the results of replacing each element of this stream with the contents of a mapped stream produced by applying the provided mapping function to each element. (One-to-many, then flattens)
    *   **Use case:** Flattening a `List<List<String>>` into a `List<String>`.
*   `distinct()`: Returns a stream consisting of the distinct elements.
*   `sorted()`: Returns a stream consisting of the elements sorted according to natural order.
*   `sorted(Comparator<T> comparator)`: Returns a stream consisting of the elements sorted according to the provided `Comparator`.
*   `peek(Consumer<T> action)`: Performs an action on each element of this stream as elements are consumed from the resulting stream. Useful for debugging.
*   `limit(long maxSize)`: Truncates the stream to be no longer than `maxSize`.
*   `skip(long n)`: Skips the first `n` elements.

**Terminal Operations:** (Return a non-Stream result, trigger execution, consume the stream)

*   `forEach(Consumer<T> action)`: Performs an action for each element in the stream.
*   `collect(Collector<T, A, R> collector)`: Performs a mutable reduction operation on the elements of this stream.
    *   Common `Collectors` methods:
        *   `toList()`, `toSet()`, `toMap(keyMapper, valueMapper)`
        *   `joining()`, `counting()`, `summingInt()`, `averagingDouble()`
        *   `groupingBy(classifier)`, `partitioningBy(predicate)`
        *   `reducing(identity, accumulator)`
        *   `minBy(comparator)`, `maxBy(comparator)`
*   `reduce(T identity, BinaryOperator<T> accumulator)`: Performs a reduction on the elements of this stream. `identity` is the initial value.
*   `reduce(BinaryOperator<T> accumulator)`: Performs a reduction, returning an `Optional<T>`.
*   `reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)`: For parallel streams, combines results.
*   `min(Comparator<T> comparator)`, `max(Comparator<T> comparator)`: Returns the minimum/maximum element in an `Optional<T>`.
*   `count()`: Returns the count of elements.
*   `anyMatch(Predicate<T> predicate)`, `allMatch(Predicate<T> predicate)`, `noneMatch(Predicate<T> predicate)`: Returns a boolean indicating if any/all/none elements match the predicate.
*   `findFirst()`, `findAny()`: Returns an `Optional<T>` containing the first/any element. Useful for short-circuiting.

**Example Stream Pipeline:**

```java
List<String> names = Arrays.asList("Alice", "Bob", "Anna", "David", "Andrew");

List<String> filteredNames = names.stream()               // Stream<String>
    .filter(name -> name.startsWith("A"))       // Stream<String>
    .sorted()                                   // Stream<String>
    .map(String::toUpperCase)                   // Stream<String>
    .limit(2)                                   // Stream<String>
    .collect(Collectors.toList());              // List<String> -> ["ALICE", "ANNA"]

// Using reduce
Optional<String> longestName = names.stream()
    .reduce((name1, name2) -> name1.length() >= name2.length() ? name1 : name2);
// longestName.get() -> "Andrew" or "Alice" (depends on order for tie)

// Grouping
Map<Character, List<String>> namesByFirstLetter = names.stream()
    .collect(Collectors.groupingBy(name -> name.charAt(0)));
// {A=[Alice, Anna, Andrew], B=[Bob], D=[David]}
```

**Common Pitfalls with Streams:**

*   **`NullPointerException`:** Streams themselves don't handle `null` elements automatically. `filter(Objects::nonNull)` can be used.
*   **Reusing streams:** A stream can only be consumed once. Attempting to use a stream after a terminal operation will throw an `IllegalStateException`.
*   **Side effects in intermediate operations:** While `peek` can be used for debugging, generally avoid side effects in `map`, `filter`, etc., to maintain functional purity.
*   **Performance overhead for small collections:** For very small collections, traditional loops might be marginally faster due to stream pipeline overhead. However, for larger collections, especially with parallel streams, the benefits become significant.

### `Optional<T>`

`Optional<T>` is a container object used to represent the presence or absence of a value. It was introduced in Java 8 to help developers write more robust code by explicitly handling potential null values, thereby reducing `NullPointerException`s.

**Why `Optional` exists:**

*   **Explicitly handle absence of a value:** Forces the developer to consider what to do when a value might not be present.
*   **Improves code readability:** Makes it clear that a method might return "nothing."
*   **Avoids `NullPointerException`:** By providing methods to safely interact with the contained value.

**Important Methods:**

*   **Creation:**
    *   `Optional.empty()`: Returns an empty `Optional` instance.
    *   `Optional.of(T value)`: Returns an `Optional` with the given non-null value. Throws `NullPointerException` if `value` is `null`.
    *   `Optional.ofNullable(T value)`: Returns an `Optional` describing the given value, or an empty `Optional` if the value is `null`.
*   **Retrieval:**
    *   `T get()`: If a value is present, returns the value, otherwise throws `NoSuchElementException`. **Use sparingly**, as it defeats the purpose of `Optional` if not guarded.
    *   `boolean isPresent()`: Returns `true` if a value is present, `false` otherwise. (Discouraged in favor of functional methods).
    *   `boolean isEmpty()`: Returns `true` if a value is not present, `false` otherwise. (Java 11+)
    *   `void ifPresent(Consumer<? super T> action)`: If a value is present, performs the given action with the value, otherwise does nothing.
    *   `void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction)`: If a value is present, performs the given action with the value, otherwise performs the given empty-based action. (Java 9+)
*   **Default Values / Fallbacks:**
    *   `T orElse(T other)`: Returns the value if present, otherwise returns `other`. `other` is *always* evaluated.
    *   `T orElseGet(Supplier<? extends T> other)`: Returns the value if present, otherwise returns the result of the `Supplier`. The `Supplier` is *only* evaluated if the `Optional` is empty (lazy evaluation). **Prefer this over `orElse` for expensive default value computations.**
    *   `T orElseThrow()`: Returns the value if present, otherwise throws `NoSuchElementException`. (Java 10+)
    *   `T orElseThrow(Supplier<? extends X> exceptionSupplier)`: Returns the value if present, otherwise throws the exception produced by the `Supplier`.
*   **Transformation:**
    *   `Optional<R> map(Function<? super T, ? extends R> mapper)`: If a value is present, applies the mapping function to it and returns an `Optional` describing the result. Otherwise returns an empty `Optional`.
    *   `Optional<R> flatMap(Function<? super T, ? extends Optional<? extends R>> mapper)`: If a value is present, applies the mapping function to it and returns the result. The mapping function itself should return an `Optional`. This flattens the result.
    *   `Optional<T> filter(Predicate<? super T> predicate)`: If a value is present and matches the predicate, returns an `Optional` describing the value. Otherwise returns an empty `Optional`.

**Example:**

```java
String name = null;
Optional<String> optName = Optional.ofNullable(name);

String result1 = optName.orElse("Default Name"); // "Default Name"
String result2 = optName.orElseGet(() -> calculateDefaultName()); // calculateDefaultName() called

optName.ifPresent(n -> System.out.println("Name is: " + n)); // Does nothing

Optional<Integer> nameLength = optName.map(String::length); // Optional.empty

name = "John";
optName = Optional.ofNullable(name);
optName.ifPresent(n -> System.out.println("Name is: " + n)); // Prints "Name is: John"

Optional<Integer> lengthGreaterThan3 = optName
    .map(String::length)
    .filter(len -> len > 3); // Optional[4]

Optional<String> upperCaseName = optName.map(String::toUpperCase); // Optional["JOHN"]
```

**When to use `Optional`:**

*   As a return type for methods that might legitimately return "no result" instead of `null`.
*   To chain operations on a potentially absent value, making the code more readable and safe.

**When NOT to use `Optional`:**

*   As a field in a class (serialization issues, adds overhead). Use `null` instead for fields and handle it appropriately.
*   As a parameter in a method (forces caller to wrap, `null` parameters can be handled with null checks or validation).
*   In collections (e.g., `List<Optional<String>>`). Prefer filtering out `null` elements or using an empty collection if no elements.

**Interview Trap:** Misusing `get()` without `isPresent()` or using `orElse` when `orElseGet` is more appropriate for performance.

### Immutability, Side Effects, and Lazy Evaluation

These are core tenets of functional programming that influence design in Java.

**Immutability:**

*   **Definition:** An object whose state cannot be modified after it is created.
*   **Benefits:**
    *   **Thread Safety:** Immutable objects are inherently thread-safe; no need for synchronization.
    *   **Simplicity:** Easier to reason about and debug, as their state never changes.
    *   **Cacheability:** Can be safely cached and reused.
    *   **Security:** Prevents unexpected modifications.
*   **How to achieve in Java:**
    *   Declare all fields `private` and `final`.
    *   Don't provide setter methods.
    *   If a field is a mutable object, make a defensive copy in the constructor and getter.
    *   Do not allow `this` reference to escape during construction.
    *   Declare the class `final` (optional, but prevents subclassing that could violate immutability).
*   **Examples:** `String`, `Integer`, `LocalDate`, most wrapper classes.
*   **Relevance to FP:** Encourages pure functions which operate on immutable data to produce new immutable data, rather than modifying existing state.

**Side Effects:**

*   **Definition:** An action performed by a function that modifies state outside its local scope or has an observable interaction with the outside world beyond returning a value.
*   **Examples:**
    *   Modifying a global variable.
    *   Modifying an input parameter.
    *   Printing to console (`System.out.println`).
    *   Writing to a file or database.
    *   Throwing an exception (if not part of the function's contract).
*   **Pure Functions:** Functions with no side effects.
*   **Relevance to FP:** Functional programming aims to minimize side effects, ideally isolating them to specific, controlled parts of the application. Pure functions are easier to test, compose, and parallelize. Stream API operations (`map`, `filter`, `reduce`) are designed to be free of side effects. `forEach` and `peek` are notable exceptions, where side effects are expected or tolerated (for `peek`, primarily for debugging).

**Lazy Evaluation:**

*   **Definition:** Delaying the computation of an expression until its value is actually needed.
*   **Benefits:**
    *   **Performance:** Avoids unnecessary computations, especially in short-circuiting operations (e.g., `findFirst`, `anyMatch`).
    *   **Infinite Streams:** Enables working with potentially infinite sequences of data (e.g., `Stream.iterate`, `Stream.generate`).
*   **Relevance to FP in Java:** The Stream API extensively uses lazy evaluation. Intermediate operations (like `map`, `filter`) are not executed until a terminal operation is called. This creates an "internal iterator" model where elements flow through the pipeline one by one, reducing memory footprint and improving efficiency.
*   **Example:**

    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    long count = names.stream()
        .filter(s -> {
            System.out.println("Filtering: " + s); // Only executed when needed
            return s.startsWith("A");
        })
        .map(s -> {
            System.out.println("Mapping: " + s); // Only executed when needed
            return s.toUpperCase();
        })
        .count(); // Terminal operation triggers execution

    // Output:
    // Filtering: Alice
    // Mapping: Alice
    // Filtering: Bob
    // Filtering: Charlie
    ```
    Notice "Bob" and "Charlie" are filtered but not mapped because they don't start with "A". The mapping only happens for elements that pass the filter.

### Performance Considerations and Best Practices

**Generics:**

*   **Type Erasure Overhead:** Virtually none at runtime. The performance cost is primarily compile-time.
*   **Primitive Wrapper Types:** Using `List<Integer>` instead of `List<int>` incurs autoboxing/unboxing overhead. For performance-critical code with primitive types, consider primitive arrays or specialized collections (e.g., `TIntArrayList` from Trove library, or `IntStream`).
*   **Wildcards Performance:** No direct performance impact. They are a compile-time safety mechanism.
*   **Best Practice:** Always use generics for type safety. The runtime overhead is negligible compared to the benefits of compile-time safety and code clarity.

**Functional Programming & Streams:**

*   **Autoboxing/Unboxing:** This can be a significant overhead when processing large streams of primitive types.
    *   **Best Practice:** Use primitive specialized streams (`IntStream`, `LongStream`, `DoubleStream`) and their corresponding functional interfaces (e.g., `IntPredicate`, `LongFunction`, `DoubleConsumer`) whenever possible to avoid boxing/unboxing.
*   **Source Conversion:** Converting large arrays/collections to streams has a minor initial overhead.
*   **Short-Circuiting Operations:** `anyMatch`, `allMatch`, `noneMatch`, `findFirst`, `findAny`, `limit` are highly efficient as they stop processing as soon as the result is determined.
*   **Stateful Lambda Expressions:** Avoid stateful lambdas within stream operations (e.g., lambdas that modify external variables or rely on previous elements in the stream for their logic, unless specifically designed for reduction operations). This can lead to non-deterministic behavior, especially in parallel streams.
    *   **Best Practice:** Strive for pure, stateless lambdas in `map`, `filter`, `forEach`, etc.
*   **Parallel Streams:**
    *   **Benefit:** Can significantly improve performance for CPU-bound tasks on large data sets, leveraging multiple cores.
    *   **Overhead:** Incurs overhead for splitting data, managing threads, and merging results.
    *   **When to use:**
        *   When the computation per element is significant.
        *   When the number of elements is large.
        *   When the stream operations are non-blocking (CPU-bound).
        *   When the stream source can be efficiently split (e.g., `ArrayList`, `Array`).
    *   **When NOT to use:**
        *   Small data sets.
        *   I/O-bound operations (e.g., network calls, file reading/writing), as parallelizing I/O often doesn't help and can introduce contention.
        *   Streams where the order of processing matters for correctness (unless using ordered collectors).
        *   Operations that use shared mutable state without proper synchronization.
    *   **Best Practice:** Start with sequential streams. Only switch to `parallelStream()` if profiling indicates a performance bottleneck and the task is suitable for parallelization. Measure, don't guess.
*   **`collect()` vs `reduce()`:**
    *   `reduce()` is for combining elements into a single result (e.g., sum, max, min).
    *   `collect()` is for mutating a container (e.g., `List`, `Map`) to accumulate results. `collect` is generally more efficient for building collections due to its ability to parallelize mutable accumulation with a `combiner`.
*   **Avoid Excessive `boxed()` calls:** Calling `boxed()` to convert `IntStream` to `Stream<Integer>` adds boxing overhead. Only do it if you genuinely need a `Stream<Integer>` (e.g., to pass to a method that accepts `Stream<Object>`).
*   **Chain Operations:** Prefer chaining multiple intermediate operations rather than performing loops for each step. This allows the Stream API to optimize the pipeline and leverage lazy evaluation.
*   **Method References vs. Lambdas:** Method references are often more concise and sometimes slightly more performant (JVM can optimize them better) but primarily a style choice.

**General Interview Tips:**

*   Be able to explain **why** these features were introduced.
*   Understand the **trade-offs** (e.g., generics for type safety vs. type erasure limitations; functional programming for immutability and concurrency vs. potential overhead for small tasks).
*   Demonstrate correct usage of wildcards (PECS rule).
*   Be proficient in common Stream API operations and `Collectors`.
*   Know the nuances of `Optional` and how to use it safely and effectively.
*   Discuss immutability benefits and how to design immutable classes.
*   Explain lazy evaluation and how it impacts stream execution.
*   Be ready to discuss when parallel streams are appropriate and their potential drawbacks.

This covers the essential, in-depth knowledge required for SDE-2 interviews on Generics and Functional Programming in Java. Good luck!