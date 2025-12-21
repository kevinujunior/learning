# Java Interview Preparation Guide: Indexed Solutions

This document provides example solutions to a selection of practice questions from the 4-month Java Interview Preparation Guide. Each solution includes the problem statement, code (where applicable), logical explanation, and complexity analysis.

---

## Month 1: Fundamentals and Data Structures - Solutions

### Week 1: Java Basics and OOPs

#### Topic 1: Java Language Fundamentals

**Practice Question 1:** Write a program to reverse a string without using built-in methods.

*   **Problem Statement:** Given a string, return a new string with the characters in reverse order.
*   **Solution Code:**
    ```java
    public class StringReverser {
        public static String reverseString(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            char[] charArray = str.toCharArray();
            int left = 0;
            int right = charArray.length - 1;

            while (left < right) {
                // Swap characters
                char temp = charArray[left];
                charArray[left] = charArray[right];
                charArray[right] = temp;
                left++;
                right--;
            }
            return new String(charArray);
        }

        public static void main(String[] args) {
            System.out.println(reverseString("hello")); // Output: olleh
            System.out.println(reverseString("Java"));  // Output: avaJ
            System.out.println(reverseString("a"));     // Output: a
            System.out.println(reverseString(""));      // Output:
            System.out.println(reverseString(null));    // Output: null
        }
    }
    ```
*   **Explanation:**
    1.  Handle edge cases: null or empty strings are returned as is.
    2.  Convert the string to a `char[]` array, as strings are immutable in Java.
    3.  Use two pointers, `left` starting at the beginning and `right` at the end of the array.
    4.  Iterate while `left` is less than `right`, swapping the characters at these positions.
    5.  Increment `left` and decrement `right` in each iteration.
    6.  Convert the modified `char[]` back to a `String`.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N), where N is the length of the string, as we iterate through approximately half the string once.
    *   **Space Complexity:** O(N) to convert the string to a character array.

#### Topic 2: Object-Oriented Programming (OOPs) Concepts

**Practice Question 1:** Design a system for a `Shape` hierarchy (Circle, Rectangle, Triangle) demonstrating inheritance and polymorphism. Calculate area for each.

*   **Problem Statement:** Create a base `Shape` class with an abstract `calculateArea()` method. Implement `Circle`, `Rectangle`, and `Triangle` subclasses, overriding `calculateArea()` appropriately. Demonstrate polymorphism by calculating areas of different shapes from a list of `Shape` objects.
*   **Solution Code:**
    ```java
    abstract class Shape {
        public abstract double calculateArea();
        public void display() {
            System.out.println("This is a shape.");
        }
    }

    class Circle extends Shape {
        private double radius;

        public Circle(double radius) {
            this.radius = radius;
        }

        @Override
        public double calculateArea() {
            return Math.PI * radius * radius;
        }

        @Override
        public void display() {
            System.out.println("Circle with radius " + radius + ", Area: " + calculateArea());
        }
    }

    class Rectangle extends Shape {
        private double length;
        private double width;

        public Rectangle(double length, double width) {
            this.length = length;
            this.width = width;
        }

        @Override
        public double calculateArea() {
            return length * width;
        }

        @Override
        public void display() {
            System.out.println("Rectangle with length " + length + " and width " + width + ", Area: " + calculateArea());
        }
    }

    class Triangle extends Shape {
        private double base;
        private double height;

        public Triangle(double base, double height) {
            this.base = base;
            this.height = height;
        }

        @Override
        public double calculateArea() {
            return 0.5 * base * height;
        }

        @Override
        public void display() {
            System.out.println("Triangle with base " + base + " and height " + height + ", Area: " + calculateArea());
        }
    }

    public class ShapeHierarchyDemo {
        public static void main(String[] args) {
            // Polymorphism in action
            Shape circle = new Circle(5.0);
            Shape rectangle = new Rectangle(4.0, 6.0);
            Shape triangle = new Triangle(3.0, 8.0);

            // Storing different shapes in a list of the base type
            java.util.List<Shape> shapes = new java.util.ArrayList<>();
            shapes.add(circle);
            shapes.add(rectangle);
            shapes.add(triangle);

            for (Shape shape : shapes) {
                shape.display(); // Polymorphic method call
            }
            // Output:
            // Circle with radius 5.0, Area: 78.53981633974483
            // Rectangle with length 4.0 and width 6.0, Area: 24.0
            // Triangle with base 3.0 and height 8.0, Area: 12.0
        }
    }
    ```
*   **Explanation:**
    1.  An `abstract class Shape` defines the common interface (`calculateArea()`) but leaves the implementation to subclasses. This demonstrates **Abstraction**.
    2.  `Circle`, `Rectangle`, `Triangle` **inherit** from `Shape`, providing their specific area calculation logic.
    3.  The `calculateArea()` method is **overridden** in each subclass.
    4.  In `main`, we create instances of different shapes and store them in a `List<Shape>`. When `shape.display()` is called, the correct `calculateArea()` method (from the actual object type) is invoked, demonstrating **Polymorphism**.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(1) for each `calculateArea()` call, as it involves fixed number of arithmetic operations.
    *   **Space Complexity:** O(1) for each `Shape` object.

---

### Week 2: Collections Framework

#### Topic 1: Introduction to Collections

**Practice Question 1:** Write a program to remove duplicate elements from an `ArrayList` using a `HashSet`.

*   **Problem Statement:** Given an `ArrayList` that may contain duplicate elements, return a new `ArrayList` with only unique elements.
*   **Solution Code:**
    ```java
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class RemoveDuplicates {
        public static <T> List<T> removeDuplicates(List<T> list) {
            // 1. Add all elements to a HashSet (automatically handles duplicates)
            Set<T> set = new HashSet<>(list);
            // 2. Convert the Set back to an ArrayList
            return new ArrayList<>(set);
        }

        public static void main(String[] args) {
            List<Integer> numbersWithDuplicates = new ArrayList<>();
            numbersWithDuplicates.add(1);
            numbersWithDuplicates.add(2);
            numbersWithDuplicates.add(1);
            numbersWithDuplicates.add(3);
            numbersWithDuplicates.add(2);
            numbersWithDuplicates.add(4);

            System.out.println("Original List: " + numbersWithDuplicates);
            List<Integer> uniqueNumbers = removeDuplicates(numbersWithDuplicates);
            System.out.println("Unique List: " + uniqueNumbers); // Output: [1, 2, 3, 4] (order not guaranteed)

            List<String> namesWithDuplicates = new ArrayList<>();
            namesWithDuplicates.add("Alice");
            namesWithDuplicates.add("Bob");
            namesWithDuplicates.add("Alice");
            namesWithDuplicates.add("Charlie");
            System.out.println("Original Names: " + namesWithDuplicates);
            List<String> uniqueNames = removeDuplicates(namesWithDuplicates);
            System.out.println("Unique Names: " + uniqueNames); // Output: [Alice, Bob, Charlie] (order not guaranteed)
        }
    }
    ```
*   **Explanation:**
    1.  Leverage the fundamental property of a `HashSet`: it does not allow duplicate elements.
    2.  Create a `HashSet` and initialize it with the given `ArrayList`. The `HashSet` constructor will iterate through the `ArrayList` and add each element. If an element already exists, it won't be added again.
    3.  Create a new `ArrayList` and initialize it with the `HashSet`. This converts the unique elements back into a `List`.
    4.  This method uses generics (`<T>`) to make it reusable for any object type.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N) on average, where N is the number of elements in the `ArrayList`. Adding elements to a `HashSet` takes average O(1) time.
    *   **Space Complexity:** O(N) to store elements in the `HashSet` and the new `ArrayList`.

#### Topic 2: `Map` Interface and `Comparable`/`Comparator`

**Practice Question 2:** Sort a list of `Employee` objects based on their salary using `Comparator`.

*   **Problem Statement:** Given a list of `Employee` objects, each with a `name` and `salary`, sort them in ascending order of salary.
*   **Solution Code:**
    ```java
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.List;

    class Employee {
        private String name;
        private double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }

        @Override
        public String toString() {
            return "Employee{name='" + name + "', salary=" + salary + '}';
        }
    }

    public class EmployeeSortDemo {
        public static void main(String[] args) {
            List<Employee> employees = new ArrayList<>();
            employees.add(new Employee("Alice", 75000));
            employees.add(new Employee("Bob", 60000));
            employees.add(new Employee("Charlie", 90000));
            employees.add(new Employee("David", 60000)); // Same salary as Bob

            System.out.println("Employees before sorting: " + employees);

            // Sort using a Comparator (lambda expression for conciseness)
            Collections.sort(employees, (e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
            // Or using Comparator.comparingDouble
            // Collections.sort(employees, Comparator.comparingDouble(Employee::getSalary));


            System.out.println("Employees after sorting by salary: " + employees);
            // Expected Output (salaries in ascending order, stable sort for equal salaries):
            // Employees after sorting by salary: [Employee{name='Bob', salary=60000.0}, Employee{name='David', salary=60000.0}, Employee{name='Alice', salary=75000.0}, Employee{name='Charlie', salary=90000.0}]
        }
    }
    ```
