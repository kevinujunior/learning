# Java Interview Preparation Guide Solutions (Month 1)

## Week 1: Java Basics and OOPs

### Topic 1: Java Language Fundamentals - Solutions

#### Interview Questions Solutions:

1.  **Explain the difference between `JVM`, `JRE`, and `JDK`.**
    *   **JVM (Java Virtual Machine):** An abstract machine that provides a runtime environment to execute Java bytecode. It's the specification and runtime instance.
    *   **JRE (Java Runtime Environment):** An implementation of the JVM and contains the core classes and supporting files. It's what you need to *run* Java applications.
    *   **JDK (Java Development Kit):** Contains the JRE along with development tools like the Java compiler (`javac`), debugger, and other utilities. It's what you need to *develop* Java applications.

2.  **What are the primitive data types in Java?**
    *   Java has eight primitive data types:
        *   **Integer types:** `byte`, `short`, `int`, `long`
        *   **Floating-point types:** `float`, `double`
        *   **Character type:** `char`
        *   **Boolean type:** `boolean`

3.  **What is the purpose of the `static` keyword?**
    *   The `static` keyword signifies that a member (variable or method) belongs to the class itself, rather than to any specific instance of the class.
    *   **Static variables:** Shared among all instances of a class.
    *   **Static methods:** Can be called directly on the class name without creating an object. They can only access static members.
    *   **Static blocks:** Used for static initialization of a class.
    *   **Static nested classes:** Can be instantiated without an instance of the outer class.

4.  **Differentiate between `final`, `finally`, and `finalize`.**
    *   **`final`:** A keyword used to make entities constant.
        *   `final` variable: Value cannot be changed once assigned.
        *   `final` method: Cannot be overridden by subclasses.
        *   `final` class: Cannot be subclassed.
    *   **`finally`:** A block in exception handling. It always executes, regardless of whether an exception was caught or not. Used for cleanup code.
    *   **`finalize`:** A method in `Object` class. It is called by the Garbage Collector just before an object is garbage collected, allowing for resource cleanup. (Note: Its use is generally discouraged due to unpredictable execution times and deprecation in newer Java versions).

5.  **What is method overloading and overriding?**
    *   **Method Overloading:** Occurs when a class has multiple methods with the same name but different parameters (different number of parameters, different types of parameters, or different order of parameters). It is a compile-time polymorphism.
    *   **Method Overriding:** Occurs when a subclass provides a specific implementation for a method that is already defined in its superclass. The method signature (name, parameters, return type) must be the same. It is a run-time polymorphism.

#### Practice Questions Solutions:

1.  **Write a program to reverse a string without using built-in methods.**

    ```java
    public class ReverseString {
        public static String reverse(String str) {
            char[] charArray = str.toCharArray();
            int start = 0;
            int end = charArray.length - 1;
            while (start < end) {
                char temp = charArray[start];
                charArray[start] = charArray[end];
                charArray[end] = temp;
                start++;
                end--;
            }
            return new String(charArray);
        }

        public static void main(String[] args) {
            String original = "hello";
            System.out.println("Original: " + original);
            System.out.println("Reversed: " + reverse(original)); // olleh
        }
    }
    ```

2.  **Implement a program to check if a number is prime.**

    ```java
    public class PrimeNumber {
        public static boolean isPrime(int num) {
            if (num <= 1) {
                return false;
            }
            for (int i = 2; i * i <= num; i++) { // Optimized loop: check up to sqrt(num)
                if (num % i == 0) {
                    return false;
                }
            }
            return true;
        }

        public static void main(String[] args) {
            System.out.println("7 is prime: " + isPrime(7));     // true
            System.out.println("10 is prime: " + isPrime(10));   // false
            System.out.println("1 is prime: " + isPrime(1));     // false
        }
    }
    ```

3.  **Create a class `Calculator` with methods for addition, subtraction, multiplication, and division.**

    ```java
    public class Calculator {
        public int add(int a, int b) {
            return a + b;
        }

        public int subtract(int a, int b) {
            return a - b;
        }

        public int multiply(int a, int b) {
            return a * b;
        }

        public double divide(int a, int b) {
            if (b == 0) {
                throw new IllegalArgumentException("Cannot divide by zero.");
            }
            return (double) a / b;
        }

        public static void main(String[] args) {
            Calculator calc = new Calculator();
            System.out.println("Addition: " + calc.add(10, 5));
            System.out.println("Subtraction: " + calc.subtract(10, 5));
            System.out.println("Multiplication: " + calc.multiply(10, 5));
            System.out.println("Division: " + calc.divide(10, 5));
        }
    }
    ```

4.  **Explain the concept of autoboxing and unboxing with an example.**
    *   **Autoboxing:** The automatic conversion that the Java compiler makes between the primitive types and their corresponding wrapper class objects.
    *   **Unboxing:** The reverse process of converting a wrapper class object to its corresponding primitive type.

    ```java
    public class AutoboxingUnboxing {
        public static void main(String[] args) {
            // Autoboxing: int to Integer
            int primitiveInt = 100;
            Integer wrapperInt = primitiveInt; // Autoboxing
            System.out.println("Autoboxed Integer: " + wrapperInt);

            // Unboxing: Integer to int
            int anotherPrimitiveInt = wrapperInt; // Unboxing
            System.out.println("Unboxed int: " + anotherPrimitiveInt);

            // Example with method arguments
            Integer numWrapper = 50; // Autoboxing
            int numPrimitive = numWrapper; // Unboxing
        }
    }
    ```

### Topic 2: Object-Oriented Programming (OOPs) Concepts - Solutions

#### Interview Questions Solutions:

1.  **Define Encapsulation. How is it achieved in Java?**
    *   **Encapsulation:** The mechanism of binding data (variables) and the methods that operate on that data into a single unit (class). It also hides the internal implementation details from the outside world.
    *   **How it's achieved:**
        *   Declaring instance variables as `private`.
        *   Providing public `getter` and `setter` methods to access and modify the private variables.

2.  **What is the difference between an abstract class and an interface?**
    *   **Abstract Class:**
        *   Can have abstract (no body) and concrete (with body) methods.
        *   Can have instance variables (non-final) and static variables (final).
        *   Can have constructors.
        *   Can implement interfaces and extend classes.
        *   A class can extend only one abstract class.
        *   Used to define a common base for closely related classes and share code.
    *   **Interface:**
        *   Before Java 8, could only have abstract methods (implicitly `public abstract`). From Java 8, can have `default` and `static` methods with implementations. From Java 9, `private` methods are allowed.
        *   Variables are implicitly `public static final`.
        *   Cannot have constructors.
        *   A class can implement multiple interfaces.
        *   Used to define contracts for unrelated classes to implement a common behavior.

3.  **Explain method overloading vs. method overriding with examples.**
    *   (Refer to Week 1, Topic 1, Interview Question 5 solution for definition).
    *   **Overloading Example:**
        ```java
        class OverloadDemo {
            int add(int a, int b) { return a + b; }
            int add(int a, int b, int c) { return a + b + c; } // Overloaded method
            double add(double a, double b) { return a + b; }   // Overloaded method
        }
        ```
    *   **Overriding Example:**
        ```java
        class Parent { void display() { System.out.println("Parent display"); } }
        class Child extends Parent {
            @Override
            void display() { System.out.println("Child display"); } // Overridden method
        }
        ```

4.  **What is aggregation and composition?**
    *   Both represent "has-a" relationships (association).
    *   **Aggregation:** A "has-a" relationship where the child object can exist independently of the parent object. It's a weaker form of association. (e.g., A `Department` has `Students`. Students can exist without a Department).
    *   **Composition:** A "has-a" relationship where the child object's lifecycle is dependent on the parent object. If the parent is destroyed, the child is also destroyed. It's a stronger form of association. (e.g., A `House` has `Rooms`. Rooms cannot exist without a House).

5.  **Discuss the "is-a" and "has-a" relationships in OOP.**
    *   **"Is-A" Relationship (Inheritance):** Represents specialization and is implemented using `extends` keyword. A subclass "is a type of" superclass. (e.g., A `Dog` `is-a` `Animal`).
    *   **"Has-A" Relationship (Association/Composition/Aggregation):** Represents ownership or a part-of relationship and is implemented using instance variables. An object "has a" reference to another object. (e.g., A `Car` `has-a` `Engine`).

#### Practice Questions Solutions:

1.  **Design a system for a `Shape` hierarchy (Circle, Rectangle, Triangle) demonstrating inheritance and polymorphism. Calculate area for each.**

    ```java
    abstract class Shape {
        public abstract double calculateArea();
    }

    class Circle extends Shape {
        private double radius;
        public Circle(double radius) { this.radius = radius; }
        @Override
        public double calculateArea() { return Math.PI * radius * radius; }
    }

    class Rectangle extends Shape {
        private double width;
        private double height;
        public Rectangle(double width, double height) { this.width = width; this.height = height; }
        @Override
        public double calculateArea() { return width * height; }
    }

    class Triangle extends Shape {
        private double base;
        private double height;
        public Triangle(double base, double height) { this.base = base; this.height = height; }
        @Override
        public double calculateArea() { return 0.5 * base * height; }
    }

    public class ShapeDemo {
        public static void main(String[] args) {
            Shape circle = new Circle(5);
            Shape rectangle = new Rectangle(4, 6);
            Shape triangle = new Triangle(7, 3);

            System.out.println("Circle Area: " + circle.calculateArea());
            System.out.println("Rectangle Area: " + rectangle.calculateArea());
            System.out.println("Triangle Area: " + triangle.calculateArea());
        }
    }
    ```

