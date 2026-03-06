Here's a comprehensive Java template for competitive programming, covering essential data structures and utilities. I've focused on commonly used methods and best practices for competitive settings, adding comments for clarity.

```java
import java.io.*;
import java.util.*;

// It's good practice to encapsulate everything within a class for competitive programming.
// The main class name should often be "Main" or "Solution" for online judges.
public class Main {

    // ----------- Input/Output Faster IO Start -----------
    // Using a custom FastReader class can significantly speed up I/O,
    // which is crucial in problems with large inputs.
    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }
    }

    static FastReader sc = new FastReader();
    // Using PrintWriter for faster output.
    static PrintWriter out = new PrintWriter(System.out);
    // ----------- Input/Output Faster IO End -----------

    public static void main(String[] args) {
        // Always call solve() to organize your code and handle multiple test cases
        // if necessary.
        solve();
        out.flush(); // Don't forget to flush the PrintWriter!
    }

    public static void solve() {
        // Example usage of FastReader and PrintWriter:
        int t = sc.nextInt(); // For multiple test cases
        while (t-- > 0) {
            int n = sc.nextInt();
            long m = sc.nextLong();
            String s = sc.nextLine();
            out.println("Result: " + (n + m));
        }


        // --------------------- Basic Data Structures ---------------------

        // --- Arrays ---
        // Declaration and Initialization
        int[] arr1 = new int[5]; // Initializes with default values (0 for int)
        int[] arr2 = {1, 2, 3, 4, 5}; // Literal initialization
        int[] arr3 = new int[]{1, 2, 3}; // Another way to initialize

        // 2D Arrays
        int[][] matrix = new int[3][4];
        int[][] matrixLiteral = {{1,2},{3,4}};

        // Utility methods from java.util.Arrays
        Arrays.sort(arr2); // Sorts in ascending order
        // For custom sorting: 
        Arrays.sort(array, Comparator.reverseOrder()); (for Integer objects)
        // For primitive arrays in reverse, you might need to convert to Wrapper type or use custom sort logic.
        
        // Filling an array
        Arrays.fill(arr1, 10); // Fills all elements with 10

        // Copying arrays
        int[] arr4 = Arrays.copyOf(arr2, arr2.length); // Full copy
        int[] arr5 = Arrays.copyOfRange(arr2, 1, 3); // Copies elements from index 1 (inclusive) to 3 (exclusive)

        // Printing arrays (for debugging)
        out.println(Arrays.toString(arr2)); // For 1D arrays
        out.println(Arrays.deepToString(matrix)); // For 2D arrays

        // --- Strings ---
        String str1 = "hello";
        String str2 = new String("world");
        char ch = str1.charAt(0); // 'h'
        int len = str1.length(); // 5
        String sub = str1.substring(1, 4); // "ell"
        boolean contains = str1.contains("lo"); // true
        String[] parts = "a,b,c".split(","); // {"a", "b", "c"}
        char[] charArray = str1.toCharArray(); // Convert to char array

        //iterate a String
        for (char ch : charArray) {
            out.print(ch + " ");
        }
        //iterate a String
        for (int i = 0; i < myString.length(); i++) {
            char ch = myString.charAt(i);
            out.print(ch + " ");
        }
        //itereate using streams
        str2.chars().forEach(ch -> System.out.print((char) ch + " "));
        

        // StringBuilder (mutable, faster for string manipulations)
        StringBuilder sb = new StringBuilder();
        sb.append("Competitive ").append("Programming");
        sb.insert(0, "Java ");
        sb.delete(5, 9); // Deletes "Prog"
        sb.reverse(); // reverse the string
        char c = sb.charAt(1); // get character at a index
        sb.setCharAt(0,'a'); //set character at a specific position
        out.println(sb.toString()); // Convert back to String

        //itereate over StringBuffer
        for (int i = 0; i < sb.length(); i++) {
            char ch = sb.charAt(i);
            out.print(ch + " ");
        }


        char[] sbCharArray = sb.toString().toCharArray();
        for (char ch : sbCharArray) {
            out.print(ch + " ");
        }

        //itereate using streams
        sb.toString().chars().forEach(ch -> out.print((char) ch + " "));

        // StringBuffer (thread-safe, slower than StringBuilder, generally not needed in CP)
        StringBuffer sbuf = new StringBuffer();

        // --- ArrayList (Dynamic Array) ---
        List<Integer> list = new ArrayList<>(); // Use List interface for declaration
        list.add(10);
        list.add(20);
        list.add(0, 5); // Add 5 at index 0
        out.println(list.get(0)); // Get element at index 0 (5)
        list.set(1, 15); // Set element at index 1 to 15 (replaces 10)
        list.remove(Integer.valueOf(20)); // Remove by value
        list.remove(0); // Remove by index
        out.println(list.size());
        out.println(list.isEmpty());
        Collections.sort(list); // Sorts the list
        
        // 2D ArrayList
        List<List<Integer>> listOfLists = new ArrayList<>();
        // Add rows:
        listOfLists.add(new ArrayList<>(Arrays.asList(1, 2)));
        listOfLists.add(new ArrayList<>(Arrays.asList(3, 4, 5)));

        // --- LinkedList (Doubly Linked List) ---
        // Good for frequent insertions/deletions at ends or known positions (via iterator)
        // Not good for random access (get(index) is O(N))
        LinkedList<String> linkedList = new LinkedList<>();
        linkedList.add("first");
        linkedList.addLast("last");
        linkedList.addFirst("new first");
        linkedList.removeFirst();
        out.println(linkedList.peekFirst());

        // --- HashSet (Unordered, no duplicates, O(1) average time for add/remove/contains) ---
        Set<String> set = new HashSet<>(); // Use Set interface for declaration
        set.add("Apple");
        set.add("Banana");
        set.add("Apple"); // Won't add, duplicates not allowed
        out.println(set.contains("Banana"));
        set.remove("Apple");

        // Iterating over a set
        for (String item : set) {
            out.println(item);
        }

        // --- TreeSet (Ordered, no duplicates, O(logN) time for add/remove/contains) ---
        // Stores elements in a sorted order. Useful for finding min/max or range queries.
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(5);
        treeSet.add(2);
        treeSet.add(8);
        out.println(treeSet.first()); // 2
        out.println(treeSet.last());  // 8
        out.println(treeSet.lower(5)); // 2 (element strictly less than 5)
        out.println(treeSet.higher(5)); // 8 (element strictly greater than 5)
        out.println(treeSet.ceiling(5)); // 2 (Smallest element >= 5)
        out.println(treeSet.floor(5)); // 8 (Largest element <= 5)


        // --- HashMap (Unordered, key-value pairs, O(1) average time) ---
        Map<String, Integer> map = new HashMap<>(); // Use Map interface for declaration
        map.put("Udit", 1);
        map.put("Rahul", 7);
        out.println(map.get("Udit")); // 1
        out.println(map.getOrDefault("NonExistent", 0)); // 0
        map.containsKey("Udit");
        map.containsValue(7);
        map.remove("Rahul");

        // Iterating over a map
        // For entries (key-value pairs)
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            out.println(entry.getKey() + " -> " + entry.getValue());
        }
        //For keys only
        for (String key : map.keySet()) {
            out.println(key);
        }
        //For values only
        for (Integer value : map.values()) {
            out.println(value);
        }

        // --- TreeMap (Ordered, key-value pairs, O(logN) time) ---
        // Stores mappings in a sorted order of keys. Useful for ordered map operations.
        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(3, "C");
        treeMap.put(1, "A");
        treeMap.put(2, "B");
        out.println(treeMap.firstKey()); // 1
        out.println(treeMap.lastKey()); // 3
        out.println(treeMap.floorKey(2)); // 2 (key <= 2)
        out.println(treeMap.ceilingKey(2)); // 2 (key >= 2)


        // --- Stack (LIFO - Last In, First Out) ---
        // Prefer Deque as a stack for better performance and flexibility (ArrayDeque).
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(10); // Add to top
        stack.push(20);
        out.println(stack.peek()); // Look at top (20)
        out.println(stack.pop()); // Remove from top (20)
        out.println(stack.isEmpty());

        // --- Queue (FIFO - First In, First Out) ---
        // Prefer Deque as a queue (ArrayDeque) for general purpose, or LinkedList.
        // For priority queues, use PriorityQueue.
        Queue<Integer> queue = new ArrayDeque<>(); // Standard queue operations
        Queue<Integer> queue = new LinkedList<>(); // Also works
        queue.offer(10); // Add to rear
        queue.offer(20);
        out.println(queue.peek()); // Look at front (10)
        out.println(queue.poll()); // Remove from front (10)

        // --- PriorityQueue (Min-Heap by default) ---
        // Elements are ordered based on their natural ordering or a provided Comparator.
        // Smallest element always at the front (peek()).
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.offer(5);
        minHeap.offer(1);
        minHeap.offer(10);
        out.println(minHeap.poll()); // 1 (smallest)

        // Max-Heap (using a custom comparator)
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        maxHeap.offer(5);
        maxHeap.offer(1);
        maxHeap.offer(10);
        out.println(maxHeap.poll()); // 10 (largest)

        // --- Deque (Double-Ended Queue) ---
        // Can be used as both a stack and a queue, or a true double-ended queue.
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(1); // Add to front
        deque.addLast(2); // Add to back
        deque.removeFirst();
        deque.removeLast();
        out.println(deque.peekFirst());
        out.println(deque.peekLast());

        // --------------------- Math & Utility Functions ---------------------

        // --- Math Class ---
        Math.abs(x)
        Math.min(a, b)
        Math.max(a, b)
        Math.sqrt(x)
        Math.pow(base, exponent)
        Math.log(x), Math.log10(x)
        Math.ceil(x), Math.floor(x), Math.round(x)

        // --- Collections Utility ---
        Collections.sort(List<T> list) - sorts a list
        Collections.reverse(List<T> list) - reverses a list
        Collections.min(Collection<T> coll), Collections.max(Collection<T> coll)
        Collections.frequency(Collection<?> c, Object o) //- counts occurrences
        Collections.binarySearch(List<? extends Comparable<? super T>> list, T key)

        // --------------------- Common Algorithms/Patterns ---------------------

        // --- Graph Traversal Directions (for BFS/DFS on grid) ---
        // These are standard 4-directional or 8-directional arrays.
        int[][] DIRS_4 = {
            {0, 1},   // right
            {1, 0},   // down
            {0, -1},  // left
            {-1, 0}   // up
        };

        int[][] DIRS_8 = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0}, // 4 directions
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1} // Diagonals
        };

        // --- `isValid` helper function for grid problems ---
        boolean isValid(int r, int c, int maxRows, int maxCols) {
            return r >= 0 && r < maxRows && c >= 0 && c < maxCols;
        }

         // --------------------- 1. Custom Comparators ---------------------
        out.println("--- Custom Comparators ---");

        // Example: A custom class to store points
        class Point implements Comparable<Point> {
            int x, y;
            Point(int x, int y) {
                this.x = x;
                this.y = y;
            }

            // Natural ordering: Sort by x, then by y
            @Override
            public int compareTo(Point other) {
                if (this.x != other.x) {
                    return Integer.compare(this.x, other.x);
                }
                return Integer.compare(this.y, other.y);
            }

            @Override
            public boolean equals(Object o) { // Important for use in Hash-based collections
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return x == point.x && y == point.y;
            }

            @Override
            public int hashCode() { // Important for use in Hash-based collections
                return Objects.hash(x, y);
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }
        }

        List<Point> points = new ArrayList<>();
        points.add(new Point(3, 5));
        points.add(new Point(1, 2));
        points.add(new Point(3, 1));
        points.add(new Point(2, 5));
        points.add(new Point(1, 2)); // Duplicate for set/map testing

        // 1.1 Sorting a List of custom objects using their natural ordering (compareTo)
        List<Point> pointsNaturalSort = new ArrayList<>(points); // Create a copy
        Collections.sort(pointsNaturalSort);
        out.println("Points (Natural Order - x then y): " + pointsNaturalSort);
        // Expected: [(1, 2), (1, 2), (2, 5), (3, 1), (3, 5)]

        // 1.2 Custom Comparator for sorting a List (e.g., sort by y in descending order, then by x ascending)
        Comparator<Point> customPointComparator = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if (p1.y != p2.y) {
                    return Integer.compare(p2.y, p1.y); // Descending y
                }
                return Integer.compare(p1.x, p2.x); // Ascending x
            }
        };

        List<Point> pointsCustomSort = new ArrayList<>(points); // Create a fresh copy
        Collections.sort(pointsCustomSort, customPointComparator);
        out.println("Points (Custom Sort - y desc, x asc): " + pointsCustomSort);
        // Expected: [(2, 5), (3, 5), (1, 2), (1, 2), (3, 1)]

        // 1.3 Using custom comparator with PriorityQueue (e.g., Min-heap based on y, then x)
        PriorityQueue<Point> pqMinY = new PriorityQueue<>(new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if (p1.y != p2.y) {
                    return Integer.compare(p1.y, p2.y); // Ascending y
                }
                return Integer.compare(p1.x, p2.x); // Ascending x
            }
        });
        pqMinY.addAll(points);
        out.print("PQ (min Y, then min X): ");
        while (!pqMinY.isEmpty()) {
            out.print(pqMinY.poll() + " "); // (3, 1) (1, 2) (1, 2) (2, 5) (3, 5)
        }
        out.println();

        // 1.4 Using custom comparator with TreeSet (e.g., sort by y descending, then x ascending)
        TreeSet<Point> treeSetCustom = new TreeSet<>(customPointComparator);
        treeSetCustom.addAll(points);
        out.println("TreeSet (y desc, x asc): " + treeSetCustom); // Duplicates handled by equals/hashCode for Point
        // Expected: [(2, 5), (3, 5), (1, 2), (3, 1)] (Note: duplicate (1,2) is removed as per Set contract)

        out.println();


        // --------------------- 2. Bit Manipulation ---------------------
        out.println("--- Bit Manipulation ---");

        int a = 5;  // Binary: 0101
        int b = 3;  // Binary: 0011

        // AND (&): Sets bit if both bits are 1
        int andResult = a & b; // 0001 (Decimal: 1)
        out.println("a & b (0101 & 0011) = " + andResult);

        // OR (|): Sets bit if at least one bit is 1
        int orResult = a | b;  // 0111 (Decimal: 7)
        out.println("a | b (0101 | 0011) = " + orResult);

        // XOR (^): Sets bit if bits are different
        int xorResult = a ^ b; // 0110 (Decimal: 6)
        out.println("a ^ b (0101 ^ 0011) = " + xorResult);

        // NOT (~): Inverts all bits (results in negative number due to two's complement)
        int notA = ~a; // For positive numbers, ~x = -(x+1)
        out.println("~a (~0101) = " + notA); // -6

        // Left Shift (<<): Shifts bits to the left, fills with 0s on the right (multiplies by 2^n)
        int leftShift = a << 1; // 01010 (Decimal: 10)
        out.println("a << 1 (0101 << 1) = " + leftShift); // 5 * 2^1 = 10

        // Signed Right Shift (>>): Shifts bits to the right, fills with sign bit on the left (divides by 2^n, handles negative)
        int rightShift = a >> 1; // 0010 (Decimal: 2)
        out.println("a >> 1 (0101 >> 1) = " + rightShift); // 5 / 2^1 = 2 (integer division)
        int negNum = -10; // Binary (two's complement): ...11110110
        int negRightShift = negNum >> 1; // ...11111011 (Decimal: -5)
        out.println("-10 >> 1 = " + negRightShift);

        // Unsigned Right Shift (>>>): Shifts bits to the right, fills with 0s on the left (always results in positive for negative numbers)
        int unsignedRightShift = a >>> 1; // 0010 (Decimal: 2)
        out.println("a >>> 1 (0101 >>> 1) = " + unsignedRightShift);
        int negUnsignedRightShift = negNum >>> 1; // 0111... (very large positive number)
        out.println("-10 >>> 1 = " + negUnsignedRightShift);

        // Common Bit Manipulation Techniques:
        int num = 13; // Binary: 1101

        // Check if k-th bit is set (0-indexed from right)
        int k = 2; // Check 2nd bit (0-indexed)
        boolean isKthBitSet = ((num >> k) & 1) == 1; // Or (num & (1 << k)) != 0
        out.println("Is " + k + "-th bit of " + num + " set? " + isKthBitSet); // True (0-indexed)

        // Set k-th bit
        int setKthBit = num | (1 << 0); // Set 0-th bit (1101 | 0001 = 1101)
        out.println("Set 0-th bit of " + num + ": " + setKthBit);

        // Clear k-th bit
        int clearKthBit = num & ~(1 << 2); // Clear 2nd bit (1101 & ~0100 = 1101 & 1011 = 1001)
        out.println("Clear 2nd bit of " + num + ": " + clearKthBit);

        // Toggle k-th bit
        int toggleKthBit = num ^ (1 << 1); // Toggle 1st bit (1101 ^ 0010 = 1111)
        out.println("Toggle 1st bit of " + num + ": " + toggleKthBit);

        // Turn off the rightmost set bit
        int turnOffRightmost = num & (num - 1); // 1101 & 1100 = 1100 (12)
        out.println("Turn off rightmost set bit of " + num + ": " + turnOffRightmost);

        // Check if a number is a power of 2 (for positive numbers)
        boolean isPowerOfTwo = (num > 0) && ((num & (num - 1)) == 0);
        out.println(num + " is power of two? " + isPowerOfTwo); // False
        out.println("8 is power of two? " + ((8 > 0) && ((8 & (8 - 1)) == 0))); // True

        // Count set bits (Brian Kernighan's Algorithm)
        int countSetBits = 0;
        int tempNum = num;
        while (tempNum > 0) {
            tempNum &= (tempNum - 1); // Turn off rightmost set bit
            countSetBits++;
        }
        out.println("Number of set bits in " + num + ": " + countSetBits); // 3

        // Using built-in functions:
        out.println("Integer.bitCount(" + num + ") = " + Integer.bitCount(num)); // 3
        out.println("Integer.numberOfLeadingZeros(" + num + ") = " + Integer.numberOfLeadingZeros(num));
        out.println("Integer.numberOfTrailingZeros(" + num + ") = " + Integer.numberOfTrailingZeros(num));
        out.println();


        // --------------------- 3. BigInteger / BigDecimal ---------------------
        out.println("--- BigInteger / BigDecimal ---");

        // 3.1 BigInteger (for arbitrarily large integers)
        BigInteger bigInt1 = new BigInteger("12345678901234567890");
        BigInteger bigInt2 = BigInteger.valueOf(500); // From long
        BigInteger bigInt3 = new BigInteger("98765432109876543210");

        // Basic arithmetic operations
        BigInteger sum = bigInt1.add(bigInt3);
        BigInteger product = bigInt1.multiply(bigInt2);
        BigInteger difference = bigInt3.subtract(bigInt1);
        BigInteger quotient = bigInt3.divide(bigInt2);
        BigInteger remainder = bigInt3.remainder(bigInt2); // % operator equivalent
        BigInteger[] divAndRem = bigInt3.divideAndRemainder(bigInt2); // Returns array [quotient, remainder]

        out.println("BigInt1: " + bigInt1);
        out.println("BigInt2: " + bigInt2);
        out.println("BigInt3: " + bigInt3);
        out.println("Sum: " + sum);
        out.println("Product: " + product);
        out.println("Difference: " + difference);
        out.println("Quotient: " + quotient);
        out.println("Remainder: " + remainder);
        out.println("Div and Rem: " + Arrays.toString(divAndRem));

        // Other useful BigInteger methods:
        BigInteger gcd = bigInt1.gcd(bigInt3);
        BigInteger pow = BigInteger.valueOf(2).pow(100); // 2^100
        BigInteger modPow = BigInteger.valueOf(2).modPow(BigInteger.valueOf(100), BigInteger.valueOf(MOD)); // (2^100) % MOD
        BigInteger negate = bigInt1.negate();
        BigInteger abs = bigInt1.abs();
        int compare = bigInt1.compareTo(bigInt3); // -1 if less, 0 if equal, 1 if greater
        boolean equals = bigInt1.equals(bigInt3);
        int intValue = bigInt2.intValue(); // Convert to int (may lose precision if too large)
        long longValue = bigInt2.longValue(); // Convert to long

        out.println("GCD(" + bigInt1 + ", " + bigInt3 + "): " + gcd);
        out.println("2^100: " + pow);
        out.println("(2^100) % " + MOD + ": " + modPow);
        out.println("bigInt1.compareTo(bigInt3): " + compare);

        // 3.2 BigDecimal (for precise decimal arithmetic)
        // IMPORTANT: Use String constructor to avoid floating-point representation issues
        BigDecimal bd1 = new BigDecimal("10.00");
        BigDecimal bd2 = new BigDecimal("3.0");
        // BigDecimal bd3 = new BigDecimal(0.1); // AVOID THIS! Use "0.1" instead.

        BigDecimal bdSum = bd1.add(bd2);
        BigDecimal bdSubtract = bd1.subtract(bd2);
        BigDecimal bdMultiply = bd1.multiply(bd2);
        // For division, you must specify rounding mode and precision to handle non-terminating decimals
        BigDecimal bdDivide = bd1.divide(bd2, 2, BigDecimal.ROUND_HALF_UP); // 2 decimal places, round half up

        out.println("BigDecimal1: " + bd1);
        out.println("BigDecimal2: " + bd2);
        out.println("bdSum: " + bdSum);
        out.println("bdSubtract: " + bdSubtract);
        out.println("bdMultiply: " + bdMultiply);
        out.println("bdDivide (10.00 / 3.0, 2 decimal places): " + bdDivide); // 3.33

        out.println();


        // --------------------- 4. Modular Arithmetic ---------------------
        out.println("--- Modular Arithmetic ---");
        long val1 = 1_000_000_000_000_000_000L; // A very large number
        long val2 = 5_000_000_000_000_000_000L; // Another very large number

        // Basic operations: (a + b) % M = ((a % M) + (b % M)) % M
        // (a - b) % M = ((a % M) - (b % M) + M) % M (add M to ensure positive result)
        // (a * b) % M = ((a % M) * (b % M)) % M
        // (a / b) % M = (a * modInverse(b)) % M (division requires modular inverse)

        // Ensure numbers are within [0, MOD-1] before operations
        long a_mod = val1 % MOD;
        long b_mod = val2 % MOD;

        long sum_mod = (a_mod + b_mod) % MOD;
        out.println("(" + val1 + " + " + val2 + ") % " + MOD + " = " + sum_mod);

        long diff_mod = (a_mod - b_mod + MOD) % MOD; // Add MOD to handle potential negative result
        out.println("(" + val1 + " - " + val2 + ") % " + MOD + " = " + diff_mod);

        long prod_mod = (a_mod * b_mod) % MOD;
        out.println("(" + val1 + " * " + val2 + ") % " + MOD + " = " + prod_mod);

        // Modular Exponentiation (a^b % M)
        long base = 2;
        long exponent = 100;
        long result_pow = power(base, exponent); // Using custom power function
        out.println(base + "^" + exponent + " % " + MOD + " = " + result_pow);
        // Or using BigInteger: BigInteger.valueOf(base).modPow(BigInteger.valueOf(exponent), BigInteger.valueOf(MOD));

        // Modular Inverse (a^-1 % M) using Fermat's Little Theorem (for prime M)
        // a^(M-2) % M is modular inverse of a for prime M
        long numForInverse = 7;
        long inverse = power(numForInverse, MOD - 2); // Only works if MOD is prime
        out.println("Modular inverse of " + numForInverse + " mod " + MOD + " = " + inverse);
        // Verify: (numForInverse * inverse) % MOD should be 1
        out.println("(" + numForInverse + " * " + inverse + ") % " + MOD + " = " + ((numForInverse * inverse) % MOD));

        // Division in modular arithmetic: (A / B) % MOD = (A * (B^-1)) % MOD
        long divResult = (a_mod * power(b_mod, MOD - 2)) % MOD;
        out.println("(" + val1 + " / " + val2 + ") % " + MOD + " = " + divResult);

        out.println();
    }

    // You can add helper methods here if they are frequently used across problems.
    // Example: GCD function
    static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    //Example: Sieve of Eratosthenes (for primes up to N)
    boolean[] isPrime;
    void sieve(int n) {
        isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= n; i += p)
                    isPrime[i] = false;
            }
        }
    }


    // Consider adding a custom Pair class if you often need to store two related values.
    class Pair<K, V> {
        K key;
        V value;
        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        // Override equals and hashCode if needed for collections
    }


    //can be used as a pair (just like c++ pair<T,U>)
    //can also be used as Tuple just add number of values
    public record Pair<A, B>(A first, B second) {}
    
    //Use case
    Pair<Integer, String> p = new Pair<>(1, "one");
    int key = p.first();
    String value = p.second();
    //Whatever the name you pass in key (here first and second) will be automatically available for access.
    // You can also choose a custom class for this case.


    // --------------------- Helper Methods for Modular Arithmetic ---------------------

    /**
     * Computes (base ^ exp) % MOD efficiently using binary exponentiation (exponentiation by squaring).
     *
     * @param base The base number.
     * @param exp  The exponent.
     * @return (base ^ exp) % MOD
     */
    static long power(long base, long exp) {
        long res = 1;
        base %= MOD; // Reduce base modulo MOD
        while (exp > 0) {
            if (exp % 2 == 1) { // If exponent is odd, multiply result by base
                res = (res * base) % MOD;
            }
            base = (base * base) % MOD; // Square the base
            exp /= 2; // Halve the exponent
        }
        return res;
    }

    // Extended Euclidean Algorithm for Modular Inverse (works for non-prime MOD if gcd(a, MOD) = 1)
    // You would typically use this if MOD is not prime or if Fermat's Little Theorem isn't applicable.
    // However, for most CP problems, MOD is prime, so power(a, MOD-2) is sufficient and simpler.
    static long extendedGcd(long a, long b, long[] x, long[] y) {
        if (a == 0) {
            x[0] = 0;
            y[0] = 1;
            return b;
        }
        long[] x1 = new long[1], y1 = new long[1];
        long gcd = extendedGcd(b % a, a, x1, y1);
        x[0] = y1[0] - (b / a) * x1[0];
        y[0] = x1[0];
        return gcd;
    }
    
    static long modInverseEuclidean(long a, long m) {
        long[] x = new long[1], y = new long[1];
        long g = extendedGcd(a, m, x, y);
        if (g != 1) return -1; // modular inverse does not exist
        return (x[0] % m + m) % m; // ensure result is positive
    }

    // --------------------- 4. Common Helper Methods ---------------------


    static int digitOps(int n){
        int sum = 0;
        while(n>0){
            int c = n%10;
            sum += c;
            n = n/10;
        }

        return sum;
    }




    
}
```

