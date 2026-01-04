import java.util.*;



public class MergeArrayList {

    static ArrayList<Integer> merge(List<Integer> list1, List<Integer> list2){
        Set<Integer> uniqueElements = new HashSet<>();
        uniqueElements.addAll(list1);
        uniqueElements.addAll(list2);

        return new ArrayList<>(uniqueElements);
    }
    


    public static void main(String[] args){
        List<Integer> listA = new ArrayList<>(List.of(1, 2, 3, 4));
        List<Integer> listB = new ArrayList<>(List.of(3, 4, 5, 6));
        System.out.println(merge(listA,listB));

    }
}
