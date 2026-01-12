# Java Streams API and Lambda Expressions: SDE-2 Interview Guide

This guide provides an exhaustive, in-depth, and interview-oriented explanation of Java Streams API and Lambda Expressions, suitable for SDE-2 and Senior-level interviews at Big Tech companies.

## 1. Lambda Expressions

### What it is
Lambda expressions provide a concise way to represent an anonymous function (a function without a name). They allow you to treat functionality as a method argument, or code as data. Introduced in Java 8, they are a core feature of functional programming in Java.

### Why it exists
Before lambdas, anonymous inner classes were used to pass behavior. This often led to verbose and boilerplate code, especially for simple operations. Lambdas significantly reduce this verbosity, making code more readable and maintainable, and enable the use of the Streams API.

### How it works internally
Lambda expressions are primarily syntactic sugar. The Java compiler translates lambda expressions into private methods within the enclosing class, and then uses a new bytecode instruction, `invokedynamic`, to dynamically link the lambda expression to its target functional interface method at runtime. This process is called "desugaring." The actual runtime implementation often involves the `java.lang.invoke.LambdaMetafactory` class to generate an anonymous class dynamically that implements the target functional interface.

### When to use it and when NOT to use it
**When to use:**
*   Implementing functional interfaces (e.g., `Runnable`, `Callable`, `Comparator`, custom interfaces).
*   Working with the Streams API for concise data processing.
*   Event handling in GUI applications (e.g., `ActionListener`).
*   Wherever an anonymous inner class would be used for a single abstract method interface.

**When NOT to use:**
*   For complex logic that spans multiple lines and requires state management. In such cases, a well-named private method or a dedicated class might be more readable.
*   When the lambda body becomes too large or difficult to understand.
*   When readability suffers due to excessive nesting or over-usage.
*   As a replacement for all anonymous inner classes; if the anonymous inner class implements multiple methods or adds state, a lambda won't work.

### Common pitfalls, performance issues, and interview traps
*   **Variable Capture:** Lambdas can access effectively final (or implicitly final) local variables from their enclosing scope. A variable is effectively final if its value is not changed after it is initialized. Attempting to modify such variables within a lambda will result in a compile-time error. This is because lambdas execute potentially on a different thread or at a later time, and direct access to mutable local variables would lead to thread-safety issues and unpredictable behavior.
*   **`this` reference:** Inside a lambda expression, `this` refers to the enclosing instance of the class where the lambda is defined, unlike anonymous inner classes where `this` refers to the instance of the anonymous class itself.
*   **Performance:** Generally, lambdas have negligible performance overhead compared to anonymous inner classes. The `invokedynamic` mechanism is highly optimized. However, excessive creation of complex lambda instances in tight loops *could* theoretically lead to minor overhead from class loading/instantiation, though this is rarely a practical concern.
*   **Debugging:** Debugging lambda expressions can sometimes be slightly trickier as the stack traces might not always be as clear as with named methods, but modern IDEs have significantly improved this.

### Lambda syntax forms and target typing

#### Syntax Forms:
1.  **Zero parameters:**
    ```java
    () -> System.out.println("No parameters");
    ```
2.  **Single parameter (type optional if inferred):**
    ```java
    x -> x * x; // Implicit type for x
    (int x) -> x * x; // Explicit type for x
    ```
3.  **Multiple parameters (parentheses mandatory, types optional if inferred):**
    ```java
    (x, y) -> x + y;
    (int x, int y) -> x + y;
    ```
4.  **Block body (curly braces mandatory, return statement for non-void lambdas):**
    ```java
    (x, y) -> {
        int sum = x + y;
        return sum;
    };
    ```
5.  **Method References:** A shorthand for lambda expressions that simply call an existing method.
    *   **Static method:** `ClassName::staticMethodName` (e.g., `Math::random`)
    *   **Instance method of a particular object:** `object::instanceMethodName` (e.g., `System.out::println`)
    *   **Instance method of an arbitrary object of a particular type:** `ClassName::instanceMethodName` (e.g., `String::length`, `String::compareTo`)
    *   **Constructor reference:** `ClassName::new` (e.g., `ArrayList::new`)

#### Target Typing:
Lambda expressions do not have a type on their own. Their type is inferred from the *context* in which they are used. This context is typically a **functional interface**. A functional interface is any interface with a single abstract method (SAM). The compiler uses this target type to determine the signature of the lambda.

Example:
```java
// Predicate is a functional interface with a single abstract method `test(T t)`
Predicate<Integer> isEven = (num) -> num % 2 == 0; // Target type: Predicate<Integer>
```

## 2. Functional Interfaces

Functional interfaces are crucial for using lambda expressions. They provide the target type for lambdas. Java 8 introduced several built-in functional interfaces in the `java.util.function` package.

### `Predicate<T>`
*   **What it is:** Represents a boolean-valued function of one argument.
*   **Abstract method:** `boolean test(T t)`
*   **Use case:** Filtering elements in a stream, validating objects.
*   **Example:**
    ```java
    Predicate<String> startsWithA = s -> s.startsWith("A");
    boolean result = startsWithA.test("Apple"); // true
    ```
*   **Default methods:** `and()`, `or()`, `negate()` for composing predicates.

### `Function<T, R>`
*   **What it is:** Represents a function that accepts one argument and produces a result.
*   **Abstract method:** `R apply(T t)`
*   **Use case:** Transforming elements from one type to another (e.g., `map` operation in streams).
*   **Example:**
    ```java
    Function<String, Integer> stringToInt = s -> Integer.parseInt(s);
    Integer length = stringToInt.apply("123"); // 123
    ```
*   **Default methods:** `compose()`, `andThen()` for chaining functions.

### `Consumer<T>`
*   **What it is:** Represents an operation that accepts a single input argument and returns no result.
*   **Abstract method:** `void accept(T t)`
*   **Use case:** Performing an action on each element, such as printing (e.g., `forEach` operation).
*   **Example:**
    ```java
    Consumer<String> printUpperCase = s -> System.out.println(s.toUpperCase());
    printUpperCase.accept("hello"); // HELLO
    ```
*   **Default method:** `andThen()` for chaining consumers.

### `Supplier<T>`
*   **What it is:** Represents a supplier of results. Has no input arguments.
*   **Abstract method:** `T get()`
*   **Use case:** Lazy initialization, factory methods, generating values (e.g., `generate` in streams).
*   **Example:**
    ```java
    Supplier<Double> randomValue = () -> Math.random();
    Double value = randomValue.get(); // A random double
    ```

### `UnaryOperator<T>`
*   **What it is:** Represents an operation on a single operand that produces a result of the same type as its operand. It extends `Function<T, T>`.
*   **Abstract method:** `T apply(T t)` (inherited from `Function`)
*   **Use case:** Applying an operation that transforms an element into another element of the same type.
*   **Example:**
    ```java
    UnaryOperator<Integer> square = x -> x * x;
    Integer result = square.apply(5); // 25
    ```

