# Month 2 Solutions: Advanced Java, Algorithms, and System Design Basics

## Week 5: Advanced Java Concepts

### Topic 1: Exception Handling

#### Interview Questions:

1.  **Explain the Java Exception Hierarchy.**
    *   The Java Exception Hierarchy starts with `Throwable` as the base class.
    *   `Throwable` has two direct subclasses: `Error` and `Exception`.
    *   **`Error`**: Represents serious problems that an application should not try to catch (e.g., `OutOfMemoryError`, `StackOverflowError`). These are typically unrecoverable.
    *   **`Exception`**: Represents conditions that an application might want to catch.
        *   **Checked Exceptions**: Subclasses of `Exception` (but not `RuntimeException`). They must be declared in a method's `throws` clause or handled within a `try-catch` block (e.g., `IOException`, `SQLException`). The compiler enforces their handling.
        *   **Unchecked Exceptions (Runtime Exceptions)**: Subclasses of `RuntimeException`. They do not need to be explicitly caught or declared. They usually indicate programming errors (e.g., `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`).

2.  **Differentiate between checked and unchecked exceptions. Give examples.**
    *   **Checked Exceptions**:
        *   **Compiler-enforced**: The compiler forces you to either handle them (using `try-catch`) or declare them (using `throws`) in the method signature.
        *   **Recoverable**: Usually represent anticipated problems that a robust application should be prepared to deal with.
        *   **Examples**: `IOException`, `SQLException`, `FileNotFoundException`.
    *   **Unchecked Exceptions (Runtime Exceptions)**:
        *   **Not compiler-enforced**: The compiler does not require explicit handling or declaration.
        *   **Unrecoverable (typically)**: Usually indicate defects in the code logic that should be fixed rather than caught (e.g., trying to access an array out of bounds).
        *   **Examples**: `NullPointerException`, `ArrayIndexOutOfBoundsException`, `ArithmeticException`, `IllegalArgumentException`.

3.  **When would you use `try-with-resources`?**
    *   `try-with-resources` is used when working with resources that implement the `AutoCloseable` interface (e.g., `FileInputStream`, `FileOutputStream`, `BufferedReader`, `Connection`, `Statement`).
    *   **Purpose**: Ensures that resources are automatically closed when the `try` block exits, whether normally or due to an exception. This prevents resource leaks and simplifies code compared to using `finally` blocks for closing resources.

4.  **What is the purpose of the `throw` keyword vs. `throws` keyword?**
    *   **`throw`**:
        *   Used **inside** a method or block of code.
        *   Used to **explicitly throw** an instance of an exception (e.g., `throw new IOException("File not found");`).
        *   It signifies that an exceptional event has occurred at that point in the code.
    *   **`throws`**:
        *   Used in a method **signature**.
        *   Used to **declare** that a method *might throw* one or more checked exceptions. It informs callers that they must handle these potential exceptions (e.g., `public void readFile() throws IOException`).

5.  **Can a `finally` block execute even if an exception is not caught?**
    *   Yes, a `finally` block will **always execute** regardless of whether an exception is caught or not, as long as the `try` block is entered.
    *   It is guaranteed to execute even if:
        *   No exception occurs.
        *   An exception occurs and is caught by a `catch` block.
        *   An exception occurs and is *not* caught by any `catch` block (in which case the exception will propagate up the call stack after `finally` executes).
    *   The only scenarios where `finally` might not execute are if the JVM exits (e.g., `System.exit()`) or if the thread executing the `try` block is killed.

#### Practice Questions:

1.  **Write a program that demonstrates multiple `catch` blocks for different exception types.**
    ```java
    public class MultipleCatchDemo {
        public static void main(String[] args) {
            try {
                String str = null;
                System.out.println(str.length()); // May throw NullPointerException

                int[] arr = new int[5];
                System.out.println(arr[10]);     // May throw ArrayIndexOutOfBoundsException

                int result = 10 / 0;             // May throw ArithmeticException
                System.out.println(result);

            } catch (NullPointerException e) {
                System.err.println("Caught NullPointerException: " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Caught ArrayIndexOutOfBoundsException: " + e.getMessage());
            } catch (ArithmeticException e) {
                System.err.println("Caught ArithmeticException: " + e.getMessage());
            } catch (Exception e) { // Generic catch block for any other exceptions
                System.err.println("Caught a general Exception: " + e.getMessage());
            } finally {
                System.out.println("Execution of try-catch blocks finished.");
            }
        }
    }
    ```

2.  **Create a custom exception for invalid age and use it in a `Person` class.**
    ```java
    class InvalidAgeException extends Exception {
        public InvalidAgeException(String message) {
            super(message);
        }
    }

    class Person {
        private String name;
        private int age;

        public Person(String name, int age) throws InvalidAgeException {
            if (age < 0 || age > 150) {
                throw new InvalidAgeException("Age " + age + " is invalid. Must be between 0 and 150.");
            }
            this.name = name;
            this.age = age;
            System.out.println(name + " created with age " + age);
        }

        public String getName() { return name; }
        public int getAge() { return age; }
    }

    public class CustomExceptionDemo {
        public static void main(String[] args) {
            try {
                Person p1 = new Person("Alice", 30);
                Person p2 = new Person("Bob", -5); // This will throw InvalidAgeException
                Person p3 = new Person("Charlie", 200); // This will also throw InvalidAgeException
            } catch (InvalidAgeException e) {
                System.err.println("Error creating person: " + e.getMessage());
            }
        }
    }
    ```

3.  **Implement a file reader that handles `FileNotFoundException` and `IOException` using `try-with-resources`.**
    ```java
    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.io.FileNotFoundException;

    public class FileReaderWithResources {

        public static void readFileContent(String filePath) {
            System.out.println("\nAttempting to read file: " + filePath);
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println("Successfully read file: " + filePath);
            } catch (FileNotFoundException e) {
                System.err.println("Error: File not found at " + filePath);
            } catch (IOException e) {
                System.err.println("Error reading file content from " + filePath + ": " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            // Create a dummy file for testing
            try {
                java.nio.file.Files.write(java.nio.file.Paths.get("test.txt"), "Line 1\nLine 2\nLine 3".getBytes());
            } catch (IOException e) {
                System.err.println("Could not create test.txt: " + e.getMessage());
            }

            readFileContent("test.txt"); // This should succeed
            readFileContent("nonexistent.txt"); // This should fail with FileNotFoundException
            // To simulate generic IOException, we'd need more complex scenarios like
            // trying to read a corrupt file, or a file with permissions issues, etc.
        }
    }
    ```

### Topic 2: Multithreading and Concurrency

#### Interview Questions:

1.  **What is a `Thread`? How do you create threads in Java?**
    *   A `Thread` is the smallest unit of execution in a program. It's an independent path of execution within a process. Multiple threads can run concurrently within the same program, sharing the same memory space but having their own call stack.
    *   **Ways to create threads:**
        1.  **Extending the `Thread` class**:
            ```java
            class MyThread extends Thread {
                public void run() { /* thread logic */ }
            }
            new MyThread().start();
            ```
            *   **Drawback**: Java does not support multiple inheritance, so if your class extends `Thread`, it cannot extend any other class.
        2.  **Implementing the `Runnable` interface**:
            ```java
            class MyRunnable implements Runnable {
                public void run() { /* thread logic */ }
            }
            Thread t = new Thread(new MyRunnable());
            t.start();
            ```
            *   **Advantage**: This is the preferred way as it allows your class to extend other classes and promotes separation of concerns (task vs. thread management).
        3.  **Using `ExecutorService` with `Callable` and `Future` (for managing thread pools and getting results)**. This is generally preferred in modern Java for managing threads efficiently.
            ```java
            import java.util.concurrent.*;
            ExecutorService executor = Executors.newFixedThreadPool(10);
            Future<Integer> future = executor.submit(() -> { /* return result */ return 1; });
            executor.shutdown();
            ```

2.  **Explain the thread lifecycle.**
    *   A thread goes through several states from its creation to its termination:
        1.  **NEW**: The thread has been created (`new Thread()`) but has not yet started execution (`start()` method not invoked).
        2.  **RUNNABLE**: The thread is either currently executing or is ready to execute and waiting for CPU time from the scheduler. (This state combines "Ready" and "Running").
        3.  **BLOCKED**: The thread is waiting to acquire a monitor lock to enter a `synchronized` block or method.
        4.  **WAITING**: The thread is waiting indefinitely for another thread to perform a particular action (e.g., `Object.wait()`, `Thread.join()`, `LockSupport.park()`). It will not return to the RUNNABLE state until it receives a specific notification.
        5.  **TIMED_WAITING**: The thread is waiting for another thread to perform an action for a specified waiting time (e.g., `Thread.sleep(long millis)`, `Object.wait(long millis)`, `LockSupport.parkNanos(long nanos)`, `LockSupport.parkUntil(long deadline)`). After the timeout or notification, it can return to RUNNABLE.
        6.  **TERMINATED**: The thread has completed its execution or has been abnormally terminated.

3.  **What is synchronization? Why is it needed?**
    *   **Synchronization** is the mechanism used to control the access of multiple threads to shared resources. It ensures that only one thread can access a critical section (a shared resource) at any given time, preventing data corruption and consistency issues.
    *   **Why it's needed**: In multithreaded environments, if multiple threads concurrently modify shared data without proper synchronization, it can lead to:
        *   **Race Conditions**: Where the outcome of the program depends on the unpredictable sequence or timing of events.
        *   **Inconsistent Data**: Shared variables might end up with incorrect values.
        *   **Visibility Issues**: Changes made by one thread might not be immediately visible to other threads.
    *   Java provides `synchronized` keyword (for methods and blocks), `ReentrantLock`, `Semaphore`, and other concurrency utilities to achieve synchronization.

4.  **Differentiate between `notify()`, `notifyAll()`, and `wait()`.**
    *   These methods are part of the `Object` class and are used for inter-thread communication. They *must* be called from within a `synchronized` block/method.
    *   **`wait()`**:
        *   Causes the current thread to release the lock it holds on the object and go into a `WAITING` state.
        *   It waits until another thread invokes `notify()` or `notifyAll()` on the same object, or a specified amount of time passes (for `wait(long timeout)`).
        *   Used to pause a thread until a certain condition becomes true.
    *   **`notify()`**:
        *   Wakes up **a single** thread that is waiting on this object's monitor.
        *   Which thread is chosen is arbitrary and depends on the JVM's scheduler.
        *   The awakened thread will then attempt to re-acquire the object's lock.
    *   **`notifyAll()`**:
        *   Wakes up **all** threads that are waiting on this object's monitor.
        *   All awakened threads will compete to re-acquire the object's lock.
        *   Generally safer than `notify()` as it prevents "lost wake-ups" where the wrong thread is notified.

5.  **Explain `volatile` keyword. When should it be used?**
    *   The `volatile` keyword in Java guarantees two things regarding a variable:
        1.  **Visibility**: Ensures that changes to the `volatile` variable are immediately written back to main memory and are always read from main memory. This means all threads will see the most up-to-date value.
        2.  **Ordering**: Prevents the compiler and CPU from reordering memory operations involving the `volatile` variable with respect to other memory operations, guaranteeing a happens-before relationship.
    *   **When to use it**:
        *   When a variable's value can be modified by one thread and read by multiple other threads, and you need to ensure that all threads see the most recent write.
        *   It's typically used for status flags (e.g., `boolean shutdownRequested = false;`) or when a shared variable doesn't participate in invariants with other variables (i.e., its value can be updated independently).
        *   `volatile` does **not** provide atomicity for compound operations (e.g., `i++`). For atomic operations, use `synchronized` or `java.util.concurrent.atomic` classes.