*   **Explanation:**
    1.  The `Employee` class is a plain old Java object (POJO) with `name` and `salary`. It doesn't implement `Comparable` because we want to define an *external* sorting logic using `Comparator`.
    2.  `Collections.sort()` is used, which accepts a `List` and a `Comparator`.
    3.  A lambda expression `(e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())` is provided as the `Comparator`. This lambda implements the `compare` method of the `Comparator` interface.
    4.  `Double.compare(val1, val2)` is a convenient static method to compare two double values, returning a negative integer, zero, or a positive integer as `val1` is less than, equal to, or greater than `val2`.
    5.  Alternatively, `Comparator.comparingDouble(Employee::getSalary)` provides an even more concise way using method references.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N log N), where N is the number of employees, due to the `Collections.sort()` method which typically uses a highly optimized Merge Sort or Tim Sort algorithm.
    *   **Space Complexity:** O(log N) or O(N) depending on the specific sort implementation (Tim Sort often uses O(N) in worst case for temporary arrays).

---

### Week 3: Data Structures (Arrays, Strings, Linked Lists)

#### Topic 1: Arrays and Strings

**Practice Question 2:** Check if two strings are anagrams of each other.

*   **Problem Statement:** Given two strings, determine if they are anagrams. Anagrams are words or phrases formed by rearranging the letters of a different word or phrase, typically using all the original letters exactly once.
*   **Solution Code:**
    ```java
    import java.util.Arrays;
    import java.util.HashMap;
    import java.util.Map;

    public class AnagramChecker {

        // Method 1: Using sorting (simple, but less efficient for very long strings)
        public static boolean areAnagramsSorting(String s1, String s2) {
            if (s1 == null || s2 == null) {
                return false; // Or throw IllegalArgumentException
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

        // Method 2: Using character count (more efficient)
        public static boolean areAnagramsCounting(String s1, String s2) {
            if (s1 == null || s2 == null) {
                return false;
            }
            if (s1.length() != s2.length()) {
                return false;
            }

            // Assuming ASCII characters (256 characters)
            int[] charCounts = new int[256];

            for (char c : s1.toCharArray()) {
                charCounts[c]++;
            }

            for (char c : s2.toCharArray()) {
                charCounts[c]--;
                if (charCounts[c] < 0) { // Found more of a char in s2 than s1
                    return false;
                }
            }

            // All counts should be zero if they are anagrams.
            // No need to check explicitly here, as the previous loop already ensures this
            // for characters present in s2. Any characters in s1 not in s2 would leave
            // positive counts, but this is handled by length check.
            return true;
        }

        public static void main(String[] args) {
            System.out.println("Sorting Method:");
            System.out.println(areAnagramsSorting("listen", "silent"));  // true
            System.out.println(areAnagramsSorting("hello", "world"));   // false
            System.out.println(areAnagramsSorting("anagram", "nagaram")); // true
            System.out.println(areAnagramsSorting("rat", "car"));      // false

            System.out.println("\nCounting Method:");
            System.out.println(areAnagramsCounting("listen", "silent"));  // true
            System.out.println(areAnagramsCounting("hello", "world"));   // false
            System.out.println(areAnagramsCounting("anagram", "nagaram")); // true
            System.out.println(areAnagramsCounting("rat", "car"));      // false
        }
    }
    ```
*   **Explanation:**
    *   **Method 1 (Sorting):**
        1.  Check for null strings and unequal lengths (anagrams must have the same length).
        2.  Convert both strings to character arrays.
        3.  Sort both character arrays.
        4.  Compare the sorted arrays. If they are identical, the strings are anagrams.
    *   **Method 2 (Counting):**
        1.  Check for null strings and unequal lengths.
        2.  Create an integer array `charCounts` of size 256 (for ASCII characters) initialized to zeros.
        3.  Iterate through the first string: for each character, increment its count in `charCounts`.
        4.  Iterate through the second string: for each character, decrement its count. If at any point a count drops below zero, it means the second string has more of that character than the first, so they cannot be anagrams.
        5.  If the loop completes, it implies all characters in `s2` were present in `s1` with equal or fewer occurrences. Since the lengths are equal, all counts must have balanced out to zero.
*   **Complexity Analysis:**
    *   **Method 1 (Sorting):**
        *   **Time Complexity:** O(N log N) due to sorting, where N is the length of the strings.
        *   **Space Complexity:** O(N) to convert strings to char arrays.
    *   **Method 2 (Counting):**
        *   **Time Complexity:** O(N), where N is the length of the strings, as we iterate through each string once. (Constant array size for counts `O(1)` or `O(C)` where C is character set size).
        *   **Space Complexity:** O(1) (or O(C) for the character count array, which is constant for a fixed character set like ASCII). This is generally preferred for its better time complexity.

#### Topic 2: Linked Lists

**Practice Question 1:** Implement a function to reverse a singly `LinkedList`.

*   **Problem Statement:** Given the head of a singly linked list, reverse the list, and return the new head.
*   **Solution Code:**
    ```java
    class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
            next = null;
        }

        // Helper to print list for demonstration
        public static void printList(ListNode head) {
            ListNode current = head;
            while (current != null) {
                System.out.print(current.val + " -> ");
                current = current.next;
            }
            System.out.println("null");
        }
    }

    public class ReverseLinkedList {

        public static ListNode reverseList(ListNode head) {
            ListNode prev = null;   // This will eventually be the new head
            ListNode current = head; // Start at the current head
            ListNode nextTemp = null; // Temporary to store next node before breaking link

            while (current != null) {
                nextTemp = current.next; // Save the next node
                current.next = prev;     // Reverse the current node's pointer
                prev = current;          // Move prev to current node
                current = nextTemp;      // Move current to the next node (that was saved)
            }
            return prev; // prev will be the new head of the reversed list
        }

        public static void main(String[] args) {
            // Create original list: 1 -> 2 -> 3 -> 4 -> 5 -> null
            ListNode head = new ListNode(1);
            head.next = new ListNode(2);
            head.next.next = new ListNode(3);
            head.next.next.next = new ListNode(4);
            head.next.next.next.next = new ListNode(5);

            System.out.print("Original List: ");
            ListNode.printList(head); // Output: 1 -> 2 -> 3 -> 4 -> 5 -> null

            ListNode reversedHead = reverseList(head);

            System.out.print("Reversed List: ");
            ListNode.printList(reversedHead); // Output: 5 -> 4 -> 3 -> 2 -> 1 -> null
        }
    }
    ```
*   **Explanation:**
    1.  We use three pointers: `prev`, `current`, and `nextTemp`.
    2.  Initialize `prev` to `null` (this will become the new tail and eventually the new head).
    3.  Initialize `current` to `head` (the node we are currently processing).
    4.  Loop while `current` is not `null`:
        *   Store `current.next` in `nextTemp` to avoid losing the rest of the list.
        *   Reverse the current node's pointer: `current.next = prev`. This makes the current node point to the previous node.
        *   Move `prev` to `current`: `prev = current`.
        *   Move `current` to `nextTemp`: `current = nextTemp`.
    5.  After the loop, `prev` will be pointing to the original last node, which is now the head of the reversed list.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N), where N is the number of nodes in the linked list, as we traverse each node once.
    *   **Space Complexity:** O(1), as we only use a few extra pointers.

---

### Week 4: Data Structures (Stacks, Queues, Trees)

#### Topic 1: Stacks and Queues

**Practice Question 1:** Check if a string of parentheses is balanced using a `Stack`.

*   **Problem Statement:** Given a string containing just the characters `(`, `)`, `{`, `}`, `[`, `]`, determine if the input string is valid. An input string is valid if:
    1.  Open brackets must be closed by the same type of brackets.
    2.  Open brackets must be closed in the correct order.