### `BinaryOperator<T>`
*   **What it is:** Represents an operation upon two operands of the same type, producing a result of the same type as the operands. It extends `BiFunction<T, T, T>`.
*   **Abstract method:** `T apply(T t1, T t2)` (inherited from `BiFunction`)
*   **Use case:** Reducing two elements into a single one of the same type (e.g., `reduce` operation).
*   **Example:**
    ```java
    BinaryOperator<Integer> sum = (a, b) -> a + b;
    Integer result = sum.apply(10, 20); // 30
    ```

### `Comparator<T>`
*   **What it is:** A comparison function, which imposes a total ordering on some collection of objects. While it existed before Java 8, it became a functional interface and gained default methods.
*   **Abstract method:** `int compare(T o1, T o2)`
*   **Use case:** Sorting collections or streams.
*   **Example:**
    ```java
    Comparator<String> lengthComparator = (s1, s2) -> s1.length() - s2.length();
    // Using with streams: .sorted(lengthComparator)
    ```
*   **Static methods:** `comparing()`, `naturalOrder()`, `reverseOrder()`, `nullsFirst()`, `nullsLast()` for convenient comparator creation.
*   **Default methods:** `reversed()`, `thenComparing()` for chaining comparators.

## 3. Java Streams API

### What it is
The Java Streams API (introduced in Java 8) provides a powerful and declarative way to process sequences of elements. It's not a data structure itself but a sequence of elements that can be processed. Streams enable functional-style operations on collections, arrays, and other data sources.

### Why it exists
Before streams, processing collections involved explicit loops (external iteration). This often led to verbose, imperative code that obscured the intent of the operation and was difficult to parallelize. Streams introduce internal iteration, allowing the API to manage the iteration process, which offers:
*   **Conciseness:** Less boilerplate code.
*   **Readability:** Expresses the *what* rather than the *how*.
*   **Composability:** Operations can be chained together.
*   **Parallelism:** Easier to parallelize computations with `parallelStream()`.

### How it works internally
A stream pipeline consists of:
1.  **Source:** A collection, array, I/O channel, generator function, etc.
2.  **Zero or more intermediate operations:** These transform the stream (e.g., `filter`, `map`, `sorted`). They are lazy, meaning they don't process data until a terminal operation is invoked.
3.  **A terminal operation:** This produces a result or a side-effect (e.g., `forEach`, `collect`, `reduce`). This triggers the actual processing of the stream.

When a terminal operation is called, the stream pipeline "wakes up" and processes elements. Each element typically flows through all the intermediate operations before the next element starts. This "vertical" processing is crucial for performance and short-circuiting.

### When to use it and when NOT to use it
**When to use:**
*   Processing collections, arrays, or other data sources where you need to filter, map, reduce, or perform other aggregate operations.
*   When your logic involves transforming data from one form to another.
*   When readability and conciseness are priorities for data manipulation.
*   When you might benefit from parallel processing (though with caution).

**When NOT to use:**
*   When you need to modify the original collection. Streams are designed for non-mutating operations on the source (though intermediate operations might create new collections).
*   For simple loops where a traditional `for-each` loop is clearer and sufficient, especially if no complex transformations are involved.
*   When performance is absolutely critical and the overhead of stream creation/boxing/unboxing (especially for primitive streams if not handled correctly) might be a concern (though often negligible).
*   When dealing with I/O operations that might block; `parallelStream()` with blocking I/O can lead to thread pool exhaustion.

### Common pitfalls, performance issues, and interview traps
*   **Laziness and Terminal Operations:** Forgetting a terminal operation means the stream pipeline will never execute, leading to unexpected behavior or no output.
*   **Side-effects in Intermediate Operations:** Intermediate operations should ideally be stateless and non-interfering. Performing side-effects (e.g., modifying a shared mutable variable) within `filter` or `map` can lead to unpredictable results, especially with parallel streams. Use `peek` for debugging side effects, but not for changing state.
*   **Source Modification:** Modifying the source of a stream *while* the stream is being processed (interfering behavior) is generally forbidden and can lead to `ConcurrentModificationException` or undefined behavior.
*   **Performance of Parallel Streams:**
    *   **Overhead:** Creating and managing threads for parallel streams has an overhead. For small data sets or operations that are not computationally intensive, parallel streams can be slower than sequential streams.
    *   **Shared Mutable State:** Parallel streams are highly susceptible to race conditions and incorrect results if intermediate or terminal operations mutate shared state. Operations should be stateless and non-interfering.
    *   **Cost of Splitting/Merging:** Splitting data for parallel processing and then merging results also incurs costs.
    *   **Workload Imbalance:** Uneven distribution of work among threads can negate the benefits.
    *   **I/O Bound Operations:** Parallel streams use the common `ForkJoinPool`. If tasks block on I/O, they can starve the common pool, negatively impacting other parts of the application that use it. It's generally better to use custom thread pools for I/O-bound parallel tasks.
*   **Boxing/Unboxing:** Using `Stream<Integer>` instead of `IntStream` for primitive integers can incur performance penalties due to boxing and unboxing operations. Java provides `IntStream`, `LongStream`, and `DoubleStream` for primitives.
*   **Debugging:** Debugging stream pipelines can be challenging due to their declarative nature and lazy evaluation. `peek()` is useful here.
*   **Reusability:** Streams cannot be reused. Once a terminal operation is invoked, the stream is "consumed." Attempting to operate on it again will throw an `IllegalStateException`.

### Lazy evaluation
Intermediate operations in a stream pipeline are "lazy." This means they are not executed until a terminal operation is invoked. When a terminal operation is called, the data elements flow through the entire pipeline one by one, rather than each intermediate operation processing the entire collection before the next one starts. This allows for:
*   **Efficiency:** Operations are only performed when necessary.
*   **Short-circuiting:** For operations like `findFirst()` or `anyMatch()`, the stream can stop processing as soon as a match is found, saving computations.

### Stateless vs stateful operations
*   **Stateless Intermediate Operations:** Do not maintain any state from previously seen elements. Each element is processed independently. Examples: `filter()`, `map()`, `peek()`. These are generally safe for parallel streams.
*   **Stateful Intermediate Operations:** Require state to be maintained during the processing of the stream, or may require multiple passes over the data. Examples: `distinct()`, `sorted()`, `limit()`, `skip()`. These operations might perform worse in parallel streams or require more memory as they might need to buffer elements. For example, `sorted()` needs to see all elements before it can produce any output.

### Short-circuiting operations
Short-circuiting operations are a special type of intermediate or terminal operation that can terminate the processing of elements in the stream pipeline early, without processing all elements, if the result can be determined sooner.
*   **Intermediate Short-circuiting:** `limit()`, `skip()`
    *   `limit(n)`: Returns a stream consisting of at most `n` elements. Once `n` elements are processed, the upstream operations can stop.
    *   `skip(n)`: Skips the first `n` elements. Once `n` elements are skipped, the remaining elements are processed.
