import java.util.*;

public class LRULinkedHashMap {


    LinkedHashMap<Integer, Integer> linkedList = new LinkedHashMap<>();




    public void printCache() {
        System.out.println("Usage order (MRU → LRU): " + linkedList);
        for (int key : linkedList.keySet()) {
            System.out.println(key + " : " + linkedList.get(key));
        }
    }

    

      public static void main(String[] args){
        W2_T2_P1 cache = new W2_T2_P1(5);
        
        cache.add(1, 100);
        cache.add(2, 200);
        cache.printCache();

        cache.access(1);
        cache.printCache();

        cache.add(3, 300);
        cache.printCache();
        cache.add(5, 500);

        cache.add(7, 700);
        cache.printCache();
        cache.add(9, 900);
        cache.add(8, 800);

        cache.access(7);
        cache.printCache();

        cache.access(3);
        cache.printCache();

        
        
    }
    
    
}