*   **Solution Code:**
    ```java
    import java.util.Stack;

    public class BracketBalancer {
        public static boolean isValid(String s) {
            Stack<Character> stack = new Stack<>();

            for (char c : s.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c); // Push opening brackets onto the stack
                } else if (c == ')' || c == '}' || c == ']') {
                    if (stack.isEmpty()) {
                        return false; // Closing bracket without a matching opening bracket
                    }
                    char top = stack.pop(); // Pop the most recent opening bracket
                    if ((c == ')' && top != '(') ||
                        (c == '}' && top != '{') ||
                        (c == ']' && top != '[')) {
                        return false; // Mismatched opening and closing brackets
                    }
                }
            }
            return stack.isEmpty(); // True if stack is empty (all brackets matched)
        }

        public static void main(String[] args) {
            System.out.println("() : " + isValid("()"));       // true
            System.out.println("()[]{} : " + isValid("()[]{}")); // true
            System.out.println("(] : " + isValid("(]"));       // false
            System.out.println("([)] : " + isValid("([)]"));     // false
            System.out.println("{[]} : " + isValid("{[]}"));     // true
            System.out.println("[ : " + isValid("["));         // false (unclosed bracket)
            System.out.println("] : " + isValid("]"));         // false (unopened bracket)
            System.out.println(" : " + isValid(""));          // true
        }
    }
    ```
*   **Explanation:**
    1.  Initialize an empty `Stack` to store opening brackets.
    2.  Iterate through each character of the input string:
        *   If it's an opening bracket (`(`, `{`, `[`), push it onto the stack.
        *   If it's a closing bracket (`)`, `}`, `]`):
            *   Check if the stack is empty. If it is, there's no matching opening bracket, so the string is invalid.
            *   Pop the top element from the stack (which should be the most recent opening bracket).
            *   Compare the popped opening bracket with the current closing bracket. If they don't form a valid pair (e.g., `)` with `{`), the string is invalid.
    3.  After iterating through the entire string, if the stack is empty, it means all opening brackets found their matching closing brackets in the correct order, so the string is valid. Otherwise, there are unmatched opening brackets, and the string is invalid.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N), where N is the length of the string, as we iterate through each character once.
    *   **Space Complexity:** O(N) in the worst case (e.g., a string like `(((((`), as all opening brackets might be pushed onto the stack.

#### Topic 2: Trees (Binary Trees, BST)

**Practice Question 3:** Check if a given binary tree is a BST.

*   **Problem Statement:** Given the root of a binary tree, determine if it is a valid Binary Search Tree (BST). A valid BST is defined as follows:
    *   The left subtree of a node contains only nodes with keys less than the node's key.
    *   The right subtree of a node contains only nodes with keys greater than the node's key.
    *   Both the left and right subtrees must also be BSTs.
*   **Solution Code:**
    ```java
    class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    public class ValidateBST {

        public boolean isValidBST(TreeNode root) {
            return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
        }

        private boolean isValidBST(TreeNode node, long minVal, long maxVal) {
            if (node == null) {
                return true; // An empty tree is a valid BST
            }

            // Check if the current node's value is within the valid range
            if (node.val <= minVal || node.val >= maxVal) {
                return false;
            }

            // Recursively check the left subtree:
            // maxVal for left subtree becomes current node's value
            if (!isValidBST(node.left, minVal, node.val)) {
                return false;
            }

            // Recursively check the right subtree:
            // minVal for right subtree becomes current node's value
            if (!isValidBST(node.right, node.val, maxVal)) {
                return false;
            }

            return true; // If all checks pass, it's a valid BST
        }

        public static void main(String[] args) {
            // Valid BST:
            //      2
            //     / \
            //    1   3
            TreeNode root1 = new TreeNode(2);
            root1.left = new TreeNode(1);
            root1.right = new TreeNode(3);
            System.out.println("Is root1 a valid BST? " + new ValidateBST().isValidBST(root1)); // true

            // Invalid BST: (root 5, left 1, right 4 -- but 4 is < 5)
            //      5
            //     / \
            //    1   4
            //       / \
            //      3   6
            TreeNode root2 = new TreeNode(5);
            root2.left = new TreeNode(1);
            root2.right = new TreeNode(4); // Fails here: 4 < 5 but is in right subtree
            root2.right.left = new TreeNode(3);
            root2.right.right = new TreeNode(6);
            System.out.println("Is root2 a valid BST? " + new ValidateBST().isValidBST(root2)); // false

            // Invalid BST: (right child of 1 is 2, but 2 > 1. However, 2 < 3. This should still fail!)
            //      10
            //     /  \
            //    5   15
            //       /  \
            //      6   20
            TreeNode root3 = new TreeNode(10);
            root3.left = new TreeNode(5);
            root3.right = new TreeNode(15);
            root3.right.left = new TreeNode(6); // Fails here: 6 is in right subtree of 10, but left subtree of 15. It must be > 10.
            root3.right.right = new TreeNode(20);
            System.out.println("Is root3 a valid BST? " + new ValidateBST().isValidBST(root3)); // false
        }
    }
    ```
*   **Explanation:**
    1.  The key idea is to use a recursive helper function that passes down a `minVal` and `maxVal` for each node.
    2.  For any node, its value must be greater than `minVal` and less than `maxVal`.
    3.  When traversing to the left child, the `maxVal` for the left child's subtree becomes the current node's value (as all nodes in the left subtree must be less than the current node). The `minVal` remains the same.
    4.  When traversing to the right child, the `minVal` for the right child's subtree becomes the current node's value (as all nodes in the right subtree must be greater than the current node). The `maxVal` remains the same.
    5.  Base case: If a node is `null`, it's considered a valid BST (an empty tree).
    6.  The initial call uses `Long.MIN_VALUE` and `Long.MAX_VALUE` to cover the entire possible range for integer values.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N), where N is the number of nodes in the tree, as we visit each node once.
    *   **Space Complexity:** O(H), where H is the height of the tree, due to the recursion stack. In the worst case (skewed tree), H can be N, so O(N). In the best case (balanced tree), H is log N, so O(log N).

---

## Month 2: Advanced Java, Algorithms, and System Design Basics - Solutions

### Week 5: Advanced Java Concepts

#### Topic 1: Exception Handling

**Practice Question 2:** Create a custom exception for invalid age and use it in a `Person` class.

*   **Problem Statement:** Define a `Person` class that takes `name` and `age`. If the age is negative or unrealistically high (e.g., > 120), throw a custom `InvalidAgeException`.
*   **Solution Code:**
    ```java
    // 1. Define the Custom Exception
    class InvalidAgeException extends Exception {
        public InvalidAgeException(String message) {
            super(message);
        }
    }

    // 2. Define the Person Class
    class Person {
        private String name;
        private int age;

        public Person(String name, int age) throws InvalidAgeException {
            if (age < 0 || age > 120) { // Age validation logic
                throw new InvalidAgeException("Age " + age + " is invalid. Age must be between 0 and 120.");
            }
            this.name = name;
            this.age = age;
            System.out.println("Person created: " + this);
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + '}';
        }
    }

    // 3. Demonstrate Usage
    public class CustomExceptionDemo {
        public static void main(String[] args) {
            try {
                Person p1 = new Person("Alice", 30);
                Person p2 = new Person("Bob", -5); // This will throw an exception
            } catch (InvalidAgeException e) {
                System.err.println("Error creating person: " + e.getMessage());
            }

            try {
                Person p3 = new Person("Charlie", 150); // Another invalid age
            } catch (InvalidAgeException e) {
                System.err.println("Error creating person: " + e.getMessage());
            }

            try {
                Person p4 = new Person("David", 70);
            } catch (InvalidAgeException e) {
                System.err.println("Error creating person: " + e.getMessage());
            }
        }
    }
    ```
*   **Explanation:**
    1.  `InvalidAgeException` extends `java.lang.Exception`, making it a **checked exception**. This means any method that *might* throw it must either `catch` it or declare that it `throws` it.
    2.  The `Person` constructor performs the age validation. If the age is invalid, it `throw`s a new `InvalidAgeException`. Since this is a checked exception, the constructor must declare `throws InvalidAgeException`.
    3.  In the `main` method, calls to the `Person` constructor are enclosed in `try-catch` blocks to handle the `InvalidAgeException`.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(1) for constructor and validation.
    *   **Space Complexity:** O(1) for object creation.

#### Topic 2: Multithreading and Concurrency

**Practice Question 2:** Implement a producer-consumer problem using `wait()` and `notify()`.