*   **Terminal Short-circuiting:** `anyMatch()`, `allMatch()`, `noneMatch()`, `findFirst()`, `findAny()`
    *   `anyMatch()`: Returns `true` as soon as a matching element is found.
    *   `findFirst()`: Returns the first element found.
    *   `findAny()`: Returns any element found (useful in parallel streams for potentially faster results).

### Stream pipeline execution
1.  **Build Phase:** When intermediate operations are chained, they build an internal representation of the pipeline. No actual data processing occurs.
2.  **Execution Phase:** When a terminal operation is called, the pipeline is activated.
    *   Elements from the source are pulled one by one.
    *   Each element flows through all intermediate operations sequentially (vertically).
    *   Intermediate operations perform their action.
    *   The terminal operation consumes the processed element or aggregates the results.
    *   This vertical processing allows for lazy evaluation and short-circuiting.

### Internal iteration vs external iteration
*   **External Iteration:** The client code explicitly controls the iteration process using constructs like `for`, `for-each`, or iterators.
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    for (String name : names) { // External iteration
        System.out.println(name);
    }
    ```
    *   **Pros:** Explicit control, familiar.
    *   **Cons:** Boilerplate code, harder to optimize for parallelism, obscures intent.

*   **Internal Iteration:** The library (Streams API) controls the iteration process. The client provides the operations to be performed on elements.
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    names.stream().forEach(System.out::println); // Internal iteration
    ```
    *   **Pros:** Concise, declarative, allows the library to optimize execution (e.g., parallelism, fusion of operations, short-circuiting), separates *what* from *how*.
    *   **Cons:** Less explicit control, can be harder to debug for complex pipelines.

### Sequential vs parallel streams

#### Sequential Streams
*   Process elements one after another in a single thread.
*   The default mode for all stream operations.
*   Generally preferred for small to medium-sized datasets or operations that are not CPU-bound.

#### Parallel Streams
*   Process elements concurrently using multiple threads, leveraging the `ForkJoinPool.commonPool()`.
*   Obtained by calling `parallelStream()` on a collection or `parallel()` on a sequential stream.
*   The stream is divided into multiple sub-streams, which are processed independently and their results are then combined.

**Advantages of Parallel Streams:**
*   **Performance:** Can significantly speed up CPU-bound operations on large datasets by utilizing multi-core processors.
*   **Simplicity:** Easy to switch from sequential to parallel using `parallelStream()` or `parallel()`.

**Disadvantages and Pitfalls of Parallel Streams (Critical for SDE-2):**
1.  **Overhead:**
    *   **Fork/Join Costs:** Splitting the stream into sub-tasks (forking) and combining results (joining) incurs overhead.
    *   **Data Structures:** The underlying data structure matters. `ArrayList` and arrays split efficiently (`O(1)`), `LinkedList` and `HashSet` are poor for parallel splitting (`O(N)` traversal to find split points).
    *   **Work Stealing:** The `ForkJoinPool` uses work-stealing, which has its own overhead.
    *   **Small Data Sets:** For small datasets, the overhead often outweighs the benefits, making parallel streams slower than sequential.

2.  **Shared Mutable State:**
    *   **Race Conditions:** Operations like `forEach` should not mutate external shared state, as multiple threads will concurrently attempt to write, leading to non-deterministic results.
    *   **Non-deterministic Behavior:** If your lambda expressions have side effects (i.e., they modify state outside the lambda), parallel streams can lead to unpredictable outcomes.
    *   **Solutions:** Use `collect` with concurrent collectors or `reduce` for safe accumulation. Ensure functions are stateless and non-interfering.

3.  **Thread Pool Exhaustion (Common ForkJoinPool):**
    *   Parallel streams use `ForkJoinPool.commonPool()`. If you have many parallel streams executing long-running or blocking I/O tasks, they can exhaust this shared pool, starving other parts of your application that rely on it (e.g., other parallel streams, `CompletableFuture`).
    *   **Solution:** For blocking I/O tasks or custom thread management, consider using `CompletableFuture` with a custom `ExecutorService` instead of directly using `parallelStream()`. Or, if absolutely necessary, create a custom `ForkJoinPool` and run parallel stream tasks within it (e.g., `ForkJoinPool customPool = new ForkJoinPool(numThreads); customPool.submit(() -> stream.parallel().forEach(...)).join();`).

4.  **Order of Elements:**
    *   For ordered streams, parallel processing still guarantees encounter order for most operations (e.g., `forEachOrdered`). However, operations like `forEach` (without `Ordered`) might process elements in non-deterministic order, which can be faster.
    *   If order is not required, explicitly remove the `ORDERED` characteristic for potential performance gains using `unordered()`.

5.  **Debugging:** Debugging parallel streams can be more complex due to concurrency issues and non-deterministic execution paths.

**When to use Parallel Streams:**
*   Large datasets (millions of elements or more).
*   CPU-bound operations (e.g., heavy computations, complex transformations).
*   Operations that are naturally parallelizable (e.g., `map`, `filter`).
*   Source data structure is efficient for splitting (e.g., `ArrayList`, arrays).
*   The operations are stateless, non-interfering, and do not mutate shared external state.

**Performance Considerations for SDE-2:**
*   **Benchmarking:** Always benchmark sequential vs. parallel performance for your specific use case. Don't assume parallel is always faster.
*   **Cost of Operation vs. Data Size:** Parallel streams are beneficial when the cost of processing each element is high, and the number of elements is large. If processing each element is trivial, the overhead might dominate.
*   **Number of Cores:** Benefits scale with the number of available CPU cores.
*   **Avoid `System.out.println`:** Printing inside parallel streams is often a performance bottleneck and can lead to interleaved output.

## 4. Stream Creation Methods

### `stream()` (from Collection)
*   **Purpose:** Creates a sequential stream from a collection.
*   **Signature:** `default Stream<E> stream()` (on `Collection` interface)
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    Stream<String> nameStream = names.stream();
    ```

### `parallelStream()` (from Collection)
*   **Purpose:** Creates a parallel stream from a collection.
*   **Signature:** `default Stream<E> parallelStream()` (on `Collection` interface)
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    Stream<String> parallelNameStream = names.parallelStream();
    ```

### `Stream.of()`
*   **Purpose:** Creates a sequential stream from a variable number of elements or an array.
*   **Signature:** `static <T> Stream<T> of(T... values)`
*   **Example (varargs):**
    ```java
    Stream<String> stringStream = Stream.of("a", "b", "c");
    ```
*   **Example (array):**
    ```java
    String[] array = {"foo", "bar", "baz"};
    Stream<String> arrayStream = Stream.of(array);
    ```