6.  **What is a `Deadlock`? How can it be prevented?**
    *   **Deadlock**: A situation in multithreading where two or more threads are blocked indefinitely, each waiting for the other to release a resource that it needs. This results in a standstill, and none of the threads can proceed.
        *   **Example**: Thread A holds Lock X and wants Lock Y. Thread B holds Lock Y and wants Lock X. Both are stuck.
    *   **Conditions for Deadlock (Coffman Conditions)**: For a deadlock to occur, all four conditions must be met:
        1.  **Mutual Exclusion**: At least one resource must be held in a non-sharable mode.
        2.  **Hold and Wait**: A thread holding at least one resource is waiting to acquire additional resources held by other threads.
        3.  **No Preemption**: Resources cannot be preempted; they can only be released voluntarily by the thread holding them.
        4.  **Circular Wait**: A set of threads `T1, T2, ..., Tn` exists such that `T1` is waiting for a resource held by `T2`, `T2` is waiting for a resource held by `T3`, and so on, with `Tn` waiting for a resource held by `T1`.
    *   **Prevention Strategies**: To prevent deadlock, you need to break at least one of these four conditions:
        1.  **Avoid Mutual Exclusion (if possible)**: Use lock-free data structures or optimistic locking where feasible. (Often not possible for shared mutable resources).
        2.  **Avoid Hold and Wait**:
            *   Acquire all required locks at once.
            *   If a thread cannot acquire all locks, it releases any locks it holds and tries again later.
        3.  **Allow Preemption**: Allow the system to take away a resource from a thread holding it (difficult to implement safely in practice).
        4.  **Break Circular Wait (Most Common Strategy)**:
            *   Establish a global ordering of locks. All threads must acquire locks in the same predefined order.
            *   If Thread A needs Lock X and Lock Y, and X comes before Y in the global order, A should always try to acquire X first, then Y.

7.  **What is an `ExecutorService`? What are its benefits?**
    *   An `ExecutorService` is a higher-level API in `java.util.concurrent` for managing and controlling threads. It separates task submission from task execution. Instead of creating and managing raw `Thread` objects directly, you submit `Runnable` or `Callable` tasks to an `ExecutorService`, and it handles thread creation, pooling, and scheduling.
    *   **Benefits**:
        1.  **Thread Pool Management**: Automatically manages a pool of worker threads, reducing the overhead of creating/destroying threads for each task.
        2.  **Resource Management**: Prevents resource exhaustion by limiting the number of concurrently running threads.
        3.  **Asynchronous Execution**: Facilitates running tasks asynchronously.
        4.  **Task Management**: Provides methods for submitting tasks (`execute()`, `submit()`), shutting down the service (`shutdown()`, `shutdownNow()`), and waiting for task completion (`awaitTermination()`).
        5.  **Result Retrieval**: With `Callable` and `Future`, it allows tasks to return results and throw exceptions, which can be retrieved later.
        6.  **Decoupling**: Decouples task submission from how the tasks are run, making the code more flexible and maintainable.

#### Practice Questions:

1.  **Write a multithreaded program to find the sum of elements in an array.**
    ```java
    import java.util.concurrent.*;
    import java.util.ArrayList;
    import java.util.List;

    class ArraySumTask implements Callable<Long> {
        private int[] array;
        private int start;
        private int end;

        public ArraySumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        public Long call() {
            long sum = 0;
            for (int i = start; i <= end; i++) {
                sum += array[i];
            }
            // System.out.println(Thread.currentThread().getName() + " summing from " + start + " to " + end + " = " + sum);
            return sum;
        }
    }

    public class MultithreadedArraySum {
        public static void main(String[] args) throws InterruptedException, ExecutionException {
            int[] largeArray = new int[10000];
            for (int i = 0; i < largeArray.length; i++) {
                largeArray[i] = i + 1; // Fill with 1 to 10000
            }

            int numThreads = 4;
            ExecutorService executor = Executors.newFixedThreadPool(numThreads);
            List<Future<Long>> futures = new ArrayList<>();

            int chunkSize = largeArray.length / numThreads;

            for (int i = 0; i < numThreads; i++) {
                int start = i * chunkSize;
                int end = (i == numThreads - 1) ? largeArray.length - 1 : (start + chunkSize - 1);
                ArraySumTask task = new ArraySumTask(largeArray, start, end);
                futures.add(executor.submit(task));
            }

            long totalSum = 0;
            for (Future<Long> future : futures) {
                totalSum += future.get(); // get() blocks until the task completes
            }

            executor.shutdown();
            System.out.println("Total sum of array elements: " + totalSum);

            // Verify with single-threaded sum
            long singleThreadSum = 0;
            for (int val : largeArray) {
                singleThreadSum += val;
            }
            System.out.println("Single-threaded sum (for verification): " + singleThreadSum);
        }
    }
    ```

2.  **Implement a producer-consumer problem using `wait()` and `notify()`.**
    ```java
    import java.util.LinkedList;
    import java.util.Queue;

    class Buffer {
        private Queue<Integer> queue;
        private int capacity;

        public Buffer(int capacity) {
            this.queue = new LinkedList<>();
            this.capacity = capacity;
        }

        public void produce(int item) throws InterruptedException {
            synchronized (this) {
                while (queue.size() == capacity) {
                    System.out.println("Buffer is full. Producer " + Thread.currentThread().getName() + " waits.");
                    wait(); // Producer waits if buffer is full
                }
                queue.add(item);
                System.out.println("Producer " + Thread.currentThread().getName() + " produced: " + item + ". Current size: " + queue.size());
                notifyAll(); // Notify consumer(s) that items are available
                Thread.sleep(100); // Simulate work
            }
        }

        public int consume() throws InterruptedException {
            synchronized (this) {
                while (queue.isEmpty()) {
                    System.out.println("Buffer is empty. Consumer " + Thread.currentThread().getName() + " waits.");
                    wait(); // Consumer waits if buffer is empty
                }
                int item = queue.poll();
                System.out.println("Consumer " + Thread.currentThread().getName() + " consumed: " + item + ". Current size: " + queue.size());
                notifyAll(); // Notify producer(s) that space is available
                Thread.sleep(150); // Simulate work
                return item;
            }
        }
    }

    class Producer implements Runnable {
        private Buffer buffer;
        private int itemsToProduce;

        public Producer(Buffer buffer, int itemsToProduce) {
            this.buffer = buffer;
            this.itemsToProduce = itemsToProduce;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < itemsToProduce; i++) {
                    buffer.produce(i + 1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    class Consumer implements Runnable {
        private Buffer buffer;
        private int itemsToConsume;

        public Consumer(Buffer buffer, int itemsToConsume) {
            this.buffer = buffer;
            this.itemsToConsume = itemsToConsume;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < itemsToConsume; i++) {
                    buffer.consume();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public class ProducerConsumerDemo {
        public static void main(String[] args) throws InterruptedException {
            Buffer buffer = new Buffer(5); // Buffer capacity of 5

            Thread producerThread1 = new Thread(new Producer(buffer, 7), "PRODUCER-1");
            Thread producerThread2 = new Thread(new Producer(buffer, 3), "PRODUCER-2");
            Thread consumerThread1 = new Thread(new Consumer(buffer, 5), "CONSUMER-1");
            Thread consumerThread2 = new Thread(new Consumer(buffer, 5), "CONSUMER-2");

            producerThread1.start();
            producerThread2.start();
            consumerThread1.start();
            consumerThread2.start();

            // Wait for all threads to complete
            producerThread1.join();
            producerThread2.join();
            consumerThread1.join();
            consumerThread2.join();

            System.out.println("All producers and consumers finished.");
        }
    }
    ```

3.  **Create a thread-safe `Singleton` class.**
    ```java
    public class ThreadSafeSingleton {

        // 1. Eager initialization (simplest, always thread-safe)
        // private static final ThreadSafeSingleton INSTANCE = new ThreadSafeSingleton();
        // private ThreadSafeSingleton() {}
        // public static ThreadSafeSingleton getInstance() { return INSTANCE; }

        // 2. Lazy initialization with double-checked locking (DCL)
        // Volatile keyword is crucial for DCL to work correctly.
        // It ensures that changes to the INSTANCE variable are visible across threads
        // and prevents partial initialization problems due to instruction reordering.
        private static volatile ThreadSafeSingleton instance;

        private ThreadSafeSingleton() {
            // Prevent instantiation via reflection
            if (instance != null) {
                throw new IllegalStateException("Singleton already initialized.");
            }
            System.out.println(Thread.currentThread().getName() + ": Creating ThreadSafeSingleton instance.");
        }

        public static ThreadSafeSingleton getInstance() {
            if (instance == null) { // First check: no need to synchronize if instance already exists
                synchronized (ThreadSafeSingleton.class) { // Synchronize on the class object
                    if (instance == null) { // Second check: instance might have been created by another thread
                        instance = new ThreadSafeSingleton();
                    }
                }
            }
            return instance;
        }

        // To demonstrate thread safety
        public void showMessage() {
            System.out.println(Thread.currentThread().getName() + ": Hello from Singleton!");
        }

        public static void main(String[] args) {
            // Create multiple threads to access the singleton
            Thread t1 = new Thread(() -> {
                ThreadSafeSingleton s1 = ThreadSafeSingleton.getInstance();
                s1.showMessage();
            }, "Thread-A");

            Thread t2 = new Thread(() -> {
                ThreadSafeSingleton s2 = ThreadSafeSingleton.getInstance();
                s2.showMessage();
            }, "Thread-B");

            Thread t3 = new Thread(() -> {
                ThreadSafeSingleton s3 = ThreadSafeSingleton.getInstance();
                s3.showMessage();
            }, "Thread-C");

            t1.start();
            t2.start();
            t3.start();

            // Expected output: "Creating ThreadSafeSingleton instance." should appear only once.
        }
    }
    ```

4.  **Demonstrate the use of `Callable` and `Future` for asynchronous tasks.**
    ```java
    import java.util.concurrent.*;
    import java.util.Random;

    class FactorialTask implements Callable<Long> {
        private int number;

        public FactorialTask(int number) {
            this.number = number;
        }

        @Override
        public Long call() throws Exception {
            if (number < 0) {
                throw new IllegalArgumentException("Number must be non-negative.");
            }
            if (number == 0) {
                return 1L;
            }
            long result = 1;
            for (int i = 1; i <= number; i++) {
                result *= i;
                Thread.sleep(100); // Simulate some work
            }
            System.out.println(Thread.currentThread().getName() + ": Calculated factorial of " + number + ".");
            return result;
        }
    }

    public class CallableFutureDemo {
        public static void main(String[] args) throws InterruptedException, ExecutionException {
            ExecutorService executor = Executors.newFixedThreadPool(2); // A pool with 2 threads

            System.out.println("Submitting tasks...");

            // Submit a task that returns a result
            Future<Long> future1 = executor.submit(new FactorialTask(5));
            Future<Long> future2 = executor.submit(new FactorialTask(10));
            Future<Long> future3 = executor.submit(new FactorialTask(7));
            Future<Long> future4 = executor.submit(new FactorialTask(-3)); // This will throw an exception

            System.out.println("Tasks submitted. Doing other work while tasks run...");
            Thread.sleep(500); // Simulate main thread doing other work

            try {
                System.out.println("\nRetrieving results:");
                // get() method blocks until the task is complete
                System.out.println("Factorial of 5: " + future1.get());
                System.out.println("Factorial of 10: " + future2.get());
                System.out.println("Factorial of 7: " + future3.get(1, TimeUnit.SECONDS)); // get with timeout

                // This will throw ExecutionException because the task threw an IllegalArgumentException
                System.out.println("Factorial of -3: " + future4.get());
            } catch (TimeoutException e) {
                System.err.println("Timeout occurred while waiting for result: " + e.getMessage());
            } catch (ExecutionException e) {
                // The actual exception thrown by the Callable is wrapped in an ExecutionException
                System.err.println("Task failed: " + e.getCause().getMessage());
            } finally {
                executor.shutdown(); // Initiates an orderly shutdown
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    System.err.println("Executor did not terminate in time. Forcing shutdown.");
                    executor.shutdownNow(); // Attempt to stop all actively executing tasks
                }
                System.out.println("\nExecutorService shut down.");
            }
        }
    }
    ```

## Week 6: I/O, Serialization, and Reflection

### Topic 1: Input/Output (I/O) Streams

#### Interview Questions:

1.  **What are byte streams and character streams? When would you use each?**
    *   **Byte Streams**:
        *   Handle raw bytes (8-bit bytes).
        *   Used for processing binary data (e.g., images, audio, video, serialized objects) or when character encoding is not a concern.
        *   Classes typically end with `InputStream` or `OutputStream` (e.g., `FileInputStream`, `FileOutputStream`, `BufferedInputStream`).
    *   **Character Streams**:
        *   Handle 16-bit Unicode characters.
        *   Used for processing text data (e.g., `.txt` files, XML, JSON). They abstract away character encoding complexities.
        *   Classes typically end with `Reader` or `Writer` (e.g., `FileReader`, `FileWriter`, `BufferedReader`, `BufferedWriter`).
    *   **When to use**:
        *   Use **Byte Streams** for binary data or when you need fine-grained control over byte-level operations.
        *   Use **Character Streams** for all text-based data, as they correctly handle character encodings and are generally more efficient for text.