2.  **Create an interface `Flyable` and implement it for `Bird` and `Aeroplane` classes.**

    ```java
    interface Flyable {
        void fly();
    }

    class Bird implements Flyable {
        @Override
        public void fly() {
            System.out.println("Bird flies with wings.");
        }
    }

    class Aeroplane implements Flyable {
        @Override
        public void fly() {
            System.out.println("Aeroplane flies with jet engines.");
        }
    }

    public class FlyableDemo {
        public static void main(String[] args) {
            Flyable myBird = new Bird();
            Flyable myAeroplane = new Aeroplane();

            myBird.fly();
            myAeroplane.fly();
        }
    }
    ```

3.  **Design a `Bank` class with `Account` objects, demonstrating encapsulation and composition.**

    ```java
    import java.util.ArrayList;
    import java.util.List;

    class Account {
        private String accountNumber;
        private double balance;

        public Account(String accountNumber, double initialBalance) {
            this.accountNumber = accountNumber;
            this.balance = initialBalance;
        }

        public String getAccountNumber() { return accountNumber; }
        public double getBalance() { return balance; }

        public void deposit(double amount) {
            if (amount > 0) {
                balance += amount;
                System.out.println("Deposited: " + amount + ". New balance: " + balance);
            }
        }

        public void withdraw(double amount) {
            if (amount > 0 && balance >= amount) {
                balance -= amount;
                System.out.println("Withdrew: " + amount + ". New balance: " + balance);
            } else {
                System.out.println("Insufficient funds or invalid amount for withdrawal.");
            }
        }
    }

    class Bank {
        private String name;
        private List<Account> accounts; // Composition: Accounts are part of Bank

        public Bank(String name) {
            this.name = name;
            this.accounts = new ArrayList<>();
        }

        public String getName() { return name; }

        public void addAccount(Account account) {
            this.accounts.add(account);
            System.out.println("Account " + account.getAccountNumber() + " added to " + this.name);
        }

        public Account findAccount(String accountNumber) {
            for (Account acc : accounts) {
                if (acc.getAccountNumber().equals(accountNumber)) {
                    return acc;
                }
            }
            return null;
        }

        public void listAllAccounts() {
            System.out.println("\nAccounts in " + name + ":");
            for (Account acc : accounts) {
                System.out.println("Account No: " + acc.getAccountNumber() + ", Balance: " + acc.getBalance());
            }
        }
    }

    public class BankDemo {
        public static void main(String[] args) {
            Bank myBank = new Bank("MyLocalBank");

            Account acc1 = new Account("12345", 1000.0);
            Account acc2 = new Account("67890", 500.0);

            myBank.addAccount(acc1);
            myBank.addAccount(acc2);

            acc1.deposit(200);
            acc2.withdraw(100);

            myBank.listAllAccounts();
        }
    }
    ```

## Week 2: Collections Framework

### Topic 1: Introduction to Collections - Solutions

#### Interview Questions Solutions:

1.  **What is the Java Collections Framework?**
    *   A set of interfaces and classes that represent groups of objects (collections) as a single unit. It provides a unified architecture for storing and manipulating groups of objects.
    *   Key interfaces: `List`, `Set`, `Map`, `Queue`, `Deque`.
    *   Key benefits: Reduces programming effort, increases program speed/quality, allows interoperability between unrelated APIs.

2.  **Differentiate between `List`, `Set`, and `Map`.**
    *   **`List`:** An ordered collection (sequence) that allows duplicate elements. Elements are accessed by their integer index. (e.g., `ArrayList`, `LinkedList`).
    *   **`Set`:** A collection that contains no duplicate elements. It models the mathematical set abstraction. Elements are not ordered in general. (e.g., `HashSet`, `LinkedHashSet`, `TreeSet`).
    *   **`Map`:** An object that maps keys to values. A `Map` cannot contain duplicate keys; each key can map to at most one value. Keys and values are objects. (e.g., `HashMap`, `LinkedHashMap`, `TreeMap`).

3.  **When would you use `ArrayList` over `LinkedList`?**
    *   **Use `ArrayList` when:**
        *   Frequent random access (getting elements by index) is required, as `ArrayList` offers O(1) for `get(index)`.
        *   Adding/removing elements from the end of the list is common.
        *   Memory overhead is slightly less as it only stores data, not pointers.
    *   **Use `LinkedList` when:**
        *   Frequent insertions and deletions in the middle of the list are required, as `LinkedList` offers O(1) for `add`/`remove` after the node is found (finding the node is O(N)).
        *   You need to use it as a `Stack` or `Queue` (implementing `Deque`).

4.  **What is a `HashSet`? How does it handle duplicates?**
    *   `HashSet` is an implementation of the `Set` interface that stores its elements in a hash table. It offers constant-time performance for the basic operations (`add`, `remove`, `contains`, `size`), assuming the hash function disperses the elements properly among the buckets.
    *   **Handling Duplicates:** `HashSet` does not allow duplicate elements. When you try to `add()` an element that already exists (determined by its `hashCode()` and `equals()` methods), the `add()` method simply returns `false` and the set remains unchanged.

5.  **Explain the concept of fail-fast and fail-safe iterators.**
    *   **Fail-Fast Iterators:**
        *   Iterators provided by `ArrayList`, `HashMap`, etc. (non-synchronized collections).
        *   They immediately throw a `ConcurrentModificationException` if a collection is structurally modified (elements added, removed, or resized) *after* the iterator has been created, except through the iterator's own `remove()` or `add()` methods.
        *   This behavior is for detecting bugs in concurrent modification, not for guaranteeing correctness.
    *   **Fail-Safe Iterators:**
        *   Iterators provided by concurrent collections like `ConcurrentHashMap` or by copying collections (e.g., `CopyOnWriteArrayList`).
        *   They do *not* throw `ConcurrentModificationException`.
        *   They operate on a clone or snapshot of the collection at the time the iterator was created. This means changes to the underlying collection are not reflected in the iterator's view.
        *   This provides thread safety but might not reflect the most up-to-date state of the collection.

#### Practice Questions Solutions:

1.  **Write a program to remove duplicate elements from an `ArrayList` using a `HashSet`.**

    ```java
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class RemoveDuplicates {
        public static <T> List<T> removeDuplicates(List<T> list) {
            Set<T> set = new HashSet<>(list); // Add all elements to a HashSet, which handles duplicates
            return new ArrayList<>(set);      // Convert back to ArrayList
        }

        public static void main(String[] args) {
            List<Integer> numbers = new ArrayList<>(List.of(1, 2, 2, 3, 4, 4, 5, 1));
            System.out.println("Original List: " + numbers);
            List<Integer> uniqueNumbers = removeDuplicates(numbers);
            System.out.println("List with no duplicates: " + uniqueNumbers); // [1, 2, 3, 4, 5] (order might vary)
        }
    }
    ```

2.  **Given a list of strings, count the frequency of each string using a `HashMap`.**

    ```java
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    public class StringFrequency {
        public static Map<String, Integer> countFrequencies(List<String> strings) {
            Map<String, Integer> frequencyMap = new HashMap<>();
            for (String str : strings) {
                frequencyMap.put(str, frequencyMap.getOrDefault(str, 0) + 1);
            }
            return frequencyMap;
        }

        public static void main(String[] args) {
            List<String> words = List.of("apple", "banana", "apple", "orange", "banana", "apple");
            Map<String, Integer> frequencies = countFrequencies(words);
            System.out.println("String Frequencies: " + frequencies); // {banana=2, orange=1, apple=3}
        }
    }
    ```

3.  **Implement a `Stack` using an `ArrayList`.**

    ```java
    import java.util.ArrayList;
    import java.util.EmptyStackException;
    import java.util.List;

    public class MyArrayListStack<T> {
        private List<T> stackList;

        public MyArrayListStack() {
            stackList = new ArrayList<>();
        }

        public void push(T element) {
            stackList.add(element);
        }

        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stackList.remove(stackList.size() - 1);
        }

        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return stackList.get(stackList.size() - 1);
        }

        public boolean isEmpty() {
            return stackList.isEmpty();
        }

        public int size() {
            return stackList.size();
        }

        public static void main(String[] args) {
            MyArrayListStack<String> stack = new MyArrayListStack<>();
            stack.push("A");
            stack.push("B");
            stack.push("C");

            System.out.println("Stack size: " + stack.size()); // 3
            System.out.println("Peek: " + stack.peek());       // C
            System.out.println("Pop: " + stack.pop());         // C
            System.out.println("Stack size after pop: " + stack.size()); // 2
            System.out.println("Pop: " + stack.pop());         // B
            System.out.println("Is Empty: " + stack.isEmpty()); // false
            System.out.println("Pop: " + stack.pop());         // A
            System.out.println("Is Empty: " + stack.isEmpty()); // true
            // stack.pop(); // Throws EmptyStackException
        }
    }
    ```

4.  **Merge two `ArrayLists` into one, ensuring no duplicates.**

    ```java
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class MergeListsNoDuplicates {
        public static <T> List<T> mergeLists(List<T> list1, List<T> list2) {
            Set<T> uniqueElements = new HashSet<>();
            uniqueElements.addAll(list1); // Add all from list1
            uniqueElements.addAll(list2); // Add all from list2 (HashSet handles duplicates)
            return new ArrayList<>(uniqueElements); // Convert back to ArrayList
        }

        public static void main(String[] args) {
            List<Integer> listA = new ArrayList<>(List.of(1, 2, 3, 4));
            List<Integer> listB = new ArrayList<>(List.of(3, 4, 5, 6));

            List<Integer> mergedList = mergeLists(listA, listB);
            System.out.println("Merged list without duplicates: " + mergedList); // [1, 2, 3, 4, 5, 6] (order might vary)
        }
    }
    ```

### Topic 2: `Map` Interface and `Comparable`/`Comparator` - Solutions

#### Interview Questions Solutions:

1.  **Explain the internal working of `HashMap`. How are collisions handled?**
    *   **Internal Working:**
        *   `HashMap` uses an array of `Node` objects (or `Entry` objects in older versions), often called `buckets` or `bins`.
        *   Each `Node` stores a key-value pair, its hash, and a reference to the next node (in case of a linked list or tree).
        *   When `put(key, value)` is called:
            1.  The `hashCode()` of the key is calculated.
            2.  This hash code is then processed (e.g., XORed with its higher bits in Java 8+) to reduce collisions and then mapped to an index in the internal array using a modulus operator. `index = (n - 1) & hash`.
            3.  At this index, the `HashMap` checks for existing entries.
            4.  If no entry exists at that index, the new key-value pair is placed there.
            5.  If an entry exists, `equals()` is used to check if the keys are the same. If yes, the old value is replaced.
            6.  If keys are different (a collision), the new entry is added to the "chain" at that index.
        *   When `get(key)` is called, the same hashing process finds the bucket, then `equals()` is used to find the specific key in the chain.
    *   **Collision Handling:**
        *   `HashMap` primarily uses **Separate Chaining** to handle collisions.
        *   When two different keys hash to the same bucket index, instead of overwriting, the new entry is appended to a linked list at that bucket.
        *   **Treeify Threshold (Java 8+):** If a bucket's linked list grows too long (specifically, if it exceeds `TREEIFY_THRESHOLD`, which is 8), the linked list is converted into a balanced tree (red-black tree) to improve performance from O(N) to O(logN) in the worst case for that bucket. This is done to mitigate denial-of-service attacks by poorly designed hash functions.

2.  **What is the difference between `HashMap`, `TreeMap`, and `LinkedHashMap`?**
    *   **`HashMap`:**
        *   No guaranteed order of elements.
        *   Provides O(1) average time complexity for `put`, `get`, `remove` operations.
        *   Allows one `null` key and multiple `null` values.
        *   Uses hashing for storage.
    *   **`LinkedHashMap`:**
        *   Maintains insertion order (or access order, if configured).
        *   Provides O(1) average time complexity for `put`, `get`, `remove`.
        *   Inherits `HashMap` functionality and adds a doubly-linked list running through its entries.
        *   Useful for building LRU caches.
    *   **`TreeMap`:**
        *   Stores elements in a natural sorting order of keys or by a custom `Comparator` provided at creation.
        *   Provides O(logN) time complexity for `put`, `get`, `remove` operations because it's implemented using a Red-Black tree.
        *   Does not allow `null` keys (as sorting a `null` key is ambiguous), but allows `null` values.
        *   Useful for sorted data or finding elements within a range.

3.  **How do `Comparable` and `Comparator` differ? When would you use each?**
    *   **`Comparable` Interface (`java.lang.Comparable`):**
        *   Provides "natural ordering" for objects of a class.
        *   Implemented by the class itself (e.g., `String`, `Integer` already implement it).
        *   Has a single method: `public int compareTo(T o)`.
        *   Use when objects have a single, obvious way to be ordered.
    *   **`Comparator` Interface (`java.util.Comparator`):**
        *   Provides "custom ordering" for objects.
        *   Implemented as a separate class or using lambda expressions/anonymous inner classes.
        *   Has a single method: `public int compare(T o1, T o2)`.
        *   Use when:
            *   You need multiple ways to sort objects.
            *   You cannot modify the class whose objects you want to sort (e.g., sorting third-party library classes).
            *   The class does not have a natural ordering or its natural ordering is not what you need.

4.  **What is a `ConcurrentHashMap`? Why is it used?**
    *   `ConcurrentHashMap` is a thread-safe implementation of `Map` that provides high performance for concurrent access, especially for read operations.
    *   **How it works (Java 8+):** Instead of locking the entire map (like `HashTable`) or using a segment-based locking mechanism (like pre-Java 8 `ConcurrentHashMap`), it uses a fine-grained locking strategy where only the portion of the array (bucket) being modified is locked. This allows multiple threads to read and write to different parts of the map concurrently. When treeifying, it uses `synchronized` blocks on the root of the tree node.
    *   **Why it's used:**
        *   To provide thread-safe `Map` operations in a multi-threaded environment.
        *   Offers much better concurrency and scalability than `Collections.synchronizedMap()` or `HashTable`.
        *   Guarantees memory visibility for changes and offers "fail-safe" iterators.

5.  **Explain `hashCode()` and `equals()` contract. Why is it important to override both?**
    *   **`hashCode()` and `equals()` Contract:**
        1.  If two objects are equal according to the `equals(Object)` method, then calling the `hashCode()` method on each of the two objects must produce the same integer result.
        2.  If two objects are unequal according to the `equals(Object)` method, it is *not* required that calling the `hashCode()` method on each of the two objects must produce distinct integer results. However, producing distinct hash codes for unequal objects can improve the performance of hash tables.
        3.  If an object's `equals()` method is not modified, then the default `hashCode()` method (from `Object` class) will return distinct integers for distinct objects.
    *   **Importance of Overriding Both:**
        *   When you override `equals()` to define custom equality logic (e.g., two `Person` objects are equal if their `id`s are the same), you **must** also override `hashCode()` to maintain the contract.
        *   **Failure to do so breaks hash-based collections (`HashMap`, `HashSet`):** If `equals()` returns `true` for two objects but their `hashCode()` methods return different values, `HashSet` might store both objects as distinct entries, and `HashMap` might fail to find a value associated with a key that is `equals()` to the one used for insertion. This leads to incorrect behavior and logical errors in your program.

#### Practice Questions Solutions:

1.  **Implement an `LRU Cache` using `LinkedHashMap`.**

    ```java
    import java.util.LinkedHashMap;
    import java.util.Map;

    public class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        public LRUCache(int capacity) {
            // true for access-order, false for insertion-order
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity; // Remove the eldest entry when capacity is exceeded
        }

        public static void main(String[] args) {
            LRUCache<Integer, String> cache = new LRUCache<>(3);
            cache.put(1, "A");
            cache.put(2, "B");
            cache.put(3, "C");
            System.out.println("Initial Cache: " + cache); // {1=A, 2=B, 3=C}

            cache.get(1); // Access A, making it most recently used
            System.out.println("Cache after get(1): " + cache); // {2=B, 3=C, 1=A}

            cache.put(4, "D"); // Add D, 2 should be removed (eldest)
            System.out.println("Cache after put(4,D): " + cache); // {3=C, 1=A, 4=D}

            cache.put(5, "E"); // Add E, 3 should be removed
            System.out.println("Cache after put(5,E): " + cache); // {1=A, 4=D, 5=E}
        }
    }
    ```

2.  **Sort a list of `Employee` objects based on their salary using `Comparator`.**

    ```java
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;

    class Employee {
        String name;
        int id;
        double salary;

        public Employee(String name, int id, double salary) {
            this.name = name;
            this.id = id;
            this.salary = salary;
        }

        public double getSalary() { return salary; }
        public String getName() { return name; }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + "}";
        }
    }

    public class SortEmployees {
        public static void main(String[] args) {
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee("Alice", 101, 75000));
            employees.add(new Employee("Bob", 102, 50000));
            employees.add(new Employee("Charlie", 103, 90000));
            employees.add(new Employee("David", 104, 50000));

            System.out.println("Before sorting: " + employees);

            // Using anonymous inner class for Comparator
            Collections.sort(employees, new Comparator<Employee>() {
                @Override
                public int compare(Employee e1, Employee e2) {
                    // Sort by salary ascending. If salaries are equal, sort by name.
                    int salaryComparison = Double.compare(e1.getSalary(), e2.getSalary());
                    if (salaryComparison == 0) {
                        return e1.getName().compareTo(e2.getName());
                    }
                    return salaryComparison;
                }
            });
            System.out.println("Sorted by Salary (ascending): " + employees);

            // Using lambda expression for Comparator (Java 8+)
            Collections.sort(employees, (e1, e2) -> Double.compare(e2.getSalary(), e1.getSalary()));
            System.out.println("Sorted by Salary (descending using lambda): " + employees);
        }
    }
    ```

3.  **Given a `HashMap` where keys are student IDs and values are `Student` objects, print the students sorted by their names.**

    ```java
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;

    class StudentWithID {
        int id;
        String name;
        int age;

        public StudentWithID(int id, String name, int age) {
            this.id = id;
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getId() { return id; }

        @Override
        public String toString() {
            return "Student{id=" + id + ", name='" + name + "', age=" + age + '}';
        }
    }

    public class SortStudentsByNameFromMap {
        public static void main(String[] args) {
            Map<Integer, StudentWithID> studentMap = new HashMap<>();
            studentMap.put(101, new StudentWithID(101, "Charlie", 20));
            studentMap.put(102, new StudentWithID(102, "Alice", 22));
            studentMap.put(103, new StudentWithID(103, "Bob", 19));

            System.out.println("Original Map: " + studentMap);

            // Get all Student objects from the map's values
            List<StudentWithID> students = new ArrayList<>(studentMap.values());

            // Sort the list of students by name using a Comparator
            Collections.sort(students, Comparator.comparing(StudentWithID::getName));
            // Or using lambda: Collections.sort(students, (s1, s2) -> s1.getName().compareTo(s2.getName()));

            System.out.println("Students sorted by name: " + students);
        }
    }
    ```