### `Arrays.stream()`
*   **Purpose:** Creates a sequential stream from an array. Provides overloaded versions for primitive arrays (`int[]`, `long[]`, `double[]`) to create `IntStream`, `LongStream`, `DoubleStream`.
*   **Signature:** `static <T> Stream<T> stream(T[] array)`
    `static IntStream stream(int[] array)` (and for `long`, `double`)
*   **Example:**
    ```java
    int[] numbers = {1, 2, 3, 4, 5};
    IntStream intStream = Arrays.stream(numbers);
    ```

### `Stream.iterate()`
*   **Purpose:** Creates an infinite sequential ordered stream by repeatedly applying a function to an initial element.
*   **Signature:**
    *   `static <T> Stream<T> iterate(T seed, UnaryOperator<T> f)` (infinite)
    *   `static <T> Stream<T> iterate(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)` (finite, Java 9+)
*   **Example (infinite):**
    ```java
    Stream<Integer> oddNumbers = Stream.iterate(1, n -> n + 2); // 1, 3, 5, ...
    oddNumbers.limit(5).forEach(System.out::println); // Prints 1, 3, 5, 7, 9
    ```
*   **Example (finite - Java 9+):**
    ```java
    Stream<Integer> finiteEvenNumbers = Stream.iterate(0, n -> n <= 10, n -> n + 2); // 0, 2, 4, 6, 8, 10
    finiteEvenNumbers.forEach(System.out::println);
    ```

### `Stream.generate()`
*   **Purpose:** Creates an infinite sequential unordered stream of elements by applying a `Supplier`.
*   **Signature:** `static <T> Stream<T> generate(Supplier<T> s)`
*   **Example:**
    ```java
    Stream<Double> randomNumbers = Stream.generate(Math::random);
    randomNumbers.limit(3).forEach(System.out::println); // Prints 3 random doubles
    ```
*   **Note:** `generate` creates an unordered stream, which can be useful for parallel processing where order is not important.

### `Stream.builder()`
*   **Purpose:** Creates a `Stream.Builder` to incrementally construct a stream. Useful when you don't know all elements upfront but want to add them one by one.
*   **Signature:** `static <T> Stream.Builder<T> builder()`
*   **Example:**
    ```java
    Stream.Builder<String> builder = Stream.builder();
    builder.add("hello").add("world");
    // ... potentially add more elements based on some logic
    Stream<String> builtStream = builder.build();
    builtStream.forEach(System.out::println); // Prints "hello", "world"
    ```

### Other Stream Creation Methods:
*   **`IntStream.range(int startInclusive, int endExclusive)`:** Creates an ordered `IntStream` from `startInclusive` to `endExclusive-1`.
*   **`IntStream.rangeClosed(int startInclusive, int endInclusive)`:** Creates an ordered `IntStream` from `startInclusive` to `endInclusive`.
*   **`BufferedReader.lines()`:** Creates a `Stream<String>` from lines in a `BufferedReader`.
*   **`Files.lines(Path path)`:** Creates a `Stream<String>` from lines of a file.
*   **`Pattern.splitAsStream(CharSequence input)`:** Creates a `Stream<String>` from input, split by a regex pattern.

## 5. Intermediate Operations

Intermediate operations transform a stream into another stream. They are lazy and return a new `Stream` object.

### `map()`
*   **Purpose:** Transforms each element of the stream by applying a function to it.
*   **Signature:** `<R> Stream<R> map(Function<? super T, ? extends R> mapper)`
*   **Example:**
    ```java
    List<String> words = Arrays.asList("apple", "banana", "cat");
    words.stream()
         .map(String::toUpperCase) // Transforms "apple" to "APPLE" etc.
         .forEach(System.out::println); // APPLE, BANANA, CAT
    ```

### `flatMap()`
*   **Purpose:** Transforms each element into a stream of elements, and then flattens these streams into a single stream. Useful when an element can produce zero, one, or many output elements.
*   **Signature:** `<R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper)`
*   **Example:**
    ```java
    List<List<Integer>> listOfLists = Arrays.asList(Arrays.asList(1, 2), Arrays.asList(3, 4, 5));
    listOfLists.stream()
               .flatMap(Collection::stream) // Flattens List<List<Integer>> to Stream<Integer>
               .forEach(System.out::println); // 1, 2, 3, 4, 5
    ```
*   **Common Use Case:** Tokenizing strings (e.g., splitting a sentence into words) or processing nested collections.

### `filter()`
*   **Purpose:** Selects elements from the stream that match a given `Predicate`.
*   **Signature:** `Stream<T> filter(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    numbers.stream()
           .filter(n -> n % 2 == 0) // Keeps only even numbers
           .forEach(System.out::println); // 2, 4
    ```

### `distinct()`
*   **Purpose:** Returns a stream consisting of the distinct elements (based on `equals()`) of the original stream. It's a stateful operation.
*   **Signature:** `Stream<T> distinct()`
*   **Example:**
    ```java
    List<String> duplicates = Arrays.asList("a", "b", "a", "c", "b");
    duplicates.stream()
              .distinct()
              .forEach(System.out::println); // a, b, c (order might vary based on implementation, typically insertion order maintained for sequential streams)
    ```

### `sorted()`
*   **Purpose:** Returns a stream consisting of the elements of this stream, sorted according to natural order or a provided `Comparator`. It's a stateful operation.
*   **Signature:**
    *   `Stream<T> sorted()` (natural order, elements must implement `Comparable`)
    *   `Stream<T> sorted(Comparator<? super T> comparator)` (custom comparator)
*   **Example (natural order):**
    ```java
    List<String> unsorted = Arrays.asList("banana", "apple", "cat");
    unsorted.stream().sorted().forEach(System.out::println); // apple, banana, cat
    ```
*   **Example (custom comparator):**
    ```java
    List<String> unsorted = Arrays.asList("banana", "apple", "cat");
    unsorted.stream()
            .sorted(Comparator.comparingInt(String::length)) // Sort by length
            .forEach(System.out::println); // cat, apple, banana (or apple, cat, banana)
    ```
*   **Performance Trap:** `sorted()` requires buffering all elements before producing any output, which can consume significant memory and time for large streams, especially with `parallelStream()`.

### `peek()`
*   **Purpose:** Performs an action on each element of the stream as elements are consumed from the resulting stream. Primarily for debugging or logging. It's an intermediate operation.
*   **Signature:** `Stream<T> peek(Consumer<? super T> action)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3);
    numbers.stream()
           .filter(n -> n > 1)
           .peek(n -> System.out.println("Filtered: " + n)) // Debugging print
           .map(n -> n * 10)
           .forEach(n -> System.out.println("Mapped: " + n));
    // Output:
    // Filtered: 2
    // Mapped: 20
    // Filtered: 3
    // Mapped: 30
    ```