2.  **Explain the concept of buffering in I/O. What are its benefits?**
    *   **Buffering** in I/O involves using a temporary storage area (a buffer) in memory to hold data before it's actually read from or written to a physical device (like a disk or network).
    *   Instead of reading/writing one byte/character at a time, data is accumulated in the buffer and then transferred in larger chunks.
    *   **Benefits**:
        1.  **Performance Improvement**: Disk and network operations are slow. Reading/writing in larger blocks reduces the number of expensive physical I/O operations, significantly improving performance.
        2.  **Reduced System Calls**: Each read/write operation usually involves a system call. Buffering reduces the frequency of these calls.
        3.  **Efficiency**: It's more efficient to handle data in contiguous blocks in memory than to constantly interact with slower external devices.
    *   **Java Classes**: `BufferedInputStream`, `BufferedOutputStream`, `BufferedReader`, `BufferedWriter`.

3.  **How do you read/write objects to a file? (Hint: Serialization)**
    *   To read/write objects to a file, you use **Java Serialization**.
    *   **Writing an object (Serialization)**:
        1.  The class of the object must implement the `java.io.Serializable` interface (it's a marker interface with no methods).
        2.  Use an `ObjectOutputStream` to write the object. `ObjectOutputStream` can be chained with a `FileOutputStream`.
        ```java
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("object.ser"))) {
            oos.writeObject(myObject);
        }
        ```
    *   **Reading an object (Deserialization)**:
        1.  Use an `ObjectInputStream` to read the object. `ObjectInputStream` can be chained with a `FileInputStream`.
        2.  The read object needs to be cast back to its original type.
        ```java
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("object.ser"))) {
            MyClass myObject = (MyClass) ois.readObject();
        }
        ```

4.  **What is `try-with-resources` and why is it important for I/O?**
    *   `try-with-resources` is a statement introduced in Java 7 that declares one or more resources. A resource is an object that must be closed after the program is finished with it.
    *   **Mechanism**: Any object declared in the `try` clause must implement the `java.lang.AutoCloseable` interface. When the `try` block completes (either normally or via an exception), the `close()` method of these resources is automatically invoked.
    *   **Importance for I/O**:
        1.  **Guaranteed Resource Closure**: Ensures that I/O streams (files, network connections, etc.) are always closed, even if exceptions occur. This is crucial for preventing resource leaks, which can lead to system instability, out-of-memory errors, and file corruption.
        2.  **Cleaner Code**: Eliminates the need for verbose `finally` blocks solely for closing resources, making code more readable and less error-prone.
        3.  **Improved Exception Handling**: If an exception occurs during resource closure (in addition to an exception in the `try` block), `try-with-resources` correctly handles it by suppressing the closure exception and propagating the original exception, while still allowing access to the suppressed exception.

5.  **What is the `NIO.2` API? What improvements does it offer?**
    *   `NIO.2` (New Input/Output API, version 2), introduced in Java 7, provides a comprehensive and improved API for file system and file I/O operations, residing primarily in the `java.nio.file` package.
    *   **Improvements it offers**:
        1.  **Path-based File System Access**: Introduced the `Path` interface and `Paths` utility class for more flexible and platform-independent file system paths, replacing the older `java.io.File`.
        2.  **Symbolic Link Support**: Explicit support for symbolic links.
        3.  **Atomic Operations**: Support for atomic file operations (e.g., moving or copying a file atomically).
        4.  **Directory Watching (WatchService)**: Provides a mechanism to monitor changes to directories (creation, deletion, modification of files/directories).
        5.  **File Attributes**: Richer API for accessing and manipulating file attributes (permissions, owner, size, timestamps) in a more consistent way across different file systems.
        6.  **Convenience Methods**: New utility methods in `Files` class for common file operations (copy, move, delete, read/write all bytes/lines, walk file tree).
        7.  **Asynchronous I/O**: While not exclusively `NIO.2`, it complements it with channels for asynchronous file and network I/O operations, allowing non-blocking I/O.

#### Practice Questions:

1.  **Copy the content of one text file to another.**
    ```java
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;

    public class FileCopier {

        public static void copyFile(String sourceFilePath, String destinationFilePath) {
            System.out.println("Copying from '" + sourceFilePath + "' to '" + destinationFilePath + "'...");
            try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFilePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine(); // Preserve line breaks
                }
                System.out.println("File copied successfully!");
            } catch (IOException e) {
                System.err.println("Error during file copy: " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            // Create a source file for demonstration
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("source.txt"))) {
                writer.write("This is the first line.");
                writer.newLine();
                writer.write("This is the second line from source.txt.");
            } catch (IOException e) {
                System.err.println("Error creating source.txt: " + e.getMessage());
            }

            copyFile("source.txt", "destination.txt");
            copyFile("nonexistent.txt", "error.txt"); // Test with non-existent source
        }
    }
    ```

2.  **Read a binary file (e.g., an image) and write it to another file.**
    ```java
    import java.io.FileInputStream;
    import java.io.FileOutputStream;
    import java.io.IOException;

    public class BinaryFileCopier {

        public static void copyBinaryFile(String sourcePath, String destinationPath) {
            System.out.println("Copying binary file from '" + sourcePath + "' to '" + destinationPath + "'...");
            try (FileInputStream fis = new FileInputStream(sourcePath);
                 FileOutputStream fos = new FileOutputStream(destinationPath)) {
                byte[] buffer = new byte[4096]; // Use a buffer for efficiency
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                System.out.println("Binary file copied successfully!");
            } catch (IOException e) {
                System.err.println("Error copying binary file: " + e.getMessage());
            }
        }

        public static void main(String[] args) {
            // To demonstrate, you would need an actual image file (e.g., "input.jpg")
            // You can replace "input.jpg" with any existing binary file for testing.
            // If you don't have one, this will just show the error.

            // Example: Create a dummy binary file (e.g., 100 bytes of zeros)
            try (FileOutputStream fos = new FileOutputStream("dummy_image.bin")) {
                byte[] data = new byte[100];
                fos.write(data); // Write 100 null bytes
                System.out.println("Created dummy_image.bin");
            } catch (IOException e) {
                System.err.println("Could not create dummy_image.bin: " + e.getMessage());
            }

            copyBinaryFile("dummy_image.bin", "dummy_image_copy.bin");
            copyBinaryFile("nonexistent.bin", "error_copy.bin"); // Test with non-existent source
        }
    }
    ```

3.  **Count the number of words in a text file.**
    ```java
    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.regex.Matcher;
    import java.util.regex.Pattern;

    public class WordCounter {

        public static int countWords(String filePath) {
            int wordCount = 0;
            // A pattern to find sequences of one or more word characters
            // \\b is a word boundary, \\w+ matches one or more word characters (letters, digits, underscore)
            Pattern wordPattern = Pattern.compile("\\b\\w+\\b");

            System.out.println("Counting words in file: " + filePath);
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = wordPattern.matcher(line);
                    while (matcher.find()) {
                        wordCount++;
                    }
                }
                System.out.println("Finished counting words in: " + filePath);
            } catch (IOException e) {
                System.err.println("Error reading file to count words: " + e.getMessage());
                return -1; // Indicate an error
            }
            return wordCount;
        }

        public static void main(String[] args) {
            // Create a dummy file for testing
            try {
                java.nio.file.Files.write(java.nio.file.Paths.get("words.txt"),
                        "Hello world. This is a test file.\nIt has multiple lines and some punctuation."
                                .getBytes());
            } catch (IOException e) {
                System.err.println("Could not create words.txt: " + e.getMessage());
            }

            int count = countWords("words.txt");
            if (count != -1) {
                System.out.println("Total words: " + count); // Expected: 13
            }

            int count2 = countWords("nonexistent_words.txt");
            if (count2 != -1) {
                System.out.println("Total words: " + count2);
            }
        }
    }
    ```

### Topic 2: Serialization and Reflection API

#### Interview Questions:

1.  **What is Serialization in Java? Why is it used?**
    *   **Serialization** is the process of converting an object's state (its data and fields) into a byte stream. This byte stream can then be saved to a file, stored in a database, or transmitted across a network.
    *   **Deserialization** is the reverse process: reconstructing the object from the byte stream.
    *   **Why it is used**:
        1.  **Persistence**: To save the state of an object so that it can be recreated later. This allows objects to "live" beyond the execution of the program.
        2.  **Inter-process Communication (IPC) / Remote Method Invocation (RMI)**: To transmit objects between different Java Virtual Machines (JVMs) or across a network.
        3.  **Caching**: To store complex object structures in a serialized form for quicker retrieval.
        4.  **Deep Copying**: Although not its primary purpose, serialization can be used to create a deep copy of an object.

2.  **What is the `Serializable` interface? Does it have any methods?**
    *   The `java.io.Serializable` interface is a **marker interface**.
    *   **It does not have any methods**. Its sole purpose is to "mark" a class, indicating to the Java Virtual Machine (JVM) that objects of this class can be serialized.
    *   If a class implements `Serializable`, its objects can be converted into a byte stream. If a class does not implement `Serializable`, attempts to serialize its objects will result in a `NotSerializableException`.

3.  **Explain the `transient` keyword.**
    *   The `transient` keyword is a field modifier in Java.
    *   **Purpose**: When a field is marked as `transient`, it means that its value should **not** be included in the serialized form of the object.
    *   **Behavior**: During deserialization, `transient` fields are initialized to their default values (0 for numeric types, `false` for boolean, and `null` for object references).
    *   **Use Cases**:
        1.  **Security**: To prevent sensitive data (like passwords or API keys) from being persisted or transmitted.
        2.  **Performance/Space**: If a field can be recomputed from other data or is not essential for the object's state to be recreated, marking it `transient` can save space and serialization time.
        3.  **Non-serializable Objects**: If a class contains fields that are not `Serializable` (e.g., `Thread` objects, `InputStream`), they *must* be marked `transient` to allow the parent class to be serialized.

4.  **What is Reflection API? Give a use case.**
    *   The **Java Reflection API** is a powerful feature that allows a running Java application to examine or "introspect" itself, and also to manipulate its internal properties (fields, methods, constructors) at runtime.
    *   It allows programs to dynamically create objects, invoke methods, and inspect/modify fields without knowing their names at compile time.
    *   Classes related to Reflection are mainly found in `java.lang.reflect` package.
    *   **Key Capabilities**:
        *   Inspect class members (fields, methods, constructors).
        *   Instantiate new objects dynamically.
        *   Invoke methods dynamically.
        *   Access and modify fields dynamically (even private ones, with appropriate permissions).
        *   Identify the interfaces implemented by a class or interface.
    *   **Use Case**:
        *   **Frameworks and Libraries**: Many Java frameworks (e.g., Spring, Hibernate, JUnit) heavily use Reflection. For instance, Spring uses it for Dependency Injection to instantiate beans and inject dependencies. JUnit uses it to find and execute test methods (those annotated with `@Test`).
        *   **IDE Tools**: Integrated Development Environments use Reflection to provide features like autocompletion, debugging, and code analysis.
        *   **Object-Relational Mappers (ORMs)**: ORMs like Hibernate use Reflection to map Java objects to database tables, dynamically accessing field values and method calls.
        *   **Dynamic Proxies**: Used to create proxy objects at runtime that can intercept method calls (e.g., for AOP or mocking frameworks).

5.  **How do you dynamically load a class at runtime using Reflection?**
    *   You can dynamically load a class at runtime using the static `forName()` method of the `java.lang.Class` class.
    *   **Syntax**: `Class<?> c = Class.forName("com.example.MyClass");`
    *   This method loads the class into memory if it hasn't been loaded already.
    *   Once you have the `Class` object, you can then perform other reflective operations:
        *   **Instantiate an object**:
            *   `Object obj = c.getDeclaredConstructor().newInstance();` (preferred for modern Java, handles private constructors)
            *   `Object obj = c.newInstance();` (deprecated in Java 9, requires a public no-arg constructor)
        *   **Access methods/fields/constructors**:
            *   `Method method = c.getDeclaredMethod("myMethod", parameterTypes);`
            *   `Field field = c.getDeclaredField("myField");`
    *   **Example**:
        ```java
        try {
            // Load the class
            Class<?> myClass = Class.forName("java.util.ArrayList");
            System.out.println("Class loaded: " + myClass.getName());

            // Create an instance (assuming a no-arg constructor)
            Object list = myClass.getDeclaredConstructor().newInstance();
            System.out.println("Instance created: " + list.getClass().getName());

            // Get a method and invoke it
            java.lang.reflect.Method addMethod = myClass.getMethod("add", Object.class);
            addMethod.invoke(list, "Dynamic Element");
            System.out.println("Method 'add' invoked. List content: " + list);

        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error during reflection: " + e.getMessage());
        }
        ```

#### Practice Questions:

1.  **Serialize and deserialize a `HashMap` containing custom objects.**
    ```java
    import java.io.*;
    import java.util.HashMap;
    import java.util.Map;

    class Product implements Serializable {
        private static final long serialVersionUID = 2L; // Updated serialVersionUID
        String id;
        String name;
        double price;

        public Product(String id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Product{id='" + id + "', name='" + name + "', price=" + price + '}';
        }
    }

    public class HashMapSerialization {
        private static final String FILE_NAME = "products_map.ser";

        public static void main(String[] args) {
            // Original HashMap
            Map<String, Product> originalMap = new HashMap<>();
            originalMap.put("P001", new Product("P001", "Laptop", 1200.00));
            originalMap.put("P002", new Product("P002", "Mouse", 25.50));
            originalMap.put("P003", new Product("P003", "Keyboard", 75.00));

            System.out.println("Original HashMap: " + originalMap);

            // Serialization
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(originalMap);
                System.out.println("\nHashMap serialized to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("Serialization error: " + e.getMessage());
            }

            // Deserialization
            Map<String, Product> deserializedMap = null;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                deserializedMap = (Map<String, Product>) ois.readObject();
                System.out.println("HashMap deserialized from " + FILE_NAME);
                System.out.println("Deserialized HashMap: " + deserializedMap);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Deserialization error: " + e.getMessage());
            }

            // Verify content
            if (originalMap.equals(deserializedMap)) {
                System.out.println("\nOriginal and deserialized maps are identical.");
            } else {
                System.out.println("\nMaps are different!");
            }
        }
    }
    ```

2.  **Use Reflection to inspect all methods of a given class.**
    ```java
    import java.lang.reflect.Method;
    import java.util.Arrays;

    public class MethodInspector {

        public static void inspectMethods(Class<?> clazz) {
            System.out.println("--- Inspecting methods of " + clazz.getName() + " ---");

            // getMethods() returns all public methods, including inherited ones
            System.out.println("\nPublic Methods (including inherited):");
            for (Method method : clazz.getMethods()) {
                System.out.println("  " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")");
            }

            // getDeclaredMethods() returns all methods declared by this class, regardless of access modifier, but not inherited
            System.out.println("\nDeclared Methods (only this class, all access modifiers):");
            for (Method method : clazz.getDeclaredMethods()) {
                System.out.println("  " + method.getName() + "(" + Arrays.toString(method.getParameterTypes()) + ")");
            }
            System.out.println("----------------------------------------------");
        }

        public static void main(String[] args) {
            inspectMethods(String.class);
            inspectMethods(java.util.ArrayList.class);
            inspectMethods(MethodInspector.class); // Inspecting itself
        }
    }
    ```

3.  **Create an instance of a class using `Class.forName()` and `newInstance()` (or `getDeclaredConstructor().newInstance()`).**
    ```java
    import java.lang.reflect.Constructor;
    import java.lang.reflect.Method;

    class MyDynamicClass {
        private String message;

        public MyDynamicClass() {
            this.message = "Hello from default constructor!";
        }

        public MyDynamicClass(String msg) {
            this.message = "Hello with message: " + msg;
        }

        public void printMessage() {
            System.out.println("MyDynamicClass instance says: " + message);
        }

        private void secretMethod() {
            System.out.println("Shhh! This is a secret method.");
        }
    }

    public class DynamicInstantiationDemo {
        public static void main(String[] args) {
            String className = "MyDynamicClass";

            try {
                // 1. Load the class
                Class<?> dynamicClass = Class.forName(className);
                System.out.println("Class '" + className + "' loaded successfully.");

                // 2. Create an instance using the no-arg constructor
                // Using getDeclaredConstructor().newInstance() is safer and handles private constructors
                Constructor<?> noArgConstructor = dynamicClass.getDeclaredConstructor();
                MyDynamicClass instance1 = (MyDynamicClass) noArgConstructor.newInstance();
                instance1.printMessage();

                // 3. Create an instance using a parameterized constructor
                Constructor<?> parameterizedConstructor = dynamicClass.getDeclaredConstructor(String.class);
                MyDynamicClass instance2 = (MyDynamicClass) parameterizedConstructor.newInstance("Dynamically created!");
                instance2.printMessage();

                // 4. Invoke a method dynamically
                Method printMethod = dynamicClass.getMethod("printMessage");
                printMethod.invoke(instance1); // Invoke on instance1
                printMethod.invoke(instance2); // Invoke on instance2

                // 5. Access and invoke a private method (requires setAccessible(true))
                Method secretMethod = dynamicClass.getDeclaredMethod("secretMethod");
                secretMethod.setAccessible(true); // Override access control
                secretMethod.invoke(instance1);

            } catch (ClassNotFoundException e) {
                System.err.println("Error: Class '" + className + "' not found.");
            } catch (NoSuchMethodException e) {
                System.err.println("Error: No such method/constructor found. " + e.getMessage());
            } catch (Exception e) { // Catch all other reflection-related exceptions
                System.err.println("An unexpected reflection error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    ```

### Week 7: Generics and Functional Programming Solutions

#### Topic 1: Generics

**Interview Questions & Solutions:**

1.  **What are Generics in Java? What problem do they solve?**
    *   **Solution:** Generics allow types (classes, interfaces, methods) to operate on objects of various types while providing compile-time type safety. They solve the problem of type casting and potential `ClassCastException` at runtime by ensuring type correctness at compile time. Before Generics, collections stored `Object`s, requiring manual casting and making code error-prone.

2.  **Explain type erasure in Generics.**
    *   **Solution:** Type erasure is the process where generic type information is removed during compilation and replaced with their raw types (or `Object` if no bound is specified). For example, `List<String>` becomes `List` at runtime. This maintains backward compatibility with older Java versions but means generic type information is not available at runtime.

3.  **What are wildcards (`?`) in Generics? Differentiate between `? extends T` and `? super T`. (PECS principle)**
    *   **Solution:** Wildcards (`?`) represent an unknown type.
        *   `? extends T` (Upper Bounded Wildcard): Represents a type that is `T` or a subclass of `T`. Used when you want to *read* (`get`) elements from a generic collection (e.g., `List<? extends Number>`). It acts as a *producer* of `T`s (or its subtypes). You cannot add elements to such a list (except `null`).
        *   `? super T` (Lower Bounded Wildcard): Represents a type that is `T` or a superclass of `T`. Used when you want to *add* (`put`) elements into a generic collection (e.g., `List<? super Integer>`). It acts as a *consumer* of `T`s (or its supertypes). You can add `T` or its subtypes to such a list.
    *   **PECS Principle:** **P**roducer-**E**xtends, **C**onsumer-**S**uper. If a generic type is a producer of values (you're reading from it), use `extends`. If it's a consumer of values (you're writing to it), use `super`.

4.  **Can you use primitive types with Generics? Why or why not?**
    *   **Solution:** No, primitive types (like `int`, `char`, `double`) cannot be used directly with Generics. This is because Generics work with objects, and primitive types are not objects. Java's auto-boxing and unboxing features allow you to use their wrapper classes (e.g., `Integer`, `Character`, `Double`) which effectively bridges this gap by automatically converting between primitive and wrapper types.

5.  **How do you create a generic method?**
    *   **Solution:** A generic method is declared with a type parameter before the return type.
        ```java
        public <T> void printArray(T[] array) {
            for (T element : array) {
                System.out.println(element);
            }
        }
        ```

**Practice Questions & Solutions:**

1.  **Create a generic `Stack` class that can hold any type of object.**
    ```java
    import java.util.ArrayList;
    import java.util.EmptyStackException;
    import java.util.List;

    public class GenericStack<T> {
        private List<T> stack;

        public GenericStack() {
            stack = new ArrayList<>();
        }

        public void push(T element) {
            stack.add(element);
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.remove(stack.size() - 1);
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.get(stack.size() - 1);
        }

        public boolean isEmpty() {
            return stack.isEmpty();
        }

        public int size() {
            return stack.size();
        }

        public static void main(String[] args) {
            GenericStack<String> stringStack = new GenericStack<>();
            stringStack.push("Hello");
            stringStack.push("World");
            System.out.println(stringStack.pop()); // World

            GenericStack<Integer> intStack = new GenericStack<>();
            intStack.push(10);
            intStack.push(20);
            System.out.println(intStack.peek()); // 20
        }
    }
    ```

2.  **Write a generic method to find the maximum element in a list of `Comparable` objects.**
    ```java
    import java.util.List;

    public class GenericsPractice {
        public static <T extends Comparable<T>> T findMax(List<T> list) {
            if (list == null || list.isEmpty()) {
                return null;
            }
            T max = list.get(0);
            for (T element : list) {
                if (element.compareTo(max) > 0) {
                    max = element;
                }
            }
            return max;
        }

        public static void main(String[] args) {
            List<Integer> numbers = List.of(5, 2, 9, 1, 7);
            System.out.println("Max Integer: " + findMax(numbers)); // 9

            List<String> words = List.of("apple", "banana", "cherry", "date");
            System.out.println("Max String: " + findMax(words)); // date
        }
    }
    ```

3.  **Implement a generic `Pair` class that holds two elements of potentially different types.**
    ```java
    public class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "Pair{" + "key=" + key + ", value=" + value + '}';
        }

        public static void main(String[] args) {
            Pair<String, Integer> student = new Pair<>("Alice", 25);
            System.out.println(student.getKey() + " is " + student.getValue() + " years old."); // Alice is 25 years old.

            Pair<Double, String> item = new Pair<>(19.99, "Book");
            System.out.println("Price: " + item.getKey() + ", Item: " + item.getValue()); // Price: 19.99, Item: Book
        }
    }
    ```

#### Topic 2: Functional Programming with Lambda Expressions and Stream API

**Interview Questions & Solutions:**

1.  **What are Lambda Expressions? How do they enable functional programming in Java?**
    *   **Solution:** Lambda expressions are anonymous functions that provide a concise way to represent an instance of a functional interface. They enable functional programming in Java by allowing functions to be treated as first-class citizens – passed as arguments, returned from methods, and assigned to variables. This promotes a more declarative and less verbose coding style, especially with the Stream API.

2.  **What is a Functional Interface? Give examples.**
    *   **Solution:** A functional interface is an interface that contains exactly one abstract method. It can have default and static methods, but only one abstract method. They are marked with the `@FunctionalInterface` annotation (though not strictly required, it's good practice).
    *   **Examples:** `Runnable` (single method `run()`), `Callable` (single method `call()`), `Comparator` (single method `compare()`), `Predicate<T>` (`test(T)`), `Function<T, R>` (`apply(T)`), `Consumer<T>` (`accept(T)`), `Supplier<T>` (`get()`).

3.  **Explain the Stream API. What are its advantages?**
    *   **Solution:** The Stream API (introduced in Java 8) provides a powerful and functional way to process collections of objects. It allows for declarative data processing, similar to SQL queries on database tables.
    *   **Advantages:**
        *   **Declarative:** Focus on *what* to do, not *how* to do it.
        *   **Concise Code:** Reduces boilerplate compared to traditional loops.
        *   **Parallel Processing:** Streams can easily be processed in parallel (`.parallelStream()`), leveraging multi-core processors for performance gains with minimal code changes.
        *   **Lazy Evaluation:** Intermediate operations are not executed until a terminal operation is invoked.
        *   **No Side Effects:** Streams are designed to be immutable, promoting pure functions.

4.  **Differentiate between intermediate and terminal operations in Streams.**
    *   **Solution:**
        *   **Intermediate Operations:** Transform a stream into another stream. They are lazy, meaning they don't execute until a terminal operation is called. They can be chained. Examples: `filter()`, `map()`, `sorted()`, `distinct()`, `peek()`, `limit()`, `skip()`.
        *   **Terminal Operations:** Produce a result or a side-effect, closing the stream. After a terminal operation, the stream cannot be reused. Examples: `forEach()`, `collect()`, `reduce()`, `count()`, `min()`, `max()`, `sum()`, `anyMatch()`, `allMatch()`, `noneMatch()`, `findFirst()`, `findAny()`.

5.  **What is `Optional`? How does it help avoid `NullPointerExceptions`?**
    *   **Solution:** `Optional<T>` is a container object that may or may not contain a non-null value. It provides a way to explicitly handle the presence or absence of a value, preventing the common `NullPointerException` (NPE) that often occurs when a method returns `null`.
    *   **How it helps:** Instead of returning `null`, a method can return an `Optional.empty()` or `Optional.of(value)`. Consumers of the method can then use methods like `isPresent()`, `orElse()`, `orElseThrow()`, `ifPresent()`, or `map()/flatMap()` to safely access or handle the absence of a value, making the code more robust and readable.

6.  **Explain `map()`, `filter()`, `reduce()`, and `collect()` in Stream API.**
    *   **Solution:**
        *   **`filter(Predicate<T> predicate)`:** An intermediate operation that returns a stream consisting of the elements that match the given predicate. It's used to select elements based on a condition.
        *   **`map(Function<T, R> mapper)`:** An intermediate operation that transforms each element of a stream into another form by applying a function. It produces a new stream of the transformed elements.
        *   **`reduce(T identity, BinaryOperator<T> accumulator)`:** A terminal operation that combines all elements of a stream into a single result. `identity` is the initial value, and `accumulator` is a function that takes the current result and the next element, returning a new result.
        *   **`collect(Collector<T, A, R> collector)`:** A terminal operation that performs mutable reduction operations on the elements of the stream, such as accumulating elements into a collection (list, set, map), summarizing elements, or concatenating strings. `Collectors` utility class provides many predefined collectors.

**Practice Questions & Solutions:**

1.  **Given a list of strings, filter out those that start with 'A' and convert them to uppercase using Streams.**
    ```java
    import java.util.Arrays;
    import java.util.List;
    import java.util.stream.Collectors;

    public class FunctionalPractice {
        public static void main(String[] args) {
            List<String> words = Arrays.asList("apple", "banana", "Apricot", "grape", "Avocado");

            List<String> filteredAndUppercase = words.stream()
                                                    .filter(s -> s.startsWith("A") || s.startsWith("a"))
                                                    .map(String::toUpperCase)
                                                    .collect(Collectors.toList());
            System.out.println("Filtered and Uppercase: " + filteredAndUppercase); // [APPLE, APRICOT, AVOCADO]
        }
    }
    ```

2.  **Calculate the average of a list of numbers using Stream API.**
    ```java
    import java.util.Arrays;
    import java.util.List;
    import java.util.OptionalDouble;

    public class FunctionalPractice {
        public static void main(String[] args) {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            OptionalDouble average = numbers.stream()
                                            .mapToInt(Integer::intValue) // Use mapToInt for primitive stream to use average()
                                            .average();

            average.ifPresent(avg -> System.out.println("Average: " + avg)); // Average: 5.5
            // Or: System.out.println("Average: " + average.orElse(0.0));
        }
    }
    ```

3.  **Find the sum of all odd numbers in a list using `reduce()`**.
    ```java
    import java.util.Arrays;
    import java.util.List;

    public class FunctionalPractice {
        public static void main(String[] args) {
            List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

            int sumOfOdds = numbers.stream()
                                    .filter(n -> n % 2 != 0)
                                    .reduce(0, Integer::sum); // (a, b) -> a + b
            System.out.println("Sum of odd numbers: " + sumOfOdds); // 25 (1+3+5+7+9)
        }
    }
    ```

4.  **Group a list of `Product` objects by their category.**
    ```java
    import java.util.Arrays;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    class Product {
        String name;
        String category;
        double price;

        public Product(String name, String category, double price) {
            this.name = name;
            this.category = category;
            this.price = price;
        }

        public String getCategory() { return category; }
        public String getName() { return name; }
        public double getPrice() { return price; }

        @Override
        public String toString() {
            return "Product{" + "name='" + name + '\'' + ", category='" + category + '\'' + ", price=" + price + '}';
        }
    }

    public class FunctionalPractice {
        public static void main(String[] args) {
            List<Product> products = Arrays.asList(
                new Product("Laptop", "Electronics", 1200.00),
                new Product("Keyboard", "Electronics", 75.00),
                new Product("Book", "Books", 25.00),
                new Product("Mouse", "Electronics", 30.00),
                new Product("Novel", "Books", 15.00)
            );

            Map<String, List<Product>> productsByCategory = products.stream()
                                                                    .collect(Collectors.groupingBy(Product::getCategory));
            System.out.println("Products by Category: " + productsByCategory);
            /*
            Output:
            Products by Category: {Books=[Product{name='Book', category='Books', price=25.0}, Product{name='Novel', category='Books', price=15.0}], Electronics=[Product{name='Laptop', category='Electronics', price=1200.0}, Product{name='Keyboard', category='Electronics', price=75.0}, Product{name='Mouse', category='Electronics', price=30.0}]}
            */
        }
    }
    ```

### Week 8: Data Structures & Algorithms (Sorting, Searching) Solutions

#### Topic 1: Sorting Algorithms

**Interview Questions & Solutions:**

1.  **Explain the working principle of Merge Sort and its time complexity.**
    *   **Solution:** Merge Sort is a divide-and-conquer algorithm.
        *   **Divide:** It recursively divides the unsorted list into `n` sublists, each containing one element (a list of one element is considered sorted).
        *   **Conquer (Merge):** It repeatedly merges sublists to produce new sorted sublists until there is only one sorted list remaining. The merging process compares elements from two sorted sublists and places them into a temporary array in sorted order.
    *   **Time Complexity:**
        *   **Best/Average/Worst Case:** `O(N log N)`. Dividing takes `log N` steps, and merging takes `N` comparisons at each level.
        *   **Space Complexity:** `O(N)` for the temporary array used during merging.

2.  **Explain the working principle of Quick Sort and its average/worst-case time complexity.**
    *   **Solution:** Quick Sort is also a divide-and-conquer algorithm that picks an element as a pivot and partitions the array around the picked pivot.
        *   **Divide:** It selects a 'pivot' element from the array (e.g., first, last, median, or random). It then partitions the other elements into two sub-arrays, according to whether they are less than or greater than the pivot.
        *   **Conquer:** It recursively sorts the two sub-arrays.
        *   **Combine:** The sorted sub-arrays are already in place, so no explicit combination step is needed (the array is sorted in-place).
    *   **Time Complexity:**
        *   **Average Case:** `O(N log N)`. This occurs when the pivot consistently divides the array into roughly equal halves.
        *   **Worst Case:** `O(N^2)`. This occurs when the pivot consistently picks the smallest or largest element, leading to highly unbalanced partitions (e.g., already sorted array with first element as pivot).

3.  **Differentiate between stable and unstable sorting algorithms.**
    *   **Solution:**
        *   **Stable Sorting Algorithm:** Preserves the relative order of equal elements. If two elements have the same value, their original order in the input array will be maintained in the sorted output. Example: Merge Sort, Insertion Sort, Bubble Sort.
        *   **Unstable Sorting Algorithm:** Does not guarantee to preserve the relative order of equal elements. Example: Quick Sort, Heap Sort, Selection Sort.

4.  **When would you choose Insertion Sort over Merge Sort?**
    *   **Solution:**
        *   **Small Datasets:** Insertion Sort performs very well on small arrays (often faster than `N log N` algorithms due to lower constant factors and overhead). Hybrid sorting algorithms often use Insertion Sort for small sub-arrays.
        *   **Nearly Sorted Arrays:** Insertion Sort has a time complexity of `O(N)` in the best case when the array is already sorted or nearly sorted, making it efficient in such scenarios.
        *   **In-Place and Simple:** It's an in-place algorithm (requires minimal extra space) and is simple to implement.
        *   **Online Sorting:** It can sort a list as it receives elements (online algorithm), whereas Merge Sort needs the entire list.

5.  **What is the best-case, average-case, and worst-case time complexity of Bubble Sort?**
    *   **Solution:**
        *   **Best Case:** `O(N)` - When the array is already sorted. The algorithm makes one pass, detects no swaps, and terminates early.
        *   **Average Case:** `O(N^2)`
        *   **Worst Case:** `O(N^2)` - When the array is sorted in reverse order, it requires the maximum number of comparisons and swaps.

**Practice Questions & Solutions:**

1.  **Implement Bubble Sort from scratch.**
    ```java
    import java.util.Arrays;

    public class BubbleSort {
        public void bubbleSort(int[] arr) {
            int n = arr.length;
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - 1 - i; j++) {
                    if (arr[j] > arr[j + 1]) {
                        // Swap arr[j] and arr[j+1]
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        swapped = true;
                    }
                }
                // If no two elements were swapped by inner loop, then break
                if (!swapped) {
                    break;
                }
            }
        }

        public static void main(String[] args) {
            int[] arr = {64, 34, 25, 12, 22, 11, 90};
            new BubbleSort().bubbleSort(arr);
            System.out.println("Sorted array (Bubble Sort): " + Arrays.toString(arr)); // [11, 12, 22, 25, 34, 64, 90]
        }
    }
    ```

2.  **Implement Quick Sort from scratch.**
    ```java
    import java.util.Arrays;

    public class QuickSort {

        public void quickSort(int[] arr) {
            if (arr == null || arr.length <= 1) {
                return;
            }
            quickSort(arr, 0, arr.length - 1);
        }

        private void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                // pi is partitioning index, arr[pi] is now at right place
                int pi = partition(arr, low, high);

                // Recursively sort elements before partition and after partition
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }

        private int partition(int[] arr, int low, int high) {
            int pivot = arr[high]; // Choosing the last element as pivot
            int i = (low - 1); // Index of smaller element

            for (int j = low; j < high; j++) {
                // If current element is smaller than or equal to pivot
                if (arr[j] <= pivot) {
                    i++;

                    // Swap arr[i] and arr[j]
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }

            // Swap arr[i+1] and arr[high] (or pivot)
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            return i + 1;
        }

        public static void main(String[] args) {
            int[] arr = {10, 7, 8, 9, 1, 5};
            new QuickSort().quickSort(arr);
            System.out.println("Sorted array (Quick Sort): " + Arrays.toString(arr)); // [1, 5, 7, 8, 9, 10]
        }
    }
    ```

3.  **Given an array of integers, sort it using Heap Sort.**
    *   **Conceptual Approach (Implementation is lengthy):**
        1.  **Build a Max-Heap:** Convert the input array into a max-heap. This means the largest element is at the root. Start from the last non-leaf node and heapify upwards.
        2.  **Extract Max and Heapify:**
            *   Swap the root (largest element) with the last element of the heap.
            *   Reduce the heap size by 1.
            *   Heapify the new root to maintain the max-heap property.
        3.  Repeat step 2 until the heap size is 1. The array will be sorted in ascending order.
    *   **Time Complexity:** `O(N log N)`
    *   **Space Complexity:** `O(1)` (in-place)

4.  **Sort an array of strings alphabetically.**
    ```java
    import java.util.Arrays;

    public class StringSort {
        public static void main(String[] args) {
            String[] names = {"banana", "apple", "date", "cherry"};

            // Using Arrays.sort() which uses a dual-pivot Quicksort for primitives
            // and TimSort (hybrid MergeSort/InsertionSort) for objects.
            Arrays.sort(names);
            System.out.println("Sorted strings: " + Arrays.toString(names)); // [apple, banana, cherry, date]

            // For case-insensitive sort:
            String[] namesCaseInsensitive = {"Banana", "apple", "Date", "cherry"};
            Arrays.sort(namesCaseInsensitive, String.CASE_INSENSITIVE_ORDER);
            System.out.println("Sorted strings (case-insensitive): " + Arrays.toString(namesCaseInsensitive)); // [apple, Banana, cherry, Date]
        }
    }
    ```

#### Topic 2: Searching Algorithms

**Interview Questions & Solutions:**

1.  **Differentiate between Linear Search and Binary Search. What are their time complexities?**
    *   **Solution:**
        *   **Linear Search:**
            *   **Working:** Scans each element of the array sequentially until the target element is found or the end of the array is reached.
            *   **Precondition:** No specific order required (works on unsorted arrays).
            *   **Time Complexity:** `O(N)` in the worst and average cases. `O(1)` in the best case (if the target is the first element).
        *   **Binary Search:**
            *   **Working:** Repeatedly divides the search interval in half. It compares the target value with the middle element of the array. If they match, the search is successful. If the target is smaller, the search continues in the lower half; if larger, in the upper half.
            *   **Precondition:** Requires the array to be sorted.
            *   **Time Complexity:** `O(log N)` in all cases (best, average, worst).

2.  **What are the preconditions for Binary Search?**
    *   **Solution:** The primary precondition for Binary Search is that the **array (or list) must be sorted** (either in ascending or descending order). If the array is not sorted, Binary Search will not yield correct results.

3.  **How does binary search work on an array with duplicate elements?**
    *   **Solution:** Standard Binary Search will find *an* occurrence of the target element. It does not guarantee finding the *first* or *last* occurrence. If multiple duplicates exist, the specific index returned depends on the exact implementation (how `mid` is calculated and how comparisons are handled). To find the first/last occurrence, you would typically find *an* occurrence and then perform additional linear scans (or modified binary searches) to the left/right of that found index.

4.  **Explain interpolation search. When is it more efficient than binary search?**
    *   **Solution:** Interpolation Search is an improvement over Binary Search for uniformly distributed data. Instead of always checking the middle element, it estimates the position of the target element based on its value relative to the low and high bounds of the search space, similar to how one might look for a word in a dictionary.
    *   **Formula:** `pos = low + (((high - low) / (arr[high] - arr[low])) * (target - arr[low]))`
    *   **When it's more efficient:** It's more efficient than Binary Search when the elements in the array are **uniformly distributed**. In such cases, its average time complexity can be `O(log log N)`.
    *   **When it's less efficient:** In the worst case (e.g., non-uniformly distributed data, like all elements being the same except one), its time complexity can degrade to `O(N)`, similar to Linear Search.

**Practice Questions & Solutions:**

1.  **Implement Linear Search for an array of strings.**
    ```java
    public class LinearSearch {
        public int linearSearch(String[] arr, String target) {
            for (int i = 0; i < arr.length; i++) {
                if (arr[i].equals(target)) { // Use .equals() for string comparison
                    return i; // Target found
                }
            }
            return -1; // Target not found
        }

        public static void main(String[] args) {
            String[] names = {"Alice", "Bob", "Charlie", "David"};
            LinearSearch searcher = new LinearSearch();

            System.out.println("Charlie found at index: " + searcher.linearSearch(names, "Charlie")); // 2
            System.out.println("Eve found at index: " + searcher.linearSearch(names, "Eve"));     // -1
        }
    }
    ```

2.  **Implement Binary Search recursively.**
    ```java
    public class RecursiveBinarySearch {

        public int binarySearch(int[] arr, int target) {
            return binarySearch(arr, target, 0, arr.length - 1);
        }

        private int binarySearch(int[] arr, int target, int left, int right) {
            if (left <= right) {
                int mid = left + (right - left) / 2;

                if (arr[mid] == target) {
                    return mid;
                }
                if (arr[mid] < target) {
                    return binarySearch(arr, target, mid + 1, right); // Search right half
                } else {
                    return binarySearch(arr, target, left, mid - 1); // Search left half
                }
            }
            return -1; // Target not found
        }

        public static void main(String[] args) {
            int[] sortedArr = {2, 5, 8, 12, 16, 23, 38, 56, 72, 91};
            RecursiveBinarySearch searcher = new RecursiveBinarySearch();

            System.out.println("Element 23 found at index: " + searcher.binarySearch(sortedArr, 23)); // 5
            System.out.println("Element 7 found at index: " + searcher.binarySearch(sortedArr, 7));   // -1
        }
    }
    ```

3.  **Find the first and last occurrence of an element in a sorted array.**
    ```java
    public class FirstLastOccurrence {

        public int findFirst(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            int firstOccurrence = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (arr[mid] == target) {
                    firstOccurrence = mid;
                    right = mid - 1; // Try to find an earlier occurrence in the left half
                } else if (arr[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return firstOccurrence;
        }

        public int findLast(int[] arr, int target) {
            int left = 0;
            int right = arr.length - 1;
            int lastOccurrence = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (arr[mid] == target) {
                    lastOccurrence = mid;
                    left = mid + 1; // Try to find a later occurrence in the right half
                } else if (arr[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return lastOccurrence;
        }

        public static void main(String[] args) {
            int[] arr = {1, 2, 3, 3, 3, 4, 5, 5, 6};
            FirstLastOccurrence finder = new FirstLastOccurrence();

            System.out.println("First occurrence of 3: " + finder.findFirst(arr, 3)); // 2
            System.out.println("Last occurrence of 3: " + finder.findLast(arr, 3));   // 4

            System.out.println("First occurrence of 5: " + finder.findFirst(arr, 5)); // 6
            System.out.println("Last occurrence of 5: " + finder.findLast(arr, 5));   // 7

            System.out.println("First occurrence of 7: " + finder.findFirst(arr, 7)); // -1
            System.out.println("Last occurrence of 7: " + finder.findLast(arr, 7));   // -1
        }
    }
    ```

4.  **Search for an element in a rotated sorted array.**
    *   **Conceptual Approach (Implementation is lengthy):**
        *   A rotated sorted array is like `[4, 5, 6, 7, 0, 1, 2]`. It's sorted in two halves.
        *   Use a modified Binary Search. First, find the pivot point (the smallest element, where rotation occurs).
        *   Once the pivot is found, you know which half of the array is sorted. Then, perform a regular Binary Search on the appropriate sorted half based on the target value.
        *   Alternatively, without finding the pivot explicitly:
            1.  Find `mid`.
            2.  Check if `arr[low]` to `arr[mid]` is sorted.
                *   If `target` is in this range, search in this range.
                *   Else, search in `arr[mid+1]` to `arr[high]`.
            3.  Else (`arr[mid]` to `arr[high]` is sorted).
                *   If `target` is in this range, search in this range.
                *   Else, search in `arr[low]` to `arr[mid-1]`.
    *   **Time Complexity:** `O(log N)`
    *   **Space Complexity:** `O(1)` (iterative) or `O(log N)` (recursive for call stack)

### Week 9: Graphs and Dynamic Programming Basics Solutions

#### Topic 1: Graph Algorithms

**Interview Questions & Solutions:**

1.  **How can graphs be represented in memory? Compare adjacency matrix vs. adjacency list.**
    *   **Solution:**
        *   **Adjacency Matrix:** A `VxV` 2D array (matrix) where `V` is the number of vertices. If there's an edge from vertex `i` to vertex `j`, `matrix[i][j]` is 1 (or weight); otherwise, it's 0.
            *   **Pros:** `O(1)` time to check if an edge exists between two vertices. Easier for dense graphs.
            *   **Cons:** `O(V^2)` space complexity (can be wasteful for sparse graphs). `O(V)` to find all neighbors of a vertex.
        *   **Adjacency List:** An array (or list) of lists. Each index `i` in the main array represents a vertex, and the list at `adj[i]` contains all the vertices adjacent to `i`.
            *   **Pros:** `O(V + E)` space complexity (more efficient for sparse graphs, where `E` is number of edges). `O(degree(V))` to find all neighbors of a vertex (efficient).
            *   **Cons:** `O(degree(V))` time to check if an edge exists (need to iterate the list).
        *   **Comparison:** Adjacency List is generally preferred for most real-world applications where graphs are typically sparse, saving memory and often performing better for traversal algorithms. Adjacency Matrix is better for very dense graphs or when quick `edge-existence` checks are frequent.

2.  **Explain Breadth-First Search (BFS) and Depth-First Search (DFS). What are their time complexities?**
    *   **Solution:** Both are graph traversal algorithms.
        *   **BFS (Breadth-First Search):**
            *   **Working:** Explores the graph level by level. It starts at a source node, visits all its direct neighbors, then all their unvisited neighbors, and so on. It uses a **queue** data structure to keep track of nodes to visit.
            *   **Use Cases:** Shortest path in unweighted graphs, finding all nodes within a certain distance, network broadcasting.
            *   **Time Complexity:** `O(V + E)` (where V is vertices, E is edges) for both adjacency list and matrix (if using adjacency list, `V` for visiting nodes, `E` for visiting edges; if using adjacency matrix, `V^2` because finding neighbors takes `V` time per vertex).
        *   **DFS (Depth-First Search):**
            *   **Working:** Explores as far as possible along each branch before backtracking. It starts at a source node, then goes deep into one of its branches until it hits a dead end, then backtracks and explores other branches. It uses a **stack** (implicitly via recursion or explicitly) data structure.
            *   **Use Cases:** Cycle detection, topological sort, path finding, strongly connected components.
            *   **Time Complexity:** `O(V + E)` for adjacency list. `O(V^2)` for adjacency matrix.

3.  **When would you use BFS over DFS, and vice versa?**
    *   **Solution:**
        *   **Choose BFS when:**
            *   You need to find the **shortest path** in an unweighted graph.
            *   You need to explore nodes level by level (e.g., finding all nodes `k` steps away from the source).
            *   You're looking for all connected components.
        *   **Choose DFS when:**
            *   You need to check for the **existence of a path** between two nodes.
            *   You need to **detect cycles** in a graph.
            *   You need to perform a **topological sort**.
            *   You need to find connected components (can also be done with BFS) or strongly connected components.
            *   When space is a concern for very deep graphs (recursive DFS stack depth can be `V` in worst case, but BFS queue can be `V` as well).

4.  **What is a topological sort? Give an example.**
    *   **Solution:** A topological sort (or topological ordering) of a directed acyclic graph (DAG) is a linear ordering of its vertices such that for every directed edge `u -> v`, vertex `u` comes before vertex `v` in the ordering. It's not possible for graphs with cycles.
    *   **Example:** Consider tasks to be completed for a project. If Task A must be completed before Task B, and B before C, a topological sort would give a valid order of tasks.
        *   Graph: `A -> B`, `A -> C`, `B -> D`, `C -> D`
        *   Possible Topological Sorts: `A, B, C, D` or `A, C, B, D` (A must come before B and C; B and C before D).
    *   **Algorithm:** Typically implemented using DFS or by maintaining in-degrees of nodes (Kahn's algorithm).

5.  **Briefly explain Dijkstra's algorithm.**
    *   **Solution:** Dijkstra's algorithm is a greedy algorithm used to find the **shortest paths from a single source vertex to all other vertices** in a graph with non-negative edge weights.
    *   **Working:** It maintains a set of visited vertices and their shortest known distances from the source. It repeatedly selects the unvisited vertex with the smallest known distance, marks it as visited, and then updates the distances of its neighbors. It uses a **priority queue** to efficiently select the next vertex with the minimum distance.

**Practice Questions & Solutions:**

1.  **Implement BFS for a graph.**
    *   **Solution:** (Provided in the context, `Graph` class includes `BFS` method)
    ```java
    // See provided context: Graph class in GraphDemo.java, specifically the BFS method.
    // Re-iterating the key logic:
    // Uses a Queue.
    // Starts with a source node, marks it visited, adds to queue.
    // While queue is not empty:
    //   Dequeue a node.
    //   Print/Process the node.
    //   For each unvisited neighbor: Mark visited, Enqueue.
    ```

2.  **Find if a path exists between two nodes in a graph.**
    *   **Solution:** This can be solved using either BFS or DFS.
        *   **Using DFS:** Perform a DFS starting from the `source` node. If the `target` node is encountered during the traversal, a path exists.
        *   **Using BFS:** Perform a BFS starting from the `source` node. If the `target` node is dequeued from the queue, a path exists.

    ```java
    // Assuming the Graph class from context:
    class GraphPathExists extends Graph {
        public GraphPathExists(int V) {
            super(V);
        }

        public boolean hasPathDFS(int src, int dest) {
            boolean[] visited = new boolean[V]; // V is from super class
            return hasPathDFSUtil(src, dest, visited);
        }

        private boolean hasPathDFSUtil(int current, int dest, boolean[] visited) {
            if (current == dest) {
                return true;
            }
            visited[current] = true;
            for (int neighbor : adj.get(current)) {
                if (!visited[neighbor]) {
                    if (hasPathDFSUtil(neighbor, dest, visited)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean hasPathBFS(int src, int dest) {
            boolean[] visited = new boolean[V];
            Queue<Integer> queue = new LinkedList<>();

            visited[src] = true;
            queue.add(src);

            while (!queue.isEmpty()) {
                int current = queue.poll();
                if (current == dest) {
                    return true;
                }
                for (int neighbor : adj.get(current)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.add(neighbor);
                    }
                }
            }
            return false;
        }

        public static void main(String[] args) {
            GraphPathExists g = new GraphPathExists(4);
            g.addEdge(0, 1);
            g.addEdge(0, 2);
            g.addEdge(1, 3);
            g.addEdge(2, 3);

            System.out.println("Path from 0 to 3 (DFS): " + g.hasPathDFS(0, 3)); // true
            System.out.println("Path from 0 to 3 (BFS): " + g.hasPathBFS(0, 3)); // true
            System.out.println("Path from 3 to 0 (DFS): " + g.hasPathDFS(3, 0)); // false (directed graph)
        }
    }
    ```

3.  **Count the number of connected components in an undirected graph.**
    *   **Solution:** Iterate through all vertices. For each unvisited vertex, start a BFS or DFS traversal. Each time you start a new traversal from an unvisited vertex, it signifies a new connected component. Increment a counter for each new traversal.

    ```java
    // Assuming the Graph class from context for adjacency list structure
    class ConnectedComponentsGraph extends Graph {
        public ConnectedComponentsGraph(int V) {
            super(V);
        }

        // Add edge for undirected graph
        @Override
        public void addEdge(int v, int w) {
            adj.get(v).add(w);
            adj.get(w).add(v); // For undirected graph
        }

        public int countConnectedComponents() {
            boolean[] visited = new boolean[V]; // V from superclass
            int count = 0;
            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    DFSUtil(i, visited); // Or BFS traversal
                    count++;
                }
            }
            return count;
        }

        // DFSUtil is inherited, but for clarity:
        // private void DFSUtil(int v, boolean[] visited) {
        //     visited[v] = true;
        //     for (int neighbor : adj.get(v)) {
        //         if (!visited[neighbor]) {
        //             DFSUtil(neighbor, visited);
        //         }
        //     }
        // }

        public static void main(String[] args) {
            ConnectedComponentsGraph g = new ConnectedComponentsGraph(5); // 5 vertices
            g.addEdge(0, 1);
            g.addEdge(2, 3);
            // Vertex 4 is isolated

            System.out.println("Number of connected components: " + g.countConnectedComponents()); // 3
        }
    }
    ```

4.  **Detect a cycle in an undirected graph.**
    *   **Solution:** Use DFS. While traversing, keep track of visited nodes and the `parent` of the current node. If you encounter a visited node that is not the parent of the current node, then a cycle is detected.

    ```java
    // Assuming the Graph class from context for adjacency list structure
    class CycleDetectionGraph extends Graph {
        public CycleDetectionGraph(int V) {
            super(V);
        }

        // Add edge for undirected graph
        @Override
        public void addEdge(int v, int w) {
            adj.get(v).add(w);
            adj.get(w).add(v); // For undirected graph
        }

        public boolean containsCycle() {
            boolean[] visited = new boolean[V];
            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    if (dfsDetectCycle(i, visited, -1)) { // -1 indicates no parent for the source of DFS
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean dfsDetectCycle(int u, boolean[] visited, int parent) {
            visited[u] = true;

            for (int v : adj.get(u)) {
                if (!visited[v]) {
                    if (dfsDetectCycle(v, visited, u)) {
                        return true;
                    }
                }
                // If v is visited and not parent of u, then there is a cycle
                else if (v != parent) {
                    return true;
                }
            }
            return false;
        }

        public static void main(String[] args) {
            CycleDetectionGraph g1 = new CycleDetectionGraph(3); // Graph with a cycle
            g1.addEdge(0, 1);
            g1.addEdge(1, 2);
            g1.addEdge(2, 0);
            System.out.println("Graph 1 has cycle: " + g1.containsCycle()); // true

            CycleDetectionGraph g2 = new CycleDetectionGraph(3); // Graph without a cycle (tree)
            g2.addEdge(0, 1);
            g2.addEdge(0, 2);
            System.out.println("Graph 2 has cycle: " + g2.containsCycle()); // false
        }
    }
    ```

#### Topic 2: Introduction to Dynamic Programming

**Interview Questions & Solutions:**

1.  **What is Dynamic Programming? What are its two key characteristics?**
    *   **Solution:** Dynamic Programming (DP) is an algorithmic technique used to solve complex problems by breaking them down into simpler subproblems. It solves each subproblem only once and stores the results to avoid redundant computations.
    *   **Two Key Characteristics:**
        1.  **Overlapping Subproblems:** The same subproblems are encountered and solved repeatedly during the computation. DP stores the solutions to these subproblems.
        2.  **Optimal Substructure:** An optimal solution to the overall problem can be constructed from optimal solutions of its subproblems.

2.  **Differentiate between memoization (top-down DP) and tabulation (bottom-up DP).**
    *   **Solution:**
        *   **Memoization (Top-down DP):**
            *   **Approach:** Starts with the original problem and recursively breaks it down into subproblems. It stores the results of subproblems in a cache (e.g., hash map or array) so that if the same subproblem is encountered again, the stored result is returned without recomputing.
            *   **Nature:** Recursive, lazy (computes subproblems only when needed).
            *   **Pros:** Often more intuitive to implement as it directly mirrors the recursive definition. Only computes necessary subproblems.
            *   **Cons:** Can incur recursion overhead (stack overflow risk for deep recursions).
        *   **Tabulation (Bottom-up DP):**
            *   **Approach:** Starts by solving the smallest subproblems first and then builds up solutions to larger subproblems based on the previously computed smaller solutions. It typically uses an iterative approach and fills a table (array) with solutions.
            *   **Nature:** Iterative, eager (computes all subproblems up to the desired one).
            *   **Pros:** Avoids recursion overhead. Often more space-efficient.
            *   **Cons:** May compute unnecessary subproblems if not all subproblems contribute to the final solution. Can be less intuitive to define the iteration order.

3.  **Explain the concept of optimal substructure with an example.**
    *   **Solution:** Optimal substructure means that an optimal solution to a problem can be constructed from optimal solutions to its subproblems. If a problem has optimal substructure, then locally optimal choices will lead to a globally optimal solution.
    *   **Example: Shortest Path Problem:** If the shortest path from vertex `A` to vertex `C` passes through vertex `B`, then the path from `A` to `B` must also be the shortest path from `A` to `B`, and the path from `B` to `C` must be the shortest path from `B` to `C`. If either subpath were not shortest, we could replace it with a shorter one, making the overall path shorter, which contradicts the assumption that the `A` to `C` path was initially the shortest.

4.  **How does DP improve upon brute-force recursion?**
    *   **Solution:** Brute-force recursive solutions often suffer from redundant computations, meaning they solve the same subproblems multiple times. This leads to exponential time complexities (e.g., `O(2^N)` for naive Fibonacci). Dynamic Programming improves upon this by:
        *   **Eliminating Redundant Computations:** By storing the results of solved subproblems (memoization/tabulation), DP ensures that each subproblem is computed only once.
        *   **Reduced Time Complexity:** This drastically reduces the time complexity from exponential to polynomial (e.g., `O(N)` for Fibonacci), making the solution feasible for larger inputs.

**Practice Questions & Solutions:**

1.  **Implement the `climbing stairs` problem using DP.**
    *   **Problem:** You are climbing a staircase. It takes `n` steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?
    *   **Solution:**
        *   `dp[i]` = number of ways to reach step `i`.
        *   To reach step `i`, you can come from step `i-1` (by taking 1 step) or from step `i-2` (by taking 2 steps).
        *   So, `dp[i] = dp[i-1] + dp[i-2]`.
        *   Base cases: `dp[0] = 1` (one way to be at step 0 - do nothing), `dp[1] = 1` (one way to reach step 1). This is essentially Fibonacci.

    ```java
    public class ClimbingStairs {
        public int climbStairs(int n) {
            if (n <= 0) return 0;
            if (n == 1) return 1;

            int[] dp = new int[n + 1];
            dp[0] = 1; // Can be thought of as one way to reach the ground (start)
            dp[1] = 1;

            for (int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }

        public static void main(String[] args) {
            ClimbingStairs climber = new ClimbingStairs();
            System.out.println("Ways to climb 2 stairs: " + climber.climbStairs(2)); // 2 (1+1, 2)
            System.out.println("Ways to climb 3 stairs: " + climber.climbStairs(3)); // 3 (1+1+1, 1+2, 2+1)
            System.out.println("Ways to climb 4 stairs: " + climber.climbStairs(4)); // 5
        }
    }
    ```

2.  **Solve the `Coin Change` problem (minimum number of coins).**
    *   **Problem:** Given a set of coin denominations and a target amount, find the minimum number of coins needed to make up that amount. If it's not possible, return -1.
    *   **Solution (Tabulation):**
        *   `dp[i]` = minimum coins to make amount `i`.
        *   Initialize `dp` array with `amount + 1` (representing infinity) and `dp[0] = 0`.
        *   Iterate `i` from 1 to `amount`. For each `i`, iterate through `coin` denominations.
        *   If `coin <= i`, then `dp[i] = min(dp[i], 1 + dp[i - coin])`.

    ```java
    import java.util.Arrays;

    public class CoinChange {
        public int coinChange(int[] coins, int amount) {
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, amount + 1); // Initialize with a value larger than any possible count
            dp[0] = 0; // 0 coins needed for amount 0

            for (int i = 1; i <= amount; i++) {
                for (int coin : coins) {
                    if (coin <= i) {
                        dp[i] = Math.min(dp[i], 1 + dp[i - coin]);
                    }
                }
            }
            return dp[amount] > amount ? -1 : dp[amount];
        }

        public static void main(String[] args) {
            CoinChange changer = new CoinChange();
            int[] coins1 = {1, 2, 5};
            System.out.println("Min coins for 11: " + changer.coinChange(coins1, 11)); // 3 (5+5+1)

            int[] coins2 = {2};
            System.out.println("Min coins for 3: " + changer.coinChange(coins2, 3));   // -1

            int[] coins3 = {1};
            System.out.println("Min coins for 0: " + changer.coinChange(coins3, 0));   // 0
        }
    }
    ```

3.  **Find the `Longest Increasing Subsequence` in an array.**
    *   **Problem:** Given an unsorted array of integers, find the length of the longest increasing subsequence.
    *   **Solution (Tabulation):**
        *   `dp[i]` = length of the longest increasing subsequence ending at index `i`.
        *   Initialize all `dp[i] = 1`.
        *   For each `i` from 1 to `n-1`:
            *   For each `j` from 0 to `i-1`:
                *   If `nums[i] > nums[j]`, then `dp[i] = max(dp[i], dp[j] + 1)`.
        *   The result is the maximum value in the `dp` array.
    *   **Time Complexity:** `O(N^2)`

    ```java
    import java.util.Arrays;

    public class LongestIncreasingSubsequence {
        public int lengthOfLIS(int[] nums) {
            if (nums == null || nums.length == 0) {
                return 0;
            }

            int n = nums.length;
            int[] dp = new int[n];
            Arrays.fill(dp, 1); // Each element itself is an LIS of length 1

            for (int i = 1; i < n; i++) {
                for (int j = 0; j < i; j++) {
                    if (nums[i] > nums[j]) {
                        dp[i] = Math.max(dp[i], dp[j] + 1);
                    }
                }
            }

            int maxLength = 0;
            for (int length : dp) {
                maxLength = Math.max(maxLength, length);
            }
            return maxLength;
        }

        public static void main(String[] args) {
            LongestIncreasingSubsequence lis = new LongestIncreasingSubsequence();
            int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
            System.out.println("LIS length for [10, 9, 2, 5, 3, 7, 101, 18]: " + lis.lengthOfLIS(nums1)); // 4 (e.g., 2, 3, 7, 101 or 2, 5, 7, 101)

            int[] nums2 = {0, 1, 0, 3, 2, 3};
            System.out.println("LIS length for [0, 1, 0, 3, 2, 3]: " + lis.lengthOfLIS(nums2)); // 4 (e.g., 0, 1, 2, 3)
        }
    }
    ```

4.  **Implement the `0/1 Knapsack` problem using DP.**
    *   **Problem:** Given weights and values of `n` items, put these items in a knapsack of capacity `W` to get the maximum total value in the knapsack. Each item can either be put in the knapsack or not (0/1 choice).
    *   **Solution (Tabulation):**
        *   `dp[i][j]` = maximum value that can be obtained using first `i` items with a knapsack capacity of `j`.
        *   Iterate `i` from 1 to `n` (items).
        *   Iterate `j` from 0 to `W` (capacity).
        *   If `weight[i-1] <= j`:
            *   `dp[i][j] = max(value[i-1] + dp[i-1][j - weight[i-1]], dp[i-1][j])` (either include current item or not)
        *   Else (`weight[i-1] > j`):
            *   `dp[i][j] = dp[i-1][j]` (cannot include current item)
    *   **Time Complexity:** `O(N*W)`
    *   **Space Complexity:** `O(N*W)` (can be optimized to `O(W)` if only previous row is needed)

    ```java
    public class Knapsack01 {
        public int knapSack(int W, int[] weights, int[] values, int n) {
            int[][] dp = new int[n + 1][W + 1];

            // Build table dp[][] in bottom-up manner
            for (int i = 0; i <= n; i++) {
                for (int w = 0; w <= W; w++) {
                    if (i == 0 || w == 0) { // Base cases
                        dp[i][w] = 0;
                    } else if (weights[i - 1] <= w) { // If current item can fit
                        // max( include current item,  don't include current item )
                        dp[i][w] = Math.max(values[i - 1] + dp[i - 1][w - weights[i - 1]], dp[i - 1][w]);
                    } else { // Current item cannot fit
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }
            return dp[n][W];
        }

        public static void main(String[] args) {
            Knapsack01 ks = new Knapsack01();
            int[] values = {60, 100, 120};
            int[] weights = {10, 20, 30};
            int W = 50;
            int n = values.length;

            System.out.println("Max value for Knapsack: " + ks.knapSack(W, weights, values, n)); // 220 (100 from item 2, 120 from item 3)
        }
    }
    ```

### Week 10: System Design Fundamentals Solutions

#### Topic 1: High-Level System Design Concepts

**Interview Questions & Solutions:**

1.  **What is the difference between vertical and horizontal scaling?**
    *   **Solution:**
        *   **Vertical Scaling (Scaling Up):** Increasing the resources (CPU, RAM, storage) of a single server. You make the existing server more powerful.
            *   **Pros:** Simpler to implement.
            *   **Cons:** Limited by the maximum capacity of a single machine. Single point of failure. Can be expensive.
        *   **Horizontal Scaling (Scaling Out):** Adding more servers (machines) to a system to distribute the load.
            *   **Pros:** Can scale almost infinitely. Offers high availability and fault tolerance (if one server fails, others can take over).
            *   **Cons:** More complex to implement (requires distributed system design, load balancing, data synchronization, etc.).

2.  **Explain the purpose of a Load Balancer.**
    *   **Solution:** A Load Balancer distributes incoming network traffic across multiple servers (or resources, like virtual machines or containers).
    *   **Purposes:**
        *   **Distributes Workload:** Prevents any single server from becoming a bottleneck, ensuring optimal resource utilization.
        *   **Increases Availability:** If a server fails, the load balancer detects it and redirects traffic to healthy servers, improving fault tolerance.
        *   **Improves Performance:** By distributing requests, it reduces response times and increases throughput.
        *   **Enables Scalability:** Facilitates horizontal scaling by easily integrating new servers into the resource pool.
        *   **Health Checks:** Monitors the health of backend servers and takes unhealthy servers out of rotation.

3.  **When would you use a SQL database vs. a NoSQL database?**
    *   **Solution:**
        *   **SQL (Relational) Databases (e.g., PostgreSQL, MySQL, Oracle):**
            *   **Use when:**
                *   Data has a **clear, fixed, and well-defined schema**.
                *   Data relationships are **complex and require strong ACID (Atomicity, Consistency, Isolation, Durability) guarantees** (e.g., financial transactions).
                *   **Complex queries and joins** are frequently needed.
                *   Data consistency and integrity are paramount.
            *   **Examples:** E-commerce platforms (orders, users), banking systems, traditional business applications.
        *   **NoSQL (Non-Relational) Databases (e.g., MongoDB, Cassandra, Redis, DynamoDB):**
            *   **Use when:**
                *   Data is **unstructured, semi-structured, or schema-less** (flexible schema).
                *   Need for **extreme horizontal scalability** and high availability for large volumes of data.
                *   Performance is critical for specific data access patterns (e.g., key-value lookups).
                *   Data consistency can be relaxed (eventual consistency often acceptable).
                *   **Examples:** Real-time analytics, content management systems, social networks, IoT data, caching.

4.  **What are the benefits of caching? Where can caching be implemented?**
    *   **Solution:**
        *   **Benefits of Caching:**
            *   **Improved Performance:** Reduces latency by serving frequently requested data from a faster, closer store.
            *   **Reduced Database Load:** Lessens the strain on backend databases or APIs, allowing them to handle more write operations or complex queries.
            *   **Increased Throughput:** Systems can serve more requests per second.
            *   **Cost Savings:** Reduces operational costs by minimizing resource usage (e.g., database queries, network bandwidth).
        *   **Where Caching can be Implemented:**
            *   **Client-Side (Browser Cache):** Stores static assets (images, CSS, JS) to avoid re-downloading.
            *   **CDN (Content Delivery Network):** Caches static and sometimes dynamic content geographically closer to users.
            *   **DNS Cache:** Stores domain name resolutions.
            *   **Server-Side (Application-Level Cache):** Within the application code (e.g., using `HashMap` or dedicated libraries like Caffeine).
            *   **Distributed Cache (e.g., Redis, Memcached):** Separate servers dedicated to caching, shared across multiple application instances.
            *   **Database Cache:** Built-in caching mechanisms within database systems (e.g., query cache, result set cache).
            *   **OS/Hardware Cache:** CPU caches, disk caches.

5.  **What are the key principles of `RESTful APIs`?**
    *   **Solution:** REST (Representational State Transfer) is an architectural style for distributed hypermedia systems. Its key principles, often referred to as constraints, are:
        1.  **Client-Server Architecture:** Separation of concerns between client (UI) and server (data storage, processing).
        2.  **Statelessness:** Each request from the client to the server must contain all the information needed to understand the request. The server should not store any client context between requests.
        3.  **Cacheability:** Responses must explicitly or implicitly define themselves as cacheable or non-cacheable to prevent clients from reusing stale or inappropriate data.
        4.  **Uniform Interface:** Simplifies overall system architecture. Achieved through:
            *   **Resource Identification:** Resources (e.g., users, products) are identified by URIs.
            *   **Resource Manipulation through Representations:** Clients manipulate resources using representations (e.g., JSON, XML) sent in the request body.
            *   **Self-Descriptive Messages:** Messages include enough information to describe how to process the message.
            *   **Hypermedia as the Engine of Application State (HATEOAS):** Resources should contain links to related resources, guiding the client through the application's state.
        5.  **Layered System:** A client cannot ordinarily tell whether it is connected directly to the end server or to an intermediary along the way (e.g., load balancer, proxy).
        6.  **(Optional) Code-On-Demand:** Servers can temporarily extend or customize the functionality of a client by transferring executable code (e.g., JavaScript).

**Practice Questions & Solutions:**

1.  **Design a URL shortening service (high-level components).**
    *   **Solution:**
        *   **Components:**
            *   **Client (Browser/App):** User inputs a long URL.
            *   **API Gateway/Load Balancer:** Entry point for all requests. Distributes traffic.
            *   **Shortening Service (Microservice):**
                *   Generates a unique short code (e.g., 6-8 alphanumeric characters). Checks for collisions.
                *   Stores the mapping: `short_code -> long_URL` in a database.
                *   Returns the short URL to the client.
            *   **Redirection Service (Microservice):**
                *   Receives requests for short URLs (e.g., `tinyurl.com/abcde`).
                *   Looks up the `short_code` in the database.
                *   Performs a 301 (Permanent) or 302 (Temporary) HTTP redirect to the `long_URL`.
            *   **Database:**
                *   NoSQL (e.g., Cassandra, DynamoDB) for high write/read throughput, eventual consistency, and horizontal scalability for key-value lookups (`short_code -> long_URL`).
                *   Could also use SQL for stronger consistency, but might need sharding.
            *   **Caching Layer (e.g., Redis, Memcached):** Caches popular `short_code -> long_URL` mappings to reduce database load and improve redirection speed.
            *   **Analytics Service (Optional):** Tracks clicks, geographic data, etc. (asynchronous processing, likely uses message queue).
            *   **Message Queue (e.g., Kafka, RabbitMQ):** For asynchronous tasks like analytics updates or generating unique IDs (if using pre-generated IDs).
        *   **Flow:**
            1.  User enters `long_URL`.
            2.  Request goes to Load Balancer -> Shortening Service.
            3.  Shortening Service generates `short_code`, stores in DB/Cache, returns `short_URL`.
            4.  User clicks `short_URL`.
            5.  Request goes to Load Balancer -> Redirection Service.
            6.  Redirection Service looks up `short_code` (Cache first, then DB).
            7.  Redirection Service sends 301/302 redirect.
            8.  Browser redirects to `long_URL`.

2.  **Explain how you would handle traffic spikes for a web application.**
    *   **Solution:** Handling traffic spikes requires a multi-faceted approach focusing on scaling, redundancy, and performance optimization at various layers.
        1.  **Load Balancing:** Distribute incoming traffic across multiple web servers. Health checks ensure traffic only goes to healthy servers.
        2.  **Horizontal Scaling (Auto-Scaling):** Automatically add/remove web servers based on demand (e.g., CPU utilization, request queue length). Cloud providers (AWS Auto Scaling, Azure VM Scale Sets) make this straightforward.
        3.  **Caching:** Implement aggressive caching at multiple layers:
            *   **CDN:** Cache static content (images, CSS, JS) and potentially dynamic content closer to users.
            *   **Distributed Cache (e.g., Redis):** Cache frequently accessed dynamic data and query results.
            *   **Browser Cache:** Leverage HTTP caching headers for client-side caching.
        4.  **Database Optimization & Scaling:**
            *   **Read Replicas:** Route read traffic to multiple read-only database replicas.
            *   **Sharding/Partitioning:** Distribute data across multiple database instances for massive scale.
            *   **Connection Pooling:** Efficiently manage database connections.
            *   **Query Optimization:** Ensure queries are performant and indexed.
        5.  **Message Queues/Asynchronous Processing:** Decouple components by using message queues (e.g., Kafka, RabbitMQ) for non-critical, long-running tasks (e.g., email sending, report generation, processing user activity). This prevents synchronous requests from blocking the main application flow.
        6.  **Rate Limiting:** Protect backend services from being overwhelmed by too many requests from a single user or IP address.
        7.  **Content Compression (Gzip) & Minification:** Reduce the size of assets transmitted over the network.
        8.  **Monitoring & Alerting:** Set up robust monitoring (CPU, memory, network I/O, latency, error rates) to detect spikes early and trigger auto-scaling or manual intervention.
        9.  **Graceful Degradation:** In extreme overload, prioritize critical functionalities and gracefully degrade non-essential features (e.g., disable comments, less accurate recommendations) to keep the core service available.

3.  **Describe how a typical web request flows from browser to server.**
    *   **Solution:**
        1.  **User Enters URL/Clicks Link:** The browser initiates a request (e.g., `GET /index.html`).
        2.  **DNS Lookup:** The browser checks its cache for the IP address corresponding to the domain name. If not found, it queries a DNS resolver, which recursively finds the authoritative DNS server for the domain, returning the IP address (e.g., `example.com` resolves to `192.0.2.1`).
        3.  **TCP Connection (Handshake):** The browser opens a TCP connection to the server's IP address (typically port 80 for HTTP, 443 for HTTPS). A 3-way handshake (`SYN`, `SYN-ACK`, `ACK`) establishes the connection.
        4.  **SSL/TLS Handshake (for HTTPS):** If HTTPS, a TLS handshake occurs to establish a secure, encrypted connection. This involves exchanging certificates and generating session keys.
        5.  **HTTP Request:** The browser sends an HTTP request (containing method, path, headers, and potentially body) over the established TCP/TLS connection to the server. This request might first hit a **CDN** if the content is cached there.
        6.  **Load Balancer:** If multiple servers are present, the request often first arrives at a **Load Balancer**, which then forwards it to an available, healthy backend web server based on its load balancing algorithm (e.g., Round Robin, Least Connections).
        7.  **Web Server/Application Server:**
            *   The web server (e.g., Nginx, Apache) receives the request.
            *   It might serve static files directly from its filesystem (if a CDN didn't already).
            *   For dynamic content, it passes the request to an **Application Server** (e.g., Tomcat for Java, Gunicorn for Python, Node.js runtime).
            *   The Application Server processes the request, potentially interacting with:
                *   **Caching Layer:** Checks if the requested data is in a cache (e.g., Redis).
                *   **Database:** Executes queries to retrieve or store data.
                *   **Other Microservices:** Makes calls to other internal services.
        8.  **HTTP Response:** The Application Server generates an HTTP response (containing status code, headers, and body—e.g., HTML, JSON). This response is sent back through the web server and load balancer.
        9.  **Browser Renders Content:** The browser receives the response. If it's HTML, it parses the HTML, fetches any linked resources (CSS, JavaScript, images) in subsequent requests (repeating steps 2-8 for each resource), and then renders the webpage for the user.