*   **Problem Statement:** Create a classic producer-consumer scenario where producers add items to a shared buffer and consumers remove them. Use `wait()` and `notify()` to manage synchronization and prevent buffer overflow/underflow.
*   **Solution Code:**
    ```java
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.Random;

    class SharedBuffer {
        private Queue<Integer> buffer;
        private int capacity;

        public SharedBuffer(int capacity) {
            this.buffer = new LinkedList<>();
            this.capacity = capacity;
        }

        // Producer method
        public void produce(int item) throws InterruptedException {
            synchronized (this) { // Acquire lock on SharedBuffer instance
                while (buffer.size() == capacity) {
                    System.out.println("Buffer is full. Producer " + Thread.currentThread().getName() + " waiting...");
                    wait(); // Producer waits if buffer is full
                }
                buffer.offer(item); // Add item to buffer
                System.out.println("Producer " + Thread.currentThread().getName() + " produced: " + item + ". Buffer size: " + buffer.size());
                notifyAll(); // Notify waiting consumers
            }
        }

        // Consumer method
        public int consume() throws InterruptedException {
            synchronized (this) { // Acquire lock on SharedBuffer instance
                while (buffer.isEmpty()) {
                    System.out.println("Buffer is empty. Consumer " + Thread.currentThread().getName() + " waiting...");
                    wait(); // Consumer waits if buffer is empty
                }
                int item = buffer.poll(); // Remove item from buffer
                System.out.println("Consumer " + Thread.currentThread().getName() + " consumed: " + item + ". Buffer size: " + buffer.size());
                notifyAll(); // Notify waiting producers
                return item;
            }
        }
    }

    class Producer implements Runnable {
        private SharedBuffer buffer;
        private Random random = new Random();

        public Producer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    int item = random.nextInt(100);
                    buffer.produce(item);
                    Thread.sleep(random.nextInt(100)); // Simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Producer interrupted.");
            }
        }
    }

    class Consumer implements Runnable {
        private SharedBuffer buffer;
        private Random random = new Random();

        public Consumer(SharedBuffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 5; i++) {
                    int item = buffer.consume();
                    Thread.sleep(random.nextInt(150)); // Simulate work
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Consumer interrupted.");
            }
        }
    }

    public class ProducerConsumerDemo {
        public static void main(String[] args) {
            SharedBuffer buffer = new SharedBuffer(3); // Buffer with capacity 3

            Thread producer1 = new Thread(new Producer(buffer), "P1");
            Thread producer2 = new Thread(new Producer(buffer), "P2");
            Thread consumer1 = new Thread(new Consumer(buffer), "C1");
            Thread consumer2 = new Thread(new Consumer(buffer), "C2");

            producer1.start();
            producer2.start();
            consumer1.start();
            consumer2.start();

            // Optionally join threads to wait for their completion
            try {
                producer1.join();
                producer2.join();
                consumer1.join();
                consumer2.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Producer-Consumer simulation finished.");
        }
    }
    ```