*   **Important:** Do NOT use `peek()` to modify state or perform other side-effects that are crucial to the stream's logic. It's meant for introspection.

### `limit()`
*   **Purpose:** Truncates the stream to contain no more than a given number of elements. It's a short-circuiting intermediate operation.
*   **Signature:** `Stream<T> limit(long maxSize)`
*   **Example:**
    ```java
    Stream.iterate(1, n -> n + 1)
          .limit(5) // Takes only the first 5 elements
          .forEach(System.out::println); // 1, 2, 3, 4, 5
    ```

### `skip()`
*   **Purpose:** Skips the first `n` elements of the stream.
*   **Signature:** `Stream<T> skip(long n)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    numbers.stream()
           .skip(2) // Skips 1 and 2
           .forEach(System.out::println); // 3, 4, 5
    ```

## 6. Terminal Operations

Terminal operations produce a result or a side-effect, and terminate the stream pipeline. After a terminal operation, the stream cannot be used again.

### `forEach()`
*   **Purpose:** Performs an action for each element of this stream.
*   **Signature:** `void forEach(Consumer<? super T> action)`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    names.stream().forEach(System.out::println); // Alice, Bob
    ```
*   **Note:** `forEach` does not guarantee order of processing for parallel streams. If order is important, use `forEachOrdered()`. Avoid using `forEach` for modifying external mutable state in parallel streams.

### `collect()`
*   **Purpose:** Performs a mutable reduction operation on the elements of this stream, accumulating them into a collection or summary result.
*   **Signature:** `<R, A> R collect(Collector<? super T, A, R> collector)`
*   **Example (toList):**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    List<String> upperCaseNames = names.stream()
                                       .map(String::toUpperCase)
                                       .collect(Collectors.toList()); // [ALICE, BOB]
    ```
*   **Explanation:** `collect()` is one of the most powerful terminal operations, as it leverages the `Collector` interface to perform various reduction strategies.

### `reduce()`
*   **Purpose:** Performs a reduction on the elements of this stream, using an associative accumulation function, and returns an `Optional` describing the reduced value.
*   **Signature:**
    *   `Optional<T> reduce(BinaryOperator<T> accumulator)` (no identity, returns `Optional`)
    *   `T reduce(T identity, BinaryOperator<T> accumulator)` (with identity, returns `T`)
    *   `<U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner)` (with identity and combiner for parallel streams)
*   **Example (sum of numbers with identity):**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    Integer sum = numbers.stream().reduce(0, (a, b) -> a + b); // 15
    // Equivalent to: Integer sum = numbers.stream().reduce(0, Integer::sum);
    ```
*   **Example (find max without identity):**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    Optional<Integer> max = numbers.stream().reduce(Integer::max); // Optional[5]
    ```
*   **The third `reduce` variant (with combiner):** This is crucial for parallel streams. The `accumulator` combines an element with the partial result. The `combiner` combines two partial results (from different threads/sub-streams) into one. The `identity` is the initial value for each sub-reduction and also the identity for the `combiner`. It must be true that `combiner.apply(identity, a)` is equal to `a`.

### `findFirst()`
*   **Purpose:** Returns an `Optional` describing the first element of this stream, or an empty `Optional` if the stream is empty. It's a short-circuiting operation.
*   **Signature:** `Optional<T> findFirst()`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    Optional<String> first = names.stream().findFirst(); // Optional["Alice"]
    ```
*   **Note:** In ordered streams, `findFirst()` will always return the first element.

### `findAny()`
*   **Purpose:** Returns an `Optional` describing any element of the stream, or an empty `Optional` if the stream is empty. It's a short-circuiting operation.
*   **Signature:** `Optional<T> findAny()`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    Optional<String> any = names.stream().findAny(); // Optional["Alice"] or Optional["Bob"]
    ```
*   **Note:** This is useful with parallel streams as it can return any element that matches, potentially much faster than `findFirst()` which has to respect encounter order. The element returned can vary.

### `anyMatch()`
*   **Purpose:** Returns `true` if any elements of this stream match the provided predicate, otherwise `false`. It's a short-circuiting operation.
*   **Signature:** `boolean anyMatch(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    boolean hasEven = numbers.stream().anyMatch(n -> n % 2 == 0); // true
    ```

### `allMatch()`
*   **Purpose:** Returns `true` if all elements of this stream match the provided predicate, otherwise `false`. It's a short-circuiting operation.
*   **Signature:** `boolean allMatch(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(2, 4, 6);
    boolean allEven = numbers.stream().allMatch(n -> n % 2 == 0); // true
    ```

### `noneMatch()`
*   **Purpose:** Returns `true` if no elements of this stream match the provided predicate, otherwise `false`. It's a short-circuiting operation.
*   **Signature:** `boolean noneMatch(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 3, 5);
    boolean noEven = numbers.stream().noneMatch(n -> n % 2 == 0); // true
    ```

### `count()`
*   **Purpose:** Returns the count of elements in this stream.
*   **Signature:** `long count()`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    long count = names.stream().count(); // 3
    ```

### `min()`
*   **Purpose:** Returns an `Optional` describing the minimum element of this stream according to the provided `Comparator`.
*   **Signature:** `Optional<T> min(Comparator<? super T> comparator)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(5, 2, 8, 1);
    Optional<Integer> min = numbers.stream().min(Integer::compare); // Optional[1]
    ```

### `max()`
*   **Purpose:** Returns an `Optional` describing the maximum element of this stream according to the provided `Comparator`.
*   **Signature:** `Optional<T> max(Comparator<? super T> comparator)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(5, 2, 8, 1);
    Optional<Integer> max = numbers.stream().max(Integer::compare); // Optional[8]
    ```

## 7. Collectors Methods (`java.util.stream.Collectors`)

`Collectors` is a utility class providing static methods for creating `Collector` instances, which are used with the `collect()` terminal operation to transform a stream into various data structures or summary results.

### `toList()`
*   **Purpose:** Accumulates the input elements into a new `List`. The type, mutability, serializability, and thread-safety of the `List` are not guaranteed; for example, `ArrayList` is a common result.
*   **Signature:** `static <T> Collector<T, ?, List<T>> toList()`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    List<String> result = names.stream().collect(Collectors.toList()); // [Alice, Bob]
    ```

### `toSet()`
*   **Purpose:** Accumulates the input elements into a new `Set`.
*   **Signature:** `static <T> Collector<T, ?, Set<T>> toSet()`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 2);
    Set<Integer> uniqueNumbers = numbers.stream().collect(Collectors.toSet()); // [1, 2, 3] (order not guaranteed)
    ```

### `toMap()`
*   **Purpose:** Accumulates elements into a `Map` whose keys and values are the results of applying the provided mapping functions to the input elements. Overloaded versions handle key collisions and specify the map type.
*   **Signature (basic):** `static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper)`
*   **Example (no collision):**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob");
    Map<Character, String> firstCharMap = names.stream()
                                              .collect(Collectors.toMap(s -> s.charAt(0), Function.identity()));
    // {A=Alice, B=Bob}
    ```
