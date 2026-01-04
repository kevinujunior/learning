# Month 2: Advanced Java, Algorithms, and System Design Basics

## Week 5: Advanced Java Concepts

#### Topic 1: Exception Handling

*   **Interview Perspective:** Understand the exception hierarchy, checked vs. unchecked exceptions, and how to effectively use `try-catch-finally` blocks. Discuss custom exceptions and the importance of `throws` and `throw`.

*   **Code Snippet: Custom Exception and `try-catch-finally`**

    ```java
    class InsufficientFundsException extends Exception {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }

    public class ExceptionHandlingDemo {
        private static double balance = 1000;

        public static void withdraw(double amount) throws InsufficientFundsException {
            if (amount > balance) {
                throw new InsufficientFundsException("Cannot withdraw " + amount + ". Insufficient funds.");
            }
            balance -= amount;
            System.out.println("Withdrawn: " + amount + ", New Balance: " + balance);
        }

        public static void main(String[] args) {
            try {
                withdraw(500);
                withdraw(700); // This will throw an exception
            } catch (InsufficientFundsException e) {
                System.err.println("Error: " + e.getMessage());
            } finally {
                System.out.println("Transaction attempt completed. Current balance: " + balance);
            }
            System.out.println("Program continues...");
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain the Java Exception Hierarchy.
    2.  Differentiate between checked and unchecked exceptions. Give examples.
    3.  When would you use `try-with-resources`?
    4.  What is the purpose of the `throw` keyword vs. `throws` keyword?
    5.  Can a `finally` block execute even if an exception is not caught?

*   **Practice Questions:**
    1.  Write a program that demonstrates multiple `catch` blocks for different exception types.
    2.  Create a custom exception for invalid age and use it in a `Person` class.
    3.  Implement a file reader that handles `FileNotFoundException` and `IOException` using `try-with-resources`.

#### Topic 2: Multithreading and Concurrency

*   **Interview Perspective:** Crucial for backend roles. Understand `Thread` class, `Runnable` interface, thread lifecycle, synchronization (synchronized methods/blocks), `volatile` keyword, and common concurrency utilities (`ExecutorService`, `Callable`, `Future`).

*   **Code Snippet: Thread Creation and `synchronized` method**

    ```java
    class MyRunnable implements Runnable {
        private String threadName;

        public MyRunnable(String name) {
            this.threadName = name;
            System.out.println("Creating " + threadName);
        }

        @Override
        public void run() {
            System.out.println("Running " + threadName);
            try {
                for (int i = 4; i > 0; i--) {
                    System.out.println("Thread: " + threadName + ", " + i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                System.out.println("Thread " + threadName + " interrupted.");
            }
            System.out.println("Thread " + threadName + " exiting.");
        }
    }

    class Counter {
        private int count = 0;

        // Synchronized method to ensure thread safety
        public synchronized void increment() {
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented to: " + count);
        }

        public int getCount() {
            return count;
        }
    }

    public class MultithreadingDemo {
        public static void main(String[] args) throws InterruptedException {
            // Using Runnable
            MyRunnable runnable1 = new MyRunnable("Thread-1");
            Thread thread1 = new Thread(runnable1);
            thread1.start();

            // Using Thread class (less common, but good to know)
            Thread thread2 = new Thread(() -> {
                System.out.println("Running Thread-2 via Lambda");
            }, "Thread-2");
            thread2.start();

            // Demonstrating synchronization
            Counter counter = new Counter();
            Runnable counterTask = () -> {
                for (int i = 0; i < 5; i++) {
                    counter.increment();
                }
            };

            Thread t3 = new Thread(counterTask, "Counter-Thread-A");
            Thread t4 = new Thread(counterTask, "Counter-Thread-B");

            t3.start();
            t4.start();

            t3.join(); // Wait for t3 to finish
            t4.join(); // Wait for t4 to finish

            System.out.println("Final Counter value: " + counter.getCount());
        }
    }
    ```

*   **Interview Questions:**
    1.  What is a `Thread`? How do you create threads in Java?
    2.  Explain the thread lifecycle.
    3.  What is synchronization? Why is it needed?
    4.  Differentiate between `notify()`, `notifyAll()`, and `wait()`.
    5.  Explain `volatile` keyword. When should it be used?
    6.  What is a `Deadlock`? How can it be prevented?
    7.  What is an `ExecutorService`? What are its benefits?

*   **Practice Questions:**
    1.  Write a multithreaded program to find the sum of elements in an array.
    2.  Implement a producer-consumer problem using `wait()` and `notify()`.
    3.  Create a thread-safe `Singleton` class.
    4.  Demonstrate the use of `Callable` and `Future` for asynchronous tasks.

### Week 6: I/O, Serialization, and Reflection

#### Topic 1: Input/Output (I/O) Streams

*   **Interview Perspective:** Understand character vs. byte streams, buffered streams, and file handling. Differentiate between `InputStream`/`OutputStream` and `Reader`/`Writer`.

*   **Code Snippet: File I/O (Reading and Writing text)**

    ```java
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;

    public class FileIODemo {
        private static final String FILE_NAME = "sample.txt";

        public static void main(String[] args) {
            // Writing to a file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
                writer.write("Hello, Java I/O!");
                writer.newLine();
                writer.write("This is a test file.");
                System.out.println("Successfully wrote to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }

            // Reading from a file
            try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
                String line;
                System.out.println("\nReading from " + FILE_NAME + ":");
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                System.err.println("Error reading from file: " + e.getMessage());
            }
        }
    }
    ```

*   **Interview Questions:**
    1.  What are byte streams and character streams? When would you use each?
    2.  Explain the concept of buffering in I/O. What are its benefits?
    3.  How do you read/write objects to a file? (Hint: Serialization)
    4.  What is `try-with-resources` and why is it important for I/O?
    5.  What is the `NIO.2` API? What improvements does it offer?

*   **Practice Questions:**
    1.  Copy the content of one text file to another.
    2.  Read a binary file (e.g., an image) and write it to another file.
    3.  Count the number of words in a text file.

#### Topic 2: Serialization and Reflection API

*   **Interview Perspective:** Serialization is used for persistent storage and network transfer of objects. Reflection is for introspection and dynamic manipulation, often asked in framework contexts.

*   **Code Snippet: Serialization and Deserialization**

    ```java
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.ObjectInputStream;
    import java.io.ObjectOutputStream;
    import java.io.Serializable;

    class Person implements Serializable {
        private static final long serialVersionUID = 1L; // Recommended for serialization
        String name;
        int age;
        transient String password; // Will not be serialized

        public Person(String name, int age, String password) {
            this.name = name;
            this.age = age;
            this.password = password;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + ", password='" + password + '\'' + '}';
        }
    }

    public class SerializationDemo {
        private static final String FILE_NAME = "person.ser";

        public static void main(String[] args) {
            Person originalPerson = new Person("Alice", 30, "securePass");

            // Serialization
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(originalPerson);
                System.out.println("Person object serialized to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("Serialization error: " + e.getMessage());
            }

            // Deserialization
            Person deserializedPerson = null;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                deserializedPerson = (Person) ois.readObject();
                System.out.println("Person object deserialized from " + FILE_NAME);
                System.out.println("Original: " + originalPerson);
                System.out.println("Deserialized: " + deserializedPerson);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Deserialization error: " + e.getMessage());
            }

            // Demonstrating transient field (password will be null)
            if (deserializedPerson != null) {
                System.out.println("Deserialized Password: " + deserializedPerson.password); // Should be null
            }
        }
    }
    ```

*   **Interview Questions:**
    1.  What is Serialization in Java? Why is it used?
    2.  What is the `Serializable` interface? Does it have any methods?
    3.  Explain the `transient` keyword.
    4.  What is Reflection API? Give a use case.
    5.  How do you dynamically load a class at runtime using Reflection?

*   **Practice Questions:**
    1.  Serialize and deserialize a `HashMap` containing custom objects.
    2.  Use Reflection to inspect all methods of a given class.
    3.  Create an instance of a class using `Class.forName()` and `newInstance()`.

### Week 7: Generics and Functional Programming

#### Topic 1: Generics

*   **Interview Perspective:** Understand type safety, compile-time checks, and the concept of type erasure. Be familiar with wildcards (`?`), bounded types, and the PECS (Producer-Extends, Consumer-Super) principle.

*   **Code Snippet: Generics with Wildcards**

    ```java
    import java.util.ArrayList;
    import java.util.List;

    public class GenericsDemo {

        // Method that accepts a List of any type
        public static void printList(List<?> list) {
            for (Object o : list) {
                System.out.print(o + " ");
            }
            System.out.println();
        }

        // PECS: Producer-Extends (E.g., source of data)
        // This method can read from a list of Numbers or its subclasses (Integer, Double)
        public static void processNumbers(List<? extends Number> numbers) {
            for (Number num : numbers) {
                System.out.println("Processing number: " + num.doubleValue());
            }
            // numbers.add(new Integer(10)); // Compile-time error: cannot add to an <? extends T> list
        }

        // PECS: Consumer-Super (E.g., sink for data)
        // This method can add to a list of Integers or its superclasses (Number, Object)
        public static void addIntegers(List<? super Integer> list) {
            list.add(10);
            list.add(20);
            System.out.println("Added integers to list: " + list);
        }

        public static void main(String[] args) {
            List<String> stringList = new ArrayList<>();
            stringList.add("A");
            stringList.add("B");
            System.out.print("String List: ");
            printList(stringList);

            List<Integer> integerList = new ArrayList<>();
            integerList.add(1);
            integerList.add(2);
            System.out.print("Integer List: ");
            printList(integerList);

            List<Double> doubleList = new ArrayList<>();
            doubleList.add(10.5);
            doubleList.add(20.3);
            processNumbers(integerList); // Works
            processNumbers(doubleList);  // Works

            List<Number> numberListForAdding = new ArrayList<>();
            addIntegers(numberListForAdding); // Works for List<Number>
            List<Object> objectListForAdding = new ArrayList<>();
            addIntegers(objectListForAdding); // Works for List<Object>
            // addIntegers(integerList); // Works for List<Integer>
        }
    }
    ```

*   **Interview Questions:**
    1.  What are Generics in Java? What problem do they solve?
    2.  Explain type erasure in Generics.
    3.  What are wildcards (`?`) in Generics? Differentiate between `? extends T` and `? super T`. (PECS principle)
    4.  Can you use primitive types with Generics? Why or why not?
    5.  How do you create a generic method?

*   **Practice Questions:**
    1.  Create a generic `Stack` class that can hold any type of object.
    2.  Write a generic method to find the maximum element in a list of `Comparable` objects.
    3.  Implement a generic `Pair` class that holds two elements of potentially different types.

#### Topic 2: Functional Programming with Lambda Expressions and Stream API

*   **Interview Perspective:** Java 8 features are standard. Be able to write lambda expressions, use functional interfaces, and perform common Stream API operations (filter, map, reduce, collect, forEach).

*   **Code Snippet: Lambda and Stream API**

    ```java
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Comparator;
    import java.util.List;
    import java.util.Map;
    import java.util.Optional;
    import java.util.stream.Collectors;

    class PersonFP {
        String name;
        int age;

        public PersonFP(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }

    public class FunctionalProgrammingDemo {
        public static void main(String[] args) {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            // Lambda Expression for filtering even numbers
            List<Integer> evenNumbers = numbers.stream()
                                            .filter(n -> n % 2 == 0) // Lambda as Predicate
                                            .collect(Collectors.toList());
            System.out.println("Even Numbers: " + evenNumbers);

            // Stream API: Map, Reduce
            int sumOfSquaresOfEvenNumbers = numbers.stream()
                                                    .filter(n -> n % 2 == 0)
                                                    .map(n -> n * n) // Map: transform each element
                                                    .reduce(0, Integer::sum); // Reduce: combine elements
            System.out.println("Sum of squares of even numbers: " + sumOfSquaresOfEvenNumbers);

            List<PersonFP> people = Arrays.asList(
                new PersonFP("Alice", 30),
                new PersonFP("Bob", 25),
                new PersonFP("Charlie", 35),
                new PersonFP("David", 25)
            );

            // Filtering and collecting names of people aged > 28
            List<String> adultNames = people.stream()
                                            .filter(p -> p.getAge() > 28)
                                            .map(PersonFP::getName) // Method reference
                                            .collect(Collectors.toList());
            System.out.println("Adult Names: " + adultNames);

            // Grouping people by age
            Map<Integer, List<PersonFP>> peopleByAge = people.stream()
                                                            .collect(Collectors.groupingBy(PersonFP::getAge));
            System.out.println("People by Age: " + peopleByAge);

            // Finding the oldest person
            Optional<PersonFP> oldestPerson = people.stream()
                                                .max(Comparator.comparing(PersonFP::getAge));
            oldestPerson.ifPresent(p -> System.out.println("Oldest Person: " + p));
        }
    }
    ```

*   **Interview Questions:**
    1.  What are Lambda Expressions? How do they enable functional programming in Java?
    2.  What is a Functional Interface? Give examples.
    3.  Explain the Stream API. What are its advantages?
    4.  Differentiate between intermediate and terminal operations in Streams.
    5.  What is `Optional`? How does it help avoid `NullPointerExceptions`?
    6.  Explain `map()`, `filter()`, `reduce()`, and `collect()` in Stream API.

*   **Practice Questions:**
    1.  Given a list of strings, filter out those that start with 'A' and convert them to uppercase using Streams.
    2.  Calculate the average of a list of numbers using Stream API.
    3.  Find the sum of all odd numbers in a list using `reduce()`.
    4.  Group a list of `Product` objects by their category.

### Week 8: Data Structures & Algorithms (Sorting, Searching)

#### Topic 1: Sorting Algorithms

*   **Interview Perspective:** Know the common sorting algorithms (Bubble, Selection, Insertion, Merge, Quick, Heap) and their time/space complexities. Be prepared to explain the working of Merge Sort and Quick Sort in detail.

*   **Code Snippet: Merge Sort**

    ```java
    import java.util.Arrays;

    public class MergeSortDemo {

        public void mergeSort(int[] array) {
            if (array == null || array.length <= 1) {
                return;
            }
            int[] temp = new int[array.length];
            mergeSort(array, temp, 0, array.length - 1);
        }

        private void mergeSort(int[] array, int[] temp, int left, int right) {
            if (left < right) {
                int mid = (left + right) / 2;
                mergeSort(array, temp, left, mid);
                mergeSort(array, temp, mid + 1, right);
                merge(array, temp, left, mid, right);
            }
        }

        private void merge(int[] array, int[] temp, int left, int mid, int right) {
            System.arraycopy(array, left, temp, left, right - left + 1);

            int i = left;      // pointer for left half
            int j = mid + 1;   // pointer for right half
            int k = left;      // pointer for merged array

            while (i <= mid && j <= right) {
                if (temp[i] <= temp[j]) {
                    array[k] = temp[i];
                    i++;
                } else {
                    array[k] = temp[j];
                    j++;
                }
                k++;
            }

            // Copy remaining elements of left half, if any
            while (i <= mid) {
                array[k] = temp[i];
                i++;
                k++;
            }
            // No need to copy remaining elements of right half, as they are already in place
        }

        public static void main(String[] args) {
            int[] arr = {12, 11, 13, 5, 6, 7};
            System.out.println("Original array: " + Arrays.toString(arr));

            MergeSortDemo sorter = new MergeSortDemo();
            sorter.mergeSort(arr);

            System.out.println("Sorted array: " + Arrays.toString(arr)); // Output: [5, 6, 7, 11, 12, 13]
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain the working principle of Merge Sort and its time complexity.
    2.  Explain the working principle of Quick Sort and its average/worst-case time complexity.
    3.  Differentiate between stable and unstable sorting algorithms.
    4.  When would you choose Insertion Sort over Merge Sort?
    5.  What is the best-case, average-case, and worst-case time complexity of Bubble Sort?

*   **Practice Questions:**
    1.  Implement Bubble Sort from scratch.
    2.  Implement Quick Sort from scratch.
    3.  Given an array of integers, sort it using Heap Sort.
    4.  Sort an array of strings alphabetically.

#### Topic 2: Searching Algorithms

*   **Interview Perspective:** Linear and Binary Search are fundamental. Understand their preconditions and time complexities. Knowledge of advanced search structures like Hash Tables (covered in Collections) is also relevant.

*   **Code Snippet: Binary Search (Iterative)**

    ```java
    public class BinarySearchDemo {

        public int binarySearch(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;

            while (left <= right) {
                int mid = left + (right - left) / 2; // Avoids potential overflow

                if (arr[mid] == target) {
                    return mid; // Target found
                }
                if (arr[mid] < target) {
                    left = mid + 1; // Target is in the right half
                } else {
                    right = mid - 1; // Target is in the left half
                }
            }
            return -1; // Target not found
        }

        public static void main(String[] args) {
            int[] sortedArr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
            BinarySearchDemo searcher = new BinarySearchDemo();

            int target1 = 23;
            int index1 = searcher.binarySearch(sortedArr, target1);
            System.out.println("Element " + target1 + " found at index: " + index1); // Output: 5

            int target2 = 7;
            int index2 = searcher.binarySearch(sortedArr, target2);
            System.out.println("Element " + target2 + " found at index: " + index2); // Output: -1
        }
    }
    ```

*   **Interview Questions:**
    1.  Differentiate between Linear Search and Binary Search. What are their time complexities?
    2.  What are the preconditions for Binary Search?
    3.  How does binary search work on an array with duplicate elements?
    4.  Explain interpolation search. When is it more efficient than binary search?

*   **Practice Questions:**
    1.  Implement Linear Search for an array of strings.
    2.  Implement Binary Search recursively.
    3.  Find the first and last occurrence of an element in a sorted array.
    4.  Search for an element in a rotated sorted array.

### Week 9: Graphs and Dynamic Programming Basics

#### Topic 1: Graph Algorithms

*   **Interview Perspective:** Understand graph representations (adjacency matrix, adjacency list). Be able to explain and trace BFS and DFS. Basic concepts of Dijkstra's and Floyd-Warshall are good to know.

*   **Code Snippet: Graph Representation (Adjacency List) and DFS**

    ```java
    import java.util.ArrayList;
    import java.util.LinkedList;
    import java.util.List;
    import java.util.Queue;

    class Graph {
        private int V; // Number of vertices
        private List<List<Integer>> adj; // Adjacency list

        public Graph(int V) {
            this.V = V;
            adj = new ArrayList<>(V);
            for (int i = 0; i < V; ++i) {
                adj.add(new ArrayList<>());
            }
        }

        // Function to add an edge into the graph
        public void addEdge(int v, int w) {
            adj.get(v).add(w);
            // For an undirected graph, add: adj.get(w).add(v);
        }

        // DFS traversal
        public void DFS(int v) {
            boolean[] visited = new boolean[V];
            DFSUtil(v, visited);
        }

        private void DFSUtil(int v, boolean[] visited) {
            visited[v] = true;
            System.out.print(v + " ");

            for (int neighbor : adj.get(v)) {
                if (!visited[neighbor]) {
                    DFSUtil(neighbor, visited);
                }
            }
        }

        // BFS traversal
        public void BFS(int s) {
            boolean[] visited = new boolean[V];
            Queue<Integer> queue = new LinkedList<>();

            visited[s] = true;
            queue.add(s);

            while (queue.size() != 0) {
                s = queue.poll();
                System.out.print(s + " ");

                for (int neighbor : adj.get(s)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }
        }
    }

    public class GraphDemo {
        public static void main(String[] args) {
            Graph g = new Graph(4); // 4 vertices (0 to 3)

            g.addEdge(0, 1);
            g.addEdge(0, 2);
            g.addEdge(1, 2);
            g.addEdge(2, 0);
            g.addEdge(2, 3);
            g.addEdge(3, 3);

            System.out.println("DFS starting from vertex 2:");
            g.DFS(2); // Output: 2 0 1 3 (order may vary slightly depending on adjacency list implementation)
            System.out.println("\nBFS starting from vertex 2:");
            g.BFS(2); // Output: 2 0 1 3
        }
    }
    ```

*   **Interview Questions:**
    1.  How can graphs be represented in memory? Compare adjacency matrix vs. adjacency list.
    2.  Explain Breadth-First Search (BFS) and Depth-First Search (DFS). What are their time complexities?
    3.  When would you use BFS over DFS, and vice versa?
    4.  What is a topological sort? Give an example.
    5.  Briefly explain Dijkstra's algorithm.

*   **Practice Questions:**
    1.  Implement BFS for a graph.
    2.  Find if a path exists between two nodes in a graph.
    3.  Count the number of connected components in an undirected graph.
    4.  Detect a cycle in an undirected graph.

#### Topic 2: Introduction to Dynamic Programming

*   **Interview Perspective:** Recognize problems that can be solved with DP. Understand the concepts of overlapping subproblems and optimal substructure. Start with classic problems like Fibonacci, Knapsack, Longest Common Subsequence.

*   **Code Snippet: Fibonacci Sequence using Dynamic Programming (Memoization)**

    ```java
    import java.util.HashMap;
    import java.util.Map;

    public class FibonacciDP {

        // Using Memoization (Top-down DP)
        private Map<Integer, Long> memo = new HashMap<>();

        public long fibMemo(int n) {
            if (n <= 1) {
                return n;
            }
            if (memo.containsKey(n)) {
                return memo.get(n);
            }

            long result = fibMemo(n - 1) + fibMemo(n - 2);
            memo.put(n, result);
            return result;
        }

        // Using Tabulation (Bottom-up DP)
        public long fibTab(int n) {
            if (n <= 1) {
                return n;
            }
            long[] dp = new long[n + 1];
            dp[0] = 0;
            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }

        public static void main(String[] args) {
            FibonacciDP fibCalculator = new FibonacciDP();
            int n = 10;
            System.out.println("Fibonacci (" + n + ") using Memoization: " + fibCalculator.fibMemo(n)); // Output: 55

            System.out.println("Fibonacci (" + n + ") using Tabulation: " + fibCalculator.fibTab(n)); // Output: 55
        }
    }
    ```

*   **Interview Questions:**
    1.  What is Dynamic Programming? What are its two key characteristics?
    2.  Differentiate between memoization (top-down DP) and tabulation (bottom-up DP).
    3.  Explain the concept of optimal substructure with an example.
    4.  How does DP improve upon brute-force recursion?

*   **Practice Questions:**
    1.  Implement the `climbing stairs` problem using DP.
    2.  Solve the `Coin Change` problem (minimum number of coins).
    3.  Find the `Longest Increasing Subsequence` in an array.
    4.  Implement the `0/1 Knapsack` problem using DP.

### Week 10: System Design Fundamentals

#### Topic 1: High-Level System Design Concepts

*   **Interview Perspective:** This is an introductory week for system design. Focus on understanding common architectural patterns and components. You won't be expected to design complex systems yet, but know the vocabulary.

*   **Key Concepts:**
    *   **Scalability:** Vertical vs. Horizontal scaling.
    *   **Availability:** Redundancy, fault tolerance.
    *   **Reliability:** Handling failures gracefully.
    *   **Performance:** Latency, throughput.
    *   **Load Balancing:** Round Robin, Least Connections, IP Hash.
    *   **Caching:** When and where to use it (client-side, CDN, server-side, database).
    *   **Databases:** SQL vs. NoSQL (pros and cons), Sharding, Replication.
    *   **APIs:** REST principles.
    *   **Message Queues:** Asynchronous processing, decoupling services.

*   **Interview Questions:**
    1.  What is the difference between vertical and horizontal scaling?
    2.  Explain the purpose of a Load Balancer.
    3.  When would you use a SQL database vs. a NoSQL database?
    4.  What are the benefits of caching? Where can caching be implemented?
    5.  What are the key principles of `RESTful APIs`?

*   **Practice Questions:**
    1.  Design a URL shortening service (high-level components).
    2.  Explain how you would handle traffic spikes for a web application.
    3.  Describe how a typical web request flows from browser to server.

---

This completes Month 2. Continue to solidify your Java knowledge and practice algorithms. Start familiarizing yourself with system design concepts; it's a marathon, not a sprint.