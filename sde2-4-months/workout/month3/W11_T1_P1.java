import java.util.*;

public class W11_T1_P1 {

    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());

        
        List<Integer> list = List.of(12,13,156,8,4,17,2,0);
        
        int k = 4;

        for(var item:list){
            pq.offer(item);
            //Remove n-k larger elements
            if(pq.size() > k){
                pq.poll();
            }
        }
        System.out.println(pq.peek());

    }
}