4.  **Find the first non-repeated character in a string using a `HashMap`.**

    ```java
    import java.util.LinkedHashMap;
    import java.util.Map;

    public class FirstNonRepeatedCharacter {
        public static Character findFirstNonRepeatedChar(String str) {
            if (str == null || str.isEmpty()) {
                return null;
            }

            // Use LinkedHashMap to maintain insertion order
            Map<Character, Integer> charCounts = new LinkedHashMap<>();

            // Populate the map with character frequencies
            for (char c : str.toCharArray()) {
                charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
            }

            // Iterate through the LinkedHashMap to find the first character with count 1
            for (Map.Entry<Character, Integer> entry : charCounts.entrySet()) {
                if (entry.getValue() == 1) {
                    return entry.getKey();
                }
            }
            return null; // No non-repeated character found
        }

        public static void main(String[] args) {
            System.out.println("First non-repeated in 'swiss': " + findFirstNonRepeatedChar("swiss")); // w
            System.out.println("First non-repeated in 'teeter': " + findFirstNonRepeatedChar("teeter")); // r
            System.out.println("First non-repeated in 'aabbcc': " + findFirstNonRepeatedChar("aabbcc")); // null
            System.out.println("First non-repeated in 'java': " + findFirstNonRepeatedChar("java"));   // j
            System.out.println("First non-repeated in '': " + findFirstNonRepeatedChar(""));         // null
        }
    }
    ```

## Week 3: Data Structures (Arrays, Strings, Linked Lists)

### Topic 1: Arrays and Strings - Solutions

#### Interview Questions Solutions:

1.  **Explain the difference between `==` and `.equals()` for strings.**
    *   **`==` operator:** Compares object references. For strings, it checks if two string references point to the exact same object in memory (either in the string pool or on the heap).
    *   **`.equals()` method:** Compares the actual content (character sequence) of two string objects. It's an overridden method in the `String` class to provide value-based comparison.
    *   **Example:**
        ```java
        String s1 = "hello";         // In String pool
        String s2 = "hello";         // In String pool, references same object as s1
        String s3 = new String("hello"); // On heap, new object
        System.out.println(s1 == s2);         // true
        System.out.println(s1 == s3);         // false
        System.out.println(s1.equals(s3));    // true
        ```

2.  **Why are strings immutable in Java? What are the benefits?**
    *   **Immutable:** Once a `String` object is created, its content cannot be changed. Any operation that appears to modify a string (like `concat()`, `substring()`, `replace()`) actually creates a new `String` object.
    *   **Benefits:**
        1.  **Thread Safety:** Immutable objects are inherently thread-safe because their state cannot change after creation, eliminating concerns about synchronization for their content.
        2.  **Security:** Used extensively for sensitive information like passwords, usernames, and network connections. Immutability prevents unauthorized modifications.
        3.  **Caching (`String Pool`):** Java can implement a "string pool" (a special memory area) where identical string literals share the same object. This saves memory and improves performance.
        4.  **Hash Codes:** Since string content doesn't change, `hashCode()` can be cached and computed only once, leading to efficient use in `HashMap` and `HashSet`.
        5.  **Used as Map Keys/Set Elements:** Their immutability and consistent `hashCode()` make them ideal for keys in `HashMap` and elements in `HashSet`.

3.  **When would you use `StringBuilder` over `StringBuffer`?**
    *   **`StringBuilder`:**
        *   **Non-synchronized (not thread-safe).**
        *   Generally faster than `StringBuffer`.
        *   Use in single-threaded environments or when thread safety is handled externally.
    *   **`StringBuffer`:**
        *   **Synchronized (thread-safe).**
        *   Generally slower than `StringBuilder` due to synchronization overhead.
        *   Use in multi-threaded environments where multiple threads might access or modify the same string buffer concurrently.
    *   In most modern applications, `StringBuilder` is preferred unless explicit thread safety is required for string manipulation, as external synchronization mechanisms (like `locks` or `ExecutorService`) are often used to manage concurrency more granularly.

4.  **How do you declare and initialize a multi-dimensional array?**
    *   A multi-dimensional array is an array of arrays. The most common is a 2D array (matrix).
    *   **Declaration:**
        ```java
        int[][] matrix; // or int matrix[][];
        ```
    *   **Initialization (fixed size):**
        ```java
        int[][] matrix = new int[3][4]; // 3 rows, 4 columns
        ```
    *   **Initialization (with values):**
        ```java
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        ```
    *   **Jagged Arrays (rows can have different lengths):**
        ```java
        int[][] jaggedArray = new int[3][];
        jaggedArray[0] = new int[2];
        jaggedArray[1] = new int[4];
        jaggedArray[2] = new int[3];
        ```

5.  **What is `ArrayIndexOutOfBoundsException`?**
    *   It's a runtime exception that occurs when a program tries to access an array element using an index that is either negative or greater than or equal to the size of the array.
    *   Array indices in Java are 0-based, meaning they range from `0` to `length - 1`.
    *   **Example:**
        ```java
        int[] arr = new int[5]; // Valid indices: 0, 1, 2, 3, 4
        System.out.println(arr[5]); // This would throw ArrayIndexOutOfBoundsException
        ```

#### Practice Questions Solutions:

1.  **Find the largest and smallest element in an array.**

    ```java
    public class MinMaxArray {
        public static void findMinMax(int[] arr) {
            if (arr == null || arr.length == 0) {
                System.out.println("Array is empty or null.");
                return;
            }

            int min = arr[0];
            int max = arr[0];

            for (int i = 1; i < arr.length; i++) {
                if (arr[i] < min) {
                    min = arr[i];
                }
                if (arr[i] > max) {
                    max = arr[i];
                }
            }
            System.out.println("Smallest element: " + min);
            System.out.println("Largest element: " + max);
        }

        public static void main(String[] args) {
            int[] numbers = {5, 2, 8, 1, 9, 3};
            findMinMax(numbers); // Smallest: 1, Largest: 9
        }
    }
    ```

2.  **Check if two strings are anagrams of each other.** (Anagrams are strings that contain the same characters with the same frequencies, but in a different order).

    ```java
    import java.util.Arrays;

    public class AnagramChecker {
        public static boolean areAnagrams(String s1, String s2) {
            if (s1 == null || s2 == null) {
                return false;
            }
            if (s1.length() != s2.length()) {
                return false;
            }

            char[] charArray1 = s1.toCharArray();
            char[] charArray2 = s2.toCharArray();

            Arrays.sort(charArray1);
            Arrays.sort(charArray2);

            return Arrays.equals(charArray1, charArray2);
        }

        public static void main(String[] args) {
            System.out.println("'listen' and 'silent' are anagrams: " + areAnagrams("listen", "silent")); // true
            System.out.println("'hello' and 'world' are anagrams: " + areAnagrams("hello", "world"));   // false
            System.out.println("'Debit Card' and 'Bad Credit' are anagrams: " + areAnagrams("Debit Card".replaceAll("\\s", "").toLowerCase(), "Bad Credit".replaceAll("\\s", "").toLowerCase())); // true
        }
    }
    ```
    *Alternative using a frequency map (HashMap or array for ASCII chars):*
    ```java
    import java.util.HashMap;
    import java.util.Map;

    public class AnagramCheckerMap {
        public static boolean areAnagrams(String s1, String s2) {
            if (s1 == null || s2 == null) return false;
            if (s1.length() != s2.length()) return false;

            Map<Character, Integer> counts = new HashMap<>();
            for (char c : s1.toCharArray()) {
                counts.put(c, counts.getOrDefault(c, 0) + 1);
            }
            for (char c : s2.toCharArray()) {
                if (!counts.containsKey(c) || counts.get(c) == 0) {
                    return false;
                }
                counts.put(c, counts.get(c) - 1);
            }
            return true;
        }

        public static void main(String[] args) {
            System.out.println("map 'listen' and 'silent' are anagrams: " + areAnagrams("listen", "silent")); // true
        }
    }
    ```

3.  **Implement a function to reverse words in a sentence (e.g., "Hello World" -> "World Hello").**

    ```java
    public class ReverseWordsInSentence {
        public static String reverseWords(String sentence) {
            if (sentence == null || sentence.trim().isEmpty()) {
                return sentence;
            }
            String[] words = sentence.trim().split("\\s+"); // Split by one or more spaces
            StringBuilder reversedSentence = new StringBuilder();
            for (int i = words.length - 1; i >= 0; i--) {
                reversedSentence.append(words[i]);
                if (i > 0) {
                    reversedSentence.append(" ");
                }
            }
            return reversedSentence.toString();
        }

        public static void main(String[] args) {
            System.out.println("Reversed: '" + reverseWords("Hello World") + "'");           // World Hello
            System.out.println("Reversed: '" + reverseWords("Java is fun") + "'");           // fun is Java
            System.out.println("Reversed: '" + reverseWords("  The   quick brown fox ") + "'"); // fox brown quick The
        }
    }
    ```

4.  **Remove a specific element from an array.** (Note: Arrays in Java have fixed size. "Removing" usually means creating a new array or shifting elements and ignoring the last one).

    ```java
    import java.util.Arrays;

    public class RemoveElementFromArray {
        public static int[] removeElement(int[] arr, int elementToRemove) {
            if (arr == null || arr.length == 0) {
                return new int[0];
            }

            int count = 0;
            for (int x : arr) {
                if (x == elementToRemove) {
                    count++;
                }
            }

            if (count == 0) { // Element not found
                return Arrays.copyOf(arr, arr.length); // Return a copy of the original
            }

            int[] newArray = new int[arr.length - count];
            int newIndex = 0;
            for (int i = 0; i < arr.length; i++) {
                if (arr[i] != elementToRemove) {
                    newArray[newIndex++] = arr[i];
                }
            }
            return newArray;
        }

        public static void main(String[] args) {
            int[] numbers = {1, 2, 3, 2, 4, 5};
            System.out.println("Original Array: " + Arrays.toString(numbers));

            int[] modifiedArray = removeElement(numbers, 2);
            System.out.println("Array after removing 2: " + Arrays.toString(modifiedArray)); // [1, 3, 4, 5]

            int[] noChangeArray = removeElement(numbers, 99);
            System.out.println("Array after removing 99: " + Arrays.toString(noChangeArray)); // [1, 2, 3, 2, 4, 5]
        }
    }
    ```