*   **Signature (with collision resolver):** `static <T, K, U> Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction)`
*   **Example (with collision):**
    ```java
    List<String> names = Arrays.asList("Alice", "Anna", "Bob");
    Map<Character, String> firstCharMap = names.stream()
                                              .collect(Collectors.toMap(s -> s.charAt(0),
                                                                        Function.identity(),
                                                                        (oldValue, newValue) -> oldValue + ", " + newValue));
    // {A=Alice, Anna, B=Bob}
    ```
*   **Signature (with map supplier):** `static <T, K, U, M extends Map<K, U>> Collector<T, ?, M> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper, BinaryOperator<U> mergeFunction, Supplier<M> mapSupplier)`
*   **Example (specific map type):**
    ```java
    Map<Character, String> treeMap = names.stream()
                                         .collect(Collectors.toMap(s -> s.charAt(0),
                                                                   Function.identity(),
                                                                   (oldValue, newValue) -> oldValue, // Take old value on collision
                                                                   TreeMap::new)); // Use TreeMap
    ```

### `groupingBy()`
*   **Purpose:** Groups input elements according to a classification function, and returns a `Map` whose keys are the results of the classification function and whose values are lists of the input elements. Overloaded versions allow specifying downstream collectors.
*   **Signature (basic):** `static <T, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends K> classifier)`
*   **Example (group by length):**
    ```java
    List<String> words = Arrays.asList("apple", "cat", "banana", "dog");
    Map<Integer, List<String>> byLength = words.stream()
                                             .collect(Collectors.groupingBy(String::length));
    // {3=[cat, dog], 5=[apple], 6=[banana]}
    ```
*   **Signature (with downstream collector):** `static <T, K, A, D> Collector<T, ?, Map<K, D>> groupingBy(Function<? super T, ? extends K> classifier, Collector<? super T, A, D> downstream)`
*   **Example (group by length, then count):**
    ```java
    Map<Integer, Long> byLengthCount = words.stream()
                                            .collect(Collectors.groupingBy(String::length, Collectors.counting()));
    // {3=2, 5=1, 6=1}
    ```
*   **Signature (with map supplier and downstream):** `static <T, K, D, A, M extends Map<K, D>> Collector<T, ?, M> groupingBy(Function<? super T, ? extends K> classifier, Supplier<M> mapFactory, Collector<? super T, A, D> downstream)`
*   **Example (specific map type, counting):**
    ```java
    TreeMap<Integer, Long> treeMapByLength = words.stream()
                                                 .collect(Collectors.groupingBy(String::length, TreeMap::new, Collectors.counting()));
    // {3=2, 5=1, 6=1} (ordered by key)
    ```

### `partitioningBy()`
*   **Purpose:** Partitions the input elements according to a `Predicate`, producing a `Map<Boolean, List<T>>`. Elements for which the predicate is `true` are in the `true` list, and `false` otherwise.
*   **Signature (basic):** `static <T> Collector<T, ?, Map<Boolean, List<T>>> partitioningBy(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
    Map<Boolean, List<Integer>> evenOdd = numbers.stream()
                                                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
    // {false=[1, 3, 5], true=[2, 4]}
    ```
*   **Signature (with downstream collector):** `static <T, A, D> Collector<T, ?, Map<Boolean, D>> partitioningBy(Predicate<? super T> predicate, Collector<? super T, A, D> downstream)`
*   **Example (partitioning and counting):**
    ```java
    Map<Boolean, Long> evenOddCount = numbers.stream()
                                             .collect(Collectors.partitioningBy(n -> n % 2 == 0, Collectors.counting()));
    // {false=3, true=2}
    ```

### `mapping()`
*   **Purpose:** Adapts a `Collector` to another `Collector` by applying a mapping function to each input element before accumulation. Often used as a downstream collector in `groupingBy`.
*   **Signature:** `static <T, U, A, R> Collector<T, ?, R> mapping(Function<? super T, ? extends U> mapper, Collector<? super U, A, R> downstream)`
*   **Example (group by length, then map to upper case and collect to a list):**
    ```java
    List<String> words = Arrays.asList("apple", "cat", "banana", "dog");
    Map<Integer, List<String>> byLengthUpperCase = words.stream()
                                                       .collect(Collectors.groupingBy(String::length,
                                                                                       Collectors.mapping(String::toUpperCase, Collectors.toList())));
    // {3=[CAT, DOG], 5=[APPLE], 6=[BANANA]}
    ```

### `joining()`
*   **Purpose:** Returns a `Collector` that concatenates the input elements into a `String`. Overloaded versions allow specifying a delimiter, prefix, and suffix.
*   **Signature (basic):** `static Collector<CharSequence, ?, String> joining()`
*   **Signature (with delimiter):** `static Collector<CharSequence, ?, String> joining(CharSequence delimiter)`
*   **Signature (with delimiter, prefix, suffix):** `static Collector<CharSequence, ?, String> joining(CharSequence delimiter, CharSequence prefix, CharSequence suffix)`
*   **Example:**
    ```java
    List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
    String joinedNames = names.stream().collect(Collectors.joining(", ")); // "Alice, Bob, Charlie"
    String fancyJoinedNames = names.stream().collect(Collectors.joining(" & ", "[", "]")); // "[Alice & Bob & Charlie]"
    ```

### `counting()`
*   **Purpose:** Returns a `Collector` that counts the number of input elements.
*   **Signature:** `static <T> Collector<T, ?, Long> counting()`
*   **Example (used as downstream):**
    ```java
    List<String> words = Arrays.asList("apple", "cat", "banana");
    Long count = words.stream().collect(Collectors.counting()); // 3
    ```

### `reducing()`
*   **Purpose:** A more general-purpose reduction collector. It allows specifying an identity, a mapping function, and a binary operator.
*   **Signature (basic):** `static <T> Collector<T, ?, Optional<T>> reducing(BinaryOperator<T> op)`
*   **Signature (with identity):** `static <T> Collector<T, ?, T> reducing(T identity, BinaryOperator<T> op)`
*   **Signature (with identity, mapper, operator):** `static <T, U> Collector<T, ?, U> reducing(U identity, Function<? super T, ? extends U> mapper, BinaryOperator<U> op)`
*   **Example (sum of lengths using `reducing`):**
    ```java
    List<String> words = Arrays.asList("apple", "cat", "banana");
    Integer totalLength = words.stream()
                               .collect(Collectors.reducing(0, String::length, Integer::sum)); // 14
    ```

