import java.util.*;


 class LRUCache<K, V> extends LinkedHashMap<K, V> {
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
}

public class W2_T2_P1 {
    Map<Integer, Integer> map = new HashMap<>();
    LinkedList<Integer> linkedList = new LinkedList<>();
    int capacity ;

    public W2_T2_P1(int capacity){
        this.capacity = capacity;
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }

        linkedList.remove((Integer)key);
        linkedList.addFirst(key);

        return map.get(key);
    }

    public void put(int key, int value){

        if(map.containsKey(key)){
            map.put(key,value);
            linkedList.remove((Integer)key);
            linkedList.addFirst(key);
        }
        else{
            if(map.size()==capacity){
                int val = linkedList.removeLast();
                map.remove(val);
                
            }

            map.put(key,value);
            
            linkedList.addFirst(key);
        }
        
        
    }


    public static void printCache(W2_T2_P1 cache) {
        System.out.println("Usage order (MRU → LRU): ");
        for (Integer k : cache.linkedList) {
            System.out.println(k + " : " + cache.map.get(k));
        }
    }

    public static void printCache(LRUCache<Integer, Integer> cache) {
        System.out.println("Usage order (LRU → MRU):");
        for (Map.Entry<Integer, Integer> entry : cache.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }


    public static void main(String[] args){
        //  LRUCache<Integer, Integer> cache = new LRUCache<>(5);
        W2_T2_P1 cache = new W2_T2_P1(5);
        
        cache.put(1, 100);
        cache.put(2, 200);
        printCache(cache);

        cache.get(1);
        printCache(cache);

        cache.put(3, 300);
        printCache(cache);

        cache.put(5, 500);
        cache.put(7, 700);
        printCache(cache);

        cache.put(9, 900);
        cache.put(8, 800);

        cache.get(7);
        printCache(cache);

        cache.get(3);
        printCache(cache);   
        
        
    }
    
}