### Topic 2: Linked Lists - Solutions

#### Interview Questions Solutions:

1.  **What is a `LinkedList`? How does it differ from an `ArrayList`?**
    *   **`LinkedList`:** A linear data structure where elements are not stored in contiguous memory locations. Instead, each element (node) contains data and a pointer (or reference) to the next node in the sequence. Doubly linked lists also have a pointer to the previous node.
    *   **Differences from `ArrayList`:**
        *   **Memory Allocation:** `ArrayList` uses a dynamic array (contiguous memory). `LinkedList` uses nodes that can be scattered in memory.
        *   **Access Time:** `ArrayList` provides O(1) random access by index. `LinkedList` provides O(N) access by index (requires traversal).
        *   **Insertion/Deletion:** `ArrayList` is O(N) for insertions/deletions in the middle (requires shifting elements). `LinkedList` is O(1) for insertions/deletions in the middle *if the node is already found* (just update pointers), but O(N) to find the node.
        *   **Memory Overhead:** `LinkedList` has higher memory overhead per element due to storing pointers.
        *   **Use Cases:** `ArrayList` for frequent reads/random access. `LinkedList` for frequent insertions/deletions at ends or in the middle (if iterating to position).

2.  **Explain the concept of singly, doubly, and circularly linked lists.**
    *   **Singly Linked List:**
        *   Each node contains data and a pointer (`next`) to the *next* node in the sequence.
        *   Traversal is only possible in one direction (forward).
        *   Efficient for insertions/deletions at the beginning.
    *   **Doubly Linked List:**
        *   Each node contains data, a pointer (`next`) to the next node, and a pointer (`prev`) to the *previous* node.
        *   Traversal is possible in both forward and backward directions.
        *   More memory overhead than singly linked lists due to the extra pointer.
        *   Efficient for insertions/deletions at any position (if the node is known) and for backward traversal.
    *   **Circularly Linked List:**
        *   The last node's `next` pointer points back to the first node (head), forming a circle.
        *   Can be singly or doubly linked.
        *   No `null` pointers, making some operations simpler (e.g., traversing the entire list from any starting point).
        *   Useful for circular buffers, round-robin scheduling.

3.  **How do you add/delete a node in a `LinkedList`?**
    *   **Adding a Node (Singly Linked List):**
        *   **At Head:** `newNode.next = head; head = newNode;` (O(1))
        *   **At End:** Traverse to the last node, `lastNode.next = newNode; newNode.next = null;` (O(N) for traversal, O(1) if tail pointer is maintained).
        *   **In Middle:** Find the node `prevNode` after which to insert. `newNode.next = prevNode.next; prevNode.next = newNode;` (O(N) to find `prevNode`, O(1) for insertion).
    *   **Deleting a Node (Singly Linked List):**
        *   **At Head:** `head = head.next;` (O(1))
        *   **At End:** Traverse to the node *before* the last one (`secondToLastNode`). `secondToLastNode.next = null;` (O(N) for traversal).
        *   **In Middle:** Find the node `prevNode` before the node to delete. `prevNode.next = prevNode.next.next;` (O(N) to find `prevNode`, O(1) for deletion).

4.  **What are the time complexities of common `LinkedList` operations?**
    *   **`addFirst()` / `removeFirst()`:** O(1)
    *   **`addLast()` / `removeLast()`:** O(1) (if tail pointer is maintained), O(N) otherwise for singly.
    *   **`get(index)`:** O(N) (requires traversal).
    *   **`add(index, element)` / `remove(index)`:** O(N) (requires traversal to the index).
    *   **`contains(element)`:** O(N) (requires traversal).

5.  **How do you find the middle element of a `LinkedList` in one pass?**
    *   Use the **"Tortoise and Hare"** algorithm (two-pointer approach).
    *   Initialize two pointers, `slow` and `fast`, both pointing to the head.
    *   Move `slow` one step at a time.
    *   Move `fast` two steps at a time.
    *   When `fast` reaches the end of the list (or `fast.next` is null for even-length lists), `slow` will be at the middle element.

    ```java
    public ListNode findMiddle(ListNode head) {
        if (head == null) {
            return null;
        }
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow; // slow is now at the middle
    }
    ```

#### Practice Questions Solutions:

1.  **Implement a function to reverse a singly `LinkedList`.**

    ```java
    class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
        // For printing purposes
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            ListNode current = this;
            while (current != null) {
                sb.append(current.val).append(" -> ");
                current = current.next;
            }
            sb.append("null");
            return sb.toString();
        }
    }

    public class ReverseLinkedList {
        public static ListNode reverseList(ListNode head) {
            ListNode prev = null;
            ListNode current = head;
            while (current != null) {
                ListNode nextTemp = current.next; // Save next node
                current.next = prev;              // Reverse current node's pointer
                prev = current;                   // Move prev to current node
                current = nextTemp;               // Move current to next node
            }
            return prev; // prev will be the new head
        }

        public static void main(String[] args) {
            ListNode head = new ListNode(1);
            head.next = new ListNode(2);
            head.next.next = new ListNode(3);
            head.next.next.next = new ListNode(4);

            System.out.println("Original List: " + head); // 1 -> 2 -> 3 -> 4 -> null
            ListNode reversedHead = reverseList(head);
            System.out.println("Reversed List: " + reversedHead); // 4 -> 3 -> 2 -> 1 -> null
        }
    }
    ```

2.  **Detect if a `LinkedList` has a cycle.**

    ```java
    // ListNode definition from above can be reused.

    public class LinkedListCycleDetector {
        public static boolean hasCycle(ListNode head) {
            if (head == null || head.next == null) {
                return false;
            }

            ListNode slow = head;
            ListNode fast = head;

            while (fast != null && fast.next != null) {
                slow = slow.next;        // Moves one step
                fast = fast.next.next;   // Moves two steps

                if (slow == fast) {      // If they meet, a cycle exists
                    return true;
                }
            }
            return false; // Fast reached null, no cycle
        }

        public static void main(String[] args) {
            // List with no cycle: 1 -> 2 -> 3 -> null
            ListNode head1 = new ListNode(1);
            head1.next = new ListNode(2);
            head1.next.next = new ListNode(3);
            System.out.println("List 1 has cycle: " + hasCycle(head1)); // false

            // List with cycle: 1 -> 2 -> 3 -> 2 (points back to node 2)
            ListNode head2 = new ListNode(1);
            ListNode node2 = new ListNode(2);
            ListNode node3 = new ListNode(3);
            head2.next = node2;
            node2.next = node3;
            node3.next = node2; // Creates a cycle
            System.out.println("List 2 has cycle: " + hasCycle(head2)); // true
        }
    }
    ```

3.  **Merge two sorted `LinkedLists`.**

    ```java
    // ListNode definition from above can be reused.

    public class MergeSortedLinkedLists {
        public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
            if (l1 == null) return l2;
            if (l2 == null) return l1;

            ListNode dummyHead = new ListNode(0); // Dummy node to simplify head handling
            ListNode current = dummyHead;

            while (l1 != null && l2 != null) {
                if (l1.val <= l2.val) {
                    current.next = l1;
                    l1 = l1.next;
                } else {
                    current.next = l2;
                    l2 = l2.next;
                }
                current = current.next;
            }

            // Append remaining nodes if any
            if (l1 != null) {
                current.next = l1;
            } else if (l2 != null) {
                current.next = l2;
            }

            return dummyHead.next; // The actual head of the merged list
        }

        public static void main(String[] args) {
            // List 1: 1 -> 3 -> 5
            ListNode l1 = new ListNode(1);
            l1.next = new ListNode(3);
            l1.next.next = new ListNode(5);

            // List 2: 2 -> 4 -> 6
            ListNode l2 = new ListNode(2);
            l2.next = new ListNode(4);
            l2.next.next = new ListNode(6);

            System.out.println("List 1: " + l1);
            System.out.println("List 2: " + l2);

            ListNode mergedHead = mergeTwoLists(l1, l2);
            System.out.println("Merged List: " + mergedHead); // 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> null
        }
    }
    ```

4.  **Remove the Nth node from the end of a `LinkedList`.**

    ```java
    // ListNode definition from above can be reused.

    public class RemoveNthFromEnd {
        public static ListNode removeNthFromEnd(ListNode head, int n) {
            if (head == null || n <= 0) {
                return head;
            }

            ListNode dummy = new ListNode(0); // Dummy node to handle case where head is removed
            dummy.next = head;

            ListNode fast = dummy;
            ListNode slow = dummy;

            // Move fast pointer N steps ahead
            for (int i = 0; i < n; i++) {
                if (fast == null) { // N is greater than list length
                    return head;
                }
                fast = fast.next;
            }

            // Move both pointers until fast reaches the end
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next;
            }

            // slow is now at the node just before the one to be removed
            if (slow.next != null) {
                 slow.next = slow.next.next;
            }

            return dummy.next; // Return the new head
        }

        public static void main(String[] args) {
            // List: 1 -> 2 -> 3 -> 4 -> 5
            ListNode head = new ListNode(1);
            head.next = new ListNode(2);
            head.next.next = new ListNode(3);
            head.next.next.next = new ListNode(4);
            head.next.next.next.next = new ListNode(5);

            System.out.println("Original List: " + head);

            // Remove 2nd from end (which is 4)
            ListNode result1 = removeNthFromEnd(head, 2);
            System.out.println("After removing 2nd from end: " + result1); // 1 -> 2 -> 3 -> 5 -> null

            // Recreate list for another test
            head = new ListNode(1);
            head.next = new ListNode(2);
            System.out.println("\nOriginal List: " + head);
            ListNode result2 = removeNthFromEnd(head, 2); // Remove 2nd from end (which is 1)
            System.out.println("After removing 2nd from end: " + result2); // 2 -> null

            // Recreate list for another test
            head = new ListNode(1);
            System.out.println("\nOriginal List: " + head);
            ListNode result3 = removeNthFromEnd(head, 1); // Remove 1st from end (which is 1)
            System.out.println("After removing 1st from end: " + result3); // null
        }
    }
    ```