*   **Explanation:**
    1.  `SharedBuffer` acts as the critical section, holding the `Queue`.
    2.  `produce()` and `consume()` methods are `synchronized` on the `SharedBuffer` instance to ensure only one thread can modify the buffer at a time.
    3.  **`produce()`:**
        *   Uses a `while (buffer.size() == capacity)` loop to check if the buffer is full. `while` loop is used instead of `if` to handle spurious wakeups (a thread might wake up but the condition isn't actually met yet).
        *   If full, `wait()` is called, releasing the lock and pausing the producer thread until a `notify()` or `notifyAll()` is called.
        *   After producing, `notifyAll()` wakes up all waiting threads (consumers, in this case).
    4.  **`consume()`:**
        *   Uses a `while (buffer.isEmpty())` loop to check if the buffer is empty.
        *   If empty, `wait()` is called, releasing the lock and pausing the consumer thread until a `notify()` or `notifyAll()` is called.
        *   After consuming, `notifyAll()` wakes up all waiting threads (producers, in this case).
    5.  `Producer` and `Consumer` classes implement `Runnable` and continuously try to produce/consume items.
*   **Complexity Analysis:**
    *   **Time Complexity:** Operations `offer()` and `poll()` on `LinkedList` (used as a queue) are O(1). The overhead is primarily due to thread synchronization and scheduling, which are dependent on the JVM and OS.
    *   **Space Complexity:** O(capacity) for the shared buffer.

---

### Week 6: I/O, Serialization, and Reflection

#### Topic 1: Input/Output (I/O) Streams

**Practice Question 1:** Copy the content of one text file to another.

*   **Problem Statement:** Given two file paths (source and destination), read the content from the source file and write it to the destination file.
*   **Solution Code:**
    ```java
    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.FileReader;
    import java.io.FileWriter;
    import java.io.IOException;

    public class FileCopier {

        public static void copyTextFile(String sourceFilePath, String destFilePath) throws IOException {
            try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(destFilePath))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.newLine(); // Preserve line breaks
                }
                System.out.println("File copied successfully from " + sourceFilePath + " to " + destFilePath);
            } catch (IOException e) {
                System.err.println("Error during file copy: " + e.getMessage());
                throw e; // Re-throw to inform caller about the failure
            }
        }

        public static void main(String[] args) {
            String source = "source.txt";
            String destination = "destination.txt";

            // 1. Create a dummy source file for testing
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(source))) {
                bw.write("This is line 1.");
                bw.newLine();
                bw.write("This is line 2.");
                bw.newLine();
                bw.write("The end.");
            } catch (IOException e) {
                System.err.println("Failed to create source file: " + e.getMessage());
                return;
            }

            // 2. Perform the copy operation
            try {
                copyTextFile(source, destination);

                // 3. Verify content of destination file (optional)
                System.out.println("\nContent of " + destination + ":");
                try (BufferedReader br = new BufferedReader(new FileReader(destination))) {
                    br.lines().forEach(System.out::println);
                }
            } catch (IOException e) {
                // Already handled in copyTextFile, but catching here for main's try-catch.
            }
        }
    }
    ```
*   **Explanation:**
    1.  The `copyTextFile` method takes two file paths as input.
    2.  It uses a `try-with-resources` statement, which automatically closes the `BufferedReader` and `BufferedWriter` even if exceptions occur. This is crucial for resource management.
    3.  `BufferedReader` is used for efficient reading line by line from the source file.
    4.  `BufferedWriter` is used for efficient writing line by line to the destination file.
    5.  `reader.readLine()` reads a line, and `writer.write(line)` writes it. `writer.newLine()` ensures that original line breaks are preserved.
    6.  `IOException` is caught to handle potential file errors, and then re-thrown to signal a failure to the caller.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(L), where L is the total number of characters in the source file, as each character is read and written once.
    *   **Space Complexity:** O(B), where B is the buffer size of `BufferedReader` and `BufferedWriter` (typically a few kilobytes). For the lines themselves, it's O(maxLengthOfLine) for the `line` variable.

#### Topic 2: Serialization and Reflection API

**Practice Question 1:** Serialize and deserialize a `HashMap` containing custom objects.

*   **Problem Statement:** Create a `Student` class (which is `Serializable`). Create a `HashMap<Integer, Student>`, populate it, serialize it to a file, and then deserialize it back into a new `HashMap`. Verify the contents.
*   **Solution Code:**
    ```java
    import java.io.*;
    import java.util.HashMap;
    import java.util.Map;

    class Student implements Serializable {
        private static final long serialVersionUID = 2L; // Recommended for version control
        private int id;
        private String name;
        private transient String secretGrade; // Will not be serialized

        public Student(int id, String name, String secretGrade) {
            this.id = id;
            this.name = name;
            this.secretGrade = secretGrade;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public String getSecretGrade() { return secretGrade; } // Will be null after deserialization

        @Override
        public String toString() {
            return "Student{id=" + id + ", name='" + name + "', secretGrade='" + secretGrade + "'}";
        }
    }

    public class MapSerializationDemo {
        private static final String FILE_NAME = "student_map.ser";

        public static void main(String[] args) {
            Map<Integer, Student> originalStudentMap = new HashMap<>();
            originalStudentMap.put(101, new Student(101, "Alice", "A+"));
            originalStudentMap.put(102, new Student(102, "Bob", "B"));
            originalStudentMap.put(103, new Student(103, "Charlie", "C+"));

            System.out.println("Original Map: " + originalStudentMap);

            // --- Serialization ---
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
                oos.writeObject(originalStudentMap);
                System.out.println("HashMap<Integer, Student> serialized to " + FILE_NAME);
            } catch (IOException e) {
                System.err.println("Serialization error: " + e.getMessage());
            }

            // --- Deserialization ---
            Map<Integer, Student> deserializedStudentMap = null;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
                deserializedStudentMap = (Map<Integer, Student>) ois.readObject();
                System.out.println("HashMap<Integer, Student> deserialized from " + FILE_NAME);
                System.out.println("Deserialized Map: " + deserializedStudentMap);
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Deserialization error: " + e.getMessage());
            }

            // --- Verification ---
            if (deserializedStudentMap != null) {
                System.out.println("\nVerifying deserialized data:");
                System.out.println("Deserialized map size: " + deserializedStudentMap.size());
                System.out.println("Alice's deserialized data: " + deserializedStudentMap.get(101));
                // Notice the transient field will be null for deserialized objects
                System.out.println("Alice's secret grade (original): " + originalStudentMap.get(101).getSecretGrade());
                System.out.println("Alice's secret grade (deserialized): " + deserializedStudentMap.get(101).getSecretGrade()); // Expected: null
            }
        }
    }
    ```
*   **Explanation:**
    1.  The `Student` class implements `Serializable`. This is mandatory for its instances to be serialized. `serialVersionUID` is declared for version control.
    2.  The `secretGrade` field is marked `transient`. This means it will *not* be serialized along with the other fields. Upon deserialization, its value will be the default for its type (null for objects, 0 for int, false for boolean, etc.).
    3.  An `ObjectOutputStream` is used to write the `HashMap` object to a file. The `writeObject()` method recursively handles the serialization of the map itself and all its contained `Student` objects.
    4.  An `ObjectInputStream` is used to read the serialized `HashMap` object from the file. The `readObject()` method returns an `Object`, which needs to be cast back to `Map<Integer, Student>`.
    5.  The verification step demonstrates that `secretGrade` is `null` in the deserialized objects, as expected for a `transient` field.
*   **Complexity Analysis:**
    *   **Time Complexity:** Serialization/deserialization time is generally proportional to the size of the object graph being serialized (number of objects and their fields). For N students in a map, it's roughly O(N * (object_size)).
    *   **Space Complexity:** O(Map_size + Object_graph_size) for the serialized data in the file.

---

### Week 7: Generics and Functional Programming

#### Topic 1: Generics

**Practice Question 1:** Create a generic `Stack` class that can hold any type of object.

*   **Problem Statement:** Implement a basic Stack data structure using generics, allowing it to store elements of any specified type. It should support `push`, `pop`, `peek`, and `isEmpty` operations.
*   **Solution Code:**
    ```java
    import java.util.ArrayList;
    import java.util.EmptyStackException;
    import java.util.List;

    public class GenericStack<T> { // <T> signifies a generic type parameter
        private List<T> elements;

        public GenericStack() {
            elements = new ArrayList<>(); // Use ArrayList internally for dynamic resizing
        }

        // Push: Adds an element to the top of the stack
        public void push(T item) {
            elements.add(item);
            System.out.println("Pushed: " + item + ", Stack: " + elements);
        }

        // Pop: Removes and returns the element from the top of the stack
        public T pop() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            T item = elements.remove(elements.size() - 1); // Remove from end
            System.out.println("Popped: " + item + ", Stack: " + elements);
            return item;
        }

        // Peek: Returns the element at the top of the stack without removing it
        public T peek() {
            if (isEmpty()) {
                throw new EmptyStackException();
            }
            return elements.get(elements.size() - 1); // Get from end
        }

        // isEmpty: Checks if the stack is empty
        public boolean isEmpty() {
            return elements.isEmpty();
        }

        // Size: Returns the number of elements in the stack
        public int size() {
            return elements.size();
        }

        public static void main(String[] args) {
            System.out.println("--- Integer Stack ---");
            GenericStack<Integer> intStack = new GenericStack<>();
            intStack.push(10);
            intStack.push(20);
            System.out.println("Peek: " + intStack.peek()); // 20
            intStack.pop(); // 20
            System.out.println("Is empty? " + intStack.isEmpty()); // false
            intStack.pop(); // 10
            System.out.println("Is empty? " + intStack.isEmpty()); // true

            System.out.println("\n--- String Stack ---");
            GenericStack<String> stringStack = new GenericStack<>();
            stringStack.push("Java");
            stringStack.push("Generics");
            System.out.println("Peek: " + stringStack.peek()); // Generics
            stringStack.pop(); // Generics
            stringStack.push("Stack");
            System.out.println("Stack size: " + stringStack.size()); // 2
            stringStack.pop(); // Stack
            stringStack.pop(); // Java

            try {
                stringStack.pop(); // Throws EmptyStackException
            } catch (EmptyStackException e) {
                System.out.println("Caught expected exception: " + e.getMessage());
            }
        }
    }
    ```
*   **Explanation:**
    1.  The class `GenericStack<T>` uses `T` as a type parameter, making it generic.
    2.  An `ArrayList<T>` is used internally to store the elements. `ArrayList` is suitable because `add(T item)` and `remove(int index)` at the end of the list are efficient (amortized O(1)).
    3.  `push(T item)` adds an element to the end of the `ArrayList`.
    4.  `pop()` removes the last element (top of the stack) and returns it. It checks for an empty stack to prevent `IndexOutOfBoundsException`.
    5.  `peek()` returns the last element without removing it.
    6.  `isEmpty()` simply delegates to the `ArrayList`'s `isEmpty()` method.
    7.  The `main` method demonstrates creating stacks of `Integer` and `String` types, showcasing the reusability of the generic implementation.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(1) for `push`, `pop`, `peek`, `isEmpty`, `size` (amortized for `ArrayList.add()` and `remove()` at the end).
    *   **Space Complexity:** O(N) where N is the number of elements in the stack, as `ArrayList` stores all elements.

#### Topic 2: Functional Programming with Lambda Expressions and Stream API

**Practice Question 1:** Given a list of strings, filter out those that start with 'A' and convert them to uppercase using Streams.

*   **Problem Statement:** Process a list of names. Keep only those names that begin with the letter 'A' (case-insensitive) and transform them into their uppercase versions.
*   **Solution Code:**
    ```java
    import java.util.Arrays;
    import java.util.List;
    import java.util.stream.Collectors;

    public class StreamProcessingDemo {
        public static void main(String[] args) {
            List<String> names = Arrays.asList("Alice", "Bob", "anna", "Charlie", "apple", "Andrew");

            System.out.println("Original Names: " + names);

            List<String> filteredAndUppercasedNames = names.stream()
                                                            // Filter: Keep names starting with 'A' or 'a'
                                                            .filter(name -> name.startsWith("A") || name.startsWith("a"))
                                                            // Map: Convert filtered names to uppercase
                                                            .map(String::toUpperCase) // Method reference to String.toUpperCase()
                                                            // Collect: Gather the results into a new List
                                                            .collect(Collectors.toList());

            System.out.println("Filtered and Uppercased Names: " + filteredAndUppercasedNames);
            // Expected Output: [ALICE, ANNA, APPLE, ANDREW]
        }
    }
    ```
*   **Explanation:**
    1.  `names.stream()`: Creates a stream from the `names` `List`.
    2.  `.filter(name -> name.startsWith("A") || name.startsWith("a"))`: This is an **intermediate operation** that filters elements based on a `Predicate` (the lambda expression). It keeps only names that start with 'A' or 'a' (case-insensitive check).
    3.  `.map(String::toUpperCase)`: This is another **intermediate operation** that transforms each element. The `String::toUpperCase` is a **method reference** equivalent to `name -> name.toUpperCase()`. It converts each filtered name to its uppercase equivalent.
    4.  `.collect(Collectors.toList())`: This is a **terminal operation** that gathers the elements from the stream into a new `List`.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(N), where N is the number of strings in the input list. Each string is processed (filtered and mapped) once.
    *   **Space Complexity:** O(M), where M is the number of filtered strings (for the new list). In the worst case, M could be N.

---

## Month 3: Microservices, Caching, and Security Basics - Solutions

### Week 11: Design Patterns and SOLID Principles

#### Topic 1: Design Patterns (Gang of Four) - Creational & Structural

**Practice Question 1:** Implement the Builder pattern for a complex object (e.g., a `Computer` with many options).

*   **Problem Statement:** Design a `Computer` class that can have various optional components (CPU, RAM, Storage, Graphics Card, OS). Use the Builder pattern to create `Computer` instances with flexible configurations without needing multiple constructors.
*   **Solution Code:**
    ```java
    // Product Class: Computer
    class Computer {
        private String cpu;
        private int ramGB;
        private String storage;
        private String graphicsCard;
        private String operatingSystem;
        private boolean hasWiFi;
        private boolean hasBluetooth;

        // Private constructor, only accessible by the Builder
        private Computer(Builder builder) {
            this.cpu = builder.cpu;
            this.ramGB = builder.ramGB;
            this.storage = builder.storage;
            this.graphicsCard = builder.graphicsCard;
            this.operatingSystem = builder.operatingSystem;
            this.hasWiFi = builder.hasWiFi;
            this.hasBluetooth = builder.hasBluetooth;
        }

        // Getters for all fields
        public String getCpu() { return cpu; }
        public int getRamGB() { return ramGB; }
        public String getStorage() { return storage; }
        public String getGraphicsCard() { return graphicsCard; }
        public String getOperatingSystem() { return operatingSystem; }
        public boolean hasWiFi() { return hasWiFi; }
        public boolean hasBluetooth() { return hasBluetooth; }

        @Override
        public String toString() {
            return "Computer [\n" +
                   "  CPU: " + cpu + ",\n" +
                   "  RAM: " + ramGB + "GB,\n" +
                   "  Storage: " + storage + ",\n" +
                   "  Graphics Card: " + graphicsCard + ",\n" +
                   "  OS: " + operatingSystem + ",\n" +
                   "  WiFi: " + hasWiFi + ",\n" +
                   "  Bluetooth: " + hasBluetooth + "\n]";
        }

        // Builder Class
        public static class Builder {
            // Required parameters
            private String cpu;
            private int ramGB;

            // Optional parameters - initialized to default values
            private String storage = "256GB SSD";
            private String graphicsCard = "Integrated";
            private String operatingSystem = "Windows 10";
            private boolean hasWiFi = true;
            private boolean hasBluetooth = true;

            public Builder(String cpu, int ramGB) {
                this.cpu = cpu;
                this.ramGB = ramGB;
            }

            // Setters for optional parameters, returning the Builder for chaining
            public Builder withStorage(String storage) {
                this.storage = storage;
                return this;
            }

            public Builder withGraphicsCard(String graphicsCard) {
                this.graphicsCard = graphicsCard;
                return this;
            }

            public Builder withOperatingSystem(String operatingSystem) {
                this.operatingSystem = operatingSystem;
                return this;
            }

            public Builder withWiFi(boolean hasWiFi) {
                this.hasWiFi = hasWiFi;
                return this;
            }

            public Builder withBluetooth(boolean hasBluetooth) {
                this.hasBluetooth = hasBluetooth;
                return this;
            }

            // Build method to construct the Computer object
            public Computer build() {
                return new Computer(this);
            }
        }
    }

    public class BuilderPatternDemo {
        public static void main(String[] args) {
            // Build a standard gaming computer
            Computer gamingPC = new Computer.Builder("Intel i9", 32)
                                            .withGraphicsCard("NVIDIA RTX 4080")
                                            .withStorage("1TB NVMe SSD")
                                            .withOperatingSystem("Windows 11")
                                            .build();
            System.out.println("Gaming PC:\n" + gamingPC);

            System.out.println("\n---------------------\n");

            // Build a lightweight work laptop
            Computer workLaptop = new Computer.Builder("AMD Ryzen 7", 16)
                                              .withStorage("512GB SSD")
                                              .withOperatingSystem("Linux Mint")
                                              .withBluetooth(false) // No Bluetooth needed
                                              .build();
            System.out.println("Work Laptop:\n" + workLaptop);

            System.out.println("\n---------------------\n");

            // Build a basic desktop with minimal options (uses defaults for most)
            Computer basicDesktop = new Computer.Builder("Intel i5", 8)
                                                .build();
            System.out.println("Basic Desktop:\n" + basicDesktop);
        }
    }
    ```
*   **Explanation:**
    1.  **`Computer` (Product):** This is the complex object we want to build. Its constructor is `private`, ensuring that `Computer` objects can only be created via its `Builder`. It has many fields, some required, some optional.
    2.  **`Computer.Builder` (Builder):** This is a static nested class within `Computer`.
        *   It has fields mirroring those of `Computer`. Required fields are passed to its constructor. Optional fields are initialized with default values or set via fluent setter methods.
        *   Each setter method (e.g., `withStorage()`) returns `this` (the `Builder` instance), allowing for method chaining.
        *   The `build()` method creates a `Computer` instance, passing `this` (the `Builder`) to `Computer`'s private constructor.
    3.  **Client Usage:** The `main` method demonstrates how to use the builder to construct `Computer` objects with varying configurations in a readable and flexible manner. You don't need dozens of constructors in `Computer` itself.
*   **Complexity Analysis:**
    *   **Time Complexity:** O(1) for constructing each `Computer` object, as it involves a fixed number of assignments.
    *   **Space Complexity:** O(1) for the `Builder` instance and the final `Computer` object.

#### Topic 2: SOLID Principles

**Practice Question 3:** Implement a notification service that uses the Observer pattern to notify users via email or SMS.

*   **Problem Statement:** Design a system where a `NotificationService` can send updates. Users can subscribe to these updates and choose their preferred notification channel (Email or SMS). Demonstrate the Observer pattern and how it aligns with OCP (Open/Closed Principle) and SRP (Single Responsibility Principle).
*   **Solution Code:**
    ```java
    import java.util.ArrayList;
    import java.util.List;

    // Subject (Observable)
    interface NotificationSubject {
        void addObserver(NotificationObserver observer);
        void removeObserver(NotificationObserver observer);
        void notifyObservers(String message);
    }

    // Observer
    interface NotificationObserver {
        void update(String message);
    }

    // Concrete Subject (adheres to SRP - only manages observers and notifications)
    class ConcreteNotificationService implements NotificationSubject {
        private List<NotificationObserver> observers = new ArrayList<>();

        @Override
        public void addObserver(NotificationObserver observer) {
            observers.add(observer);
            System.out.println("Observer added: " + observer.getClass().getSimpleName());
        }

        @Override
        public void removeObserver(NotificationObserver observer) {
            observers.remove(observer);
            System.out.println("Observer removed: " + observer.getClass().getSimpleName());
        }

        @Override
        public void notifyObservers(String message) {
            System.out.println("\n--- Notifying Observers: '" + message + "' ---");
            for (NotificationObserver observer : observers) {
                observer.update(message); // Polymorphic call
            }
        }
    }

    // Concrete Observer 1 (adheres to SRP - only responsible for email sending)
    class EmailNotifier implements NotificationObserver {
        private String emailAddress;

        public EmailNotifier(String emailAddress) {
            this.emailAddress = emailAddress;
        }

        @Override
        public void update(String message) {
            System.out.println("Sending Email to " + emailAddress + ": " + message);
        }
    }

    // Concrete Observer 2 (adheres to SRP - only responsible for SMS sending)
    class SmsNotifier implements NotificationObserver {
        private String phoneNumber;

        public SmsNotifier(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @Override
        public void update(String message) {
            System.out.println("Sending SMS to " + phoneNumber + ": " + message);
        }
    }

    public class ObserverPatternAndSolidDemo {
        public static void main(String[] args) {
            ConcreteNotificationService service = new ConcreteNotificationService();

            EmailNotifier aliceEmail = new EmailNotifier("alice@example.com");
            SmsNotifier bobSms = new SmsNotifier("123-456-7890");
            EmailNotifier charlieEmail = new EmailNotifier("charlie@example.com");

            service.addObserver(aliceEmail);
            service.addObserver(bobSms);
            service.addObserver(charlieEmail);

            service.notifyObservers("New product launch event!");

            // Demonstrate OCP: Easily add a new notification type (e.g., Push Notification)
            // without modifying ConcreteNotificationService
            System.out.println("\n--- Adding Push Notification ---");
            class PushNotifier implements NotificationObserver {
                private String deviceToken;
                public PushNotifier(String deviceToken) { this.deviceToken = deviceToken; }
                @Override
                public void update(String message) {
                    System.out.println("Sending Push Notification to device " + deviceToken + ": " + message);
                }
            }
            PushNotifier davidPush = new PushNotifier("DEVICE_XYZ_789");
            service.addObserver(davidPush);
            service.notifyObservers("Don't miss our flash sale!");

            service.removeObserver(bobSms);
            service.notifyObservers("Maintenance scheduled for next week.");
        }
    }
    ```
*   **Explanation:**
    1.  **Observer Pattern:**
        *   `NotificationSubject` (interface) and `NotificationObserver` (interface) define the core roles.
        *   `ConcreteNotificationService` maintains a list of `NotificationObserver`s and notifies them.
        *   `EmailNotifier` and `SmsNotifier` are concrete observers that react to updates.
    2.  **SOLID Principles:**
        *   **Single Responsibility Principle (SRP):**
            *   `ConcreteNotificationService` is solely responsible for managing subscribers and dispatching notifications. It doesn't know *how* to send an email or SMS.
            *   `EmailNotifier` is solely responsible for sending emails.
            *   `SmsNotifier` is solely responsible for sending SMS.
        *   **Open/Closed Principle (OCP):**
            *   The `ConcreteNotificationService` is **closed for modification** when adding new notification types. You don't need to change its code.
            *   It is **open for extension**: you can easily create new `NotificationObserver` implementations (e.g., `PushNotifier` in the demo) and `addObserver()` to the service without altering `ConcreteNotificationService` itself. This is a direct benefit of the Observer pattern and adhering to OCP.
        *   **Dependency Inversion Principle (DIP):** `ConcreteNotificationService` depends on the `NotificationObserver` abstraction (interface), not on concrete implementations like `EmailNotifier` or `SmsNotifier`. This reduces coupling.
        *   **Liskov Substitution Principle (LSP):** Any `NotificationObserver` implementation can be substituted where a `NotificationObserver` is expected (e.g., in the `observers` list), and the system will behave correctly (`update` method will be called).
        *   **Interface Segregation Principle (ISP):** The interfaces (`NotificationSubject`, `NotificationObserver`) are small and specific. Observers only need an `update` method, subjects only need observer management and notification methods.
*   **Complexity Analysis:**
    *   **Time Complexity:**
        *   `addObserver`, `removeObserver`: O(1) on average for `ArrayList`.
        *   `notifyObservers`: O(N) where N is the number of subscribed observers, as it iterates through the list. Each `update` call is O(1) (simulated).
    *   **Space Complexity:** O(N) for storing the list of observers.

---

### Week 12: Spring Boot Fundamentals

#### Topic 1: Introduction to Spring Boot and Core Concepts

**Practice Question 1:** Create a simple Spring Boot application with a REST endpoint that returns a JSON object.

*   **Problem Statement:** Develop a Spring Boot application that exposes a `/product/{id}` GET endpoint. This endpoint should return a `Product` object (JSON) for a given ID.
*   **Solution Code:**
    ```java
    // Product.java (Model class)
    public class Product {
        private Long id;
        private String name;
        private double price;

        public Product(Long id, String name, double price) {
            this.id = id;
            this.name = name;
            this.price = price;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        @Override
        public String toString() {
            return "Product{id=" + id + ", name='" + name + "', price=" + price + '}';
        }
    }

    // ProductController.java (REST Controller)
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.GetMapping;
    import org.springframework.web.bind.annotation.PathVariable;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.Optional;

    @RestController // Combines @Controller and @ResponseBody
    @RequestMapping("/api/products") // Base path for all endpoints in this controller
    public class ProductController {

        // Simulate a database/service layer with a Map
        private Map<Long, Product> productData = new HashMap<>();

        public ProductController() {
            productData.put(1L, new Product(1L, "Laptop", 1200.00));
            productData.put(2L, new Product(2L, "Mouse", 25.50));
            productData.put(3L, new Product(3L, "Keyboard", 75.00));
        }

        @GetMapping("/{id}") // Handles GET requests to /api/products/{id}
        public ResponseEntity<Product> getProductById(@PathVariable Long id) {
            Optional<Product> product = Optional.ofNullable(productData.get(id));
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get()); // Returns 200 OK with product JSON
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Returns 404 Not Found
            }
        }

        @GetMapping // Handles GET requests to /api/products (returns all products)
        public ResponseEntity<List<Product>> getAllProducts() {
            return ResponseEntity.ok(new ArrayList<>(productData.values()));
        }
    }

    // MyApplication.java (Main Spring Boot Application class)
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication // Marks this as a Spring Boot application
    public class MyApplication {
        public static void main(String[] args) {
            SpringApplication.run(MyApplication.class, args);
            System.out.println("Spring Boot application started!");
            System.out.println("Access products at: http://localhost:8080/api/products/{id}");
        }
    }
    ```
*   **Explanation:**
    1.  **`Product.java`:** A simple POJO representing the data structure for a product. It will be automatically converted to/from JSON by Spring.
    2.  **`ProductController.java`:**
        *   `@RestController`: Combines `@Controller` and `@ResponseBody`. `@Controller` marks the class as a Spring MVC controller, and `@ResponseBody` indicates that the return value of its methods should be directly bound to the web response body (e.g., as JSON).
        *   `@RequestMapping("/api/products")`: Sets the base URI path for all handler methods in this controller.
        *   The constructor initializes a `HashMap` to simulate product data.
        *   `@GetMapping("/{id}")`: Maps HTTP GET requests for paths like `/api/products/1` to this method. `@PathVariable Long id` extracts the ID from the URL.
        *   `ResponseEntity`: Provides more control over the HTTP response (status code, headers, body).
        *   `Optional.ofNullable(productData.get(id))`: Safely retrieves a product, handling the case where an ID might not exist.
        *   `ResponseEntity.ok(product.get())`: Returns an HTTP 200 OK status with the product object as the response body.
        *   `ResponseEntity.status(HttpStatus.NOT_FOUND).build()`: Returns an HTTP 404 Not Found status if the product is not found.
    3.  **`MyApplication.java`:**
        *   `@SpringBootApplication`: This is a convenience annotation that adds:
            *   `@Configuration`: Tags the class as a source of bean definitions for the application context.
            *   `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings (e.g., if Spring MVC is on the classpath, it configures a DispatcherServlet).
            *   `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the `com.example` package, allowing it to find `ProductController`.
        *   `SpringApplication.run(MyApplication.class, args)`: This static method starts the embedded Tomcat server and configures the Spring application context.
*   **Complexity Analysis (for `getProductById`):**
    *   **Time Complexity:** O(1) on average for `HashMap.get(id)`.
    *   **Space Complexity:** O(1) for method execution. (The `productData` map itself takes O(N) space where N is number of products).

#### Topic 2: Spring Data JPA and Database Interaction

**Practice Question 1:** Build a full CRUD REST API for a `Product` entity using Spring Boot and Spring Data JPA.

*   **Problem Statement:** Create a Spring Boot application that manages `Product` entities. Implement REST endpoints for:
    *   `GET /products`: Get all products.
    *   `GET /products/{id}`: Get product by ID.
    *   `POST /products`: Create a new product.
    *   `PUT /products/{id}`: Update an existing product.
    *   `DELETE /products/{id}`: Delete a product.
    *   Use Spring Data JPA with an in-memory database (H2) for simplicity.
*   **Solution Code (Requires `pom.xml` setup):**

    **1. `pom.xml` (Dependencies):**
    ```xml
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>3.2.5</version> <!-- Use a recent Spring Boot version -->
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <groupId>com.example</groupId>
        <artifactId>product-crud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <name>product-crud</name>
        <description>Demo project for Spring Boot Product CRUD</description>
        <properties>
            <java.version>17</java.version>
        </properties>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-data-jpa</artifactId>
            </dependency>
            <dependency>
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <scope>test</scope>
            </dependency>
        </dependencies>

        <build>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                </plugin>
            </plugins>
        </build>
    </project>
    ```

    **2. `application.properties` (Configuration for H2):**
    ```properties
    spring.datasource.url=jdbc:h2:mem:productdb
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    spring.h2.console.enabled=true # Enable H2 console for viewing data
    spring.jpa.hibernate.ddl-auto=update # Create/update schema automatically
    ```

    **3. `Product.java` (JPA Entity):**
    ```java
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;

    @Entity // Marks this class as a JPA entity, mapped to a table
    public class Product {
        @Id // Marks this field as the primary key
        @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
        private Long id;
        private String name;
        private double price;
        private String description;

        // Default constructor is required by JPA
        public Product() {}

        public Product(String name, double price, String description) {
            this.name = name;
            this.price = price;
            this.description = description;
        }

        // Getters and Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return "Product{id=" + id + ", name='" + name + "', price=" + price + ", description='" + description + "'}";
        }
    }
    ```

    **4. `ProductRepository.java` (JPA Repository Interface):**
    ```java
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository // Optional, but good practice
    public interface ProductRepository extends JpaRepository<Product, Long> {
        // JpaRepository provides CRUD operations (save, findById, findAll, deleteById, etc.)
        // and also supports derived query methods if needed, e.g.,
        // List<Product> findByNameContainingIgnoreCase(String name);
    }
    ```

    **5. `ProductController.java` (REST Controller):**
    ```java
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Optional;

    @RestController
    @RequestMapping("/api/products")
    public class ProductController {

        @Autowired // Injects the ProductRepository instance
        private ProductRepository productRepository;

        // GET all products
        @GetMapping
        public List<Product> getAllProducts() {
            return productRepository.findAll();
        }

        // GET product by ID
        @GetMapping("/{id}")
        public ResponseEntity<Product> getProductById(@PathVariable Long id) {
            Optional<Product> product = productRepository.findById(id);
            return product.map(ResponseEntity::ok) // If product found, return 200 OK
                          .orElseGet(() -> ResponseEntity.notFound().build()); // Else, return 404 Not Found
        }

        // POST create new product
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED) // Return 201 Created status
        public Product createProduct(@RequestBody Product product) {
            return productRepository.save(product); // Save and return the saved product (with generated ID)
        }

        // PUT update product by ID
        @PutMapping("/{id}")
        public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
            return productRepository.findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productDetails.getName());
                    existingProduct.setPrice(productDetails.getPrice());
                    existingProduct.setDescription(productDetails.getDescription());
                    Product updatedProduct = productRepository.save(existingProduct);
                    return ResponseEntity.ok(updatedProduct); // Return 200 OK with updated product
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Return 404 Not Found if product doesn't exist
        }

        // DELETE product by ID
        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.NO_CONTENT) // Return 204 No Content status
        public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
            return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().build(); // Return 204 No Content
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Return 404 Not Found
        }
    }
    ```

    **6. `ProductCrudApplication.java` (Main Application class):**
    ```java
    import org.springframework.boot.SpringApplication;
    import org.springframework.boot.autoconfigure.SpringBootApplication;

    @SpringBootApplication
    public class ProductCrudApplication {

        public static void main(String[] args) {
            SpringApplication.run(ProductCrudApplication.class, args);
            System.out.println("Product CRUD application started!");
            System.out.println("H2 Console: http://localhost:8080/h2-console");
            System.out.println("Endpoints available at http://localhost:8080/api/products");
        }
    }
    ```

*   **Explanation:**
    1.  **`pom.xml`**: Includes `spring-boot-starter-web` for REST, `spring-boot-starter-data-jpa` for JPA integration, and `h2` for an in-memory database.
    2.  **`application.properties`**: Configures H2 database, enables the H2 console (accessible at `/h2-console` to view table data), and sets `ddl-auto=update` for automatic schema creation/updates.
    3.  **`Product.java` (Entity)**:
        *   `@Entity`: Marks the class as a JPA entity. Hibernate (the default JPA provider) will map this to a database table named `product`.
        *   `@Id`: Specifies `id` as the primary key.
        *   `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Configures the `id` to be auto-incremented by the database.
        *   A no-argument constructor is required by JPA.
    4.  **`ProductRepository.java` (Repository)**:
        *   Extends `JpaRepository<Product, Long>`. This interface automatically provides common CRUD operations (like `save()`, `findById()`, `findAll()`, `deleteById()`) for the `Product` entity with a `Long` ID. No implementation is needed!
        *   Spring Data JPA will create an implementation at runtime.
    5.  **`ProductController.java` (Controller)**:
        *   `@RestController` and `@RequestMapping("/api/products")`: Standard setup for a REST controller.
        *   `@Autowired ProductRepository productRepository`: Spring's Dependency Injection automatically provides an instance of the `ProductRepository` to the controller.
        *   **`getAllProducts()` (GET /api/products)**: Uses `productRepository.findAll()` to retrieve all products.
        *   **`getProductById()` (GET /api/products/{id})**: Uses `productRepository.findById(id)` which returns an `Optional<Product>`. The `map().orElseGet()` pattern is used for concise handling of found/not-found cases.
        *   **`createProduct()` (POST /api/products)**: Takes a `Product` object from the request body (`@RequestBody`), saves it using `productRepository.save()`, and returns the saved product (which now includes the database-generated ID). `@ResponseStatus(HttpStatus.CREATED)` sets the response code to 201.
        *   **`updateProduct()` (PUT /api/products/{id})**: Finds the existing product by ID. If found, it updates its properties with data from `productDetails` and saves it. If not found, returns 404.
        *   **`deleteProduct()` (DELETE /api/products/{id})**: Finds the product by ID. If found, it deletes it using `productRepository.delete()` and returns a 204 No Content status. If not found, returns 404.
    6.  **`ProductCrudApplication.java`**: The main Spring Boot entry point.

*   **To run this:**
    1.  Create a new Spring Boot project (e.g., using Spring Initializr) with `Spring Web`, `Spring Data JPA`, and `H2 Database` dependencies.
    2.  Copy these files into the appropriate `src/main/java` and `src/main/resources` directories.
    3.  Run the `ProductCrudApplication`'s `main` method.
    4.  Test with tools like Postman or curl.
        *   `GET http://localhost:8080/api/products`
        *   `POST http://localhost:8080/api/products` with JSON body `{"name": "New Product", "price": 50.0, "description": "A wonderful new item"}`
        *   `GET http://localhost:8080/api/products/1` (after creating some)
        *   `PUT http://localhost:8080/api/products/1` with JSON body `{"name": "Updated Laptop", "price": 1250.0, "description": "High-performance laptop"}`
        *   `DELETE http://localhost:8080/api/products/1`
*   **Complexity Analysis (for typical repository operations):**
    *   **`findAll()`:** O(N) where N is the number of products, as it fetches all.
    *   **`findById()`:** O(log N) for indexed primary key lookup in a balanced tree structure (like B-tree used by RDBMS).
    *   **`save()` (insert/update):** O(log N) for insert/update with primary key index, plus I/O overhead.
    *   **`delete()`:** O(log N) for primary key lookup and deletion, plus I/O overhead.
    *   **Space Complexity:** Dependent on the database and number of entries.

---

## Month 4: Advanced System Design, Design Patterns, SOLID Principles & Interview Mastery - Solutions

### Week 17: Distributed Systems Concepts & Design Patterns Deep Dive

#### Topic 1: Consistency Models and CAP Theorem

**Practice Question 1:** Describe a scenario where a system would prioritize Availability over Consistency.

*   **Problem Statement:** Provide a real-world example of a distributed system that would explicitly choose Availability over strong Consistency according to the CAP Theorem, explaining why this choice is made.
*   **Solution:**
    *   **Scenario:** A large-scale social media platform's news feed or timeline.
    *   **Explanation:**
        *   **High Availability is paramount:** Users expect to see their feed updates instantly, almost 100% of the time. If the system goes down, even for a short period, user experience is severely degraded, leading to frustration and potential loss of users.
        *   **Eventual Consistency is acceptable:** While a user might post an update, and a friend's feed might not show it *immediately* (due to network partitions or replication delays), it's generally acceptable for the update to appear within a few seconds or minutes. Users tolerate a slight delay in seeing the absolute latest data if it means the service is always accessible.
        *   **Partition Tolerance is unavoidable:** Distributed systems, especially those spanning multiple data centers or regions, are inherently subject to network partitions. When a partition occurs, the system must choose between guaranteeing consistency (meaning some requests might fail if they can't reach a consistent state across partitions) or availability (meaning all requests are served, even if the data might be slightly stale).
        *   **Trade-off:** For a news feed, sacrificing strong consistency (i.e., allowing different users to momentarily see slightly different versions of the feed) to ensure that the feed is *always* available and responsive is a common and necessary trade-off. This is why many social media platforms use NoSQL databases (like Cassandra, DynamoDB) that favor BASE (Basically Available, Soft state, Eventually consistent) properties.
*   **Complexity Analysis:** N/A (conceptual explanation).

#### Topic 2: Behavioral Design Patterns & SOLID Principles Applied

**Practice Question 1:** Implement the State pattern for a `Door` object (states: Open, Closed, Locked).

*   **Problem Statement:** Design a `Door` object whose behavior changes based on its current state. The door can be `Open`, `Closed`, or `Locked`. Implement methods like `open()`, `close()`, `lock()`, and `unlock()` which behave differently depending on the door's current state.
*   **Solution Code:**
    ```java
    // 1. State Interface
    interface DoorState {
        void open(Door door);
        void close(Door door);
        void lock(Door door);
        void unlock(Door door);
    }

    // 2. Concrete State: OpenState (Adheres to SRP for Open state behavior)
    class OpenState implements DoorState {
        @Override
        public void open(Door door) {
            System.out.println("Door is already open.");
        }

        @Override
        public void close(Door door) {
            System.out.println("Closing the door.");
            door.setState(new ClosedState()); // Change state
        }

        @Override
        public void lock(Door door) {
            System.out.println("Cannot lock an open door. Close it first.");
        }

        @Override
        public void unlock(Door door) {
            System.out.println("Door is open, no need to unlock.");
        }

        @Override
        public String toString() { return "Open"; }
    }

    // 3. Concrete State: ClosedState (Adheres to SRP for Closed state behavior)
    class ClosedState implements DoorState {
        @Override
        public void open(Door door) {
            System.out.println("Opening the door.");
            door.setState(new OpenState()); // Change state
        }

        @Override
        public void close(Door door) {
            System.out.println("Door is already closed.");
        }

        @Override
        public void lock(Door door) {
            System.out.println("Locking the door.");
            door.setState(new LockedState()); // Change state
        }

        @Override
        public void unlock(Door door) {
            System.out.println("Door is closed, no need to unlock.");
        }

        @Override
        public String toString() { return "Closed"; }
    }

    // 4. Concrete State: LockedState (Adheres to SRP for Locked state behavior)
    class LockedState implements DoorState {
        @Override
        public void open(Door door) {
            System.out.println("Cannot open a locked door. Unlock it first.");
        }

        @Override
        public void close(Door door) {
            System.out.println("Door is already locked (and closed).");
        }

        @Override
        public void lock(Door door) {
            System.out.println