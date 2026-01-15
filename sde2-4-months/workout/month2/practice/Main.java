// Generic class with two type parameters: K (Key) and V (Value)
class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void display() {
        System.out.println("Key: " + key + " (Type: " + key.getClass().getSimpleName() + ")");
        System.out.println("Value: " + value + " (Type: " + value.getClass().getSimpleName() + ")");
    }
}

public class Main {
    // Generic method with two independent type parameters
    public static <T, U> void printInfo(T item1, U item2) {
        System.out.println("Item 1: " + item1 + ", Item 2: " + item2);
    }

    public static void main(String[] args) {
        // Instantiating with String and Integer
        Pair<String, Integer> pair1 = new Pair<>("Age", 25);
        pair1.display();

        // Instantiating with Double and Boolean
        Pair<Double, Boolean> pair2 = new Pair<>(98.6, true);
        pair2.display();

        // Calling the generic method
        printInfo("Version", 2.0); // T is String, U is Double
    }
}