## Week 4: Data Structures (Stacks, Queues, Trees)

### Topic 1: Stacks and Queues - Solutions

#### Interview Questions Solutions:

1.  **Explain `Stack` and `Queue`. What are their primary operations?**
    *   **Stack:**
        *   A linear data structure that follows the **LIFO (Last In, First Out)** principle.
        *   Analogy: A stack of plates – the last plate added is the first one removed.
        *   **Primary Operations:**
            *   `push(element)`: Adds an element to the top of the stack.
            *   `pop()`: Removes and returns the element from the top of the stack.
            *   `peek()`: Returns the element at the top of the stack without removing it.
            *   `isEmpty()`: Checks if the stack is empty.
            *   `size()`: Returns the number of elements in the stack.
    *   **Queue:**
        *   A linear data structure that follows the **FIFO (First In, First Out)** principle.
        *   Analogy: A line of people waiting – the first person in line is the first one served.
        *   **Primary Operations:**
            *   `enqueue(element)` / `offer(element)`: Adds an element to the rear (tail) of the queue.
            *   `dequeue()` / `poll()`: Removes and returns the element from the front (head) of the queue.
            *   `peek()` / `element()`: Returns the element at the front of the queue without removing it.
            *   `isEmpty()`: Checks if the queue is empty.
            *   `size()`: Returns the number of elements in the queue.

2.  **How would you implement a `Stack` using an `ArrayList`?**
    *   (Refer to Week 2, Topic 1, Practice Question 3 for code example).
    *   An `ArrayList` can be used to implement a `Stack` by always adding/removing elements from the end of the `ArrayList`.
    *   `push()`: `list.add(element)` (adds to the end).
    *   `pop()`: `list.remove(list.size() - 1)` (removes from the end).
    *   `peek()`: `list.get(list.size() - 1)` (gets from the end).
    *   This provides O(1) average time complexity for all `Stack` operations.

3.  **How would you implement a `Queue` using two `Stacks`?**

    ```java
    import java.util.Stack;
    import java.util.EmptyStackException;

    public class QueueUsingTwoStacks<T> {
        private Stack<T> inStack;    // For enqueue operations
        private Stack<T> outStack;   // For dequeue operations

        public QueueUsingTwoStacks() {
            inStack = new Stack<>();
            outStack = new Stack<>();
        }

        // Add element to the rear of the queue
        public void enqueue(T element) {
            inStack.push(element);
        }

        // Remove element from the front of the queue
        public T dequeue() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty.");
            }
            if (outStack.isEmpty()) {
                // Transfer elements from inStack to outStack
                while (!inStack.isEmpty()) {
                    outStack.push(inStack.pop());
                }
            }
            return outStack.pop();
        }

        // Peek at the front element without removing
        public T peek() {
            if (isEmpty()) {
                throw new IllegalStateException("Queue is empty.");
            }
            if (outStack.isEmpty()) {
                while (!inStack.isEmpty()) {
                    outStack.push(inStack.pop());
                }
            }
            return outStack.peek();
        }

        public boolean isEmpty() {
            return inStack.isEmpty() && outStack.isEmpty();
        }

        public int size() {
            return inStack.size() + outStack.size();
        }

        public static void main(String[] args) {
            QueueUsingTwoStacks<Integer> queue = new QueueUsingTwoStacks<>();
            queue.enqueue(1);
            queue.enqueue(2);
            queue.enqueue(3);
            System.out.println("Dequeued: " + queue.dequeue()); // 1
            queue.enqueue(4);
            System.out.println("Peek: " + queue.peek());     // 2
            System.out.println("Dequeued: " + queue.dequeue()); // 2
            System.out.println("Dequeued: " + queue.dequeue()); // 3
            System.out.println("Dequeued: " + queue.dequeue()); // 4
            System.out.println("Is Empty: " + queue.isEmpty()); // true
        }
    }
    ```

4.  **Give real-world examples where `Stack` and `Queue` are used.**
    *   **`Stack` Applications:**
        *   **Browser History:** Back/Forward buttons in web browsers (LIFO).
        *   **Undo/Redo Functionality:** In text editors or software.
        *   **Function Call Stack:** Managing function calls during program execution.
        *   **Expression Evaluation:** Converting infix to postfix, evaluating postfix expressions.
        *   **Balanced Parentheses:** Checking if parentheses, brackets, and braces are balanced.
        *   **Depth-First Search (DFS):** Can be implemented using a stack (explicitly or implicitly via recursion).
    *   **`Queue` Applications:**
        *   **Printer Spooler:** Jobs are printed in the order they are submitted.
        *   **Operating System Scheduling:** CPU task scheduling (e.g., Round Robin).
        *   **Message Queues:** In distributed systems for inter-process communication.
        *   **Breadth-First Search (BFS):** Graph traversal algorithm.
        *   **Waiting Lists:** Customer service call centers, ticket queues.

5.  **What is the time complexity of push/pop/enqueue/dequeue operations?**
    *   **For `Stack` (using `ArrayList` or `LinkedList` as underlying):**
        *   `push()`: O(1) (amortized for `ArrayList`, always for `LinkedList`)
        *   `pop()`: O(1)
        *   `peek()`: O(1)
    *   **For `Queue` (using `LinkedList` as underlying):**
        *   `enqueue()` / `offer()`: O(1)
        *   `dequeue()` / `poll()`: O(1)
        *   `peek()` / `element()`: O(1)
    *   **For `Queue` implemented using `ArrayList` (less efficient):**
        *   `enqueue()`: O(1) (add to end)
        *   `dequeue()`: O(N) (remove from beginning requires shifting all elements)

#### Practice Questions Solutions:

1.  **Check if a string of parentheses is balanced using a `Stack`.**

    ```java
    import java.util.Stack;

    public class BalancedParentheses {
        public static boolean isBalanced(String s) {
            Stack<Character> stack = new Stack<>();
            for (char c : s.toCharArray()) {
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                } else if (c == ')' || c == ']' || c == '}') {
                    if (stack.isEmpty()) { // Closing bracket without an opening one
                        return false;
                    }
                    char top = stack.pop();
                    if ((c == ')' && top != '(') ||
                        (c == ']' && top != '[') ||
                        (c == '}' && top != '{')) {
                        return false; // Mismatched brackets
                    }
                }
            }
            return stack.isEmpty(); // True if all opening brackets have been matched
        }

        public static void main(String[] args) {
            System.out.println("([]{}) is balanced: " + isBalanced("([]{})")); // true
            System.out.println("([)] is balanced: " + isBalanced("([)]"));     // false
            System.out.println("{[ is balanced: " + isBalanced("{["));         // false
            System.out.println("} is balanced: " + isBalanced("}"));           // false
            System.out.println(" is balanced: " + isBalanced(""));             // true
        }
    }
    ```

2.  **Implement a simple browser history using a `Stack`.**

    ```java
    import java.util.Stack;

    public class BrowserHistory {
        private Stack<String> backStack;
        private Stack<String> forwardStack;
        private String currentPage;

        public BrowserHistory(String homepage) {
            backStack = new Stack<>();
            forwardStack = new Stack<>();
            currentPage = homepage;
            System.out.println("Visited: " + homepage);
        }

        public void visit(String url) {
            backStack.push(currentPage); // Push current page to back stack
            currentPage = url;           // Update current page
            forwardStack.clear();        // Clear forward stack on new visit
            System.out.println("Visited: " + url);
        }

        public String back(int steps) {
            while (steps > 0 && !backStack.isEmpty()) {
                forwardStack.push(currentPage);
                currentPage = backStack.pop();
                steps--;
            }
            System.out.println("Moved back to: " + currentPage);
            return currentPage;
        }

        public String forward(int steps) {
            while (steps > 0 && !forwardStack.isEmpty()) {
                backStack.push(currentPage);
                currentPage = forwardStack.pop();
                steps--;
            }
            System.out.println("Moved forward to: " + currentPage);
            return currentPage;
        }

        public static void main(String[] args) {
            BrowserHistory browser = new BrowserHistory("leetcode.com");
            browser.visit("google.com");
            browser.visit("facebook.com");
            browser.visit("youtube.com");

            browser.back(1);    // facebook.com
            browser.back(1);    // google.com
            browser.forward(1); // facebook.com
            browser.visit("linkedin.com"); // youtube.com is cleared from forward
            browser.forward(2); // linkedin.com (no more forward pages)
            browser.back(2);    // google.com
        }
    }
    ```

3.  **Find the next greater element for each element in an array using a `Stack`.** (For an element `x`, the next greater element is the first element to its right that is greater than `x`).

    ```java
    import java.util.Arrays;
    import java.util.Stack;

    public class NextGreaterElement {
        public static int[] findNextGreaterElements(int[] nums) {
            int n = nums.length;
            int[] result = new int[n];
            Arrays.fill(result, -1); // Initialize result with -1 (no greater element)
            Stack<Integer> stack = new Stack<>(); // Stores indices

            for (int i = 0; i < n; i++) {
                // While stack is not empty and the element at stack's top index
                // is less than the current element, pop and set its next greater element
                while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                    result[stack.pop()] = nums[i];
                }
                stack.push(i); // Push current element's index onto stack
            }
            return result;
        }

        public static void main(String[] args) {
            int[] nums1 = {4, 5, 2, 25};
            System.out.println("Array: " + Arrays.toString(nums1));
            System.out.println("Next Greater Elements: " + Arrays.toString(findNextGreaterElements(nums1))); // [5, 25, 25, -1]

            int[] nums2 = {13, 7, 6, 12};
            System.out.println("Array: " + Arrays.toString(nums2));
            System.out.println("Next Greater Elements: " + Arrays.toString(findNextGreaterElements(nums2))); // [-1, 12, 12, -1]
        }
    }
    ```

