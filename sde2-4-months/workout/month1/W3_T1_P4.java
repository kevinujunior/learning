import java.util.*;
import java.util.stream.Collectors;

public class W3_T1_P4 {

    public static int[] removeElement(int[] nums, int toRemove){
        List<Integer> array = Arrays.stream(nums)   // IntStream
                           .boxed()        // convert int -> Integer
                           .collect(Collectors.toList());

        array.remove((Integer) toRemove);

        return array.stream().mapToInt(Integer::intValue).toArray();
    }


    public static void main(String[] args){

        int[] nums = {1,2,3,4,5,6,7,8};
        int toRemove = 5;

        System.out.println(Arrays.toString(nums));

        System.out.println(Arrays.toString(removeElement(nums,toRemove)));

    }
    
}   