Here's a breakdown of the template and why certain elements are included:

### Key Components and Explanations:

1.  **`FastReader` and `PrintWriter`**:
    *   **Why?** Standard `Scanner` and `System.out.println` can be too slow for problems with large I/O, leading to Time Limit Exceeded (TLE). `BufferedReader` and `PrintWriter` combined with `StringTokenizer` offer significant speed improvements.
    *   **Usage:** Create a `static` instance of `FastReader` and `PrintWriter`. Use `sc.nextInt()`, `sc.nextLong()`, `sc.nextLine()` for input and `out.println()` for output.
    *   **Important:** Remember `out.flush()` at the end of `main` to ensure all buffered output is written.

2.  **`main` and `solve` Methods**:
    *   **Why?** It's common practice to put the core logic inside a `solve()` method. This makes it easy to handle multiple test cases (just call `solve()` inside a loop).

3.  **Basic Data Structures**:
    *   **Arrays:** Essential for fixed-size collections. `Arrays.sort()`, `Arrays.fill()`, `Arrays.copyOf()`, `Arrays.toString()`, `Arrays.deepToString()` are your best friends.
    *   **Strings and `StringBuilder`:** `String` is immutable. For any string manipulation (appending, inserting, deleting), `StringBuilder` is far more efficient than repeated `String` concatenations, which create new `String` objects each time.
    *   **`ArrayList` (`List`):** Dynamic array. Good for general-purpose lists where elements are added/removed mostly at the end or random access is frequent.
    *   **`LinkedList` (`Deque` or `List`):** Good for efficient insertions/deletions at the beginning or end. Less efficient for random access.
    *   **`HashSet` (`Set`):** Stores unique elements, unordered. Average O(1) for add, remove, contains.
    *   **`TreeSet` (`Set`):** Stores unique elements, ordered (natural order or custom comparator). Average O(logN) for add, remove, contains. Useful for finding min/max, or nearest elements.
    *   **`HashMap` (`Map`):** Stores key-value pairs, unordered. Average O(1) for put, get, remove.
    *   **`TreeMap` (`Map`):** Stores key-value pairs, ordered by keys. Average O(logN) for put, get, remove. Useful for ordered map operations (e.g., `firstKey()`, `lastKey()`).
    *   **`Deque` (`ArrayDeque`):** **Highly recommended** over `Stack` and `LinkedList` when functioning as a stack or a queue. It's faster and more flexible.
        *   **As a Stack:** Use `push()`, `pop()`, `peek()`.
        *   **As a Queue:** Use `offer()`, `poll()`, `peek()`.
    *   **`PriorityQueue`:** Implements a min-heap by default. `offer()` adds an element, `poll()` retrieves and removes the smallest element, `peek()` just retrieves the smallest. You can easily make it a max-heap using `Collections.reverseOrder()`.

