# 4-Month Java Interview Preparation Guide

This guide provides a comprehensive preparation plan for Java developers aiming to ace their interviews. It covers essential topics, includes code snippets, explains concepts from an interview perspective, and offers practice questions with solutions.

## Month 1: Fundamentals and Data Structures

### Week 1: Java Basics and OOPs

#### Topic 1: Java Language Fundamentals

*   **Interview Perspective:** Expect questions on basic syntax, data types, operators, control flow statements, and how Java manages memory (JVM, JRE, JDK differences). Focus on `final` keyword usage and immutable objects.

*   **Code Snippet: `Final` keyword**

    ```java
    public class FinalKeywordDemo {
        final int fixedValue = 10; // final variable
        final class MyFinalClass { } // final class, cannot be subclassed

        public final void display() { // final method, cannot be overridden
            System.out.println("This is a final method.");
        }

        public static void main(String[] args) {
            FinalKeywordDemo obj = new FinalKeywordDemo();
            // obj.fixedValue = 20; // Compile-time error: cannot assign a value to final variable fixedValue
            System.out.println(obj.fixedValue);
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain the difference between `JVM`, `JRE`, and `JDK`.
    2.  What are the primitive data types in Java?
    3.  What is the purpose of the `static` keyword?
    4.  Differentiate between `final`, `finally`, and `finalize`.
    5.  What is method overloading and overriding?

*   **Practice Questions:**
    1.  Write a program to reverse a string without using built-in methods.
    2.  Implement a program to check if a number is prime.
    3.  Create a class `Calculator` with methods for addition, subtraction, multiplication, and division.
    4.  Explain the concept of autoboxing and unboxing with an example.

#### Topic 2: Object-Oriented Programming (OOPs) Concepts

*   **Interview Perspective:** This is a cornerstone. Be prepared to explain and demonstrate all four pillars: Encapsulation, Inheritance, Polymorphism, and Abstraction. Discuss real-world examples and design patterns related to OOP.

*   **Code Snippet: Polymorphism (Method Overriding)**

    ```java
    class Animal {
        void makeSound() {
            System.out.println("Animal makes a sound");
        }
    }

    class Dog extends Animal {
        @Override
        void makeSound() {
            System.out.println("Dog barks");
        }
    }

    class Cat extends Animal {
        @Override
        void makeSound() {
            System.out.println("Cat meows");
        }
    }

    public class PolymorphismDemo {
        public static void main(String[] args) {
            Animal myDog = new Dog(); // Polymorphism
            Animal myCat = new Cat(); // Polymorphism

            myDog.makeSound(); // Calls Dog's makeSound()
            myCat.makeSound(); // Calls Cat's makeSound()
        }
    }
    ```

*   **Interview Questions:**
    1.  Define Encapsulation. How is it achieved in Java?
    2.  What is the difference between an abstract class and an interface?
    3.  Explain method overloading vs. method overriding with examples.
    4.  What is aggregation and composition?
    5.  Discuss the "is-a" and "has-a" relationships in OOP.

*   **Practice Questions:**
    1.  Design a system for a `Shape` hierarchy (Circle, Rectangle, Triangle) demonstrating inheritance and polymorphism. Calculate area for each.
    2.  Create an interface `Flyable` and implement it for `Bird` and `Aeroplane` classes.
    3.  Design a `Bank` class with `Account` objects, demonstrating encapsulation and composition.

### Week 2: Collections Framework

#### Topic 1: Introduction to Collections

*   **Interview Perspective:** The Collections Framework is heavily tested. Understand the hierarchy (`List`, `Set`, `Map`), their implementations, and when to use each. Be ready to discuss their time complexities for common operations.

*   **Code Snippet: `ArrayList` and `HashSet`**

    ```java
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class CollectionsIntroDemo {
        public static void main(String[] args) {
            // List: ordered, allows duplicates
            List<String> namesList = new ArrayList<>();
            namesList.add("Alice");
            namesList.add("Bob");
            namesList.add("Alice");
            System.out.println("ArrayList: " + namesList); // [Alice, Bob, Alice]

            // Set: unordered, no duplicates
            Set<String> namesSet = new HashSet<>();
            namesSet.add("Alice");
            namesSet.add("Bob");
            namesSet.add("Alice");
            System.out.println("HashSet: " + namesSet); // [Alice, Bob] (order might vary)
        }
    }
    ```

*   **Interview Questions:**
    1.  What is the Java Collections Framework?
    2.  Differentiate between `List`, `Set`, and `Map`.
    3.  When would you use `ArrayList` over `LinkedList`?
    4.  What is a `HashSet`? How does it handle duplicates?
    5.  Explain the concept of fail-fast and fail-safe iterators.

*   **Practice Questions:**
    1.  Write a program to remove duplicate elements from an `ArrayList` using a `HashSet`.
    2.  Given a list of strings, count the frequency of each string using a `HashMap`.
    3.  Implement a `Stack` using an `ArrayList`.
    4.  Merge two `ArrayLists` into one, ensuring no duplicates.

#### Topic 2: `Map` Interface and `Comparable`/`Comparator`

*   **Interview Perspective:** `HashMap` internal working (hashing, collision resolution) is a common deep-dive question. Also, `Comparable` and `Comparator` for custom sorting are crucial.

*   **Code Snippet: `HashMap` and Custom Sorting**

    ```java
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Comparator;

    class Student implements Comparable<Student> {
        String name;
        int age;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student{" + "name='" + name + '\'' + ", age=" + age + '}';
        }

        @Override
        public int compareTo(Student other) {
            return this.name.compareTo(other.name); // Sort by name (natural ordering)
        }
    }

    public class MapAndSortingDemo {
        public static void main(String[] args) {
            // HashMap Demo
            Map<Integer, String> studentMap = new HashMap<>();
            studentMap.put(1, "Alice");
            studentMap.put(2, "Bob");
            studentMap.put(3, "Charlie");
            System.out.println("HashMap: " + studentMap); // {1=Alice, 2=Bob, 3=Charlie}

            // Custom Sorting with Comparable
            List<Student> students = new ArrayList<>();
            students.add(new Student("Bob", 20));
            students.add(new Student("Alice", 22));
            students.add(new Student("Charlie", 19));

            Collections.sort(students); // Sorts using Student's compareTo (by name)
            System.out.println("Students sorted by name (Comparable): " + students);

            // Custom Sorting with Comparator (Sort by age)
            Collections.sort(students, new Comparator<Student>() {
                @Override
                public int compare(Student s1, Student s2) {
                    return Integer.compare(s1.age, s2.age);
                }
            });
            System.out.println("Students sorted by age (Comparator): " + students);
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain the internal working of `HashMap`. How are collisions handled?
    2.  What is the difference between `HashMap`, `TreeMap`, and `LinkedHashMap`?
    3.  How do `Comparable` and `Comparator` differ? When would you use each?
    4.  What is a `ConcurrentHashMap`? Why is it used?
    5.  Explain `hashCode()` and `equals()` contract. Why is it important to override both?

*   **Practice Questions:**
    1.  Implement a `LRU Cache` using `LinkedHashMap`.
    2.  Sort a list of `Employee` objects based on their salary using `Comparator`.
    3.  Given a `HashMap` where keys are student IDs and values are `Student` objects, print the students sorted by their names.
    4.  Find the first non-repeated character in a string using a `HashMap`.

### Week 3: Data Structures (Arrays, Strings, Linked Lists)

#### Topic 1: Arrays and Strings

*   **Interview Perspective:** Basic array operations, searching, sorting, and manipulation are fundamental. String immutability and `StringBuilder`/`StringBuffer` are frequently asked.

*   **Code Snippet: Array and String operations**

    ```java
    public class ArrayAndStringDemo {
        public static void main(String[] args) {
            // Arrays
            int[] numbers = {5, 2, 8, 1, 9};
            System.out.println("Array elements:");
            for (int num : numbers) {
                System.out.print(num + " ");
            }
            System.out.println();

            java.util.Arrays.sort(numbers); // Sorting an array
            System.out.println("Sorted array: " + java.util.Arrays.toString(numbers));

            // Strings
            String s1 = "hello";
            String s2 = "hello";
            String s3 = new String("hello");

            System.out.println("s1 == s2: " + (s1 == s2)); // true (String Pool)
            System.out.println("s1 == s3: " + (s1 == s3)); // false (Different objects in heap)
            System.out.println("s1.equals(s3): " + (s1.equals(s3))); // true (Content comparison)

            // StringBuilder for mutable strings
            StringBuilder sb = new StringBuilder("Java");
            sb.append(" Programming");
            sb.insert(4, " is fun");
            System.out.println("StringBuilder: " + sb.toString());
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain the difference between `==` and `.equals()` for strings.
    2.  Why are strings immutable in Java? What are the benefits?
    3.  When would you use `StringBuilder` over `StringBuffer`?
    4.  How do you declare and initialize a multi-dimensional array?
    5.  What is `ArrayIndexOutOfBoundsException`?

*   **Practice Questions:**
    1.  Find the largest and smallest element in an array.
    2.  Check if two strings are anagrams of each other.
    3.  Implement a function to reverse words in a sentence (e.g., "Hello World" -> "World Hello").
    4.  Remove a specific element from an array.

#### Topic 2: Linked Lists

*   **Interview Perspective:** Understand the concept of nodes, pointers, and the trade-offs between `ArrayList` and `LinkedList`. Be able to implement basic `LinkedList` operations from scratch.

*   **Code Snippet: Singly Linked List (basic node structure)**

    ```java
    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }
    }

    public class LinkedListDemo {
        public static void main(String[] args) {
            // Creating a simple linked list: 1 -> 2 -> 3
            ListNode head = new ListNode(1);
            ListNode second = new ListNode(2);
            ListNode third = new ListNode(3);

            head.next = second;
            second.next = third;

            // Traversing the linked list
            ListNode current = head;
            System.out.print("Linked List: ");
            while (current != null) {
                System.out.print(current.val + " -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }
    ```

*   **Interview Questions:**
    1.  What is a `LinkedList`? How does it differ from an `ArrayList`?
    2.  Explain the concept of singly, doubly, and circularly linked lists.
    3.  How do you add/delete a node in a `LinkedList`?
    4.  What are the time complexities of common `LinkedList` operations?
    5.  How do you find the middle element of a `LinkedList` in one pass?

*   **Practice Questions:**
    1.  Implement a function to reverse a singly `LinkedList`.
    2.  Detect if a `LinkedList` has a cycle.
    3.  Merge two sorted `LinkedLists`.
    4.  Remove the Nth node from the end of a `LinkedList`.

### Week 4: Data Structures (Stacks, Queues, Trees)

#### Topic 1: Stacks and Queues

*   **Interview Perspective:** Understand LIFO/FIFO principles. Implementations using arrays or linked lists are good to know. Common applications like balanced parentheses or BFS/DFS (conceptually).

*   **Code Snippet: `Stack` and `Queue`**

    ```java
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.Stack;

    public class StackQueueDemo {
        public static void main(String[] args) {
            // Stack (LIFO)
            Stack<String> browserHistory = new Stack<>();
            browserHistory.push("Google");
            browserHistory.push("Facebook");
            browserHistory.push("Twitter");
            System.out.println("Stack: " + browserHistory); // [Google, Facebook, Twitter]
            System.out.println("Pop: " + browserHistory.pop()); // Twitter
            System.out.println("Peek: " + browserHistory.peek()); // Facebook
            System.out.println("Stack after pop and peek: " + browserHistory); // [Google, Facebook]

            // Queue (FIFO) - using LinkedList as implementation
            Queue<String> printerQueue = new LinkedList<>();
            printerQueue.offer("Doc1");
            printerQueue.offer("Doc2");
            printerQueue.offer("Doc3");
            System.out.println("Queue: " + printerQueue); // [Doc1, Doc2, Doc3]
            System.out.println("Poll: " + printerQueue.poll()); // Doc1
            System.out.println("Peek: " + printerQueue.peek()); // Doc2
            System.out.println("Queue after poll and peek: " + printerQueue); // [Doc2, Doc3]
        }
    }
    ```

*   **Interview Questions:**
    1.  Explain `Stack` and `Queue`. What are their primary operations?
    2.  How would you implement a `Stack` using an `ArrayList`?
    3.  How would you implement a `Queue` using two `Stacks`?
    4.  Give real-world examples where `Stack` and `Queue` are used.
    5.  What is the time complexity of push/pop/enqueue/dequeue operations?

*   **Practice Questions:**
    1.  Check if a string of parentheses is balanced using a `Stack`.
    2.  Implement a simple browser history using a `Stack`.
    3.  Find the next greater element for each element in an array using a `Stack`.
    4.  Implement a `Queue` using a circular array.

#### Topic 2: Trees (Binary Trees, BST)

*   **Interview Perspective:** Understand tree terminology (root, node, leaf, height, depth). Be able to perform traversals (inorder, preorder, postorder) and identify properties of Binary Search Trees (BSTs).

*   **Code Snippet: Binary Tree Node and Inorder Traversal**

    ```java
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public class BinaryTreeDemo {
        public void inorderTraversal(TreeNode node) {
            if (node == null) {
                return;
            }
            inorderTraversal(node.left);
            System.out.print(node.val + " ");
            inorderTraversal(node.right);
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

            BinaryTreeDemo tree = new BinaryTreeDemo();
            System.out.print("Inorder Traversal: ");
            tree.inorderTraversal(root); // Output: 4 2 5 1 3
            System.out.println();
        }
    }
    ```

*   **Interview Questions:**
    1.  What is a Binary Tree? What is a Binary Search Tree (BST)?
    2.  Explain the different types of tree traversals (inorder, preorder, postorder).
    3.  How do you insert and delete elements in a BST?
    4.  What are the advantages of a BST? What are its disadvantages?
    5.  Explain the difference between a complete binary tree and a full binary tree.

*   **Practice Questions:**
    1.  Implement preorder and postorder traversals for a binary tree.
    2.  Find the height of a binary tree.
    3.  Check if a given binary tree is a BST.
    4.  Invert a binary tree (mirror image).
    5.  Implement Breadth-First Search (BFS) and Depth-First Search (DFS) for a tree.

---