4.  **Implement a `Queue` using a circular array.**

    ```java
    public class CircularArrayQueue<T> {
        private T[] queueArray;
        private int head; // Index of the front element
        private int tail; // Index where the next element will be added
        private int size; // Current number of elements
        private int capacity;

        @SuppressWarnings("unchecked")
        public CircularArrayQueue(int capacity) {
            this.capacity = capacity;
            this.queueArray = (T[]) new Object[capacity];
            this.head = 0;
            this.tail = 0;
            this.size = 0;
        }

        public boolean enqueue(T element) {
            if (isFull()) {
                // System.out.println("Queue is full, cannot enqueue " + element);
                return false;
            }
            queueArray[tail] = element;
            tail = (tail + 1) % capacity; // Wrap around if necessary
            size++;
            return true;
        }

        public T dequeue() {
            if (isEmpty()) {
                // System.out.println("Queue is empty, cannot dequeue.");
                return null;
            }
            T element = queueArray[head];
            queueArray[head] = null; // Clear the reference for garbage collection
            head = (head + 1) % capacity; // Wrap around if necessary
            size--;
            return element;
        }

        public T peek() {
            if (isEmpty()) {
                return null;
            }
            return queueArray[head];
        }

        public boolean isEmpty() {
            return size == 0;
        }

        public boolean isFull() {
            return size == capacity;
        }

        public int size() {
            return size;
        }

        public static void main(String[] args) {
            CircularArrayQueue<Integer> queue = new CircularArrayQueue<>(5); // Capacity 5

            queue.enqueue(10);
            queue.enqueue(20);
            queue.enqueue(30);
            System.out.println("Queue size: " + queue.size()); // 3
            System.out.println("Peek: " + queue.peek());       // 10

            System.out.println("Dequeued: " + queue.dequeue()); // 10
            System.out.println("Queue size: " + queue.size()); // 2
            System.out.println("Peek: " + queue.peek());       // 20

            queue.enqueue(40);
            queue.enqueue(50);
            queue.enqueue(60); // Queue is full, returns false or throws exception
            System.out.println("Queue size: " + queue.size()); // 5
            System.out.println("Is full: " + queue.isFull());  // true

            System.out.println("Dequeued: " + queue.dequeue()); // 20
            System.out.println("Dequeued: " + queue.dequeue()); // 30
            queue.enqueue(70); // Space available now
            System.out.println("Queue size: " + queue.size()); // 4
            System.out.println("Peek: " + queue.peek());       // 40 (wraps around)
        }
    }
    ```

### Topic 2: Trees (Binary Trees, BST) - Solutions

#### Interview Questions Solutions:

1.  **What is a Binary Tree? What is a Binary Search Tree (BST)?**
    *   **Binary Tree:** A tree data structure where each node has at most two children, referred to as the left child and the right child.
    *   **Binary Search Tree (BST):** A special type of binary tree with the following properties (for all nodes `N`):
        *   The value of all nodes in `N`'s left subtree is less than `N`'s value.
        *   The value of all nodes in `N`'s right subtree is greater than `N`'s value.
        *   Both the left and right subtrees are also BSTs.
        *   This property allows for efficient searching, insertion, and deletion.

2.  **Explain the different types of tree traversals (inorder, preorder, postorder).**
    *   **Tree Traversal:** The process of visiting each node in a tree exactly once.
    *   **Inorder Traversal (Left -> Root -> Right):**
        1.  Traverse the left subtree.
        2.  Visit the current node (root).
        3.  Traverse the right subtree.
        *   For a BST, inorder traversal yields elements in sorted order.
    *   **Preorder Traversal (Root -> Left -> Right):**
        1.  Visit the current node (root).
        2.  Traverse the left subtree.
        3.  Traverse the right subtree.
        *   Used to create a copy of the tree or to prefix expressions.
    *   **Postorder Traversal (Left -> Right -> Root):**
        1.  Traverse the left subtree.
        2.  Traverse the right subtree.
        3.  Visit the current node (root).
        *   Used to delete a tree or to postfix expressions.

3.  **How do you insert and delete elements in a BST?**
    *   **Insertion:**
        1.  Start at the root.
        2.  Compare the new value with the current node's value.
        3.  If the new value is less, go to the left child; if greater, go to the right child.
        4.  Repeat until a null child pointer is found.
        5.  Insert the new node as the left or right child at that null position.
    *   **Deletion (More complex, 3 cases):**
        1.  **Node is a Leaf:** Simply remove the node.
        2.  **Node has one Child:** Replace the node with its single child.
        3.  **Node has two Children:**
            *   Find the **inorder successor** (smallest node in the right subtree) or **inorder predecessor** (largest node in the left subtree).
            *   Replace the value of the node to be deleted with the value of its inorder successor/predecessor.
            *   Delete the inorder successor/predecessor (which will be a leaf or have one child, making its deletion simpler).

4.  **What are the advantages of a BST? What are its disadvantages?**
    *   **Advantages:**
        *   **Efficient Searching:** Average O(logN) time to find an element (if balanced).
        *   **Efficient Insertion/Deletion:** Average O(logN) time (if balanced).
        *   **Ordered Elements:** Inorder traversal provides sorted data.
        *   **Good for Dynamic Data:** Data can be added or removed efficiently.
    *   **Disadvantages:**
        *   **Worst-Case Performance:** If the tree becomes skewed (e.g., elements inserted in sorted order), it degenerates into a linked list, leading to O(N) for search, insert, delete operations.
        *   **Balancing:** To guarantee O(logN) performance, the tree needs to be balanced (e.g., AVL trees, Red-Black trees), which adds complexity to implementation.
        *   **Higher Memory Usage:** Each node requires pointers to children (and possibly parent), consuming more memory than arrays for simple lists.

5.  **Explain the difference between a complete binary tree and a full binary tree.**
    *   **Full Binary Tree:** A binary tree in which every node has either 0 or 2 children. No node has only one child.
    *   **Complete Binary Tree:** A binary tree in which all levels are completely filled except possibly the last level, and the last level has all its nodes as far left as possible.
        *   A perfect binary tree (all levels completely filled, leaves at the same depth) is always a complete binary tree.
        *   A complete binary tree can be efficiently represented using an array.

#### Practice Questions Solutions:

1.  **Implement preorder and postorder traversals for a binary tree.**

    ```java
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public class TreeTraversals {
        // Preorder Traversal: Root -> Left -> Right
        public void preorderTraversal(TreeNode node) {
            if (node == null) {
                return;
            }
            System.out.print(node.val + " "); // Visit root
            preorderTraversal(node.left);     // Traverse left
            preorderTraversal(node.right);    // Traverse right
        }

        // Postorder Traversal: Left -> Right -> Root
        public void postorderTraversal(TreeNode node) {
            if (node == null) {
                return;
            }
            postorderTraversal(node.left);      // Traverse left
            postorderTraversal(node.right);     // Traverse right
            System.out.print(node.val + " ");  // Visit root
        }

        public static void main(String[] args) {
            // Constructing a sample tree:
            //       1
            //      / \
            //     2   3
            //    / \
            //   4   5
            TreeNode root = new TreeNode(1);
            root.left = new TreeNode(2);
            root.right = new TreeNode(3);
            root.left.left = new TreeNode(4);
            root.left.right = new TreeNode(5);

            TreeTraversals tree = new TreeTraversals();

            System.out.print("Preorder Traversal: ");
            tree.preorderTraversal(root); // Output: 1 2 4 5 3
            System.out.println();

            System.out.print("Postorder Traversal: ");
            tree.postorderTraversal(root); // Output: 4 5 2 3 1
            System.out.println();
        }
    }
    ```

2.  **Find the height of a binary tree.** (Height is the number of edges on the longest path from the root to a leaf. A single node tree has height 0).

    ```java
    // TreeNode definition from above can be reused.

    public class TreeHeight {
        public static int calculateHeight(TreeNode root) {
            if (root == null) {
                return -1; // Height of an empty tree is -1 (or 0 if defined as number of nodes)
            }
            int leftHeight = calculateHeight(root.left);
            int rightHeight = calculateHeight(root.right);

            // Height is 1 + maximum of left and right subtree heights
            return 1 + Math.max(leftHeight, rightHeight);
        }

        public static void main(String[] args) {
            // Sample tree:
            //       1
            //      / \
            //     2   3
            //    / \
            //   4   5
            TreeNode root = new TreeNode(1);
            root.left = new TreeNode(2);
            root.right = new TreeNode(3);
            root.left.left = new TreeNode(4);
            root.left.right = new TreeNode(5);

            System.out.println("Height of the tree: " + calculateHeight(root)); // Output: 2

            // Single node tree
            TreeNode singleNode = new TreeNode(10);
            System.out.println("Height of single node tree: " + calculateHeight(singleNode)); // Output: 0

            // Empty tree
            System.out.println("Height of empty tree: " + calculateHeight(null)); // Output: -1
        }
    }
    ```