4.  **Math & Utility Functions**:
    *   The `Math` class provides common mathematical operations.
    *   `Collections` utility class offers methods for sorting, searching, and manipulating lists and collections.

5.  **Common Algorithms/Patterns**:
    *   **`DIRS_4` and `DIRS_8`**: Standard arrays for navigating grids (e.g., in BFS/DFS problems).
    *   **`isValid` function**: A simple helper to check if coordinates are within grid boundaries, crucial for many grid-based problems.

6.  **Helper Methods (Example)**:
    *   **`gcd` (Greatest Common Divisor):** A common number theory function.
    *   **`sieve` (Sieve of Eratosthenes):** For generating prime numbers up to a certain limit.
    *   **`Pair` class:** If you frequently need to return or store two related values, a simple `Pair` class can be helpful. Remember to override `equals()` and `hashCode()` if you plan to use `Pair` objects in `HashSet` or `HashMap`.

### Things not explicitly included (as per your request to exclude large algorithms you've covered) but important for CP:

*   **Custom Comparators:** For sorting custom objects or providing specific sorting logic to `Arrays.sort()`, `Collections.sort()`, `PriorityQueue`, `TreeSet`, `TreeMap`.
*   **Bit Manipulation:** Operators like `&`, `|`, `^`, `~`, `<<`, `>>`, `>>>` are very powerful.
*   **`BigInteger` / `BigDecimal`:** For problems involving extremely large numbers that exceed `long` capacity, or precise decimal arithmetic.
*   **Modular Arithmetic:** For problems where answers need to be returned modulo a large prime.
*   **Specific Algorithms:** DFS, BFS, Dijkstra, Floyd-Warshall, Dynamic Programming patterns, Segment Trees, Fenwick Trees, Disjoint Set Union (DSU), etc. (You mentioned you've covered these).

This template should give you a solid foundation for most competitive programming problems in Java. Good luck!