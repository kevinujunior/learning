# Java Multithreading and Concurrency: SDE-2 Interview Guide

This comprehensive guide covers Java Multithreading and Concurrency, tailored for SDE-2 interviews at top tech companies. It aims to provide strong conceptual clarity, practical coding ability, and confidence in handling interview questions.

## 1. Process vs. Thread (Java Context)

*   **Process**: A process is an independent execution unit that has its own memory space, resources, and execution environment. Each Java application runs as a single process.
    *   **Why**: Provides isolation between applications. A crash in one process typically doesn't affect others.
    *   **Interview Note**: Think of a process as a running instance of a program.
*   **Thread**: A thread is the smallest unit of execution within a process. Multiple threads can exist within a single process and share the process's memory space and resources.
    *   **Why**: Enables concurrent execution within an application, improving responsiveness and utilizing multi-core processors.
    *   **Interview Note**: Threads are "lightweight processes" in the context of resource usage compared to processes.

## 2. Thread Lifecycle and States

A thread can be in one of six states:

*   **NEW**: A thread that has not yet started.
    *   **When**: When you create a `new Thread()` object.
*   **RUNNABLE**: A thread executing in the Java Virtual Machine (JVM). It might be currently running or waiting for its turn on the CPU.
    *   **When**: After `thread.start()` is called. The OS scheduler decides when it actually runs.
*   **BLOCKED**: A thread that is blocked waiting for a monitor lock (e.g., trying to enter a `synchronized` block/method when another thread holds the lock).
    *   **When**: When a thread tries to acquire a lock that is already held.
*   **WAITING**: A thread that is waiting indefinitely for another thread to perform a particular action.
    *   **When**: Calling `Object.wait()`, `Thread.join()` (without timeout), `LockSupport.park()`.
*   **TIMED\_WAITING**: A thread that is waiting for another thread to perform an action for a specified waiting time.
    *   **When**: Calling `Thread.sleep(long millis)`, `Object.wait(long millis)`, `Thread.join(long millis)`, `Lock.tryLock(long timeout, TimeUnit unit)`, `Condition.await(long time, TimeUnit unit)`.
*   **TERMINATED**: A thread that has exited.
    *   **When**: The `run()` method completes its execution.

```java
// Example to illustrate thread states (conceptual, actual state changes happen fast)
public class ThreadStatesDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000); // TIMED_WAITING
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Thread 1 finished.");
        }, "Thread-1");

        System.out.println("State after creation: " + thread1.getState()); // NEW

        thread1.start();
        System.out.println("State after start: " + thread1.getState());   // RUNNABLE (or potentially TIMED_WAITING if sleep starts immediately)

        // Give some time for thread1 to enter TIMED_WAITING state
        Thread.sleep(100);
        System.out.println("State during sleep: " + thread1.getState()); // TIMED_WAITING

        Thread thread2 = new Thread(() -> {
            synchronized (ThreadStatesDemo.class) {
                while (true) { // Simulate blocking indefinitely
                    // Thread will be BLOCKED if another thread holds the lock
                }
            }
        }, "Thread-2");

        Thread thread3 = new Thread(() -> {
            synchronized (ThreadStatesDemo.class) {
                try {
                    ThreadStatesDemo.class.wait(); // WAITING
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }, "Thread-3");

        synchronized (ThreadStatesDemo.class) { // Main thread holds the lock
            thread2.start();
            thread3.start();
            Thread.sleep(100); // Give time for threads to try acquiring lock/wait
            System.out.println("State of Thread 2 (BLOCKED): " + thread2.getState()); // BLOCKED
            System.out.println("State of Thread 3 (WAITING): " + thread3.getState()); // WAITING
        } // Main thread releases the lock

        thread1.join(); // Main thread waits for thread1 to terminate
        System.out.println("State after Thread 1 finished: " + thread1.getState()); // TERMINATED
    }
}
```

## 3. Creating Threads

There are three primary ways to create threads in Java:

1.  **Extending `Thread` class**:
    *   **How**: Create a class that extends `java.lang.Thread` and override its `run()` method.
    *   **When**: Simple cases, but limits your class from extending other classes (Java doesn't support multiple inheritance).
    *   **Code Example**:
        ```java
        class MyThread extends Thread {
            @Override
            public void run() {
                System.out.println("MyThread running in: " + Thread.currentThread().getName());
            }
        }

        public class ThreadCreationDemo {
            public static void main(String[] args) {
                MyThread t1 = new MyThread();
                t1.start(); // Invokes run() in a new thread
            }
        }
        ```
2.  **Implementing `Runnable` interface**:
    *   **How**: Create a class that implements `java.lang.Runnable` and override its `run()` method. Pass an instance of this class to a `Thread` constructor.
    *   **When**: Preferred approach as it allows the class to extend other classes. Promotes separation of concerns (task vs. thread).
    *   **Code Example**:
        ```java
        class MyRunnable implements Runnable {
            @Override
            public void run() {
                System.out.println("MyRunnable running in: " + Thread.currentThread().getName());
            }
        }

        public class RunnableCreationDemo {
            public static void main(String[] args) {
                MyRunnable r1 = new MyRunnable();
                Thread t2 = new Thread(r1);
                t2.start();
            }
        }
        ```
3.  **Using Lambda Expressions (with `Runnable`)**:
    *   **How**: Since `Runnable` is a functional interface, you can use lambda expressions to provide its implementation inline.
    *   **When**: Concise way for short, single-use tasks.
    *   **Code Example**:
        ```java
        public class LambdaThreadCreationDemo {
            public static void main(String[] args) {
                Thread t3 = new Thread(() -> {
                    System.out.println("Lambda thread running in: " + Thread.currentThread().getName());
                });
                t3.start();
            }
        }
        ```
    *   **Interview Note**: This is the most common and preferred way for simple tasks in modern Java.

## 4. `start()` vs. `run()`

*   **`start()` method**:
    *   **What it does**: Creates a new thread of execution and then calls the `run()` method in that newly created thread. It handles all the low-level OS operations to set up the new thread.
    *   **When to use**: Always call `start()` to begin a thread's execution.
    *   **Interview Trap**: Calling `start()` multiple times on the same `Thread` object will throw an `IllegalThreadStateException`.
*   **`run()` method**:
    *   **What it does**: Contains the actual code that will be executed by the thread.
    *   **When to use**: Never call `run()` directly yourself if you want to create a new thread. If you call `run()` directly, the code will execute in the *current thread* (e.g., the `main` thread), not a new thread.
    *   **Interview Note**: The `run()` method is what the JVM's new thread will execute after `start()` is called.

```java
public class StartRunDifference {
    public static void main(String[] args) {
        Runnable myTask = () -> {
            System.out.println("Task running in thread: " + Thread.currentThread().getName());
        };

        System.out.println("Main thread: " + Thread.currentThread().getName());

        // CORRECT: Using start() to create a new thread
        Thread thread1 = new Thread(myTask, "Worker-Thread-1");
        thread1.start(); // Output will show "Worker-Thread-1"

        // INCORRECT: Calling run() directly
        Thread thread2 = new Thread(myTask, "Worker-Thread-2");
        thread2.run();   // Output will show "main" (or the current thread name)

        // Proof: thread2 is still in NEW state because start() was never called
        System.out.println("Is Worker-Thread-2 alive? " + thread2.isAlive());
    }
}
```

## 5. Thread Naming, Priority, Daemon Threads

*   **Thread Naming**:
    *   **Methods**:
        *   `Thread(Runnable target, String name)`: Constructor to set the name.
        *   `Thread.setName(String name)`: Sets the name of the thread.
        *   `Thread.getName()`: Retrieves the name of the thread.
    *   **Why**: Crucial for debugging and logging. Stack traces and logs are much easier to understand when threads have meaningful names.
    *   **Best Practice**: Always name your threads, especially in complex applications.
    *   **Code Example**: See previous examples.
*   **Thread Priority**:
    *   **Constants**: `Thread.MIN_PRIORITY` (1), `Thread.NORM_PRIORITY` (5), `Thread.MAX_PRIORITY` (10). Default is `NORM_PRIORITY`.
    *   **Methods**:
        *   `Thread.setPriority(int newPriority)`: Sets the priority.
        *   `Thread.getPriority()`: Gets the priority.
    *   **Why**: Hints to the OS scheduler about the relative importance of threads. Higher priority threads *might* get preference.
    *   **Limitations**:
        *   Highly platform-dependent. JVM maps Java priorities to OS priorities, which behave differently.
        *   Should generally be avoided for critical logic, as it doesn't guarantee execution order or performance. Never rely on priorities for correctness.
    *   **Interview Note**: Mention that priorities are hints, not guarantees.
*   **Daemon Threads**:
    *   **What they are**: Background threads that provide services to user threads. The JVM exits when only daemon threads remain.
    *   **Methods**:
        *   `Thread.setDaemon(boolean on)`: Sets the thread as daemon (`true`) or user thread (`false`). Must be called *before* `start()`.
        *   `Thread.isDaemon()`: Checks if the thread is a daemon.
    *   **Why**: Useful for background tasks like garbage collection, logging, or monitoring, which don't prevent the application from exiting.
    *   **Interview Note**: If a user thread spawns a new thread, by default, it will be a user thread. If a daemon thread spawns a new thread, it will by default be a daemon thread.
    *   **Code Example**:
        ```java
        public class DaemonThreadDemo {
            public static void main(String[] args) throws InterruptedException {
                Thread userThread = new Thread(() -> {
                    for (int i = 0; i < 5; i++) {
                        System.out.println("User thread working...");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("User thread finished.");
                }, "User-Thread");

                Thread daemonThread = new Thread(() -> {
                    while (true) {
                        System.out.println("Daemon thread providing background service...");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                    System.out.println("Daemon thread finished (shouldn't print if JVM exits before interruption).");
                }, "Daemon-Thread");

                daemonThread.setDaemon(true); // MUST be called before start()

                userThread.start();
                daemonThread.start();

                // Main thread waits for userThread to finish
                userThread.join();
                System.out.println("Main thread exiting. JVM will terminate as only daemon threads remain.");
                // daemonThread will be abruptly terminated when the JVM exits
            }
        }
        ```

## 6. Important `Thread` Methods

### `sleep(long millis)` and `sleep(long millis, int nanos)`

*   **Purpose**: Causes the currently executing thread to temporarily cease execution for a specified period.
*   **Behavior**: The thread goes into the `TIMED_WAITING` state. It does *not* release any monitor locks it holds.
*   **Interruption**: Throws `InterruptedException` if another thread interrupts it while sleeping.
*   **When to use**: To pause execution for a short period, simulate work, or control timing.
*   **Example**:
    ```java
    public class SleepDemo {
        public static void main(String[] args) throws InterruptedException {
            System.out.println("Before sleep...");
            Thread.sleep(2000); // Current thread (main) sleeps for 2 seconds
            System.out.println("After sleep...");

            Thread worker = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " starting work...");
                try {
                    Thread.sleep(1500); // Worker thread sleeps
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " was interrupted while sleeping.");
                    Thread.currentThread().interrupt(); // Re-interrupt
                }
                System.out.println(Thread.currentThread().getName() + " finished work.");
            }, "Worker");

            worker.start();
            Thread.sleep(500); // Main thread sleeps for a bit
            worker.interrupt(); // Interrupt the worker thread
            worker.join(); // Wait for worker to finish
        }
    }
    ```
*   **`wait()` vs. `sleep()` (Interview Critical)**:
    *   **`sleep()`**: Static method of `Thread`. Puts the *current thread* to sleep. Does *not* release any acquired locks. Typically used for a timed pause.
    *   **`wait()`**: Instance method of `Object`. Causes the *current thread* to wait until another thread invokes `notify()` or `notifyAll()` on *the same object*. **Releases the intrinsic lock** on the object it was called on. Must be called from a `synchronized` block/method. Used for inter-thread communication.

### `yield()`

*   **Purpose**: A hint to the scheduler that the current thread is willing to yield its current use of a processor. The scheduler is free to ignore this hint.
*   **Behavior**: The thread moves from `RUNNING` to `RUNNABLE` state. It might immediately be scheduled again. Does *not* release locks.
*   **When to use**: Rarely used. Might be helpful in debugging or highly specific performance tuning scenarios, but generally not for production code as its behavior is unpredictable and platform-dependent.
*   **Interview Note**: Emphasize it's a *hint*, not a guarantee.

### `join()` and `join(long millis)`

*   **Purpose**: Waits for a thread to die (terminate).
*   **Behavior**: The calling thread (`main` in most examples) will block until the thread it's joining (`t.join()`) finishes execution.
*   **Interruption**: Throws `InterruptedException` if the calling thread is interrupted while waiting.
*   **When to use**: To ensure a specific order of execution, or to wait for a background task to complete before proceeding.
*   **Example**:
    ```java
    public class JoinDemo {
        public static void main(String[] args) throws InterruptedException {
            Thread worker1 = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " starting.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " finished.");
            }, "Worker-1");

            Thread worker2 = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " starting.");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(Thread.currentThread().getName() + " finished.");
            }, "Worker-2");

            worker1.start();
            worker2.start();

            System.out.println("Main thread waiting for Worker-1 to finish...");
            worker1.join(); // Main thread waits for worker1

            System.out.println("Main thread waiting for Worker-2 to finish...");
            worker2.join(500); // Main thread waits for worker2 for max 500ms

            if (worker2.isAlive()) {
                System.out.println("Worker-2 is still alive after 500ms.");
            } else {
                System.out.println("Worker-2 finished within 500ms.");
            }

            System.out.println("All workers joined. Main thread exiting.");
        }
    }
    ```

### `interrupt()` and Interruption Handling

*   **Purpose**: To signal a thread that it should stop what it's doing and terminate, or at least acknowledge the interruption. It does *not* forcefully stop a thread.
*   **Methods**:
    *   `Thread.interrupt()`: Sets the thread's interruption status flag to `true`. If the thread is in a blocking operation (like `sleep()`, `wait()`, `join()`), it will throw an `InterruptedException` and clear the interrupt flag (setting it back to `false`).
    *   `Thread.isInterrupted()`: Tests whether the current thread has been interrupted. (Non-static method)
    *   `Thread.interrupted()`: Tests whether the *current thread* has been interrupted. The interrupt status of the thread is *cleared* by this method (set to `false`). (Static method)
*   **Interruption Handling**:
    1.  **Catch `InterruptedException`**: When a blocking method throws it, you must handle it.
    2.  **Restore Interrupt Status**: Best practice is to call `Thread.currentThread().interrupt()` inside the `catch` block to re-interrupt the current thread. This allows higher-level code (callers) to know that an interruption occurred.
    3.  **Clean up and Exit**: If interruption means the task should stop, clean up resources and exit the `run()` method gracefully.
*   **When to use**: For graceful termination of long-running tasks or responsive shutdown mechanisms.
*   **Interview Note**: Explain that `interrupt()` is a cooperative mechanism. The interrupted thread must explicitly check and respond to the interrupt status. Never ignore `InterruptedException`.
*   **Code Example**:
    ```java
    public class InterruptionDemo {
        public static void main(String[] args) throws InterruptedException {
            Thread worker = new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " starting loop.");
                long startTime = System.currentTimeMillis();
                try {
                    while (!Thread.currentThread().isInterrupted() && (System.currentTimeMillis() - startTime < 5000)) {
                        System.out.println(Thread.currentThread().getName() + " working...");
                        Thread.sleep(500); // This might throw InterruptedException
                    }
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " caught InterruptedException.");
                    // Restore the interrupted status
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println(Thread.currentThread().getName() + " cleaning up resources.");
                }
                System.out.println(Thread.currentThread().getName() + " gracefully exiting.");
            }, "Interruptible-Worker");

            worker.start();
            Thread.sleep(2000); // Let worker run for 2 seconds
            System.out.println("Main thread interrupting " + worker.getName());
            worker.interrupt(); // Signal the worker to stop
            worker.join(); // Wait for worker to finish
            System.out.println("Main thread exiting.");
        }
    }
    ```

## 7. Synchronization Fundamentals

*   **Problem**: When multiple threads access shared mutable data concurrently, it can lead to inconsistent results. This is known as a **race condition**.
*   **Solution**: Synchronization mechanisms ensure that only one thread can access a critical section (shared resource) at a time.

### `synchronized` keyword (Method & Block Level)

The `synchronized` keyword provides an intrinsic lock (also known as a monitor lock or mutex) for a block of code or a method.

*   **`synchronized` method**:
    *   **How**: `public synchronized void myMethod() { ... }`
    *   **Behavior**: When a `synchronized` instance method is called, the calling thread acquires the intrinsic lock of the *object* instance. No other thread can call any other `synchronized` instance method on the *same object* until the lock is released.
    *   **Static `synchronized` method**:
        *   **How**: `public static synchronized void myStaticMethod() { ... }`
        *   **Behavior**: When a `synchronized` static method is called, the calling thread acquires the intrinsic lock of the *Class object* (e.g., `MyClass.class`). No other thread can call any other `synchronized` static method on that *class* until the lock is released.
*   **`synchronized` block**:
    *   **How**: `synchronized (this) { ... }` or `synchronized (myObject) { ... }`
    *   **Behavior**: The thread acquires the intrinsic lock of the object specified in parentheses (`this` refers to the current instance, `myObject` to any object). This allows for finer-grained control, synchronizing only the critical section rather than the entire method.
    *   **When to use**: When you need to protect only a part of a method or when you need to synchronize on an object other than `this` or the `Class` object.
*   **Rules for `synchronized`**:
    *   Only one thread can hold an intrinsic lock at a time.
    *   When a thread exits a `synchronized` block/method, it releases the lock.
    *   Releases the lock automatically if an exception occurs within the `synchronized` block/method.
    *   Locks are reentrant: a thread can acquire a lock it already holds without deadlocking itself.
    *   `synchronized` ensures both **mutual exclusion** (only one thread at a time) and **visibility** (all changes made inside a `synchronized` block are visible to other threads when they subsequently acquire the same lock).

*   **Code Example**:
    ```java
    class Counter {
        private int count = 0;

        // Synchronized method
        public synchronized void incrementMethod() {
            count++;
            System.out.println(Thread.currentThread().getName() + " - Method Increment: " + count);
        }

        // Synchronized block on 'this'
        public void incrementBlock() {
            // Can have non-synchronized code here
            synchronized (this) { // Lock on the current Counter instance
                count++;
                System.out.println(Thread.currentThread().getName() + " - Block Increment: " + count);
            }
        }

        // Synchronized block on a separate lock object
        private final Object lock = new Object();
        public void incrementBlockWithSeparateLock() {
            synchronized (lock) { // Lock on a dedicated lock object
                count++;
                System.out.println(Thread.currentThread().getName() + " - Separate Lock Increment: " + count);
            }
        }

        public int getCount() {
            return count;
        }
    }

    public class SynchronizedDemo {
        public static void main(String[] args) throws InterruptedException {
            Counter counter = new Counter();

            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    counter.incrementMethod(); // Try with incrementBlock() or incrementBlockWithSeparateLock()
                }
            };

            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("Final count: " + counter.getCount()); // Should be 2000 if synchronized correctly
        }
    }
    ```

### Object Monitor, Intrinsic Lock

*   **Concept**: Every object in Java (including `Class` objects) intrinsically has an associated monitor (or lock). When you use the `synchronized` keyword, you are using this object's monitor.
*   **How it works**:
    1.  When a thread enters a `synchronized` block or method, it attempts to acquire the monitor lock of the specified object.
    2.  If the lock is available, the thread acquires it and enters the critical section.
    3.  If the lock is already held by another thread, the current thread becomes `BLOCKED` until the lock is released.
    4.  When the thread exits the `synchronized` block/method (normally or via exception), it releases the lock.

### `wait()`, `notify()`, `notifyAll()`

These methods are used for inter-thread communication and must be called from within a `synchronized` block/method on the *same lock object* that they are called upon. They are methods of `java.lang.Object`.

*   **`wait()`**:
    *   **Purpose**: Causes the current thread to wait until another thread invokes `notify()` or `notifyAll()` on *this object*, or a specified amount of time elapses.
    *   **Behavior**: The thread releases the intrinsic lock on the object and enters the `WAITING` (or `TIMED_WAITING`) state. It reacquires the lock before it can resume execution.
    *   **Rule**: Always call `wait()` in a loop to check the condition it's waiting for (e.g., `while (condition) { object.wait(); }`). This protects against "spurious wakeups" (waking up without `notify()` being called) and ensures the condition is truly met.
*   **`notify()`**:
    *   **Purpose**: Wakes up a single thread that is waiting on *this object's* monitor. The choice of which thread to wake is arbitrary and implementation-dependent.
    *   **Behavior**: The woken thread transitions from `WAITING` to `BLOCKED` (it tries to reacquire the lock). It doesn't immediately run; it must reacquire the lock.
*   **`notifyAll()`**:
    *   **Purpose**: Wakes up *all* threads that are waiting on *this object's* monitor.
    *   **Behavior**: All woken threads transition from `WAITING` to `BLOCKED` and compete to reacquire the lock.
*   **When to use**: For implementing producer-consumer patterns, guarded blocks, or any scenario where threads need to wait for a specific condition to become true, and be notified by another thread when it is.
*   **Interview Pitfalls**:
    *   Calling `wait()`, `notify()`, or `notifyAll()` outside a `synchronized` block/method will throw an `IllegalMonitorStateException`.
    *   Calling these methods on different lock objects than the one synchronized on will lead to `IllegalMonitorStateException`.
    *   Not using `wait()` in a loop can lead to incorrect behavior due to spurious wakeups or the condition not being met when awakened.
*   **Code Example (Producer-Consumer)**:
    ```java
    import java.util.LinkedList;
    import java.util.Queue;

    class ProducerConsumer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int CAPACITY = 5;

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                synchronized (this) { // Lock on the ProducerConsumer instance
                    while (buffer.size() == CAPACITY) {
                        System.out.println("Buffer is full. Producer waiting.");
                        wait(); // Releases lock and waits
                    }
                    buffer.add(value);
                    System.out.println("Produced: " + value);
                    value++;
                    notifyAll(); // Notifies waiting consumers
                    Thread.sleep(500); // Simulate work
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                synchronized (this) { // Lock on the ProducerConsumer instance
                    while (buffer.isEmpty()) {
                        System.out.println("Buffer is empty. Consumer waiting.");
                        wait(); // Releases lock and waits
                    }
                    int val = buffer.remove();
                    System.out.println("Consumed: " + val);
                    notifyAll(); // Notifies waiting producers
                    Thread.sleep(1000); // Simulate work
                }
            }
        }
    }

    public class WaitNotifyDemo {
        public static void main(String[] args) {
            ProducerConsumer pc = new ProducerConsumer();

            Thread producerThread = new Thread(() -> {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer");

            Thread consumerThread = new Thread(() -> {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer");

            producerThread.start();
            consumerThread.start();
        }
    }
    ```

## 8. Deadlock, Livelock, Starvation

These are common concurrency problems.

*   **Deadlock**:
    *   **Definition**: A situation where two or more threads are blocked indefinitely, each waiting for the other to release a resource.
    *   **Conditions for Deadlock (Coffman Conditions)**: All four must hold for a deadlock to occur:
        1.  **Mutual Exclusion**: At least one resource must be held in a non-sharable mode.
        2.  **Hold and Wait**: A thread holding at least one resource is waiting to acquire additional resources held by other threads.
        3.  **No Preemption**: Resources cannot be forcibly taken from a thread; they must be released voluntarily.
        4.  **Circular Wait**: A set of threads `T1, T2, ..., Tn` exists such that `T1` is waiting for a resource held by `T2`, `T2` for `T3`, ..., and `Tn` for `T1`.
    *   **Example**: Two threads, two locks, each trying to acquire the other's lock.
        ```java
        public class DeadlockDemo {
            private static final Object lock1 = new Object();
            private static final Object lock2 = new Object();

            public static void main(String[] args) {
                Thread t1 = new Thread(() -> {
                    synchronized (lock1) {
                        System.out.println("Thread 1: Acquired lock1");
                        try {
                            Thread.sleep(100); // Simulate some work
                        } catch (InterruptedException ignored) {}
                        System.out.println("Thread 1: Waiting for lock2");
                        synchronized (lock2) { // T1 tries to acquire lock2
                            System.out.println("Thread 1: Acquired lock2");
                        }
                    }
                }, "Thread-1");

                Thread t2 = new Thread(() -> {
                    synchronized (lock2) { // T2 acquires lock2 first
                        System.out.println("Thread 2: Acquired lock2");
                        try {
                            Thread.sleep(100); // Simulate some work
                        } catch (InterruptedException ignored) {}
                        System.out.println("Thread 2: Waiting for lock1");
                        synchronized (lock1) { // T2 tries to acquire lock1
                            System.out.println("Thread 2: Acquired lock1");
                        }
                    }
                }, "Thread-2");

                t1.start();
                t2.start();
            }
        }
        ```
    *   **How to avoid**:
        *   **Order Locks**: Establish a global ordering of locks and always acquire them in that order. (Breaks Circular Wait)
        *   **Timeout on Locks**: Use `tryLock(long timeout, TimeUnit unit)` with `Lock` interface to attempt to acquire a lock for a limited time. (Breaks Hold and Wait)
        *   **Avoid Nested Locks**: Minimize acquiring locks within other locks.
        *   **Detect and Recover**: Monitor for deadlocks and restart involved threads/processes (more complex).
*   **Livelock**:
    *   **Definition**: Threads are not blocked but are continuously reacting to each other's actions, preventing any actual progress. They are busy trying to resolve a conflict but keep re-entering the problematic state.
    *   **Example**: Two people trying to pass each other in a narrow hallway, both stepping aside to the same side simultaneously.
    *   **How to avoid**: Introduce randomness or priority in retries.
*   **Starvation**:
    *   **Definition**: A thread is continuously denied access to a shared resource, even though the resource is available, because other threads are always given preference.
    *   **Example**:
        *   A low-priority thread never gets CPU time if high-priority threads are always ready to run.
        *   A thread might never acquire a lock because other threads keep acquiring and releasing it without letting the waiting thread get a turn (e.g., unfair lock implementations).
    *   **How to avoid**:
        *   Fairness in locks (e.g., `ReentrantLock(true)`).
        *   Careful use of thread priorities.
        *   Ensuring all threads eventually get a chance to execute.

## 9. Visibility Issues and Race Conditions

*   **Race Condition**:
    *   **Definition**: Occurs when multiple threads access shared mutable data, and the final outcome depends on the non-deterministic relative timing of their execution.
    *   **Example**: Incrementing a shared counter without synchronization. Two threads might read the same value, increment it, and write it back, leading to lost updates.
    *   **Code Example**: See `SynchronizedDemo` if `incrementMethod` was *not* `synchronized`. `final count` would often be less than 2000.
*   **Visibility Issue**:
    *   **Definition**: Occurs when changes made by one thread to shared data are not immediately visible to other threads. This is due to CPU caches and compiler optimizations.
    *   **Example**: A loop condition modified by one thread but never observed by another, causing an infinite loop.
    *   **Root Cause**: Java Memory Model (JMM) allows threads to cache variables. Without proper synchronization, a thread's local cache might not be synchronized with main memory, and other threads read stale values.

## 10. `volatile` keyword

*   **Purpose**: Ensures that changes to a variable are always written to main memory and read from main memory. It guarantees visibility for a single variable across threads.
*   **Behavior**:
    1.  **Writes**: When a thread writes to a `volatile` variable, the value is immediately written from the thread's local cache to main memory.
    2.  **Reads**: When a thread reads a `volatile` variable, the value is always read from main memory, discarding any locally cached value.
*   **Limitations**:
    *   Only guarantees visibility, *not* atomicity. It cannot prevent race conditions if the operation on the variable is not atomic (e.g., `count++` is read-modify-write, which is three separate operations).
    *   Cannot replace `synchronized` for operations involving multiple variables or complex critical sections.
*   **When to use**:
    *   **Flag variables**: To signal state changes between threads (e.g., a `stop` flag).
    *   **Single writer, multiple readers**: When one thread writes to a variable and multiple threads read it, and all threads need to see the latest value.
*   **Code Example**:
    ```java
    public class VolatileDemo {
        private static volatile boolean keepRunning = true;

        public static void main(String[] args) throws InterruptedException {
            Thread worker = new Thread(() -> {
                System.out.println("Worker started.");
                while (keepRunning) {
                    // Simulate work
                }
                System.out.println("Worker stopped.");
            });

            worker.start();
            Thread.sleep(1000); // Let the worker run for a while
            System.out.println("Main thread setting keepRunning to false.");
            keepRunning = false; // Worker thread will see this change and stop
        }
    }
    ```
    *   **Interview Note**: If `keepRunning` were not `volatile`, the worker thread might never see the change to `false` and loop indefinitely due to caching.

## 11. Java Memory Model (JMM)

*   **Purpose**: Defines how threads interact with memory and ensures consistent behavior of concurrent applications across different hardware and JVM implementations. It addresses visibility and ordering issues.
*   **Key Concepts (Beginner-Friendly)**:
    *   **Main Memory**: Where shared variables reside.
    *   **Working Memory (Thread Local Cache)**: Each thread has its own working memory where it keeps copies of variables it uses.
    *   **`happens-before` relationship**: The core of JMM. It's a guarantee that if action A `happens-before` action B, then A's effects are visible to B, and A must precede B in time.
        *   **Program Order Rule**: Each action in a thread `happens-before` every subsequent action in that same thread.
        *   **Monitor Lock Rule**: An unlock on a monitor `happens-before` every subsequent lock on the *same monitor*.
        *   **`volatile` Variable Rule**: A write to a `volatile` variable `happens-before` every subsequent read of that same `volatile` variable.
        *   **Thread Start Rule**: `Thread.start()` `happens-before` any action in the started thread.
        *   **Thread Join Rule**: All actions in a thread `happens-before` a successful `return` from `Thread.join()` on that thread.
        *   **Transitivity**: If A `happens-before` B, and B `happens-before` C, then A `happens-before` C.
    *   **Reordering**: Compilers and processors can reorder instructions for performance, as long as the sequential consistency within a *single thread* is preserved. The JMM specifies rules to limit this reordering in multithreaded contexts to ensure correctness.
*   **Why it matters**: Without JMM and its `happens-before` guarantees, concurrent programs would be unpredictable. Synchronization mechanisms (like `synchronized` and `volatile`) establish `happens-before` relationships.
*   **Interview Note**: Focus on the `happens-before` relationship as the fundamental concept for understanding visibility and ordering guarantees.

## 12. Atomic Operations and Atomic Classes

*   **Atomic Operation**: An operation that is guaranteed to be performed completely and indivisibly. It either completes entirely or not at all; it cannot be interrupted in the middle.
*   **Problem with `count++`**: Even `count++` (read, increment, write) is not atomic. In a multithreaded environment, it can lead to race conditions.
*   **`java.util.concurrent.atomic` package**: Provides classes that support atomic operations on single variables without using locks. These classes use low-level processor instructions (like Compare-And-Swap - CAS) for efficiency.
*   **Important Classes**:
    *   `AtomicInteger`: For atomic operations on `int` values.
        *   `int get()`
        *   `void set(int newValue)`
        *   `int incrementAndGet()`: Atomically increments by one.
        *   `int decrementAndGet()`: Atomically decrements by one.
        *   `int addAndGet(int delta)`: Atomically adds `delta`.
        *   `int getAndSet(int newValue)`: Atomically sets to `newValue` and returns the old value.
        *   `boolean compareAndSet(int expectedValue, int newValue)`: Atomically sets the value to `newValue` if the current value is `expectedValue`. Returns `true` on success.
    *   `AtomicLong`, `AtomicBoolean`, `AtomicReference<V>`: Similar functionality for `long`, `boolean`, and object references respectively.
    *   `AtomicStampedReference`, `AtomicMarkableReference`: For situations where ABA problem might occur (A -> B -> A, `compareAndSet` might succeed even if an intermediate change happened).
    *   `LongAdder`, `LongAccumulator`: More performant alternatives to `AtomicLong` for high contention scenarios, especially when updating counters.
*   **When to use**: When you need atomic updates on single variables and want to avoid the overhead of explicit locking (`synchronized` or `ReentrantLock`).
*   **Interview Note**: Explain that atomic classes are lock-free and use CAS operations for performance. They are suitable for single-variable updates.
*   **Code Example**:
    ```java
    import java.util.concurrent.atomic.AtomicInteger;

    class AtomicCounter {
        private AtomicInteger count = new AtomicInteger(0);

        public void increment() {
            count.incrementAndGet(); // Atomic operation
        }

        public int getCount() {
            return count.get();
        }
    }

    public class AtomicDemo {
        public static void main(String[] args) throws InterruptedException {
            AtomicCounter counter = new AtomicCounter();

            Runnable task = () -> {
                for (int i = 0; i < 10000; i++) {
                    counter.increment();
                }
            };

            Thread t1 = new Thread(task, "Thread-1");
            Thread t2 = new Thread(task, "Thread-2");

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("Final count: " + counter.getCount()); // Always 20000
        }
    }
    ```

## 13. `Lock` Interface and Implementations (`ReentrantLock`)

The `java.util.concurrent.locks` package provides a more flexible and powerful alternative to the `synchronized` keyword.

*   **`Lock` Interface**:
    *   **Methods**:
        *   `void lock()`: Acquires the lock. If the lock is not available, the current thread is disabled for thread scheduling purposes and lies dormant until the lock has been acquired.
        *   `void unlock()`: Releases the lock.
        *   `boolean tryLock()`: Attempts to acquire the lock without blocking. Returns `true` on success, `false` otherwise.
        *   `boolean tryLock(long timeout, TimeUnit unit)`: Attempts to acquire the lock within a given waiting time.
        *   `void lockInterruptibly()`: Acquires the lock unless the current thread is interrupted. Throws `InterruptedException`.
        *   `Condition newCondition()`: Returns a `Condition` instance for this `Lock` instance.
    *   **Key Difference from `synchronized`**:
        *   **Explicit Lock/Unlock**: Requires explicit `lock()` and `unlock()` calls (must be in a `finally` block to ensure release).
        *   **Non-Blocking Attempts**: `tryLock()` allows attempting to acquire a lock without waiting indefinitely, which helps avoid deadlocks.
        *   **Interruptible**: `lockInterruptibly()` allows a thread waiting for a lock to be interrupted.
        *   **Fairness**: Can specify a `fair` policy (threads acquire lock in the order they requested it), which helps prevent starvation (e.g., `new ReentrantLock(true)`). `synchronized` locks are inherently unfair.
        *   **Multiple `Condition` variables**: A single `Lock` can have multiple associated `Condition` objects, allowing threads to wait on different conditions (more flexible than `wait()/notify()` which ties conditions to the object's single monitor).
*   **`ReentrantLock`**:
    *   **Implementation**: A concrete implementation of the `Lock` interface. It is a reentrant mutual exclusion lock, meaning a thread can acquire the same lock multiple times without deadlocking itself.
    *   **When to use**: When you need more control than `synchronized` (e.g., non-blocking lock attempts, interruptible locks, fairness, multiple conditions).
*   **Code Example**:
    ```java
    import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;

    class SafeCounter {
        private int count = 0;
        private final Lock lock = new ReentrantLock(); // Non-fair by default

        public void increment() {
            lock.lock(); // Acquire the lock
            try {
                count++;
                System.out.println(Thread.currentThread().getName() + " incremented to: " + count);
            } finally {
                lock.unlock(); // ALWAYS release the lock in a finally block
            }
        }

        public int getCount() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }

        // Example with tryLock
        public boolean tryIncrement() {
            if (lock.tryLock()) { // Try to acquire lock without blocking
                try {
                    count++;
                    System.out.println(Thread.currentThread().getName() + " incremented (tryLock): " + count);
                    return true;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " failed to acquire lock.");
                return false;
            }
        }
    }

    public class ReentrantLockDemo {
        public static void main(String[] args) throws InterruptedException {
            SafeCounter counter = new SafeCounter();

            Runnable task = () -> {
                for (int i = 0; i < 5000; i++) {
                    counter.increment();
                    // counter.tryIncrement(); // Can test tryLock functionality
                }
            };

            Thread t1 = new Thread(task, "Lock-Thread-1");
            Thread t2 = new Thread(task, "Lock-Thread-2");

            t1.start();
            t2.start();

            t1.join();
            t2.join();

            System.out.println("Final count: " + counter.getCount()); // Should be 10000
        }
    }
    ```

## 14. Condition Variables

*   **Purpose**: A `Condition` object (associated with a `Lock`) provides a mechanism for threads to suspend their execution until a particular condition is true. It is a more flexible alternative to `wait()`, `notify()`, `notifyAll()`.
*   **How to get**: `Lock.newCondition()`
*   **Methods**:
    *   `void await()`: Causes the current thread to wait until another thread signals (calls `signal()` or `signalAll()`) or the thread is interrupted. Releases the associated lock.
    *   `void signal()`: Wakes up one waiting thread.
    *   `void signalAll()`: Wakes up all waiting threads.
    *   **Time-based**: `await(long time, TimeUnit unit)`, `awaitUntil(Date deadline)`, `awaitNanos(long nanosTimeout)`.
*   **Key Differences from `wait()/notify()`**:
    *   **Multiple conditions**: A single `Lock` can have multiple `Condition` objects. This allows different sets of threads to wait on different conditions without interfering with each other (e.g., `notFull` condition for producers, `notEmpty` condition for consumers, both tied to the same `Lock`). With `wait()/notify()`, all threads wait on the *same* monitor, requiring `notifyAll()` more often and potentially leading to unnecessary wakeups.
    *   **Explicit Lock**: `Condition` methods must be called while holding the associated `Lock`.
*   **When to use**: For complex producer-consumer scenarios or other coordination problems where `wait()/notify()` might lead to "lost" notifications or excessive `notifyAll()` calls.
*   **Code Example (Producer-Consumer with `Condition`)**:
    ```java
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.concurrent.locks.Condition;
    import java.util.concurrent.locks.Lock;
    import java.util.concurrent.locks.ReentrantLock;

    class ProducerConsumerWithCondition {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int CAPACITY = 5;
        private final Lock lock = new ReentrantLock();
        private final Condition notFull = lock.newCondition();  // Condition for producers
        private final Condition notEmpty = lock.newCondition(); // Condition for consumers

        public void produce() throws InterruptedException {
            int value = 0;
            while (true) {
                lock.lock();
                try {
                    while (buffer.size() == CAPACITY) {
                        System.out.println("Buffer is full. Producer waiting.");
                        notFull.await(); // Producer waits, releases lock
                    }
                    buffer.add(value);
                    System.out.println("Produced: " + value);
                    value++;
                    notEmpty.signalAll(); // Signal consumers that buffer is not empty
                    Thread.sleep(500);
                } finally {
                    lock.unlock();
                }
            }
        }

        public void consume() throws InterruptedException {
            while (true) {
                lock.lock();
                try {
                    while (buffer.isEmpty()) {
                        System.out.println("Buffer is empty. Consumer waiting.");
                        notEmpty.await(); // Consumer waits, releases lock
                    }
                    int val = buffer.remove();
                    System.out.println("Consumed: " + val);
                    notFull.signalAll(); // Signal producers that buffer is not full
                    Thread.sleep(1000);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public class ConditionDemo {
        public static void main(String[] args) {
            ProducerConsumerWithCondition pc = new ProducerConsumerWithCondition();

            Thread producerThread = new Thread(() -> {
                try {
                    pc.produce();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer");

            Thread consumerThread = new Thread(() -> {
                try {
                    pc.consume();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer");

            producerThread.start();
            consumerThread.start();
        }
    }
    ```

## 15. Thread Pools and `ExecutorService`

*   **Problem**: Creating and destroying threads frequently is expensive. Managing a large number of threads manually is complex and error-prone.
*   **Solution**: Thread pools manage a collection of worker threads that can execute submitted tasks. `ExecutorService` is the high-level interface for managing thread pools.
*   **`Executor` Interface**:
    *   `void execute(Runnable command)`: Executes the given command at some time in the future.
*   **`ExecutorService` Interface (extends `Executor`)**:
    *   `Future<?> submit(Runnable task)`: Submits a `Runnable` task for execution and returns a `Future` representing that task.
    *   `Future<T> submit(Callable<T> task)`: Submits a `Callable` task (which returns a result) for execution.
    *   `void shutdown()`: Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
    *   `List<Runnable> shutdownNow()`: Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were awaiting execution.
    *   `boolean isShutdown()`: Returns `true` if this executor has been shut down.
    *   `boolean isTerminated()`: Returns `true` if all tasks have completed following `shutdown`.
    *   `boolean awaitTermination(long timeout, TimeUnit unit)`: Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted.
    *   `List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)`: Executes the given tasks, returning a list of `Future`s. Each `Future` is completed when its task completes.
    *   `T invokeAny(Collection<? extends Callable<T>> tasks)`: Executes the given tasks, returning the result of one that completes successfully (first one). All other tasks are cancelled.
*   **`Executors` Utility Class**: Factory methods for creating common `ExecutorService` types:
    *   `Executors.newFixedThreadPool(int nThreads)`: Creates a thread pool that reuses a fixed number of threads operating off a shared unbounded queue.
    *   `Executors.newCachedThreadPool()`: Creates a thread pool that creates new threads as needed, but reuses previously constructed threads when they are available. Good for many short-lived tasks.
    *   `Executors.newSingleThreadExecutor()`: Creates a single-threaded executor that guarantees sequential execution of tasks.
    *   `Executors.newScheduledThreadPool(int corePoolSize)`: Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
*   **When to use**: Almost always for managing asynchronous tasks in modern Java. Simplifies concurrency, improves performance, and enhances resource management.
*   **Best Practice**: Always `shutdown()` an `ExecutorService` when it's no longer needed to release resources. Use `try-finally` or `ExecutorService.awaitTermination()` for graceful shutdown.
*   **Code Example**:
    ```java
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import java.util.concurrent.TimeUnit;

    public class ExecutorServiceDemo {
        public static void main(String[] args) throws InterruptedException {
            // 1. Fixed Thread Pool
            ExecutorService fixedPool = Executors.newFixedThreadPool(3);
            System.out.println("FixedThreadPool: Submitting tasks...");
            for (int i = 0; i < 5; i++) {
                final int taskId = i;
                fixedPool.execute(() -> {
                    System.out.println("Fixed Pool Task " + taskId + " executed by: " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(500); // Simulate work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            fixedPool.shutdown(); // No new tasks accepted
            fixedPool.awaitTermination(1, TimeUnit.MINUTES); // Wait for tasks to complete
            System.out.println("FixedThreadPool: All tasks completed.\n");

            // 2. Cached Thread Pool
            ExecutorService cachedPool = Executors.newCachedThreadPool();
            System.out.println("CachedThreadPool: Submitting tasks...");
            for (int i = 0; i < 10; i++) {
                final int taskId = i;
                cachedPool.execute(() -> {
                    System.out.println("Cached Pool Task " + taskId + " executed by: " + Thread.currentThread().getName());
                    try {
                        Thread.sleep(100); // Simulate short work
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            cachedPool.shutdown();
            cachedPool.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println("CachedThreadPool: All tasks completed.\n");
        }
    }
    ```

### 15.1 ThreadPoolExecutor

This cheat sheet covers essential concepts of `ThreadPoolExecutor` in Java, geared towards intermediate-level interview questions.

#### 1. Introduction to ThreadPoolExecutor

*   **What is it?** `ThreadPoolExecutor` is a core class in `java.util.concurrent` package that manages a pool of worker threads. It's designed to execute submitted `Runnable` or `Callable` tasks.
*   **Why use it?**
    *   **Resource Management:** Reuses threads, reducing the overhead of creating and destroying threads for each task.
    *   **Performance:** Improves responsiveness and throughput by managing thread lifecycle efficiently.
    *   **Concurrency Control:** Provides mechanisms to control the number of concurrent threads.
    *   **Decoupling:** Separates task submission from task execution.
*   **Key Interface:** Implements the `ExecutorService` interface.

#### 2. Core Constructor Parameters

Understanding these parameters is crucial for configuring a `ThreadPoolExecutor` effectively.

`ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)`

*   **`corePoolSize`**: The number of threads to keep in the pool, even if they are idle, unless `allowCoreThreadTimeOut` is set to true. **Important:** These threads are created when tasks are submitted, not necessarily at initialization.
*   **`maximumPoolSize`**: The maximum number of threads allowed in the pool. When the `workQueue` is full, new tasks will cause new threads (up to `maximumPoolSize`) to be created.
*   **`keepAliveTime`**: When the number of threads is greater than `corePoolSize`, this is the maximum time that excess idle threads will wait for new tasks before terminating.
*   **`unit`**: The time unit for the `keepAliveTime` argument (e.g., `TimeUnit.SECONDS`).
*   **`workQueue`**: The queue used to hold tasks before they are executed. This queue will only hold `Runnable` tasks.
    *   **Common types:**
        *   `LinkedBlockingQueue`: Unbounded queue (can lead to `OutOfMemoryError` if tasks are submitted faster than processed and `maximumPoolSize` is effectively ignored).
        *   `ArrayBlockingQueue`: Bounded queue.
        *   `SynchronousQueue`: A queue with no internal capacity. Each insertion must wait for a corresponding removal, and vice-versa.
*   **`threadFactory`**: An object used to create new threads. Allows customization of thread names, daemon status, priority, etc.
*   **`handler`**: The policy for handling tasks that cannot be executed. This happens when the executor has been shut down, or when `maximumPoolSize` and `workQueue` are both full.


#### 3. RejectedExecutionHandler (Rejection Policy)

When the `ThreadPoolExecutor` cannot accept a new task (e.g., when the executor is shut down, or `workQueue` is full and `maximumPoolSize` has been reached), the `RejectedExecutionHandler` is invoked.

*   **Default Policies:**
    *   **`ThreadPoolExecutor.AbortPolicy` (Default):** Throws a `RejectedExecutionException`. **Important:** This is the default behavior and can crash your application if not handled.
    *   **`ThreadPoolExecutor.CallerRunsPolicy`:** The thread that submitted the task (caller) executes the task itself. This provides a simple form of backpressure.
    *   **`ThreadPoolExecutor.DiscardPolicy`:** Silently discards the rejected task.
    *   **`ThreadPoolExecutor.DiscardOldestPolicy`:** Discards the oldest unhandled task in the `workQueue` and then retries to submit the new task (which might still be rejected).
*   **Custom Policy:** You can implement the `RejectedExecutionHandler` interface to define your own custom rejection logic.

#### 6. ThreadPoolExecutor's Internal Working Flow

1.  If the number of running threads is less than `corePoolSize`, a new thread is created and the task is executed.
2.  If the number of running threads is equal to `corePoolSize` or more, the task is added to the `workQueue`.
3.  If the `workQueue` is full:
    *   If the number of running threads is less than `maximumPoolSize`, a new thread is created and the task is executed.
    *   If the number of running threads is equal to `maximumPoolSize`, the task is rejected by the `RejectedExecutionHandler`.

#### 7. Common Interview Questions 


*   **Describe the trade-offs of different `RejectedExecutionHandler` policies.**
    *   `AbortPolicy`: Simplest, but can crash. Good for critical tasks where failure must be immediately apparent.
    *   `CallerRunsPolicy`: Provides backpressure, useful for slowing down task submission when the executor is overloaded.
    *   `DiscardPolicy`/`DiscardOldestPolicy`: Useful for non-critical tasks where losing some tasks is acceptable (e.g., logging, metrics).
*   **How would you determine appropriate `corePoolSize` and `maximumPoolSize` values?**
    *   **CPU-bound tasks:** `corePoolSize = number_of_cores`. `maximumPoolSize` can be similar or slightly higher.
    *   **IO-bound tasks:** `corePoolSize = number_of_cores * (1 + wait_time / service_time)`. `maximumPoolSize` can be significantly higher to account for threads blocked on I/O.
    *   **Monitoring:** Start with reasonable defaults, then monitor CPU usage, queue length, and response times, adjusting as needed.


#### 8. Example Usage

```java
import java.util.concurrent.*;

public class ThreadPoolExecutorExample {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        // 1. Create a ThreadPoolExecutor
        // Core: 2, Max: 4, KeepAlive: 10s, Bounded Queue: 2, Default Rejected Handler
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,                          // corePoolSize
                4,                          // maximumPoolSize
                10,                         // keepAliveTime
                TimeUnit.SECONDS,           // unit
                new ArrayBlockingQueue<>(2),// workQueue (bounded)
                Executors.defaultThreadFactory(), // threadFactory
                new ThreadPoolExecutor.AbortPolicy() // rejectedExecutionHandler
        );

        // 2. Submit Runnable tasks
        System.out.println("Submitting Runnable tasks...");
        for (int i = 0; i < 6; i++) { // Submit 6 tasks
            final int taskId = i;
            executor.execute(() -> {
                System.out.println("Executing Runnable Task " + taskId + " by " + Thread.currentThread().getName());
                try {
                    TimeUnit.SECONDS.sleep(2); // Simulate work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Runnable Task " + taskId + " interrupted.");
                }
            });
        }

        // 3. Submit Callable tasks and get Future results
        System.out.println("\nSubmitting Callable tasks...");
        Future<String> future1 = executor.submit(() -> {
            System.out.println("Executing Callable Task 1 by " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(1);
            return "Result of Callable 1";
        });

        Future<String> future2 = executor.submit(() -> {
            System.out.println("Executing Callable Task 2 by " + Thread.currentThread().getName());
            TimeUnit.SECONDS.sleep(3);
            return "Result of Callable 2";
        });

        // Get results from Futures
        System.out.println("Future 1 Result: " + future1.get());
        System.out.println("Future 2 Result: " + future2.get());

        // 4. Proper Shutdown
        System.out.println("\nInitiating shutdown...");
        executor.shutdown(); // No new tasks accepted, existing tasks complete

        try {
            // Wait for existing tasks to terminate
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Timeout reached, forcing shutdown...");
                executor.shutdownNow(); // Interrupt tasks and stop immediately
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown interrupted: " + e.getMessage());
            executor.shutdownNow();
        }
        System.out.println("Executor has terminated: " + executor.isTerminated());

        // Example of task rejection (if submitted after shutdown or when queue+max full)
        try {
            executor.execute(() -> System.out.println("This task should be rejected!"));
        } catch (RejectedExecutionException e) {
            System.out.println("Successfully rejected task after shutdown: " + e.getMessage());
        }
    }
}
```


## 16. `Callable`, `Future`, `CompletableFuture`

*   **`Runnable` vs. `Callable`**:
    *   **`Runnable`**:
        *   `void run()`: Does not return a result.
        *   Cannot throw checked exceptions.
    *   **`Callable<V>`**: (Introduced in Java 5)
        *   `V call()`: Returns a result of type `V`.
        *   Can throw checked exceptions.
    *   **When to use**: `Callable` is used when tasks need to return a result or throw exceptions.
*   **`Future<V>`**:
    *   **Purpose**: Represents the result of an asynchronous computation. It provides methods to check if the computation is complete, wait for its completion, and retrieve the result.
    *   **How to get**: Returned by `ExecutorService.submit(Callable<T> task)`.
    *   **Methods**:
        *   `boolean isDone()`: Returns `true` if the task completed.
        *   `boolean isCancelled()`: Returns `true` if the task was cancelled.
        *   `boolean cancel(boolean mayInterruptIfRunning)`: Attempts to cancel the task.
        *   `V get()`: Blocks until the computation is complete, then retrieves its result. Throws `InterruptedException` or `ExecutionException`.
        *   `V get(long timeout, TimeUnit unit)`: Blocks for a specified time to retrieve the result. Throws `TimeoutException` if the timeout expires.
    *   **Limitations**: `Future` is blocking. You have to actively poll `isDone()` or `get()` which blocks the current thread.
*   **`CompletableFuture<T>`**: (Introduced in Java 8)
    *   **Purpose**: Addresses the limitations of `Future` by providing a non-blocking, reactive, and composable way to perform asynchronous computations. It's an API for asynchronous programming similar to JavaScript Promises.
    *   **Key Features**:
        *   **Composition**: Can chain multiple asynchronous operations.
        *   **Error Handling**: Provides methods for handling exceptions in the chain.
        *   **Combinations**: Can combine results of multiple `CompletableFuture`s.
        *   **Non-blocking**: Operations are typically non-blocking.
    *   **Methods (Examples)**:
        *   `CompletableFuture.supplyAsync(Supplier<U> supplier)`: Runs a `Supplier` asynchronously, returning a new `CompletableFuture` that is completed with the value of the `Supplier`.
        *   `CompletableFuture.runAsync(Runnable runnable)`: Runs a `Runnable` asynchronously.
        *   `thenApply(Function<? super T,? extends U> fn)`: Processes the result of the previous stage asynchronously.
        *   `thenAccept(Consumer<? super T> action)`: Consumes the result without returning one.
        *   `thenCompose(Function<? super T, ? extends CompletionStage<U>> fn)`: FlatMap-like operation to chain dependent `CompletableFuture`s.
        *   `thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)`: Combines results from two independent `CompletableFuture`s.
        *   `exceptionally(Function<Throwable, ? extends T> fn)`: Handles exceptions.
        *   `allOf(CompletableFuture<?>... cfs)`: Returns a new `CompletableFuture` that is completed when all given `CompletableFuture`s complete.
        *   `anyOf(CompletableFuture<?>... cfs)`: Returns a new `CompletableFuture` that is completed when any of the given `CompletableFuture`s complete.
    *   **When to use**: For complex asynchronous workflows, microservices communication, or scenarios requiring event-driven, non-blocking asynchronous programming.
*   **Code Example**:
    ```java
    import java.util.concurrent.*;

    public class CallableFutureCompletableFutureDemo {
        public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
            ExecutorService executor = Executors.newFixedThreadPool(2);

            // --- Callable & Future ---
            Callable<Integer> callableTask = () -> {
                System.out.println("Callable Task starting in: " + Thread.currentThread().getName());
                Thread.sleep(2000); // Simulate long computation
                return 123;
            };

            System.out.println("Submitting Callable task...");
            Future<Integer> futureResult = executor.submit(callableTask);

            // You can do other work here while the task runs
            System.out.println("Main thread doing other work...");
            Thread.sleep(500);

            // Blocking call to get result
            System.out.println("Main thread waiting for Future result...");
            // int result = futureResult.get(); // Blocks indefinitely
            int result = futureResult.get(3, TimeUnit.SECONDS); // Blocks with timeout
            System.out.println("Future Task Result: " + result);

            // --- CompletableFuture ---
            System.out.println("\n--- CompletableFuture Demo ---");

            // 1. Running a task and getting a result (supplyAsync)
            CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
                System.out.println("CF Task 1 (supplyAsync) starting in: " + Thread.currentThread().getName());
                try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                return "Hello from CF1!";
            });

            // 2. Chaining operations (thenApply)
            CompletableFuture<String> future2 = future1.thenApply(s -> {
                System.out.println("CF Task 2 (thenApply) processing: " + s + " in: " + Thread.currentThread().getName());
                return s.toUpperCase();
            });

            // 3. Consuming a result (thenAccept)
            future2.thenAccept(s -> {
                System.out.println("CF Task 3 (thenAccept) received: " + s + " in: " + Thread.currentThread().getName());
            });

            // 4. Combining independent futures (thenCombine)
            CompletableFuture<Integer> cfInt1 = CompletableFuture.supplyAsync(() -> { try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } return 10; });
            CompletableFuture<Integer> cfInt2 = CompletableFuture.supplyAsync(() -> { try { Thread.sleep(700); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } return 20; });

            CompletableFuture<Integer> combinedFuture = cfInt1.thenCombine(cfInt2, (res1, res2) -> {
                System.out.println("CF Combine Task in: " + Thread.currentThread().getName());
                return res1 + res2;
            });
            System.out.println("Combined Future Result: " + combinedFuture.get()); // Blocks to get final result

            // 5. AllOf / AnyOf
            CompletableFuture<String> page1 = CompletableFuture.supplyAsync(() -> { try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } return "Content from Page 1"; });
            CompletableFuture<String> page2 = CompletableFuture.supplyAsync(() -> { try { Thread.sleep(500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); } return "Content from Page 2"; });

            CompletableFuture<Void> allFutures = CompletableFuture.allOf(page1, page2);
            allFutures.thenRun(() -> {
                System.out.println("All pages loaded.");
                try {
                    System.out.println(page1.get());
                    System.out.println(page2.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });

            allFutures.join(); // Block until all futures complete for the main thread to see output

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
    ```

## 17. Concurrent Collections

*   **Problem**: Standard Java collections (`ArrayList`, `HashMap`, `LinkedList`, etc.) are *not* thread-safe. Concurrent modifications can lead to `ConcurrentModificationException`, inconsistent data, or infinite loops.
*   **Solution**: `java.util.concurrent` package provides thread-safe, highly optimized concurrent collections.
*   **Key Concurrent Collections**:
    *   **`ConcurrentHashMap<K, V>`**:
        *   **Why**: A thread-safe alternative to `HashMap`. Offers high concurrency for retrievals and good concurrency for updates. It does not lock the entire map for every operation.
        *   **How it works**: Uses a fine-grained locking mechanism (often segment-level or node-level locking) and `volatile` or CAS operations. Read operations typically do not require locking.
        *   **Methods**: Same as `HashMap`, but thread-safe.
        *   **Interview Note**: Explain its high concurrency due to fine-grained locking and non-blocking reads. Faster than `Collections.synchronizedMap()` for concurrent access.
    *   **`CopyOnWriteArrayList<E>` / `CopyOnWriteArraySet<E>`**:
        *   **Why**: Thread-safe lists/sets where all mutative operations (add, set, remove, etc.) are implemented by making a fresh copy of the underlying array.
        *   **When to use**: When read operations vastly outnumber write operations. Reading is very fast (no synchronization needed), but writes are expensive as they involve copying the entire data structure.
        *   **Limitations**: High memory consumption on writes, iterators reflect the state of the array at the time the iterator was created (eventual consistency).
    *   **BlockingQueue Interface**: See next section.
    *   **`ConcurrentSkipListMap<K, V>` / `ConcurrentSkipListSet<E>`**:
        *   **Why**: Concurrent, sorted alternatives to `TreeMap` and `TreeSet`.
        *   **How it works**: Uses a Skip List data structure for efficient concurrent access.
        *   **When to use**: When you need thread-safe, sorted collections with good concurrent performance.
    *   **`ConcurrentLinkedQueue<E>`**:
        *   **Why**: An unbounded, thread-safe, non-blocking queue. Uses a CAS-based algorithm.
        *   **When to use**: When you need a fast, non-blocking queue for producer-consumer patterns where a fixed capacity isn't required.
*   **`Collections.synchronized...` methods (e.g., `Collections.synchronizedList(new ArrayList<T>())`)**:
    *   **Why**: Provides thread-safe wrappers for existing collections.
    *   **How it works**: Each method call on the wrapped collection is `synchronized` on the collection object itself.
    *   **Limitations**: Provides coarser-grained locking (locks the entire collection for every operation), leading to lower concurrency compared to `ConcurrentHashMap` or other `java.util.concurrent` classes. Iteration often requires external synchronization.
*   **Code Example (`ConcurrentHashMap`)**:
    ```java
    import java.util.concurrent.ConcurrentHashMap;
    import java.util.concurrent.ExecutorService;
    import java.util.concurrent.Executors;
    import java.util.concurrent.TimeUnit;

    public class ConcurrentHashMapDemo {
        public static void main(String[] args) throws InterruptedException {
            ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
            ExecutorService executor = Executors.newFixedThreadPool(10);

            Runnable task = () -> {
                for (int i = 0; i < 1000; i++) {
                    String key = "Key-" + (i % 5); // 5 distinct keys
                    map.compute(key, (k, v) -> (v == null) ? 1 : v + 1); // Atomically updates
                }
            };

            for (int i = 0; i < 5; i++) {
                executor.submit(task);
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            System.out.println("Final Map Content: " + map);
            // Sum of values should be 5 threads * 1000 updates = 5000
            int total = map.values().stream().mapToInt(Integer::intValue).sum();
            System.out.println("Total count: " + total); // Should be 5000
        }
    }
    ```

## 18. Blocking Queues

*   **`BlockingQueue<E>` Interface**:
    *   **Purpose**: A queue that additionally supports operations that wait for the queue to become non-empty when retrieving an element, and wait for space to become available in the queue when storing an element.
    *   **When to use**: Essential for producer-consumer patterns, where producers add elements and consumers remove elements. They handle synchronization automatically.
    *   **Key Methods**:
        *   **Throw exception**: `add(E e)`, `remove()`, `element()`
        *   **Return special value**: `offer(E e)`, `poll()`, `peek()`
        *   **Block**: `put(E e)`, `take()`
        *   **Time out**: `offer(E e, long timeout, TimeUnit unit)`, `poll(long timeout, TimeUnit unit)`
*   **Common Implementations**:
    *   **`ArrayBlockingQueue<E>`**: A bounded, fair or unfair, blocking queue backed by an array.
        *   **When to use**: When you need a fixed-size buffer between producers and consumers.
    *   **`LinkedBlockingQueue<E>`**: An optionally bounded (if capacity is specified, otherwise unbounded by default), blocking queue backed by a linked list.
        *   **When to use**: When you need a queue with potentially large or unlimited capacity. Good for producer-consumer where producer rates might vary.
    *   **`PriorityBlockingQueue<E>`**: An unbounded blocking queue that uses the natural ordering of its elements, or a `Comparator`, to order elements.
        *   **When to use**: When consumers need to process items based on priority.
    *   **`DelayQueue<E extends Delayed>`**: An unbounded blocking queue of `Delayed` elements, in which an element can only be taken when its delay has expired.
        *   **When to use**: Scheduling tasks, caching, or other scenarios requiring delayed processing.
    *   **`SynchronousQueue<E>`**: A blocking queue in which each `put` must wait for a corresponding `take`, and vice versa. It effectively has a capacity of zero.
        *   **When to use**: For hand-off scenarios where threads directly pass elements to each other without an intermediate buffer.
*   **Code Example (Producer-Consumer with `ArrayBlockingQueue`)**:
    ```java
    import java.util.concurrent.ArrayBlockingQueue;
    import java.util.concurrent.BlockingQueue;
    import java.util.concurrent.TimeUnit;

    public class BlockingQueueDemo {
        public static void main(String[] args) throws InterruptedException {
            BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5); // Bounded queue of capacity 5

            // Producer
            Thread producer = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        System.out.println("Producing: " + i);
                        queue.put(i); // Blocks if queue is full
                        Thread.sleep(200);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Producer");

            // Consumer
            Thread consumer = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000); // Consumer is slower
                        int val = queue.take(); // Blocks if queue is empty
                        System.out.println("Consumed: " + val);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }, "Consumer");

            producer.start();
            consumer.start();

            producer.join();
            consumer.join();

            System.out.println("Producer and Consumer finished.");
        }
    }
    ```

## 19. Fork/Join Framework (Overview)

*   **Purpose**: A framework introduced in Java 7 for parallelizing tasks that can be broken down into smaller subtasks (divide and conquer). It's designed for efficiency on multi-core processors.
*   **Key Components**:
    *   **`ForkJoinPool`**: An `ExecutorService` designed to work with `ForkJoinTask`s. It efficiently manages worker threads and work-stealing.
    *   **`ForkJoinTask<V>`**: An abstract class for tasks executable within a `ForkJoinPool`.
        *   **`RecursiveAction`**: A `ForkJoinTask` that does not return a result (like `Runnable`).
        *   **`RecursiveTask<V>`**: A `ForkJoinTask` that returns a result of type `V` (like `Callable`).
    *   **`fork()`**: Asynchronously executes the task in the `ForkJoinPool`.
    *   **`join()`**: Waits for the task to complete and returns its result.
*   **How it works (Work-Stealing)**: When a worker thread runs out of tasks in its own double-ended queue, it can "steal" tasks from the tail of another busy worker's queue. This balances the workload and keeps all cores busy.
*   **When to use**: For problems that are inherently recursive and can be broken down into independent subproblems, such as:
    *   Parallel sorting (e.g., merge sort)
    *   Tree traversals
    *   Image processing
    *   Recursive computations on large data sets
*   **Limitations**: Not suitable for tasks that perform I/O or other blocking operations, as it can starve the pool of available threads.
*   **Code Example (Summing an array)**:
    ```java
    import java.util.concurrent.ForkJoinPool;
    import java.util.concurrent.RecursiveTask;

    class SumArrayTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000; // Threshold for direct computation
        private final long[] array;
        private final int start;
        private final int end;

        public SumArrayTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= THRESHOLD) {
                // Base case: compute directly if task is small enough
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // Recursive case: divide task into two subtasks
                int mid = start + (end - start) / 2;
                SumArrayTask leftTask = new SumArrayTask(array, start, mid);
                SumArrayTask rightTask = new SumArrayTask(array, mid, end);

                // Fork the left task, compute the right task, then join the left
                leftTask.fork(); // Asynchronously execute leftTask
                Long rightResult = rightTask.compute(); // Execute rightTask on current thread
                Long leftResult = leftTask.join(); // Wait for leftTask to complete

                return leftResult + rightResult;
            }
        }
    }

    public class ForkJoinDemo {
        public static void main(String[] args) {
            long[] numbers = new long[1_000_000];
            for (int i = 0; i < numbers.length; i++) {
                numbers[i] = i + 1;
            }

            ForkJoinPool forkJoinPool = new ForkJoinPool();
            SumArrayTask task = new SumArrayTask(numbers, 0, numbers.length);

            long startTime = System.currentTimeMillis();
            long sum = forkJoinPool.invoke(task); // Start the computation
            long endTime = System.currentTimeMillis();

            System.out.println("Sum using Fork/Join: " + sum);
            System.out.println("Time taken: " + (endTime - startTime) + " ms");

            // Verify with sequential sum
            long sequentialSum = 0;
            for (long number : numbers) {
                sequentialSum += number;
            }
            System.out.println("Sequential Sum: " + sequentialSum);

            forkJoinPool.shutdown();
        }
    }
    ```

## 20. Common Concurrency Interview Problems

*   **Producer-Consumer Problem**: (Covered with `wait()/notify()` and `Condition` variables, also with `BlockingQueue`).
    *   **Key**: Correct synchronization, handling full/empty conditions, `wait()` in a loop.
*   **Dining Philosophers Problem**: (Demonstrates deadlock and resource allocation issues).
    *   **Key**: Breaking one of the deadlock conditions (e.g., ordered resource acquisition, `tryLock` with backoff).
*   **Readers-Writers Problem**: (Multiple readers can access concurrently, but writers need exclusive access).
    *   **Key**: `ReadWriteLock` (from `java.util.concurrent.locks`) is the ideal solution. `ReentrantReadWriteLock` allows multiple readers to acquire a read lock simultaneously, but only one writer to acquire a write lock, and writers block readers and other writers.
*   **Implementing a custom `ThreadPoolExecutor`**:
    *   **Key**: Understanding its core parameters (corePoolSize, maximumPoolSize, keepAliveTime, workQueue, ThreadFactory, RejectedExecutionHandler).
*   **Singleton Pattern in a Multithreaded Environment**:
    *   **Key**: Double-checked locking with `volatile` for lazy initialization to ensure thread safety and avoid race conditions during instantiation.
        ```java
        public class Singleton {
            private static volatile Singleton instance; // volatile is crucial

            private Singleton() {} // Private constructor

            public static Singleton getInstance() {
                if (instance == null) { // First check: no sync cost if instance exists
                    synchronized (Singleton.class) { // Synchronize on class lock
                        if (instance == null) { // Second check: inside sync block
                            instance = new Singleton();
                        }
                    }
                }
                return instance;
            }
        }
        ```
*   **Implementing `BlockingQueue`**: (From scratch, without using `java.util.concurrent` classes).
    *   **Key**: Using `ArrayBlockingQueue` as a reference, implement with `synchronized`, `wait()`, `notifyAll()`, and an array.
*   **Counting occurrences in a large file / Summing a large array**:
    *   **Key**: Demonstrates `ExecutorService`, `Callable`/`Future`, or `ForkJoin` framework for parallel processing.

## 21. Best Practices and Anti-Patterns

### Best Practices

*   **Use High-Level Concurrency Utilities**: Prioritize `java.util.concurrent` classes (`ExecutorService`, `ConcurrentHashMap`, `BlockingQueue`, `Atomic` classes, `CountDownLatch`, `CyclicBarrier`, `Semaphore`) over low-level `synchronized` and `wait()/notify()` where possible. They are less error-prone, more performant, and easier to use.
*   **Favor `Callable` over `Runnable`**: If your task returns a result or might throw checked exceptions, use `Callable` with `ExecutorService.submit()`.
*   **Manage `ExecutorService` Lifecycle**: Always `shutdown()` your `ExecutorService` when it's no longer needed to prevent resource leaks and allow the JVM to exit. Use `awaitTermination()` for graceful shutdown.
*   **Protect Shared Mutable Data**: Any data accessed and modified by multiple threads *must* be protected by proper synchronization (locks, atomic variables, concurrent collections).
*   **Prefer Immutable Objects**: Immutable objects are inherently thread-safe as their state cannot change after construction. Minimize mutable state.
*   **Use `volatile` for Visibility of Flags/Single Variables**: When a single variable needs to be visible across threads, especially for control flags, `volatile` is appropriate. Remember its limitation (no atomicity).
*   **Name Your Threads**: Crucial for debugging and logging.
*   **Handle `InterruptedException` Properly**: Don't just swallow it. Restore the interrupt status (`Thread.currentThread().interrupt()`) or propagate it.
*   **Use `try-finally` for Locks**: Always release `Lock`s in a `finally` block to prevent deadlocks and resource leaks.
*   **Keep Critical Sections Small**: Minimize the amount of code inside `synchronized` blocks or `Lock.lock()/unlock()` sections to maximize concurrency.
*   **Use `ReentrantLock` for Advanced Scenarios**: When you need fairness, timed `tryLock()`, interruptible lock acquisition, or multiple `Condition` objects, `ReentrantLock` is superior to `synchronized`.
*   **Understand `happens-before`**: It's the foundation of JMM.