import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Template {

    public static void main(String[] args) {

        // learn commmon
        // use "of" methods to quickly create list,set or map (immutable)
        List.of(1, 2, 3);
        Set.of("udit", "rahul");
        Map.of("udit", 1, "rahul", 7);

        // learn map
        Map<String, Integer> map = new HashMap<>();

        map.put("Apple", 10);
        map.put("Banana", 20);
        // entry set-> iterate over map entries
        for (var entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        // key set -> iterate over keys
        for (String key : map.keySet()) {
            System.out.println(key);
        }
        // value set -> iterate over values
        for (Integer value : map.values()) {
            System.out.println(value);
        }

        // optional use var to auto detect type
        for (var value : map.values()) {
            System.out.println(value);
        }
        // return Map.of("A", 1, "B", 2);

        // put(K key, V value), putIfAbsent(K key, V value), get(Object key),
        // getOrDefault(Object key, V defaultValue), remove(Object key),
        // remove(Object key, Object value), replace(K key, V value), replace(K key, V
        // oldValue, V newValue), containsKey(Object key), containsValue(Object value),
        // keySet(), values(), entrySet(),
        // clear(), isEmpty(), size(), forEach(BiConsumer<? super K, ? super V> action)

        ConcurrentHashMap<Integer, String> map2 = new ConcurrentHashMap<>(16);

        // thread safe
        for (Map.Entry<Integer, String> entry : map2.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        // Atomic operations

        map2.putIfAbsent(1, "A");

        map2.computeIfAbsent(2, k -> "B");

        map2.compute(1, (k, v) -> v + "X");

        map2.merge(3, "C", String::concat);

        // V compute(K key, BiFunction<? super K, ? super V, ? extends V>
        // remappingFunction), V computeIfAbsent(K key, Function<? super K, ? extends V>
        // mappingFunction),
        // V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V>
        // remappingFunction)

        // learn set
        // Set<String> fruitSet = Set.of("Apple", "Banana", "Orange");
        Set<String> fruitSet = new HashSet<>(Arrays.asList("A", "B"));

        for (String fruit : fruitSet) {
            System.out.println(fruit);
        }

        Iterator<String> setIterator = fruitSet.iterator();
        while (setIterator.hasNext()) {
            String fruit = setIterator.next();
            if (fruit.equals("Banana")) {
                setIterator.remove(); // Safely removes the element
            }
        }

        // return new HashSet<>(Set.of(1, 2, 3));

        // important methods ->add(E e), addAll(Collection<? extends E> c),
        // contains(Object o), remove(Object o), removeAll(Collection<?> c),
        // retainAll(Collection<?> c), clear(), isEmpty(), size(), iterator(),
        // forEach(Consumer<? super E> action), toArray(), toArray(T[] a)

        // learn ArrayList

        ArrayList<String> list = new ArrayList<>();
        // ArrayList<String> list2 = new ArrayList<>(List.of("Udit","Dabsay"));
        // List<String> list3 = new ArrayList<>(Arrays.asList("A", "B"));
        list.add("Udit");
        list.add("Dabsay");

        for (String s : list) {
            System.out.println(s);
        }

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }

        Iterator<String> listIterator = list.iterator();
        while (listIterator.hasNext()) {
            System.out.println(listIterator.next());
        }

        // return new ArrayList<>(List.of("A", "B", "C")); // Java 9+

        // important methods -> add(E e), add(int index, E e), addAll(Collection<?
        // extends E> c), addAll(int index, Collection<? extends E> c), get(int index),
        // set(int index, E element), remove(int index), remove(Object o),
        // contains(Object o), size(), isEmpty(), indexOf(Object o), lastIndexOf(Object
        // o), clear(), toArray(), toArray(T[] a),
        // sort(Comparator<? super E> c), forEach(Consumer<? super E> action)

        // Learn arrays
        int[] a = new int[5]; // default values (0)
        String[] names = { "A", "B", "C" }; // literal
        double[] values = new double[] { 1.1, 2.2, 3.3 };

        // return new int{1,2};

        // important fields -> length
        // important utilities - > Arrays.toString(array), Arrays.deepToString(array),
        // Arrays.sort(array), Arrays.fill(array, value),
        // Arrays.equals(array1, array2), Arrays.deepEquals(array1, array2),
        // Arrays.copyOf(array, newLength), Arrays.copyOfRange(array, from, to),
        // Arrays.binarySearch(array, key), Arrays.stream(array), Arrays.asList(array)

        // learn strings

        String s1 = new String("Hello");
        String s2 = new String("Hello");

        System.out.println(s1 == s2); // false, different objects
        System.out.println(s1.equals(s2)); // true, same content

        String s3 = "Hello";
        String s4 = "Hello";

        System.out.println(s3 == s4); // true, same literal in string pool
        System.out.println(s3.equals(s4)); // true, same content

        // return "Hello World";
        // or return new String[]{"Hello", "World"};

        // important methods -> length(), charAt(int), substring(int),
        // substring(int,int), indexOf(String), lastIndexOf(String),
        // contains(CharSequence), startsWith(String), endsWith(String), equals(Object),
        // equalsIgnoreCase(String), compareTo(String),
        // isEmpty(), toLowerCase(), toUpperCase(), trim(), replace(char,char),
        // replace(CharSequence,CharSequence),
        // replaceAll(String,String), split(String), concat(String), toCharArray(),
        // valueOf(Object)

        // str = UditDabsay

        // str.substr(0,4) = Udit;
        // str.substr(4) = Dabsay

        // learn linkedlist

        LinkedList<String> linkedList = new LinkedList<>();

        for (String s : linkedList) {
            System.out.println(s);
        }

        Iterator<String> it = linkedList.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }

        // add(E e), add(int index, E e), addAll(Collection<? extends E> c),
        // get(int index), set(int index, E element),
        // remove(int index), remove(Object o),
        // contains(Object o), size(), isEmpty(), clear()

        // learn StringBuilder
        // learn StringBuffer

        StringBuilder sb1 = new StringBuilder(); // empty
        StringBuilder sb2 = new StringBuilder("hello"); // with initial text
        StringBuilder sb3 = new StringBuilder(50); // with capacity

        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer("hello");
        StringBuffer buf3 = new StringBuffer(50);

        for (int i = 0; i < sb1.length(); i++) {
            char ch = sb1.charAt(i);
            System.out.println(ch);
        }

        for (char ch : sb1.toString().toCharArray()) {
            System.out.println(ch);
        }

        // append(value), insert(index,value), delete(startIndex,endIndex),
        // deleteCharAt(index), replace(startIndex,endIndex,newString), reverse(),
        // charAt(index), setCharAt(index,newChar), substring(startIndex),
        // substring(startIndex,endIndex),
        // indexOf(string), lastIndexOf(string), length(), capacity(),
        // ensureCapacity(minCapacity), trimToSize(), toString()

        // learn stack

        Stack<Integer> stack = new Stack<>();

        // Stack is synchronized → slower
        // Consider Deque instead for real usage
        Deque<Integer> stack2 = new ArrayDeque<>();
        stack.push(10); // add
        stack.push(20);
        stack.push(30);

        // LIFO order
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

        // FIFO order
        for (int i = 0; i < stack.size(); i++) {
            System.out.println(stack.get(i));
        }

        // boolean empty(), E push(E item), E pop(), E peek(), int search(Object o),
        // int size(), boolean isEmpty(), boolean contains(Object o),
        // E get(int index), Iterator<E> iterator()

        // learn queue
        Queue<Integer> q = new LinkedList<>();
        Queue<Integer> q1 = new ArrayDeque<>();
        Queue<Integer> q2 = new PriorityQueue<>();

        // FIFO order
        while (!q.isEmpty()) {
            System.out.println(q.poll());
        }

        // incorrect order
        Iterator<Integer> it4 = q.iterator();
        while (it4.hasNext()) {
            System.out.println(it4.next());
        }

    }

    //use offer to add element to rear of the queue
    // use poll to remove element from the front of the queue

    // boolean add(E e), boolean offer(E e), E remove(), E poll(), E element(),
    // E peek(), int size(), boolean isEmpty(), boolean contains(Object o),
    // Iterator<E> iterator()
}