### Other Important Collectors (Self-explanatory for SDE-2):
*   `minBy()`, `maxBy()`: Returns an `Optional` containing the minimum/maximum element based on a `Comparator`.
*   `summingInt()`, `summingLong()`, `summingDouble()`: Calculates the sum of a mapped integer/long/double property.
*   `averagingInt()`, `averagingLong()`, `averagingDouble()`: Calculates the average of a mapped integer/long/double property.
*   `summarizingInt()`, `summarizingLong()`, `summarizingDouble()`: Returns a summary statistics object (`IntSummaryStatistics`, etc.) containing count, sum, min, max, and average.
*   `collectingAndThen()`: Performs a final transformation on the result of another collector. E.g., `collectingAndThen(toList(), Collections::unmodifiableList)`.

## 8. Optional Methods (`java.util.Optional`)

`Optional` is a container object used to represent a value that may or may not be present. It helps in dealing with `null` values and avoiding `NullPointerExceptions`. It encourages a more explicit way of handling potential absence of a value.

### `Optional.of()`
*   **Purpose:** Returns an `Optional` with the specified present non-null value. Throws `NullPointerException` if the value is null.
*   **Signature:** `static <T> Optional<T> of(T value)`
*   **Example:**
    ```java
    Optional<String> name = Optional.of("John"); // Optional["John"]
    // Optional.of(null); // Throws NullPointerException
    ```

### `Optional.ofNullable()`
*   **Purpose:** Returns an `Optional` describing the specified value, with an empty `Optional` if the value is null.
*   **Signature:** `static <T> Optional<T> ofNullable(T value)`
*   **Example:**
    ```java
    Optional<String> name1 = Optional.ofNullable("John"); // Optional["John"]
    Optional<String> name2 = Optional.ofNullable(null); // Optional.empty
    ```

### `Optional.empty()`
*   **Purpose:** Returns an empty `Optional` instance. No value is present.
*   **Signature:** `static <T> Optional<T> empty()`
*   **Example:**
    ```java
    Optional<String> emptyOptional = Optional.empty(); // Optional.empty
    ```

### `isPresent()`
*   **Purpose:** Returns `true` if a value is present in this `Optional`, otherwise `false`.
*   **Signature:** `boolean isPresent()`
*   **Example:**
    ```java
    Optional<String> name = Optional.of("John");
    if (name.isPresent()) { // true
        System.out.println(name.get()); // John
    }
    ```
*   **Anti-pattern:** Using `isPresent()` followed by `get()` is often considered an anti-pattern as it negates some of the benefits of `Optional`. Prefer `ifPresent()`, `orElse()`, `orElseGet()`, `orElseThrow()`, `map()`, or `flatMap()`.

### `isEmpty()` (Java 11+)
*   **Purpose:** Returns `true` if a value is not present in this `Optional`, otherwise `false`. (Complement of `isPresent()`).
*   **Signature:** `boolean isEmpty()`
*   **Example:**
    ```java
    Optional<String> emptyOptional = Optional.empty();
    if (emptyOptional.isEmpty()) { // true
        System.out.println("No value present.");
    }
    ```

### `ifPresent()`
*   **Purpose:** If a value is present, performs the given action with the value, otherwise does nothing.
*   **Signature:** `void ifPresent(Consumer<? super T> action)`
*   **Example:**
    ```java
    Optional<String> name = Optional.of("John");
    name.ifPresent(s -> System.out.println("Hello, " + s)); // Hello, John
    Optional.empty().ifPresent(s -> System.out.println("Won't print"));
    ```

### `orElse()`
*   **Purpose:** If a value is present, returns the value, otherwise returns a default value.
*   **Signature:** `T orElse(T other)`
*   **Example:**
    ```java
    Optional<String> name1 = Optional.ofNullable("John");
    String result1 = name1.orElse("Default Name"); // John
    Optional<String> name2 = Optional.ofNullable(null);
    String result2 = name2.orElse("Default Name"); // Default Name
    ```
*   **Pitfall:** The `other` argument to `orElse()` is *always evaluated*, even if the `Optional` is present. If `other` is a costly operation, this can be inefficient. For lazy evaluation, use `orElseGet()`.

### `orElseGet()`
*   **Purpose:** If a value is present, returns the value, otherwise returns the result produced by the supplying function.
*   **Signature:** `T orElseGet(Supplier<? extends T> supplier)`
*   **Example:**
    ```java
    Optional<String> name1 = Optional.ofNullable("John");
    String result1 = name1.orElseGet(() -> "Default Name (expensive)"); // John (supplier not called)
    Optional<String> name2 = Optional.ofNullable(null);
    String result2 = name2.orElseGet(() -> "Default Name (expensive)"); // Default Name (expensive) (supplier called)
    ```
*   **Best Practice:** Prefer `orElseGet()` over `orElse()` when the default value involves a potentially expensive computation or object creation.

### `orElseThrow()`
*   **Purpose:** If a value is present, returns the value, otherwise throws an exception produced by the exception supplying function.
*   **Signature:**
    *   `T orElseThrow()` (throws `NoSuchElementException`, Java 10+)
    *   `T orElseThrow(Supplier<? extends X> exceptionSupplier)` (throws custom exception)
*   **Example:**
    ```java
    Optional<String> name1 = Optional.of("John");
    String result1 = name1.orElseThrow(() -> new IllegalArgumentException("Name not found")); // John
    Optional<String> name2 = Optional.empty();
    // String result2 = name2.orElseThrow(() -> new IllegalArgumentException("Name not found")); // Throws IllegalArgumentException
    ```

### `map()`
*   **Purpose:** If a value is present, applies the given mapping function to it, and if the result is non-null, returns an `Optional` describing the result. Otherwise, returns an empty `Optional`.
*   **Signature:** `<U> Optional<U> map(Function<? super T, ? extends U> mapper)`
*   **Example:**
    ```java
    Optional<String> name = Optional.of("  john  ");
    Optional<Integer> length = name.map(String::trim).map(String::length); // Optional[4]
    ```
*   **Note:** The mapping function applied to `map` should return a non-`Optional` value. If it returns an `Optional`, you'll get a nested `Optional<Optional<U>>`. Use `flatMap` for this scenario.

### `flatMap()`
*   **Purpose:** If a value is present, applies the given `Optional`-bearing mapping function to it, returns that result. Otherwise, returns an empty `Optional`. This is used to flatten nested `Optional`s.
*   **Signature:** `<U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper)`
*   **Example:**
    ```java
    // Imagine a method that might return an Optional<String>
    Function<String, Optional<String>> getLastName = firstName -> Optional.ofNullable(firstName.equals("John") ? "Doe" : null);

    Optional<String> firstName = Optional.of("John");
    Optional<String> lastName = firstName.flatMap(getLastName); // Optional["Doe"]

    Optional<String> otherName = Optional.of("Alice");
    Optional<String> otherLastName = otherName.flatMap(getLastName); // Optional.empty
    ```