3.  **Check if a given binary tree is a BST.**

    ```java
    // TreeNode definition from above can be reused.

    public class IsBinarySearchTree {

        // Method to check if a tree is a BST by ensuring each node satisfies the BST property
        // This helper method takes min and max bounds
        private boolean isValidBST(TreeNode node, Integer min, Integer max) {
            if (node == null) {
                return true;
            }

            // Current node's value must be within the min and max bounds
            if (min != null && node.val <= min) {
                return false;
            }
            if (max != null && node.val >= max) {
                return false;
            }

            // Recursively check left subtree (values must be less than current node.val)
            // and right subtree (values must be greater than current node.val)
            return isValidBST(node.left, min, node.val) &&
                   isValidBST(node.right, node.val, max);
        }

        public boolean isValidBST(TreeNode root) {
            return isValidBST(root, null, null);
        }

        // Alternative: Inorder Traversal approach
        // If an inorder traversal of a BST is performed, the elements should be in sorted order.
        private TreeNode prev; // Stores the previously visited node in inorder traversal

        public boolean isValidBSTInorder(TreeNode root) {
            prev = null; // Reset prev for each call
            return checkInorder(root);
        }

        private boolean checkInorder(TreeNode node) {
            if (node == null) {
                return true;
            }

            if (!checkInorder(node.left)) {
                return false;
            }

            // Check if current node's value is greater than previous node's value
            if (prev != null && node.val <= prev.val) {
                return false;
            }
            prev = node; // Update prev to current node

            return checkInorder(node.right);
        }

        public static void main(String[] args) {
            IsBinarySearchTree checker = new IsBinarySearchTree();

            // Valid BST:
            //       4
            //      / \
            //     2   5
            //    / \
            //   1   3
            TreeNode root1 = new TreeNode(4);
            root1.left = new TreeNode(2);
            root1.right = new TreeNode(5);
            root1.left.left = new TreeNode(1);
            root1.left.right = new TreeNode(3);
            System.out.println("Tree 1 is a BST (Bounds Method): " + checker.isValidBST(root1)); // true
            System.out.println("Tree 1 is a BST (Inorder Method): " + checker.isValidBSTInorder(root1)); // true


            // Invalid BST: (5 is in left subtree of 4, but is > 4)
            //       4
            //      / \
            //     2   5
            //    / \
            //   1   5
            TreeNode root2 = new TreeNode(4);
            root2.left = new TreeNode(2);
            root2.right = new TreeNode(5);
            root2.left.left = new TreeNode(1);
            root2.left.right = new TreeNode(5); // Invalid element
            System.out.println("Tree 2 is a BST (Bounds Method): " + checker.isValidBST(root2)); // false
            System.out.println("Tree 2 is a BST (Inorder Method): " + checker.isValidBSTInorder(root2)); // false

            // Invalid BST: (3 is in right subtree of 2, but is in left of 4, valid)
            // Example for (node.val <= min) check
            //      2
            //     / \
            //    1   3
            TreeNode root3 = new TreeNode(2);
            root3.left = new TreeNode(1);
            root3.right = new TreeNode(3);
            System.out.println("Tree 3 is a BST (Bounds Method): " + checker.isValidBST(root3)); // true

            // Invalid BST: root = 5, left = 1, right = 4 (right child must be greater)
            //      5
            //     / \
            //    1   4
            TreeNode root4 = new TreeNode(5);
            root4.left = new TreeNode(1);
            root4.right = new TreeNode(4);
            System.out.println("Tree 4 is a BST (Bounds Method): " + checker.isValidBST(root4)); // false
            System.out.println("Tree 4 is a BST (Inorder Method): " + checker.isValidBSTInorder(root4)); // false
        }
    }
    ```

# Tree Interview Solutions

## 1. Invert a Binary Tree (Mirror Image)

**Problem:** Given the `root` of a binary tree, invert the tree, and return its `root`.

**Explanation:** Inverting a binary tree means swapping the left and right children for every node in the tree. This can be achieved recursively by traversing the tree. For each node, we swap its left and right children, then recursively call the invert function on its new left child and new right child.

**Java Code:**

 ```java

    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    class Solution {
        public TreeNode invertTree(TreeNode root) {
            if (root == null) {
                return null;
            }

            // Swap the left and right children
            TreeNode temp = root.left;
            root.left = root.right;
            root.right = temp;

            // Recursively invert the left and right subtrees
            invertTree(root.left);
            invertTree(root.right);

            return root;
        }
    }    
 ```

**Interview Perspective:**

*   **Time Complexity:** O(N), where N is the number of nodes in the tree, as we visit each node exactly once.
*   **Space Complexity:** O(H) in the worst case (skewed tree) for the recursion stack, where H is the height of the tree. In the best case (balanced tree), it's O(log N).
*   **Key Concept:** Recursion, understanding of tree traversal.
*   **Follow-up:** Can you do this iteratively? (Using a queue for BFS-like approach or a stack for DFS-like approach).

## 2. Breadth-First Search (BFS) for a Tree

**Problem:** Implement BFS for a given tree, printing the node values level by level.

**Explanation:** BFS explores a tree level by level. It uses a queue to keep track of the nodes to visit. We start by adding the root to the queue. Then, we repeatedly dequeue a node, process it (e.g., print its value), and then enqueue its children (if they exist).

**Java Code:**

 ```java
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.List;
    import java.util.ArrayList;

    class Solution {
        public List<List<Integer>> levelOrder(TreeNode root) {
            List<List<Integer>> result = new ArrayList<>();
            if (root == null) {
                return result;
            }

            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                int levelSize = queue.size();
                List<Integer> currentLevel = new ArrayList<>();
                for (int i = 0; i < levelSize; i++) {
                    TreeNode currentNode = queue.poll();
                    currentLevel.add(currentNode.val);

                    if (currentNode.left != null) {
                        queue.offer(currentNode.left);
                    }
                    if (currentNode.right != null) {
                        queue.offer(currentNode.right);
                    }
                }
                result.add(currentLevel);
            }
            return result;
        }
    }
 ```

**Interview Perspective:**

*   **Time Complexity:** O(N), where N is the number of nodes, as each node is enqueued and dequeued once.
*   **Space Complexity:** O(W) in the worst case, where W is the maximum width of the tree (number of nodes at the widest level). In the worst case (complete binary tree), this can be O(N/2), which simplifies to O(N).
*   **Key Concept:** Queue data structure, level-by-level traversal.
*   **Follow-up:** How would you modify this to get the average of nodes at each level? Or the maximum/minimum?

## 3. Depth-First Search (DFS) for a Tree

**Problem:** Implement DFS for a given tree. Provide implementations for all three common DFS traversals: pre-order, in-order, and post-order.

**Explanation:** DFS explores as far as possible along each branch before backtracking. It can be implemented recursively or iteratively using a stack.

**Java Code (Recursive):**

 ```java
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Stack; // For iterative approach

    class Solution {
        // Pre-order Traversal (Root, Left, Right)
        public List<Integer> preorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            preorder(root, result);
            return result;
        }

        private void preorder(TreeNode node, List<Integer> result) {
            if (node == null) {
                return;
            }
            result.add(node.val);      // Visit root
            preorder(node.left, result);  // Traverse left
            preorder(node.right, result); // Traverse right
        }

        // In-order Traversal (Left, Root, Right)
        public List<Integer> inorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            inorder(root, result);
            return result;
        }

        private void inorder(TreeNode node, List<Integer> result) {
            if (node == null) {
                return;
            }
            inorder(node.left, result);   // Traverse left
            result.add(node.val);       // Visit root
            inorder(node.right, result);  // Traverse right
        }

        // Post-order Traversal (Left, Right, Root)
        public List<Integer> postorderTraversal(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            postorder(root, result);
            return result;
        }

        private void postorder(TreeNode node, List<Integer> result) {
            if (node == null) {
                return;
            }
            postorder(node.left, result);   // Traverse left
            postorder(node.right, result);  // Traverse right
            result.add(node.val);         // Visit root
        }

        // Iterative Pre-order Traversal using Stack
        public List<Integer> preorderTraversalIterative(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) {
                return result;
            }
            Stack<TreeNode> stack = new Stack<>();
            stack.push(root);

            while (!stack.isEmpty()) {
                TreeNode node = stack.pop();
                result.add(node.val);
                if (node.right != null) { // Push right first so left is processed first
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                }
            }
            return result;
        }

        // Iterative In-order Traversal using Stack
        public List<Integer> inorderTraversalIterative(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) {
                return result;
            }
            Stack<TreeNode> stack = new Stack<>();
            TreeNode current = root;

            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }
                current = stack.pop();
                result.add(current.val);
                current = current.right;
            }
            return result;
        }

        // Iterative Post-order Traversal using Two Stacks (or one stack with extra check)
        public List<Integer> postorderTraversalIterative(TreeNode root) {
            List<Integer> result = new ArrayList<>();
            if (root == null) {
                return result;
            }
            Stack<TreeNode> s1 = new Stack<>();
            Stack<TreeNode> s2 = new Stack<>();
            s1.push(root);

            while (!s1.isEmpty()) {
                TreeNode node = s1.pop();
                s2.push(node);
                if (node.left != null) {
                    s1.push(node.left);
                }
                if (node.right != null) {
                    s1.push(node.right);
                }
            }

            while (!s2.isEmpty()) {
                result.add(s2.pop().val);
            }
            return result;
        }
    }
 ```

**Interview Perspective:**

*   **Time Complexity (All DFS):** O(N), as each node is visited once.
*   **Space Complexity (Recursive DFS):** O(H) for the recursion stack, where H is the height of the tree.
*   **Space Complexity (Iterative DFS):** O(H) for the stack in the worst case (skewed tree).
*   **Key Concept:** Stack data structure for iterative approaches, understanding of recursion.
*   **Difference between traversals:** Pre-order is often used for creating a copy of the tree or expression trees. In-order is useful for sorted output in a Binary Search Tree. Post-order is used for deleting nodes in a tree or evaluating expression trees.