### `filter()`
*   **Purpose:** If a value is present, and the value matches the given predicate, returns an `Optional` describing the value. Otherwise (if no value is present or the value doesn't match), returns an empty `Optional`.
*   **Signature:** `Optional<T> filter(Predicate<? super T> predicate)`
*   **Example:**
    ```java
    Optional<Integer> number = Optional.of(10);
    Optional<Integer> evenNumber = number.filter(n -> n % 2 == 0); // Optional[10]
    Optional<Integer> oddNumber = number.filter(n -> n % 2 != 0); // Optional.empty
    ```

### `or()` (Java 9+)
*   **Purpose:** If a value is present, returns this `Optional`. Otherwise, returns an `Optional` produced by the supplying function.
*   **Signature:** `Optional<T> or(Supplier<? extends Optional<? extends T>> supplier)`
*   **Example:**
    ```java
    Optional<String> primary = Optional.empty();
    Optional<String> secondary = Optional.of("Secondary Value");
    Optional<String> result = primary.or(() -> secondary); // Optional["Secondary Value"]
    ```

## 9. Best Practices, Anti-Patterns, and Performance Considerations (SDE-2)

### Best Practices:
1.  **Immutability:** Strive for immutable data. Streams work best with immutable objects as elements, reducing side-effect risks, especially in parallel streams.
2.  **Referential Transparency:** Use pure functions (lambdas without side effects) in intermediate operations (`map`, `filter`, etc.). This makes code predictable and easier to reason about.
3.  **Use Primitive Streams:** For `int`, `long`, `double` primitives, use `IntStream`, `LongStream`, `DoubleStream` to avoid boxing/unboxing overhead, which can be significant for large datasets.
    *   `mapToInt`, `mapToLong`, `mapToDouble` can convert `Stream<T>` to primitive streams.
    *   `boxed()` can convert primitive streams back to object streams (e.g., `IntStream` to `Stream<Integer>`).
4.  **Chain Operations:** Chain intermediate operations for fluent, readable code. The stream pipeline optimizes these chains.
5.  **Be Specific with Collectors:** Use the most appropriate collector for the job (e.g., `toList()` if order matters and duplicates are fine, `toSet()` for uniqueness, `toMap()` for key-value pairs).
6.  **Handle `Optional` Gracefully:**
    *   Avoid `isPresent()` followed by `get()`.
    *   Prefer `orElseGet()` over `orElse()` for expensive default value computations.
    *   Use `map()`, `flatMap()`, `filter()` to chain operations on `Optional`.
    *   Use `ifPresent()` for side-effects if a value is present.
7.  **Readability over One-liners:** While streams promote conciseness, don't sacrifice readability for an overly complex one-liner. Break down complex pipelines into multiple lines or methods.
8.  **Understand Source Characteristics:** Be aware of whether your stream source is ordered or unordered, and how operations like `distinct()`, `sorted()`, `findAny()` interact with these characteristics.
9.  **Clear Naming:** Name your lambda parameters descriptively (e.g., `user -> user.getName()` instead of `u -> u.getName()`).

### Anti-Patterns:
1.  **External Side-Effects in Intermediate Operations:**
    ```java
    List<Integer> list = new ArrayList<>();
    numbers.stream()
           .filter(n -> { list.add(n); return n % 2 == 0; }) // Anti-pattern: Modifying external state
           .forEach(System.out::println);
    ```
    This is non-deterministic in parallel streams and generally bad practice. Use `collect()` for accumulation.
2.  **Overuse of `forEach`:** `forEach` is a terminal operation that consumes the stream and performs an action. It should generally be used for side-effects (e.g., printing). For transforming or accumulating, `map`, `filter`, `collect`, `reduce` are more appropriate.
    ```java
    // Anti-pattern: using forEach to build a new list
    List<String> upperCaseNames = new ArrayList<>();
    names.stream().forEach(name -> upperCaseNames.add(name.toUpperCase())); // Bad
    // Good:
    List<String> upperCaseNames = names.stream().map(String::toUpperCase).collect(Collectors.toList());
    ```
3.  **Converting to Array then Stream:** Avoid converting a collection to an array only to create a stream from it again.
    ```java
    List<String> names = Arrays.asList("a", "b");
    String[] arr = names.toArray(new String[0]);
    Stream<String> stream = Arrays.stream(arr); // Avoid this
    // Just use: names.stream()
    ```
4.  **Unnecessary Parallel Streams:** As discussed, `parallelStream()` is not a magic bullet and can often degrade performance for smaller datasets or I/O-bound tasks. Benchmark before using.
5.  **Reusing Consumed Streams:**
    ```java
    Stream<String> stream = names.stream().filter(s -> s.startsWith("A"));
    stream.count(); // Stream consumed
    stream.forEach(System.out::println); // IllegalStateException
    ```
    Always create a new stream for each pipeline if you need to perform multiple terminal operations.
6.  **Blocking Operations in `ForkJoinPool.commonPool()`:** Avoid performing blocking I/O (database calls, network requests) directly within a `parallelStream()` pipeline, as it can starve the shared common pool.

### Performance Considerations for SDE-2:
1.  **Source Type:** The efficiency of parallel streams heavily depends on the stream source.
    *   **Excellent:** `ArrayList`, `Array` (O(1) spliterator traversal and splitting)
    *   **Good:** `HashSet`, `TreeSet` (O(logN) or O(N) spliterator traversal, but splitting is okay)
    *   **Poor:** `LinkedList` (O(N) spliterator traversal and splitting due to non-random access characteristics)
    *   **Infinite Streams:** Be careful with parallel streams from `Stream.iterate` or `Stream.generate`, as splitting them efficiently is complex, and `limit()` is stateful.
2.  **Number of Elements:** For `N < 10,000` (roughly), sequential streams are often faster due to parallelism overhead. Benchmark to confirm.
3.  **Cost of Work per Element:** Parallel streams shine when the computation per element is high (CPU-bound tasks). If it's trivial (e.g., `map(s -> s + "!")`), the overhead will likely outweigh benefits.
4.  **Stateless vs. Stateful Operations:** Stateful operations (`distinct()`, `sorted()`, `limit()`, `skip()`) can be expensive in parallel streams as they might require buffering or more complex coordination.
5.  **Garbage Collection:** Excessive creation of intermediate `Optional` or wrapper objects (due to primitive stream misuse) can lead to GC pressure.
6.  **`unordered()`:** If the encounter order is not important, calling `unordered()` on a stream can sometimes enable more aggressive optimizations in parallel streams.
7.  **Custom `ForkJoinPool`:** For advanced scenarios, or when dealing with blocking operations, consider creating your own `ForkJoinPool` to isolate your parallel stream tasks from the common pool.

By understanding these detailed concepts, common pitfalls, and best practices, you'll be well-equipped to discuss Java Streams and Lambda Expressions at an SDE-2 level in Big Tech interviews.